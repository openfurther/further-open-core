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
package edu.utah.further.fqe.ds.api.service.results;

import java.util.List;
import java.util.Map;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.query.domain.SearchQuery;

/**
 * Stub implementation used for extension when only some of the methods need to be
 * implemented.
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
public abstract class AbstractResultService implements ResultService
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.math.ResultService#join(java.util.List,
	 * edu.utah.further.core.api.math.ResultType, java.lang.Integer)
	 */
	@Override
	public Long join(final List<String> queryIds, final ResultType resultType,
			final Integer intersectionIndex)
	{
		throw new ApplicationException("This result service does not support joining.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.math.ResultService#join(java.util.List,
	 * java.lang.String, edu.utah.further.core.api.math.ResultType, int)
	 */
	@Override
	public Map<String, Long> join(final List<String> queryIds,
			final String attributeName, final ResultType resultType,
			final int intersectionIndex)
	{
		throw new ApplicationException(
				"This result service does not support joining by an attributeName.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.math.ResultService#getQueryResults(java.util.List)
	 */
	@Override
	public List<Long> getQueryResultIdentifiers(final List<String> queryIds)
	{
		throw new ApplicationException(
				"This result service does not retrieving query identifier results.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.results.ResultService#getQueryResults(java.util.List)
	 */
	@Override
	public <T> List<T> getQueryResults(final List<String> queryIds)
	{
		throw new ApplicationException(
				"This result service does not retrieving query results using query ids.");
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.fqe.ds.api.results.ResultService#getQueryResults(edu.utah.further.core.query.domain.SearchQuery)
	 */
	@Override
	public <T> List<T> getQueryResults(final SearchQuery query)
	{
		throw new ApplicationException(
				"This result service does not retrieving query results using a query.");
	}
	
	

}
