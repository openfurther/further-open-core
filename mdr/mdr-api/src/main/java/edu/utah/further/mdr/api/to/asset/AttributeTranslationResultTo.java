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
package edu.utah.further.mdr.api.to.asset;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.GenericJaxbMapAdapter;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * Represents a result of an attribute translation.
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
 * @version Apr 12, 2012
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.MDR, name = AttributeTranslationResultTo.ENTITY_NAME, propOrder =
{ "translatedAttribute", "attributeProperties" })
@XmlRootElement(namespace = XmlNamespace.MDR, name = AttributeTranslationResultTo.ENTITY_NAME)
public class AttributeTranslationResultTo
{
	// ========================= CONSTANTS =================================

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "attributeTranslationResult";

	// ========================= FIELDS ====================================

	/**
	 * The translated attribute name
	 */
	@XmlElement(name = "translatedAttribute")
	private String translatedAttribute;

	/**
	 * Properties about the attribute such as its Java type
	 */
	@XmlElement(name = "properties")
	@XmlJavaTypeAdapter(GenericJaxbMapAdapter.class)
	private Map<String, String> attributeProperties;

	/**
	 * Return the translatedAttribute property.
	 * 
	 * @return the translatedAttribute
	 */
	public String getTranslatedAttribute()
	{
		return translatedAttribute;
	}

	/**
	 * Set a new value for the translatedAttribute property.
	 * 
	 * @param translatedAttribute
	 *            the translatedAttribute to set
	 */
	public void setTranslatedAttribute(final String translatedAttribute)
	{
		this.translatedAttribute = translatedAttribute;
	}

	/**
	 * Return the attributeProperties property.
	 * 
	 * @return the attributeProperties
	 */
	public Map<String, String> getAttributeProperties()
	{
		return attributeProperties;
	}

	/**
	 * Set a new value for the attributeProperties property.
	 * 
	 * @param attributeProperties
	 *            the attributeProperties to set
	 */
	public void setAttributeProperties(final Map<String, String> attributeProperties)
	{
		this.attributeProperties = attributeProperties;
	}

}
