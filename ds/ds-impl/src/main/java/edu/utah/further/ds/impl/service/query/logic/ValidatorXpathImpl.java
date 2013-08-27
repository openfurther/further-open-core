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

import static edu.utah.further.core.api.constant.Constants.Scope.PROTOTYPE;
import static edu.utah.further.core.api.text.StringUtil.pluralForm;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static javax.xml.xpath.XPathConstants.NODESET;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * Validates a data query result. A result is deemed INVALID if and only if a certain
 * XPath expression yields non-empty results or if we fail to run the expression at all
 * (e.g. when the result is <code>null</code>.
 * <p>
 * This class should be able to handle ALL possible results without throwing an exception.
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
@Service("validatorXpath")
@Scope(PROTOTYPE)
public final class ValidatorXpathImpl extends AbstractValidatorXpath
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ValidatorXpathImpl.class);

	// ========================= IMPL: Validator ============================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.Validator#validate(edu.utah.further
	 * .core.query.domain.SearchQuery, java.util.Map)
	 */
	@Override
	public boolean validate(final Object object, final Map<String, Object> attributes)
	{
		try
		{
			// Can only validate String-type result
			Validate.notNull(object, "Result must be non-null to be valid");
			Validate.isTrue(ReflectionUtil.instanceOf(object, String.class),
					"Result of type " + object.getClass()
							+ " is unsupported; only String results are");

			// Run the XPath expression
			final int numNodes = getNumMatchingNodes(object);
			if (log.isDebugEnabled())
			{
				log.debug("Found " + numNodes + " " + pluralForm("node", numNodes)
						+ " matching the XPath expression " + quote(getExpression())
						+ ", " + ((numNodes == 0) ? "valid" : "invalid") + " result");
			}

			// Result is valid if and only if the XPath result is empty
			return (numNodes == 0);
		}
		catch (final Throwable t)
		{
			log.error("Error during result validation", t);
			// Null result, non-string result, other XPath failure
			return false;
		}
	}

	// ========================= PRIVATE METHODS ============================

	/**
	 * @param object
	 * @return
	 */
	private int getNumMatchingNodes(final Object object)
	{
		final String result = (String) object;
		final NodeList nodes = (NodeList) getParser().evaluateXPath(result, NODESET);
		return nodes.getLength();
	}
}
