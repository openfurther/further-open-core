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

import static edu.utah.further.core.api.message.ValidationUtil.validateIsTrue;
import static edu.utah.further.core.api.message.ValidationUtil.validateNotNull;
import static java.lang.Math.max;

import java.util.Arrays;
import java.util.NoSuchElementException;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.lang.RichIterator;
import edu.utah.further.core.api.lang.UnmodifiableIterator;

/**
 * An d-dimensional subscript iterator. This is useful when looping over a volume or an
 * area. Supports both periodic looping and one-time looping (reaches EOF when the
 * "upper-left" vertex of the cube is reached).
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
public final class MultiDimensionalIterator extends UnmodifiableIterator<int[]> implements
		RichIterator<int[]>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Lower-left corner of box (in d dimensions).
	 */
	private final int[] lower;

	/**
	 * Upper-right corner of box (in d dimensions).
	 */
	private final int[] upper;

	/**
	 * Increment (step) in each dimension in an iteration step.
	 */
	private final int[] increment;

	/**
	 * If true, loops periodically over the box.
	 */
	private final boolean periodic;

	/**
	 * Number of dimensions of the box.
	 */
	private final int numDims;

	/**
	 * Iteration counter's maximum value.
	 */
	private final int maxCounter;

	/**
	 * The subscript we are currently at during looping.
	 */
	private final int[] current;

	/**
	 * Iteration counter. Incremented every time {@link #next()} is called. When it
	 * reaches box size - 1, we know we're at the end of the box.
	 */
	private int counter = 0;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a multi-dimensional iterator.
	 * 
	 * @param lower
	 *            lower-left corner of box (in d dimensions)
	 * @param upper
	 *            upper-right corner of box (in d dimensions)
	 * @param increment
	 *            increment (step) in each dimension in an iteration step.
	 * @param periodic
	 *            if true, loops periodically over the box
	 */
	public MultiDimensionalIterator(final int[] lower, final int[] upper,
			final int[] increment, final boolean periodic)
	{
		validateArguments(lower, upper, increment);
		this.lower = lower;
		this.upper = upper;
		this.increment = increment;
		this.periodic = periodic;

		this.numDims = lower.length;
		this.current = copyOf(lower, numDims);
		this.maxCounter = boxSize(lower, upper, increment);
	}

	/**
	 * Initialize a multi-dimensional iterator.
	 * 
	 * @param lower
	 *            lower-left corner of box (in d dimensions)
	 * @param upper
	 *            upper-right corner of box (in d dimensions)
	 * @param increment
	 *            increment (step) in each dimension in an iteration step.
	 */
	public MultiDimensionalIterator(final int[] lower, final int[] upper,
			final int[] increment)
	{
		this(lower, upper, increment, false);
	}

	/**
	 * Initialize a multi-dimensional iterator that loops over an entire box.
	 * 
	 * @param lower
	 *            lower-left corner of box (in d dimensions)
	 * @param upper
	 *            upper-right corner of box (in d dimensions)
	 * @param periodic
	 *            if true, loops periodically over the box
	 */
	public MultiDimensionalIterator(final int[] lower, final int[] upper,
			final boolean periodic)
	{
		this(lower, upper, ones(lower.length), periodic);
	}

	/**
	 * Initialize a multi-dimensional iterator that loops over an entire box.
	 * 
	 * @param lower
	 *            lower-left corner of box (in d dimensions)
	 * @param upper
	 *            upper-right corner of box (in d dimensions)
	 */
	public MultiDimensionalIterator(final int[] lower, final int[] upper)
	{
		this(lower, upper, false);
	}

	// ========================= IMPLEMENTATION: Iterator ==================

	/**
	 * Returns the first element in the iteration (<code>lower</code>).
	 * 
	 * @return the first element in the iteration (<code>lower</code>)
	 * @exception NoSuchElementException
	 *                not thrown here
	 * @see edu.utah.further.core.api.lang.RichIterator#begin()
	 */
	@Override
	public int[] begin()
	{
		return lower;
	}

	/**
	 * Returns <code>true</code> if and only if the element returned by the last
	 * {@link #next()} call is the last element in the iteration.
	 * 
	 * @return loop-not-yet-terminated condition
	 * @see edu.utah.further.core.api.lang.RichIterator#notEnd()
	 */
	@Override
	public boolean notEnd()
	{
		return periodic ? false : (counter < maxCounter);
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		return nextSubscript(current, null);
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	@Override
	public int[] next()
	{
		counter++;
		nextSubscript(current, current);
		return current;
	}

	// ========================= METHODS ===================================

	/**
	 * Computes the a multi-dimensional box size.
	 * 
	 * @param lower
	 *            lower-left corner of box (in d dimensions)
	 * @param upper
	 *            upper-right corner of box (in d dimensions)
	 * @param increment
	 *            increment in each dimension in an iteration step
	 * @return number of points in the box (including lower and upper)
	 */
	public static int boxSize(final int[] lower, final int[] upper, final int[] increment)
	{
		validateArguments(lower, upper, increment);

		int boxSize = 1;
		for (int d = 0; d < lower.length; d++)
		{
			boxSize *= max(0, (upper[d] - lower[d]) / increment[d] + 1);
		}

		return boxSize;
	}

	/**
	 * Returns a vector of ones.
	 * 
	 * @param numDims
	 *            size of vector
	 * @return a vector filled with ones of size <code>numDims</code>
	 */
	public static int[] ones(final int numDims)
	{
		final int[] v = new int[numDims];
		for (int d = 0; d < numDims; d++)
		{
			v[d] = 1;
		}

		return v;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Validates iterator construction arguments.
	 * 
	 * @param lower
	 *            lower-left corner of box (in d dimensions)
	 * @param upper
	 *            upper-right corner of box (in d dimensions)
	 * @param increment
	 *            increment in each dimension in an iteration step
	 * @return number of vertices in the box
	 */
	private static void validateArguments(final int[] lower, final int[] upper,
			final int[] increment)
	{
		validateNotNull("lower", lower);
		validateNotNull("upper", upper);
		validateNotNull("sub", increment);

		validateIsTrue(lower.length == upper.length,
				"lower, upper must have the same length");
		validateIsTrue(upper.length == increment.length,
				"lower, upper, sub must have the same length");

		for (int d = 0; d < lower.length; d++)
		{
			validateIsTrue(increment[d] > 0,
					"for now, all increments must be positive (reverse iterations not supported)");
			validateIsTrue((upper[d] - lower[d]) % increment[d] == 0,
					"upper-lower must be element-wise divisible by increment "
							+ Arrays.toString(increment));
		}

	}

	/**
	 * Compute the next subscript in the iteration.
	 * 
	 * @param previous
	 *            current subscript
	 * @param nextContainer
	 *            if <code>null</code>, upon returning from this method, this is set to
	 *            the next subscript in the iteration. Otherwise, it is ignored and
	 *            <code>previous</code> is updated with the next subscript in the
	 *            iteration
	 * @return is there a second
	 */
	private boolean nextSubscript(final int[] previous, final int[] nextContainer)
	{
		// Create a copy so that we don't override initial
		int[] next = nextContainer;
		if (next == null)
		{
			next = copyOf(previous, previous.length);
		}
		int d = 0;
		next[d] += increment[d];
		if (next[d] > upper[d])
		{
			while (next[d] > upper[d])
			{
				next[d] = lower[d];
				d++;
				if (d == numDims)
				{
					// End of box reached
					if (periodic)
					{
						// Return to beginning of box
						next = copyOf(lower, lower.length);
						return true;
					}
					// Wrap first index so that next = upper
					next[0] -= increment[0];
					return false;
				}
				next[d] += increment[d];
			}
		}

		return true;
	}

	/**
	 * Perform an integer array copy using Java 5 API. In Java 6, use
	 * <code>Arrays.copyOf()</code>.
	 * <p>
	 * TODO: move this method to IOUtil and make it public.
	 * 
	 * @param src
	 *            source array
	 * @param length
	 *            array length
	 * @return array copy
	 */
	private static int[] copyOf(final int[] src, final int length)
	{
		final int[] dest = new int[length];
		System.arraycopy(src, 0, dest, 0, length);
		return dest;
	}
}
