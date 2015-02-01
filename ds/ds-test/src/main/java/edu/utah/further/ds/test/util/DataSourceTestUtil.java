/*******************************************************************************
 * Source File: DataSourceTestUtil.java
 ******************************************************************************/
package edu.utah.further.ds.test.util;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * Data source XQuery testing framework - utilities.
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
public final class DataSourceTestUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * TODO: Change PATIENT_DIMENSIONS when fqe-model becomes analytical model.
	 */
	private static final String PATIENT_COUNT_QUERY = "SELECT COUNT(*) FROM PATIENT_DIMENSION";

	/**
	 * Query for selecting all patients from the link table
	 */
	private static final String PATIENT_LINK_COUNT_QUERY = "SELECT COUNT(*) FROM PERSON_MAP";

	/**
	 * Deletes all patients from the virtual repository.
	 */
	private static final String PATIENT_CLEAN_UP_QUERY = "DELETE FROM PATIENT_DIMENSION";
	
	/**
	 * Deletes all patients from the virtual repository.
	 */
	private static final String OBSERVATION_FACT_CLEAN_UP_QUERY = "DELETE FROM OBSERVATION_FACT";

	/**
	 * Deletes all patients from the link table.
	 */
	private static final String PATIENT_LINK_CLEAN_UP_QUERY = "DELETE FROM PERSON_MAP";

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in a singleton class.
	 * </p>
	 */
	private DataSourceTestUtil()
	{
		super();
	}

	// ========================= PUBLIC STATIC METHODS =====================

	/**
	 * @param jdbcTemplate
	 * @return
	 */
	public static long getPatientCountByPatientTable(final SimpleJdbcTemplate jdbcTemplate)
	{
		return jdbcTemplate.queryForLong(PATIENT_COUNT_QUERY);
	}

	/**
	 * @param jdbcTemplate
	 * @return
	 */
	public static long getPatientCountByLinkTable(final SimpleJdbcTemplate jdbcTemplate)
	{
		return jdbcTemplate.queryForLong(PATIENT_LINK_COUNT_QUERY);
	}

	/**
	 * Deletes saved entities after test, asserts that zero entities exist.
	 * 
	 * @param jdbcTemplate
	 */
	public static void cleanUpDataSourceTest(final SimpleJdbcTemplate jdbcTemplate)
	{
		jdbcTemplate.update(PATIENT_CLEAN_UP_QUERY);
		jdbcTemplate.update(PATIENT_LINK_CLEAN_UP_QUERY);
		assertClean(jdbcTemplate, 0);
	}
	
	/**
	 * Deletes saved entities after test, asserts that zero entities exist.
	 * 
	 * @param jdbcTemplate
	 */
	public static void cleanUpI2b2DataSourceTest(final SimpleJdbcTemplate jdbcTemplate)
	{
		jdbcTemplate.update(OBSERVATION_FACT_CLEAN_UP_QUERY);
		jdbcTemplate.update(PATIENT_CLEAN_UP_QUERY);
		jdbcTemplate.update(PATIENT_LINK_CLEAN_UP_QUERY);
		assertClean(jdbcTemplate, 0);
	}

	// ========================= PRIVATE METHODS ===================================

	/**
	 * @param jdbcTemplate
	 * @param expectedNumResultsi
	 */
	private static void assertClean(final SimpleJdbcTemplate jdbcTemplate,
			final long expectedNumResults)
	{
		assertThat(new Long(getPatientCountByPatientTable(jdbcTemplate)),
				greaterThanOrEqualTo(new Long(expectedNumResults)));

		assertThat(new Long(getPatientCountByLinkTable(jdbcTemplate)),
				greaterThanOrEqualTo(new Long(expectedNumResults)));
	}
}
