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

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import edu.utah.further.core.api.constant.Strings;

/**
 * Spring custom namespace handler for the "core" schema.
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
 * @version Oct 12, 2009
 * @see http://static.springsource.org/spring/docs/2.5.6/reference/extensible-xml.html
 */
public final class CoreNamespaceHandler extends NamespaceHandlerSupport
{
	// ========================= CONSTANTS =================================

	/**
	 * Suffix of BDP class names dealing with boxed types.
	 */
	private static final String BOXED_BEAN_DEFINITION_PARSER_SUFFIX = "BeanDefinitionParser";

	// ========================= IMPLEMENTATION: NamespaceHandlerSupport ===

	/**
	 * Register bean definition parsers for each top-level element in the schema.
	 *
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
	@Override
	public void init()
	{
		// Register parsers here, each of which handles an individual top-level element
		// in the schema.

		registerBeanDefinitionParser(CoreSchemaConstants.ELEMENT_COMPONENT,
				new ComponentBeanDefinitionParser());
		registerBeanDefinitionParser(CoreSchemaConstants.ELEMENT_PORTABLE_FIXTURE,
				new PFixtureManagerBeanDefinitionParser());

		// Primitive-boxed creation elements. Use reflection to attach the correct
		// parsers by naming conventions of both the elements and parser classes
		final List<Class<?>> boxedElementTypes = Arrays.<Class<?>> asList(Boolean.class,
				Integer.class, Long.class, Float.class, Double.class, String.class,
				Class.class);
		registerBoxedTypeElementParsers(boxedElementTypes);

		// Utility type creation elements
		registerUtilityTypeElementParsers();

		// AOP elements
		registerAopElementParsers();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Register bean definition parsers for supported boxed-primitive creation elements.
	 * Use reflection to attach the correct parsers by naming conventions of both the
	 * elements and parser classes
	 *
	 * @param boxedElementTypes
	 *            list of supported boxed-primitive types
	 */
	private void registerBoxedTypeElementParsers(final List<Class<?>> boxedElementTypes)
	{
		for (final Class<?> elementType : boxedElementTypes)
		{
			final String typeName = elementType.getSimpleName();
			final String elementName = typeName.toLowerCase();
			final Class<? extends BeanDefinitionParser> parserClass = loadParserClass(
					elementName, typeName + BOXED_BEAN_DEFINITION_PARSER_SUFFIX);
			final BeanDefinitionParser parserInstance = getParserInstance(parserClass);
			registerBeanDefinitionParser(elementName, parserInstance);
		}
	}

	/**
	 * Load a bean definition parser class by naming convention.
	 *
	 * @param elementName
	 * @param parserClassName
	 * @param parserClass
	 * @return
	 */
	private Class<? extends BeanDefinitionParser> loadParserClass(
			final String elementName, final String parserClassName)
	{
		try
		{
			final String parserPackageName = getClass().getPackage().getName();
			return (Class<? extends BeanDefinitionParser>) getClass()
					.getClassLoader()
					.loadClass(
							parserPackageName + Strings.PROPERTY_SCOPE_CHAR
									+ parserClassName);
		}
		catch (final ClassNotFoundException e)
		{
			throw new BeanDefinitionStoreException(
					"Could not find bean definition parser class " + parserClassName
							+ " for schema element " + elementName);
		}
		catch (final ClassCastException e)
		{
			throw new BeanDefinitionStoreException("Bean definition parser class "
					+ parserClassName + " does not implement "
					+ BeanDefinitionParser.class);
		}
	}

	/**
	 * Create an instance of a bean definition parser class.
	 *
	 * @param parserClass
	 * @return bean definition parser instance
	 */
	private BeanDefinitionParser getParserInstance(
			final Class<? extends BeanDefinitionParser> parserClass)
	{
		try
		{
			return parserClass.newInstance();
		}
		catch (final InstantiationException e)
		{
			throw new BeanDefinitionStoreException(
					"Could not find instantiate default constructor of bean definition parser class "
							+ parserClass);
		}
		catch (final IllegalAccessException e)
		{
			throw new BeanDefinitionStoreException(
					"Default constructor of bean definition parser class " + parserClass
							+ " is not publicly accessible");
		}
	}

	/**
	 * Register bean definition parsers for supported utility type creation elements. Use
	 * reflection to attach the correct parsers by naming conventions of both the elements
	 * and parser classes.
	 */
	private void registerUtilityTypeElementParsers()
	{
		registerBeanDefinitionParser(CoreSchemaConstants.ELEMENT_DATE_FORMAT,
				new SimpleDateFormatBeanDefinitionParser());
	}

	/**
	 * Register parser of AOP-related elements.
	 */
	private void registerAopElementParsers()
	{
		registerBeanDefinitionParser(CoreSchemaConstants.ELEMENT_ASPECTJ_AUTOPROXY,
				new SelectiveAspectJAutoProxyCreatorBeanDefinitionParser());
	}
}