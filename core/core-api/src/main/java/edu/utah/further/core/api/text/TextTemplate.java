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
package edu.utah.further.core.api.text;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import edu.utah.further.core.api.context.Api;

/**
 * An immutable template string that supports replacements of parameters by values.
 * Parameters are denoted by <code>%parameterName%</code> in the template.
 * <p>
 * Example of a template with the parameters <code>USER</code> and <code>PASSWORD</code>:
 * <code>username=%USER%&password=%PASSWORD%</code>
 * <p>
 * Note: this class does not validate the template's format.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 19, 2008
 */
@Api
public final class TextTemplate
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * The template string containing place-holders.
	 */
	private final String text;

	/**
	 * A set of parameters.
	 */
	private final List<String> parameters = newList();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a text template.
	 *
	 * @param template
	 *            template
	 * @param parameters
	 *            list of replacement parameters
	 */
	public TextTemplate(final String template, final List<String> parameters)
	{
		super();
		this.text = template;
		for (final String param : parameters)
		{
			this.parameters.add("%" + param + "%");
		}
	}

	// ========================= METHODS ===================================

	/**
	 * Evaluate a template.
	 *
	 * @param values
	 *            parameter value list. Must match the parameter list size and order (i.e.
	 *            the first <code>values</code> element corresponds to the first
	 *            parameter, etc.)
	 * @return evaluated template
	 */
	public String evaluate(final List<String> values)
	{
		final int size = parameters.size();
		Validate.isTrue(values.size() == size,
				"Value list size must equal the parameter list size. parameters "
						+ parameters + " values " + values);

		// Replace arguments by values
		String evaluated = text;
		for (int i = 0; i < size; i++)
		{
			evaluated = StringUtils.replace(evaluated, parameters.get(i), values.get(i));
		}
		return evaluated;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the text template.
	 *
	 * @return the text template
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Return the parameters property.
	 *
	 * @return the parameters
	 */
	public List<String> getParameters()
	{
		return Collections.unmodifiableList(parameters);
	}

}
