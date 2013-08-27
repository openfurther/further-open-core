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

import java.util.Iterator;
import java.util.List;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * A bean that holds a list of XQuery test beans.
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
 * @version May 13, 2010
 */
public final class QTestSuite implements Iterable<QTestData>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * List of tester objects.
	 */
	private List<QTestData> testers = CollectionUtil.newList();

	/**
	 * Holds global parameters of the entire test suite.
	 */
	private QTestContext context = new QTestContext();

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPL: Iterable ============================

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<QTestData> iterator()
	{
		return testers.iterator();
	}

	// ========================= GET & SET =================================

	/**
	 * Return the number of tests in the suite.
	 *
	 * @return test suite size
	 * @see java.util.List#size()
	 */
	public int getSize()
	{
		return testers.size();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public QTestData get(final int index)
	{
		return testers.get(index);
	}

	/**
	 * Set a new value for the testers property.
	 *
	 * @param testers
	 *            the testers to set
	 */
	public void setTesters(final List<? extends QTestData> testers)
	{
		CollectionUtil.setListElements(this.testers, testers);
	}

	/**
	 * Return the context property.
	 *
	 * @return the context
	 */
	public QTestContext getContext()
	{
		return context;
	}

	/**
	 * Set a new value for the context property.
	 *
	 * @param context the context to set
	 */
	public void setContext(final QTestContext context)
	{
		this.context = context;
	}

	// ========================= METHODS ===================================
}
