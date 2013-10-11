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
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;

/**
 * Unmarshal a result list and ensure that the type of the List is of type {@link Person}
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
 * @version Oct 10, 2013
 */
public class UTestUnmarshalResultList
{
	@Test
	public void unmarshalResultList() throws IOException, JAXBException
	{
		final XmlService xmlService = new XmlServiceImpl();
		final Resource input = new ClassPathResource("expected.xml");
		try (final InputStream unmarshalInputStream = input.getInputStream())
		{
			final ResultList resultList = xmlService.unmarshal(unmarshalInputStream, ResultList.class);
			final List<?> results = resultList.getResultList();
			assertTrue(Person.class.isAssignableFrom(results.get(0).getClass()));
		}
		finally
		{

		}
	}
}
