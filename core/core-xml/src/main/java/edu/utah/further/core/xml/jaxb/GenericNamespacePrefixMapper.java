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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Custom namespace prefix mapper for all FURTHeR marshalling backed by a general set of
 * root-tag namespace URIs and a general map of namespace-URI-to-prefix.
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
 * @version Jul 8, 2010
 */
public final class GenericNamespacePrefixMapper extends NamespacePrefixMapper
{
	// ========================= DEPENDENCIES ==============================

	/**
	 * A map of namespace URIs to desired prefixes in the XML documents.
	 */
	private final Map<String, String> namespaceUriToPrefix = CollectionUtil.newMap();

	/**
	 * List of namespace URIs to appear in the root tag of the XML document.
	 */
	private final Set<String> rootNamespaceUris = CollectionUtil.newSet();

	// ========================= IMPL: NamespacePrefixMapper ===============

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sun.xml.bind.marshaller.NamespacePrefixMapper#getPreDeclaredNamespaceUris()
	 */
	@Override
	public String[] getPreDeclaredNamespaceUris()
	{
		return rootNamespaceUris.toArray(CollectionUtil.EMPTY_STRING_ARRAY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sun.xml.bind.marshaller.NamespacePrefixMapper#getPreferredPrefix(java.lang.
	 * String, java.lang.String, boolean)
	 */
	@Override
	public String getPreferredPrefix(final String namespaceUri, final String suggestion,
			final boolean requirePrefix)
	{
		if (namespaceUriToPrefix.containsKey(namespaceUri))
		{
			return namespaceUriToPrefix.get(namespaceUri);
		}

		return suggestion;
	}

	// ========================= GET / SET =================================

	/**
	 * Set a new value for the namespaceUriToPrefix property.
	 *
	 * @param namespaceUriToPrefix
	 *            the namespaceUriToPrefix to set
	 */
	public void setNamespaceUriToPrefix(final Map<String, String> namespaceUriToPrefix)
	{
		CollectionUtil.setMapElements(this.namespaceUriToPrefix, namespaceUriToPrefix);
	}

	/**
	 * @param namespaceUri
	 * @param prefix
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public String addNamespaceUri(final String namespaceUri, final String prefix)
	{
		return namespaceUriToPrefix.put(namespaceUri, prefix);
	}

	/**
	 * @param namespaceUri
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public String removeNamespaceUri(final String namespaceUri)
	{
		return namespaceUriToPrefix.remove(namespaceUri);
	}

	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void addNamespaceUris(final Map<String, String> m)
	{
		namespaceUriToPrefix.putAll(m);
	}

	/**
	 * @param namespaceUris
	 */
	public void removeNamespaceUris(final Set<String> namespaceUris)
	{
		for (final String namespaceUri : namespaceUris)
		{
			removeNamespaceUri(namespaceUri);
		}
	}

	/**
	 * Set a new value for the rootNamespaceUris property.
	 *
	 * @param rootNamespaceUris
	 *            the rootNamespaceUris to set
	 */
	public void setRootNamespaceUris(final Set<String> rootNamespaceUris)
	{
		CollectionUtil.setCollectionElements(this.rootNamespaceUris, rootNamespaceUris);
	}

	/**
	 * @param rootNamespaceUri
	 * @return
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean addRootNamespaceUri(final String rootNamespaceUri)
	{
		return rootNamespaceUris.add(rootNamespaceUri);
	}

	/**
	 * @param rootNamespaceUri
	 * @return
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	public boolean removeRootNamespaceUri(final String rootNamespaceUri)
	{
		return rootNamespaceUris.remove(rootNamespaceUri);
	}

	/**
	 * @param someRootNamespaceUris
	 * @return
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	public boolean addRootNamespaceUris(final Collection<String> someRootNamespaceUris)
	{
		return someRootNamespaceUris.addAll(someRootNamespaceUris);
	}

	/**
	 * @param someRootNamespaceUris
	 * @return
	 * @see java.util.Set#removeAll(java.util.Collection)
	 */
	public boolean removeRootNamespaceUris(final Collection<String> someRootNamespaceUris)
	{
		return someRootNamespaceUris.removeAll(someRootNamespaceUris);
	}
}
