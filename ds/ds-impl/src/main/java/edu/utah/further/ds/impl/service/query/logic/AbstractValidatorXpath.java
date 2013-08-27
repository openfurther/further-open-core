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
package edu.utah.further.ds.impl.service.query.logic;

import static org.slf4j.LoggerFactory.getLogger;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.xml.xpath.XPathNamespaceContext;
import edu.utah.further.core.xml.xpath.XPathParser;
import edu.utah.further.ds.api.service.query.logic.Validator;

/**
 * A base class that validates XML documents based on the result of an XPath expression.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Sep 17, 2010
 */
abstract class AbstractValidatorXpath implements Validator<Object>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ValidatorXpathImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * XPath expression to evaluate against the XML result. Must return a node list; only
	 * the first node's text value is considered, as the error message to propagate.
	 */
	@Autowired
	private String expression;

	// ========================= FIELDS =====================================

	/**
	 * Caches the compiled XPath expression.
	 */
	@Final
	private XPathParser parser;

	/**
	 * Handles XML prefix-to-namespace mapping.
	 */
	private XPathNamespaceContext nsContext = new XPathNamespaceContext();

	// ========================= CONSTRUCTORS ===============================

	/**
	 * Validate dependencies.
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		Validate.notNull(expression, "An XPath expression must be set");

		// Use post-construct method as a builder pattern!
		if (expression != null)
		{
			this.parser = new XPathParser(expression, nsContext);
		}
	}

	// ========================= IMPL: Validator ============================

	// ========================= SETTERS ====================================

	/**
	 * Set a new value for the expression property.
	 * 
	 * @param expression
	 *            the expression to set
	 */
	public final void setExpression(final String expression)
	{
		this.expression = expression;
	}

	/**
	 * Set a new value for the nsContext property.
	 * 
	 * @param nsContext
	 *            the nsContext to set
	 */
	public final void setNsContext(final XPathNamespaceContext nsContext)
	{
		this.nsContext = nsContext;
	}

	// ========================= PRIVATE METHODS ============================

	/**
	 * Return the parser property.
	 * 
	 * @return the parser
	 */
	protected final XPathParser getParser()
	{
		return parser;
	}

	/**
	 * Return the expression property.
	 * 
	 * @return the expression
	 */
	protected final String getExpression()
	{
		return expression;
	}
}
