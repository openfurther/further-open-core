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

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang.ArrayUtils;

import edu.utah.further.core.api.context.Utility;

/**
 * Arithmetic utilities.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) Oren E. Livne, Ph.D., University of Utah<br>
 * Email: {@code <oren.livne@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-213-3713<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Dec 20, 2009
 */
@Utility
public final class ArithmeticUtil
{
	// ========================= METHODS ====================================

	/**
	 * Find maximum (largest) value in array.
	 *
	 * @param array
	 * @return
	 */
	public static byte max(final byte[] array)
	{
		byte maxValue = array[0];
		for (int i = 1; i < array.length; i++)
		{
			if (array[i] > maxValue)
			{
				maxValue = array[i];
			}
		}
		return maxValue;
	}

	/**
	 * Find minimum (lowest) value in array.
	 *
	 * @param array
	 * @return
	 */
	public static byte min(final byte[] array)
	{
		byte minValue = array[0];
		for (int i = 1; i < array.length; i++)
		{
			if (array[i] < minValue)
			{
				minValue = array[i];
			}
		}
		return minValue;
	}

	/**
	 * Find maximum (largest) value in array.
	 *
	 * @param array
	 * @return
	 */
	public static int max(final int[] array)
	{
		int maxValue = array[0];
		for (int i = 1; i < array.length; i++)
		{
			if (array[i] > maxValue)
			{
				maxValue = array[i];
			}
		}
		return maxValue;
	}

	/**
	 * Find minimum (lowest) value in array.
	 *
	 * @param array
	 * @return
	 */
	public static int min(final int[] array)
	{
		int minValue = array[0];
		for (int i = 1; i < array.length; i++)
		{
			if (array[i] < minValue)
			{
				minValue = array[i];
			}
		}
		return minValue;
	}

	/**
	 * Find maximum (largest) value in array.
	 *
	 * @param array
	 * @return
	 */
	public static double max(final double[] array)
	{
		double maxValue = array[0];
		for (int i = 1; i < array.length; i++)
		{
			if (array[i] > maxValue)
			{
				maxValue = array[i];
			}
		}
		return maxValue;
	}

	/**
	 * Find minimum (lowest) value in array.
	 *
	 * @param array
	 * @return
	 */
	public static double min(final double[] array)
	{
		double minValue = array[0];
		for (int i = 1; i < array.length; i++)
		{
			if (array[i] < minValue)
			{
				minValue = array[i];
			}
		}
		return minValue;
	}

	/**
	 * @param array
	 * @return
	 */
	public static int minIndex(final double[] array)
	{
		final double minValue = array[0];
		int indexOfMinValue = 0;
		for (int i = 1; i < array.length; i++)
		{
			final double value = array[i];
			if (value < minValue)
			{
				indexOfMinValue = i;
			}
		}
		return indexOfMinValue;
	}

	/**
	 * Compute the factorial of a {@link BigInteger}. Allows computing big numbers.
	 *
	 * @param n
	 * @return
	 */
	public static BigInteger factorial(final int n)
	{
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--)
		{
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		}
		return fact;
	}

	/**
	 * Return a cumulative sum array of an array
	 *
	 * @param array
	 *            original array of size n
	 * @return array of size n whose ith element is sum_{j=0..i-1} (a[j])
	 */
	public static double[] cumulativeSum(final double[] array)
	{
		final double[] cumsum = ArrayUtils.clone(array);
		// Compute by recursion
		for (int i = 1; i < cumsum.length; i++)
		{
			// A clever implementation. Since cumsum[i] = array[i], this computes
			// cumsum[i] = array[i] + cumsum[i-1]
			cumsum[i] += cumsum[i - 1];
		}
		return cumsum;
	}

	/**
	 * Return the minimal index for which the corresponding array element is greater than
	 * a certain threshold.
	 *
	 * @param array
	 *            array
	 * @param p
	 *            threshold value
	 * @return minimal index for which <code>array[k] >= p</code>. If no such index
	 *         exists, returns <code>array.length</code>
	 */
	public static int minIndexGreaterThan(final double[] array, final double p)
	{
		final int size = array.length;
		for (int k = 0; k < size; k++)
		{
			if (array[k] >= p)
			{
				return k;
			}
		}
		return size;
	}

	/**
	 * Translates the string representation of a {@code Long} into a
	 * {@code Long}.
	 *
	 * @param string
	 *            String representation of {@code Long}.
	 *
	 * @throws NumberFormatException
	 *             if {@code val} is not a valid representation of a {@code BigDecimal}.
	 * @return new {@link Long} copy of <code>other</code>, or <code>null</code> if
	 *         <code>string</code> is <code>null</code>
	 */
	public static Long newLongNullSafe(final String string)
	{
		return isEmpty(string) ? null : new Long(string);
	}

	/**
	 * Translates the string representation of a {@code BigDecimal} into a
	 * {@code BigDecimal}.
	 *
	 * @param string
	 *            String representation of {@code BigDecimal}.
	 *
	 * @throws NumberFormatException
	 *             if {@code val} is not a valid representation of a {@code BigDecimal}.
	 * @return new {@link BigDecimal} copy of <code>other</code>, or <code>null</code> if
	 *         <code>string</code> is <code>null</code>
	 */
	public static BigDecimal newBigDecimalNullSafe(final String string)
	{
		return isEmpty(string) ? null : new BigDecimal(string);
	}

	/**
	 * Translates the string representation of a {@code BigInteger} into a
	 * {@code BigInteger}.
	 *
	 * @param string
	 *            String representation of {@code BigInteger}.
	 *
	 * @throws NumberFormatException
	 *             if {@code val} is not a valid representation of a {@code BigInteger}.
	 * @return new {@link BigInteger} copy of <code>other</code>, or <code>null</code> if
	 *         <code>string</code> is <code>null</code>
	 */
	public static BigInteger newBigIntegerNullSafe(final String string)
	{
		return isEmpty(string) ? null : new BigInteger(string);
	}
}
