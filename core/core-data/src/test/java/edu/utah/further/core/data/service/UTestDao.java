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
package edu.utah.further.core.data.service;

import static edu.utah.further.core.util.io.LoggingUtil.debugPrintAndCenter;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.data.domain.SimpleEntity;
import edu.utah.further.core.data.fixture.CoreDataFixture;
import edu.utah.further.core.test.annotation.UnitTest;

/**
 * A unit test class of the Hibernate configuration and DAO.
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
 * @version Jan 28, 2009
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UTestDao extends CoreDataFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestDao.class);

	// ========================= FIELDS ====================================

	/**
	 * Handles generic DAO operations and searches.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;
	
	@Autowired
	private TransactionTemplate transactionTemplate;

	// ========================= SETUP METHODS =============================

	// ========================= TESTING METHODS ===========================

	/**
	 * A unit test of this service that runs when the bundle is started.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void saveEntity()
	{
		// Create and save entity
		final SimpleEntity entity = new SimpleEntity();
		entity.setAge(50);
		getDao().create(entity);
		if (log.isDebugEnabled())
		{
			log.debug("Entity: " + entity);
		}
		assertNotNull(entity.getId());

		// Search for entities in the database
		final List<SimpleEntity> list = getDao().findAll(SimpleEntity.class);
		if (log.isDebugEnabled())
		{
			log.debug("Entities in database: " + list);
		}
		assertEquals(1, list.size());

		// Clean up
		getDao().deleteAll(SimpleEntity.class);
		final List<SimpleEntity> list2 = getDao().findAll(SimpleEntity.class);
		if (log.isDebugEnabled())
		{
			log.debug("After clean up, entities in database: " + list2);
		}
		assertEquals(0, list2.size());

		debugPrintAndCenter(log, "saveEntity() end");
	}
	
	@Test
	public void preventNullUpdate() {
		
		final SimpleEntity entity = new SimpleEntity();
		transactionTemplate.execute(new TransactionCallbackWithoutResult()
		{
			
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				entity.setAge(new Integer(50));
				getDao().create(entity);
			}
		});
		
		final Long id = entity.getId();
		
		final SimpleEntity entity1 = new SimpleEntity();
		transactionTemplate.execute(new TransactionCallbackWithoutResult()
		{
			
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				entity1.setId(id);
				entity1.setAge(null);
				entity1.setComment("Comment");
				getDao().update(entity1);
			}
		});
		
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult()
		{
			
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final SimpleEntity entity2 = getDao().getById(SimpleEntity.class, id);
				assertThat(entity2.getAge(), notNullValue());
				assertThat(entity2.getComment(), is("Comment"));
				getDao().deleteAll(SimpleEntity.class);
			}
		});
		
	}
	
	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the dao property.
	 * 
	 * @return the dao
	 */
	protected Dao getDao()
	{
		return dao;
	}
}
