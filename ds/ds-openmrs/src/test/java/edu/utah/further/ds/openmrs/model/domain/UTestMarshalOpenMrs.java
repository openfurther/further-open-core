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
package edu.utah.further.ds.openmrs.model.domain;

import java.util.Date;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;

/**
 * Test marshalling the output of a patient in OpenMRS
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
 * @version Aug 14, 2013
 */
public class UTestMarshalOpenMrs
{
	@Test
	public void marshalPatient() throws JAXBException
	{
		final Person person = new Person();
		person.setBirthdate(new Date());
		person.setBirthdateEstimated(Byte.MAX_VALUE);
		person.setCauseOfDeath(1);
		person.setChangedBy(1);
		person.setCreator(1);
		person.setDateChanged(new Date());
		person.setDateCreated(new Date());
		person.setDateVoided(new Date());
		person.setDead(Byte.MAX_VALUE);
		person.setDeathDate(new Date());
		person.setGender("M");
		person.setId(new Integer(1));
		person.setUuid("uuid");
		person.setVoided(Byte.MAX_VALUE);
		person.setVoidedBy(1);
		person.setVoidReason("reason");

		final Patient patient = new Patient();
		patient.setPerson(person);
		patient.setChangedBy(1);
		patient.setCreator(1);
		patient.setDateChanged(new Date());
		patient.setDateCreated(new Date());
		patient.setDateVoided(new Date());
		patient.setId(new Integer(1));
		patient.setTribe(1);
		patient.setVoided(Byte.MAX_VALUE);
		patient.setVoidedBy(1);
		patient.setVoidReason("reason");

		final XmlService service = new XmlServiceImpl();
		final String result = service.marshal(patient);
		System.out.println(result);
	}
}
