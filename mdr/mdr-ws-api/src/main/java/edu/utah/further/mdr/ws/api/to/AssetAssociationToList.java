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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.mdr.api.domain.asset.AssetAssociation;
import edu.utah.further.mdr.api.to.asset.AssetAssociationTo;

/**
 * A {@link Collection} of {@link AssetAssociationTo} transfer objects.
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
@XmlType(namespace = XmlNamespace.MDR, name = AssetAssociationToList.ENTITY_NAME, propOrder =
{ "assetAssociations" })
@XmlRootElement(namespace = XmlNamespace.MDR, name = AssetAssociationToList.ENTITY_NAME)
public class AssetAssociationToList
{
	// ========================= CONSTANTS ====================================

	/**
	 * Serial Version UID
	 */

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "assetAssociationList";

	// ========================= FIELDS ====================================

	/**
	 * A list of {@link AssetAssociationToImpl}'s
	 */
	@XmlElement(name = "assetAssociation")
	private List<AssetAssociationToImpl> assetAssociations;

	// ========================= Constructors ====================================

	/**
	 * Default constructor
	 */
	public AssetAssociationToList()
	{
	}

	/**
	 * 
	 * @param associations
	 */
	public AssetAssociationToList(final List<AssetAssociation> associations)
	{
		super();
		final List<AssetAssociationToImpl> assetAssocations = new ArrayList<>();
		for (final AssetAssociation association : associations)
		{
			assetAssocations.add(new AssetAssociationToImpl().copyFrom(association));
		}
		this.assetAssociations = assetAssocations;
	}

	// ========================= GET/SET ====================================

	/**
	 * Return the associations property.
	 * 
	 * @return the associations
	 */
	public List<AssetAssociationToImpl> getAssociations()
	{
		return assetAssociations;
	}

	/**
	 * Set a new value for the associations property.
	 * 
	 * @param associations
	 *            the associations to set
	 */
	public void setAssociations(final List<AssetAssociationToImpl> associations)
	{
		this.assetAssociations = associations;
	}

}
