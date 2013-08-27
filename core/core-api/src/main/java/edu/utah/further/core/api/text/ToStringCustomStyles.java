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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.lang.CoreUtil;

/**
 * Custom <code>toString</code> styles that we use throughout the code.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Api
@Utility
public final class ToStringCustomStyles
{
	// ========================= CONSTANTS =================================

	/**
	 * The short prefix toString style with field spaces. Using the <code>Person</code>
	 * example from {@link ToStringBuilder}, the output would look like this:
	 *
	 * <pre>
	 * Person[name=John Doe, age=33, smoker=false]
	 * </pre>
	 */
	public static final ToStringStyle SHORT_WITH_SPACES_STYLE = new ShortWithspacesToStringStyle();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in a singleton class.
	 * </p>
	 */
	private ToStringCustomStyles()
	{
		CoreUtil.preventUtilityConstruction();
	}

	// ========================= NESTED TYPES ==============================

	/**
	 * <p>
	 * <code>ToStringStyle</code> that prints out the short class name and no identity
	 * hashcode. It prints spaces between fields.
	 * </p>
	 *
	 * <p>
	 * This is an inner class rather than using a <code>ToStringStyle</code> instance to
	 * ensure its immutability.
	 * </p>
	 */
	private static final class ShortWithspacesToStringStyle extends ToStringStyle
	{
		/**
		 * @serial Serializable version identifier.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * <p>
		 * Constructor.
		 * </p>
		 *
		 * <p>
		 * Use the static constant rather than instantiating.
		 * </p>
		 */
		ShortWithspacesToStringStyle()
		{
			super();
			this.setUseShortClassName(true);
			this.setUseIdentityHashCode(false);
			this.setFieldSeparator(", ");
		}

		/**
		 * <p>
		 * Ensure <code>Singleton</code> after serialization.
		 * </p>
		 *
		 * @return the singleton
		 */
		private Object readResolve()
		{
			return SHORT_WITH_SPACES_STYLE;
		}

	}
}
