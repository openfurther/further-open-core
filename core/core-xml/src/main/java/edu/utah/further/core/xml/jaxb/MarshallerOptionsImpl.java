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

import static edu.utah.further.core.api.constant.Strings.JAXB_PACKAGE_SEPARATOR;
import static edu.utah.further.core.api.text.StringUtil.chain;
import static edu.utah.further.core.api.xml.XmlNamespace.XML_SCHEMA;
import static edu.utah.further.core.api.xml.XmlNamespace.XML_SCHEMA_INSTANCE;
import static edu.utah.further.core.api.xml.XmlNamespace.XML_SCHEMA_INSTANCE_NAMESPACE;
import static edu.utah.further.core.api.xml.XmlNamespace.XML_SCHEMA_NAMESPACE;
import static org.slf4j.LoggerFactory.getLogger;

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
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.xml.sax.SAXException;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.xml.JaxbMarshallerProperty;
import edu.utah.further.core.api.xml.MarshallerOptions;
import edu.utah.further.core.api.xml.XmlService;

/**
 * An {@link XmlService} helper class that builds the {@link JAXBContext} and JAXB
 * marshaller, unmarshaller.
 * <p>
 * Usage:
 * <ol>
 * <li>First, use the chaining calls and call {@link #buildContext()} to set up a
 * {@link JAXBContext}.</li>
 * <li>the {@link MarshallerOptionsImpl} can then be reused to set different marshaller
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
final class MarshallerOptionsImpl implements MarshallerOptions
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(MarshallerOptionsImpl.class);

	/**
	 * SchemaFactory for creating Schemas needed for validation.
	 */
	private static final SchemaFactory schemaFactory = SchemaFactory
			.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

	// ========================= DEPENDENCIES ==============================

	// /**
	// * Owning XML service, for call-backs.
	// */
	// private final XmlService xmlService;

	private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	// ========================= INPUT FIELDS - JAXBContext ================

	/**
	 * Packages on the the JAXB context path.
	 */
	private Set<String> packages = CollectionUtil.newSet();

	/**
	 * JAXB context configuration properties.
	 */
	private Map<String, Object> jaxbConfig = CollectionUtil.newMap();

	/**
	 * Default namespace-URI-to-prefix mapp.
	 */
	private final GenericNamespacePrefixMapper namespacePrefixMapper = new GenericNamespacePrefixMapper();

	// ========================= INPUT FIELDS - Marshaller/Unmarshaller ====

	/**
	 * Marshaller configuration properties.
	 */
	private final Map<JaxbMarshallerProperty, Object> marshallerConfig = CollectionUtil
			.newMap();

	/**
	 * If <code>true</code>, converts the XML input stream to UTF8 before unmarshalling.
	 */
	private boolean convertInputToUtf8 = true;

	/**
	 * If <code>true</code>, logs the marshalled result for debugging.
	 */
	private boolean debugPrintResult = false;

	/**
	 * If <code>true</code>, formats the output XML.
	 */
	private boolean format = false;

	// ========================= TARGET OBJECTT ============================

	/**
	 * The constructed JAXB context object.
	 */
	private JAXBContext context;

	/**
	 * Marshalling delegate.
	 */
	private Marshaller marshaller;

	/**
	 * Unmarshalling delegate.
	 */
	private Unmarshaller unmarshaller;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Hide constructor - instantiate using {@link XmlService#options()}.
	 * 
	 * @param xmlService
	 *            owning XML service; We call-back this object's methods within this
	 *            class.
	 * @param rootClass
	 *            class of root object to be marshaled
	 */
	private MarshallerOptionsImpl()
	{
		super();
		setNamespaceUriToPrefix(getDefaultNamespaceUriToPrefix());
		setRootNamespaceUris(getDefaultRootNamespaceUris());
	}

	/**
	 * Hide constructor - instantiate using {@link XmlService#options()}.
	 * 
	 * @param xmlService
	 *            owning XML service; We call-back this object's methods within this
	 *            class.
	 * @param rootClass
	 *            class of root object to be marshaled
	 */
	static MarshallerOptions newInstance()
	{
		return new MarshallerOptionsImpl();
	}

	// /**
	// * Hide constructor - instantiate using {@link XmlService#options()}.
	// *
	// * @param xmlService
	// * owning XML service; We call-back this object's methods within this
	// * class.
	// * @param rootClass
	// * class of root object to be marshaled
	// */
	// private static MarshallerOptions newInstance(final String rootPackage)
	// {
	// ValidationUtil.validateNotNull("rootPackage", rootPackage);
	// return newInstance().addPackage(rootPackage);
	// }
	//
	// /**
	// * Hide constructor - instantiate using {@link XmlService#options()}.
	// *
	// * @param xmlService
	// * owning XML service; We call-back this object's methods within this
	// * class.
	// * @param rootClass
	// * class of root object to be marshaled
	// */
	// private static MarshallerOptions newInstance(final Class<?> rootClass)
	// {
	// ValidationUtil.validateNotNull("rootClass", rootClass);
	// return newInstance().addClass(rootClass);
	// }

	// ========================= METHODS ===================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.jaxb.MarshallerOptions#buildContext()
	 */
	@Override
	public MarshallerOptions buildContext() throws JAXBException
	{
		// Add validation rules here in the future
		this.context = createContext();
		this.marshaller = context.createMarshaller();
		this.unmarshaller = context.createUnmarshaller();

		// Set marshaller properties based on all parameter setting to this point
		if (format)
		{
			JaxbMarshallerProperty.setProperties(marshaller, formattedOutputProperties());
		}
		// Overrides all options set by other calls than setMarshallerConfig().
		JaxbMarshallerProperty.setProperties(marshaller, marshallerConfig);
		return this;
	}

	@Override
	public MarshallerOptions buildContext(ClassLoader loader) throws JAXBException
	{
		this.classLoader = loader;
		return buildContext();
	}

	// /**
	// * Return the owning {@link XmlService} that provides marshalling and unmarshalling
	// * calls.
	// *
	// * @return
	// */
	// public XmlService readyToMarshal()
	// {
	//
	// }

	// ========================= GETTERS & SETTERS =========================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.jaxb.MarshallerOptions#addContextClass(java.lang.Class)
	 */
	@Override
	public MarshallerOptions addClass(final Class<?> e)
	{
		return addPackage(e.getPackage().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.jaxb.MarshallerOptions#addContextClasses(java.util.Collection
	 * )
	 */
	@Override
	public MarshallerOptions addClasses(final Collection<? extends Class<?>> c)
	{
		for (final Class<?> clazz : c)
		{
			addClass(clazz);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.xml.MarshallerOptions#addClasses(java.lang.Class<?>[])
	 */
	@Override
	public MarshallerOptions addClasses(final Class<?>... classes)
	{
		return addClasses(CollectionUtil.<Class<?>> toSet(classes));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.jaxb.MarshallerOptions#setContextClasses(java.util.Set)
	 */
	@Override
	public MarshallerOptions setClasses(final Set<Class<?>> classes)
	{
		packages.clear();
		return addClasses(classes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.xml.MarshallerOptions#setClasses(java.lang.Class<?>[])
	 */
	@Override
	public MarshallerOptions setClasses(final Class<?>... classes)
	{
		return setClasses(CollectionUtil.<Class<?>> toSet(classes));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.jaxb.MarshallerOptions#addPackage(java.lang.Class)
	 */
	@Override
	public MarshallerOptions addPackage(final String e)
	{
		packages.add(e);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.jaxb.MarshallerOptions#addPackages(java.util.Collection)
	 */
	@Override
	public MarshallerOptions addPackages(final Collection<String> c)
	{
		packages.addAll(c);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.xml.MarshallerOptions#addPackages(java.lang.String[])
	 */
	@Override
	public MarshallerOptions addPackages(final String... pkgs)
	{
		return addPackages(CollectionUtil.<String> toSet(pkgs));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.jaxb.MarshallerOptions#setPackages(java.util.Set)
	 */
	@Override
	public MarshallerOptions setPackages(final Set<String> packages)
	{
		this.packages = CollectionUtil.newSet(packages);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.xml.MarshallerOptions#setPackages(java.lang.String[])
	 */
	@Override
	public MarshallerOptions setPackages(final String... packages)
	{
		return setPackages(CollectionUtil.<String> toSet(packages));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.jaxb.MarshallerOptions#setJaxbConfig(java.util.Map)
	 */
	@Override
	public MarshallerOptions setJaxbConfig(final Map<String, Object> jaxbConfig)
	{
		this.jaxbConfig = jaxbConfig;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.jaxb.MarshallerOptions#setMarshallerConfig(java.util.Map)
	 */
	@Override
	public MarshallerOptions setMarshallerConfig(
			final Map<JaxbMarshallerProperty, Object> marshallerConfig)
	{
		// Set marshaller properties: defaults are applied first, overridden by
		// marshallerConfig
		JaxbMarshallerProperty.setProperties(marshaller, marshallerConfig);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.jaxb.MarshallerOptions#setValidationSchema(javax.xml.
	 * validation.Schema)
	 */
	@Override
	public MarshallerOptions setValidationSchema(final Schema validationSchema)
	{
		marshaller.setSchema(validationSchema);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.jaxb.MarshallerOptions#setValidationSchema(javax.xml.
	 * transform.Source)
	 */
	@Override
	public MarshallerOptions setValidationSchema(final Source validationSchema)
			throws SAXException
	{
		// ValidationUtil.validateNotNull("XML validation schema", validationSchema);
		return setValidationSchema(validationSchema == null ? null : schemaFactory
				.newSchema(validationSchema));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.xml.MarshallerOptions#setValidationSchema(java.util.List)
	 */
	@Override
	public MarshallerOptions setValidationSchema(List<Source> validationSchemas)
			throws SAXException
	{
		return setValidationSchema(validationSchemas == null ? null
				: schemaFactory.newSchema(validationSchemas
						.toArray(new Source[validationSchemas.size()])));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.jaxb.MarshallerOptions#setNamespaceUriToPrefix(java.util
	 * .Map)
	 */
	@Override
	public MarshallerOptions setNamespaceUriToPrefix(
			final Map<String, String> namespaceUriToPrefix)
	{
		this.namespacePrefixMapper.setNamespaceUriToPrefix(namespaceUriToPrefix);
		updateMarshallerConfig();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.jaxb.MarshallerOptions#setRootNamespaceUris(java.util
	 * .Set)
	 */
	@Override
	public MarshallerOptions setRootNamespaceUris(final Set<String> rootNamespaceUris)
	{
		this.namespacePrefixMapper.setRootNamespaceUris(rootNamespaceUris);
		updateMarshallerConfig();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.jaxb.MarshallerOptions#isDebugPrintResult()
	 */
	@Override
	public boolean isDebugPrintResult()
	{
		return debugPrintResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.jaxb.MarshallerOptions#setDebugPrintResult(boolean)
	 */
	@Override
	public MarshallerOptions setDebugPrintResult(final boolean debugPrintResult)
	{
		this.debugPrintResult = debugPrintResult;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.jaxb.MarshallerOptions#isConvertInputToUtf8()
	 */
	@Override
	public boolean isConvertInputToUtf8()
	{
		return convertInputToUtf8;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.xml.jaxb.MarshallerOptions#setConvertInputToUtf8(boolean)
	 */
	@Override
	public MarshallerOptions setConvertInputToUtf8(final boolean convertInputToUtf8)
	{
		this.convertInputToUtf8 = convertInputToUtf8;
		return this;
	}

	/**
	 * Return the format property.
	 * 
	 * @return the format
	 */
	@Override
	public boolean isFormat()
	{
		return format;
	}

	/**
	 * Set a new value for the format property.
	 * 
	 * @param format
	 *            the format to set
	 * @return this, for method chaining
	 */
	@Override
	public MarshallerOptions setFormat(final boolean format)
	{
		this.format = format;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.jaxb.MarshallerOptions#getMarshaller()
	 */
	@Override
	public Marshaller getMarshaller()
	{
		return marshaller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.xml.jaxb.MarshallerOptions#getUnmarshaller()
	 */
	@Override
	public Unmarshaller getUnmarshaller()
	{
		return unmarshaller;
	}

	/**
	 * Return the context property.
	 * 
	 * @return the context
	 */
	@Override
	public JAXBContext getContext()
	{
		return context;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Synchronize the marshalling configuration properties with input field values.
	 */
	private void updateMarshallerConfig()
	{
		marshallerConfig.put(JaxbMarshallerProperty.NAMESPACE_PREFIX_MAPPER,
				namespacePrefixMapper);
	}

	/**
	 * @return
	 */
	private static Set<String> getDefaultRootNamespaceUris()
	{
		return CollectionUtil.<String> toSet(XML_SCHEMA_INSTANCE_NAMESPACE,
				XML_SCHEMA_NAMESPACE);
	}

	/**
	 * @return
	 */
	private static Map<String, String> getDefaultNamespaceUriToPrefix()
	{
		final Map<String, String> map = CollectionUtil.newMap();
		// map.put(CORE_QUERY, EMPTY_STRING);
		map.put(XML_SCHEMA_INSTANCE_NAMESPACE, XML_SCHEMA_INSTANCE);
		map.put(XML_SCHEMA_NAMESPACE, XML_SCHEMA);
		return map;
	}

	/**
	 * Return a properties map to set on a marshaller for human-readable XML printouts.
	 * 
	 * @return marshaller properties map
	 */
	private static Map<JaxbMarshallerProperty, Object> formattedOutputProperties()
	{
		final Map<JaxbMarshallerProperty, Object> properties = CollectionUtil.newMap();
		// Pretty printing / indentation
		properties.put(JaxbMarshallerProperty.FORMATTED_OUTPUT, Boolean.TRUE);
		return properties;
	}

	/**
	 * Creates a JAXB context from the current object's state.
	 * 
	 * @return JAXB context instance
	 * @throws JAXBException
	 */
	private JAXBContext createContext() throws JAXBException
	{
		final String contextPath = chain(packages, JAXB_PACKAGE_SEPARATOR);
		return JAXBContext.newInstance(contextPath, classLoader, jaxbConfig);
	}
}
