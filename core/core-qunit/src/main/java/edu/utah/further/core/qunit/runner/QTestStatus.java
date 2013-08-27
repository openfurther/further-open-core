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
package edu.utah.further.core.qunit.runner;

import edu.utah.further.core.api.context.Named;

/**
 * Test status types.
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
 * @version Sep 2, 2010
 */
public enum QTestStatus implements Named
{
	// ========================= ENUMERATED CONSTANTS ======================

	OK(false, "OK"),

	FAIL(true, "failed");

	// ========================= FIELDS ====================================

	/**
	 * Does status type represent failure or not.
	 */
	private final boolean failed;

	/**
	 * This status type's name.
	 */
	private final String name;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param failed
	 * @param name
	 */
	private QTestStatus(final boolean failed, final String name)
	{
		this.failed = failed;
		this.name = name;
	}

	// ========================= IMPL: Named ================================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	// ========================= METHODS ===================================

	/**
	 * A factory method based on failure code.
	 *
	 * @param failed
	 *            failure code
	 * @return corresponding status type
	 */
	public static QTestStatus valueOf(final boolean failed)
	{
		return failed ? FAIL : OK;
	}

	/**
	 * Return the failed property.
	 *
	 * @return the failed
	 */
	public boolean isFailed()
	{
		return failed;
	}

}
