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

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import edu.utah.further.core.api.chain.AttributeContainer;
import edu.utah.further.core.api.chain.AttributeContainerImpl;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.util.io.IoUtil;

/**
 * Custom {@link java.beans.PropertyEditor} for {@link AttributeContainer} objects.
 * <p>
 * <p>
 * Works similarly to {@link PropertiesEditor}: it handles conversion from content
 * {@link String} to <code>Properties</code> object. Also handles {@link Map} to
 * <code>Properties</code> conversion, for populating a <code>Properties</code> object via
 * XML "map" entries.
 * <p>
 * The required format is defined in the standard <code>Properties</code> documentation.
 * Each property must be on a new line.
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
public final class AttributeContainerEditor extends PropertyEditorSupport
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Context resource locator.
	 */
	private final ResourceLoader resourceLoader;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a new instance of the {@link ResourceEditor} class using a
	 * {@link DefaultResourceLoader}.
	 */
	public AttributeContainerEditor()
	{
		this(new DefaultResourceLoader());
	}

	/**
	 * Create a new instance of the {@link ResourceEditor} class using the given
	 * {@link ResourceLoader}.
	 *
	 * @param resourceLoader
	 *            the <code>ResourceLoader</code> to use
	 */
	public AttributeContainerEditor(final ResourceLoader resourceLoader)
	{
		Assert.notNull(resourceLoader, "ResourceLoader must not be null");
		this.resourceLoader = resourceLoader;
	}

	// ========================= IMPL: PropertyEditorSupport ===============

	/**
	 * Convert {@link String} into {@link AttributeContainer}, considering it as
	 * properties content.
	 *
	 * @param text
	 *            the text to be so converted
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@Override
	public void setAsText(final String text) throws IllegalArgumentException
	{
		final AttributeContainer attributeContainer = newAttributeContainer();
		if (StringUtils.hasText(text))
		{
			final String fileText = getResourceAsText(text);

			final PropertyEditor propertiesEditor = new PropertiesEditor();
			propertiesEditor.setAsText(fileText);
			final Properties props = (Properties) propertiesEditor.getValue();
			attributeContainer.setAttributes(CollectionUtil.asMap(props));
		}
		setValue(attributeContainer);
	}

	/**
	 * Take {@link AttributeContainer} as-is; convert {@link Map} into
	 * <code>Properties</code>.
	 *
	 * @see java.beans.PropertyEditorSupport#setValue(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(final Object value)
	{
		if (ReflectionUtil.instanceOfOneOf(value, Map.class, Properties.class))
		{
			final AttributeContainer attributeContainer = newAttributeContainer();
			attributeContainer.setAttributes((Map<String, Object>) value);
			super.setValue(attributeContainer);
		}
		else
		{
			super.setValue(value);
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Resolve the given path, replacing placeholders with corresponding system property
	 * values if necessary.
	 *
	 * @param path
	 *            the original file path
	 * @return the resolved file path
	 * @see org.springframework.util.SystemPropertyUtils#resolvePlaceholders
	 */
	private static String resolvePath(final String path)
	{
		return SystemPropertyUtils.resolvePlaceholders(path);
	}

	/**
	 * A factory method of empty {@link AttributeContainer} objects.
	 *
	 * @return empty {@link AttributeContainer} instance
	 */
	private static AttributeContainerImpl newAttributeContainer()
	{
		return new AttributeContainerImpl();
	}

	/**
	 * @param location
	 * @return
	 */
	private String getResourceAsText(final String location)
	{
		final String locationToUse = resolvePath(location).trim();
		final Resource resource = this.resourceLoader.getResource(locationToUse);
		try
		{
			return IoUtil.getInputStreamAsString(resource.getInputStream());
		}
		catch (final IOException e)
		{
			throw new IllegalArgumentException("Resource " + resource + " not found");
		}
	}
}
