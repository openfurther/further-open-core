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
package edu.utah.further.ds.omop.model.v4.domain;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;
import edu.utah.further.ds.omop.model.v4.domain.ConditionEra;
import edu.utah.further.ds.omop.model.v4.domain.ConditionOccurrence;
import edu.utah.further.ds.omop.model.v4.domain.Death;
import edu.utah.further.ds.omop.model.v4.domain.DrugEra;
import edu.utah.further.ds.omop.model.v4.domain.DrugExposure;
import edu.utah.further.ds.omop.model.v4.domain.Location;
import edu.utah.further.ds.omop.model.v4.domain.Observation;
import edu.utah.further.ds.omop.model.v4.domain.PayerPlanPeriod;
import edu.utah.further.ds.omop.model.v4.domain.Person;
import edu.utah.further.ds.omop.model.v4.domain.ProcedureOccurrence;
import edu.utah.further.ds.omop.model.v4.domain.Provider;
import edu.utah.further.ds.omop.model.v4.domain.VisitOccurrence;

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
 * @version Apr 22, 2013
 */
@UnitTest
public class UTestMarshalOmopV4
{
	@Test
	public void marshalPerson() throws JAXBException
	{
		final Death death = new Death();
		death.setId(new Long(1));
		
		final Provider provider = new Provider();
		provider.setId(new Long(1));
		
		final Location location = new Location();
		location.setId(new Long(1));
		
		
		final Person person = new Person();
		person.setId(new Long(1));
		person.setCareSiteId(new BigDecimal(1));
		person.setDayOfBirth(new BigDecimal(1));
		person.setEthnicityConceptId(new BigDecimal(1));
		person.setEthnicitySourceValue("Blue");
		person.setGenderConceptId(new BigDecimal(1));
		person.setGenderSourceValue("Green");
		person.setMonthOfBirth(new BigDecimal(1));
		person.setPersonSourceValue("Moon");
		person.setRaceConceptId(new BigDecimal(1));
		person.setRaceSourceValue("Race");
		person.setConditionEras(CollectionUtil.<ConditionEra> newList());
		person.setConditionOccurrences(CollectionUtil
				.<ConditionOccurrence> newList());
		person.setDeath(death);
		person.setDrugEras(CollectionUtil.<DrugEra> newList());
		person.setDrugExposures(CollectionUtil.<DrugExposure> newList());
		person.setObservations(CollectionUtil.<Observation> newList());
		person.setPayerPlanPeriods(CollectionUtil.<PayerPlanPeriod> newList());
		person.setLocation(location);
		person.setProvider(provider);
		person.setProcedureOccurrences(CollectionUtil
				.<ProcedureOccurrence> newList());
		person.setVisitOccurrences(CollectionUtil.<VisitOccurrence> newList());
		
		final XmlService service = new XmlServiceImpl();
		final String result = service.marshal(person);
		
		assertThat(result, containsString("<person"));
		assertThat(result, containsString("</person>"));
		assertThat(result, containsString("<provider>"));
		assertThat(result, containsString("</provider>"));
		assertThat(result, containsString("<death>"));
		assertThat(result, containsString("</death>"));
		assertThat(result, containsString("Moon"));
		assertThat(result, containsString("Blue"));
		assertThat(result, containsString("Green"));
	}
}
