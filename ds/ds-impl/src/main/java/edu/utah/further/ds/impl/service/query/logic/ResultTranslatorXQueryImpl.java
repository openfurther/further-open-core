/**
 * Copyright (C) [2013] [The FURTHeR Project]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.utah.further.ds.impl.service.query.logic;

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.constant.Constants.Scope.PROTOTYPE;
import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static edu.utah.further.core.chain.RequestHandlerBuilder.chainBuilder;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static edu.utah.further.ds.api.util.AttributeName.RESULT_MARSHAL_PKGS;
import static edu.utah.further.ds.api.util.AttributeName.RESULT_SCHEMA;
import static edu.utah.further.ds.api.util.AttributeName.RESULT_TRANSLATION;
import static edu.utah.further.ds.impl.util.DsImplUtil.getMdrResourceInputStream;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.chain.AttributeContainerImpl;
import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.chain.AbstractRequestProcessor;
import edu.utah.further.core.chain.ChainRequestImpl;
import edu.utah.further.core.chain.MarshallRequestProcessor;
import edu.utah.further.core.chain.RequestHandlerBuilder;
import edu.utah.further.core.util.io.LoggingUtil;
import edu.utah.further.core.xml.stax.XmlStreamPrinter;
import edu.utah.further.core.xml.xquery.XQueryService;
import edu.utah.further.ds.api.results.ResultList;
import edu.utah.further.ds.api.service.query.logic.ResultTranslator;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.util.FqeNames;
import edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest;

/**
 * A reusable XQuery Result Translator. It excepts that the proper translation artifact be
 * set during query initialization under the attribute
 * {@link AttributeName#RESULT_TRANSLATION}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version May 3, 2010
 */
@Service("resultTranslatorXQuery")
@Scope(PROTOTYPE)
public final class ResultTranslatorXQueryImpl implements ResultTranslator
{
	// ========================= CONSTANTS =============================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ResultTranslatorXQueryImpl.class);

	// ========================= DEPENDENCIES ===========================

	/**
	 * XQuery Service
	 */
	@Autowired
	@Qualifier("xqueryService")
	private XQueryService xqueryService;

	/**
	 * A marshalling processor.
	 */
	@Autowired
	@Qualifier("marshallRequestProcessor")
	private MarshallRequestProcessor marshallRp;

	/**
	 * An unmarshalling processor.
	 */
	@Autowired
	@Qualifier("unmarshallRequestProcessor")
	private MarshallRequestProcessor unmarshallRp;

	/**
	 * MDR web service client.
	 */
	@Resource(name="mdrAssetServiceRestClient")
	private AssetServiceRest assetServiceRest;

	// ========================= IMPL: ResultTranslator ====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.ResultTranslator#translate(edu.utah
	 * .further.fqe.ds.api.domain.ResultContext, java.util.Map)
	 */
	@Override
	public Object translate(final Object result, final Map<String, Object> attributes)
	{
		Validate.notNull(result, "Result object returned from execution cannot be null.");

		// Create a chain request for our result translation sub-chain
		final ChainRequest subRequest = new ChainRequestImpl(new AttributeContainerImpl(
				attributes));

		// Set the validation schema in the request for the marshalling processor to
		// perform W3C Schema validation
		if (subRequest.getAttribute(RESULT_SCHEMA) != null)
		{
			setValidationSchema(subRequest);
		}

		// Build & invoke the chain
		final RequestHandler requestHandler = buildSubChain(result, subRequest);
			log.debug("CALLING HANDLE ON: " + subRequest);
		requestHandler.handle(subRequest);

		attributes.clear();
		attributes.putAll(subRequest.getAttributes());

		// Return the result of the conversion
			log.debug("CALLED HANDLE TO GET: " + subRequest.getAttribute(unmarshallRp.getResultAttr()));
		return subRequest.getAttribute(unmarshallRp.getResultAttr());
	}

	// ========================= PRIVATE METHODS =======================

	/**
	 * Private helper method to build the sub chain of processing. The chain is built to
	 * execute the following process: marshal -> execute -> unmarshal
	 * 
	 * @param result
	 *            result set
	 * @return sub-chain
	 */
	private final RequestHandler buildSubChain(final Object result,
			final ChainRequest request)
	{
		// Build the sub-chain
		final RequestHandlerBuilder subChainBuilder = chainBuilder();

		if (instanceOf(result, String.class))
		{
			log.debug("Skip marshalling branch ");
			// Skip marshalling, it's already marshalled
			request.setAttribute(marshallRp.getResultAttr(), result);
		}
		else if (instanceOf(result, List.class))
		{
			// Marshal the list of entities
			subChainBuilder.addProcessor(new TransferObjectProcessorImpl(result));
			subChainBuilder.addProcessor(marshallRp);
			log.debug("Marshalling branch ");
		}
		else
		{
			throw new ApplicationException(
					"XQuery Result Translator expects List or String but received "
							+ result.getClass());
		}
		subChainBuilder.addProcessor(new XQueryExecutionProcessorImpl());
		subChainBuilder.addProcessor(unmarshallRp);
			log.debug("Rest of subchain branch ");

		request.setAttribute(unmarshallRp.getMarshalPkgsAttr(),
				request.getAttribute(AttributeName.RESULT_UNMARSHAL_PKGS));

		final RequestHandler subChain = subChainBuilder.build();

		if (log.isDebugEnabled())
		{
			log.debug("Processing sub-chain " + subChain);
		}
		return subChain;
	}

	/**
	 * Private helper method to retrieve and set Schema validation for marshalling
	 * validation.
	 * 
	 * @param request
	 */
	@SuppressWarnings("resource")
	// Resource closed by caller
	private final void setValidationSchema(final ChainRequest request)
	{
		// Get the path
		final String schemaMdrPath = request.getAttribute(RESULT_SCHEMA);
		final InputStream schemaInputStream = getMdrResourceInputStream(assetServiceRest,
				schemaMdrPath);
		// Set the schema on the marshaller
		request.setAttribute(marshallRp.getSchemaAttr(), new StreamSource(
				schemaInputStream));
	}

	/**
	 * A processor that converts entities to transfer objects.
	 */
	private class TransferObjectProcessorImpl extends AbstractRequestProcessor
	{
		/**
		 * The resultcontext
		 */
		private final Object result;

		/**
		 * @param result
		 */
		private TransferObjectProcessorImpl(final Object result)
		{
			super();
			this.result = result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core
		 * .api.chain.ChainRequest)
		 */
		@Override
		public boolean process(final ChainRequest request)
		{
			final List<?> entityList = (List<?>) result;
log.trace("entityList:: " + entityList.getClass().getName() + ":: " + entityList.size());
			LoggingUtil.printEntityList(log, entityList);

			// Ensure that all of the entities/dtos are included since we'll be wrapping
			// this in a ResultList
			request.setAttribute(marshallRp.getMarshalPkgsAttr(),
					request.getAttribute(RESULT_MARSHAL_PKGS));

			// Set the marshalling processor input attribute to a ResultList to marshall
			// to XML
			request.setAttribute(marshallRp.getSourceAttr(), new ResultList(entityList));

			return false;
		}
	}

	/**
	 * A processor that executes XQuery Translations against the results.
	 */
	private class XQueryExecutionProcessorImpl extends AbstractRequestProcessor
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core
		 * .api.chain.ChainRequest)
		 */
		@Override
		public boolean process(final ChainRequest request)
		{
			// TODO: Cache translation artifact!!
			log.debug("TODO: Cache translation artifact!!");
			// Get the translation artifact from the request
			final String mdrPath = request.getAttribute(RESULT_TRANSLATION);
			log.debug("RESULT_TRANSLATION: " + mdrPath);

			String resultStr = (String) request.getAttribute(marshallRp.getResultAttr());
			if(resultStr == null)
			{
				resultStr = "";
			}

			resultStr = resultStr.replaceAll("FURTHeRQueryResults", "ResultList");
			log.debug("ResultStr !!! " + resultStr);

			try ( // Get the result of marshalling
				ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(resultStr.getBytes());

				InputStream xQueryInputStream = getMdrResourceInputStream(
							assetServiceRest, mdrPath);)
			{

				if (log.isTraceEnabled() && xmlInputStream.markSupported())
				{
					xmlInputStream.mark(0);
					log.debug("XQuery Translation input: " + NEW_LINE_STRING
							+ XmlStreamPrinter.printToString(xmlInputStream));
					xmlInputStream.reset();
				}

				final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);

				// Set XQuery parameter values
				final Map<String, String> parameters = newMap();
				parameters.put(FqeNames.QUERY_ID, queryContext.getExecutionId());
				parameters.put(FqeNames.LOCAL_NAMESPACE_ID, StringUtil
						.getNullSafeToString(queryContext.getTargetNamespaceId()));

					log.debug("XQuery Translation parameters: " + parameters);
				// Set the source attribute for the unmarshalling processor
				final String xqueryResult = xqueryService.executeIntoString(
						xQueryInputStream, xmlInputStream, parameters);
				if (log.isTraceEnabled())
				{
					log.debug("XQuery Translation output: " + NEW_LINE_STRING
							+ XmlStreamPrinter.printToString(xqueryResult));
				}
				request.setAttribute(unmarshallRp.getSourceAttr(),
						new ByteArrayInputStream(xqueryResult.getBytes()));
			}
			catch (final IOException e)
			{
				// ignore if an exception on close is called
			}

			return false;
		}
	}
}
