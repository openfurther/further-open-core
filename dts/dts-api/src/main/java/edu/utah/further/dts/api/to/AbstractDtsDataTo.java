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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.namespace.DtsData;
import edu.utah.further.dts.api.domain.namespace.DtsDataType;

/**
 * A transfer object for a generic FURTHeR DTS object.
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
 * @version Dec 17, 2008
 */
@Implementation
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = AbstractDtsDataTo.ENTITY_NAME, propOrder =
{ "type", "id", "name", "code" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = AbstractDtsDataTo.ENTITY_NAME)
abstract class AbstractDtsDataTo implements DtsDataTo
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "abstractDtsData";

	protected static final String UNSUPPORTED_OPERATION_MESSAGE = "DTS Concept TO operation that is not yet JAXB-annotated";

	// ========================= FIELDS ====================================

	/**
	 * DTS Data type of this object.
	 */
	@Final
	@XmlElement(name = "type", nillable = false)
	private DtsDataType type;

	/**
	 * Concept's ID within its namespace. Copied from {@link #concept} and cached.
	 */
	@XmlElement(name = "id", nillable = false)
	private int id;

	/**
	 * Concept's name. Copied from {@link #concept} and cached.
	 */
	@XmlElement(name = "name", nillable = false)
	private String name;

	/**
	 * Concept's code. Copied from {@link #concept} and cached.
	 */
	@XmlElement(name = "code", nillable = false)
	private String code;

	// ========================= DEPENDENCIES ==============================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * For JAXB.
	 */
	protected AbstractDtsDataTo()
	{
		super();
	}

	/**
	 * @param type
	 * @param id
	 * @param name
	 * @param code
	 */
	protected AbstractDtsDataTo(final DtsDataType type)
	{
		super();
		this.type = type;
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public DtsDataTo copyFrom(final DtsData other)
	{
		if (other == null)
		{
			return this;
		}
		this.code = other.getCode();
		this.id = other.getId();
		this.name = other.getName();
		return this;
	}

	// ========================= IMPLEMENTATION: Named =====================

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	// ========================= IMPLEMENTATION: DtsData ===================

	/**
	 * Return the type property.
	 *
	 * @return the type
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getType()
	 */
	@Override
	public DtsDataType getType()
	{
		return type;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#getCode()
	 */
	@Override
	public String getCode()
	{
		return code;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#getId()
	 */
	@Override
	public int getId()
	{
		return id;
	}

	/**
	 * @param id
	 * @see edu.utah.further.dts.api.to.DtsDataTo#setId(int)
	 */
	@Override
	public void setId(final int id)
	{
		this.id = id;
	}

	/**
	 * @param name
	 * @see edu.utah.further.dts.api.to.DtsDataTo#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @param code
	 * @see edu.utah.further.dts.api.to.DtsDataTo#setCode(java.lang.String)
	 */
	@Override
	public void setCode(final String code)
	{
		this.code = code;
	}

	/**
	 * Set a new value for the type property.
	 *
	 * @param type
	 *            the type to set
	 */
	@Override
	public void setType(final DtsDataType type)
	{
		this.type = type;
	}

	// ========================= PRIVATE METHODS ===========================
}
