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

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.UUID;

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

		final PersonAttributeType personAttributeType = new PersonAttributeType();
		personAttributeType.setName("Race");
		personAttributeType
				.setDescription("Group of persons related by common descent or heredity");
		personAttributeType.setFormat("java.lang.String");
		personAttributeType.setSearchable((byte) 0);
		personAttributeType.setCreator(1);
		personAttributeType.setDateCreated(new Date());
		personAttributeType.setRetired((byte) 0);
		personAttributeType.setUuid(UUID.randomUUID().toString());
		personAttributeType.setSortWeight(6);

		final PersonAttribute personAttribute = new PersonAttribute();
		personAttribute.setCreator(1);
		personAttribute.setPersonAttributeType(personAttributeType);
		personAttribute.setValue("Caucasian");
		personAttribute.setPerson(person);
		personAttribute.setVoided((byte) 0);
		personAttribute.setUuid(UUID.randomUUID().toString());

		person.addPersonAttribute(personAttribute);

		final XmlService service = new XmlServiceImpl();
		final String result = service.marshal(person);

		assertThat(result, containsString("<Person"));
		assertThat(result, containsString("<personId>"));
		assertThat(result, containsString("<birthdate>"));
		assertThat(result, containsString("<birthdateEstimated>"));
		assertThat(result, containsString("<causeOfDeath>"));
		assertThat(result, containsString("<changedBy>"));
		assertThat(result, containsString("<creator>"));
		assertThat(result, containsString("<dateChanged>"));
		assertThat(result, containsString("<dateCreated>"));
		assertThat(result, containsString("<dateVoided>"));
		assertThat(result, containsString("<dead>"));
		assertThat(result, containsString("<deathDate>"));
		assertThat(result, containsString("<gender>"));
		assertThat(result, containsString("<uuid>"));
		assertThat(result, containsString("<voidReason>"));
		assertThat(result, containsString("<voided>"));
		assertThat(result, containsString("<voidedBy>"));
		assertThat(result, containsString("<voidedBy>"));
		assertThat(result, containsString("<personAttributes>"));
		assertThat(result, containsString("<personAttribute>"));
		assertThat(result, containsString("<personAttributeId>"));
		assertThat(result, containsString("<personAttributeType>"));

	}
}
