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

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * A convenient base class of sub-classes of {@link EqualsBuilder} that returns its type
 * in method chaining.
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
 * @version Dec 24, 2009
 */
public class GenericEqualsBuilder<T extends GenericEqualsBuilder<T>> extends
		EqualsBuilder
{
	// ========================= METHODS ===================================

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(boolean, boolean)
	 */
	@Override
	public T append(final boolean lhs, final boolean rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(boolean[], boolean[])
	 */
	@Override
	public T append(final boolean[] lhs, final boolean[] rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(byte, byte)
	 */
	@Override
	public T append(final byte lhs, final byte rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(byte[], byte[])
	 */
	@Override
	public T append(final byte[] lhs, final byte[] rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(char, char)
	 */
	@Override
	public T append(final char lhs, final char rhs)
	{

		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(char[], char[])
	 */
	@Override
	public T append(final char[] lhs, final char[] rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(double, double)
	 */
	@Override
	public T append(final double lhs, final double rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(double[], double[])
	 */
	@Override
	public T append(final double[] lhs, final double[] rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(float, float)
	 */
	@Override
	public T append(final float lhs, final float rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(float[], float[])
	 */
	@Override
	public T append(final float[] lhs, final float[] rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(int, int)
	 */
	@Override
	public T append(final int lhs, final int rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(int[], int[])
	 */
	@Override
	public T append(final int[] lhs, final int[] rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(long, long)
	 */
	@Override
	public T append(final long lhs, final long rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(long[], long[])
	 */
	@Override
	public T append(final long[] lhs, final long[] rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public T append(final Object lhs, final Object rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(java.lang.Object[],
	 *      java.lang.Object[])
	 */
	@Override
	public T append(final Object[] lhs, final Object[] rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(short, short)
	 */
	@Override
	public T append(final short lhs, final short rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#append(short[], short[])
	 */
	@Override
	public T append(final short[] lhs, final short[] rhs)
	{
		return (T) super.append(lhs, rhs);
	}

	/**
	 * @param superEquals
	 * @return
	 * @see org.apache.commons.lang.builder.EqualsBuilder#appendSuper(boolean)
	 */
	@Override
	public T appendSuper(final boolean superEquals)
	{
		return (T) super.appendSuper(superEquals);
	}
}
