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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.mdr.api.to.asset.AttributeTranslationResultTo;

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
 * @version May 22, 2013
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.MDR, name = AttributeTranslationResultToList.ENTITY_NAME, propOrder =
{ "translatedAttributes" })
@XmlRootElement(namespace = XmlNamespace.MDR, name = AttributeTranslationResultToList.ENTITY_NAME)
public class AttributeTranslationResultToList
{
	// ========================= CONSTANTS ====================================

	/**
	 * Serial Version UID
	 */

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "attributeTranslationResultList";

	// ========================= FIELDS ====================================

	@XmlElement(name = "attributeTranslationResult")
	private List<AttributeTranslationResultTo> translatedAttributes;

	/**
	 * Default constructor
	 */
	public AttributeTranslationResultToList()
	{
	}

	/**
	 * Take a list of {@link AttributeTranslationResultTo}'s
	 * 
	 * @param attributeTranslations
	 */
	public AttributeTranslationResultToList(
			final List<AttributeTranslationResultTo> attributeTranslations)
	{
		super();
		this.translatedAttributes = attributeTranslations;
	}
	
	// ========================= GET/SET ====================================

	/**
	 * Return the attributeTranslations property.
	 * 
	 * @return the attributeTranslations
	 */
	public List<AttributeTranslationResultTo> getAttributeTranslations()
	{
		return translatedAttributes;
	}

	/**
	 * Set a new value for the attributeTranslations property.
	 * 
	 * @param attributeTranslations
	 *            the attributeTranslations to set
	 */
	public void setAttributeTranslations(
			final List<AttributeTranslationResultTo> attributeTranslations)
	{
		this.translatedAttributes = attributeTranslations;
	}
}
