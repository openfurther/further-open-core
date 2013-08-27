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
package edu.utah.further.core.data.logging;

import java.util.List;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.PubliclyCloneable;

/**
 * A JavaBean that holds logging options used by multiple objects in this package.
 * Contains only getters.
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
 * @version Jun 29, 2011
 */
public class ImmutableLoggingOptions implements
		PubliclyCloneable<ImmutableLoggingOptions>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Escape quote characters in logging statements. Useful if a
	 * <code>JDBCAppender</code> is used on conjunction with this class.
	 */
	protected boolean escapeQuotes;

	/**
	 * Proxy only connections to one the following URLs. If the list is empty, all
	 * connections are proxied.
	 */
	protected final List<String> connectionUrls = CollectionUtil.newList();

	// ========================= GET/SET ===================================

	/**
	 * Return the escapeQuotes property.
	 *
	 * @return the escapeQuotes
	 */
	public boolean isEscapeQuotes()
	{
		return escapeQuotes;
	}

	/**
	 * Return the connectionUrls property.
	 *
	 * @return the connectionUrls
	 */
	public List<String> getConnectionUrls()
	{
		return CollectionUtil.newList(connectionUrls);
	}

	// ========================= IMPL: PubliclyCloneable ===================

	/**
	 * Return a deep copy of this object.
	 *
	 * @return a deep copy of this object
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public ImmutableLoggingOptions copy()
	{
		// Do not use clone - will copy the List reference and violate this object's
		// immutability!
		final ImmutableLoggingOptions copy = new ImmutableLoggingOptions();
		copy.escapeQuotes = this.escapeQuotes;
		// Deep copy collections
		CollectionUtil.setListElements(copy.connectionUrls, this.connectionUrls);
		return copy;
	}
}
