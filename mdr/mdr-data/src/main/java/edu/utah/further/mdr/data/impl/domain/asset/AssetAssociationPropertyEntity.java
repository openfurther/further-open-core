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
package edu.utah.further.mdr.data.impl.domain.asset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty;

/**
 * An asset association property entity
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2011 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-213-3288<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Apr 11, 2012
 */
@Entity(name = "ASSET_ASSOC_PROP")
public class AssetAssociationPropertyEntity implements AssetAssociationProperty
{

	@Id
	@Column(name = "ASSET_ASSOC_PROP_ID")
	private Long id;

	@Column(name = "PROP_NAME")
	private String name;

	@Column(name = "PROP_VAL")
	private String value;

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 6207499933073657155L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * Return the name property.
	 * 
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Return the value property.
	 * 
	 * @return the value
	 */
	@Override
	public String getValue()
	{
		return value;
	}

	/**
	 * Set a new value for the value property.
	 * 
	 * @param value
	 *            the value to set
	 */
	@Override
	public void setValue(final String value)
	{
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public AssetAssociationProperty copyFrom(final AssetAssociationProperty other)
	{
		if (other != null)
		{
			this.id = other.getId();
			this.name = other.getName();
			this.value = other.getValue();
		}

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object)
	{
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (getClass() != object.getClass())
			return false;

		final AssetAssociationPropertyEntity that = (AssetAssociationPropertyEntity) object;
		return new EqualsBuilder().append(this.getId(), that.getId()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getId()).toHashCode();
	}

}
