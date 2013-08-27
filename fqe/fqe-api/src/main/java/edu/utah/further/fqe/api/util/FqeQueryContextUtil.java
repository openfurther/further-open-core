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
package edu.utah.further.fqe.api.util;

import java.util.List;

import org.apache.commons.lang.Validate;

import edu.utah.further.core.api.context.Utility;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.fqe.ds.api.util.FqeNames;

/**
 * A stateless test utility class related to {@link QueryContext}s that does not require
 * JUnit.
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
 * @version Nov 3, 2010
 */
@Utility
public final class FqeQueryContextUtil
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	// ========================= METHODS ===================================

	/**
	 * Returns the masked safe result, that is if results are less than mask but greater
	 * than zero, a masked value is returned.
	 * 
	 * @param qc
	 * @param mask
	 * @return
	 */
	public static String getResultMaskSafe(final QueryContextTo qc, final int mask)
	{
		if (qc.getNumRecords() == ResultContext.ACCESS_DENIED)
		{
			return "Unauthorized";
		}
		final boolean masked = qc.getHasChildren() ? isAnyChildMasked(qc, mask)
				: isChildMasked(qc, mask);
		return masked ? FqeNames.MASK_STRING : new Long(qc.getNumRecords()).toString();
	}

	/**
	 * Returns <code>true</code> if the number of records is greater than zero but less
	 * than or equal to mask, otherwise false.
	 * 
	 * @param numRecords
	 *            # records under consideration
	 * @param mask
	 *            the maximum value for which results must be otherwise they will be
	 *            masked.
	 * @param numRecords
	 * @return true if and only if <code>numRecords</code> should be masked
	 */
	public static boolean shouldBeMasked(final long numRecords, final int mask)
	{
		return (numRecords > 0 && numRecords <= mask);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Returns true if any of the children {@link QueryContext}'s are masked, otherwise
	 * false.
	 * 
	 * @param qc
	 *            the Parent {@link QueryContext}
	 * @param mask
	 *            the maximum value for which results must be otherwise they will be
	 *            masked.
	 */
	private static boolean isAnyChildMasked(final QueryContextTo qc, final int mask)
	{
		final List<QueryContextTo> children = qc.getChildren();
		for (final QueryContextTo child : children)
		{
			if (isChildMasked(child, mask))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns <code>true</code> if the number of records is greater than zero but less
	 * than or equal to mask, otherwise false.
	 * 
	 * @param qc
	 *            a child {@link QueryContext}
	 * @param mask
	 *            the maximum value for which results must be otherwise they will be
	 *            masked.
	 */
	private static boolean isChildMasked(final QueryContextTo qc, final int mask)
	{
		// Ensure we have a child
		Validate.isTrue(!qc.getHasChildren());

		// We are a child, mask results that are <= mask
		final long numRecords = qc.getNumRecords();
		return shouldBeMasked(numRecords, mask);
	}
}
