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
package edu.utah.further.core.util.math.random;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.core.api.context.Mock;
import edu.utah.further.core.api.math.ArithmeticUtil;

/**
 * Mock random number generator that generates the same values for every category (int,
 * double, etc.). For each category, the values periodically rotate through a wired array
 * of values.
 * <p>
 * The rotation is currently implemented only for integers and doubles.
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
 * @version Dec 25, 2009
 */
@Qualifier("randomGeneratorRotating")
@Mock
public final class RandomGeneratorRotatingImpl extends RandomGeneratorFixedImpl
{
	// ========================= DEPENDENCIES ==============================

	/**
	 * Hard-coded list of rotating integer values to return from <code>nextInt*</code>
	 * methods.
	 */
	private int[] intValues;

	/**
	 * Hard-coded list of rotating integer values to return from <code>nextDouble*</code>
	 * methods.
	 */
	private double[] doubleValues;

	// ========================= FIELDS ====================================

	/**
	 * Caches the maximum value in {@link #intValues}.
	 */
	private int intMaximumSupportedRange;

	// /**
	// * Caches the maximum value in {@link #doubleValues}.
	// */
	// private double doubleMaximumSupportedRange;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * No need to actually implement the interface; using annotations instead.
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		Validate.notNull(intValues, "A list of hard-coded integer values must be wired");
		Validate.notNull(doubleValues,
				"A list of hard-coded integer values must be wired");
	}

	// ========================= FIELDS ====================================

	/**
	 * Integer value array rotating index.
	 */
	private volatile int intIndex = 0;

	/**
	 * Double value array rotating index.
	 */
	private volatile int doubleIndex = 0;

	// ========================= IMPLEMENTATION: RadomGenerator ============

	/**
	 * @return
	 * @see org.apache.commons.math.random.RandomGenerator#nextInt()
	 */
	@Override
	public int nextInt()
	{
		final int value = intValues[intIndex++];
		if (intIndex == intValues.length)
		{
			// Reached end of array, reset index
			intIndex = 0;
		}
		return value;
	}

	/**
	 * @param n
	 * @return
	 * @see org.apache.commons.math.random.RandomGenerator#nextInt(int)
	 */
	@Override
	public int nextInt(final int n)
	{
		if (intMaximumSupportedRange >= n)
		{
			throw new UnsupportedOperationException("Random range " + n
					+ " is too large. Maximum range is " + intMaximumSupportedRange);
		}
		return nextInt();
	}

	/**
	 * @return
	 * @see org.apache.commons.math.random.RandomGenerator#nextDouble()
	 */
	@Override
	public double nextDouble()
	{
		final double value = doubleValues[doubleIndex++];
		if (doubleIndex == doubleValues.length)
		{
			// Reached end of array, reset index
			doubleIndex = 0;
		}
		return value;
	}

	// ========================= DEPENDENCY INJECTION ======================

	/**
	 * Set a new value for the intValues property.
	 * 
	 * @param intValues
	 *            the intValues to set
	 */
	public void setIntValues(final int[] intValues)
	{
		this.intValues = intValues;
		this.intMaximumSupportedRange = ArithmeticUtil.max(intValues);
	}

	/**
	 * Set a new value for the doubleValues property.
	 * 
	 * @param doubleValues
	 *            the doubleValues to set
	 */
	public void setDoubleValues(final double[] doubleValues)
	{
		this.doubleValues = doubleValues;
		// this.doubleMaximumSupportedRange = ArrayUtil.max(doubleValues);
	}
}
