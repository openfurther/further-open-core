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
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.utah.further.fqe.impl.validation.domain package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory
{

	private final static QName _ValidationRule_QNAME = new QName(
			"http://further.utah.edu/fqe/validation", "validationRule");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema
	 * derived classes for package: edu.utah.further.fqe.impl.validation.domain
	 * 
	 */
	public ObjectFactory()
	{
	}

	/**
	 * Create an instance of {@link ValidationRule }
	 * 
	 */
	public ValidationRule createValidationRule()
	{
		return new ValidationRule();
	}

	/**
	 * Create an instance of {@link RuleDefinition }
	 * 
	 */
	public RuleDefinition createRuleDefinition()
	{
		return new RuleDefinition();
	}

	/**
	 * Create an instance of {@link CriteriaIdentifier }
	 * 
	 */
	public CriteriaIdentifier createCriteriaIdentifier()
	{
		return new CriteriaIdentifier();
	}

	/**
	 * Create an instance of {@link Rule }
	 * 
	 */
	public Rule createRule()
	{
		return new Rule();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link ValidationRule }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://further.utah.edu/fqe/validation", name = "validationRule")
	public JAXBElement<ValidationRule> createValidationRule(final ValidationRule value)
	{
		return new JAXBElement<>(_ValidationRule_QNAME,
				ValidationRule.class, null, value);
	}

}
