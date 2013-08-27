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
 * <p>Java class for ValidationRule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValidationRule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="criteriaIdentifier" type="{}CriteriaIdentifier"/>
 *         &lt;element name="rule" type="{}Rule"/>
 *         &lt;element name="action" type="{}ActionType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValidationRule", propOrder =
{ "criteriaIdentifier", "rule", "action" })
public class ValidationRule
{

	@XmlElement(required = true)
	protected CriteriaIdentifier criteriaIdentifier;
	@XmlElement(required = true)
	protected Rule rule;
	@XmlElement(required = true)
	protected ActionType action;

	/**
	 * Gets the value of the criteriaIdentifier property.
	 * 
	 * @return possible object is {@link CriteriaIdentifier }
	 * 
	 */
	public CriteriaIdentifier getCriteriaIdentifier()
	{
		return criteriaIdentifier;
	}

	/**
	 * Sets the value of the criteriaIdentifier property.
	 * 
	 * @param value
	 *            allowed object is {@link CriteriaIdentifier }
	 * 
	 */
	public void setCriteriaIdentifier(CriteriaIdentifier value)
	{
		this.criteriaIdentifier = value;
	}

	/**
	 * Gets the value of the rule property.
	 * 
	 * @return possible object is {@link Rule }
	 * 
	 */
	public Rule getRule()
	{
		return rule;
	}

	/**
	 * Sets the value of the rule property.
	 * 
	 * @param value
	 *            allowed object is {@link Rule }
	 * 
	 */
	public void setRule(Rule value)
	{
		this.rule = value;
	}

	/**
	 * Gets the value of the action property.
	 * 
	 * @return possible object is {@link ActionType }
	 * 
	 */
	public ActionType getAction()
	{
		return action;
	}

	/**
	 * Sets the value of the action property.
	 * 
	 * @param value
	 *            allowed object is {@link ActionType }
	 * 
	 */
	public void setAction(ActionType value)
	{
		this.action = value;
	}

}
