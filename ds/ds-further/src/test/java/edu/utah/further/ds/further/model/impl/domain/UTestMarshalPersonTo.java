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

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.Date;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.ds.further.model.impl.domain.Person;
import edu.utah.further.ds.further.model.impl.domain.PersonId;

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
	 * Marshal a {@link PersonTo}
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void marshalPersonTo() throws JAXBException
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
		person.setId(new PersonId(new Long(1), new Long(1)));
		person.setPrimaryLanguage("12345");
		person.setPrimaryLanguageNamespaceId(new Long(30));
		person.setRace("12345");
		person.setRaceNamespaceId(new Long(30));
		person.setReligion("12345");
		person.setReligionNamespaceId(new Long(30));

		final String marshalled = xmlService.marshal(person);
		assertThat(marshalled, containsString("administrativeGender"));
		assertThat(marshalled, containsString("administrativeGenderNamespaceId"));
		assertThat(marshalled, containsString("birthDay"));
		assertThat(marshalled, containsString("birthYear"));
		assertThat(marshalled, containsString("causeOfDeath"));
		assertThat(marshalled, containsString("causeOfDeathNamespaceId"));
		assertThat(marshalled, containsString("dateOfBirth"));
		assertThat(marshalled, containsString("dateOfDeath"));
		assertThat(marshalled, containsString("educationLevel"));
		assertThat(marshalled, containsString("ethnicity"));
		assertThat(marshalled, containsString("ethnicityNamespaceId"));
		assertThat(marshalled, containsString("maritalStatus"));
		assertThat(marshalled, containsString("maritalStatusNamespaceId"));
		assertThat(marshalled, containsString("multipleBirthIndicator"));
		assertThat(marshalled, containsString("multipleBirthIndicatorOrderNumber"));
		assertThat(marshalled, containsString("pedigreeQuality"));
		assertThat(marshalled, containsString("compositeId"));
		assertThat(marshalled, containsString("id"));
		assertThat(marshalled, containsString("datasetId"));
		assertThat(marshalled, containsString("primaryLanguage"));
		assertThat(marshalled, containsString("primaryLanguageNamespaceId"));
		assertThat(marshalled, containsString("race"));
		assertThat(marshalled, containsString("raceNamespaceId"));
		assertThat(marshalled, containsString("religion"));
		assertThat(marshalled, containsString("religionNamespaceId"));

	}
}
