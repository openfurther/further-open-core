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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.core.api.tree.ListComposite;
import edu.utah.further.core.api.xml.TimestampAdapter;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A composite service transfer object for a single piece of data within the FQE.
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
@XmlType(namespace = Data.ENTITY_NS, name = Data.ENTITY_NAME, propOrder =
{ "name", "description", "date", "children" })
@XmlRootElement(namespace = Data.ENTITY_NS, name = Data.ENTITY_NAME)
public class Data implements ListComposite<Data>, Named, Serializable
{
	// ========================= CONSTANTS =================================

	/**
	 * Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * JAXB name of this entity.
	 */
	public static final String ENTITY_NAME = "data";

	/**
	 * JAXB namespace of this entity.
	 */
	public static final String ENTITY_NS = XmlNamespace.FQE;

	/**
	 * A comparator of {@link Data} objects by their names.
	 */
	private static final Comparator<Data> DATA_COMPARATOR_BY_NAME = new Comparator<Data>()
	{
		@Override
		public int compare(final Data arg0, final Data arg1)
		{
			return new CompareToBuilder()
					.append(arg0.getName(), arg1.getName())
					.toComparison();
		}
	};

	// ========================= FIELDS ====================================

	/**
	 * Unique identifier of the service that generated this meta data object.
	 */
	@XmlElement(name = "name", required = false)
	private String name;

	/**
	 * Time of this object creation.
	 */
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	@XmlElement(name = "date", required = false)
	private final Timestamp date = new Timestamp(TimeService.getMillis());

	/**
	 * Service long description.
	 */
	@XmlElement(name = "description", required = false)
	private String description;

	/**
	 * Metadata children collection.
	 */
	// @XmlElement(name = "data", required = false)
	@XmlElementRef
	private final List<Data> children = newList();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Sets no fields.
	 */
	public Data()
	{
		super();
	}

	/**
	 * Sets all fields.
	 * 
	 * @param name
	 * @param description
	 */
	public Data(final String name, final String description)
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
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("description",
				getDescription()).toString();
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

		final Data that = (Data) o;
		return new EqualsBuilder().append(this.name, that.name).isEquals();
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

	// ========================= IMPLEMENTATION: Composite<Data> ===========

	/**
	 * Returns a defensive copy of the children list.
	 * 
	 * @return copy of the children list
	 * @see edu.utah.further.core.api.tree.Composite#getChildren()
	 */
	@Override
	public List<Data> getChildren()
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
	 * Add a child datum to the data list.
	 * 
	 * @param data
	 *            data to add
	 * @return <code>true</code>
	 * @see java.util.List#add(java.lang.Object)
	 */
	public void addChild(final Data data)
	{
		children.add(data);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int getNumChildren()
	{
		return children.size();
	}

	/**
	 * Effect deterministic children sorting of sources by ascending {@link Data} name.
	 * This can't be done by trying to sort the result of {@link #getChildren()} in a
	 * client code, because it returns a defensive copy.
	 */
	public void sortChildrenByName()
	{
		Collections.sort(children, DATA_COMPARATOR_BY_NAME);
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

	/**
	 * Return the date property.
	 * 
	 * @return the date
	 */
	public Timestamp getDate()
	{
		return date;
	}

	// ========================= PRIVATE METHODS ===========================
}
