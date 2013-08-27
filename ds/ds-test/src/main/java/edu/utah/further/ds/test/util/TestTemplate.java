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
package edu.utah.further.ds.test.util;

import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import edu.utah.further.ds.api.lifecycle.LifeCycle;

/**
 * A generic test template of camel flows.
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
 * @version Jan 27, 2011
 */
public interface TestTemplate<I, O>
{
	// ========================= METHODS ===================================

	/**
	 * Assert that the command succeeded by checking its status.
	 */
	void assertCommandSucceeded();

	/**
	 * Assert that the command failed.
	 */
	void assertCommandFailed();

	/**
	 * Assert that the query failed because of a certain processor.
	 * 
	 * @param processorName
	 *            name of processor that generated the query's failure status message
	 */
	void assertCommandFailedInProcessor(final String processorName);

	/**
	 * Run the query and set the actual query results on this object.
	 */
	void run();

	/**
	 * Run the query, and assert that it succeeded and that the actual count >= expected
	 * count set on this object.
	 */
	void runAndAssertCountGeExpected();

	/**
	 * Assert that the actual count = expected count set on this object.
	 */
	void assertCountEqExpected();

	/**
	 * Assert that the actual count >= expected count set on this object.
	 */
	void assertCountGeExpected();

	/**
	 * Set a new value for the query property.
	 * 
	 * @param input
	 *            the query to set
	 */
	void setInput(final Resource input);

	/**
	 * Return the expectedCount property.
	 * 
	 * @return the expectedCount
	 */
	long getExpectedCount();

	/**
	 * Return the expectedLinkCount property.
	 * 
	 * @return the expectedLinkCount
	 */
	Long getExpectedLinkCount();

	/**
	 * Set a new value for the expectedCount property.
	 * 
	 * @param expectedCount
	 *            the expectedCount to set
	 */
	void setExpectedCount(final long expectedCount);

	/**
	 * Set a new value for the expectedLinkCount property.
	 * 
	 * @param expectedLinkCount
	 *            the expectedLinkCount to set
	 */
	void setExpectedLinkCount(Long expectedLinkCount);

	/**
	 * Set a new value for the jdbcTemplate property.
	 * 
	 * @param jdbcTemplate
	 *            the jdbcTemplate to set
	 */
	void setJdbcTemplate(final SimpleJdbcTemplate jdbcTemplate);

	/**
	 * Set a new value for the lifeCycle property.
	 * 
	 * @param lifeCycle
	 *            the lifeCycle to set
	 */
	void setLifeCycle(final LifeCycle<I, O> lifeCycle);

	/**
	 * Return the actualCount property.
	 * 
	 * @return the actualCount
	 */
	long getActualCount();

	/**
	 * Return the actualLinkCount property.
	 * 
	 * @return the actualLinkCount
	 */
	long getActualLinkCount();

	/**
	 * Set a new value for the listener property.
	 * 
	 * @param listener
	 *            the listener to set
	 */
	void setListener(final QueryTestListener listener);

	/**
	 * @return the input command object
	 */
	Resource getInput();

	/**
	 * @return the output result from the life cycle
	 */
	O getResult();
	
	/**
	 * Return the username property.
	 *
	 * @return the username
	 */
	String getUsername();

	/**
	 * Set a new value for the username property.
	 *
	 * @param username the username to set
	 */
	void setUsername(final String username);

	/**
	 * @return the output result count from the life cycle
	 */
	void assertLifecycleResultCountGeExpected();
}