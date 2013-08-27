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

import static edu.utah.further.core.api.collections.CollectionUtil.toList;
import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_INTEGER;
import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.api.message.ValidationUtil.validateIsTrue;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.random.RandomGenerator;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.exception.BusinessRuleException;

/**
 * Centralized utilities related to random number generation. Centralizes all methods
 * related to randomization, shuffling, etc. Can be wired with any {@link RandomGenerator}
 * implementation (in testing a deterministic source may be desirable to reproduce
 * results).
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
 * @version Dec 17, 2008
 */
@Service("randomUtilService")
public class RandomUtilService
{
	// ========================= CONSTANTS =================================

	/**
	 * For logging printouts.
	 */
	private static final Logger log = getLogger(RandomUtilService.class);

	// ==========================================================
	// Confidence intervals / t-Distributions
	// ==========================================================
	/*
	 * Original table Reference: http://en.wikipedia.org/wiki/Student%27s_t-distribution r
	 * 75% 80% 85% 90% 95% 97.5% 99% 99.5% 99.75% 99.9% 99.95% 1 1.000 1.376 1.963 3.078
	 * 6.314 12.71 31.82 63.66 127.3 318.3 636.6 2 0.816 1.061 1.386 1.886 2.920 4.303
	 * 6.965 9.925 14.09 22.33 31.60 3 0.765 0.978 1.250 1.638 2.353 3.182 4.541 5.841
	 * 7.453 10.21 12.92 4 0.741 0.941 1.190 1.533 2.132 2.776 3.747 4.604 5.598 7.173
	 * 8.610 5 0.727 0.920 1.156 1.476 2.015 2.571 3.365 4.032 4.773 5.893 6.869 6 0.718
	 * 0.906 1.134 1.440 1.943 2.447 3.143 3.707 4.317 5.208 5.959 7 0.711 0.896 1.119
	 * 1.415 1.895 2.365 2.998 3.499 4.029 4.785 5.408 8 0.706 0.889 1.108 1.397 1.860
	 * 2.306 2.896 3.355 3.833 4.501 5.041 9 0.703 0.883 1.100 1.383 1.833 2.262 2.821
	 * 3.250 3.690 4.297 4.781 10 0.700 0.879 1.093 1.372 1.812 2.228 2.764 3.169 3.581
	 * 4.144 4.587
	 */
	private static final double[] conf =
	{ .75, .80, .85, .90, .95, .975, .99, .995, .9975, .999, .9995 };

	private static final int[] dof =
	{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	private static final double[][] tDistrib =
	{
	{ 1.000, 1.376, 1.963, 3.078, 6.314, 12.71, 31.82, 63.66, 127.3, 318.3, 636.6 },
	{ 0.816, 1.061, 1.386, 1.886, 2.920, 4.303, 6.965, 9.925, 14.09, 22.33, 31.60 },
	{ 0.765, 0.978, 1.250, 1.638, 2.353, 3.182, 4.541, 5.841, 7.453, 10.21, 12.92 },
	{ 0.741, 0.941, 1.190, 1.533, 2.132, 2.776, 3.747, 4.604, 5.598, 7.173, 8.610 },
	{ 0.727, 0.920, 1.156, 1.476, 2.015, 2.571, 3.365, 4.032, 4.773, 5.893, 6.869 },
	{ 0.718, 0.906, 1.134, 1.440, 1.943, 2.447, 3.143, 3.707, 4.317, 5.208, 5.959 },
	{ 0.711, 0.896, 1.119, 1.415, 1.895, 2.365, 2.998, 3.499, 4.029, 4.785, 5.408 },
	{ 0.706, 0.889, 1.108, 1.397, 1.860, 2.306, 2.896, 3.355, 3.833, 4.501, 5.041 },
	{ 0.703, 0.883, 1.100, 1.383, 1.833, 2.262, 2.821, 3.250, 3.690, 4.297, 4.781 },
	{ 0.700, 0.879, 1.093, 1.372, 1.812, 2.228, 2.764, 3.169, 3.581, 4.144, 4.587 } };

	// ========================= DEPENDENCIES ==============================

	/**
	 * Random number generator.
	 */
	private RandomGenerator randomGenerator;

	// ========================= FIELDS ====================================

	/**
	 * Commons math random generator utility class.
	 */
	private RandomData randomData = newRandomData(randomGenerator);

	// ========================= METHODS ===================================

	/**
	 * Returns a reference to commons math random generator utility class. This is
	 * basically a factory of random numbers in various forms.
	 * 
	 * @return random data factory instance
	 */
	public RandomData getRandomData()
	{
		return randomData;
	}

	/**
	 * Generate a random double in [0.0,1.0].
	 * 
	 * @return random integer in [low:high]
	 */
	public double randomDouble()
	{
		return randomDouble(0.0d, 1.0d);
	}

	/**
	 * Generate a random double in an interval.
	 * 
	 * @param low
	 *            lower bound (inclusive)
	 * @param high
	 *            upper bound (inclusive)
	 * @return random integer in [low:high]
	 */
	public double randomDouble(final double low, final double high)
	{
		validateIsTrue(low < high, "Empty range [" + low + "," + high + "]");
		return getRandomData().nextUniform(low, high);
	}

	/**
	 * Generate a random integer in an interval (including endpoints). It's OK to specify
	 * <code>low=high</code>.
	 * 
	 * @param low
	 *            lower bound (inclusive)
	 * @param high
	 *            upper bound (inclusive)
	 * @return random integer in [low:high]
	 */
	public int randomInteger(final int low, final int high)
	{
		validateIsTrue(low <= high, "Empty range [" + low + "," + high + "]");
		return (low == high) ? low : getRandomData().nextInt(low, high);
	}

	/**
	 * Generate a random boolean.
	 * 
	 * @param frequency
	 *            average (number of returning false + 1) per one true return
	 * @return random boolean; average (number of returning false + 1) per one true return
	 */
	public boolean randomBoolean(final int frequency)
	{
		// Generate a random integer in the range
		return (randomInteger(1, frequency) == 1);
	}

	/**
	 * Generate a random boolean with equal probabilities for returning true and false.
	 * 
	 * @return a random boolean with equal probabilities for returning true and false
	 */
	public boolean randomBoolean()
	{
		return (randomDouble() < 0.5);
	}

	/**
	 * Generate a random enumerated type.
	 * 
	 * @param <E>
	 *            enum type
	 * @param enumType
	 *            the Class object of the enumerated type
	 * @return random enum constant
	 */
	public <E extends Enum<E>> E randomEnum(final Class<E> enumType)
	{
		final E[] values = enumType.getEnumConstants();
		return values[randomInteger(0, values.length - 1)];
	}

	/**
	 * Create a new random numerical password for a user. Consists of digits only between
	 * 0 and <code>maxPassword-1</code>.
	 * 
	 * @param maxPassword
	 *            maximum number allowed for the password
	 * @return password String
	 */
	public String randomLongPassword(final int maxPassword)
	{
		if (maxPassword <= 0)
		{
			throw new BusinessRuleException(
					"Cannot generate password with non-positive length " + maxPassword);
		}
		int num = 0;
		while (num == 0)
		{
			num = getRandomData().nextSecureInt(0, maxPassword - 1);
		}
		String password = EMPTY_STRING;
		int pad = 10 * num;
		while (pad < maxPassword)
		{
			password = password + "0";
			pad *= 10;
		}
		password = password + num; // Bizare long-to-String conversion
		return password;
	} // randomPassword()

	/**
	 * Generate a random element in a discrete set (list), where each element has a
	 * probability of being selected. This is a multinomial distribution with values
	 * <code>[0..p.length-1]</code> and corresponding probabilities
	 * <code>[p[0],...,p[p.length-1]]</code>.
	 * 
	 * @param p
	 *            array of probabilities. <code>p[i]</code> is the probability of
	 *            selecting <code>i, sum(p[i]) = 1, 0 <= p[i] <= 1</code>.
	 * @param numSamples
	 *            #generated random indices
	 * @return random integer in <code>[0..p.length-1]</code>
	 */
	public int randomMultinomial(final double[] p)
	{
		// x is uniformly random in [0,1]
		final double x = randomDouble();

		// If x is in the interval [q[i]..q[i+1]), choose i
		// where q[i] = sum(p[j], j=0..i-1), q[0] = 0
		double low, high = 0.0d;
		for (int i = 0; i < p.length; i++)
		{
			low = high;
			high += p[i];
			if ((x >= low) && (x < high))
			{
				return i;
			}
		}
		// Something went wrong
		return INVALID_VALUE_INTEGER;
	}

	/**
	 * Generate a random element in a discrete set (list), where each element has a
	 * probability of being selected. This is a multinomial distribution with values
	 * <code>[0..p.length-1]</code> and corresponding probabilities
	 * <code>[p[0],...,p[p.length-1]]</code>.
	 * 
	 * @param p
	 *            array of probabilities. <code>p[i]</code> is the probability of
	 *            selecting <code>i, sum(p[i]) = 1, 0 <= p[i] <= 1</code>.
	 * @param numSamples
	 *            #generated random indices
	 * @return random integer in <code>[0..p.length-1]</code>
	 */
	public int randomMultinomial(final Double[] p)
	{
		// x is uniformly random in [0,1]
		final double x = randomDouble();

		// If x is in the interval [q[i]..q[i+1]), choose i
		// where q[i] = sum(p[j], j=0..i-1), q[0] = 0
		double low, high = 0.0d;
		for (int i = 0; i < p.length; i++)
		{
			low = high;
			high += p[i].doubleValue();
			if ((x >= low) && (x < high))
			{
				return i;
			}
		}

		// Something went wrong
		log.error("Could not select random multinomial, check that p sums to 1: "
				+ Arrays.toString(p));
		return INVALID_VALUE_INTEGER;
	}

	/**
	 * Return the constant A in the one-sided confidence interval lower bound on a
	 * Gaussian random variable X, which is (mu - A*std/sqrt(n-1)) where mu = average of n
	 * samples of X and std = their standard deviation. Here #dof = n-1.
	 * 
	 * @param confidence
	 *            desired confidence level (in [0,1])
	 * @param thisDof
	 *            degrees of freedom
	 * @return confidence interval constant A
	 */
	@Deprecated
	public double tDistribution(final double confidence, final int thisDof)
	{
		if (thisDof <= 0)
		{
			return POSITIVE_INFINITY;
		}
		int dofIndex = 0;
		for (int i = 0; i < dof.length; i++, dofIndex++)
		{
			if (thisDof <= dof[i])
			{
				break;
			}
		}
		if (dofIndex == dof.length)
		{
			dofIndex--;
		}

		int confIndex = 0;
		for (int i = 0; i < conf.length; i++, confIndex++)
		{
			if (confidence <= conf[i])
			{
				break;
			}
		}
		if (confIndex == conf.length)
			confIndex--;

		return tDistrib[dofIndex][confIndex];
	}

	/**
	 * One-sided confidence interval lower bound on a Gaussian random variable X, which is
	 * (mu - A*std/sqrt(n-1)) where mu = average of n samples of X and std = their
	 * standard deviation. Here #dof = n-1 and A is a function of the dof
	 * 
	 * @param mu
	 *            average of X measured over n samples
	 * @param std
	 *            standard deviation of X (continuous, or measured over the same n
	 *            samples)
	 * @param confidence
	 *            desired confidence level (in [0,1])
	 * @param n
	 *            number of samples
	 * @return One-sided confidence interval lower bound
	 */
	public double confidenceLowerBound(final double confidence, final int n,
			final double mu, final double std)
	{
		final int degreesOfFreedom = n - 1;
		if (degreesOfFreedom <= 0)
		{
			return NEGATIVE_INFINITY;
		}
		final double A = tDistribution(confidence, degreesOfFreedom);
		return mu - A * std / Math.sqrt(degreesOfFreedom);
		// TODO: test this code and if it yields the same resulst as the above
		// deprecated call, use it instead and delete the tDistribution() method
		// final TDistribution tDistribution = new TDistributionImpl(degreesOfFreedom);
		// try
		// {
		// double A = tDistribution.cumulativeProbability(confidence);
		// return mu - A * std / Math.sqrt(degreesOfFreedom);
		// }
		// catch (final MathException e)
		// {
		// throw new ApplicationException(ErrorCode.MATH_ERROR,
		// "Failed to evaluate t-distribution at " + confidence, e);
		// }
	}

	/**
	 * Generate a random letter.
	 * 
	 * @return random letter
	 */
	public char randomLetter()
	{
		return (char) randomInteger('a', 'z');
	}

	/**
	 * Generate a random string consisting of letters only.
	 * 
	 * @param low
	 *            minimum string length
	 * @param high
	 *            maximum string length
	 * @return random string consisting of letters only
	 */
	public String randomWord(final int low, final int high)
	{
		final int n = randomInteger(low, high);
		final byte b[] = new byte[n];
		for (int i = 0; i < n; i++)
		{
			b[i] = (byte) randomInteger('a', 'z');
		}
		return new String(b);
	}

	/**
	 * Generate a random member of a list.
	 * 
	 * @param list
	 *            list
	 * @return random member of the list
	 */
	public <E> E randomInList(final List<? extends E> list)
	{
		return list.get(randomInteger(0, list.size() - 1));
	}

	/**
	 * Generate a random member of an array.
	 * 
	 * @param array
	 *            array
	 * @return random member of the array
	 */
	public <E> E randomInList(final E[] array)
	{
		return array[randomInteger(0, array.length - 1)];
	}

	/**
	 * Return a random permutation of <code>[0..n-1]</code> an array.
	 * 
	 * @param n
	 *            size of permutation
	 * @return an array of integers that are a random permutation of [0..n-1].
	 */
	public int[] randomPermutation(final int n)
	{
		return randomPermutation(n, n);
	}

	/**
	 * Generates an integer array of length <code>k</code> whose entries are selected
	 * randomly, without repetition, from the integers <code>0</code> through
	 * <code>n-1</code> (inclusive).
	 * <p>
	 * Generated arrays represent permutations of n taken k at a time.
	 * <p>
	 * 
	 * @param n
	 *            domain of the permutation
	 * @param k
	 *            size of the permutation
	 * @return random k-permutation of n
	 * @see RandomData#nextPermutation(int, int)
	 */
	public int[] randomPermutation(final int n, final int k)
	{
		return getRandomData().nextPermutation(n, k);
	}

	/**
	 * Return a random permutation of [0..n-1] a list (uniformly distributed).
	 * 
	 * @param n
	 *            size of permutation
	 * @return a list of integers that are a random permutation of [0..n-1].
	 */
	public List<Integer> randomPermutationAsList(final int n)
	{
		return toList(getRandomData().nextPermutation(n, n));
	}

	// ========================= DEPENDENCY INJECTION ======================

	/**
	 * Set a new value for the randomGenerator property.
	 * 
	 * @param randomGenerator
	 *            the randomGenerator to set
	 */
	public void setRandomGenerator(final RandomGenerator randomGenerator)
	{
		this.randomGenerator = randomGenerator;
		this.randomData = newRandomData(randomGenerator);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param randomGenerator
	 * @return
	 */
	private static RandomData newRandomData(final RandomGenerator randomGenerator)
	{
		return (randomGenerator == null) ? new RandomDataImpl() : new RandomDataImpl(
				randomGenerator);
	}
}
