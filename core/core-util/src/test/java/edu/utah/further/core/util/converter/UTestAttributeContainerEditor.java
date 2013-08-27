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
package edu.utah.further.core.util.converter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.chain.AttributeContainer;

/**
 * A unit test of {@link AttributeContainer}'s property editor.
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
 * @version Aug 18, 2010
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/core-util-test-property-editors-context.xml" }, inheritLocations = false)
public final class UTestAttributeContainerEditor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestAttributeContainerEditor.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Client containing {@link AttributeContainer}-type bean dependencies.
	 */
	@Autowired
	private AttributeContainerClient client;

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	// ========================= METHODS ===================================

	/**
	 * Verify various {@link AttributeContainer} conversions + DI into a client bean.
	 *
	 * @throws Exception
	 */
	@Test
	@SuppressWarnings("boxing")
	public void attributeContainerConversions() throws Exception
	{
		final Map<String, Object> attributes = client.getMainContainer().getAttributes();
		if (log.isDebugEnabled())
		{
			log.debug("attributes " + attributes);
		}
		assertThat(client.getContainers().size(), is(3));
		assertThat(attributes.size(), is(4));
		assertAttributeEquals(attributes, "test1", "value1");
		assertAttributeEquals(attributes, "test2", "value2");
		assertAttributeEquals(attributes, "test3", "value3");
		assertAttributeEquals(attributes, "intValue", 123);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param attributes
	 * @param key
	 * @param expectedValue
	 */
	private static void assertAttributeEquals(final Map<String, Object> attributes,
			final String key, final Object expectedValue)
	{
		assertEquals(attributes.get(key), expectedValue);
	}
}
