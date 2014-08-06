/*******************************************************************************
 * Source File: UTestPersistI2b2Model.java
 ******************************************************************************/
package edu.utah.further.i2b2.model.impl.domain;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.utah.further.ds.i2b2.model.api.to.PatientDimensionsTo;
import edu.utah.further.ds.i2b2.model.impl.domain.PatientDimensionEntity;
import edu.utah.further.i2b2.model.impl.fixture.I2b2ModelImplFixture;

/**
 * Test persisting I2b2 entities to a database.
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
 * @version May 6, 2010
 */
public final class UTestPersistI2b2Model extends I2b2ModelImplFixture
{
	// ========================= DEPENDENCIES ==============================

	/**
	 * The patient dimension to persist
	 */
	private PatientDimensionEntity patientDimension;

	// ========================= SETUP METHODS =============================

	/**
	 * Setup for test
	 *
	 * @throws IOException
	 * @throws JAXBException
	 *
	 * @throws IOException
	 * @throws XmlMappingException
	 */
	@Before
	public void setup() throws JAXBException, IOException
	{
		final PatientDimensionsTo patientDimensionsTo = unmarshalPatients();
		patientDimension = (PatientDimensionEntity) patientDimensionsTo
				.getPatientDimension()
				.get(0)
				.copy();
	}

	/**
	 * Delete all patients from the database.
	 */
	@After
	public void tearDown()
	{
		dao.deleteAll(PatientDimensionEntity.class);
		assertNumTableRowsEquals("PATIENT_DIMENSION", 0);
	}

	// ========================= METHODS ===================================

	/**
	 * Persist a patient
	 */
	@Test
	public void persist()
	{
		dao.save(patientDimension);
		assertNumTableRowsEquals("PATIENT_DIMENSION", 1);
	}
}
