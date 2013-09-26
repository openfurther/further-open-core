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
package edu.utah.further.fqe.ds.model.further.service.results;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.service.results.ResultDataService;
import edu.utah.further.fqe.ds.api.service.results.ResultType;

/**
 * Result services which can retrieve or access row level data.
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
 * @version Sep 24, 2013
 */
@Service("resultDataService")
public class ResultDataServiceImpl implements ResultDataService
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.service.results.ResultDataService#join(java.util.List,
	 * java.lang.String, edu.utah.further.fqe.ds.api.service.results.ResultType, int)
	 */
	@Override
	public Map<String, Long> join(final List<String> queryIds, final String attributeName,
			final ResultType resultType, final int intersectionIndex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.service.results.ResultDataService#getQueryResults(java
	 * .util.List)
	 */
	@Override
	public <T> List<T> getQueryResults(final List<String> queryIds)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.service.results.ResultDataService#getQueryResults(edu
	 * .utah.further.core.query.domain.SearchQuery)
	 */
	@Override
	public <T> List<T> getQueryResults(final SearchQuery query)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
