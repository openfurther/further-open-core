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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.xml.XmlService;

/**
 * Unmarshalling tests for {@link Person}
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/fqe-model-test-annotation-context.xml" })
public class UTestUnmarshalPersonTo
{
	@Autowired
	private XmlService xmlService;

	/**
	 * Test unmarsahlling a {@link Person} into an object.
	 * 
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public void unmarshalPerson() throws JAXBException, IOException
	{
		final Resource resource = new ClassPathResource("query/marshalled-example.xml");
		try (final InputStream xmlInputStream = resource.getInputStream())
		{
			final Person person = xmlService.unmarshal(xmlInputStream, Person.class);
			assertNotNull(person);

			assertThat(person.getId(), notNullValue());
			assertThat(person.getId().getDatasetId(), is("abcdefghijklmnopqrstuvwxyz"));
			assertThat(person.getId().getId(), is(new Long(1)));
			assertThat(person.getMaritalStatus(), is("12345"));
			assertThat(person.getErrors(), notNullValue());
		}
		finally
		{

		}
	}
}
