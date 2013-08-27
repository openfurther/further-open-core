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
package edu.utah.further.dts.api.to;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.namespace.InNamespace;

/**
 * A JavaBean that holds a minimal, uniquely identifying representation of a concept.
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
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsConceptUniqueId.ENTITY_NAME, propOrder =
{ "namespaceId", "id" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsConceptUniqueId.ENTITY_NAME)
public class DtsConceptUniqueId implements InNamespace
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "conceptUniqueId";

	// ========================= FIELDS ====================================

	/**
	 * DTS namespace.
	 */
	@XmlElement(name = "namespaceId", required = false)
	private int namespaceId;

	/**
	 * DTS concept property name.
	 */
	@XmlElement(name = "id", required = false)
	private int id;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Set no fields. Required for JAXB.
	 */
	public DtsConceptUniqueId()
	{
		super();
	}

	/**
	 * @param namespaceId
	 * @param id
	 */
	public DtsConceptUniqueId(final int namespaceId, final int id)
	{
		super();
		this.namespaceId = namespaceId;
		this.id = id;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("namespaceId", namespaceId)
				.append("id", id)
				.toString();
	}

	/**
	 * Return the hash code of this object. includes all fields.
	 *
	 * @return hash code of this object
	 * @see edu.utah.further.audit.api.domain.AbstractMessage#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37).append(namespaceId).append(id).toHashCode();
	}

	/**
	 * @param o
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object o)
	{
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (getClass() != o.getClass())
			return false;

		final DtsConceptUniqueId that = (DtsConceptUniqueId) o;
		return new EqualsBuilder()
				.append(namespaceId, that.namespaceId)
				.append(id, that.id)
				.isEquals();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the namespaceId property.
	 *
	 * @return the namespaceId
	 */
	@Override
	public int getNamespaceId()
	{
		return namespaceId;
	}

	/**
	 * Set a new value for the namespaceId property.
	 *
	 * @param namespaceId
	 *            the namespaceId to set
	 */
	@Override
	public void setNamespaceId(final int namespaceId)
	{
		this.namespaceId = namespaceId;
	}

	/**
	 * Return the id property.
	 *
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Set a new value for the id property.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(final int id)
	{
		this.id = id;
	}
}
