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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

/**
 * Collection utilities unit tests.
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
 * @version Sep 26, 2010
 */
public final class UTestCollectionUtil
{
	// ========================= CONSTANTS =================================

	// ========================= TESTING METHODS ===========================

	/**
	 * Test converting an element paraeter array to a list.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void toSet()
	{
		final Set<Integer> set = CollectionUtil.newSet();
		set.add(1);
		set.add(2);
		set.add(3);
		assertThat(CollectionUtil.toSet(1, 2, 3), is(set));
	}

	/**
	 * Test collocating a list to a unique element set.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void getUniqueElements()
	{
		assertThat(CollectionUtil.getUniqueElements(Arrays.<Integer> asList(1, 1, 2, 1,
				3, 3, 2)), is(CollectionUtil.toSet(1, 2, 3)));
	}
}
