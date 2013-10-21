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
package edu.utah.further.ds.openmrs.model.v1_9.domain;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.utah.further.core.api.lang.CoreUtil;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.test.xml.IgnoreNamedElementsDifferenceListener;
import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;
import edu.utah.further.ds.openmrs.model.v1_9.domain.Person;
import edu.utah.further.ds.openmrs.model.v1_9.domain.PersonAttribute;

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
public class UTestMarshalOpenMrsV1_9
{

	@Before
	public void setup()
	{
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
	}

	@Test
	public void marshalPatient() throws JAXBException, SAXException, IOException
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

		final PersonAttribute personAttribute = new PersonAttribute();
		personAttribute.setCreator(1);
		personAttribute.setPersonAttributeType(new Long(1));
		personAttribute.setValue("Caucasian");
		personAttribute.setPerson(person);
		personAttribute.setVoided((byte) 0);
		personAttribute.setUuid(UUID.randomUUID().toString());

		person.addPersonAttribute(personAttribute);

		final XmlService service = new XmlServiceImpl();
		final String result = service.marshal(person);
		final String expected = IoUtil.getInputStreamAsString(CoreUtil
				.getResourceAsStream("result/result-1.9.xml"));
		final DetailedDiff diff = new DetailedDiff(new Diff(expected, result));
		diff.overrideDifferenceListener(new IgnoreNamedElementsDifferenceListener(Arrays
				.asList("birthdate", "dateChanged", "dateCreated", "dateVoided",
						"deathDate", "uuid")));
		assertTrue("XML is different" + diff.getAllDifferences(), diff.similar());
	}
}
