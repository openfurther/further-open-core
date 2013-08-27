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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.tree.ListComposite;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A service meta data transfer object. A useful utility class for both services and
 * clients.
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
@XmlType(namespace = XmlNamespace.MDR, name = MetaData.ENTITY_NAME, propOrder =
{ "name", "description", "children" })
@XmlRootElement(namespace = XmlNamespace.MDR, name = MetaData.ENTITY_NAME)
public class MetaData implements ListComposite<MetaData>
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "metaData";

	// ========================= FIELDS ====================================

	/**
	 * Service name.
	 */
	@XmlElement(name = "name", required = false)
	private String name;

	/**
	 * Service long description.
	 */
	@XmlElement(name = "description", required = false)
	private String description;

	/**
	 * Metadata children collection.
	 */
	@XmlElement(name = "metaData", required = false)
	// It would be nice to have this List<T> where MetaData<T extends MetaData<T>> extends
	// ListComposite<T>, but JAXB can't work with generics.
	private List<MetaData> children = newList();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Sets no fields. Required for JAXB.
	 */
	public MetaData()
	{
		super();
	}

	/**
	 * Sets all fields.
	 * 
	 * @param name
	 * @param description
	 */
	public MetaData(final String name, final String description)
	{
		super();
		this.name = name;
		this.description = description;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	/**
	 * Return the hash code of this object. Includes the name field only.
	 * 
	 * @return hash code of this object
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.appendSuper(super.hashCode())
				.append(name)
				.toHashCode();
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

		final MetaData that = (MetaData) o;
		return new EqualsBuilder().append(this.name, that.name).isEquals();
	}

	// ========================= IMPLEMENTATION: Named =====================

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	public String getName()
	{
		return name;
	}

	// ========================= METHODS ===================================

	/**
	 * Add a metaData to the metaData list.
	 * 
	 * @param metaData
	 *            metaData to add
	 * @return <code>true</code>
	 * @see java.util.List#add(java.lang.Object)
	 */
	public void addChild(final MetaData metaData)
	{
		children.add(metaData);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int getNumChildren()
	{
		return children.size();
	}

	// ========================= IMPLEMENTATION: Composite<MetaData> =======

	/**
	 * Note: returns a defensive copy of the children property.
	 * 
	 * @return a defensive copy of the children property
	 * @see edu.utah.further.core.api.tree.Composite#getChildren()
	 */
	@Override
	public List<MetaData> getChildren()
	{
		return newList(children);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.dts.domain.uml.UmlElement#getHasChildren()
	 */
	@Override
	public boolean getHasChildren()
	{
		return !children.isEmpty();
	}

	// ========================= METHODS ===================================

	/**
	 * Remove all children of this object.
	 * 
	 * @see java.util.List#clear()
	 */
	public void removeAllChildren()
	{
		children.clear();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Return the description property.
	 * 
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Set a new value for the description property.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description)
	{
		this.description = description;
	}

	// ========================= PRIVATE METHODS ===========================
}
