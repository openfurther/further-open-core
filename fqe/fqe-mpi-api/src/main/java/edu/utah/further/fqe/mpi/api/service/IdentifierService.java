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
package edu.utah.further.fqe.mpi.api.service;

import java.util.List;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.fqe.mpi.api.Identifier;

/**
 * A service which provides and retrieves virtual identifiers for queries and results.
 * Virtual identifiers are new identifiers requested during result translation or other
 * processes so as to avoid identifier collisions.
 * 
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 6, 2010
 */
public interface IdentifierService
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

	/**
	 * Generates a new unique identifier
	 * 
	 * @return a unique identifier
	 */
	Long generateNewId();

	/**
	 * Generates a unique virtual identifier based on the parameters. If a virtual
	 * identifier already exists for these parameters, the existing virtual identifier is
	 * returned.
	 * 
	 * @param params
	 *            Parameters required to generate or retrieve the identifier
	 * @return the generated or existing identifier
	 */
	Long generateId(Identifier params);

	/**
	 * Translates a list of virtual IDs to physical IDs.
	 * 
	 * @param virtualIds
	 *            list of federated IDs
	 * @return corresponding list of physical IDs
	 */
	List<Long> translateIds(List<Long> virtualIds, String dataSourceId);

	/**
	 * Returns a list of virtual identifiers based on the query ids.
	 * 
	 * @param a
	 *            list of query ids
	 * @return a list of identifiers
	 */
	List<Long> getVirtualIdentifiers(List<String> queryIds);

	/**
	 * Returns a list of {@link Identifier}s for which identity resolution needs to be
	 * performed.
	 * 
	 * @param queryId
	 * @return
	 */
	List<Identifier> getUnresolvedIdentifiers(String queryId);
	
	/**
	 * Persists updates to already saved identifiers
	 * 
	 * @param identifiers
	 */
	void updateSavedIdentifiers(List<? extends PersistentEntity<?>> identifiers);
}
