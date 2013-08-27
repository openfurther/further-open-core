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
package edu.utah.further.fqe.impl.validation.domain;

/**
 * <p>Java class for CriteriaIdentifier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CriteriaIdentifier">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="observation" type="{}ObservationType"/>
 *         &lt;element name="relationship" type="{}RelationshipType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version Feb 6, 2012
 */

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.collections.CollectionUtil;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CriteriaIdentifier", propOrder =
{ "observation", "relationship" })
public class CriteriaIdentifier
{

	@XmlList
	@XmlElement(required = true)
	protected List<String> observation;
	@XmlElement(required = true)
	protected RelationshipType relationship;

	/**
	 * Gets the value of the observation property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present inside the
	 * JAXB object. This is why there is not a <CODE>set</CODE> method for the observation
	 * property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getObservation().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getObservation()
	{
		if (observation == null)
		{
			observation = CollectionUtil.newList();
		}
		return this.observation;
	}

	/**
	 * Gets the value of the relationship property.
	 * 
	 * @return possible object is {@link RelationshipType }
	 * 
	 */
	public RelationshipType getRelationship()
	{
		return relationship;
	}

	/**
	 * Sets the value of the relationship property.
	 * 
	 * @param value
	 *            allowed object is {@link RelationshipType }
	 * 
	 */
	public void setRelationship(final RelationshipType value)
	{
		this.relationship = value;
	}

}
