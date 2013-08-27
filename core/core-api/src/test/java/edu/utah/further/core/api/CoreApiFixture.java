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
package edu.utah.further.core.api;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Common test methods for core-api tests.
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
 * @version Jul 19, 2013
 */
public class CoreApiFixture
{
	/**
	 * @param entity
	 * @return
	 * @throws JAXBException
	 */
	protected String testMarshalling(final Object entity, final String pkg) throws JAXBException
	{
		final JAXBContext jaxbContext = JAXBContext.newInstance(pkg);
		final Marshaller m = jaxbContext.createMarshaller();
		final String s = marshal(m, entity);
		assertNotNull(s);
		return s;
	}

	/**
	 * Marshal a JAXB-annotated Java object graph into an XML string.
	 * 
	 * @param entity
	 *            entity to marshal
	 * @return entity XML representation string
	 */
	protected String marshal(final Marshaller m, final Object entity)
	{
		try (final OutputStream os = new ByteArrayOutputStream())
		{
			m.marshal(entity, os);
			final String s = os.toString();
			return s;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
