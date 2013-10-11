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
package edu.utah.further.ds.further.model.impl.domain;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.test.xml.IgnoreNamedElementsDifferenceListener;
import edu.utah.further.core.util.io.IoUtil;

/**
 * Unit test for marshalling a person transfer object
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
 * @version Aug 5, 2013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/fqe-model-test-annotation-context.xml" })
public class UTestMarshalPersonTo
{
	@Autowired
	private XmlService xmlService;

	/**
	 * Setup for tests
	 */
	@Before
	public void setup()
	{
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
	}

	/**
	 * Marshal a {@link PersonTo}
	 * 
	 * @throws JAXBException
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void marshalPersonTo() throws JAXBException, SAXException, IOException
	{
		final Person person = new Person();
		person.setAdministrativeGender("12345");
		person.setAdministrativeGenderNamespaceId(new Long(30));
		person.setBirthDay(new Long(1));
		person.setBirthMonth(new Long(1));
		person.setBirthYear(new Long(1980));
		person.setCauseOfDeath("12345");
		person.setCauseOfDeathNamespaceId(new Long(30));
		person.setDateOfBirth(new Date());
		person.setDateOfDeath(new Date());
		person.setDeathYear(new Long(2013));
		person.setEducationLevel("High");
		person.setEthnicity("Caucasian");
		person.setEthnicityNamespaceId(new Long(30));
		person.setMaritalStatus("12345");
		person.setMaritalStatusNamespaceId(new Long(30));
		person.setMultipleBirthIndicator(Boolean.TRUE);
		person.setMultipleBirthIndicatorOrderNumber(new Integer(1));
		person.setPedigreeQuality(new Long(1));
		person.setCompositeId("12345");
		person.setId(new PersonId(new Long(1), "abcdefghijklmnopqrstuvwxyz"));
		person.setPrimaryLanguage("12345");
		person.setPrimaryLanguageNamespaceId(new Long(30));
		person.setRace("12345");
		person.setRaceNamespaceId(new Long(30));
		person.setReligion("12345");
		person.setReligionNamespaceId(new Long(30));

		final String marshalled = xmlService.marshal(person);
		final Diff diff = new Diff(
				IoUtil.getResourceAsString("query/marshalled-example.xml"), marshalled);
		diff.overrideDifferenceListener(new IgnoreNamedElementsDifferenceListener(Arrays
				.asList("dateOfBirth", "dateOfDeath")));
		assertTrue(diff.similar());

	}
}
