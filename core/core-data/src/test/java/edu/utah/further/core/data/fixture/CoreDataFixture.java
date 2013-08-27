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
package edu.utah.further.core.data.fixture;

import java.util.Iterator;
import java.util.List;

import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.collections.page.IterableType;
import edu.utah.further.core.api.collections.page.PagerFactory;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.core.data.domain.ComplexPersonEntity;
import edu.utah.further.core.data.hibernate.adapter.CriteriaType;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.data.hibernate.query.QueryBuilderHibernateImpl;
import edu.utah.further.core.data.util.CoreDataResourceLocator;
import edu.utah.further.core.query.domain.SearchQuery;

/**
 * A stand-alone unit test of searching for persons using Restlet and Spring.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 17, 2008
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/core-data-test-annotation-context.xml", "/core-data-test-datasource-context.xml",
		"/core-data-test-page-context.xml" }, inheritLocations = false)
public abstract class CoreDataFixture
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * Hibernate session factory in the current context.
	 */
	@Autowired
	protected SessionFactory sessionFactory;

	/**
	 * Instantiates pager objects.
	 */
	@Autowired
	private PagerFactory pagerFactory;

	// ========================= SETUP METHODS =============================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	protected static final QueryBuilderHibernateImpl personCriteriaBuilder()
	{
		return QueryBuilderHibernateImpl.newInstance(CriteriaType.CRITERIA,
				ComplexPersonEntity.class.getPackage().getName(),
				getCoreDataSessionFactory());
	}

	/**
	 * @return
	 */
	protected static final GenericCriteria newPersonCriteria(final SearchQuery searchQuery)
	{
		return QueryBuilderHibernateImpl.convert(CriteriaType.CRITERIA,
				ComplexPersonEntity.class.getPackage().getName(),
				getCoreDataSessionFactory(), searchQuery);
	}

	/**
	 * A pager factory method hook.
	 * 
	 * @param pageSize
	 *            page size
	 * @return pager instance of the specific implementation being tested in a sub-class
	 *         of this class
	 */
	protected Iterator<List<? extends PersistentEntity<Long>>> newPager(
			final int pageSize, final ScrollableResults resultSet)
	{
		// Convert list of IDs to a list of entities to pass to the pager
		return (Iterator<List<? extends PersistentEntity<Long>>>) getPagerFactory()
				.pager(resultSet, IterableType.SCROLLABLE_RESULTS, pageSize);
	}

	/**
	 * Return the pagerFactory property.
	 * 
	 * @return the pagerFactory
	 */
	protected final PagerFactory getPagerFactory()
	{
		return pagerFactory;
	}

	/**
	 * @return
	 */
	private static final SessionFactory getCoreDataSessionFactory()
	{
		ValidationUtil.validateNotNull("CoreDataResourceLocator.getInstance()",
				CoreDataResourceLocator.getInstance());
		return CoreDataResourceLocator.getInstance().getSessionFactory();
	}

}
