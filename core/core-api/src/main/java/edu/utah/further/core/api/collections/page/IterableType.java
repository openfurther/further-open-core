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
package edu.utah.further.core.api.collections.page;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Labeled;

/**
 * An enumeration of supported iterable objects. This is meant to be comprehensive for the
 * FURTHeR code, but can be extended by paging providers using a general {@link Labeled}
 * iterable type.
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
 * @version Aug 16, 2010
 */
public enum IterableType implements Labeled
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * A list.
	 */
	LIST,

	/**
	 * A sorted set.
	 */
	SORTED_SET,

	/**
	 * An XML document input stream or a StAX XML document stream reader.
	 */
	XML_STREAM,

	/**
	 * A CSV file input stream.
	 */
	CSV_STREAM,

	/**
	 * Hibernate scrollable result set.
	 */
	SCROLLABLE_RESULTS;

	// ========================= CONSTANTS =================================

	/**
	 * Cached part of {@link #getLabel()}'s computation.
	 */
	private static final String LABEL_PREFIX = IterableType.class
			.getCanonicalName()
			.toLowerCase() + Strings.PROPERTY_SCOPE_CHAR;

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
