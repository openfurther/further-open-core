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
package edu.utah.further.core.xml.jaxb;

import java.util.Collections;
import java.util.Map;

import com.sun.xml.bind.api.JAXBRIContext;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * Some pre-cooked, useful JAXB configurations.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jun 15, 2010
 */
public enum JaxbConfig
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * An empty JAXB context configuration.
	 */
	EMPTY(Collections.<String, Object> emptyMap()),

	/**
	 * A custom JAXB context for standard FURTHeR module use.
	 */
	FURTHER(createDefaultJaxbConfig()),

	/**
	 * A custom JAXB context configuration with an introduction that can ignore some
	 * methods and fields via a runtime configuration so that exception classes can be
	 * marshalled.
	 */
	TRANSIENT_ANNOTATION_READER(createTransientAnnotationReaderJaxbConfig());

	// ========================= FIELDS =======================================

	/**
	 * Cached JAXB configuration for this enumerated type.
	 */
	private final Map<String, Object> jaxbConfig;

	// ========================= CONSTRUCTORS =================================

	/**
	 * Construct a JAXB configuration template.
	 *
	 * @param jaxbConfig
	 *            configuration object cached by this template
	 */
	private JaxbConfig(final Map<String, Object> jaxbConfig)
	{
		this.jaxbConfig = jaxbConfig;
	}

	// ========================= IMPL: Named ==================================

	/**
	 * Return the jaxbConfig property.
	 *
	 * @return the jaxbConfig
	 */
	public Map<String, Object> getJaxbConfig()
	{
		// Defensive copy, for immutability
		return CollectionUtil.newMap(jaxbConfig);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * A custom JAXB context for standard FURTHeR module use. Supports exception handling.
	 *
	 * @return custom JAXB context for standard FURTHeR module use
	 */
	private static final Map<String, Object> createDefaultJaxbConfig()
	{
		final Map<String, Object> jaxbConfig = createTransientAnnotationReaderJaxbConfig();
		jaxbConfig.put(JAXBRIContext.DEFAULT_NAMESPACE_REMAP, XmlNamespace.BASE);
		return jaxbConfig;
	}

	/**
	 * Create a custom JAXB context configuration with an introduction that can ignore
	 * some methods and fields via a runtime configuration so that exception classes can
	 * be marshaled.
	 *
	 * @return custom JAXB context configuration that can marshal exceptions
	 */
	private static Map<String, Object> createTransientAnnotationReaderJaxbConfig()
	{
		// Initialize our custom reader
		final TransientAnnotationReader reader = new TransientAnnotationReader();
		try
		{
			reader.addTransientField(Throwable.class.getDeclaredField("stackTrace"));
			reader.addTransientMethod(Throwable.class.getDeclaredMethod("getStackTrace"));
		}
		catch (final Exception ignored)
		{
		}

		// Set properties
		final Map<String, Object> jaxbConfig = CollectionUtil.newMap();
		jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, reader);
		return jaxbConfig;
	}
}
