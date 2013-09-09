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
package edu.utah.further.fqe.ds.api.util;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayInputStream;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryTo;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.ds.api.to.ResultContextToImpl;

/**
 * A stateless test utility class related to {@link QueryContext}s that does not require
 * JUnit.
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
 * @version Nov 3, 2010
 */
@Utility
public final class FqeDsQueryContextUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(FqeDsQueryContextUtil.class);

	/**
	 * Fictitious number of records to set on a {@link QueryContext} test object.
	 */
	public static long NUM_RECORDS = 50L;

	/**
	 * For result view testing.
	 */
	public static final long NUM_RESULTS_IN_VIEW = 456l;

	// ========================= DEPENDENCIES ==============================

	// ========================= METHODS ===================================

	/**
	 * Print a query context to the console.
	 * 
	 * @param queryContext
	 *            object to print
	 */
	public static void print(final QueryContext queryContext)
	{
		if (log.isDebugEnabled())
		{
			log.debug("QC: " + queryContext);
		}
	}

	/**
	 * @return
	 */
	public static ResultContext newResultContextTo()
	{
		return newResultContextTo(NUM_RESULTS_IN_VIEW);
	}

	/**
	 * @return
	 */
	public static ResultContext newResultContextTo(final long numRecords)
	{
		final ResultContext resultContext = new ResultContextToImpl();
		resultContext.setNumRecords(numRecords);
		return resultContext;
	}

	/**
	 * @return
	 */
	public static void addResultViewTo(final QueryContext queryContext,
			final ResultType type, final Integer intersectionIndex, final long numRecords)
	{
		queryContext.addResultView(type, intersectionIndex,
				newResultContextTo(numRecords));
	}

	/**
	 * @return
	 */
	public static QueryContextTo newQueryContextToInstance()
	{
		final QueryContextTo queryContext = QueryContextToImpl
				.newInstanceWithExecutionId();
		// Set some data fields
		setStaleDateTimeInPast(queryContext);
		queryContext.setNumRecords(NUM_RECORDS);
		return queryContext;
	}

	/**
	 * @param queryContext
	 */
	public static void setStaleDateTimeInPast(final QueryContextTo queryContext)
	{
		queryContext.setStaleDateTime(getStaleDateTimeInPast());
	}

	/**
	 * @return
	 */
	public static Date getStaleDateTimeInPast()
	{
		// Fix system time at some early point in the past
		TimeService.fixSystemTime(10000);
		final Date staleDateTime = TimeService.getDate();

		// Reset time source so that all subsequent dates in the code are "normal"
		TimeService.reset();
		return staleDateTime;
	}

	/**
	 * Marshal a {@link SearchQuery} object to a String.
	 * 
	 * @param xmlService
	 * @param searchQuery
	 * @return
	 */
	public static String marshalSearchQuery(final XmlService xmlService,
			final SearchQuery searchQuery)
	{
		try
		{
			return xmlService.marshal(searchQuery);
		}
		catch (final JAXBException e)
		{
			throw new ApplicationException("Unable to marshal SearchQuery", e);
		}
	}

	/**
	 * Unmarshal a search query XML document into an object graph.
	 * 
	 * @param queryXml
	 *            search query XML
	 * @return {@link SearchQuery} instance
	 */
	public static SearchQuery unmarshalSearchQuery(final XmlService xmlService,
			final String queryXml)
	{
		try
		{
			return xmlService.unmarshal(new ByteArrayInputStream(queryXml.getBytes()),
					xmlService
							.options()
							.addClass(SearchQueryTo.class)
							.buildContext()
							.setRootNamespaceUris(CollectionUtil.<String> newSet()));
		}
		catch (final JAXBException e)
		{
			throw new ApplicationException("Unable to unmarshal SearchQuery", e);
		}
	}

	// ========================= PRIVATE METHODS ===========================
}
