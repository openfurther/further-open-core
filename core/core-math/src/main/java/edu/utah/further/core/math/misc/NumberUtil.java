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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;

/**
 * Class that contains a selection of numerical routines, integer permutation routines and
 * number-theory-related functions.
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
@Utility
@Api
@SuppressWarnings("boxing")
public final class NumberUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(NumberUtil.class);

	/**
	 * The number of witnesses queried in randomized primality test.
	 */
	public static final int TRIALS = 5;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private NumberUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Return x^n (mod p) Assumes x, n >= 0, p > 0, x < p, 0^0 = 1 Overflow may occur if p
	 * > 31 bits.
	 */
	public static long power(final long x, final long n, final long p)
	{
		if (n == 0)
			return 1;

		long tmp = power((x * x) % p, n / 2, p);

		if (n % 2 != 0)
			tmp = (tmp * x) % p;

		return tmp;
	}

	/**
	 * Randomized primality test. Adjust TRIALS to increase confidence level.
	 *
	 * @param n
	 *            the number to test.
	 * @return if false, n is definitely not prime. If true, n is probably prime.
	 */
	public static boolean isPrime(final long n)
	{
		final Random r = new Random();

		for (int counter = 0; counter < TRIALS; counter++)
			if (witness(r.nextInt((int) n - 3) + 2, n - 1, n) != 1)
				return false;

		return true;
	}

	/**
	 * Return the greatest common divisor.
	 */
	public static long gcd(final long a, final long b)
	{
		if (b == 0)
			return a;
		return gcd(b, a % b);
	}

	/**
	 * Works back through Euclid's algorithm to find x and y such that if gcd(a,b) = 1, ax
	 * + by = 1.
	 *
	 * @param a
	 * @param b
	 * @param pair
	 *            holds intermediate results as this method is called recursively
	 */
	private static void fullGcd(final long a, final long b,
			final ComparablePair<Long, Long> pair)
	{
		long x1, y1;

		if (b == 0)
		{
			pair.setLeft(1l);
			pair.setRight(0l);
		}
		else
		{
			fullGcd(b, a % b, pair);
			x1 = pair.getLeft();
			y1 = pair.getRight();
			pair.setLeft(y1);
			pair.setRight(x1 - (a / b) * y1);
		}
	}

	/**
	 * Solve ax == 1 (mod n), assuming gcd( a, n ) = 1.
	 *
	 * @return x
	 */
	public static long inverse(final long a, final long n)
	{
		final ComparablePair<Long, Long> pair = new ComparablePair<>(0l, 0l);
		fullGcd(a, n, pair);
		final long _x = pair.getLeft();
		return _x > 0 ? _x : _x + n;
	}

	/**
	 * Return the reverse permutation <code>b</code> of a permutation <code>a</code> of
	 * the numbers <code>[0..n-1]</code>. This means that <code>a o b = b o a = </code>
	 * the identity permutation.
	 * <p>
	 * For instance, if <code>n = 5, a = {4,0,3,1,2} then
	 * <code>b = {1,3,4,2,0}.
	 *
	 * @param permutation
	 *            permutation of the numbers <code>[0..n-1]</code>
	 * @return reverse permutation
	 */
	public static int[] reversePermutation(final int[] permutation)
	{
		final int n = permutation.length;
		final int[] reverse = new int[n];
		for (int i = 0; i < n; i++)
		{
			reverse[permutation[i]] = i;
		}
		return reverse;
	}

	/**
	 * Return the reverse permutation <code>b</code> of a permutation <code>a</code> of
	 * the numbers <code>[0..n-1]</code>. This means that <code>a o b = b o a = </code>
	 * the identity permutation.
	 * <p>
	 * For instance, if <code>n = 5, a = {4,0,3,1,2} then
	 * <code>b = {1,3,4,2,0}.
	 *
	 * @param permutation
	 *            permutation of the numbers <code>[0..n-1]</code>, as list
	 * @return reverse permutation
	 */
	public static int[] reversePermutation(final List<Integer> permutation)
	{
		final int n = permutation.size();
		final int[] array = new int[n];
		for (int i = 0; i < n; i++)
		{
			array[i] = permutation.get(i);
		}

		return reversePermutation(array);
	}

	/**
	 * Get base-<code>b</code> digits of a number. If <code>x</code> is negative, return
	 * the digits of <code>-x</code>. If <code>x=0</code>, return an array with a single
	 * entry (<code>0</code>).
	 *
	 * @param base
	 *            base to compute digits in
	 * @param x
	 *            a <i>positive</i> number to be analyzed
	 * @return list of digits. The first entry is the least significant digit; the last
	 *         one is the most significant digit.
	 */
	public static List<Integer> toDigits(final int base, final int x)
	{
		int y = x;
		final List<Integer> digits = newList();
		if (y == 0)
		{
			digits.add(0);
		}
		else
		{
			if (y < 0)
			{
				y = -y;
			}
			while (y != 0)
			{
				digits.add(y % base);
				y = y / base;
			}
		}
		return digits;
	}

	/**
	 * Convert base-<code>b</code> digits to a composite number. That is, <code>
	 * x = digits(k-1)*b^(k-1) + ... + digits(1)*b + digits(0)
	 * </code> The
	 * implementation uses Horner's rule for an <code>O(k)</code> computation.
	 *
	 * @param base
	 *            base to compute digits in
	 * @param digits
	 *            list of digits. The first entry is the least significant digit; the last
	 *            one is the most significant digit.
	 * @return <code>digits(k-1)*b^(k-1) + ... + digits(1)*b + digits(0)</code>
	 * @see http://www.brpreiss.com/books/opus4/html/page39.html
	 */
	public static int digitIntListToNumber(final int base, final List<Integer> digits)
	{
		final int k = digits.size() - 1;
		int x = digits.get(k);
		for (int i = k - 1; i >= 0; i--)
		{
			x = base * x + digits.get(i);
		}
		return x;
	}

	/**
	 * Convert base-<code>b</code> digits to a composite number. That is, <code>
	 * x = digits(k-1)*b^(k-1) + ... + digits(1)*b + digits(0)
	 * </code> The
	 * implementation uses Horner's rule for an <code>O(k)</code> computation.
	 *
	 * @param base
	 *            base to compute digits in
	 * @param digits
	 *            array of digits. The first entry is the least significant digit; the
	 *            last one is the most significant digit.
	 * @return <code>digits(k-1)*b^(k-1) + ... + digits(1)*b + digits(0)</code>
	 * @see http://www.brpreiss.com/books/opus4/html/page39.html
	 */
	public static int toNumber(final int base, final Integer... digits)
	{
		return digitIntListToNumber(base, asList(digits));
	}

	/**
	 * Get base-<code>b</code> digits of a number. If <code>x</code> is negative, return
	 * the digits of <code>-x</code>. If <code>x=0</code>, return an array with a single
	 * entry (<code>0</code>).
	 *
	 * @param base
	 *            base to compute digits in
	 * @param x
	 *            a <i>positive</i> number to be analyzed
	 * @return list of digits. The first entry is the least significant digit; the last
	 *         one is the most significant digit.
	 */
	public static List<Long> toDigits(final int base, final long x)
	{
		long y = x;
		final List<Long> digits = newList();
		if (y == 0)
		{
			digits.add(0l);
		}
		else
		{
			if (y < 0)
			{
				y = -y;
			}
			while (y != 0)
			{
				digits.add(y % base);
				y = y / base;
			}
		}
		return digits;
	}

	/**
	 * Convert base-<code>b</code> digits to a composite number. That is, <code>
	 * x = digits(k-1)*b^(k-1) + ... + digits(1)*b + digits(0)
	 * </code> The
	 * implementation uses Horner's rule for an <code>O(k)</code> computation.
	 *
	 * @param base
	 *            base to compute digits in
	 * @param digits
	 *            list of digits. The first entry is the least significant digit; the last
	 *            one is the most significant digit.
	 * @return <code>digits(k-1)*b^(k-1) + ... + digits(1)*b + digits(0)</code>
	 * @see http://www.brpreiss.com/books/opus4/html/page39.html
	 */
	public static long digitLongListToNumber(final int base, final List<Long> digits)
	{
		final int k = digits.size() - 1;
		long x = digits.get(k);
		for (int i = k - 1; i >= 0; i--)
		{
			x = base * x + digits.get(i);
		}
		return x;
	}

	/**
	 * Convert base-<code>b</code> digits to a composite number. That is, <code>
	 * x = digits(k-1)*b^(k-1) + ... + digits(1)*b + digits(0)
	 * </code> The
	 * implementation uses Horner's rule for an <code>O(k)</code> computation.
	 *
	 * @param base
	 *            base to compute digits in
	 * @param digits
	 *            array of digits. The first entry is the least significant digit; the
	 *            last one is the most significant digit.
	 * @return <code>digits(k-1)*b^(k-1) + ... + digits(1)*b + digits(0)</code>
	 * @see http://www.brpreiss.com/books/opus4/html/page39.html
	 */
	public static long toNumber(final int base, final Long... digits)
	{
		return digitLongListToNumber(base, asList(digits));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Private method that implements the basic primality test. If witness does not return
	 * 1, n is definitely composite. Do this by computing a^i (mod n) and looking for
	 * non-trivial square roots of 1 along the way.
	 *
	 * @param a
	 * @param i
	 * @param n
	 * @return
	 */
	private static long witness(final long a, final long i, final long n)
	{
		if (i == 0)
			return 1;

		final long x = witness(a, i / 2, n);
		if (x == 0) // If n is recursively composite, stop
			return 0;

		// n is not prime if we find a non-trivial square root of 1
		long y = (x * x) % n;
		if (y == 1 && x != 1 && x != n - 1)
			return 0;

		if (i % 2 != 0)
			y = (a * y) % n;

		return y;
	}
}
