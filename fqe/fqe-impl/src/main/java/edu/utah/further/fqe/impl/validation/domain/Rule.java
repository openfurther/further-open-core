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
 * <p>Java class for Rule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Rule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ruleCondition" type="{}RuleType"/>
 *         &lt;element name="parameter" type="{}RuleParameter"/>
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

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rule", propOrder =
{ "ruleDefinition", "parameter" })
public class Rule
{

	@XmlElement(required = true)
	protected RuleDefinition ruleDefinition;
	@XmlElement(required = true)
	protected BigInteger parameter;

	/**
	 * Gets the value of the ruleDefinition property.
	 * 
	 * @return possible object is {@link RuleDefinition }
	 * 
	 */
	public RuleDefinition getRuleDefinition()
	{
		return ruleDefinition;
	}

	/**
	 * Sets the value of the ruleDefinition property.
	 * 
	 * @param value
	 *            allowed object is {@link RuleDefinition }
	 * 
	 */
	public void setRuleDefinition(RuleDefinition value)
	{
		this.ruleDefinition = value;
	}

	/**
	 * Gets the value of the parameter property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getParameter()
	{
		return parameter;
	}

	/**
	 * Sets the value of the parameter property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setParameter(BigInteger value)
	{
		this.parameter = value;
	}

}
