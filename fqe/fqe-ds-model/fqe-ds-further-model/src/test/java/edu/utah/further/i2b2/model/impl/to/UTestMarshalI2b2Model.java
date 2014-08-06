/*******************************************************************************
s * Source File: UTestMarshalI2b2Model.java
 ******************************************************************************/
package edu.utah.further.i2b2.model.impl.to;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Ignore;
import org.junit.Test;

import edu.utah.further.ds.i2b2.model.api.to.ObservationFactsTo;
import edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo;
import edu.utah.further.ds.i2b2.model.api.to.PatientDimensionsTo;
import edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo;
import edu.utah.further.ds.i2b2.model.impl.to.PatientDimensionsToImpl;
import edu.utah.further.i2b2.model.impl.fixture.I2b2ModelImplFixture;

/**
 * Marshalling tests of the i2b2 model
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
public final class UTestMarshalI2b2Model extends I2b2ModelImplFixture
{
	/**
	 * Unmarshalls an i2b2 model and then marshals it and compares.
	 *
	 * @throws IOException
	 * @throws JAXBException
	 *
	 * @throws XmlMappingException
	 * @throws IOException
	 */
	@Test
	public void marshal() throws JAXBException, IOException
	{
		// Use impl for JAXB
		final PatientDimensionsTo patientDimensionsTo = unmarshalPatients();

		// Marshal output to stream
		final String marshalled = xmlService.marshal(patientDimensionsTo);

		// Use stream to unmarshal
		final PatientDimensionsTo patientDimensionsTo2 = xmlService.unmarshal(
				new ByteArrayInputStream(marshalled.getBytes()),
				PatientDimensionsToImpl.class);

		// If marshalling was done correctly then they should be equal. (Note, depends on
		// proper equals implementations)
		assertThat(patientDimensionsTo, is(patientDimensionsTo2));
	}

	/**
	 * Unmarshalls an i2b2 model and tests that it was properly unmarshalled
	 *
	 * @throws IOException
	 * @throws JAXBException
	 *
	 * @throws XmlMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("boxing")
	@Test
	public void unmarshal() throws JAXBException, IOException
	{
		final PatientDimensionsTo patientDimensionsTo = unmarshalPatients();
		assertThat(patientDimensionsTo, notNullValue());

		final List<PatientDimensionTo> patients = patientDimensionsTo
				.getPatientDimension();
		assertThat(patients, notNullValue());

		// Only 1 patient in this example
		final PatientDimensionTo patient = patients.get(0);
		assertThat(patient, notNullValue());
		assertThat(patient.getAgeInYearsNum(), is("111"));

		// Only 1 visit in this example
		final VisitDimensionTo visit = patient
				.getVisitDimensions()
				.getVisitDimension()
				.get(0);

		assertThat(visit, notNullValue());
		assertThat(visit.getVisitDimensionPK().getPatientNum(), is("14173714"));

		// 3 observations in this example;
		final ObservationFactsTo observations = patient.getObservationFacts();
		assertThat(observations, notNullValue());
		assertThat(observations.getObservationFact().size(), is(3));
	}
}
