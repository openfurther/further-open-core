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

import org.apache.commons.math.random.RandomGenerator;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.core.api.context.Mock;
import edu.utah.further.core.api.lang.CoreUtil;

/**
 * Mock random number generator that generates the same value for every category (int,
 * double, etc.).
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
@Qualifier("randomGeneratorFixed")
@Mock
public class RandomGeneratorFixedImpl implements RandomGenerator
{
	// ========================= IMPLEMENTATION: RadomGenerator ============

	/**
	 * @return
	 * @see org.apache.commons.math.random.RandomGenerator#nextBoolean()
	 */
	@Override
	public boolean nextBoolean()
	{
		return false;
	}

	/**
	 * @param bytes
	 * @see org.apache.commons.math.random.RandomGenerator#nextBytes(byte[])
	 */
	@Override
	public void nextBytes(final byte[] bytes)
	{
		throw CoreUtil.newUnsupportedOperationExceptionNotImplementedYet();
	}

	/**
	 * @return
	 * @see org.apache.commons.math.random.RandomGenerator#nextDouble()
	 */
	@Override
	public double nextDouble()
	{
		return 0.1d;
	}

	/**
	 * @return
	 * @see org.apache.commons.math.random.RandomGenerator#nextFloat()
	 */
	@Override
	public float nextFloat()
	{
		return 0.2f;
	}

	/**
	 * @return
	 * @see org.apache.commons.math.random.RandomGenerator#nextGaussian()
	 */
	@Override
	public double nextGaussian()
	{
		return 0.3d;
	}

	/**
	 * @return
	 * @see org.apache.commons.math.random.RandomGenerator#nextInt()
	 */
	@Override
	public int nextInt()
	{
		return 0;
	}

	/**
	 * @param n
	 * @return
	 * @see org.apache.commons.math.random.RandomGenerator#nextInt(int)
	 */
	@Override
	public int nextInt(final int n)
	{
		return 0;
	}

	/**
	 * @return
	 * @see org.apache.commons.math.random.RandomGenerator#nextLong()
	 */
	@Override
	public long nextLong()
	{
		return 0;
	}

	/**
	 * @param seed
	 * @see org.apache.commons.math.random.RandomGenerator#setSeed(int)
	 */
	@Override
	public void setSeed(final int seed)
	{
		// Seed is always fixed
	}

	/**
	 * @param seed
	 * @see org.apache.commons.math.random.RandomGenerator#setSeed(int[])
	 */
	@Override
	public void setSeed(final int[] seed)
	{
		// Seed is always fixed
	}

	/**
	 * @param seed
	 * @see org.apache.commons.math.random.RandomGenerator#setSeed(long)
	 */
	@Override
	public void setSeed(final long seed)
	{
		// Seed is always fixed
	}

}
