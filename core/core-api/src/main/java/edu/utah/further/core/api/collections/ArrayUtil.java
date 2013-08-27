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
package edu.utah.further.core.api.collections;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.lang.ArrayUtils;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.math.ArithmeticUtil;
import edu.utah.further.core.api.text.StringUtil;

/**
 * Low-level array-related utilities. See {@link ArithmeticUtil} for arithmetic operations
 * of arrays.
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
public final class ArrayUtil
{
	// ========================= METHODS ====================================

	/**
	 * @param list
	 * @return
	 */
	public static byte[] byteListToPrimitiveArray(final List<Byte> list)
	{
		return ArrayUtils.toPrimitive(list.toArray(ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY));
	}

	/**
	 * @param list
	 * @return
	 */
	public static int[] intListToPrimitiveArray(final List<Integer> list)
	{
		return ArrayUtils
				.toPrimitive(list.toArray(ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY));
	}

	/**
	 * @param set
	 * @return
	 */
	public static double[] toDoubleArray(final SortedSet<Integer> set)
	{
		final int length = set.size();
		final double[] doubleArray = new double[length];
		int i = 0;
		for (final Integer element : set)
		{
			doubleArray[i] = element.intValue();
			i++;
		}
		return doubleArray;
	}

	/**
	 * @param intArray
	 * @return
	 */
	public static double[] toDoubleArray(final int[] intArray)
	{
		final int length = intArray.length;
		final double[] doubleArray = new double[length];
		for (int i = 0; i < intArray.length; i++)
		{
			doubleArray[i] = intArray[i];
		}
		return doubleArray;
	}

	/**
	 * @param row
	 * @return
	 */
	public static double[] toDoubleArray(final List<Double> row)
	{
		final int rowSize = row.size();
		final double[] rowArray = new double[rowSize];
		for (int i = 0; i < rowSize; i++)
		{
			rowArray[i] = row.get(i).doubleValue();
		}
		return rowArray;
	}

	/**
	 * @param size1
	 * @param size2
	 * @param fillValue
	 * @return
	 */
	public static int[][] newIntMatrix(final int size1, final int size2,
			final int fillValue)
	{
		final int[][] a = new int[size1][];
		for (int i = 0; i < size1; i++)
		{
			a[i] = newIntVector(size2, fillValue);
		}
		return a;
	}

	/**
	 * @param size1
	 * @param size2
	 * @param fillValue
	 * @return
	 */
	public static double[][] newDoubleMatrix(final int size1, final int size2,
			final double fillValue)
	{
		final double[][] a = new double[size1][];
		for (int i = 0; i < size1; i++)
		{
			a[i] = newDoubleVector(size2, fillValue);
		}
		return a;
	}

	/**
	 * @param size1
	 * @param size2
	 * @param fillValue
	 * @return
	 */
	public static String[][] newStringMatrix(final int size1, final int size2,
			final String fillValue)
	{
		final String[][] a = new String[size1][];
		for (int i = 0; i < size1; i++)
		{
			a[i] = newStringVector(size2, fillValue);
		}
		return a;
	}

	/**
	 * @param size
	 * @param fillValue
	 * @return
	 */
	public static byte[] newByteVector(final int size, final byte fillValue)
	{
		final byte[] a = new byte[size];
		Arrays.fill(a, fillValue);
		return a;
	}

	/**
	 * @param size
	 * @param fillValue
	 * @return
	 */
	public static int[] newIntVector(final int size, final int fillValue)
	{
		final int[] a = new int[size];
		Arrays.fill(a, fillValue);
		return a;
	}

	/**
	 * /**
	 *
	 * @param size
	 * @param fillValue
	 * @return
	 */
	public static double[] newDoubleVector(final int size, final double fillValue)
	{
		final double[] a = new double[size];
		Arrays.fill(a, fillValue);
		return a;
	}

	/**
	 * @param size
	 * @param fillValue
	 * @return
	 */
	public static String[] newStringVector(final int size, final String fillValue)
	{
		final String[] a = new String[size];
		Arrays.fill(a, fillValue);
		return a;
	}

	/**
	 * Return an array of the numbers 0..n-1.
	 *
	 * @param n
	 *            array size
	 * @return ordinal array of size n
	 */
	public static int[] newOrdinalArray(final int n)
	{
		final int[] ordinalVector = new int[n];
		for (int i = 0; i < n; i++)
		{
			ordinalVector[i] = i;
		}
		return ordinalVector;
	}

	/**
	 * Sets <code>matrix</code>'s column. Column indices start at 0.
	 *
	 * @param matrix
	 *            matrix to update
	 * @param column
	 *            column index to update
	 * @param newColumn
	 *            new column values
	 */
	public static void setColumnVector(final byte[][] matrix, final int column,
			final byte[] newColumn)
	{
		final int numRows = matrix.length;
		validateNumRows(numRows, newColumn.length);
		for (int i = 0; i < numRows; i++)
		{
			matrix[i][column] = newColumn[i];
		}
	}

	/**
	 * Sets <code>matrix</code>'s column. Column indices start at 0.
	 *
	 * @param matrix
	 *            matrix to update
	 * @param column
	 *            column index to update
	 * @param newColumn
	 *            new column values
	 */
	public static void setColumnVector(final int[][] matrix, final int column,
			final int[] newColumn)
	{
		final int numRows = matrix.length;
		validateNumRows(numRows, newColumn.length);
		for (int i = 0; i < numRows; i++)
		{
			matrix[i][column] = newColumn[i];
		}
	}

	/**
	 * Sets <code>matrix</code>'s entries in a set <code>selectedColumns</code> of column
	 * numbers. Column indices start at 0.
	 *
	 * @param matrix
	 *            matrix to update
	 * @param selectedColumns
	 *            the Columns to be override
	 * @param newSubMatrix
	 *            column matrix (must have <code>selectedColumns.length</code> Columns and
	 *            the same number of columns as the instance. The <code>j</code> <i>th</i>
	 *            column of <code>matrix</code> will override the instance's column number
	 *            <code>selectedColumns[j]</code>,
	 *            <code>j = 0..selectedColumns.length</code>
	 */
	public static void setColumnMatrix(final byte[][] matrix,
			final int[] selectedColumns, final byte[][] newSubMatrix)
	{
		final int numRows = matrix.length;
		validateNumRows(numRows, newSubMatrix.length);
		for (int j = 0; j < selectedColumns.length; j++)
		{
			final int targetColumn = selectedColumns[j];
			for (int i = 0; i < numRows; i++)
			{
				matrix[i][targetColumn] = newSubMatrix[i][j];
			}
		}
	}

	/**
	 * Sets <code>matrix</code>'s entries in a set <code>selectedColumns</code> of column
	 * numbers. Column indices start at 0.
	 *
	 * @param matrix
	 *            matrix to update
	 * @param selectedColumns
	 *            the Columns to be override
	 * @param newSubMatrix
	 *            column matrix (must have <code>selectedColumns.length</code> Columns and
	 *            the same number of columns as the instance. The <code>j</code> <i>th</i>
	 *            column of <code>matrix</code> will override the instance's column number
	 *            <code>selectedColumns[j]</code>,
	 *            <code>j = 0..selectedColumns.length</code>
	 */
	public static void setColumnMatrix(final int[][] matrix, final int[] selectedColumns,
			final int[][] newSubMatrix)
	{
		final int numRows = matrix.length;
		validateNumRows(numRows, newSubMatrix.length);
		for (int j = 0; j < selectedColumns.length; j++)
		{
			final int targetColumn = selectedColumns[j];
			for (int i = 0; i < numRows; i++)
			{
				matrix[i][targetColumn] = newSubMatrix[i][j];
			}
		}
	}

	/**
	 * Copy array with zero offsets. src is copied into dest starting at src[0] and
	 * dest[0].
	 *
	 * @param src
	 *            source array
	 * @param dest
	 *            destination array
	 */
	public static void arrayCopy(final byte[] src, final byte[] dest)
	{
		System.arraycopy(src, 0, dest, 0, src.length);
	}

	/**
	 * Copy array with zero offsets. src is copied into dest starting at src[0] and
	 * dest[0].
	 *
	 * @param src
	 *            source array
	 * @param dest
	 *            destination array
	 */
	public static void arrayCopy(final int[] src, final int[] dest)
	{
		System.arraycopy(src, 0, dest, 0, src.length);
	}

	/**
	 * Copy array with zero offsets. src is copied into dest starting at src[0] and
	 * dest[0].
	 *
	 * @param src
	 *            source array
	 * @param dest
	 *            destination array
	 */
	public static void arrayCopy(final double[] src, final double[] dest)
	{
		System.arraycopy(src, 0, dest, 0, src.length);
	}

	/**
	 * Appends two byte array into one.
	 *
	 * @param a
	 *            An integer array
	 * @param b
	 *            An integer array
	 * @return result of concatenation of a and b
	 */
	public static byte[] concat(final byte[] a, final byte[] b)
	{
		final byte[] result = new byte[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	/**
	 * Appends two integer array into one.
	 *
	 * @param a
	 *            An integer array
	 * @param b
	 *            An integer array
	 * @return result of concatenation of a and b
	 */
	public static int[] concat(final int[] a, final int[] b)
	{
		final int[] result = new int[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	/**
	 * @param array
	 * @param numDigits
	 * @return
	 */
	public static String toString(final double[] array, final int numDigits)
	{
		final StringBuilder s = StringUtil.newStringBuilder().append("[ ");
		for (int i = 0; i < array.length; i++)
		{
			s.append(String.format("%." + numDigits + "f ", new Double(array[i])));
		}
		return s.append("]").toString();
	}

	/**
	 * Print a matrix in a grid format with a default separator and column size.
	 *
	 * @param matrix
	 *            data matrix
	 * @param separator
	 *            entry separator
	 * @param columnSize
	 *            size of each column (not including the separator)
	 * @return textual representation of the matrix
	 */
	public static String toString(final byte[][] matrix)
	{
		return toString(matrix, Strings.SPACE_STRING, null, 3);
	}

	/**
	 * Print a matrix in a grid format.
	 *
	 * @param matrix
	 *            data matrix
	 * @param separator
	 *            entry separator
	 * @param labels
	 *            column labels. If <code>null</code>, ignored
	 * @param columnSize
	 *            size of each column (not including the separator)
	 * @return textual representation of the matrix
	 */
	public static String toString(final byte[][] matrix, final String separator,
			final String[] labels, final int columnSize)
	{
		final String labelFormat = "%-" + columnSize + "s";
		final String entryFormat = "%-" + columnSize + "d";
		final StringBuilder builder = StringUtil.newStringBuilder();
		final int columnDimension = matrix[0].length;
		if (labels != null)
		{
			for (int j = 0; j < matrix[0].length; j++)
			{
				builder.append(String.format(labelFormat, labels[j]));
				builder.append(separator);
			}
		}
		builder.append(Strings.NEW_LINE_STRING);
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[0].length; j++)
			{
				builder.append(String.format(entryFormat, new Byte(matrix[i][j])));
				// builder.append(matrix[i][j]);
				if (j < columnDimension - 1)
				{
					builder.append(separator);
				}
			}
			builder.append(Strings.NEW_LINE_STRING);
		}
		return builder.toString();
	}

	// ========================= PRIVATE METHODS ============================

	/**
	 * @param thisNumRows
	 * @param otherNumRows
	 */
	private static void validateNumRows(final int thisNumRows, final int otherNumRows)
	{
		if (otherNumRows != thisNumRows)
		{
			throw new IllegalArgumentException(
					"Invalid number of rows in the matrix passed to set columns in this instance. Expected "
							+ thisNumRows + " found " + otherNumRows);
		}
	}
}
