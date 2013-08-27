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
package edu.utah.further.core.qunit.schema;

import javax.annotation.Resource;

import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.qunit.runner.QTestFixture;
import edu.utah.further.core.qunit.runner.QTestRunner;
import edu.utah.further.core.qunit.runner.QTestSuite;

/**
 * A unit test of the QUnit framework.
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
 * @version Sep 2, 2010
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/core-qunit-test-schema-context.xml" })
public final class QTestSchemaQunit extends QTestFixture
{

	// ========================= DEPENDENCIES ===========================

	/**
	 * Test running service.
	 */
	@Autowired
	@Qualifier("qTestRunnerSimple")
	private QTestRunner qTestRunner;

	/**
	 * Spring application context.
	 */
	@Resource(name = "testSuite")
	private QTestSuite qTestSuite;

	// ========================= TESTING METHODS ===========================

	/**
	 * A temporary workaround until we figure out how to test both failed and passed tests
	 * in a better way.
	 */
	@Override
	@Test
	@ExpectedException(ComparisonFailure.class)
	public void testSuite() throws Throwable
	{
		super.testSuite();
	}

	// ========================= IMPL: QTestFixture ========================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.ds.test.util.XmlTestFixture#getXmlTestRunner()
	 */
	@Override
	public QTestRunner getQTestRunner()
	{
		return qTestRunner;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.ds.test.util.QTestFixture#getQTestSuite()
	 */
	@Override
	protected QTestSuite getQTestSuite()
	{
		return qTestSuite;
	}

}
