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

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static org.slf4j.LoggerFactory.getLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.core.api.ws.Documentation;
import edu.utah.further.core.api.ws.ExamplePath;
import edu.utah.further.core.api.ws.HttpMethod;
import edu.utah.further.core.metadata.service.UtilServiceRest;
import edu.utah.further.core.metadata.to.MetaData;
import edu.utah.further.core.metadata.to.ParamType;
import edu.utah.further.core.metadata.to.WsElementMd;
import edu.utah.further.core.metadata.to.WsElementType;
import edu.utah.further.core.ws.WsUtil;

/**
 * Terminology translation RESTful web service implementation.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Implementation
// Wire manually in other modules with the proper properties
// @Service("coreUtilServiceRest")
@Path(UtilServiceRest.PATH)
public class UtilServiceRestImpl implements UtilServiceRest
{
	// ========================= FIELDS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UtilServiceRestImpl.class);

	/**
	 * Default documentation string of an element.
	 */
	public static final String DEFAULT_DESCRIPTION = "This element does not have associated documentation";

	/**
	 * REST POST methods don't require parameter annotation, use this default name for the
	 * sole method parameter.
	 */
	public static final String POST_REQUEST_BODY_PARAMETER_NAME = "POST request body";

	/**
	 * Delineates between different types of web services for which we build meta data
	 * elements.
	 */
	private static enum WsType
	{
		REST, SOAP
	}

	// ========================= DEPENDENCIES ==============================

	/**
	 * List of SOAP service classes - uses an annotation finder.
	 */
	@Resource(name = "soapClasses")
	private Set<Class<?>> soapClasses;

	/**
	 * List of REST service classes - uses an annotation finder.
	 */
	@Resource(name = "restClasses")
	private Set<Class<?>> restClasses;

	/**
	 * The release version number of web services.
	 */
	private String version;

	// ========================= FIELDS ====================================

	/**
	 * A cached MD with the list of SOAP web service MD objects.
	 */
	private MetaData soapServiceMd;

	/**
	 * A cached MD with the list of REST web service MD objects.
	 */
	private MetaData restServiceMd;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Load the client's property from file.
	 */
	@PostConstruct
	public void setup()
	{
		ValidationUtil.validateNotEmpty("version", version);
	}

	// ========================= IMPLEMENTATION: UtilServiceRest ===========

	/**
	 * Return the list of all known RESTful services' meta data objects.
	 *
	 * @return a composite MD object containing a list of service meta data objects
	 * @see edu.utah.further.core.metadata.service.UtilServiceRest#getAllSoapServiceMd()
	 */
	@Override
	public MetaData getAllSoapServiceMd()
	{
		final WsType webServiceType = WsType.SOAP;
		if (soapServiceMd == null)
		{
			soapServiceMd = new MetaData();

			// Build class meta data elements
			for (final Class<?> serviceClass : getSoapClasses())
			{
				// Process only interfaces, not implementations
				if (serviceClass.getAnnotation(Implementation.class) != null)
				{
					continue;
				}

				final WsElementMd classMd = new WsClassMdBuilder(webServiceType, null,
						serviceClass).setType(serviceClass.getName()).build();
				if (classMd == null)
				{
					continue;
				}
				soapServiceMd.addChild(classMd);

				// Build method meta data elements
				for (final Method method : serviceClass.getMethods())
				{
					final WsElementMd methodMd = new WsMethodMdBuilder(webServiceType,
							classMd, method).setDocumentation(
							method.getAnnotation(Documentation.class)).build();
					if (methodMd == null)
					{
						continue;
					}
					classMd.addChild(methodMd);
				}
			}
		}
		return soapServiceMd;
	}

	/**
	 * Return the list of all known RESTful services' meta data objects.
	 *
	 * @return a composite MD object containing a list of service meta data objects
	 * @see edu.utah.further.core.metadata.service.UtilServiceRest#getAllRestServiceMd()
	 */
	@Override
	public MetaData getAllRestServiceMd()
	{
		final WsType webServiceType = WsType.REST;
		if (restServiceMd == null)
		{
			restServiceMd = new MetaData();

			// Build class meta data elements
			for (final Class<?> serviceClass : getRestClasses())
			{
				// Process only interfaces, not implementations
				if (serviceClass.getAnnotation(Implementation.class) != null)
				{
					continue;
				}

				final WsElementMd classMd = new WsClassMdBuilder(webServiceType, null,
						serviceClass).setType(serviceClass.getName()).build();
				if (classMd == null)
				{
					continue;
				}
				restServiceMd.addChild(classMd);

				// Build method meta data elements
				for (final Method method : serviceClass.getMethods())
				{
					final WsElementMd methodMd = new WsMethodMdBuilder(webServiceType,
							classMd, method)
							.setHttpMethod(method.getAnnotations())
							.setDocumentation(method.getAnnotation(Documentation.class))
							.setPath(method.getAnnotation(Path.class))
							.setExamplePath(method.getAnnotation(ExamplePath.class))
							.build();
					if (methodMd == null)
					{
						continue;
					}
					classMd.addChild(methodMd);

					// Build field meta data elements
					final Class<?>[] parameterTypes = method.getParameterTypes();
					final Annotation[][] parameterAnnotations = method
							.getParameterAnnotations();
					for (int i = 0; i < parameterTypes.length; i++)
					{
						final WsElementMd parameterMd = new WsParameterMdBuilder(
								webServiceType, methodMd).setParameterAnnotations(
								parameterAnnotations[i]).setType(
								parameterTypes[i].getSimpleName()).build();
						if (parameterMd == null)
						{
							continue;
						}
						methodMd.addChild(parameterMd);
					}
				}
			}
		}
		return restServiceMd;
	}

	/**
	 * @return
	 * @throws WsException
	 * @see edu.utah.further.core.metadata.service.UtilServiceRest#getVersion()
	 */
	@Override
	public String getVersion() throws WsException
	{
		return version;
	}

	// ========================= DEPENDENCY INJECTION ======================

	/**
	 * Set a new value for the version property.
	 *
	 * @param version
	 *            the version to set
	 */
	public void setVersion(final String version)
	{
		this.version = version;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return the soapClasses property.
	 *
	 * @return the soapClasses
	 */
	private Set<Class<?>> getSoapClasses()
	{
		return soapClasses;
	}

	/**
	 * Return the restClasses property.
	 *
	 * @return the restClasses
	 */
	private Set<Class<?>> getRestClasses()
	{
		// Add ourselves as a web service provider, because we might be called
		// from a different OSGi bundle that can't easily scan for this class on its
		// classpath
		final Set<Class<?>> set = CollectionUtil.newSet(restClasses);
		set.add(UtilServiceRest.class); // where our JSR-311 annotations are declared
		return set;
	}

	/**
	 * -----------------------------------------------------------------------------------<br>
	 * A common class of all {@link MetaData} object builders.
	 * -----------------------------------------------------------------------------------<br>
	 */
	private abstract class AbstractWsElementMdBuilder<T extends AbstractWsElementMdBuilder<T>>
	{
		// Fields holding the context that this builder was created for (e.g. class name,
		// method name)
		// protected final WsElementMd parentElement;

		// MetaData fields managed by this builder
		protected final WsType webServiceType;
		protected final WsElementType wsElementType;
		protected String name = DEFAULT_DESCRIPTION;
		protected String description = DEFAULT_DESCRIPTION;
		protected String path;
		protected String type;
		protected String examplePath;
		protected HttpMethod httpMethod;

		/**
		 * @param webServiceType
		 * @param parentElement
		 */
		public AbstractWsElementMdBuilder(final WsType webServiceType,
				final WsElementType wsElementType, final WsElementMd parentElement)
		{
			super();
			this.webServiceType = webServiceType;
			this.wsElementType = wsElementType;

			// Set fields from parent element that can be overridden with this builder's
			// setters
			if (parentElement != null)
			{
				setHttpMethod(parentElement.getHttpMethod());
			}
			// this.parentElement = parentElement;
		}

		/**
		 * Set a new value for the type property.
		 *
		 * @param type
		 *            the type to set
		 */
		@SuppressWarnings("unchecked")
		public T setType(final String type)
		{
			this.type = type;
			return (T) this;
		}

		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
					.append("description", description)
					.append("examplePath", examplePath)
					.append("httpMethod", httpMethod)
					.append("name", name)
					.append("path", path)
					.append("type", type)
					.append("webServiceType", webServiceType)
					.toString();
		}

		/**
		 * @param documentation
		 */
		@SuppressWarnings("unchecked")
		public T setDocumentation(final Documentation documentation)
		{
			if (documentation != null)
			{
				this.name = documentation.name();
				this.description = documentation.description();
			}
			return (T) this;
		}

		/**
		 * Set a new value for the httpMethod property.
		 *
		 * @param httpMethod
		 *            the httpMethod to set
		 */
		@SuppressWarnings("unchecked")
		public T setHttpMethod(final HttpMethod httpMethod)
		{
			this.httpMethod = httpMethod;
			return (T) this;
		}

		/**
		 * @param annotations
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public T setHttpMethod(final Annotation[] annotations)
		{
			// Find the applicable annotation (there will be at most one by JAX-RS
			// annotation rules), and set the HTTP method accordingly if the annotation is
			// found.
			for (final Annotation annotation : annotations)
			{
				if (ReflectionUtil.instanceOf(annotation, GET.class))
				{
					this.httpMethod = HttpMethod.GET;
					break;
				}
				else if (ReflectionUtil.instanceOf(annotation, POST.class))
				{
					this.httpMethod = HttpMethod.POST;
					break;
				}
			}
			return (T) this;
		}

		/**
		 * @param method
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public T setPath(final Path annotation)
		{
			// log.warn("Missing annotation @" + annotationClazz.getName()
			// + " for REST element " + method);
			if (annotation != null)
			{
				this.path = annotation.value();
			}
			return (T) this;
		}

		/**
		 * @param md
		 * @see edu.utah.further.core.api.lang.Builder#build()
		 */
		public WsElementMd build()
		{
			if (!isValid())
			{
				return null;
			}
			final WsElementMd md = new WsElementMd();
			md.setWsElementType(wsElementType);
			md.setName(name);
			md.setDescription(description);
			md.setClazz(type);
			md.setPath(path);
			md.setHttpMethod(httpMethod);
			md.setExamplePath(examplePath);
			return md;
		}

		/**
		 * Run validations on the construction arguments and decide whether we are ready
		 * to build a valid {@link WsElementMd} object.
		 *
		 * @return is {@link WsElementMd} construction information valid
		 */
		protected boolean isValid()
		{
			// Perform negative validation checks
			if (name == null)
			{
				log.warn("Did not find name of element " + this);
				return false;
			}

			return true;
		}
	}

	/**
	 * -----------------------------------------------------------------------------------<br>
	 * A convenient builder of web class meta data elements.
	 * -----------------------------------------------------------------------------------<br>
	 */
	private class WsClassMdBuilder extends AbstractWsElementMdBuilder<WsClassMdBuilder>
			implements Builder<WsElementMd>
	{
		// Fields holding the context that this builder was created for (e.g. class name,
		// method name)
		private final Class<?> serviceClass;

		// MetaData fields managed by this builder

		/**
		 * @param webServiceType
		 * @param parentElement
		 * @param serviceClass
		 */
		public WsClassMdBuilder(final WsType webServiceType,
				final WsElementMd parentElement, final Class<?> serviceClass)
		{
			super(webServiceType, WsElementType.CLASS, parentElement);
			this.serviceClass = setServiceClass(serviceClass);
		}

		/**
		 * Set the class' path and documentation elements.
		 *
		 * @param serviceClass
		 * @return this object, for method chaining
		 */
		private Class<?> setServiceClass(final Class<?> serviceClass)
		{
			this.path = WsUtil.getWebServiceClassPath(serviceClass);
			setDocumentation(serviceClass.getAnnotation(Documentation.class));
			return serviceClass;
		}

		/**
		 * @return
		 * @see edu.utah.further.core.cxf.UtilServiceRestImpl.AbstractWsElementMdBuilder#isValid()
		 */
		@Override
		protected boolean isValid()
		{
			// Perform parent checks first
			if (!super.isValid())
			{
				return false;
			}

			// Perform negative validation checks
			if (webServiceType == WsType.REST)
			{
				if (path == null)
				{
					log.warn("Did not find @Path annotation on REST class "
							+ serviceClass);
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * -----------------------------------------------------------------------------------<br>
	 * A convenient builder of method meta data elements.
	 * -----------------------------------------------------------------------------------<br>
	 */
	private class WsMethodMdBuilder extends AbstractWsElementMdBuilder<WsMethodMdBuilder>
			implements Builder<WsElementMd>
	{
		// Fields holding the context that this builder was created for (e.g. class name,
		// method name)
		private final Method method;

		/**
		 * @param webServiceType
		 * @param parentElement
		 * @param method
		 */
		public WsMethodMdBuilder(final WsType webServiceType,
				final WsElementMd parentElement, final Method method)
		{
			super(webServiceType, WsElementType.METHOD, parentElement);
			this.method = method;
		}

		/**
		 * @param method
		 * @return
		 */
		public WsMethodMdBuilder setExamplePath(final ExamplePath annotation)
		{
			if (annotation != null)
			{
				this.examplePath = annotation.value();
			}
			return this;
		}

		/**
		 * @return
		 * @see edu.utah.further.core.cxf.UtilServiceRestImpl.AbstractWsElementMdBuilder#isValid()
		 */
		@Override
		protected boolean isValid()
		{
			// Perform parent checks first
			if (!super.isValid())
			{
				return false;
			}

			// Perform negative validation checks
			if (webServiceType == WsType.REST)
			{
				if (path == null)
				{
					log.warn("Did not find @Path annotation on REST method " + method);
					return false;
				}
			}
			return true;
		}

		/**
		 * @return
		 * @see edu.utah.further.core.api.lang.Builder#build()
		 */
		@Override
		public WsElementMd build()
		{
			final WsElementMd md = super.build();

			// Add warnings on missing here and in all other build() methods!!
			// log.warn("Missing annotation @" + annotationClazz.getName()
			// + " for REST method " + method);

			return md;
		}
	}

	/**
	 * -----------------------------------------------------------------------------------<br>
	 * A convenient builder of web parameter meta data elements.
	 * -----------------------------------------------------------------------------------<br>
	 */
	private class WsParameterMdBuilder extends
			AbstractWsElementMdBuilder<WsParameterMdBuilder> implements
			Builder<WsElementMd>
	{
		// Web-parameter-specific Fields
		private ParamType paramType;
		private String defaultValue;

		/**
		 * @param webServiceType
		 * @param parentElement
		 */
		public WsParameterMdBuilder(final WsType webServiceType,
				final WsElementMd parentElement)
		{
			super(webServiceType, WsElementType.PARAMETER, parentElement);

			if ((webServiceType == WsType.REST) && (httpMethod == HttpMethod.POST))
			{
				// REST POST methods don't require parameter annotation, use default name
				this.name = POST_REQUEST_BODY_PARAMETER_NAME;
				this.paramType = ParamType.POST_BODY;
			}
		}

		/**
		 * @param annotations
		 * @return
		 */
		public WsParameterMdBuilder setParameterAnnotations(final Annotation[] annotations)
		{
			// Loop over all parameter annotations and retrieve useful ones
			for (final Annotation annotation : annotations)
			{
				if (ReflectionUtil.instanceOf(annotation, Documentation.class))
				{
					this.description = ((Documentation) annotation).description();
				}
				else if (ReflectionUtil.instanceOf(annotation, PathParam.class))
				{
					this.name = ((PathParam) annotation).value();
					this.paramType = ParamType.PATH;
				}
				else if (ReflectionUtil.instanceOf(annotation, QueryParam.class))
				{
					this.name = ((QueryParam) annotation).value();
					this.paramType = ParamType.QUERY;
				}
				else if (ReflectionUtil.instanceOf(annotation, DefaultValue.class))
				{
					this.defaultValue = ((DefaultValue) annotation).value();
				}
			}
			return this;
		}

		/**
		 * @return
		 * @see edu.utah.further.core.api.lang.Builder#build()
		 */
		@Override
		public WsElementMd build()
		{
			final WsElementMd md = super.build();
			md.setParamType(paramType);
			md.setDefaultValue(defaultValue);
			return md;
		}
	}

}
