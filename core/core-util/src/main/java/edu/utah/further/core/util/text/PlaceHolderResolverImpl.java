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
package edu.utah.further.core.util.text;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX;
import static org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX;
import static org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.DEFAULT_VALUE_SEPARATOR;

import java.util.Properties;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.util.PropertyPlaceholderHelper;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.CoreUtil;
import edu.utah.further.core.api.text.PlaceHolderResolver;

/**
 * A utility service that resolves place holders in strings. It harnesses Spring's
 * property place holder replacement facility. The only difference between this class and
 * Spring's {@link PropertyPlaceholderConfigurer} is that the former is intended to
 * directly process strings, and the latter processes spring context bean definitions.
 * <p>
 * This implementation class is built for extension.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Apr 26, 2010
 */
@Service("placeHolderResolver")
public class PlaceHolderResolverImpl implements PlaceHolderResolver
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(PlaceHolderResolverImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Default properties to use as place-holders.
	 */
	private Properties defaultPlaceHolders;

	// ========================= FIELDS ====================================

	private final PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(
			DEFAULT_PLACEHOLDER_PREFIX, DEFAULT_PLACEHOLDER_SUFFIX,
			DEFAULT_VALUE_SEPARATOR, true);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a resolver. By default, no place holders are declared.
	 */
	public PlaceHolderResolverImpl()
	{
		setDefaultPlaceHolders(new Properties());
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append(
				"defaultPlaceHolders", defaultPlaceHolders).toString();
	}

	// ========================= METHODS ===================================

	/**
	 * @param strVal
	 * @return
	 * @see edu.utah.further.core.api.text.PlaceHolderResolver#resolvePlaceholders(java.lang.String)
	 */
	@Override
	public String resolvePlaceholders(final String strVal)
	{
		return helper.replacePlaceholders(strVal, defaultPlaceHolders);
	}

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
	@Override
	public String resolvePlaceholders(final String strVal,
			final Properties overriddenPlaceholders)
	{
		if (strVal == null)
		{
			return strVal;
		}

		final Properties props = new Properties(defaultPlaceHolders);
		props.putAll(overriddenPlaceholders);
		return helper.replacePlaceholders(strVal, props);
	}

	// ========================= GET & SET =================================

	/**
	 * Return the defaultPlaceHolders property.
	 * 
	 * @return the defaultPlaceHolders
	 */
	@Override
	public Properties getDefaultPlaceHolders()
	{
		// Make a defensive copy
		return CoreUtil.newPropertiesCopy(defaultPlaceHolders);
	}

	/**
	 * Set a new value for the defaultPlaceHolders property.
	 * 
	 * @param defaultPlaceHolders
	 *            the defaultPlaceHolders to set
	 */
	@Override
	public final void setDefaultPlaceHolders(final Properties defaultPlaceHolders)
	{
		// Make a defensive copy
		this.defaultPlaceHolders = CoreUtil.newPropertiesCopy(defaultPlaceHolders);
	}
}
