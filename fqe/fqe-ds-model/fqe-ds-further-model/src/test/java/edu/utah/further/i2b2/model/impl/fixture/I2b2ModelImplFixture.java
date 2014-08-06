/*******************************************************************************
 * Source File: I2b2ModelImplFixture.java
 ******************************************************************************/
package edu.utah.further.i2b2.model.impl.fixture;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.xml.jaxb.JaxbConfig;
import edu.utah.further.ds.i2b2.model.api.to.PatientDimensionsTo;
import edu.utah.further.ds.i2b2.model.impl.to.PatientDimensionsToImpl;

/**
 * Test fixture for I2b2 Model Impl tests.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Apr 19, 2010
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/i2b2-model-impl-test-context-services.xml",
		"/i2b2-model-impl-test-context-annotation.xml",
		"/i2b2-model-impl-test-context-datasource.xml" })
public abstract class I2b2ModelImplFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * One patient with observation - XML file. Used in marshalling/unmarshalling
	 */
	private static final String FILE = "patientdimensions.xml";

	/**
	 * Patient data for use in tests - XML file.
	 */
	public static final String TEST_PATIENTS_FILE = "patient-dimensions-histogram.xml";

	// ========================= DEPENDENCIES ==============================

	/**
	 * For XML marhshalling & unmarshalling.
	 */
	@Autowired
	protected XmlService xmlService;

	/**
	 * JdbcTemplate for assertion querying
	 */
	@Autowired
	protected SimpleJdbcTemplate jdbcTemplate;

	/**
	 * Executes basic DAO operations.
	 */
	@Autowired
	@Qualifier("dao")
	protected Dao dao;

	/**
	 * Pre-cooked query identifiers for testing.
	 */
	protected static final List<String> queryIds = Arrays.asList(new String[]
	{ "12345", "67890" });

	protected static final List<Object> queryIdObjs = Arrays.asList(new Object[]
	{ "12345", "67890" });

	// ========================= SETUP METHODS =============================

	@Before
	public void globalSetup()
	{
		xmlService.setDefaultJaxbConfig(JaxbConfig.EMPTY.getJaxbConfig());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Private helper method to do the unmarshalling since we use this in both marshalling
	 * and unmarshalling tests.
	 * 
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 * @throws XmlMappingException
	 * @throws IOException
	 */
	protected final PatientDimensionsTo unmarshalPatients() throws JAXBException,
			IOException
	{
		return xmlService.unmarshalResource(FILE, PatientDimensionsToImpl.class);
	}

	/**
	 * Assert a number of table rows.
	 * 
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("boxing")
	protected final void assertNumTableRowsEquals(final String tableName,
			final int expected)
	{
		assertThat(countRowsInTable(tableName), is(expected));
	}

	/**
	 * Private helper method to count rows in table.
	 * 
	 * @param tableName
	 * @return
	 */
	protected final int countRowsInTable(final String tableName)
	{
		return jdbcTemplate.queryForInt("SELECT COUNT(0) FROM " + tableName);
	}
}
