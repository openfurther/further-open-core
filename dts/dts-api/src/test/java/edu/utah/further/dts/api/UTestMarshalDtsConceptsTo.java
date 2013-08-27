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
package edu.utah.further.dts.api;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptsTo;

/**
 * Marshals a DtsConcepts Transfer Object
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 22, 2010
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/dts-api-test-context.xml" }, inheritLocations = false)
public class UTestMarshalDtsConceptsTo
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestMarshalDtsConceptsTo.class);

	/**
	 * Marshalling/Unmarshalling service
	 */
	@Autowired
	private XmlService xmlService;

	/**
	 * Marshal a {@link DtsConceptsTo}
	 * 
	 * @throws JAXBException
	 */
	@Test
	@Ignore("Ignored until multiple property name values are supported")
	public void marshalDtsConceptsTo() throws JAXBException
	{
		final DtsConceptId one = new DtsConceptId();
		one.setNamespace("SNOMED CT");
		one.setPropertyName("Code in Source");
		one.setPropertyValue("12345");

		final DtsConceptId two = new DtsConceptId();
		two.setNamespace("SNOMED CT");
		two.setPropertyName("Code in Source");
		two.setPropertyValue("67890");

		final List<DtsConceptId> conceptIds = CollectionUtil.newList();
		conceptIds.add(one);
		conceptIds.add(two);

		final DtsConceptsTo conceptsTo = new DtsConceptsTo(conceptIds);

		log.debug(xmlService.marshal(conceptsTo));
	}
}
