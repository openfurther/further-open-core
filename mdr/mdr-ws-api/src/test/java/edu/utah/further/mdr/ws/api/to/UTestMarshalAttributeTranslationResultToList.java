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
package edu.utah.further.mdr.ws.api.to;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;
import edu.utah.further.mdr.api.to.asset.AttributeTranslationResultTo;

/**
 * Tests marshalling of an {@link AttributeTranslationResultToList}.
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
 * @version May 22, 2013
 */
public class UTestMarshalAttributeTranslationResultToList
{
	@Test
	public void marshalAttributeTranslationResultToList() throws JAXBException
	{
		final Map<String, String> properties = new HashMap<>();
		properties.put("ATTR_JAVA_TYPE", "java.lang.Long");

		final AttributeTranslationResultTo translationResultOne = new AttributeTranslationResultTo();
		translationResultOne.setTranslatedAttribute("id.code");
		translationResultOne.setAttributeProperties(properties);

		final AttributeTranslationResultTo translationResultTwo = new AttributeTranslationResultTo();
		translationResultTwo.setTranslatedAttribute("id.obsvtnStudyCodeDwid");
		translationResultTwo.setAttributeProperties(properties);

		final List<AttributeTranslationResultTo> attributeTranslations = new ArrayList<>();
		attributeTranslations.add(translationResultOne);
		attributeTranslations.add(translationResultTwo);

		final AttributeTranslationResultToList translationResultList = new AttributeTranslationResultToList(
				attributeTranslations);

		final XmlService xmlService = new XmlServiceImpl();
		final String result = xmlService.marshal(translationResultList);
		
		System.out.println(result);
		
//		assertThat(result, containsString("attributeTranslationResultList>"));
//		assertThat(result, containsString("<assetAssociation>"));
//		assertThat(result, containsString("<leftType>"));
//		assertThat(result, containsString("<rightType>"));
//		assertThat(result, containsString("<properties>"));
//		assertThat(result, containsString("<key "));
//		assertThat(result, containsString("<value "));

	}
}
