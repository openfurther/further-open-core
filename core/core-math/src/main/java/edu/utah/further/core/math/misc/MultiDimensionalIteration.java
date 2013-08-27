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

import static edu.utah.further.core.math.misc.MultiDimensionalIterator.ones;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Api;

/**
 * A struct holding the ranges of a multi-dimensional iterator over a box. Immutable.
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
public final class MultiDimensionalIteration
{
	// ========================= PRIVATE CONSTANTS =========================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(MultiDimensionalIteration.class);

	// ========================= FIELDS ====================================

	/**
	 * number of dimensions.
	 */
	public final int numDims;

	/**
	 * Lower-left corner of box (in d dimensions).
	 */
	public final int[] lower;

	/**
	 * Upper-right corner of box (in d dimensions).
	 */
	public final int[] upper;

	/**
	 * Increment (step) in each dimension in an iteration step.
	 */
	public final int[] increment;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a multi-dimensional iteration struct.
	 *
	 * @param numDims
	 *            number of dimensions
	 */
	public MultiDimensionalIteration(int numDims)
	{
		this.numDims = numDims;
		lower = new int[numDims];
		upper = new int[numDims];
		increment = ones(numDims);
	}

	// ========================= METHODS ===================================

	/**
	 * Return an instance of the corresponding multi-dimensional iterator.
	 *
	 * @return a corresponding multi-dimensional iterator instance
	 */
	public MultiDimensionalIterator iterator()
	{
		return new MultiDimensionalIterator(lower, upper, increment);
	}
}
