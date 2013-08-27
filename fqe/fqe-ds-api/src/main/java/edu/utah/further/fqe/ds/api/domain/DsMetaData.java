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
package edu.utah.further.fqe.ds.api.domain;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A data source's meta data transfer object.
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
@XmlType(namespace = XmlNamespace.FQE, name = DsMetaData.ENTITY_NAME, propOrder =
{ "namespaceId", "state" })
@XmlRootElement(namespace = XmlNamespace.FQE, name = DsMetaData.ENTITY_NAME)
public class DsMetaData extends Data
{
	// ========================= CONSTANTS =================================

	/**
	 * Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * JAXB name of this entity.
	 */
	@SuppressWarnings("hiding")
	static final String ENTITY_NAME = "dsMetaData";

	// ========================= FIELDS ====================================

	@XmlElement(name = "namespaceId", required = true)
	private Long namespaceId;

	/**
	 * Data source current state.
	 */
	@XmlElement(name = "state", required = false)
	private DsState state;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Sets no fields.
	 */
	public DsMetaData()
	{
		super();
	}

	/**
	 * Sets all fields.
	 * 
	 * @param name
	 * @param description
	 */
	public DsMetaData(final String name, final String description,
			final Long namespaceId, final DsState state)
	{
		super(name, description);
		setState(state);
		setNamespaceId(namespaceId);
	}
	
	/**
	 * Sets name and description fields.
	 * 
	 * @param name
	 * @param description
	 */
	public DsMetaData(final String name, final String description)
	{
		super(name, description);
	}

	/**
	 * Copy-constructor.
	 * 
	 * @param other
	 *            another meta data object to copy fields from
	 */
	public DsMetaData(final DsMetaData other)
	{
		this(other.getName(), other.getDescription(), other.getNamespaceId(), other.getState());
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("description", getDescription())
				.append("state", getState())
				.append("namespaceId", getNamespaceId())
				.toString();
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the namespaceId property.
	 * 
	 * @return the namespaceId
	 */
	public Long getNamespaceId()
	{
		return namespaceId;
	}

	/**
	 * Set a new value for the namespaceId property.
	 * 
	 * @param namespaceId
	 *            the namespaceId to set
	 */
	public void setNamespaceId(final Long namespaceId)
	{
		this.namespaceId = namespaceId;
	}

	/**
	 * Return the state property.
	 * 
	 * @return the state
	 */
	public DsState getState()
	{
		return state;
	}

	/**
	 * Set a new value for the state property.
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(final DsState state)
	{
		this.state = state;
	}

	// ========================= PRIVATE METHODS ===========================
}
