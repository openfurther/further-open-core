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
package edu.utah.further.core.util.aop;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test decorating Log4J logging calls.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 16, 2011
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/core-util-test-aop-logging-context.xml" })
public final class UTestLoggingAspect
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestLoggingAspect.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Advisor.
	 */
	@Autowired
	private LoggingAspect loggingAspect;

	/**
	 * Advised.
	 */
	@Autowired
	private AdvisedBean advisedBean;

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	/**
	 * Runs after before test method.
	 */
	@Before
	public void resetInvocationCounter()
	{
		loggingAspect.setNumInvocations(0);
	}

	// ========================= METHODS ===================================

	/**
	 * Runs after before test method.
	 */
	@Test
	public void verifyLoggingAop()
	{
		assertAspectInvocationsEquals(0);
		log.debug("Test message logging call that should be advised");

		// Well... the Logger object is created outside Spring, so we can't advise it with
		// plain Spring AOP. Need to use CTW, LTW or RTW.
		assertAspectInvocationsEquals(0);
	}

	/**
	 * Test the <code>boolean</code> schema element as a top-level element in a Spring
	 * context file.
	 *
	 * @throws Exception
	 */
	@Test
	public void advisingBean() throws Exception
	{
		assertAspectInvocationsEquals(0);
		advisedBean.print();
		assertAspectInvocationsEquals(1);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param expected
	 */
	private void assertAspectInvocationsEquals(final int expected)
	{
		assertEquals("Aspect not invoked the expected number of times", expected,
				loggingAspect.getNumInvocations());
	}

}
