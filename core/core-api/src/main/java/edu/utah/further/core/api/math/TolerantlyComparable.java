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

/**
 * An object that can be compared with another object up to a finite tolerance.
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
@Deprecated
public interface TolerantlyComparable<T extends TolerantlyComparable<T>>
{
	// ========================= CONSTANTS =================================

	/**
	 * Signals that tolerant equality cannot be determined. Happens for instance when the
	 * quantities are both infinity, or both NaN.
	 */
	int INDETERMINATE = -1;

	/**
	 * Signals that tolerant equality holds.
	 */
	int EQUAL = 0;

	/**
	 * Signals that tolerant equality does not hold.
	 */
	int NOT_EQUAL = 1;

	/**
	 * This indicates that it's infinity vs. -infinity, infinity vs. nan or nan vs.
	 * -infinity, the quantities are different (compare re part vs. re part, im part vs.
	 * im part).
	 */
	int NOT_EQUAL_WHEN_NAN = 2;

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * The result of equality of two evaluable objects up to a tolerance. This checks for
	 * mathematical equivalence of expressions, without analyzing elements or comparing
	 * syntax trees.
	 * 
	 * @param other
	 *            an evaluable object
	 * @param tol
	 *            tolerance of equality, if we compare to a finite precision (for n digits
	 *            of accuracy, use tol = 10^{-n}).
	 * @return the result of tolerant equality of two evaluable quantities. Returns
	 *         {@link #EQUAL} if they are tolerantly equal; returns {@link #INDETERMINATE}
	 *         if tolerant equality cannot be returned; otherwise, returns a number that
	 *         is different from both constants, e.g., {@link #NON_EQUAL}.
	 */
	int tolerantlyEquals(T other, double tol);
}
