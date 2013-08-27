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
package edu.utah.further.core.ws;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;

import java.util.List;

import javax.ws.rs.core.PathSegment;

import org.apache.commons.lang.WordUtils;

/**
 * Centralizes web service utility methods.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Sep 24, 2009
 */
public final class WsUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * All SOAP web service class names should end with this string.
	 */
	private static final String SERVICE_SOAP_SUFFIX = "ServiceSoap";

	/**
	 * All REST web service class names should end with this string.
	 */
	private static final String SERVICE_REST_SUFFIX = "ServiceRest";

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Private constructor
	 */
	private WsUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Return the convention path for both SOAP and REST web service classes. All web
	 * service classes are assumed to end with "ServiceSoap" or "ServiceRest". The
	 * correspondence is then: class "MyServiceSoap" -> path "my"; class "MyServiceRest"
	 * -> path "my".
	 *
	 * @param serviceClass
	 *            SOAP or REST service class
	 * @return corresponding web service deployment path
	 */
	public static String getWebServiceClassPath(final Class<?> serviceClass)
	{
		return WordUtils.uncapitalize(serviceClass
				.getSimpleName()
				.replaceAll(SERVICE_SOAP_SUFFIX, EMPTY_STRING)
				.replaceAll(SERVICE_REST_SUFFIX, EMPTY_STRING));
	}

	/**
	 *
	 * Takes a list of path segments and returns an array of paths. Useful for gathering
	 * multiple parameters in the form of /parameterlist/param1/param2/param3/param4 where
	 * the array would contain param1-param4.
	 *
	 * @param parameters
	 * @return
	 */
	public static String[] pathSegmentToPathArray(
			final List<? extends PathSegment> parameters)
	{
		final List<String> pathParams = newList();
		for (final PathSegment segment : parameters)
		{
			pathParams.add(segment.getPath());
		}
		return pathParams.toArray(new String[pathParams.size()]);
	}
}
