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

import static edu.utah.further.core.api.constant.Strings.PROPERTY_SCOPE_CHAR;
import static org.apache.commons.lang.Validate.notNull;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.ReflectionUtils;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.ds.impl.executor.db.hibernate.HibernateExecReq;

/**
 * An executor which takes a Hibernate {@link GenericCriteria} from a previous
 * {@link RequestProcessor} and creates a distinct projection (a native ISO SQL distinct)
 * on the properties defined by the root entity's JPA {@code @ID} annotation. This
 * executor can handle both singular and composite keys. A result transformer is used to
 * tranform the results into the corresponding type of the Id.
 *
 * Note that this {@link RequestProcessor} does not execute the Hibernate Criteria, see
 * {@link HibernateCriteriaListExecutor}.
 *
 * This executor expects the following to be set (see {@link HibernateExecReq}):
 *
 * {@literal <ul>
 * <li>Criteria</li>
 * <li>SessionFactory</li>
 * <li>Root Entity Class</li></ul> }
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
public class HibernateDistinctIdExecutor extends AbstractNonDelegatingUtilityProcessor
{
	// ========================= CONSTANTS ==================================

	/**
	 * This persistent object's context in HQL. Required to avoid HHH-817.
	 */
	private static final String THIS_CONTEXT = "this" + PROPERTY_SCOPE_CHAR;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Handles generic DAO operations and searches.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;

	// ========================= FIELDS =====================================

	/**
	 * Whether or not to override a criteria's projection if it has one, otherwise add to
	 * it. Default is false.
	 */
	private final boolean overrideExistingProjection;

	// ========================= CONSTRUCTORS ===============================

	/**
	 * Construct a district ID executor with no existing projection override.
	 */
	public HibernateDistinctIdExecutor()
	{
		this(false);
	}

	/**
	 * @param overrideExistingProjection
	 *            Whether or not to override an existing projection or to add to the
	 *            projection. The default is <code>false</code>.
	 */
	public HibernateDistinctIdExecutor(final boolean overrideExistingProjection)
	{
		this.overrideExistingProjection = overrideExistingProjection;
	}

	// ========================= IMPLEMENTATION: RequestHandler ==============

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.chain.AbstractRequestHandler#process(edu.utah.further.core.api.chain.ChainRequest)
	 * @see http://opensource.atlassian.com/projects/hibernate/browse/HHH-817
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		final HibernateExecReq executionReq = new HibernateExecReq(request);

		// Validate required input
		final GenericCriteria hibernateCriteria = executionReq.getResult();
		notNull(hibernateCriteria, "Expected Hibernate criteria");

		final Class<? extends PersistentEntity<?>> domainClass = executionReq
				.getRootEntity();
		final Class<? extends PersistentEntity<?>> entityClass = dao
				.getEntityClass(domainClass);

		notNull(entityClass, "Expected root entity class");

		final SessionFactory sessionFactory = executionReq.getSessionFactory();
		notNull(sessionFactory, "Expected SessionFactory");

		final ClassMetadata classMetadata = sessionFactory.getClassMetadata(entityClass);
		final String identifierName = classMetadata.getIdentifierPropertyName();
		final Type identifierType = classMetadata.getIdentifierType();

		// A hack to obtain projections out of the critieria by casting to the Hibernate
		// implementation. TODO: improve adapter to do that via interface access
		final ProjectionList projectionList = Projections.projectionList();
		final Projection existingProjection = ((CriteriaImpl) hibernateCriteria
				.getHibernateCriteria()).getProjection();

		if (existingProjection != null && !overrideExistingProjection)
		{
			return false;
		}

		if (identifierType.isComponentType())
		{
			final ComponentType componentType = (ComponentType) identifierType;
			final String[] idPropertyNames = componentType.getPropertyNames();

			// Add distinct to the first property
			projectionList.add(
					Projections.distinct(Property.forName(identifierName
							+ PROPERTY_SCOPE_CHAR + idPropertyNames[0])),
					idPropertyNames[0]);

			// Add the remaining properties to the projection list
			for (int i = 1; i < idPropertyNames.length; i++)
			{
				projectionList.add(
						Property.forName(identifierName + PROPERTY_SCOPE_CHAR
								+ idPropertyNames[i]), idPropertyNames[i]);
			}

			hibernateCriteria.setProjection(projectionList);
			hibernateCriteria.setResultTransformer(new AliasToBeanResultTransformer(
					ReflectionUtils.findField(entityClass, identifierName).getType()));
		}
		else
		{
			// 'this' required to avoid HHH-817
			projectionList.add(Projections.distinct(Property.forName(THIS_CONTEXT
					+ identifierName)));
			hibernateCriteria.setProjection(projectionList);
		}

		executionReq.setResult(hibernateCriteria);

		return false;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the dao property.
	 *
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(final Dao dao)
	{
		this.dao = dao;
	}

}
