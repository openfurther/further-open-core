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

import java.beans.PropertyEditorSupport;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.PropertiesEditor;

import edu.utah.further.core.api.chain.AttributeContainer;
import edu.utah.further.core.util.context.EnumAliasService;

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
public final class LabeledContainerEditor extends PropertyEditorSupport
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * Provides access to the enum alias map member.
	 */
	@Autowired
	private EnumAliasService enumAliasService;

	// ========================= CONSTRUCTORS ==============================

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
		super.setAsText(text); // remove when done

		// TODO: complete this method
		//
		// final Labeled attributeContainer = newAttributeContainer();
		// if (StringUtils.hasText(text))
		// {
		// enumAliasService.getEnumAliases().
		//
		// final String fileText = getResourceAsText(text);
		//
		// final PropertyEditor propertiesEditor = new PropertiesEditor();
		// propertiesEditor.setAsText(fileText);
		// final Properties props = (Properties) propertiesEditor.getValue();
		// attributeContainer.setAttributes(CollectionUtil.asMap(props));
		// }
		// setValue(attributeContainer);
	}

	// ========================= PRIVATE METHODS ===========================

}
