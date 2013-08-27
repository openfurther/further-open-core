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
package edu.utah.further.core.util.schema;

import javax.annotation.PostConstruct;

/**
 * A bean that depends on an enumerated constant.
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
 * @version Aug 19, 2010
 */
public final class SimpleAttributeClient
{
	// ========================= FIELDS =================================

	/**
	 * An enum property.
	 */
	private SimpleAttributeName sourceAttr;

	/**
	 * A string property.
	 */
	private String stringAttr;

	/**
	 * An integer property.
	 */
	private Integer intAttr;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate dependencies .
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		// Validate.notNull(sourceAttr, "A source attribute must be set");
	}

	// ========================= GET/SET =================================

	/**
	 * Return the sourceAttr property.
	 *
	 * @return the sourceAttr
	 */
	public SimpleAttributeName getSourceAttr()
	{
		return sourceAttr;
	}

	/**
	 * Set a new value for the sourceAttr property.
	 *
	 * @param sourceAttr
	 *            the sourceAttr to set
	 */
	public void setSourceAttr(final SimpleAttributeName sourceAttr)
	{
		this.sourceAttr = sourceAttr;
	}

	/**
	 * Return the stringAttr property.
	 *
	 * @return the stringAttr
	 */
	public String getStringAttr()
	{
		return stringAttr;
	}

	/**
	 * Set a new value for the stringAttr property.
	 *
	 * @param stringAttr the stringAttr to set
	 */
	public void setStringAttr(final String stringAttr)
	{
		this.stringAttr = stringAttr;
	}

	/**
	 * Return the intAttr property.
	 *
	 * @return the intAttr
	 */
	public Integer getIntAttr()
	{
		return intAttr;
	}

	/**
	 * Set a new value for the intAttr property.
	 *
	 * @param intAttr the intAttr to set
	 */
	public void setIntAttr(final Integer intAttr)
	{
		this.intAttr = intAttr;
	}
}
