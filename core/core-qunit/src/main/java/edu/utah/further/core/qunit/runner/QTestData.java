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
package edu.utah.further.core.qunit.runner;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.CopyableFrom;

/**
 * A bean that holds the input and expected output of an XML transformation test.
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
 * @version May 13, 2010
 */
public final class QTestData implements CopyableFrom<QTestData, QTestData>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * MDR path or class-path resource path of the XML tranformation (e.g. XQuery) program
	 * resource to run.
	 */
	private String transformer;

	/**
	 * Actual XML document class-path resource path.
	 */
	private String input;

	/**
	 * Expected XML document class-path resource path.
	 */
	private String expected;

	/**
	 * If <code>true</code>, the test SpecialAction fails.
	 */
	private SpecialAction specialAction = SpecialAction.NONE;

	/**
	 * Externally-binded parameter names and values (potentially also parameter types in
	 * the future).
	 */
	private Map<String, String> parameters = CollectionUtil.newMap();

	/**
	 * If stream matching is turned on, these elements are ignored from the XML stream
	 * comparison.
	 */
	private Set<String> ignoredElements = CollectionUtil.newSet();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * JavaBean constructor. Initializer no fields.
	 */
	public QTestData()
	{
		super();
	}

	/**
	 * A copy-constructor.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public QTestData(final QTestData other)
	{
		this();
		copyFrom(other);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Return the hash code of this object. includes all fields.
	 *
	 * @return hash code of this object
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(transformer)
				.append(input)
				.append(expected)
				.append(parameters)
				.append(ignoredElements)
				.append(specialAction)
				.toHashCode();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;

		final QTestData that = (QTestData) obj;
		return new EqualsBuilder()
				.append(transformer, that.transformer)
				.append(input, that.input)
				.append(expected, that.expected)
				.append(parameters, that.parameters)
				.append(ignoredElements, that.ignoredElements)
				.append(specialAction, that.specialAction)
				.isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE);
		if (transformer != null)
		{
			builder.append("transformer", transformer);
		}
		builder.append("input", input);
		if (expected != null)
		{
			builder.append("expected", expected);
		}
		if (!ignoredElements.isEmpty())
		{
			builder.append("ignoredElements", ignoredElements);
		}
		if (specialAction != null)
		{
			builder.append("specialAction", specialAction);
		}
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public QTestData copyFrom(final QTestData other)
	{
		if (other == null)
		{
			return this;
		}

		// Copy primitive fields
		setSpecialAction(other.getSpecialAction());
		setExpected(other.getExpected());
		setInput(other.getInput());
		setTransformer(other.getTransformer());

		// Deep-copy collection fields
		setIgnoredElements(CollectionUtil.<String> newSet(other.getIgnoredElements()));
		setParameters(CollectionUtil.<String, String> newMap(other.getParameters()));

		return this;
	}

	// ========================= METHODS ===================================

	// ========================= GET & SET =================================

	/**
	 * Return the transformer property.
	 *
	 * @return the transformer
	 */
	public String getTransformer()
	{
		return transformer;
	}

	/**
	 * Set a new value for the transformer property.
	 *
	 * @param transformer
	 *            the transformer to set
	 */
	public void setTransformer(final String transformer)
	{
		this.transformer = transformer;
	}

	/**
	 * Return the input property.
	 *
	 * @return the input
	 */
	public String getInput()
	{
		return input;
	}

	/**
	 * Set a new value for the input property.
	 *
	 * @param input
	 *            the input to set
	 */
	public void setInput(final String input)
	{
		this.input = input;
	}

	/**
	 * Return the expected property.
	 *
	 * @return the expected
	 */
	public String getExpected()
	{
		return expected;
	}

	/**
	 * Set a new value for the expected property.
	 *
	 * @param expected
	 *            the expected to set
	 */
	public void setExpected(final String expected)
	{
		this.expected = expected;
	}

	/**
	 * Return the parameters property.
	 *
	 * @return the parameters
	 */
	public Map<String, String> getParameters()
	{
		return parameters;
	}

	/**
	 * Set a new value for the parameters property.
	 *
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(final Map<String, String> parameters)
	{
		this.parameters = parameters;
	}

	/**
	 * Return the ignoredElements property.
	 *
	 * @return the ignoredElements
	 */
	public Set<String> getIgnoredElements()
	{
		return ignoredElements;
	}

	/**
	 * Set a new value for the ignoredElements property.
	 *
	 * @param ignoredElements
	 *            the ignoredElements to set
	 */
	public void setIgnoredElements(final Set<String> ignoredElements)
	{
		this.ignoredElements = ignoredElements;
	}

	/**
	 * Return the specialAction property.
	 *
	 * @return the specialAction
	 */
	public SpecialAction getSpecialAction()
	{
		return specialAction;
	}

	/**
	 * Set a new value for the specialAction property.
	 *
	 * @param specialAction
	 *            the specialAction to set
	 */
	public void setSpecialAction(final SpecialAction specialAction)
	{
		this.specialAction = (specialAction == null) ? SpecialAction.NONE : specialAction;
	}

}
