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
package edu.utah.further.ds.fqe.test.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;

/**
 * Strictly a testing class for performing FQE functions needed to test data source
 * functions.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version Jan 13, 2012
 */
@Service("fqeDataSourceUtil")
public final class FQEDataSourceTestUtil
{
	/**
	 * QueryContextService for queueing queries into the FQE
	 */
	@Autowired
	@Qualifier("queryContextService")
	private QueryContextService qcService;

	/**
	 * Unmarshals and queues the federated query context as well as creating and queue
	 * 
	 * @param query
	 * @param xmlService
	 * @return
	 */
	public QueryContext unmarshalAndQueueQueryContext(final Resource resource,
			final XmlService xmlService, final String username)
	{
		QueryContext parent = null;
		try
		{
			parent = xmlService
					.unmarshal(resource.getInputStream(), QueryContextTo.class);
		}
		catch (final Exception e)
		{
			throw new ApplicationException(
					"Failed to unmarshal query context from resource " + resource, e);
		}

		final QueryContextTo parentWithExecId = QueryContextToImpl
				.newCopyWithGeneratedExecutionId(parent);
		
		parentWithExecId.setUserId(username);

		// Generate, queue and return the parent
		return QueryContextToImpl.newCopy(qcService.queue(parentWithExecId));
	}

	/**
	 * Queues a new parent {@link QueryContext} with a supplied {@link SearchQuery}.
	 * 
	 * @param searchQuery
	 * @return
	 */
	public QueryContext queueNewParent(final SearchQuery searchQuery, final String userId)
	{
		final QueryContextTo queryContext = QueryContextToImpl
				.newInstanceWithExecutionId();
		queryContext.setQuery(searchQuery);
		queryContext.setUserId(userId);
		return QueryContextToImpl.newCopy(qcService.queue(queryContext));
	}

	/**
	 * Deletes the querycontext after testing
	 */
	public void cleanUpQueryContext(final QueryContextTo child)
	{
		final QueryContext parent = qcService.findById(child.getParentId());
		qcService.delete(parent);
	}
}
