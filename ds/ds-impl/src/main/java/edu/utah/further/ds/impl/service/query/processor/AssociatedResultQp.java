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
package edu.utah.further.ds.impl.service.query.processor;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.chain.AbstractDelegatingUtilityProcessor;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.ds.api.service.query.logic.AssociatedResultAttacher;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty;
import edu.utah.further.mdr.api.to.asset.AttributeTranslationResultTo;
import edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest;

/**
 * Query processor for attaching the results of a previous query to the criteria of a
 * current query
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
 * @version Mar 22, 2012
 */
public final class AssociatedResultQp extends
		AbstractDelegatingUtilityProcessor<AssociatedResultAttacher>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AssociatedResultQp.class);

	/**
	 * Asset service to get the translated patient id property
	 */
	private AssetServiceRest assetService;

	/**
	 * A known list of data types for attribute translation
	 */
	private static final List<String> knownDataTypes = newList();

	// ========================= IMPL:RequestProcessor =================================

	/**
	 * Default constructor
	 */
	public AssociatedResultQp()
	{
		knownDataTypes.add("java.lang.String");
		knownDataTypes.add("java.lang.Integer");
		knownDataTypes.add("java.lang.Long");
		knownDataTypes.add("java.math.BigDecimal");
		knownDataTypes.add("java.math.BigInteger");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.
	 * api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);
		if (queryContext.getAssociatedResult() != null)
		{
			// We default to 'associated by patients' - compositeId is equiv of patient id
			// in FURTHeR logical model - could be exposed as parameters
			final AttributeTranslationResultTo attributeResult = assetService
					.getUniqueAttributeTranslation("compositeId", "FURTHeR",
							queryContext.getDataSourceId());

			final String translatedAttribute = attributeResult.getTranslatedAttribute();
			final Map<String, String> attributeProperties = attributeResult
					.getAttributeProperties();
			final String translatedTypeFqcn = attributeProperties
					.get(AssetAssociationProperty.JAVA_DATA_TYPE);

			if (!isKnownDataType(translatedTypeFqcn))
			{
				throw new ApplicationException("Translated type " + translatedTypeFqcn
						+ " is not a supported type. Only " + knownDataTypes
						+ " are supported");
			}

			final Class<?> translatedType = loadClass(translatedTypeFqcn);

			final SearchQuery newSearchQuery = getDelegate().attachAssociatedResult(
					queryContext, queryContext.getDataSourceId(), translatedAttribute,
					translatedType);
			queryContext.setQuery(newSearchQuery);
			request.setAttribute(QUERY_CONTEXT, queryContext);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("QueryContext did not contain an associated result, "
						+ "skipping associated results processing");
			}
		}

		return false;
	}

	/**
	 * Set a new value for the assetService property.
	 * 
	 * @param assetService
	 *            the assetService to set
	 */
	public void setAssetService(final AssetServiceRest assetService)
	{
		this.assetService = assetService;
	}

	// ========================= PRIVATE =================================

	/**
	 * Ensures that the data type is part of a white list of known data types.
	 * 
	 * @param clazz
	 * @return
	 */
	private boolean isKnownDataType(final String clazz)
	{
		if (!knownDataTypes.contains(clazz))
		{
			return false;
		}
		return true;
	}

	/**
	 * Loads a class from a FQ-String - OSGi safe.
	 * 
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Class<?> loadClass(final String clazz)
	{
		try
		{
			return Thread.currentThread().getContextClassLoader().loadClass(clazz);
		}
		catch (final ClassNotFoundException e)
		{
			throw new ApplicationException("Unable to load class of type " + clazz
					+ " for attribute translation.");
		}
	}

}
