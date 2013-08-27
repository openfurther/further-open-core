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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

/**
 * /** An {@link XmlService} helper class that builds the {@link JAXBContext} and JAXB
 * marshaller, unmarshaller.
 * <p>
 * Usage:
 * <ol>
 * <li>First, use the chaining calls and call {@link #buildContext()} to set up a
 * {@link JAXBContext}.</li>
 * <li>the {@link MarshallerOption} can then be reused to set different marshaller
 * properties and marshal using a {@link #marshal()} call or unmarshal using an
 * {@link #unmarshal()} call.</li>
 * </ol>
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
 * @version Jul 19, 2010
 */
public interface MarshallerOptions
{
	// ========================= METHODS ===================================

	/**
	 * Build the {@link JAXBContext} from this object's current state. This class can then
	 * be reused for different marshalling configurations by calling marshalling property
	 * setters and then <code>marshal()</code>.
	 * 
	 * @return this, for method chaining
	 * @throws JAXBException
	 *             if context creation failed
	 */
	MarshallerOptions buildContext() throws JAXBException;

	/**
	 * Overloaded buildContext method that takes a ClassLoader parameter for use in OSGI
	 * environment
	 * 
	 * @param loader
	 * 
	 * @return this, for method chaining
	 * @throws JAXBException
	 *             if context creation failed
	 */
	MarshallerOptions buildContext(ClassLoader loader) throws JAXBException;

	/**
	 * @param pkg
	 * @return
	 */
	MarshallerOptions addPackage(String pkg);

	/**
	 * @param packages
	 * @return
	 */
	MarshallerOptions addPackages(Collection<String> packages);

	/**
	 * @param packages
	 * @return
	 */
	MarshallerOptions addPackages(String... packages);

	/**
	 * @param packages
	 * @return
	 */
	MarshallerOptions setPackages(String... packages);

	/**
	 * @param packages
	 * @return
	 */
	MarshallerOptions setPackages(Set<String> packages);

	/**
	 * @param clazz
	 * @return this, for method chaining
	 */
	MarshallerOptions addClass(Class<?> clazz);

	/**
	 * @param classes
	 * @return
	 * @return this, for method chaining
	 */
	MarshallerOptions addClasses(Class<?>... classes);

	/**
	 * @param classes
	 * @return
	 * @return this, for method chaining
	 */
	MarshallerOptions addClasses(Collection<? extends Class<?>> classes);

	/**
	 * Set a new value for the contextClasses property.
	 * 
	 * @param classes
	 *            the contextClasses to set
	 * @return this, for method chaining
	 */
	MarshallerOptions setClasses(Set<Class<?>> classes);

	/**
	 * Set a new value for the contextClasses property.
	 * 
	 * @param classes
	 *            the contextClasses to set
	 * @return this, for method chaining
	 */
	MarshallerOptions setClasses(Class<?>... classes);

	/**
	 * Set a new value for the jaxbConfig property.
	 * 
	 * @param jaxbConfig
	 *            the jaxbConfig to set
	 * @return this, for method chaining
	 */
	MarshallerOptions setJaxbConfig(Map<String, Object> jaxbConfig);

	/**
	 * Set a new value for the marshallerConfig property.
	 * 
	 * @param marshallerConfig
	 *            the marshallerConfig to set
	 * @return this, for method chaining
	 */
	MarshallerOptions setMarshallerConfig(
			Map<JaxbMarshallerProperty, Object> marshallerConfig);

	/**
	 * Set a new value for the validationSchema property. Passing null into this method
	 * will disable validation.
	 * 
	 * @param validationSchema
	 *            the validationSchema to set
	 * @return this, for method chaining
	 */
	MarshallerOptions setValidationSchema(Schema validationSchema);

	/**
	 * Set a new value for the validationSchema property from an XML input source. Passing
	 * null into this method will disable validation.
	 * 
	 * @param validationSchema
	 *            the validationSchema source
	 * @return this, for method chaining
	 * @throws SAXException
	 */
	MarshallerOptions setValidationSchema(Source validationSchema) throws SAXException;

	/**
	 * Set a new value for the validationSchema property from a list of XML input sources.
	 * Passing null into this method will disable validation.
	 * 
	 * @param validationSchemas
	 *            the validationSchema sources
	 * @return this, for method chaining
	 * @throws SAXException
	 */
	MarshallerOptions setValidationSchema(List<Source> validationSchemas)
			throws SAXException;

	/**
	 * @see edu.utah.further.core.api.xml.XmlService#setNamespaceUriToPrefix(java.util.Map)
	 * @return this, for method chaining
	 */
	MarshallerOptions setNamespaceUriToPrefix(Map<String, String> namespaceUriToPrefix);

	/**
	 * @see edu.utah.further.core.api.xml.XmlService#setRootNamespaceUris(java.util.Set)
	 * @return this, for method chaining
	 */
	MarshallerOptions setRootNamespaceUris(Set<String> rootNamespaceUris);

	/**
	 * Return the debugPrintResult property.
	 * 
	 * @return the debugPrintResult
	 */
	boolean isDebugPrintResult();

	/**
	 * Set a new value for the debugPrintResult property.
	 * 
	 * @param debugPrintResult
	 *            the debugPrintResult to set
	 * @return this, for method chaining
	 */
	MarshallerOptions setDebugPrintResult(boolean debugPrintResult);

	/**
	 * Return the convertInputToUtf8 property.
	 * 
	 * @return the convertInputToUtf8
	 */
	boolean isConvertInputToUtf8();

	/**
	 * Set a new value for the convertInputToUtf8 property.
	 * 
	 * @param convertInputToUtf8
	 *            the convertInputToUtf8 to set
	 * @return this, for method chaining
	 */
	MarshallerOptions setConvertInputToUtf8(boolean convertInputToUtf8);

	/**
	 * @param format
	 * @return
	 */
	MarshallerOptions setFormat(boolean format);

	/**
	 * @return
	 */
	boolean isFormat();

	/**
	 * Return the marshaller property.
	 * 
	 * @return the marshaller
	 */
	Marshaller getMarshaller();

	/**
	 * Return the unmarshaller property.
	 * 
	 * @return the unmarshaller
	 */
	Unmarshaller getUnmarshaller();

	/**
	 * @return
	 */
	JAXBContext getContext();
}