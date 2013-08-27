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

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.core.api.context.Api;

/**
 * A pair of objects.
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
 * @version May 29, 2009
 */
@Api
public class Pair<A, B> implements Serializable
{
	// ========================= CONSTANTS =================================

	private static final long serialVersionUID = 1L;

	public static enum Element
	{
		LEFT, RIGHT
	}

	// ========================= FIELDS ====================================

	/**
	 * Left element.
	 */
	protected A left;

	/**
	 * Right element.
	 */
	protected B right;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a pair <code>(left,right)</code>.
	 * 
	 * @param left
	 *            left element
	 * @param right
	 *            right element
	 */
	public Pair(final A left, final B right)
	{
		this.left = left;
		this.right = right;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(left).append(right).toHashCode();
	}

	/**
	 * Compare two Pairs based on data.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		@SuppressWarnings("unchecked")
		final Pair<A, B> that = (Pair<A, B>) obj;

		return new EqualsBuilder()
				.append(this.left, that.left)
				.append(this.right, that.right)
				.isEquals();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return Returns the left.
	 */
	public final A getLeft()
	{
		return left;
	}

	/**
	 * @param left
	 *            The left to set.
	 */
	public final void setLeft(final A left)
	{
		this.left = left;
	}

	/**
	 * @return Returns the right.
	 */
	public final B getRight()
	{
		return right;
	}

	/**
	 * @param right
	 *            The right to set.
	 */
	public final void setRight(final B right)
	{
		this.right = right;
	}

	/**
	 * @param left
	 *            The left to set.
	 * @param right
	 *            The right to set.
	 */
	public final void set(final A left, final B right)
	{
		this.left = left;
		this.right = right;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Print a pair.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		return "(" + left + "," + right + ")";
	}
}
