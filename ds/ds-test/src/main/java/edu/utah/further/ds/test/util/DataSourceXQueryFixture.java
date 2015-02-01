/*******************************************************************************
 * Source File: DataSourceXQueryFixture.java
 ******************************************************************************/
package edu.utah.further.ds.test.util;

import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.qunit.runner.QTestFixture;
import edu.utah.further.core.qunit.runner.QTestRunner;
import edu.utah.further.core.util.context.PFixtureManager;

/**
 * Data source XQuery test fixture. This class is designed for extension.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 13, 2010
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/META-INF/ds/test/ds-test-mdr-ws-client-context.xml",
		"/META-INF/ds/test/ds-test-xquery-context.xml" })
public abstract class DataSourceXQueryFixture extends QTestFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(DataSourceXQueryFixture.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Manages the web service server application context for integration tests.
	 */
	@Autowired
	private PFixtureManager manager;

	/**
	 * Runs individual XQuery tests.
	 */
	@Autowired
	@Qualifier("qTestRunnerXQuery")
	private QTestRunner xmlTestRunner;

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	/**
	 * Set up fixture.
	 */
	@Before
	public void initializeFixture()
	{
		manager.initialize();
	}

	// ========================= IMPL: QTestRunner =========================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.ds.test.util.XmlTestFixture#getXmlTestRunner()
	 */
	@Override
	protected QTestRunner getQTestRunner()
	{
		return xmlTestRunner;
	}

}