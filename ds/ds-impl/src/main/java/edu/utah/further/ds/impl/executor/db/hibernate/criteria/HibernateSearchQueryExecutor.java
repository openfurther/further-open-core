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
package edu.utah.further.ds.impl.executor.db.hibernate.criteria;

import static org.apache.commons.lang.Validate.notNull;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;
import edu.utah.further.core.data.hibernate.adapter.CriteriaType;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.data.hibernate.query.QueryBuilderHibernateImpl;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.ds.impl.executor.db.hibernate.HibernateExecReq;

/**
 * An {@link RequestProcessor} which takes as input a {@link SearchQuery} and transforms
 * this {@link SearchQuery} into a Hibernate equivalent {@link GenericCriteria}.
 * 
 * This executor expects the following to be set (see {@link HibernateExecReq})
 * <ul>
 * <li>SearchQuery</li>
 * <li>Root Entity Class</li>
 * </ul>
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
 * @version Sep 30, 2009
 * 
 * @see QueryBuilderHibernateImpl
 */
@Service("hibernateSearchQueryExecutor")
public class HibernateSearchQueryExecutor extends AbstractNonDelegatingUtilityProcessor
{
	// ========================= IMPLEMENTATION: RequestHandler ==============

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.chain.AbstractRequestHandler#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		final HibernateExecReq executionReq = new HibernateExecReq(request);

		final SearchQuery searchQuery = executionReq.getSearchQuery();
		notNull(searchQuery, "Expected SearchQuery");

		final Class<? extends PersistentEntity<?>> persistentClass = executionReq
				.getRootEntity();
		notNull(persistentClass, "Expected Root Entity");

		final SessionFactory sessionFactory = executionReq.getSessionFactory();
		notNull(sessionFactory, "Expected Datasource SessionFactory");

		final GenericCriteria hibernateCriteria = QueryBuilderHibernateImpl.convert(
				CriteriaType.CRITERIA, persistentClass.getPackage().getName(),
				sessionFactory, searchQuery);
		executionReq.setResult(hibernateCriteria);
		executionReq.setStatus("Transformed query for execution");

		return false;
	}
}
