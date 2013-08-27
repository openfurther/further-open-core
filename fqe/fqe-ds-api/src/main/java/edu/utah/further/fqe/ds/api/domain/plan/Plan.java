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
package edu.utah.further.fqe.ds.api.domain.plan;

import java.util.Collection;
import java.util.List;

import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A {@link QueryContext} execution plan. It is represented as a graph whose nodes are
 * {@link ExecutionRule}s (each representing a single {@link SearchQuery} execution,
 * potentially directed at a single data source) and {@link DependencyRule} edges, which
 * determine execution dependencies.
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
 * @version Nov 22, 2010
 */
public interface Plan
{
	// ========================= NESTED TYPEs ==============================

	enum Type
	{
		/**
		 * The trivial plan (has no execution rules). This is <code>true</code> if and
		 * only if this is a broadcast query.
		 */
		BROADCAST,

		/**
		 * A general dependency-graph plan. Includes the fully parallelizable case (known
		 * as "branched query").
		 */
		PHASED
	}

	// ========================= METHODS ===================================

	/**
	 * Return the type of this plan.
	 *
	 * @return plan type
	 */
	Type getType();

	/**
	 * Add a send rule.
	 *
	 * @param executionRule
	 *            send rule to add
	 * @see java.util.List#add(java.lang.Object)
	 */
	void addExecutionRule(ExecutionRule executionRule);

	/**
	 * Add multiple send rules.
	 *
	 * @param executionRulesToAdd
	 *            send rule collection to add
	 * @see java.util.List#add(java.lang.Object)
	 */
	void addExecutionRules(Collection<? extends ExecutionRule> executionRulesToAdd);

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	int getNumExecutionRules();

	/**
	 * Return the executionRules property.
	 *
	 * @return the executionRules
	 */
	List<ExecutionRule> getExecutionRules();

	/**
	 * Add a send rule.
	 *
	 * @param dependencyRule
	 *            send rule to add
	 * @see java.util.List#add(java.lang.Object)
	 */
	void addDependencyRule(DependencyRule dependencyRule);

	/**
	 * Add multiple send rules.
	 *
	 * @param dependencyRulesToAdd
	 *            send rule collection to add
	 * @see java.util.List#add(java.lang.Object)
	 */
	void addDependencyRules(Collection<? extends DependencyRule> dependencyRulesToAdd);

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	int getNumDependencyRules();

	/**
	 * Return the dependencyRules property.
	 *
	 * @return the dependencyRules
	 */
	List<DependencyRule> getDependencyRules();
}