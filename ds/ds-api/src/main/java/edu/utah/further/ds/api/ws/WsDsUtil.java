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
package edu.utah.further.ds.api.ws;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.HasErrorMessage;

/**
 * Utility class for anything related to web service data sources.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jun 22, 2012
 */
public final class WsDsUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private WsDsUtil()
	{
		preventUtilityConstruction();
	}

	/**
	 * Handles the result of the web service response.
	 * 
	 * @param result
	 *            raw web service response
	 * @return {@link WsDsResponse} wrapper of <code>result</code>
	 */
	public static final WsDsResponse handleErrorResponse(final WsDsResponse response)
	{
		final Object result = response.getResponse();
		if (instanceOf(result, HasErrorMessage.class))
		{
			final HasErrorMessage exception = (HasErrorMessage) result;
			throw new ApplicationException("Response error:" + exception.getMessage());
		}
		return response;
	}
}
