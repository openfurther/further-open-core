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
package edu.utah.further.ds.impl.service.query.logic;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.page.PagingLoopController;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.ds.api.service.query.logic.Persister;

/**
 * Persists new entities to a database using <code>dao.create()</code>. Note that if the
 * entity collection contains duplicate records (entities with the same ID), this
 * processor will fail on a primary key violation.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 1, 2010
 */
@Service("persisterCreate")
public class PersisterCreateImpl implements Persister
{
	
	/**
	 * A generic data access object to persistent entities.
	 */
	@Autowired
	@Qualifier("inMemoryDao")
	private Dao dao;

	/**
	 * Transaction template for more fine grained control over the transaction
	 */
	@Autowired
	@Qualifier("inMemoryTransactionTemplate")
	private TransactionTemplate transactionTemplate;
	
	// ========================= CONSTANTS =============================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(PersisterCreateImpl.class);

	// ========================= IMPL: RequestProcessor ====================

	/**
	 * @param dao
	 * @param entities
	 * @param controller
	 * @return
	 * @see edu.utah.further.ds.api.service.query.logic.Persister#persist(edu.utah.further.core.api.data.Dao,
	 *      java.util.Collection,
	 *      edu.utah.further.core.api.collections.page.PagingLoopController)
	 */
	public List<PersistentEntity<?>> getEntities(
			final Collection<? extends PersistentEntity<?>> entities,
			final PagingLoopController controller)
	{
		int entityNum = 0;
		int counter = controller.getResultCount();
		final int maxResults = controller.getMaxResults();
		final List<PersistentEntity<?>> persistedEntities = CollectionUtil.newList();
		for (final PersistentEntity<?> persistentEntity : entities)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Creating entity #" + entityNum + " ID "
						+ persistentEntity.getId());
			}
			dao.save(persistentEntity);
			persistedEntities.add(persistentEntity);
			entityNum++;
			counter++;
			if (counter == maxResults)
			{
				// Reached end of outer paging loop, truncate this page
				break;
			}
		}

		// Flush the current page's batch of reads, inserts and updates
		// and release memory
		try
		{
			dao.flush();
		}
		catch (final Throwable e)
		{
			// May happen if current session configured with JPA, not Hibernate
		}

		return persistedEntities;
	}
	
	/**
	 * @param entities
	 * @param controller
	 * @return
	 * @see edu.utah.further.ds.api.service.query.logic.Persister#persist(edu.utah.further.core.api.data.Dao,
	 *      java.util.Collection,
	 *      edu.utah.further.core.api.collections.page.PagingLoopController)
	 */
	@Override
	public List<PersistentEntity<?>> persist(final Collection<? extends PersistentEntity<?>> entities,
			final PagingLoopController controller) 
	{
		final List<PersistentEntity<?>> persistedEntities = transactionTemplate.execute(new TransactionCallback<List<PersistentEntity<?>>>()
		{
			@Override
			public List<PersistentEntity<?>> doInTransaction(
					final TransactionStatus status)
			{
				// Read input from request and convert to an entity list
				return getEntities(entities, controller);


			}
		});
		
		return persistedEntities;
		
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.ds.api.service.query.logic.Persister#persist(java.util.Collection, edu.utah.further.core.api.collections.page.PagingLoopController, edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public List<PersistentEntity<?>> persist(
			Collection<? extends PersistentEntity<?>> entities,
			PagingLoopController controller, ChainRequest request)
	{
		// TODO Auto-generated method stub
		return persist(entities, controller);
	}

}
