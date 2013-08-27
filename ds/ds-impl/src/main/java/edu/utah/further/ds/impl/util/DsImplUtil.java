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
package edu.utah.further.ds.impl.util;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest;

/**
 * Data source implementation utilities.
 * <p>
 * <br>
 * (c) 20082010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 18012133288<br>
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 17, 2010
 */
@Utility
public final class DsImplUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(DsImplUtil.class);

	// ========================= DEPENDENCIES ===========================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Private constructor
	 */
	private DsImplUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Return an MDR resource content via a web service. Convert to unchecked exceptions.
	 * 
	 * @param assetServiceRest
	 *            web service client
	 * @param path
	 *            MDR resource path
	 * @return active resource content, if found. If not found, returns <code>null</code>
	 */
	public static InputStream getMdrResourceInputStream(
			final AssetServiceRest assetServiceRest, final String path)
	{
		try
		{
			final String resourceContent = assetServiceRest
					.getActiveResourceContentByPath(path);
			return new ByteArrayInputStream(resourceContent.getBytes());
		}
		catch (final WsException e)
		{
			throw new ApplicationException(ErrorCode.SERVER_ERROR,
					"Failed to retrieve MDR resource", e);
		}
	}
}