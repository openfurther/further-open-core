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
package edu.utah.further.core.api.xml;

import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

/**
 * Centralizes JAXB XML utilities.
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
 * @version Sep 15, 2009
 */
public interface XmlService
{
	// ========================= METHODS ===================================

	/**
	 * A factory method of marshalling builder.
	 *
	 * @return empty marshalling options builder instance. Use chaining calls to set
	 *         options
	 */
	MarshallerOptions options();

	/**
	 * @param entity
	 * @param builder
	 * @return
	 */
	String marshal(Object entity) throws JAXBException;

	/**
	 * @param entity
	 * @param builder
	 * @return
	 */
	String marshalAndPrint(Object entity) throws JAXBException;

	/**
	 * @param entity
	 * @param builder
	 * @return
	 */
	String marshal(Object entity, MarshallerOptions builder) throws JAXBException;

	/**
	 * @param entity
	 * @param result
	 * @param builder
	 */
	void marshal(Object entity, Object result, MarshallerOptions builder)
			throws JAXBException;

	/**
	 * Unmarshal a classpath resource into a JAXB-annotated Java object graph using JAXB.
	 * The input stream is closed at the end of this method. Uses an empty context
	 * configuration.
	 *
	 * @param <T>
	 *            Root Java object type
	 * @param inputSource
	 *            an XML input source
	 * @param jaxbPackage
	 *            Java package in which the root Java object exists
	 * @return Java object graph
	 * @throws JAXBException
	 *             upon failure to unmarshal the input source
	 * @throws IOException
	 *             if classpath resource cannot be opened
	 */
	<T> T unmarshalResource(String resourceName, Class<T> rootClass) throws JAXBException,
			IOException;

	/**
	 * Unmarshal a classpath resource into a JAXB-annotated Java object graph using JAXB.
	 * The input stream is closed at the end of this method. Uses an empty context
	 * configuration.
	 *
	 * @param <T>
	 *            Root Java object type
	 * @param inputSource
	 *            an XML input source
	 * @param jaxbPackage
	 *            Java package in which the root Java object exists
	 * @return Java object graph
	 * @throws JAXBException
	 *             upon failure to unmarshal the input source
	 * @throws IOException
	 *             if classpath resource cannot be opened
	 */
	<T> T unmarshalResource(String resourceName, MarshallerOptions builder) throws JAXBException,
			IOException;

	/**
	 * Unmarshal an input source into a JAXB-annotated Java object graph using JAXB. The
	 * input stream is closed at the end of this method. Uses an empty context
	 * configuration.
	 *
	 * @param <T>
	 *            Root Java object type
	 * @param input
	 *            an XML input source
	 * @param jaxbPackage
	 *            Java package in which the root Java object exists
	 * @return Java object graph
	 * @throws JAXBException
	 *             upon failure to unmarshal the input source
	 * @throws IOException
	 *             if classpath resource cannot be opened
	 */
	<T> T unmarshal(Object input, Class<?> rootClass) throws JAXBException;

	/**
	 * Unmarshal an input source into a JAXB-annotated Java object graph using JAXB. The
	 * input stream is closed at the end of this method. Uses an empty context
	 * configuration.
	 *
	 * @param <T>
	 *            Root Java object type
	 * @param input
	 *            an XML input source
	 * @param jaxbPackage
	 *            Java package in which the root Java object exists
	 * @return Java object graph
	 * @throws JAXBException
	 *             upon failure to unmarshal the input source
	 * @throws IOException
	 *             if classpath resource cannot be opened
	 */
	<T> T unmarshal(Object input, MarshallerOptions builder) throws JAXBException;

	/**
	 * @return
	 */
	Map<String, Object> getDefaultJaxbConfig();

	/**
	 * @param defaultJaxbConfig
	 */
	void setDefaultJaxbConfig(Map<String, Object> defaultJaxbConfig);
}