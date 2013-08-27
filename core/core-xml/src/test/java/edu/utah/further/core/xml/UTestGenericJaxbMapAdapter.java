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
package edu.utah.further.core.xml;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.xml.GenericJaxbMapAdapter;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;

/**
 * Unit test for {@link GenericJaxbMapAdapter}
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
 * @version May 6, 2013
 */
public class UTestGenericJaxbMapAdapter
{
	@Test
	public void marshallTransferObjectWithMap() throws JAXBException
	{
		final Map<String, String> map = CollectionUtil.newMap();
		map.put("1", "One");
		map.put("2", "Two");
		final TransferObjectWithMap transferObjectWithMap = new TransferObjectWithMap();
		transferObjectWithMap.setProperties(map);

		final XmlService xmlService = new XmlServiceImpl();
		final String marshalled = xmlService.marshal(transferObjectWithMap);
		assertThat(new Integer(StringUtils.countMatches(marshalled, "<entry>")),
				is(new Integer(2)));
		assertThat(new Integer(StringUtils.countMatches(marshalled, "<key")),
				is(new Integer(2)));
		assertThat(new Integer(StringUtils.countMatches(marshalled, "<value")),
				is(new Integer(2)));
		assertThat(marshalled, containsString("One"));
		assertThat(marshalled, containsString("Two"));
	}

	@Test
	public void unmarshallTransferObjectWithMap() throws JAXBException, IOException
	{
		final XmlService xmlService = new XmlServiceImpl();
		final TransferObjectWithMap transferObjectWithMap = xmlService.unmarshalResource(
				"transfer-object-with-map.xml", TransferObjectWithMap.class);
		assertNotNull(transferObjectWithMap);
		assertTrue(transferObjectWithMap.getProperties().containsKey("1"));
		assertTrue(transferObjectWithMap.getProperties().containsKey("2"));
		assertTrue(transferObjectWithMap.getProperties().containsValue("One"));
		assertTrue(transferObjectWithMap.getProperties().containsValue("Two"));
	}
}
