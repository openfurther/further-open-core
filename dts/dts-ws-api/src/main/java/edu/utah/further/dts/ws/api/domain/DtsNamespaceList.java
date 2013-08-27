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
package edu.utah.further.dts.ws.api.domain;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.to.DtsNamespaceToImpl;

/**
 * Implementation of a list of namespaces. It seems that JAXB has problems returning the
 * exposed list from a web method, so we wrap it with this root entity.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Nov 2, 2008
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsNamespaceList.ENTITY_NAME, propOrder =
{ "namespaceList" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsNamespaceList.ENTITY_NAME)
public class DtsNamespaceList
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "namespaceList";

	// ========================= METHODS ===================================

	/**
	 * A namespace collection.
	 */
	@Final
	@XmlElement(name = "namespace", required = false)
	private final List<DtsNamespaceToImpl> namespaceList;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct am empty namespace list.
	 */
	public DtsNamespaceList()
	{
		this.namespaceList = newList();
	}

	/**
	 * @param namespaceList
	 */
	public DtsNamespaceList(final List<DtsNamespace> namespaceList)
	{
		this();

		for (final DtsNamespace namespace : namespaceList)
		{
			addNamespace(namespace);
		}
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("namespaceList",
				namespaceList).toString();
	}

	// ========================= METHODS ===================================

	/**
	 * Return the namespace list property.
	 * <p>
	 * Note: the original list is returned, so its internals can be manipulated by a
	 * client. We could return an immutable copy of the list, but do not do it, for better
	 * performance.
	 *
	 * @return the namespace list
	 */
	public List<DtsNamespaceToImpl> getNamespaceList()
	{
		final List<DtsNamespaceToImpl> copy = CollectionUtil
				.<DtsNamespaceToImpl> newList();
		for (final DtsNamespace namespace : namespaceList)
		{
			copy.add(new DtsNamespaceToImpl().copyFrom(namespace));
		}
		return copy;
	}

	/**
	 * Add a namespace to namespace list.
	 *
	 * @param namespace
	 *            namespace to add
	 * @return <code>true</code>
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addNamespace(final DtsNamespace namespace)
	{
		return namespaceList.add(new DtsNamespaceToImpl().copyFrom(namespace));
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int getSize()
	{
		return namespaceList.size();
	}

}
