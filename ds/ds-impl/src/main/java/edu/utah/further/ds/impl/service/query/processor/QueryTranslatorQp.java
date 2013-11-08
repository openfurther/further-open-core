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
import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.chain.AbstractDelegatingUtilityProcessor;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.ds.api.service.query.logic.QueryTranslator;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * Query translation processor.
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
 * @version Apr 12, 2010
 */
public class QueryTranslatorQp extends
		AbstractDelegatingUtilityProcessor<QueryTranslator>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(QueryTranslatorQp.class);

	// ========================= FIELDS ====================================

	// ========================= Impl: RequestHandler ======================

	@Override
	public boolean process(final ChainRequest request)
	{
		// Read inputs from chain request
		final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);

		Validate.notNull(queryContext.getQuery(), "Cannot translate null query");
		Validate.notNull(queryContext.getTargetNamespaceId(),
				"Target namespace id required for translation");

		try
		{
			// Translate search query
			final SearchQuery translatedQuery = getDelegate().translate(queryContext,
					request.getAttributes());
			// Prepare new query context
			final QueryContext newQueryContext = QueryContextToImpl.newCopy(queryContext);
			newQueryContext.setQuery(translatedQuery);

			// Save results in the chain request
			request.setAttribute(QUERY_CONTEXT, newQueryContext);
			return false;
		}
		catch (final ApplicationException e)
		{
			log.error("****Query translation returned error!****, translation failed: "
					+ e.getMessage());
			return true;
		}
	}

}
