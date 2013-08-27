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
package edu.utah.further.core.cxf;

import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.core.api.xml.XmlService;

/**
 * A custom JAXB provider for CXF JAX-RS servers and clients. Useful when we want extra
 * packages on the context's path.
 * <p>
 * Since CXF 2.2.5, {@link JAXBElementProvider} provides a context properties
 * customization facility. This class may be deprecated in the future; we may use
 * {@link JAXBElementProvider} directly. But for minimal code changes with respect to all
 * existing FURTHeR client code of this class, we keep it.
 * <p>
 * In unit and integration tests {@link #initContext()} should still return a valid JAXB
 * context; on the bus {@link JAXBElementProvider#getClassContext(Class)} is now used as a
 * fall-back in case {@link #initContext()} fails.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 1, 2009
 */
@Provider
public class JaxbElementProvider extends JAXBElementProvider
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(JaxbElementProvider.class);

	// ========================= FIELDS ====================================

	/**
	 * JAXB context produced by this object.
	 */
	private JAXBContext context;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Set of JAXB-annotated packages.
	 */
	private Set<String> jaxbPackages = CollectionUtil.newSet();

	/**
	 * Custom JAXB context properties.
	 */
	private Map<String, Object> jaxbConfig = CollectionUtil.newMap();

	/**
	 * Handles internal JAXB operations in this object.
	 */
	@Autowired
	private XmlService xmlService;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize dependencies.
	 */
	@PostConstruct
	protected void afterPropertiesSet()
	{
		ValidationUtil.validateNotEmpty("jaxbPackages", jaxbPackages);
		this.context = initContext();
	}

	// ========================= IMPLEMENTATION: JAXBElementProvider =======

	/**
	 * @param type
	 * @param genericType
	 * @return
	 * @throws JAXBException
	 * @see org.apache.cxf.jaxrs.provider.AbstractJAXBProvider#getJAXBContext(java.lang.Class,
	 *      java.lang.reflect.Type)
	 */
	@Override
	protected JAXBContext getJAXBContext(final Class<?> type, final Type genericType)
			throws JAXBException
	{
		// If initContext() works, use its return type; use default
		// implementation as a fall-back
		return (context != null) ? context : super.getJAXBContext(type, genericType);
	}

	/**
	 * Set a new value for the contextProperties property.
	 *
	 * @param jaxbConfig
	 *            the contextProperties to set
	 * @see org.apache.cxf.jaxrs.provider.AbstractJAXBProvider#setContextProperties(java.util.Map)
	 */
	@Override
	public void setContextProperties(final Map<String, Object> jaxbConfig)
	{
		super.setContextProperties(jaxbConfig);
		this.jaxbConfig = jaxbConfig;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the jaxbPackages property.
	 *
	 * @param jaxbPackages
	 *            the jaxbPackages to set
	 */
	public void setJaxbPackages(final Set<String> jaxbPackages)
	{
		this.jaxbPackages = jaxbPackages;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Initialize our custom JAXB context. May fail on when this class is deployed to the
	 * ESB inside a bundle.
	 *
	 * @return custom JAXB context instance
	 */
	private JAXBContext initContext()
	{
		try
		{
			if (log.isDebugEnabled())
			{
				log.debug("Initializing JAXB context with packages " + jaxbPackages
						+ ", contextProperties " + jaxbConfig);
			}
			return xmlService
					.options()
					.setPackages(jaxbPackages)
					.setJaxbConfig(jaxbConfig)
					.buildContext()
					.getContext();
		}
		catch (final JAXBException e)
		{
			if (log.isWarnEnabled())
			{
				log.warn("Failed to initialize JAXB context: " + e.getMessage());
			}
			// throw new RuntimeException(e);
			return null;
		}
	}

}