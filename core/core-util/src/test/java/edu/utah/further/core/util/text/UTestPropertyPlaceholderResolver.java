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
package edu.utah.further.core.util.text;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.text.PlaceHolderResolver;

/**
 * Tests property place holder replacement in strings.
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
 * @version Apr 26, 2010
 */
public final class UTestPropertyPlaceholderResolver
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestPropertyPlaceholderResolver.class);

	/**
	 * Simple string to test.
	 */
	private static final String STRING_WITH_PLACEHOLDERS = "Hello http://${mdr.host}/${mdr.port}/aaa";

	// ========================= FIELDS ====================================

	/**
	 * Class to test.
	 */
	private final PlaceHolderResolver resolver = new PlaceHolderResolverImpl();

	// ========================= TESTING METHODS ===========================

	/**
	 * Test simple property placement.
	 */
	@Test
	public void resolveProperties()
	{
		final Properties placeHolders = newDefaultProperties();
		placeHolders.put("mdr.host", "host");
		assertCorrectResolutionForSimpleString(placeHolders, "Hello http://host/9000/aaa");
	}

	/**
	 * Test property placement recursively.
	 */
	@Test
	public void resolvePropertiesRecursive()
	{
		final Properties placeHolders = newDefaultProperties();
		placeHolders.put("mdr.host", "${mdr.port}/8000");
		assertCorrectResolutionForSimpleString(placeHolders,
				"Hello http://9000/8000/9000/aaa");
	}

	/**
	 * Can't replace cyclic references.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void resolveCyclicReferences()
	{
		final Properties placeHolders = newDefaultProperties();
		placeHolders.put("mdr.host", "${mdr.port}/8000");
		placeHolders.put("mdr.port", "${mdr.host}");
		assertCorrectResolutionForSimpleString(placeHolders, "WE SHOULDN'T BE HERE!!");
	}

	// ========================= PRIVATE METHODS ============================

	/**
	 * @param placeHolders
	 * @param expected
	 * @param original
	 */
	private void assertCorrectResolution(final Properties placeHolders,
			final String original, final String expected)
	{
		resolver.setDefaultPlaceHolders(placeHolders);
		assertEquals(expected, resolver.resolvePlaceholders(original));
	}

	/**
	 * @param placeHolders
	 * @param expected
	 */
	private void assertCorrectResolutionForSimpleString(final Properties placeHolders,
			final String expected)
	{
		assertCorrectResolution(placeHolders, STRING_WITH_PLACEHOLDERS, expected);
	}

	/**
	 * @return
	 */
	private Properties newDefaultProperties()
	{
		final Properties placeHolders = new Properties();
		placeHolders.put("mdr.port", "9000");
		return placeHolders;
	}
}
