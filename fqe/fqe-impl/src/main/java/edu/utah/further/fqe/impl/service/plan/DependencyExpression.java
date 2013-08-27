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
package edu.utah.further.fqe.impl.service.plan;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.regex.RegExUtil;
import edu.utah.further.core.query.domain.SearchNames;

/**
 * A search query expression that describes a quantity to be computed by a previous query.
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
 * @version Dec 23, 2010
 */
public final class DependencyExpression
{
	// ========================= CONSTANTS =================================

	/**
	 * A regular expression for query dependency expressions. Example: "QUERY[1].id".
	 */
	private static final String DEPENDENCY_EXPRESSION = SearchNames.QUERY
			+ "\\[(\\d+)\\]\\.(.*)";

	// ========================= FIELDS ====================================

	/**
	 * Query whose result set is the realm of this expression.
	 */
	private final Long qid;

	/**
	 * The expression represents this result set property.
	 */
	private final String property;

	// /**
	// * A handle to the value, which can be a large list.
	// */
	// private Pager<?> pager;
	//
	// /**
	// * The size of the value of this expression.
	// */
	// private int size;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param dependencyExpression
	 */
	public DependencyExpression(final String dependencyExpression)
	{
		final List<String> captureGroups = RegExUtil.capture(dependencyExpression,
				DEPENDENCY_EXPRESSION);
		this.qid = new Long(captureGroups.get(0));
		this.property = captureGroups.get(1);
	}

	// ========================= METHODS ===================================

	/**
	 * @param parameter
	 * @return
	 */
	public static boolean matches(final Object parameter)
	{
		return ReflectionUtil.instanceOf(parameter, String.class)
				&& ((String) parameter).matches(DEPENDENCY_EXPRESSION);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("qid", qid)
				.append("property", property)
				.toString();
	}

	// ========================= GET & SET =================================

	// /**
	// * Return the size of the value of this expression.
	// *
	// * @return value size
	// */
	// public int getSize()
	// {
	// return size;
	// }
	//
	// /**
	// * Set a new value for the size property.
	// *
	// * @param size
	// * the size to set
	// */
	// public void setSize(final int size)
	// {
	// this.size = size;
	// }
	//
	// /**
	// * Return the pager property.
	// *
	// * @return the pager
	// */
	// public Pager<?> getPager()
	// {
	// return pager;
	// }
	//
	// /**
	// * Set a new value for the pager property.
	// *
	// * @param pager
	// * the pager to set
	// */
	// public void setPager(final Pager<?> pager)
	// {
	// this.pager = pager;
	// }
}
