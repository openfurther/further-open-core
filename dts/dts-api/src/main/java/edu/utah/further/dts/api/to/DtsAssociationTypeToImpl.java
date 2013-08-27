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
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.association.DtsAssociationType;
import edu.utah.further.dts.api.domain.namespace.DtsData;

/**
 * A DTS association type implementation and a transfer object.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------<br>
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 8, 2008
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsAssociationTypeToImpl.ENTITY_NAME, propOrder =
{ "namespaceId", "inverseName" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsAssociationTypeToImpl.ENTITY_NAME)
public class DtsAssociationTypeToImpl extends AbstractDtsDataTo implements
		DtsAssociationTypeTo
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	@SuppressWarnings("hiding")
	static final String ENTITY_NAME = "associationType";

	// ========================= FIELDS ====================================

	/**
	 * Data fields: DTS Object.
	 */

	@XmlElement(name = "namespaceId", required = false)
	private int namespaceId;

	/**
	 * Data fields: Association.
	 */

	@XmlElement(name = "inverseName", nillable = false)
	private String inverseName;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * An explicit no-argument constructor is required for JAXB.
	 */
	public DtsAssociationTypeToImpl()
	{
		super();
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @param o
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object o)
	{
		// return namespace.equals(arg0);
		if (o == null)
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}

		// Works only because this method is final!!
		// if (getClass() != o.getClass())
		if (!ReflectionUtil.instanceOf(o, DtsAssociationTypeToImpl.class))
		{
			return false;
		}

		final DtsAssociationTypeToImpl that = (DtsAssociationTypeToImpl) o;
		return new EqualsBuilder().append(this.getName(), that.getName()).append(
				this.inverseName, that.inverseName).isEquals();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(getName()).append(inverseName).toHashCode();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("name",
				getName()).append("inverseName", inverseName).toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public DtsAssociationTypeToImpl copyFrom(final DtsData other)
	{
		if (other == null)
		{
			return this;
		}
		super.copyFrom(other);
		if (!ReflectionUtil.instanceOf(other, DtsAssociationType.class))
		{
			final DtsAssociationType that = (DtsAssociationType) other;
			this.inverseName = that.getInverseName();
		}

		// Do not copy REST links because they do not exist in business entities,
		// just in TOs.

		return this;
	}

	// ========================= IMPLEMENTATION: DtsAssociationType ========

	/**
	 * Return the inverseName property.
	 * 
	 * @return the inverseName
	 */
	@Override
	public String getInverseName()
	{
		return inverseName;
	}

	/**
	 * Set a new value for the inverseName property.
	 * 
	 * @param inverseName
	 *            the inverseName to set
	 */
	@Override
	public void setInverseName(final String inverseName)
	{
		this.inverseName = inverseName;
	}

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

	// ========================= PRIVATE METHODS ===========================
}
