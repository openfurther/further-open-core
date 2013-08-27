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
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * Test the custom <code>aspectj-autoproxy</code> element.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Jun 2, 2010
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/core-util-test-aop-context.xml" })
public final class UTestAspectJAutoproxy
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestAspectJAutoproxy.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Spring application context.
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Advisor.
	 */
	@Autowired
	private CountingAspect countingAspect;

	/**
	 * Advised.
	 */
	@Autowired
	private AdvisedBean advisedBean;

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	// ========================= METHODS ===================================

	/**
	 * First, check that the default bean creator (under the hard-coded Spring bean name)
	 * is the dummy one we created.
	 *
	 * @throws Exception
	 */
	@Test
	public void defaultProxyCreatorIsOurOwn() throws Exception
	{
		final Object defaultCreator = applicationContext
				.getBean(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
		// AopConfigUtils.APS_PRIORITY_LIST mandates a *specific* class name
		// assertTrue("Unexpected proxy creator bean type", ReflectionUtil.instanceOf(
		// defaultCreator, SelectiveAspectJAutoProxyCreator.class));
		assertTrue("Unexpected proxy creator bean type", ReflectionUtil.instanceOf(
				defaultCreator, AnnotationAwareAspectJAutoProxyCreator.class));
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
				countingAspect.getNumInvocations());
	}
}
