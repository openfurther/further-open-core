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
import static javax.xml.xpath.XPathConstants.NODESET;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * A class whose purpose is to throw an exception with an appropriate message. Assumes
 * that the input result is an error message XML document; matches an XPath expression to
 * extract the error message and sets it on the raised exception's message.
 * <p>
 * Must supply XPath expression to evaluate against the XML result. The expression must
 * return a node list; only the first node's text value is considered, as the error
 * message to propagate.
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
@Service("errorHandlerXpath")
@Scope(PROTOTYPE)
public class ErrorHandlerXpathImpl extends AbstractValidatorXpath
{
	// ========================= CONSTANTS =================================

	/**
	 * Fall-back text when error message not found in the XML document.
	 */
	private static final String DEFAULT_ERROR_MESSAGE = "Invalid result; no error message was found in the XML body";

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ValidatorXpathImpl.class);

	// ========================= DEPENDENCIES ==============================

	// ========================= IMPL: Validator ===========================

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.Validator#validate(edu.utah.further
	 * .core.query.domain.SearchQuery, java.util.Map)
	 */
	@Override
	public boolean validate(final Object result, final Map<String, Object> attributes)
	{
		// Can only validate String-type result
		Validate.notNull(result, "Result must be non-null to be valid");
		Validate.isTrue(ReflectionUtil.instanceOf(result, String.class),
				"Result of type " + result.getClass()
						+ " is unsupported; only String results are");

		// Run the XPath expression
		final String errorMessage = getErrorMessage((String) result);
		throw new ApplicationException(ErrorCode.RECOVERABLE_IO_ERROR, "Invalid result: " + errorMessage);
	}

	// ========================= PRIVATE METHODS ============================

	/**
	 * Return the error message.
	 *
	 * @param result
	 *            XML text
	 * @return Must always return a non-<code>null</code> text
	 */
	private String getErrorMessage(final String result)
	{
		final NodeList nodes = (NodeList) getParser().evaluateXPath(result, NODESET);
		final int numNodes = nodes.getLength();
		if (numNodes == 0)
		{
			// No error message found
			return DEFAULT_ERROR_MESSAGE;
		}
		// First node's text is interpreted as the error message
		return nodes.item(0).getNodeValue();
	}
}
