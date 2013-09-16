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
package edu.utah.further.ds.omop.model.v2.domain;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;
import edu.utah.further.ds.omop.model.v2.domain.ConditionEra;
import edu.utah.further.ds.omop.model.v2.domain.ConditionOccurrence;
import edu.utah.further.ds.omop.model.v2.domain.DrugEra;
import edu.utah.further.ds.omop.model.v2.domain.DrugExposure;
import edu.utah.further.ds.omop.model.v2.domain.Observation;
import edu.utah.further.ds.omop.model.v2.domain.ObservationPeriod;
import edu.utah.further.ds.omop.model.v2.domain.Person;
import edu.utah.further.ds.omop.model.v2.domain.ProcedureOccurrence;
import edu.utah.further.ds.omop.model.v2.domain.VisitOccurrence;

/**
 * ...
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
 * @version Apr 24, 2013
 */
@UnitTest
public class UTestMarshallOmopV2
{
	@Test
	public void marshalPerson() throws JAXBException
	{

		final Person person = new Person();
		person.setId(new Long(1));
		person.setGenderConceptId(new BigDecimal(1));
		person.setLocationConceptId(new BigDecimal(1));
		person.setRaceConceptId(new BigDecimal(1));
		person.setSourceGenderCode("Male");
		person.setSourceLocationCode("Moon");
		person.setSourceRaceCode("Blue");
		person.setSourcePersonKey("key");
		person.setYearOfBirth(new BigDecimal(2013));
		person.setConditionEras(CollectionUtil.<ConditionEra> newList());
		person.setConditionOccurrences(CollectionUtil.<ConditionOccurrence> newList());
		person.setDrugEras(CollectionUtil.<DrugEra> newList());
		person.setDrugExposures(CollectionUtil.<DrugExposure> newList());
		person.setObservations(CollectionUtil.<Observation> newList());
		person.setObservationPeriods(CollectionUtil.<ObservationPeriod> newList());
		person.setProcedureOccurrences(CollectionUtil.<ProcedureOccurrence> newList());
		person.setVisitOccurrences(CollectionUtil.<VisitOccurrence> newList());

		final XmlService service = new XmlServiceImpl();
		final String result = service.marshal(person);

		assertThat(result, containsString("<Person"));
		assertThat(result, containsString("</Person>"));
		assertThat(result, containsString("Male"));
		assertThat(result, containsString("Moon"));
		assertThat(result, containsString("Blue"));
		assertThat(result, containsString("key"));
		assertThat(result, containsString("2013"));
	}
}
