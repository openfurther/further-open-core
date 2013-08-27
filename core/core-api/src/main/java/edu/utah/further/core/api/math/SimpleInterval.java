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

import static edu.utah.further.core.api.message.Messages.illegalValueMessage;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.exception.BusinessRuleException;

/**
 * An immutable interval <code>[a,b]</code>, where a and b are integers.
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
public final class SimpleInterval<D extends Comparable<? super D>> implements
		Interval<D, SimpleInterval<D>>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Lower bound of the interval.
	 */
	protected final D low;

	/**
	 * Upper bound of the interval.
	 */
	protected final D high;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct an interval from bounds.
	 * 
	 * @param low
	 *            Lower bound of the interval
	 * @param high
	 *            Upper bound of the interval
	 */
	public SimpleInterval(final D low, final D high)
	{
		if (low.compareTo(high) <= 0)
		{
			this.low = low;
			this.high = high;
		}
		else
		{
			throw new BusinessRuleException(illegalValueMessage("interval"));
		}
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Print an interval.
	 */
	@Override
	public String toString()
	{
		return "[" + low + "," + high + "]";
	}

	/**
	 * Must be overridden when <code>equals()</code> is.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37).append(low).append(high).toHashCode();
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for example for this object
	 * to serve as a target of an assembly.
	 * <p>
	 * WARNING: the parameter class E must be immutable for this class to properly be
	 * cloned (and serve as part of a parser's target).
	 * 
	 * @return a deep copy of this object
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public SimpleInterval<D> copy()
	{
		return new SimpleInterval<>(low, high);
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Result of comparing two intervals [a,b], [c,d]. They are equal if and only if a=c
	 * and b=d. [a,b] is less than [c,d] if a &lt; c or if a = c and b &lt; d. Otherwise,
	 * [a,b] is greater than [c,d] (lexicographic ordering).
	 * 
	 * @param obj
	 *            the other <code>Interval</code>
	 * @return the result of comparison
	 */
	@Override
	public int compareTo(final SimpleInterval<D> other)
	{
		return (low.equals(other.getLow()) && high.equals(other.getHigh())) ? 0 : (((low
				.compareTo(other.getLow()) < 0) || (low.equals(other.getLow()) && high
				.compareTo(other.getHigh()) < 0))) ? -1 : 1;
	}

	/**
	 * Result of equality of two intervals. They are equal if and only if their
	 * <code>low,high</code> fields are equal up to a relative tolerance of 1e-16.
	 * 
	 * @param o
	 *            The other <code>Interval</code> object.
	 * @return boolean The result of equality.
	 */
	@Override
	public boolean equals(final Object obj)
	{
		// Standard checks to ensure the adherence to the general contract of
		// the equals() method
		if (this == obj)
		{
			return true;
		}
		if ((obj == null) || (obj.getClass() != this.getClass()))
		{
			return false;
		}
		// Cast to friendlier version
		final SimpleInterval<?> other = (SimpleInterval<?>) obj;

		// Do not use compareTo() == 0 for a generic type because of unchecked
		// warning
		return low.equals(other.getLow()) && high.equals(other.getHigh());
	}

	// // ========================= IMPLEMENTATION: TolerantlyComparable ======
	//
	// /**
	// * Result of equality of two interval. They are equal if and only if their
	// * <code>low,high</code> fields are equal up to a relative tolerance of 1e-16,
	// * regardless of <code>tol</code>. This is an implementation of the
	// * <code>TolerantlyComparable</code> interface.
	// *
	// * @param obj
	// * The other <code>Interval</code> object.
	// * @param tol
	// * tolerance of equality; ignored
	// * @return the result of equality
	// */
	// public int tolerantlyEquals(final SimpleInterval<D> obj, final double tol)
	// {
	// return (this.equals(obj) ? TolerantlyComparable.EQUAL
	// : TolerantlyComparable.NOT_EQUAL);
	// }

	// ========================= PUBLIC METHODS ============================

	/**
	 * Does this interval a intersect another interval
	 * 
	 * @param b
	 *            another interval
	 * @return true iff this intersects b
	 */
	@Override
	public boolean intersects(final SimpleInterval<D> b)
	{
		if (((b.getLow().compareTo(high) <= 0 && b.getLow().compareTo(low) >= 0))
				|| ((low.compareTo(b.getHigh()) <= 0 && low.compareTo(b.getLow()) >= 0)))
		{
			return true;
		}
		return false;
	}

	/**
	 * Does this interval a intersect another interval
	 * 
	 * @param b
	 *            an element
	 * @return true iff this intersects b
	 */
	@Override
	public boolean contains(final D b)
	{
		return (low.compareTo(b) <= 0) && (high.compareTo(b) >= 0);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return Returns the high.
	 */
	@Override
	public D getHigh()
	{
		return high;
	}

	/**
	 * @return Returns the low.
	 */
	@Override
	public D getLow()
	{
		return low;
	}
}
