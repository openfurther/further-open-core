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

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.message.ValidationUtil.validateIsTrue;
import static java.lang.Math.abs;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Utility;

/**
 * This function returns a variable-base multiple-digit gray code. G = grayCode(N,K)
 * returns the gray code permutation of the integers from 0 to prod(K)-1. N bust be a
 * non-negative integer and K must be an N-vector of non-negative integers of bases. K[0]
 * is the base of the right-most digit (LSB) in the N-digit string space, K[1] the base of
 * the next right digit, and so on. The generated gray code is not necessarily cyclic. G
 * is an array of size prod(K)xN, whose rows are the gray-code-ordered N-digit strings.
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
public final class GrayCode
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(GrayCode.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private GrayCode()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Generate a base-k, length-n gray code. WARNING: this code is very very slow for
	 * some reason!
	 *
	 * @param n
	 *            #digits
	 * @param k
	 *            base for each digit
	 * @return gray code
	 * @deprecated
	 */
	@Deprecated
	public static int[][] generate(final int n, final int[] k)
	{
		if (log.isDebugEnabled())
		{
			log.debug("n " + n + " k " + k);
		}
		validateIsTrue(n > 0, "n must be positive");
		validateIsTrue(k.length == n, "k's length must be n");

		int numRows = 1;
		int numCols = 0;
		int m = 0;
		numRows *= k[m];
		numCols++;
		int[][] G = new int[numRows][];
		for (int i = 0; i < numRows; i++)
		{
			G[i] = new int[numCols];
		}
		for (int i = 0; i < k[0]; i++)
		{
			G[i][m] = i;
		}
		if (log.isDebugEnabled())
		{
			log.debug("G numRows=" + G.length + " numCols = " + G[0].length);
			log.debug("  G for " + m + " digits = " + G);
		}

		// Generate G recursively
		for (m = 1; m < n; m++)
		{
			final int b = k[m];
			numRows *= b;
			numCols++;
			final int[][] Gnew = new int[numRows][];
			for (int i = 0; i < numRows; i++)
			{
				Gnew[i] = new int[numCols];
			}
			int startRow = 0;
			if (log.isDebugEnabled())
			{
				log.debug("m = " + m + " " + "b = " + b);
			}
			for (int d = 0; d < b; d++)
			{
				if ((d % 2) == 1)
				{
					// odd d
					if (log.isDebugEnabled())
					{
						log.debug("  (G*)^(" + d + ")");
					}
					for (int i = 0; i < G.length; i++)
					{
						for (int j = 0; j < G[i].length; j++)
						{
							// Gnew(startRow+G.numRows()-1-i,j) = G(i,j);
							Gnew[startRow + G.length - 1 - i][j] = G[i][j];
						}
						// Gnew(startRow+G.numRows()-1-i,m) = d;
						Gnew[startRow + G.length - 1 - i][m] = d;
					}
				} // end odd d
				else
				{
					// even d
					if (log.isDebugEnabled())
					{
						log.debug("  G^(" + d + ")");
					}
					for (int i = 0; i < G.length; i++)
					{
						for (int j = 0; j < G[i].length; j++)
						{
							// Gnew(startRow+i,j) = G(i,j);
							Gnew[startRow + i][j] = G[i][j];
						}
						// Gnew(startRow+i,m) = d;
						Gnew[startRow + i][m] = d;
					}
				} // end even d
				startRow += G.length;
			} // end for d

			G = Gnew;
			/*
			 * G.setSize(Gnew.size()); for (int i = 0; i < Gnew.size(); i++) { G.set(i,
			 * new Array<Integer>()); G.get(i).setSize(Gnew.get(i).size()); for (int j =
			 * 0; j < Gnew.get(i).size(); j++) { G.get(i).set(j, Gnew.get(i).get(j)); } }
			 */
			if (log.isDebugEnabled())
			{
				log.debug("G numRows=" + G.length + " numCols = " + G[0].length);
				log.debug("  G for " + m + " digits = " + G);
			}
		} // end for m

		// Check result
		boolean fail = false;
		for (int i = 0; i < G.length - 1; i++)
		{
			int diff = 0;
			for (int j = 0; j < G[i].length; j++)
			{
				diff += (abs(G[i][j] - G[i + 1][j]));
			}
			if (diff != 1)
			{
				fail = true;
				log.debug("failed in difference between rows " + i + " and " + (i + 1));
				break;
			}
		} // end for i

		for (int i = 0; (i < G.length) && (!fail); i++)
		{
			for (int i2 = 0; i2 < G.length; i2++)
			{
				if (i != i2)
				{
					int diff = 0;
					for (int j = 0; j < G[i].length; j++)
					{
						diff += (Math.abs(G[i][j] - G[i2][j]));
					}
					if (diff == 0)
					{
						fail = true;
						log.debug("failed in equality of rows " + i + " and " + i2);
						break;
					}
				}
			} // end for i2
		} // end for i
		if (fail)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Gray code is incorrect!!!");
			}
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("Gray code is correct.");
			}
		}

		if (log.isDebugEnabled())
		{
			log.debug("size(G) " + G.length);
		}
		return G;
	} // end generate()
}
