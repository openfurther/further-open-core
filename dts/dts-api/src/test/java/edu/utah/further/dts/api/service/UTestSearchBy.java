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
package edu.utah.further.dts.api.service;

import edu.utah.further.dts.api.util.SearchBy;

import static org.junit.Assert.*;

import org.junit.Test;

public class UTestSearchBy
{

	@Test
	public void testPadStartsWith()
	{
		assertEquals("test*", SearchBy.STARTS_WITH.pad("test"));
	}

	@Test
	public void testPadContains()
	{
		assertEquals("*test*", SearchBy.CONTAINS.pad("test"));
	}

	@Test
	public void testPadEndsWith()
	{
		assertEquals("*test", SearchBy.ENDS_WITH.pad("test"));
	}

}
