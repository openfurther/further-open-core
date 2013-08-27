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
package edu.utah.further.core.api.text;

import java.util.Properties;

import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A utility service that resolves place holders in strings. It harnesses Spring's
 * property place holder replacement facility. The only difference between this class and
 * Spring's <code>PropertyPlaceholderConfigurer</code> is that the former is intended to
 * directly process strings, and the latter processes spring context bean definitions.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Apr 27, 2010
 */
public interface PlaceHolderResolver
{
	// ========================= METHODS ======================================

	/**
	 * Resolve all place holders in a string.
	 * 
	 * @param strVal
	 *            original string
	 * @return string with place holders replaced with their values
	 * @throws ApplicationException
	 *             if circular place holder references are detected
	 */
	String resolvePlaceholders(String strVal);

	/**
	 * Resolve all place holders in a string. Allows to override the default place holders
	 * for this specific string.
	 * 
	 * @param strVal
	 *            original string
	 * @param overriddenPlaceholders
	 *            map of key-value place holders to use. Is overlaid over the default
	 *            place holder map, potentially overriding the default values with
	 *            identical keys in both maps
	 * @return string with place holders replaced with their values
	 * @throws ApplicationException
	 *             if unresolved or circular place holder references are detected
	 */
	String resolvePlaceholders(String strVal, Properties overriddenPlaceholders);
	
	/**
	 * Return the defaultPlaceHolders property.
	 * 
	 * @return the defaultPlaceHolders
	 */
	Properties getDefaultPlaceHolders();

	/**
	 * Set a new value for the default place holder map.
	 * 
	 * @param placeHolders
	 *            the placeHolders to set
	 */
	void setDefaultPlaceHolders(Properties defaultPlaceHolders);
}