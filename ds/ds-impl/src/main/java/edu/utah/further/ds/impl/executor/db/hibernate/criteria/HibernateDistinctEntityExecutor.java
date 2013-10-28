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
import static org.slf4j.LoggerFactory.getLogger;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.ds.impl.executor.db.hibernate.HibernateExecReq;

/**
 * A Hibernate Distinct Entity executor that utilizes a SQL distinct projection to select
 * distinct root entities.
 * 
 * The implementation requires a custom projection class
 * {@link CustomPropertyAliasProjection} because hibernate will try and use aliases in the
 * WHERE clause which is not permitted (HHH-817)
 * 
 * Results and then transformed using Hibernate's {@link AliasToBeanResultTransformer} so
 * that ONLY root entity objects are returned.
 * 
 * This differs from {@link HibernateDistinctRootExecutor} in that the distinct is done
 * within the database and not programmatically after the results are returned.
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
 * @version Aug 31, 2011
 */
@Service("hibernateDistinctEntityExecutor")
public final class HibernateDistinctEntityExecutor extends
		AbstractNonDelegatingUtilityProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(HibernateDistinctEntityExecutor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.
	 * api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		final HibernateExecReq execReq = new HibernateExecReq(request);
		final GenericCriteria criteria = execReq.getResult();
		notNull(criteria, "Expected Hibernate criteria");

		final Class<? extends PersistentEntity<?>> domainClass = execReq.getRootEntity();

		final SessionFactory sessionFactory = execReq.getSessionFactory();
		notNull(sessionFactory, "Expected SessionFactory");

		// Get information about the root entity class
		final ClassMetadata classMetadata = sessionFactory.getClassMetadata(domainClass);
		final String[] properties = classMetadata.getPropertyNames();
		final String identifierName = classMetadata.getIdentifierPropertyName();

		final ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.distinct(Projections.property(identifierName)),
				identifierName);

		// When you use projections, you have to manually specify the selection criteria
		// so we loop through all the properties and specify them here. Note that we skip
		// all the relationship properties (collections).
		for (final String property : properties)
		{
			final Type type = classMetadata.getPropertyType(property);
			if (!(type.isCollectionType() || type.isAssociationType()))
			{
				projectionList.add(Projections.property(property), property);
			}
		}

		criteria.setProjection(projectionList);

		// This turns all of the results into the actual root entity class - calling
		// setters/etc
		criteria.setResultTransformer(new AliasToBeanResultTransformer(domainClass));

		execReq.setResult(criteria);

		return false;
	}
}
