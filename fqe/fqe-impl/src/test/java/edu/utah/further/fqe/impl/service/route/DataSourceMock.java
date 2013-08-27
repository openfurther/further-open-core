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
package edu.utah.further.fqe.impl.service.route;

import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.InOnly;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.impl.domain.QueryContextEntity;

/**
 * A simple mock data source for response-reply unit tests.
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
 * @version Feb 22, 2010
 */
public final class DataSourceMock implements Named
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(DataSourceMock.class);

	// ========================= FIELDS ====================================

	/**
	 * Name of this data source.
	 */
	private final String name;

	/**
	 * Context service
	 */
	@Autowired
	private QueryContextService contextService;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param name
	 */
	public DataSourceMock(final String name)
	{
		super();
		this.name = name;
	}

	// ========================= IMPLEMENTATION: Named =====================

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	// ========================= METHODS ===================================

	/**
	 * Process a string body.
	 * 
	 * @param inputString
	 *            input message body
	 * @return processed input string. Contains this data source's details for easy test
	 *         assertions
	 */
	@Handler
	@InOnly
	public String process(final String inputString)
	{
		if (log.isTraceEnabled())
		{
			log.trace(getDataSourcePrefix() + "process() " + inputString);
		}
		return name + ": " + inputString;
	}

	/**
	 * @param inputString
	 * @return processed input string
	 */
	@Handler
	public QueryContext processQueryContext(final @Body QueryContext parentContext)
	{
		Validate.notNull(parentContext);
		if (log.isDebugEnabled())
		{
			log.debug(getDataSourcePrefix() + "processQueryContext() " + parentContext);
			log.debug("QC Data Source ID: " + parentContext.getDataSourceId());
			log.debug("Search criteria: " + parentContext.getQuery());
		}

		// This section mocks a data source's creation of a new child context, queuing it
		// and setting its parent.
		QueryContextEntity childContext = QueryContextEntity
				.newInstanceWithExecutionId();
		childContext.setDataSourceId(name);
		childContext.setParent(QueryContextEntity.newCopy(parentContext));
		// Assuming the search query's first criterion is of type [EQ,
		// desiredNumRecordsBackFromThisDataSource, value]. Set value (parameter #2
		// (0-based index) on the parameter list)as our output records.
		//
		// If there are two criteria, using the second criterion's value instead.
		final Long numRecords = getNumRecordsFromQuery(parentContext);

		childContext.setNumRecords(numRecords.longValue());
		childContext = (QueryContextEntity) contextService.queue(childContext);
		childContext.start();
		childContext.finish();

		final long sleepValue = getSleepValue();
		if (log.isDebugEnabled())
		{
			log.debug("Sleeping for " + sleepValue + " miliseconds");
		}
		IoUtil.sleep(sleepValue);

		return QueryContextToImpl.newCopy(childContext);
	}

	@SuppressWarnings("boxing")
	private Long getNumRecordsFromQuery(final QueryContext parentContext)
	{
		try
		{
			final List<SearchCriterion> criteria = parentContext
					.getQuery()
					.getRootCriterion()
					.getCriteria();
			final Long numRecords = (criteria.size() == 2) ? (Long) criteria
					.get(1)
					.getParameter(2) : (Long) criteria.get(0).getParameter(2);
			return numRecords;
		}
		catch (final Exception e)
		{
			// Signifies that SearchQueryDeEvaluator might have not operated
			// correctly
			return -1l;
		}
	}

	/**
	 * @return
	 */
	private long getSleepValue()
	{
		// Make DS4 slower than the DS3 but have both within the range of 1000l that
		// testPlan() waits for to collect results
		return "DS3".equals(name) ? 0l : 200l;
	}

	/**
	 * Return a {@link DsMetaData} response.
	 * 
	 * @param inputString
	 *            input message body
	 * @return meta data XML entity instance
	 */
	@Handler
	@InOnly
	public DsMetaData getDsMetaData(final String inputString)
	{
		if (log.isDebugEnabled())
		{
			log.debug(getDataSourcePrefix() + "getDsMetaData()");
			// " inputString "
			// + inputString);
		}

		// For test assertion simplicity, the name field below does not depend on
		// the input string. Only the description field does.
		return new DsMetaData(name, name + ": " + inputString);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private String getDataSourcePrefix()
	{
		return "Data source " + name + ": ";
	}
}
