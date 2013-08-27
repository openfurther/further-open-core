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

import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.chain.AbstractDelegatingUtilityProcessor;
import edu.utah.further.ds.api.service.query.logic.Validator;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * Validates the physical result set response obtained from the physical data source
 * during the execution processors. This is slightly different than {@link ValidatorQp},
 * because we redirect failed reponses to an error handling sub-chain instead of before
 * failing.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Sep 16, 2010
 */
public class ResultValidatorQp extends AbstractDelegatingUtilityProcessor<Validator<Object>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ResultValidatorQp.class);

	// ========================= DEPENDENCIES ===========================

	/**
	 * Handles error responses.
	 */
	private Validator<Object> errorHandler;

	// ========================= CONSTRUCTORS ==============================

	// ========================= Impl: RequestProcessor ====================

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.chain.Validator<Object>#process(edu.utah.further.core.
	 * api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Read inputs from chain request
		final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);
		final Object result = request.getAttribute(QUERY_RESULT);

		// Translate result set
		final boolean validResponse = getDelegate().validate(result,
				request.getAttributes());

		// Prepare new query context; save result, whether an error message
		// or a valid result set, under an attribute to be processed by other RPs next
		final QueryContext newQueryContext = QueryContextToImpl.newCopy(queryContext);
		newQueryContext.getResultContext().setResult(result);

		// Save results in the chain request
		request.setAttribute(QUERY_CONTEXT, newQueryContext);
		request.setAttribute(QUERY_RESULT, newQueryContext.getResultContext().getResult());

		if (!validResponse)
		{
			// Since we can't set the next node to the next request *handler* inside a
			// request *processor*, manually invoke a process() method. TODO: improve
			// by replacing our chain framework with camel, which allows such switches
			// much more easily.
			errorHandler.validate(result, request.getAttributes());

			// Note: error handler must throw an exception, or we're an illegal state
			throw new IllegalStateException(
					"Result error handler didn't throw an exception!");
		}
		// Continue the normal processing chain
		return false;
	}

	// ========================= GET/SET =================================

	/**
	 * Set a new value for the errorHandler property.
	 *
	 * @param errorHandler
	 *            the errorHandler to set
	 */
	public void setErrorHandler(final Validator<Object> errorHandler)
	{
		this.errorHandler = errorHandler;
	}
}
