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
package edu.utah.further.mdr.data.common.domain.asset;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.lang.Final;
import edu.utah.further.mdr.api.domain.asset.BasicLookupValue;
import edu.utah.further.mdr.api.domain.asset.ResourceType;

/**
 * An MDR asset type lookup value.
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
 * @resource Mar 19, 2009
 */
@Entity
@Table(name = "ASSET_RESOURCE_TYPE")
public class ResourceTypeEntity implements ResourceType
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity.
	 */
	@Id
	@GeneratedValue
	@Column(name = "ASSET_RESOURCE_TYPE_ID")
	@Final
	private Long id;

	/**
	 * Asset type description.
	 */
	@Column(name = "ASSET_RESOURCE_TYPE_DSC", length = 100, nullable = true)
	private String label;

	/**
	 * Defines a weak ordering relation among lookup values. Groups are to be sorted by
	 * ascending lookup value <code>order</code> field value order.
	 * <p>
	 * Currently not in use; foresees resource type ordering in the future.
	 */
	// @Column(name = "LOOKUP_VALUE_ORDER", nullable = true)
	@Transient
	private Integer order;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param id
	 * @param label
	 * @param order
	 */
	public static ResourceTypeEntity newInstance(final Long id, final String label,
			final Integer order)
	{
		final ResourceTypeEntity instance = new ResourceTypeEntity();
		instance.id = id;
		instance.setLabel(label);
		instance.setOrder(order);
		return instance;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("id", id)
				.append("label", label);
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.LookupValue#compareTo(edu.utah.further.mdr.api.domain.asset.LookupValue)
	 */
	@Override
	public int compareTo(final ResourceType other)
	{
		return new CompareToBuilder().append(this.order, other.getOrder()).append(
				this.label, other.getLabel()).toComparison();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#copyFrom(edu.utah.further.mdr.api.domain.asset.Resource)
	 */
	@Override
	public ResourceType copyFrom(final BasicLookupValue other)
	{
		if (other == null)
		{
			return this;
		}

		// Identifier is not copied

		// Deep-copy fields
		this.label = other.getLabel();
		final Integer otherOrder = other.getOrder();
		if (otherOrder != null)
		{
			this.order = new Integer(otherOrder.intValue());
		}

		// Deep-copy collection references but soft-copy their elements

		return this;
	}

	// ========================= IMPLEMENTATION: PersistentEntity ==========

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.LookupValue#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= IMPLEMENTATION: LookupValue ===============

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.LookupValue#getLabel()
	 */
	@Override
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label
	 * @see edu.utah.further.mdr.api.domain.asset.LookupValue#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(final String label)
	{
		this.label = label;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.LookupValue#getOrder()
	 */
	@Override
	public Integer getOrder()
	{
		return order;
	}

	/**
	 * @param order
	 * @see edu.utah.further.mdr.api.domain.asset.LookupValue#setOrder(java.lang.Integer)
	 */
	@Override
	public void setOrder(final Integer order)
	{
		this.order = order;
	}

	// ========================= PRIVATE METHODS ===========================
}
