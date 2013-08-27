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
package edu.utah.further.core.api.math;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.lang.PubliclyCloneable;

/**
 * An abstraction of an immutable interval <code>[a,b]</code>.
 * <p>
 * WARNING: the parameter class D must be immutable for this class to properly be cloned
 * (e.g. for serving as part of a parser's target).
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Mar 23, 2009
 */
@Api
public interface Interval<D extends Comparable<? super D>, T extends Interval<D, T>>
		extends PubliclyCloneable<Interval<D, T>>, Comparable<T>
// , TolerantlyComparable<T> // Not only deprecated, but also depends on whether D is
// double or integer, for example and therefore we can't keep SimpleInteval<D> concrete
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Does this interval a intersect another interval
	 * 
	 * @param b
	 *            another interval
	 * @return true iff this intersects b
	 */
	boolean intersects(T b);

	/**
	 * Does this interval a intersect another interval
	 * 
	 * @param b
	 *            an element
	 * @return true iff this intersects b
	 */
	boolean contains(D b);

	/**
	 * @return Returns the upper bound of the interval.
	 */
	D getHigh();

	/**
	 * @return Returns the lower bound of the interval.
	 */
	D getLow();
}
