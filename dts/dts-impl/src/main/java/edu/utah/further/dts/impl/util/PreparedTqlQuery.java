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
package edu.utah.further.dts.impl.util;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;

import java.util.List;

import edu.utah.further.core.api.text.TextTemplate;

/**
 * Centralizes pre-fabricated TQL queries for use by DTS services.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
public enum PreparedTqlQuery
{
	// ========================= ENUMERATED CONSTANTS ======================

	FIND_MAPPED_DESCANDANTS("from [\"%NAMESPACE%\"]"
			+ " with INV \"ExactMatch[Further]\" EXISTS AND ANCESTOR EQUALS "
			+ "\"%CONCEPT_NAME%\"" + ";", "NAMESPACE", "CONCEPT_NAME")
	{
	},

	TRANSLATE_CHILDREN(
			"from [\"%SOURCE_NAMESPACE%\"]"
					+ " with INV \"ExactMatch[Further]\" EXISTS AND ANCESTOR EQUALS "
					+ "\"%CONCEPT_NAME%\""
					+ " export INV \"ExactMatch[Further]\" -> Concept_Name, INV \"ExactMatch[Further]\" -> \"%TARGET_PROPERTY_NAME%[%TARGET_NAMESPACE%]\""
					+ ";", "SOURCE_NAMESPACE", "CONCEPT_NAME", "TARGET_NAMESPACE",
			"TARGET_PROPERTY_NAME")
	{
	};

	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * The parametric TQL query string.
	 */
	private final TextTemplate tqlQuery;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a parametric TQL object.
	 *
	 * @param template
	 *            parametric TQL query string
	 * @param parameters
	 *            list of replacement parameters
	 * @see {@link TextTemplate}
	 */
	private PreparedTqlQuery(final String tqlQuery, final String... parameters)
	{
		this.tqlQuery = new TextTemplate(tqlQuery, newList(parameters));
	}

	// ========================= METHODS ===================================

	/**
	 * Return the parametric TQL query.
	 *
	 * @return the tqlQuery property
	 */
	public TextTemplate getTqlQuery()
	{
		return tqlQuery;
	}

	/**
	 * Evaluate the TQL template.
	 *
	 * @param values
	 *            parameter value list. Must match the parameter list size and order (i.e.
	 *            the first <code>values</code> element corresponds to the first
	 *            parameter, etc.)
	 * @return evaluated TQL string
	 */
	public String evaluate(final List<String> values)
	{
		return tqlQuery.evaluate(values);
	}

	/**
	 * Evaluate the TQL template.
	 *
	 * @param values
	 *            parameter value array. Must match the parameter list size and order
	 *            (i.e. the first <code>values</code> element corresponds to the first
	 *            parameter, etc.)
	 * @return evaluated TQL string
	 */
	public String evaluate(final String... values)
	{
		return tqlQuery.evaluate(newList(values));
	}
}
