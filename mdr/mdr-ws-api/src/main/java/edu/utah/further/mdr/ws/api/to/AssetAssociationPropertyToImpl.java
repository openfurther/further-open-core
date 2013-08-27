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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty;
import edu.utah.further.mdr.api.to.asset.AssetAssociationPropertyTo;

/**
 * ...
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
@XmlType(namespace = XmlNamespace.MDR, name = AssetAssociationPropertyToImpl.ENTITY_NAME, propOrder =
{ "name", "value" })
@XmlRootElement(namespace = XmlNamespace.MDR, name = AssetAssociationPropertyToImpl.ENTITY_NAME)
public class AssetAssociationPropertyToImpl implements AssetAssociationPropertyTo
{

	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1137566318945815501L;

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "assetAssociationProperty";

	// ========================= FIELDS ====================================

	@XmlTransient
	private Long id;

	@XmlElement
	private String name;

	@XmlElement
	private String value;

	// ========================= Get/Set ====================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty#getName()
	 */
	@Override
	public String getName()
	{
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty#setName(java.lang
	 * .String)
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty#getValue()
	 */
	@Override
	public String getValue()
	{
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty#setValue(java.lang
	 * .String)
	 */
	@Override
	public void setValue(final String value)
	{
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return this.getId();
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

}
