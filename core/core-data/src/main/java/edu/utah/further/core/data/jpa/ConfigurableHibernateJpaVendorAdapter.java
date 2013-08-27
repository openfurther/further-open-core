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
package edu.utah.further.core.data.jpa;

import java.util.Map;
import java.util.Properties;

import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * An extension of {@link HibernateJpaVendorAdapter} that allows adding more configuration
 * properties.
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
 * @version Jun 3, 2010
 */
public final class ConfigurableHibernateJpaVendorAdapter extends
		HibernateJpaVendorAdapter
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Extension properties.
	 */
	private Properties properties = new Properties();

	// ========================= IMPL: HibernateJpaVendorAdapter ===========

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter#getJpaPropertyMap()
	 */
	// All Maps are in effect Map<String, String>, but Hibernate must support raw types
	// for backward-compatibility with Java 1.4-
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map getJpaPropertyMap()
	{
		// Note that we do not create a defensive copy because super.getJpaPropertyMap()
		// does not, and it seems that clients of this method modify the map. This is bad
		// -- against the PLK principle; but we must mimic the same thing here regardless
		// of that. It means that "properties" will be re-added every time this function
		// is called, but that should be harmless unless someone decides to remove one the
		// properties keys from jpaPropertyMap and then we re-added it, causing
		// potentially adverse effects. This is unlikely however, because the whole point
		// of adding "properties" in the first place is that they are extensions, and no
		// client code will normally know what keys we put into "properties".
		final Map jpaPropertyMap = super.getJpaPropertyMap();
		jpaPropertyMap.putAll(properties);
		return jpaPropertyMap;
	}

	// ========================= GET / SET =================================

	/**
	 * Set a new value for the properties property.
	 *
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final Properties properties)
	{
		this.properties = properties;
	}
}
