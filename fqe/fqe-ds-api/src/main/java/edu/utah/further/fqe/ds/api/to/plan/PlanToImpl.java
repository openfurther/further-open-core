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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.plan.DependencyRule;
import edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule;
import edu.utah.further.fqe.ds.api.domain.plan.Plan;

/**
 * A {@link QueryContext} execution plan - JAXB transfer object. It is represented as a
 * graph whose nodes are {@link ExecutionRule}s (each representing a single
 * {@link SearchQuery} execution, potentially directed at a single data source) and
 * {@link DependencyRule} edges, which determine execution dependencies.
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
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = XmlNamespace.FQE, name = PlanToImpl.ENTITY_NAME)
@XmlType(name = "", propOrder =
{ "executionRules", "dependencyRules" })
public final class PlanToImpl implements Plan
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(PlanToImpl.class);

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "plan";

	// ========================= FIELDS ====================================

	/**
	 * List of executions = query send rules.
	 */
	@XmlElementRef(name = "executionRule", namespace = XmlNamespace.FQE, type = ExecutionRuleToImpl.class)
	private final List<ExecutionRule> executionRules = CollectionUtil.newList();

	/**
	 * List of execution dependencies.
	 */
	@XmlElementRef(name = "dependencyRule", namespace = XmlNamespace.FQE, type = DependencyRuleToImpl.class)
	private final List<DependencyRule> dependencyRules = CollectionUtil.newList();

	// ========================= IMPL: Plan ================================

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.plan.Plan#getType()
	 */
	@Override
	public Type getType()
	{
		if (executionRules.isEmpty())
		{
			return Type.BROADCAST;
		}
		return Type.PHASED;
	}

	/**
	 * @param executionRule
	 * @see edu.utah.further.fqe.ds.api.domain.plan.Plan#addExecutionRule(edu.utah.further.fqe.ds.api.domain.plan.ExecutionRule)
	 */
	@Override
	public void addExecutionRule(final ExecutionRule executionRule)
	{
		executionRules.add(new ExecutionRuleToImpl(executionRule));
	}

	/**
	 * @param executionRulesToAdd
	 * @see edu.utah.further.fqe.ds.api.domain.plan.Plan#addExecutionRules(java.util.Collection)
	 */
	@Override
	public void addExecutionRules(
			final Collection<? extends ExecutionRule> executionRulesToAdd)
	{
		for (final ExecutionRule child : executionRulesToAdd)
		{
			addExecutionRule(child);
		}
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.plan.Plan#getNumExecutionRules()
	 */
	@Override
	public int getNumExecutionRules()
	{
		return executionRules.size();
	}

	/**
	 * Return the executionRules property.
	 *
	 * @return the executionRules
	 */
	@Override
	public List<ExecutionRule> getExecutionRules()
	{
		// Defensive copy
		return CollectionUtil.newList(executionRules);
	}

	/**
	 * @param dependencyRule
	 * @see edu.utah.further.fqe.ds.api.domain.plan.Plan#addDependencyRule(edu.utah.further.fqe.ds.api.domain.plan.DependencyRule)
	 */
	@Override
	public void addDependencyRule(final DependencyRule dependencyRule)
	{
		dependencyRules.add(new DependencyRuleToImpl(dependencyRule));
	}

	/**
	 * @param dependencyRulesToAdd
	 * @see edu.utah.further.fqe.ds.api.domain.plan.Plan#addDependencyRules(java.util.Collection)
	 */
	@Override
	public void addDependencyRules(
			final Collection<? extends DependencyRule> dependencyRulesToAdd)
	{
		for (final DependencyRule child : dependencyRulesToAdd)
		{
			addDependencyRule(child);
		}
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.plan.Plan#getNumDependencyRules()
	 */
	@Override
	public int getNumDependencyRules()
	{
		return dependencyRules.size();
	}

	/**
	 * Return the dependencyRules property.
	 *
	 * @return the dependencyRules
	 */
	@Override
	public List<DependencyRule> getDependencyRules()
	{
		// Defensive copy
		return CollectionUtil.newList(dependencyRules);
	}

	// ========================= PRIVATE METHODS ===========================
}