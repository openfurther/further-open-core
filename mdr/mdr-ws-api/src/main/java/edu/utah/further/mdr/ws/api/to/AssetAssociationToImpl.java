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
package edu.utah.further.mdr.ws.api.to;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.GenericJaxbMapAdapter;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.mdr.api.domain.asset.AssetAssociation;
import edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty;
import edu.utah.further.mdr.api.to.asset.AssetAssociationTo;

/**
 * {@link AssetAssociation} transfer object implementation
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version May 13, 2013
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.MDR, name = AssetAssociationToImpl.ENTITY_NAME, propOrder =
{ "id", "leftType", "leftTypeId", "leftNamespace", "leftNamespaceId", "leftAsset",
		"leftAssetId", "association", "associationId", "rightType", "rightTypeId",
		"rightNamespace", "rightNamespaceId", "rightAsset", "rightAssetId", "properties" })
@XmlRootElement(namespace = XmlNamespace.MDR, name = AssetAssociationToImpl.ENTITY_NAME)
public class AssetAssociationToImpl implements AssetAssociationTo
{
	// ========================= CONSTANTS ====================================

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1871396410750215361L;

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "assetAssociation";

	// ========================= FIELDS ====================================

	/**
	 * Entity identifier
	 */
	@XmlElement
	private Long id;

	/**
	 * The left side of the association's type
	 */
	@XmlElement
	private String leftType;

	/**
	 * The left side asset type id.
	 */
	@XmlElement
	private Long leftTypeId;

	/**
	 * The left side of the association's namespace
	 */
	@XmlElement
	private String leftNamespace;

	/**
	 * The left side asset id namespace.
	 */
	@XmlElement
	private Long leftNamespaceId;

	/**
	 * The left side of the association's asset
	 */
	@XmlElement
	private String leftAsset;

	/**
	 * The left side asset id.
	 */
	@XmlElement
	private Long leftAssetId;

	/**
	 * The association
	 */
	@XmlElement
	private String association;

	/**
	 * The association asset id.
	 */
	@XmlElement
	private Long associationId;

	/**
	 * The right side of the association's type
	 */
	@XmlElement
	private String rightType;

	/**
	 * The right type asset id;
	 */
	@XmlElement
	private Long rightTypeId;

	/**
	 * The right side of the association's namespace
	 */
	@XmlElement
	private String rightNamespace;

	/**
	 * The right asset id namespace.
	 */
	@XmlElement
	private Long rightNamespaceId;

	/**
	 * The right side of the association's namespace
	 */
	@XmlElement
	private String rightAsset;

	/**
	 * The right side asset id.
	 */
	@XmlElement
	private Long rightAssetId;

	@XmlJavaTypeAdapter(GenericJaxbMapAdapter.class)
	private Map<String, String> properties;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= IMPLEMENTATION: AssetAssocationTo =====================

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
	 * Return the association property.
	 * 
	 * @return the association
	 */
	@Override
	public String getAssociation()
	{
		return association;
	}

	/**
	 * Set a new value for the association property.
	 * 
	 * @param association
	 *            the association to set
	 */
	@Override
	public void setAssociation(final String association)
	{
		this.association = association;
	}

	/**
	 * Return the associationId property.
	 * 
	 * @return the associationId
	 */
	@Override
	public Long getAssociationId()
	{
		return associationId;
	}

	/**
	 * Set a new value for the associationId property.
	 * 
	 * @param associationId
	 *            the associationId to set
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
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.AssetAssociation#getProperties()
	 */
	@Override
	public Collection<AssetAssociationProperty> getProperties()
	{
		final List<AssetAssociationProperty> propertiesList = new ArrayList<>();
		for (final Entry<String, String> property : this.properties.entrySet())
		{
			final AssetAssociationProperty assetAssociationProp = new AssetAssociationPropertyToImpl();
			assetAssociationProp.setName(property.getKey());
			assetAssociationProp.setValue(property.getValue());
			propertiesList.add(assetAssociationProp);
		}

		return propertiesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.AssetAssociation#setProperties(java.util.
	 * Collection)
	 */
	@Override
	public void setProperties(final Collection<AssetAssociationProperty> properties)
	{
		final Map<String, String> propertiesMap = new HashMap<>();
		for (final AssetAssociationProperty property : properties)
		{
			propertiesMap.put(property.getName(), property.getValue());
		}
		this.properties = propertiesMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.AssetAssociation#getPropertiesAsMap()
	 */
	@Override
	public Map<String, String> getPropertiesAsMap()
	{
		return properties;
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public AssetAssociationToImpl copyFrom(final AssetAssociation other)
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
			this.properties = other.getPropertiesAsMap();
		}

		return this;
	}

}
