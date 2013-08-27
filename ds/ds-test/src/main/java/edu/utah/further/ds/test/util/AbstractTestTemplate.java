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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.ds.api.lifecycle.LifeCycle;

/**
 * A useful JavaBean that holds the input and output of a flow (chain of processors) test.
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
 * @version Jul 15, 2010
 */
public abstract class AbstractTestTemplate<I, O> implements TestTemplate<I, O>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AbstractTestTemplate.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * JDBC Template for querying results.
	 */
	@Autowired
	protected SimpleJdbcTemplate jdbcTemplate;

	// ========================= FIELDS ====================================

	/**
	 * A query life cycle to run the query in.
	 */
	private LifeCycle<I, O> lifeCycle;

	/**
	 * Serialized input command class-path resource.
	 */
	private Resource input;

	/**
	 * Expected output result set size.
	 */
	private long expectedCount;

	/**
	 * Expected link output result set size.
	 */
	private Long expectedLinkCount;

	/**
	 * Actual patient output result set size.
	 */
	private long actualCount;

	/**
	 * Actual patient link output result set size.
	 */
	private long actualLinkCount;
	
	/**
	 * Count directly from life cycle result
	 */
	private long lifecycleResultCount;

	/**
	 * A function pointer that runs before and after the query runs (set-up).
	 */
	private QueryTestListener listener = new QueryTestListener();

	/**
	 * Resulting object from life cycle.
	 */
	private O result;
	
	/**
	 * Set a username to run this query as
	 */
	private String username;

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Converts the input resource to the command fed to the life cycle.
	 *
	 * @return command object
	 */
	protected abstract I generateCommand();

	/**
	 * Gets a count directly from the lifeCycle result
	 *
	 * @return command object
	 */
	protected abstract long getCountByResult();
	// ========================= METHODS ===================================

	/**
	 *
	 * @see edu.utah.further.ds.test.util.TestTemplate#run()
	 */
	@Override
	public final void run()
	{
		listener.before();

		// Trigger valid life cycle with valid query context
		setResult(lifeCycle.triggerCommand(generateCommand()));
		final long patientCount = getPatientCountByPatientTable();
		setActualCount(patientCount);
		final long resultCount = getCountByResult();
		setLifecycleResultCount(resultCount);
		if (log.isInfoEnabled())
		{
			log.info("Checked patient table: " + patientCount + " entities were saved");
		}
		final long linkCount = getPatientCountByLinkTable();
		setActualLinkCount(linkCount);
		if (log.isInfoEnabled())
		{
			log.info("Checked link table: " + linkCount + " entities were saved");
		}
		
		cleanUpDataSourceTest();

		listener.after();
	}

	/**
	 *
	 * @see edu.utah.further.ds.test.util.TestTemplate#runAndAssertCountGeExpected()
	 */
	@Override
	public final void runAndAssertCountGeExpected()
	{
		run();
		assertCommandSucceeded();
		assertCountGeExpected();
	}

	/**
	 * @see edu.utah.further.ds.test.util.TestTemplate#assertCountEqExpected()
	 */
	@Override
	public final void assertCountEqExpected()
	{
		// Check the patient count
		assertThat(new Long(getActualCount()), is(new Long(getExpectedCount())));

		// Check the link count against expected link count if it is set; otherwise
		// against the patient count
		final Long expectedLinkCountLocal = getExpectedLinkCount();
		assertThat(new Long(actualLinkCount),
				is((expectedLinkCountLocal != null) ? expectedLinkCountLocal : new Long(
						getExpectedCount())));
	}

	/**
	 *
	 * @see edu.utah.further.ds.test.util.TestTemplate#assertCountGeExpected()
	 */
	@Override
	public final void assertCountGeExpected()
	{
		
		// Check the patient count
		assertThat(new Long(getActualCount()), greaterThanOrEqualTo(new Long(
				getExpectedCount())));

		// against the patient count
		final Long expectedLinkCountLocal = getExpectedLinkCount();
		assertThat(
				new Long(actualLinkCount),
				greaterThanOrEqualTo((expectedLinkCountLocal != null) ? expectedLinkCountLocal
						: new Long(getExpectedCount())));
	}
	
	/**
	 * 
	 * @see edu.utah.further.ds.test.util.TestTemplate#assertLifecycleResultCountGeExpected()
	 */
	@Override
	public final void assertLifecycleResultCountGeExpected()
	{
		assertThat(new Long(getLifecycleResultCount()), greaterThanOrEqualTo(new Long(getExpectedCount())));
	}

	// ========================= GET / SET =================================

	/**
	 * @param input
	 * @see edu.utah.further.ds.test.util.TestTemplate#setInput(org.springframework.core.io.Resource)
	 */
	@Override
	public final void setInput(final Resource input)
	{
		this.input = input;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.test.util.TestTemplate#getExpectedCount()
	 */
	@Override
	public final long getExpectedCount()
	{
		return expectedCount;
	}

	/**
	 * Return the expectedLinkCount property.
	 *
	 * @return the expectedLinkCount
	 */
	@Override
	public Long getExpectedLinkCount()
	{
		return expectedLinkCount;
	}

	/**
	 * @param expectedCount
	 * @see edu.utah.further.ds.test.util.TestTemplate#setExpectedCount(long)
	 */
	@Override
	public final void setExpectedCount(final long expectedCount)
	{
		this.expectedCount = expectedCount;
	}

	/**
	 * Set a new value for the expectedLinkCount property.
	 *
	 * @param expectedLinkCount
	 *            the expectedLinkCount to set
	 */
	@Override
	public final void setExpectedLinkCount(final Long expectedLinkCount)
	{
		this.expectedLinkCount = expectedLinkCount;
	}

	/**
	 * @param jdbcTemplate
	 * @see edu.utah.further.ds.test.util.TestTemplate#setJdbcTemplate(org.springframework.jdbc.core.simple.SimpleJdbcTemplate)
	 */
	@Override
	public final void setJdbcTemplate(final SimpleJdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @param lifeCycle
	 * @see edu.utah.further.ds.test.util.TestTemplate#setLifeCycle(edu.utah.further.ds.api.lifecycle.LifeCycle)
	 */
	@Override
	public final void setLifeCycle(final LifeCycle<I, O> lifeCycle)
	{
		this.lifeCycle = lifeCycle;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.test.util.TestTemplate#getActualCount()
	 */
	@Override
	public final long getActualCount()
	{
		return actualCount;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.test.util.TestTemplate#getActualLinkCount()
	 */
	@Override
	public long getActualLinkCount()
	{
		return actualLinkCount;
	}

	/**
	 * @param listener
	 * @see edu.utah.further.ds.test.util.TestTemplate#setListener(edu.utah.further.ds.test.util.QueryTestListener)
	 */
	@Override
	public final void setListener(final QueryTestListener listener)
	{
		ValidationUtil.validateNotNull("listener", listener);
		this.listener = listener;
	}

	/**
	 * Return the query property.
	 *
	 * @return the query
	 * @see edu.utah.further.ds.test.util.TestTemplate#getInput()
	 * @return
	 */
	@Override
	public final Resource getInput()
	{
		return input;
	}

	/**
	 * Return the result property.
	 *
	 * @return the result
	 * @see edu.utah.further.ds.test.util.TestTemplate#getResult()
	 * @return
	 */
	@Override
	public final O getResult()
	{
		return result;
	}
	
	/**
	 * Return the username property.
	 *
	 * @return the username
	 */
	@Override
	public String getUsername()
	{
		return username;
	}

	/**
	 * Set a new value for the username property.
	 *
	 * @param username the username to set
	 */
	@Override
	public void setUsername(final String username)
	{
		this.username = username;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param jdbcTemplate
	 * @return
	 */
	protected long getPatientCountByPatientTable()
	{
		return DataSourceTestUtil.getPatientCountByPatientTable(jdbcTemplate);
	}

	/**
	 * @param jdbcTemplate
	 * @return
	 */
	protected long getPatientCountByLinkTable()
	{
		return DataSourceTestUtil.getPatientCountByLinkTable(jdbcTemplate);
	}

	/**
	 * Deletes saved entities after test, asserts that zero entities exist.
	 *
	 * @param jdbcTemplate
	 */
	protected void cleanUpDataSourceTest()
	{
		DataSourceTestUtil.cleanUpDataSourceTest(jdbcTemplate);
	}

	/**
	 * Set a new value for the actualCount property.
	 *
	 * @param actualCount
	 *            the actualCount to set
	 */
	private void setActualCount(final long actualCount)
	{
		this.actualCount = actualCount;
	}

	/**
	 * Set a new value for the actualLinkCount property.
	 *
	 * @param actualLinkCount
	 *            the actualLinkCount to set
	 */
	private void setActualLinkCount(final long actualLinkCount)
	{
		this.actualLinkCount = actualLinkCount;
	}

	/**
	 * Return the lifecycleResultCount property.
	 *
	 * @return the lifecycleResultCount
	 */
	public long getLifecycleResultCount()
	{
		return lifecycleResultCount;
	}

	/**
	 * Set a new value for the lifecycleResultCount property.
	 *
	 * @param lifecycleResultCount the lifecycleResultCount to set
	 */
	public void setLifecycleResultCount(final long lifecycleResultCount)
	{
		this.lifecycleResultCount = lifecycleResultCount;
	}

	/**
	 * Set a new value for the result property.
	 *
	 * @param result
	 *            the result to set
	 */
	private void setResult(final O result)
	{
		this.result = result;
	}
}
