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
package edu.utah.further.fqe.impl.data;

import java.util.List;

import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Provides custom CRUD operations on {@link QueryContext}s.
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
 * @version Nov 4, 2010
 */
public interface QueryContextDao
{
	// ========================= METHODS ===================================

	/**
	 * Return all federated query contexts (roots of query context trees).
	 *
	 * @return list of query contexts that don't have parents
	 */

	List<QueryContext> findAllFederatedQueryContexts();

	/**
	 * Delete all query contexts from a specific user.
	 *
	 * @param userId
	 *            user ID
	 */
	void deleteByUser(String userId);

	/**
	 * Find a query by origin ID.
	 *
	 * @param originId
	 *            query origin ID (e.g. i2b2 ID)
	 * @return FQE query, or <code>null</code>, if not found
	 */
	QueryContext findQueryContextWithOriginId(Long originId);
}