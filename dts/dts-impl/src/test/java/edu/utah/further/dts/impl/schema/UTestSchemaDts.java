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
package edu.utah.further.dts.impl.schema;

import static edu.utah.further.core.util.context.SpringUtil.getFirstBeanOfType;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.dts.impl.aspect.AspectManager;
import edu.utah.further.dts.impl.aspect.DtsTransactionAspect;
import edu.utah.further.dts.impl.aspect.ReturnTypeChangingDtsTransactionAspect;

/**
 * Test our custom Spring schema "dts".
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;oren.livne@utah.edu&gt;</code>
 * @version Feb 13, 2009
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/dts-impl-test-schema-context.xml" })
public final class UTestSchemaDts
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestSchemaDts.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Spring application context.
	 */
	@Autowired
	private ApplicationContext applicationContext;

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	// ========================= METHODS ===================================

	/**
	 * Test the <code>aspects</code> schema element as a top-level element in a Spring
	 * context file.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("boxing")
	@Ignore
	// @Test
	public void aspectsTopLevelElement() throws Exception
	{
		final Class<AspectManager> elementType = AspectManager.class;
		final String elementName = "aspects";
		final AspectManager topLevelBean = getFirstBeanOfType(applicationContext,
				elementType);
		assertNotNull("Top level schema element " + elementName
				+ " was not created by Spring", topLevelBean);
		assertThat("Nested beans were not properly set on the top level bean by Spring",
				topLevelBean.getAspects().size(), greaterThanOrEqualTo(1));
	}

	/**
	 * Test the <code>aspect</code> schema element nested within a <code>aspects</code>
	 * element for creating an aspect bean.
	 * 
	 * @throws Exception
	 */
	@Test
	public void nestedAspectCreated() throws Exception
	{
		assertNotNull("ReturnTypeChangingDtsTransactionAspect bean was not created",
				getFirstBeanOfType(applicationContext,
						ReturnTypeChangingDtsTransactionAspect.class));
	}

	/**
	 * Test that the <code>aspect</code> schema element's created bean is the same object
	 * as a child of <code>aspects</code> top-level bean.
	 * 
	 * @throws Exception
	 */
	// @Test
	@Ignore
	public void nestedAspectBeanEqualsAspectsBeanChild() throws Exception
	{
		final AspectManager manager = getFirstBeanOfType(applicationContext,
				AspectManager.class);
		final DtsTransactionAspect aspectBean = getFirstBeanOfType(applicationContext,
				DtsTransactionAspect.class);
		if (log.isDebugEnabled())
		{
			log.debug("manager.children " + manager.getAspects());
			log.debug("aspectBean " + aspectBean);
		}
		assertTrue(
				"DtsTransactionAspect bean was not found in the AspectManager children list",
				manager.getAspects().contains(aspectBean));
	}

	// ========================= PRIVATE METHODS ===========================
}
