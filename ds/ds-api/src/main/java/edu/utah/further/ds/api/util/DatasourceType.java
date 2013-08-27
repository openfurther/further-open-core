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
package edu.utah.further.ds.api.util;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Labeled;

/**
 * An enum to identify a given data source type, typically set during query intialization.
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
 * @version Apr 26, 2010
 */
public enum DatasourceType implements Labeled
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * A database based data source where direct access to the database is allowed.
	 */
	DATABASE,

	/**
	 * A web service based data source where data is exposed via CRUD web services.
	 */
	WEB_SERVICE;

	// ========================= CONSTANTS =================================

	/**
	 * Cached part of {@link #getLabel()}'s computation.
	 */
	private static final String LABEL_PREFIX = DatasourceType.class
			.getCanonicalName()
			.toLowerCase()
			+ Strings.PROPERTY_SCOPE_CHAR;

	// ========================= IMPL: Labeled ==============================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.context.Labeled#getLabel()
	 */
	@Override
	public String getLabel()
	{
		return LABEL_PREFIX + name();
	}
}
