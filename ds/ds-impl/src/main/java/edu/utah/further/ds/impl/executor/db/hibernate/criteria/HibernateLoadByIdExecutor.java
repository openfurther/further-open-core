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

import static edu.utah.further.core.data.util.HibernateUtil.MAX_IN;
import static edu.utah.further.core.data.util.HibernateUtil.THIS;
import static org.apache.commons.lang.Validate.notNull;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.discrete.HasIdentifier;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;
import edu.utah.further.core.data.hibernate.adapter.CriteriaType;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteriaFactory;
import edu.utah.further.core.data.util.HibernateUtil;
import edu.utah.further.ds.impl.executor.db.hibernate.HibernateExecReq;

/**
 * An executor which takes a list of ID's (extends {@link HasIdentifier}) from a previous
 * {@link RequestProcessor} and loads each entity by ID respecting fetch strategy
 * originally defined by the entities relationships (i.e. lazy or eager). This
 * {@link RequestProcessor} can handle both singular and composite ID's.
 *
 * This executor expects the following to be set (see {@link HibernateExecReq})
 *
 * {@literal <ul>
 * <li>List<? extends HasIdentifier<?>></li>
 * <li>SessionFactory</li>
 * <li>Root Entity Class</li> * </ul>}
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
 * @version Oct 1, 2009
 */
public class HibernateLoadByIdExecutor extends AbstractNonDelegatingUtilityProcessor
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
		// Read input arguments
		final HibernateExecReq executionReq = new HibernateExecReq(request);
		final SessionFactory sessionFactory = executionReq.getSessionFactory();
		notNull(sessionFactory, "Expected SessionFactory");

		final Class<? extends PersistentEntity<?>> rootEntity = executionReq
				.getRootEntity();
		notNull(rootEntity, "Expected root entity class");

		// Read the search criteria's root entity meta data
		final List<Object> list = executionReq.getResult();
		final Object[] listArray = CollectionUtil.toArrayNullSafe(list);
		final ClassMetadata classMetadata = sessionFactory.getClassMetadata(rootEntity);
		final String identifierName = classMetadata.getIdentifierPropertyName();
		final Type identifierType = classMetadata.getIdentifierType();
		final int numTypes = listArray.length;
		final Type[] types = new Type[numTypes];
		for (int i = 0; i < numTypes; i++)
		{
			types[i] = identifierType;
		}

		// Build Hibernate criteria
		final GenericCriteria criteria = GenericCriteriaFactory.criteria(
				CriteriaType.CRITERIA, rootEntity, sessionFactory.getCurrentSession());
		if (identifierType.isComponentType())
		{
			final String sqlInClause = HibernateUtil.sqlRestrictionCompositeIn(
					rootEntity, sessionFactory, numTypes);
			criteria.add(Restrictions.sqlRestriction(sqlInClause, listArray, types));
		}
		else
		{
			final int size = list.size();
			if (size > MAX_IN)
			{
				// Create a disjunction of IN clauses. Add MAX_IN elements at a time to
				// each IN clause (except the last IN, whose size is size % MAX_IN).
				final Junction junction = Restrictions.disjunction();
				for (int i = 0; i < size; i += MAX_IN)
				{
					junction.add(Restrictions.in(THIS + identifierName,
							list.subList(i, Math.max(size, MAX_IN + i))));
				}
				criteria.add(junction);
			}
			else
			{
				// Single chunk, add directly as a criterion without the junction trick
				criteria.add(Restrictions.in(THIS + identifierName, list));
			}

		}

		executionReq.setResult(criteria);
		return false;
	}
}
