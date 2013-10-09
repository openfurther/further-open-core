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
package edu.utah.further.ds.api.results;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;

/**
 * Marshalling tests related to {@link ResultList}
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
 * @version Oct 9, 2013
 */
public class UTestMarshalResultList
{
	@Before
	public void setup()
	{
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreComments(true);
	}

	/**
	 * Marshal a list of test objects to ensure they are wrapped with ResultList.
	 * 
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 */
	@Test
	public void marshalResult() throws JAXBException, SAXException, IOException
	{
		final XmlService xmlService = new XmlServiceImpl();
		final Person one = new Person("John Doe", 20);
		final Person two = new Person("Jane Doe", 30);
		final List<Person> persons = new ArrayList<>();
		persons.add(one);
		persons.add(two);
		final ResultList resultList = new ResultList(persons);
		final String result = xmlService.marshal(resultList);
		final Diff diff = new Diff(IoUtil.getResourceAsString("expected.xml"), result);
		assertTrue(diff.similar());
	}
}
