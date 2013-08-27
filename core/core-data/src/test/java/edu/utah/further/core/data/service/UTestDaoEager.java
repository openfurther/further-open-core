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

import static org.slf4j.LoggerFactory.getLogger;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.data.Dao;
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
public final class UTestDaoEager extends UTestDao
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestDaoEager.class);

	// ========================= FIELDS ====================================

	/**
	 * Handles generic DAO operations and searches.
	 */
	@Autowired
	@Qualifier("daoEager")
	private Dao dao;

	// ========================= TESTING METHODS ===========================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the dao property.
	 * 
	 * @return the dao
	 */
	@Override
	protected Dao getDao()
	{
		return dao;
	}
}
