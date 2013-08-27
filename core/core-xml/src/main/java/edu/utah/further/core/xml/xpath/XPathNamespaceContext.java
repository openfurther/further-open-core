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
package edu.utah.further.core.xml.xpath;

import static javax.xml.XMLConstants.NULL_NS_URI;
import static javax.xml.XMLConstants.XML_NS_URI;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;

/**
 * A namespace implementation for XPath processing.
 * <p>
 * Has a default namespace key constant {@link #PRE}. Use it in namespace-qualified XPath
 * expressions when this {@link NamespaceContext} implementation is set on an
 * {@link XPathParser} instance.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 5, 2008
 */
@Implementation
public final class XPathNamespaceContext implements NamespaceContext
{
	// ========================= CONSTANTS =================================

	/**
	 * Default namespace key. Use in namespace-qualified XPath expressions when this
	 * {@link NamespaceContext} implementation is set on an {@link XPathParser} instance.
	 */
	public static final String PRE = "pre";

	// ========================= FIELDS ====================================

	/**
	 * A map of prefixes to namespaces.
	 */
	private final Map<String, String> namespaceMapping = new HashMap<>();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a namespace context. The <code>xml</code> prefix is binded to
	 * <code>XML_NS_URI</code> by default.
	 */
	public XPathNamespaceContext()
	{
		super();
		addPrefix("xml", XML_NS_URI);
	}

	/**
	 * Initialize a namespace context with a default namespace. The <code>xml</code>
	 * prefix is also binded to <code>XML_NS_URI</code> by default.
	 * 
	 * @param defaultNamespace
	 *            default namespace URI. Binded to the prefix <code>pre</code>
	 */
	public XPathNamespaceContext(final String defaultNamespace)
	{
		this();
		setDefaultNamespace(defaultNamespace);
	}

	// ========================= IMPLEMENTATION: NamespaceContext ==========

	/**
	 * @param prefix
	 * @return
	 * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
	 */
	@Override
	public String getNamespaceURI(final String prefix)
	{
		final String namespace = namespaceMapping.get(prefix);
		return (namespace != null) ? namespace : NULL_NS_URI;
	}

	/**
	 * This method isn't necessary for XPath processing..
	 * 
	 * @param uri
	 * @return
	 * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
	 */
	@Override
	public final String getPrefix(final String uri)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * This method isn't necessary for XPath processing either..
	 * 
	 * @param uri
	 * @return
	 * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
	 */
	@Override
	public final Iterator<?> getPrefixes(final String uri)
	{
		throw new UnsupportedOperationException();
	}

	// ========================= METHODS ===================================

	/**
	 * Add a prefix to the prefix map.
	 * 
	 * @param prefix
	 *            namespace prefix
	 * @param namespace
	 *            namespace URI
	 * @return this object, for chaining calls
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public XPathNamespaceContext addPrefix(final String prefix, final String namespace)
	{
		if (prefix == null)
		{
			throw new NullPointerException("Null prefix");
		}
		namespaceMapping.put(prefix, namespace);
		return this;
	}

	/**
	 * Remove a prefix from the prefix map.
	 * 
	 * @param prefix
	 * @return this object, for chaining calls
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public XPathNamespaceContext removePrefix(final String prefix)
	{
		namespaceMapping.remove(prefix);
		return this;
	}

	// ========================= GET & SET =================================

	/**
	 * Add the default namespace.
	 * 
	 * @param namespace
	 *            namespace URI
	 * @return this object, for chaining calls
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public String getDefaultNamespace()
	{
		return namespaceMapping.get(PRE);
	}

	/**
	 * Add the default namespace.
	 * 
	 * @param namespace
	 *            namespace URI
	 * @return this object, for chaining calls
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public XPathNamespaceContext setDefaultNamespace(final String namespace)
	{
		addPrefix(PRE, namespace);
		return this;
	}

	/**
	 * Return the namespaceMapping property.
	 * 
	 * @return the namespaceMapping
	 */
	public Map<String, String> getNamespaceMapping()
	{
		return CollectionUtil.newMap(namespaceMapping);
	}

	/**
	 * Set the prefix map.
	 * 
	 * @param namespaceMapping
	 *            prefix-to-namespace URI map
	 * @return this object, for chaining calls
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public XPathNamespaceContext setNamespaceMapping(
			final Map<String, String> namespaceMapping)
	{
		CollectionUtil.setMapElements(this.namespaceMapping, namespaceMapping);
		return this;
	}
}