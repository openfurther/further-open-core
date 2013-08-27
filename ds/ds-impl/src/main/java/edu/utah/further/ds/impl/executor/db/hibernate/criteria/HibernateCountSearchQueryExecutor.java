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

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.ds.impl.executor.db.hibernate.HibernateExecReq;

/**
 * Adds a count projection to Hibernate search criteria.
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
 * @version Apr 5, 2012
 */
@Service("hibernateCountQueryExecutor")
public class HibernateCountSearchQueryExecutor extends
		AbstractNonDelegatingUtilityProcessor
{

	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(HibernateCountSearchQueryExecutor.class);
	
	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@SuppressWarnings("boxing")
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
		final String identifierName = classMetadata.getIdentifierPropertyName();
		final Type identifierType = classMetadata.getIdentifierType();
		
		if (identifierType.isComponentType()) 
		{
			criteria.setProjection(Projections.countDistinct(identifierName + "." + identifierName));
		}
		else
		{
			criteria.setProjection(Projections.countDistinct(identifierName));
		}
		Long result = -1l;
		try 
		{
			result = criteria.uniqueResult();
		}
		catch (final HibernateException e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Caught Hibernate exception.");
			}
		}
		
		execReq.setResult(result);
		execReq.setStatus("Returned search query count result");
		
		
		return false;
	}

}
