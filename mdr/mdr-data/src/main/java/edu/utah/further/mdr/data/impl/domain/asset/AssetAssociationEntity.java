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

import static edu.utah.further.core.api.collections.CollectionUtil.newMap;

import java.util.Collection;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.AssetAssociation;
import edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty;

/**
 * An {@link Asset} association entity.
 * 
 * The "table" this entity goes against is actually a view that has already joined the
 * data from individual assets. Rick suggested this view as being the interface if case he
 * changed the way some of the asset associations were done. The entity could have just as
 * well as been done against the actual association table but this was a little easier.
 * The existing strings in this impl would have been Asset's and calls would be
 * leftAsset.namespace = xyz AND leftAsset.label = abc.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Mar 27, 2012
 */
@Entity
@Table(name = "ASSET_ASSOC_V")
public class AssetAssociationEntity implements AssetAssociation
{
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 4154360680412517890L;

	/**
	 * Entity identifier
	 */
	@Id
	@GeneratedValue
	@Column(name = "ASSET_ASSOC_ID")
	private Long id;

	/**
	 * The left side of the association's type
	 */
	@Column(name = "LS_TYPE_LABEL")
	private String leftType;

	/**
	 * The left side asset id type of the association. Note: not joined to ASSET as it's
	 * already joined in the view.
	 */
	@Column(name = "LS_TYPE_ASSET_ID")
	private Long leftTypeId;

	/**
	 * The left side of the association's namespace.
	 */
	@Column(name = "LS_NAMESPACE_LABEL")
	private String leftNamespace;

	/**
	 * The left side asset id namespace of the association. Note: not joined to ASSET as
	 * it's already joined in the view.
	 */
	@Column(name = "LS_NAMESPACE_ASSET_ID")
	private Long leftNamespaceId;

	/**
	 * The left side of the association's asset
	 */
	@Column(name = "LS_ASSET_LABEL")
	private String leftAsset;

	/**
	 * The left side asset id of the association. Note: not joined to ASSET as it's
	 * already joined in the view.
	 */
	@Column(name = "LS_ASSET_ID")
	private Long leftAssetId;

	/**
	 * The association
	 */
	@Column(name = "ASSOC_ASSET_LABEL")
	private String association;

	/**
	 * The association asset id of the association. Note: not joined to ASSET as it's
	 * already joined in the view.
	 */
	@Column(name = "ASSOC_ASSET_ID")
	private Long associationId;

	/**
	 * The right side of the association's type
	 */
	@Column(name = "RS_TYPE_LABEL")
	private String rightType;

	/**
	 * The right side asset id type of the association. Note: not joined to ASSET as it's
	 * already joined in the view.
	 */
	@Column(name = "RS_TYPE_ASSET_ID")
	private Long rightTypeId;

	/**
	 * The right side of the association's namespace
	 */
	@Column(name = "RS_NAMESPACE_LABEL")
	private String rightNamespace;

	/**
	 * The right side asset id namespace of the association. Note: not joined to ASSET as
	 * it's already joined in the view.
	 */
	@Column(name = "RS_NAMESPACE_ASSET_ID")
	private Long rightNamespaceId;

	/**
	 * The right side of the association's namespace
	 */
	@Column(name = "RS_ASSET_LABEL")
	private String rightAsset;

	/**
	 * The right side asset id of the association. Note: not joined to ASSET as it's
	 * already joined in the view.
	 */
	@Column(name = "RS_ASSET_ID")
	private Long rightAssetId;

	/**
	 * A collection of {@link AssetAssociationProperty}'s
	 */
	@Column
	@OneToMany(targetEntity = AssetAssociationPropertyEntity.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "ASSET_ASSOC_ID")
	private Collection<AssetAssociationProperty> properties;

	/**
	 * Return the id property.
	 * 
	 * @return the id
	 */
	@Override
	public Long getId()
	{
		return id;
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
	 * Return the leftType property.
	 * 
	 * @return the leftType
	 */
	@Override
	public String getLeftType()
	{
		return leftType;
	}

	/**
	 * Set a new value for the leftType property.
	 * 
	 * @param leftType
	 *            the leftType to set
	 */
	@Override
	public void setLeftType(final String leftType)
	{
		this.leftType = leftType;
	}

	/**
	 * Return the leftTypeId property.
	 * 
	 * @return the leftTypeId
	 */
	@Override
	public Long getLeftTypeId()
	{
		return leftTypeId;
	}

	/**
	 * Set a new value for the leftTypeId property.
	 * 
	 * @param leftTypeId
	 *            the leftTypeId to set
	 */
	@Override
	public void setLeftTypeId(final Long leftTypeId)
	{
		this.leftTypeId = leftTypeId;
	}

	/**
	 * Return the leftNamespace property.
	 * 
	 * @return the leftNamespace
	 */
	@Override
	public String getLeftNamespace()
	{
		return leftNamespace;
	}

	/**
	 * Set a new value for the leftNamespace property.
	 * 
	 * @param leftNamespace
	 *            the leftNamespace to set
	 */
	@Override
	public void setLeftNamespace(final String leftNamespace)
	{
		this.leftNamespace = leftNamespace;
	}

	/**
	 * Return the leftNamespaceId property.
	 * 
	 * @return the leftNamespaceId
	 */
	@Override
	public Long getLeftNamespaceId()
	{
		return leftNamespaceId;
	}

	/**
	 * Set a new value for the leftNamespaceId property.
	 * 
	 * @param leftNamespaceId
	 *            the leftNamespaceId to set
	 */
	@Override
	public void setLeftNamespaceId(final Long leftNamespaceId)
	{
		this.leftNamespaceId = leftNamespaceId;
	}

	/**
	 * Return the leftAsset property.
	 * 
	 * @return the leftAsset
	 */
	@Override
	public String getLeftAsset()
	{
		return leftAsset;
	}

	/**
	 * Set a new value for the leftAsset property.
	 * 
	 * @param leftAsset
	 *            the leftAsset to set
	 */
	@Override
	public void setLeftAsset(final String leftAsset)
	{
		this.leftAsset = leftAsset;
	}

	/**
	 * Return the leftAssetId property.
	 * 
	 * @return the leftAssetId
	 */
	@Override
	public Long getLeftAssetId()
	{
		return leftAssetId;
	}

	/**
	 * Set a new value for the leftAssetId property.
	 * 
	 * @param leftAssetId
	 *            the leftAssetId to set
	 */
	@Override
	public void setLeftAssetId(final Long leftAssetId)
	{
		this.leftAssetId = leftAssetId;
	}

	/**
	 * Return the associationLabel property.
	 * 
	 * @return the associationLabel
	 */
	@Override
	public String getAssociation()
	{
		return association;
	}

	/**
	 * Set a new value for the associationLabel property.
	 * 
	 * @param associationLabel
	 *            the associationLabel to set
	 */
	@Override
	public void setAssociation(final String association)
	{
		this.association = association;
	}

	/**
	 * Return the assocationId property.
	 * 
	 * @return the assocationId
	 */
	@Override
	public Long getAssociationId()
	{
		return associationId;
	}

	/**
	 * Set a new value for the assocationId property.
	 * 
	 * @param assocationId
	 *            the assocationId to set
	 */
	@Override
	public void setAssociationId(final Long associationId)
	{
		this.associationId = associationId;
	}

	/**
	 * Return the rightType property.
	 * 
	 * @return the rightType
	 */
	@Override
	public String getRightType()
	{
		return rightType;
	}

	/**
	 * Set a new value for the rightType property.
	 * 
	 * @param rightType
	 *            the rightType to set
	 */
	@Override
	public void setRightType(final String rightType)
	{
		this.rightType = rightType;
	}

	/**
	 * Return the rightTypeId property.
	 * 
	 * @return the rightTypeId
	 */
	@Override
	public Long getRightTypeId()
	{
		return rightTypeId;
	}

	/**
	 * Set a new value for the rightTypeId property.
	 * 
	 * @param rightTypeId
	 *            the rightTypeId to set
	 */
	@Override
	public void setRightTypeId(final Long rightTypeId)
	{
		this.rightTypeId = rightTypeId;
	}

	/**
	 * Return the rightNamespace property.
	 * 
	 * @return the rightNamespace
	 */
	@Override
	public String getRightNamespace()
	{
		return rightNamespace;
	}

	/**
	 * Set a new value for the rightNamespace property.
	 * 
	 * @param rightNamespace
	 *            the rightNamespace to set
	 */
	@Override
	public void setRightNamespace(final String rightNamespace)
	{
		this.rightNamespace = rightNamespace;
	}

	/**
	 * Return the rightNamespaceId property.
	 * 
	 * @return the rightNamespaceId
	 */
	@Override
	public Long getRightNamespaceId()
	{
		return rightNamespaceId;
	}

	/**
	 * Set a new value for the rightNamespaceId property.
	 * 
	 * @param rightNamespaceId
	 *            the rightNamespaceId to set
	 */
	@Override
	public void setRightNamespaceId(final Long rightNamespaceId)
	{
		this.rightNamespaceId = rightNamespaceId;
	}

	/**
	 * Return the rightAsset property.
	 * 
	 * @return the rightAsset
	 */
	@Override
	public String getRightAsset()
	{
		return rightAsset;
	}

	/**
	 * Set a new value for the rightAsset property.
	 * 
	 * @param rightAsset
	 *            the rightAsset to set
	 */
	@Override
	public void setRightAsset(final String rightAsset)
	{
		this.rightAsset = rightAsset;
	}

	/**
	 * Return the rightAssetId property.
	 * 
	 * @return the rightAssetId
	 */
	@Override
	public Long getRightAssetId()
	{
		return rightAssetId;
	}

	/**
	 * Set a new value for the rightAssetId property.
	 * 
	 * @param rightAssetId
	 *            the rightAssetId to set
	 */
	@Override
	public void setRightAssetId(final Long rightAssetId)
	{
		this.rightAssetId = rightAssetId;
	}

	/**
	 * Return the properties property.
	 * 
	 * @return the properties
	 */
	@Override
	public Collection<AssetAssociationProperty> getProperties()
	{
		return properties;
	}

	/**
	 * Set a new value for the properties property.
	 * 
	 * @param properties
	 *            the properties to set
	 */
	@Override
	public void setProperties(final Collection<AssetAssociationProperty> properties)
	{
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.AssetAssociation#getPropertiesAsMap()
	 */
	@Override
	public Map<String, String> getPropertiesAsMap()
	{
		final Map<String, String> propertyMap = newMap();
		for (final AssetAssociationProperty property : getProperties())
		{
			propertyMap.put(property.getName(), property.getValue());
		}
		return propertyMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public AssetAssociation copyFrom(final AssetAssociation other)
	{
		if (other != null)
		{
			this.id = other.getId();
			this.leftType = other.getLeftType();
			this.leftTypeId = other.getLeftTypeId();
			this.leftNamespace = other.getLeftNamespace();
			this.leftNamespaceId = other.getLeftNamespaceId();
			this.leftAsset = other.getLeftAsset();
			this.leftAssetId = other.getLeftAssetId();
			this.association = other.getAssociation();
			this.associationId = other.getAssociationId();
			this.rightType = other.getRightType();
			this.rightTypeId = other.getRightTypeId();
			this.rightNamespace = other.getRightNamespace();
			this.rightNamespaceId = other.getRightNamespaceId();
			this.rightAsset = other.getRightAsset();
			this.rightAssetId = other.getRightAssetId();
			this.properties = other.getProperties();
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

		final AssetAssociationEntity that = (AssetAssociationEntity) object;
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
