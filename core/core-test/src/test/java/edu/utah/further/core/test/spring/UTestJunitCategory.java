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
package edu.utah.further.core.test.spring;

import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test supporting JUnit categories via the <code>TSuite</code> framework.
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
 * @version Jul 30, 2010
 */
@RunWith(SpringJUnit4ClassRunner.class)
public final class UTestJunitCategory extends JunitCategoryFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestJunitCategory.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	// ========================= SETUP METHODS =============================

	// ========================= TESTING METHODS ===========================

	/**
	 * A normal JUnit test.
	 */
	@Test
	public void normalTest()
	{
		if (log.isDebugEnabled())
		{
			log.debug("normalTest()");
		}
	}

	// ========================= PRIVATE METHODS ===========================
}
