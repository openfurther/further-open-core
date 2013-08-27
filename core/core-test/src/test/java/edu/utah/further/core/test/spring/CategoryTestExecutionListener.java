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

import java.lang.reflect.Method;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * ...
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
public class CategoryTestExecutionListener implements TestExecutionListener
{
	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.test.context.TestExecutionListener#beforeTestClass(org.
	 * springframework.test.context.TestContext)
	 */
	@Override
	public void beforeTestClass(final TestContext testContext) throws Exception
	{
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.test.context.TestExecutionListener#prepareTestInstance(org.
	 * springframework.test.context.TestContext)
	 */
	@Override
	public void prepareTestInstance(final TestContext testContext) throws Exception
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.test.context.TestExecutionListener#beforeTestMethod(org.
	 * springframework.test.context.TestContext)
	 */
	@Override
	public void beforeTestMethod(final TestContext testContext) throws Exception
	{
		// TODO Auto-generated method stub
		final Method method = testContext.getTestMethod();
		method.getAnnotations();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.test.context.TestExecutionListener#afterTestMethod(org.
	 * springframework.test.context.TestContext)
	 */
	@Override
	public void afterTestMethod(final TestContext testContext) throws Exception
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.test.context.TestExecutionListener#afterTestClass(org.
	 * springframework.test.context.TestContext)
	 */
	@Override
	public void afterTestClass(final TestContext testContext) throws Exception
	{
		// TODO Auto-generated method stub

	}

}
