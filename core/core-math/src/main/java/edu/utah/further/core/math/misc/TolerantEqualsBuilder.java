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
package edu.utah.further.core.math.misc;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.math.util.MathUtils;

import edu.utah.further.core.api.math.TolerantlyEquals;

/**
 * A sub-class of {@link EqualsBuilder} that adds tolerant equality features.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Dec 24, 2009
 */
public final class TolerantEqualsBuilder extends
		GenericEqualsBuilder<TolerantEqualsBuilder>
{
	// ========================= METHODS ===================================

	/**
	 * Append the tolerant equality of two doubles.
	 * 
	 * @param lhs
	 *            left-hand-side
	 * @param rhs
	 *            right-hand-side
	 * @param maxUlps
	 *            ULPS (roughly equivalent to unit round-off relative error)
	 * @return this object, for method chaining
	 */
	public TolerantEqualsBuilder appendTolerantly(final double lhs, final double rhs,
			final int maxUlps)
	{
		return super.append(MathUtils.equals(lhs, rhs, maxUlps), true);
	}

	/**
	 * Append the tolerant equality of two tolerantly-comparable objects.
	 * 
	 * @param lhs
	 *            left-hand-side
	 * @param rhs
	 *            right-hand-side
	 * @param maxUlps
	 *            ULPS (roughly equivalent to unit round-off relative error)
	 * @return this object, for method chaining
	 */
	public <T extends TolerantlyEquals<T>> TolerantEqualsBuilder appendTolerantly(
			final T lhs, final T rhs, final int maxUlps)
	{
		return super.append(lhs.tolerantlyEquals(rhs, maxUlps), true);
	}

}
