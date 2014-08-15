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

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.ds.further.model.impl.domain.Person;
import edu.utah.further.fqe.ds.api.service.results.ResultDataService;

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
	/**
	 * Data sessionfactory
	 */
	@Autowired
	private SessionFactory sessionFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.service.results.ResultDataService#getRootResultClass
	 * (java.util.List)
	 */
	@Override
	public Class<?> getRootResultClass(final List<String> queryIds)
	{
		// in future versions, we'll query the database to figure out the root class based
		// on the query identifiers but for now, we only support root results as Persons.
		return Person.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.service.results.ResultDataService#getQueryResults(java
	 * .lang.String, java.util.List)
	 */
	@Override
	public <T> T getQueryResults(final String hql,
			final List<Object> orderedParameterValues)
	{
		final Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < orderedParameterValues.size(); i++)
		{
			query.setParameter(i, orderedParameterValues.get(i));
		}
		return (T) query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.service.results.ResultDataService#getQueryResults(java
	 * .lang.String, java.util.List)
	 */
	@Override
	public <T> T getQueryResultsInList(final String hql, final String parameterName,
			final List<Object> orderedParameterValues)
	{
		final Query query = sessionFactory.getCurrentSession().getNamedQuery(hql);
		query.setParameterList(parameterName, orderedParameterValues);
		return (T) query.list();
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
