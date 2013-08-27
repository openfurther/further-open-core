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
package edu.utah.further.ds.api.service.query.logic;

import java.util.Collection;
import java.util.List;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.collections.page.PagingLoopController;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * Encapsulates the logic of persisting entities to a database within a persistence
 * request processor.
 * <p>
 * Implementations should be aware that implementations of this class may be used by
 * multiple threads as multiple data sources are processed and will likely need to
 * consider a thread safe implementation or a new instantiation for each use.
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
 * @version Oct 1, 2010
 */
public interface Persister
{
	// ========================= METHODS ===================================

	/**
	 * Persist an entity to a database as part of a paging loop.
	 * 
	 * @param entities
	 *            a collection of entities to persist (in the natural iteration order of
	 *            this collection)
	 * @param controller
	 *            contains the number of entities persisted to date in the paging loop and
	 *            the maximum number of results to persist in the entire paging loop
	 * @return the list of persisted entities during this call. Its size is less than or
	 *         equal to <code>entities.size()</code>. Bounded by <code>maxResults</code>
	 *         and by the entities' uniqueness (some can be duplicates = share the same
	 *         ID). May or may not contain duplicate records depending on the
	 *         implementation. The list is not guaranteed to be sorted in any particular
	 *         order
	 */
	List<PersistentEntity<?>> persist(Collection<? extends PersistentEntity<?>> entities,
			PagingLoopController controller);
	
	/**
	 * Persist an entity to a database as part of a paging loop.
	 * 
	 * @param entities
	 *            a collection of entities to persist (in the natural iteration order of
	 *            this collection)
	 * @param controller
	 *            contains the number of entities persisted to date in the paging loop and
	 *            the maximum number of results to persist in the entire paging loop
	 * @param request
	 * 		contains the full data source request with all attritubutes
	 * @return the list of persisted entities during this call. Its size is less than or
	 *         equal to <code>entities.size()</code>. Bounded by <code>maxResults</code>
	 *         and by the entities' uniqueness (some can be duplicates = share the same
	 *         ID). May or may not contain duplicate records depending on the
	 *         implementation. The list is not guaranteed to be sorted in any particular
	 *         order
	 */
	List<PersistentEntity<?>> persist(Collection<? extends PersistentEntity<?>> entities,
			PagingLoopController controller, ChainRequest request);
}
