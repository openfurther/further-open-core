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
package edu.utah.further.core.util.schema;

import static edu.utah.further.core.util.context.SpringUtil.getBean;
import static edu.utah.further.core.util.context.SpringUtil.getFirstBeanOfType;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.MavenPhase;
import edu.utah.further.core.util.aop.SelectiveAspectJAutoProxyCreator;
import edu.utah.further.core.util.context.PFixtureManager;
import edu.utah.further.core.util.context.SpringUtil;

/**
 * Test our custom Spring schema "core".
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne <code>&lt;oren.livne@utah.edu&gt;</code>
 * @version Feb 13, 2009
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/core-util-test-schema-context.xml" })
public final class UTestSchemaCore
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestSchemaCore.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Spring application context.
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Client of enum attributes.
	 */
	@Resource(name = "simpleAttributeClient")
	private SimpleAttributeClient attributeClient;

	/**
	 * Client of enum attributes.
	 */
	@Resource(name = "simpleAttributeClient2")
	private SimpleAttributeClient attributeClient2;

	// ========================= FIELDS ====================================

	// ========================= SETUP METHODS =============================

	// ========================= METHODS ===================================

	/**
	 * Test the <code>boolean</code> schema element as a top-level element in a Spring
	 * context file.
	 *
	 * @throws Exception
	 */
	@Test
	public void booleanTopLevelElement() throws Exception
	{
		testTopLevelElement(Boolean.class);
	}

	/**
	 * Test the <code>int</code> schema element as a top-level element in a Spring context
	 * file.
	 *
	 * @throws Exception
	 */
	@Test
	public void intTopLevelElement() throws Exception
	{
		testTopLevelElement(Integer.class);
	}

	/**
	 * Test the <code>float</code> schema element as a top-level element in a Spring
	 * context file.
	 *
	 * @throws Exception
	 */
	@Test
	public void floatTopLevelElement() throws Exception
	{
		testTopLevelElement(Float.class);
	}

	/**
	 * Test the <code>double</code> schema element as a top-level element in a Spring
	 * context file.
	 *
	 * @throws Exception
	 */
	@Test
	public void doubleTopLevelElement() throws Exception
	{
		testTopLevelElement(Double.class);
	}

	/**
	 * Test the <code>string</code> schema element as a top-level element in a Spring
	 * context file.
	 *
	 * @throws Exception
	 */
	@Test
	public void stringTopLevelElement() throws Exception
	{
		testTopLevelElement(String.class);
	}

	/**
	 * Test the <code>class</code> schema element as a top-level element in a Spring
	 * context file.
	 *
	 * @throws Exception
	 */
	@Test
	public void classTopLevelElement() throws Exception
	{
		final String elementTypeName = "topLevelClass";
		// we happen to know that the class bean is a string class
		final Class<String> topLevelBean = SpringUtil.getBean(applicationContext,
				elementTypeName, Class.class);
		assertNotNull("Top level schema element " + elementTypeName + " was not created",
				topLevelBean);
		assertEquals("Top level string element value was not properly set", String.class,
				topLevelBean);
	}

	/**
	 * Test the <code>dateformat</code> schema element as a top-level element in a Spring
	 * context file.
	 *
	 * @throws Exception
	 */
	@Test
	public void dateFormatTopLevelElement() throws Exception
	{
		final SimpleDateFormat topLevelDateFormat = getBean(applicationContext,
				"topLevelDateFormat", SimpleDateFormat.class);
		assertNotNull("Top level date format element was not created", topLevelDateFormat);
	}

	/**
	 * Test that the <code>aspect</code> schema element's created bean is the same object
	 * as a child of <code>aspects</code> top-level bean.
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("boxing")
	@Test
	public void springComponentExample() throws Exception
	{
		final Component parentComponent = getFirstBeanOfType(applicationContext,
				Component.class);
		final String elementName = "component";
		assertNotNull("Top level schema element " + elementName + " was not created"
				+ parentComponent);
		if (log.isDebugEnabled())
		{
			log.debug("parentComponent " + parentComponent);
		}
		assertThat("Nested beans were not properly set on the top level bean",
				parentComponent.getComponents().size(), greaterThanOrEqualTo(1));
		assertEquals(
				"Nested bean types were not properly set on the top level bean. This should anyway be caught at compile time using generics safety",
				parentComponent.getComponents().get(0).getClass(), Component.class);
	}

	/**
	 * Test constructing a portable fixture manager from the corresponding Spring XML
	 * element.
	 *
	 * @throws Exception
	 */
	@Test
	public void pfixtureManagerCustomMavenPhase() throws Exception
	{
		final PFixtureManager pfixtureManager = getBean(applicationContext,
				"pfixtureManager", PFixtureManager.class);
		assertNotNull("Portable fixture element bean was not created", pfixtureManager);
		assertEquals("Maven phase attribute was not properly wired",
				MavenPhase.INTEGRATION_TEST, pfixtureManager.getMavenPhase());
		assertThat(pfixtureManager.getContextLocations(),
				is(Arrays.asList("/log4j.properties", "location2")));
	}

	/**
	 * Test constructing a portable fixture manager from the corresponding Spring XML
	 * element.
	 *
	 * @throws Exception
	 */
	@Test
	public void pfixtureManagerDefaultMavenPhase() throws Exception
	{
		final PFixtureManager pfixtureManager = getBean(applicationContext,
				"pfixtureManagerDefaultPhase", PFixtureManager.class);
		assertNotNull("Portable fixture bean was not created", pfixtureManager);
		assertEquals("Maven phase attribute was not properly wired", MavenPhase.TEST,
				pfixtureManager.getMavenPhase());
		assertThat(pfixtureManager.getContextLocations(),
				is(Arrays.asList("/log4j.properties", "location3")));
	}

	/**
	 * Test constructing an AspectJ auto-proxy creator bean.
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void aspectjAutoproxy() throws Exception
	{
		final SelectiveAspectJAutoProxyCreator creator = getBean(applicationContext,
				getSelectiveBeanId(), SelectiveAspectJAutoProxyCreator.class);
		assertNotNull("AspectJ auto-proxy creator bean was not created", creator);

		final List<String> includePatterns = toString((List<Pattern>) getPrivateFieldValue(
				creator, AnnotationAwareAspectJAutoProxyCreator.class, "includePatterns"));
		assertThat("Include pattern field was not correctly set", includePatterns,
				is(Arrays.asList("abcd", "abcd2")));

		final List<String> includeBeanPatterns = toString((List<Pattern>) getPrivateFieldValue(
				creator, SelectiveAspectJAutoProxyCreator.class, "includeBeanPatterns"));
		assertThat("Include bean pattern field was not correctly set",
				includeBeanPatterns, is(Arrays.asList("abcd3", "abcd4")));
	}

	/**
	 * Test DI-ing an enum-type property using Spring.
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("boxing")
	@Test
	public void enumPropertySet()
	{
		// Using a bean <property> tag works
		assertThat(attributeClient.getSourceAttr(), is(SimpleAttributeName.CONST_A));
		assertThat(attributeClient.getStringAttr(), is("abc"));
		assertThat(attributeClient.getIntAttr(), is(123));

		// Using a p-namespace attribute doesn't work
		assertThat(attributeClient2.getSourceAttr(), is(SimpleAttributeName.CONST_B));
		assertThat(attributeClient2.getStringAttr(), is("def"));
		assertThat(attributeClient2.getIntAttr(), is(456));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private static String getSelectiveBeanId()
	{
		return new SelectiveAspectJAutoProxyCreatorBeanDefinitionParser()
				.getGeneratedId();
	}

	/**
	 * Tests all top level elements that convert a string to a custom type.
	 *
	 * @param <T>
	 * @param elementType
	 * @throws Exception
	 */
	private <T> void testTopLevelElement(final Class<T> elementType) throws Exception
	{
		final String elementTypeName = elementType.getSimpleName();
		final String elementName = elementTypeName.toLowerCase();
		final String beanName = "topLevel" + elementTypeName;
		final String expectedValuePropertyName = elementName + ".value";
		final T topLevelBean = SpringUtil.getBean(applicationContext, beanName,
				elementType);
		assertNotNull("Top level schema element " + elementName + " was not created",
				topLevelBean);
		final T expected = elementType.getConstructor(String.class).newInstance(
				getContextProperties().getProperty(expectedValuePropertyName));
		assertEquals("Top level string element value was not properly set", expected,
				topLevelBean);
	}

	/**
	 * @return
	 */
	private Properties getContextProperties()
	{
		return SpringUtil.getBean(applicationContext, "contextProperties",
				Properties.class);
	}

	/**
	 * @param creator
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private static <T> T getPrivateFieldValue(
			final SelectiveAspectJAutoProxyCreator creator, final Class<?> clazz,
			final String fieldName) throws NoSuchFieldException, IllegalAccessException
	{
		final Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		final T fieldValue = (T) field.get(creator);
		field.setAccessible(false);
		return fieldValue;
	}

	/**
	 * @param patterns
	 * @return
	 */
	private static List<String> toString(final Collection<Pattern> patterns)
	{
		final List<String> patternsAsStrings = CollectionUtil.newList();
		for (final Pattern pattern : patterns)
		{
			patternsAsStrings.add(pattern.pattern());
		}
		return patternsAsStrings;
	}
}
