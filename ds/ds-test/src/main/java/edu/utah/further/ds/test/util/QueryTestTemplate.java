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
package edu.utah.further.ds.test.util;

import static edu.utah.further.ds.api.util.StatusType.FAIL;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.ds.fqe.test.util.FQEDataSourceTestUtil;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.QueryState;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;

/**
 * A useful JavaBean that holds the input and output of a federated query test.
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
 * @version Jul 15, 2010
 */
public final class QueryTestTemplate extends
		AbstractTestTemplate<QueryContext, QueryContextTo>
{
	// ========================= DEPENDENCIES ==============================

	/**
	 * Marshalling service.
	 */
	@Autowired
	private XmlService xmlService;

	/**
	 * 
	 * FQE Data Source testing service
	 */
	@Autowired
	private FQEDataSourceTestUtil fqeDataSourceTestUtil;

	// ========================= FIELDS ====================================

	// ========================= IMPL: AbstractTestTemplate ================

	/**
	 * Converts the input resource to a {@link QueryContext} from file, queues the
	 * QueryContext and creates a generated child id.
	 * 
	 * @return data source child query context
	 */
	@Override
	protected QueryContext generateCommand()
	{
		// Simulate FQE queuing parent and child
		return fqeDataSourceTestUtil.unmarshalAndQueueQueryContext(getInput(),
				xmlService, getUsername());
	}

	/**
	 * Assert that the actual count >= expected count set on this object.
	 */
	@Override
	public void assertCommandSucceeded()
	{
		// Ensure the result isn't null
		assertThat("Valid query failed", getResult().getResultContext().getResult(),
				notNullValue());

		// Ensure we aren't in a failed state
		assertThat(getActualState(), is(not(QueryState.FAILED)));
	}

	/**
	 * Assert that the query failed.
	 */
	@Override
	public void assertCommandFailed()
	{
		// Assert proper state
		assertThat(getActualState(), is(QueryState.FAILED));

		// Assert proper latest status message
		final StatusMetaData currentStatus = getActualStatus();
		assertThat(currentStatus.getDataSourceId(), is(getResult().getDataSourceId()));
		// TODO: replace this with property assertions per FUR-974
		assertThat(currentStatus.getStatus(), containsString(FAIL.getName()));
	}

	/**
	 * Assert that the query failed because of a certain processor.
	 * 
	 * @param processorName
	 *            name of processor that generated the query's failure status message
	 */
	@Override
	public void assertCommandFailedInProcessor(final String processorName)
	{
		assertThat(getActualStatus().getStatus(),
				is(processorName + " " + FAIL.getName()));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Returns a count directly from the resultContext in the case of a COUNT_QUERY
	 * 
	 */
	@SuppressWarnings("boxing")
	@Override
	protected long getCountByResult()
	{
		switch (getResult().getQueryType())
		{
			case DATA_QUERY:
			{
				return 0l;
			}
			case COUNT_QUERY:
			{
				return (Long) (getResult()).getResultContext().getResult();
			}
			default:
			{
				return 0l;
			}
		}
	}

	/**
	 * Return the final query state.
	 * 
	 * @return query state at the end of the life cycle
	 * @see edu.utah.further.core.api.state.StateMachine#getState()
	 */
	private QueryState getActualState()
	{
		return getResult().getState();
	}

	/**
	 * Return the final query status message.
	 * 
	 * @return query latest status message
	 * @see edu.utah.further.fqe.ds.api.domain.QueryContext#getCurrentStatus()
	 */
	private final StatusMetaData getActualStatus()
	{
		// if (finalQueryContext.isFailed())
		// {
		// // Since failure propagates from sub-chains to the main chain, multiple
		// // failure messages might have been picked on the way. The first one is
		// // the original failure.
		// for (final StatusMetaData statusMetaData : finalQueryContext.getStatuses())
		// {
		// if (statusMetaData.getStatus().contains(FAIL.getName()))
		// {
		// return statusMetaData;
		// }
		// }
		// throw new IllegalStateException(
		// "Failed query context with no failed status meta data");
		// }
		// else
		// {
		return getResult().getCurrentStatus();
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.ds.test.util.AbstractTestTemplate#cleanUpDataSourceTest()
	 */
	@Override
	protected void cleanUpDataSourceTest()
	{
		fqeDataSourceTestUtil.cleanUpQueryContext(getResult());
	}

}
