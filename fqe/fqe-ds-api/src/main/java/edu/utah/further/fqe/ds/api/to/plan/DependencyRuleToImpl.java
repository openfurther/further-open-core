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
package edu.utah.further.fqe.ds.api.to.plan;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.plan.DependencyRule;
import edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule;

/**
 * A query plan dependency rule (an edge of the query plan graph) JAXB transfer object.
 * Specifies that a {@link ExecutionRule} depends on another {@link ExecutionRule}.
 * <p>
 * Only the execution IDs of both send rules are stored here; the parent
 * {@link QueryContext} of this object can be used to look up the send rule objects if
 * necessary.
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
 * @version Nov 18, 2010
 */
@Implementation
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "dependencyExecutionId", "outcomeExecutionId" })
@XmlRootElement(namespace = XmlNamespace.FQE, name = DependencyRuleToImpl.ENTITY_NAME)
public final class DependencyRuleToImpl implements DependencyRule
{
	// ========================= CONSTANTS =================================

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "dependencyRule";

	// ========================= FIELDS ====================================

	/**
	 * Edge head: must run this execution ID before {@link #outcomeExecutionId}.
	 */
	@XmlAttribute(name = "dependencyExecutionId", required = false)
	@Final
	private String dependencyExecutionId;

	/**
	 * Edge tail: must run this execution ID after {@link #dependencyExecutionId}.
	 */
	@XmlAttribute(name = "outcomeExecutionId", required = false)
	@Final
	private String outcomeExecutionId;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * No-argument constructor, required for a JAXB entity.
	 */
	public DependencyRuleToImpl()
	{
		super();
	}

	/**
	 * @param dependencyExecutionId
	 * @param outcomeExecutionId
	 */
	public DependencyRuleToImpl(final String dependencyExecutionId, final String outcomeExecutionId)
	{
		super();
		this.dependencyExecutionId = dependencyExecutionId;
		this.outcomeExecutionId = outcomeExecutionId;
	}

	/**
	 * Copy constructor.
	 *
	 * @param other
	 *            dependency rule to fields copy from
	 */
	DependencyRuleToImpl(final DependencyRule other)
	{
		super();
		this.dependencyExecutionId = other.getDependencyExecutionId();
		this.outcomeExecutionId = other.getOutcomeExecutionId();
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.appendSuper(super.toString())
				.append("dependencyExecutionId", getDependencyExecutionId())
				.append("outcomeExecutionId", getOutcomeExecutionId())
				.toString();
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.plan.DependencyRule#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(getDependencyExecutionId())
				.append(getOutcomeExecutionId())
				.toHashCode();
	}

	/**
	 * @param o
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (o == null)
			return false;
		if (o == this)
			return true;
		// ------------------------------------------------
		// Works only because this method is final!!!
		// ------------------------------------------------
		if (!ReflectionUtil.instanceOf(o, DependencyRule.class))
			return false;

		final DependencyRule that = (DependencyRule) o;
		return new EqualsBuilder()
				.append(this.getDependencyExecutionId(), that.getDependencyExecutionId())
				.append(this.getOutcomeExecutionId(), that.getOutcomeExecutionId())
				.isEquals();
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Result of comparing two edges. Edges are ordered by edge head ID, then by edge tail
	 * ID.
	 *
	 * @param that
	 *            the other {@link DependencyRule} object
	 * @return the result of comparison
	 */
	@Override
	public final int compareTo(final DependencyRule that)
	{
		return new CompareToBuilder()
				.append(this.getDependencyExecutionId(), that.getDependencyExecutionId())
				.append(this.getOutcomeExecutionId(), that.getOutcomeExecutionId())
				.toComparison();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.plan.DependencyRule#getDependencyExecutionId()
	 */
	@Override
	public String getDependencyExecutionId()
	{
		return dependencyExecutionId;
	}

	/**
	 * @param dependencyExecutionId
	 * @see edu.utah.further.fqe.ds.api.domain.plan.DependencyRule#setDependencyExecutionId(java.lang.Long)
	 */
	@Override
	public void setDependencyExecutionId(final String dependencyExecutionId)
	{
		this.dependencyExecutionId = dependencyExecutionId;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.plan.DependencyRule#getOutcomeExecutionId()
	 */
	@Override
	public String getOutcomeExecutionId()
	{
		return outcomeExecutionId;
	}

	/**
	 * @param outcomeExecutionId
	 * @see edu.utah.further.fqe.ds.api.domain.plan.DependencyRule#setOutcomeExecutionId(java.lang.Long)
	 */
	@Override
	public void setOutcomeExecutionId(final String outcomeExecutionId)
	{
		this.outcomeExecutionId = outcomeExecutionId;
	}
}
