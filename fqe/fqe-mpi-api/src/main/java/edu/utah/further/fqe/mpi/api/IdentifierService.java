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
package edu.utah.further.fqe.mpi.api;

import java.util.List;


/**
 * A service which provides unique identifiers or returns an existing identifier if one
 * exists based on criteria.
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
	 * Generates a unique identifier based on the parameters. If an identifier already
	 * exists for these parameters, the existing identifier is returned.
	 *
	 * @param params
	 *            Parameters required to generate or retrieve the identifier
	 * @return the generated or existing identifier
	 */
	Long generateId(Identifier params);
	
	/**
	 * Translates a list of federated IDs to physical IDs.
	 *
	 * @param federatedIds
	 *            list of federated IDs
	 * @return corresponding list of physical IDs
	 */
	List<Long> translateIds(List<Long> federatedIds, String dataSourceId);
}
