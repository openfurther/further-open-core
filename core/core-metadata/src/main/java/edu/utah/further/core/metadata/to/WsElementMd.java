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
package edu.utah.further.core.metadata.to;

import static edu.utah.further.core.api.constant.Strings.VIRTUAL_DIRECTORY;
import static edu.utah.further.core.api.text.StringUtil.trim;
import static org.slf4j.LoggerFactory.getLogger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.ws.HttpMethod;
import edu.utah.further.core.api.ws.UrlBuilder;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * An abstraction of web service Java element metadata object.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Implementation
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.MDR, name = WsElementMd.ENTITY_NAME, propOrder =
{ "wsElementType", "httpMethod", "path", "examplePath", "clazz", "paramType",
		"defaultValue" })
@XmlRootElement(namespace = XmlNamespace.MDR, name = WsElementMd.ENTITY_NAME)
public final class WsElementMd extends MetaData
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	@SuppressWarnings("hiding")
	static final String ENTITY_NAME = "wsElementMd";

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(WsElementMd.class);

	// ========================= FIELDS ====================================

	/**
	 * Web service element type. Must be set on this object to determine its context.
	 */
	@Final
	@XmlElement(name = "wsElementType", required = false)
	private WsElementType wsElementType;

	/**
	 * Supported HTTP method. Assuming there's only one.
	 */
	@XmlElement(name = "httpMethod", required = false)
	private HttpMethod httpMethod;

	/**
	 * Relative URL (path) of the web element (class or method).
	 */
	@XmlElement(name = "path", required = false)
	private String path;

	/**
	 * Example of a specific method URL of a web service method element. May not include
	 * URI parameters.
	 */
	@XmlElement(name = "examplePath", required = false)
	private String examplePath;

	/**
	 * If this a web parameter metadata element, this holds the web parameter's Java type
	 * class name. Also used in other WS elements: method return types and class interface
	 * names.
	 */
	@Final
	@XmlElement(name = "clazz", required = false)
	private String clazz;

	/**
	 * If this a web parameter metadata element, indicates the type of parameter.
	 */
	@Final
	@XmlElement(name = "paramType", required = false)
	private ParamType paramType;

	/**
	 * If this a web parameter metadata element, indicates its default value or
	 * <code>null</code> if there's no default..
	 */
	@Final
	@XmlElement(name = "defaultValue", required = false)
	private String defaultValue;

	/**
	 * Parent object in a {@link WsElementMd} graph.
	 */
	@XmlTransient
	private WsElementMd parent;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Sets no fields. Required for JAXB.
	 */
	public WsElementMd()
	{
		super();
	}

	/**
	 * Construct a Java element MD object.
	 *
	 * @param name
	 *            element name
	 * @param description
	 *            element description
	 * @param paramType
	 *            element's Java type class name
	 */
	public WsElementMd(final String name, final String description, final String paramType)
	{
		super(name, description);
		this.clazz = paramType;
	}

	// ========================= METHODS ===================================

	/**
	 * Compute and return the absolute path of this element from its path and its parent's
	 * path, delimited by {@link Strings#VIRTUAL_DIRECTORY}. If this is a method MD
	 * object, the path includes query parameters from children MDs of this object. Since
	 * GET REST method parameters are properly sorted by so that all
	 * {@link ParamType#PATH} parameters appear first and then all {@link ParamType#QUERY}
	 * parameters appear, there's no need to sort the children for constructing the path
	 * here. We simply append to the path all children paths starting with the first
	 * {@link ParamType#QUERY} child encountered.
	 *
	 * @return absolute element path
	 */
	public String getAbsolutePath()
	{
		switch (wsElementType)
		{
			case PARAMETER:
			{
				// A parameter does not have a path
				return Strings.EMPTY_STRING;
			}

			default:
			{
				final String url = (getParent() == null) ? getPath() : getParent()
						.getPath()
						+ VIRTUAL_DIRECTORY + getPath();
				if (log.isDebugEnabled())
				{
					log.debug("Building path for " + this + " base URL " + url);
				}
				if (wsElementType == WsElementType.METHOD)
				{
					final UrlBuilder builder = new UrlBuilder(url).setEncode(false);
					if (log.isDebugEnabled())
					{
						log.debug("Method path UriBuilder " + builder);
					}

					// Append query parameters if found
					for (final MetaData child : getChildren())
					{
						final WsElementMd md = (WsElementMd) child;
						if (md.getParamType() == ParamType.QUERY)
						{
							final String paramName = md.getName();
							if (log.isDebugEnabled())
							{
								log.debug("Appending query param " + paramName);
							}
							builder.queryParam(paramName, "{" + paramName + "}");
						}
					}
					final String absolutePath = builder.buildAsString();
					if (log.isDebugEnabled())
					{
						log.debug("Absolute path " + absolutePath);
					}

					return absolutePath;
				}
				return url;
			}
		}
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the paramType property.
	 *
	 * @return the paramType
	 */
	public ParamType getParamType()
	{
		return paramType;
	}

	/**
	 * Set a new value for the paramType property.
	 *
	 * @param paramType
	 *            the paramType to set
	 */
	public void setParamType(final ParamType paramType)
	{
		this.paramType = paramType;
	}

	/**
	 * Return the clazz property.
	 *
	 * @return the clazz
	 */
	public String getClazz()
	{
		return clazz;
	}

	/**
	 * Set a new value for the clazz property.
	 *
	 * @param clazz
	 *            the clazz to set
	 */
	public void setClazz(final String clazz)
	{
		this.clazz = clazz;
	}

	/**
	 * Return the wsElementType property.
	 *
	 * @return the wsElementType
	 */
	public WsElementType getWsElementType()
	{
		return wsElementType;
	}

	/**
	 * Set a new value for the wsElementType property.
	 *
	 * @param wsElementType
	 *            the wsElementType to set
	 */
	public void setWsElementType(final WsElementType wsElementType)
	{
		this.wsElementType = wsElementType;
	}

	/**
	 * Return the defaultValue property.
	 *
	 * @return the defaultValue
	 */
	public String getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * Set a new value for the defaultValue property.
	 *
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(final String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	/**
	 * Return the path property.
	 *
	 * @return the path
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * Set a new value for the path property.
	 *
	 * @param path
	 *            the path to set
	 */
	public void setPath(final String path)
	{
		this.path = trim(path, VIRTUAL_DIRECTORY);
	}

	/**
	 * Return the parent property.
	 *
	 * @return the parent
	 */
	public WsElementMd getParent()
	{
		return parent;
	}

	/**
	 * Set a new value for the parent property.
	 *
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(final WsElementMd parent)
	{
		this.parent = parent;
	}

	/**
	 * Return the examplePath property.
	 *
	 * @return the examplePath
	 */
	public String getExamplePath()
	{
		return examplePath;
	}

	/**
	 * Set a new value for the examplePath property.
	 *
	 * @param examplePath
	 *            the examplePath to set
	 */
	public void setExamplePath(final String examplePath)
	{
		this.examplePath = trim(examplePath, VIRTUAL_DIRECTORY);
	}

	/**
	 * Return the httpMethod property.
	 *
	 * @return the httpMethod
	 */
	public HttpMethod getHttpMethod()
	{
		return httpMethod;
	}

	/**
	 * Set a new value for the httpMethod property.
	 *
	 * @param httpMethod
	 *            the httpMethod to set
	 */
	public void setHttpMethod(final HttpMethod httpMethod)
	{
		this.httpMethod = httpMethod;
	}

	// ========================= PRIVATE METHODS ===========================
}
