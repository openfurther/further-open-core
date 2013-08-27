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
package edu.utah.further.dts.ws.impl.util;

import static edu.utah.further.core.api.constant.ErrorCode.INVALID_INPUT_ARGUMENT_TYPE;
import static edu.utah.further.core.api.constant.ErrorCode.MISSING_INPUT_ARGUMENT;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.text.StringUtil.isInvalidInteger;
import static edu.utah.further.dts.ws.impl.util.DtsWsNames.SOURCE_CONCEPT_ID;
import static edu.utah.further.dts.ws.impl.util.DtsWsNames.SOURCE_NAMESPACE;
import static edu.utah.further.dts.ws.impl.util.DtsWsNames.SOURCE_PROPERTY_NAME;
import static edu.utah.further.dts.ws.impl.util.DtsWsNames.SOURCE_PROPERTY_VALUE;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.text.TextTemplate;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;

/**
 * Terminology web services - utilities.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Mar 23, 2009
 */
@Utility
public final class DtsWsUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(DtsWsUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private DtsWsUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Validate a DTS concept ID input argument to a web service.
	 *
	 * @param conceptId
	 *            DTS concept identifier
	 */
	public static void validateConceptIdArgument(final DtsConceptId conceptId)
			throws WsException
	{
		final TextTemplate missingArgumentMessage = new TextTemplate(
				"Missing argument %ARGUMENT_NAME%", asList("ARGUMENT_NAME"));
		if (conceptId == null)
		{
			throw new WsException(INVALID_INPUT_ARGUMENT_TYPE,
					"Null concept input argument");
		}
		if (isBlank(conceptId.getNamespace()))
		{
			throw new WsException(MISSING_INPUT_ARGUMENT, missingArgumentMessage
					.evaluate(asList(SOURCE_NAMESPACE)));
		}
		if (isBlank(conceptId.getPropertyName()))
		{
			throw new WsException(MISSING_INPUT_ARGUMENT, missingArgumentMessage
					.evaluate(asList(SOURCE_PROPERTY_NAME)));
		}
		if (isBlank(conceptId.getPropertyValue()))
		{
			throw new WsException(MISSING_INPUT_ARGUMENT, missingArgumentMessage
					.evaluate(asList(SOURCE_PROPERTY_VALUE)));
		}
	}

	/**
	 * Validate a DTS concept unique ID input argument to a web service.
	 *
	 * @param conceptUniqueId
	 *            DTS concept identifier
	 */
	public static void validateConceptUniqueIdArgument(
			final DtsConceptUniqueId conceptUniqueId) throws WsException
	{
		final TextTemplate missingArgumentMessage = new TextTemplate(
				"Missing argument %ARGUMENT_NAME%", asList("ARGUMENT_NAME"));
		if (conceptUniqueId == null)
		{
			throw new WsException(INVALID_INPUT_ARGUMENT_TYPE,
					"Null concept input argument");
		}
		if (isInvalidInteger(conceptUniqueId.getNamespaceId()))
		{
			throw new WsException(MISSING_INPUT_ARGUMENT, missingArgumentMessage
					.evaluate(asList(SOURCE_NAMESPACE)));
		}
		if (isInvalidInteger(conceptUniqueId.getId()))
		{
			throw new WsException(MISSING_INPUT_ARGUMENT, missingArgumentMessage
					.evaluate(asList(SOURCE_CONCEPT_ID)));
		}
	}
}
