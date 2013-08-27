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

import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_RESULT;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.chain.AbstractDelegatingUtilityProcessor;
import edu.utah.further.ds.api.service.query.logic.ResultTranslator;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * Result Translator Query Processor.
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
 * @version Apr 21, 2010
 */
public class ResultTranslatorQp extends AbstractDelegatingUtilityProcessor<ResultTranslator>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ResultTranslatorQp.class);

	// ========================= FIELDS =================================

	/**
	 * The name of the attribute where to put the marshalled result. Not an AttributeName
	 * in order to be flexible but can wire in an AttributeName if needed.
	 */
	private Labeled resultAttr;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate dependencies .
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		Validate.notNull(resultAttr, "A result attribute must be set");
	}

	// ========================= Impl: Requestprocessor ====================

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
		// Read inputs from chain request
		final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);
		final Object result = request.getAttribute(QUERY_RESULT);
		
		final Map<String, Object> attributes = request.getAttributes();
		
		// Translate result set
		final Object translatedResult = getDelegate().translate(result,
				attributes);

		// Prepare new query context
		
		request.setAttributes(attributes);
		
		final QueryContext newQueryContext = QueryContextToImpl.newCopy(queryContext);
		newQueryContext.getResultContext().setResult(translatedResult);

		// Save results in the chain request
		request.setAttribute(QUERY_CONTEXT, newQueryContext);
		request.setAttribute(resultAttr, newQueryContext.getResultContext().getResult());
		return false;
	}

	// ========================= GET/SET =================================

	/**
	 * Set a new value for the resultAttr property.
	 *
	 * @param resultAttr
	 *            the resultAttr to set
	 */
	public void setResultAttr(final Labeled resultAttr)
	{
		this.resultAttr = resultAttr;
	}
}
