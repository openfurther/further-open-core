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
 * 
 * <p>Java class for RuleDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RuleDefinition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ruleType" type="{}RuleType"/>
 *         &lt;element name="ruleCondition" type="{}RuleCondition"/>
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
@XmlType(name = "RuleDefinition", propOrder =
{ "ruleType", "ruleCondition" })
public class RuleDefinition
{

	@XmlElement(required = true)
	protected RuleType ruleType;
	@XmlElement(required = true)
	protected RuleCondition ruleCondition;

	/**
	 * Gets the value of the ruleType property.
	 * 
	 * @return possible object is {@link RuleType }
	 * 
	 */
	public RuleType getRuleType()
	{
		return ruleType;
	}

	/**
	 * Sets the value of the ruleType property.
	 * 
	 * @param value
	 *            allowed object is {@link RuleType }
	 * 
	 */
	public void setRuleType(RuleType value)
	{
		this.ruleType = value;
	}

	/**
	 * Gets the value of the ruleCondition property.
	 * 
	 * @return possible object is {@link RuleCondition }
	 * 
	 */
	public RuleCondition getRuleCondition()
	{
		return ruleCondition;
	}

	/**
	 * Sets the value of the ruleCondition property.
	 * 
	 * @param value
	 *            allowed object is {@link RuleCondition }
	 * 
	 */
	public void setRuleCondition(RuleCondition value)
	{
		this.ruleCondition = value;
	}

}
