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
package edu.utah.further.core.xml.transform;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;

/**
 * Resolves resources on the classpath using Spring's Resource framework. Use this with a
 * {@link TransformerFactory} to resolve xsl:import and xsl:includes on the classpath.
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
 * @version Dec 8, 2009
 */
public final class SpringClasspathUriResolver implements URIResolver
{
	// ========================= IMPLEMENTATION: URIResolver ===============

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.transform.URIResolver#resolve(java.lang.String, java.lang.String)
	 */
	@Override
	public Source resolve(final String href, final String base) throws TransformerException
	{
		final ClassPathResource classPathResource = new ClassPathResource(href);
		try
		{
			return new StreamSource(classPathResource.getInputStream());
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
