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
package edu.utah.further.fqe.ds.api.to;

import java.util.Collection;
import java.util.Date;

import edu.utah.further.core.api.security.HasSettableUserIdentifier;
import edu.utah.further.core.api.tree.ListComposite;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A transfer object counterpart of {@link QueryContext}.
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
 * @version Sep 10, 2009
 */
public interface QueryContextTo extends QueryContext, QueryContextIdentifier,
		QueryContextState, ListComposite<QueryContextTo>,
		HasSettableUserIdentifier<String>
{
	// ========================= METHODS ===================================

	/**
	 * Only to be used in constructors/copy instance methods. Set a new value for the
	 * executionId property.
	 * 
	 * @param executionId
	 *            the executionId to set
	 */
	void setExecutionId(String executionId);

	/**
	 * Return the parent context's ID property. Doesn't have a setter because it is
	 * managed by {@link #setParent(QueryContext)}.
	 * 
	 * @return the parentId
	 */
	Long getParentId();

	/**
	 * Return the assosciated {@link QueryContext} id for which results should be used as
	 * criteria
	 * 
	 * @return
	 */
	Long getAssociatedResultId();

	/**
	 * Set a new value for the search query TO property.
	 * 
	 * @param query
	 *            the search query TO to set
	 */
	@Override
	void setQuery(SearchQuery query);

	/**
	 * Add a child context to the children context list.
	 * 
	 * @param child
	 *            child context to add
	 * @see java.util.List#add(java.lang.Object)
	 */
	void addChild(final QueryContext data);

	/**
	 * Add children contexts to the children context list.
	 * 
	 * @param children
	 *            data to add
	 * @see java.util.List#add(java.lang.Object)
	 */
	void addChildren(Collection<? extends QueryContext> children);

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	int getNumChildren();

	/**
	 * Set a new value for the queue date.
	 * 
	 * @param queueDate
	 *            the queue to set
	 */
	void setQueueDate(Date queueDate);

	/**
	 * Set a new value for the queue date.
	 * 
	 * @param queueTime
	 *            time in milliseconds
	 */
	void setQueueTime(Long queueTime);

	/**
	 * @param start
	 */
	void setStartDate(Date start);

	/**
	 * @param startTime
	 */
	void setStartTime(Long startTime);

	/**
	 * @param end
	 */
	void setEndDate(Date end);

	/**
	 * @param endTime
	 */
	void setEndTime(Long endTime);
}