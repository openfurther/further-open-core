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
 * Translates federated IDs to physical IDs and visa-versa
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
 * @version Dec 22, 2010
 */
public interface IdTranslationProvider
{
	// ========================= METHODS ===================================

	/**
	 * Translates a federated ID to a physical IDs.
	 * 
	 * @param federatedId
	 *            the federated identifier
	 * @return corresponding physical ID (1 or more)
	 */
	List<Long> translateToSourceId(Long federatedId);

	/**
	 * Translates a federated ID to a physical IDs.
	 * 
	 * @param sourceId
	 *           	the source identifier
	 * @return corresponding federated id (always 1)
	 */
	Long translateToFederatedId(Long sourceId);

	/**
	 * Translates a list of federated IDs to physical IDs.
	 * 
	 * @param federatedIds
	 *            list of federated IDs
	 * @return corresponding list of physical IDs
	 */
	List<Long> translateToSourceIds(List<Long> federatedIds);

	/**
	 * Translates a list of physical IDs to federated IDs.
	 * 
	 * @param federatedIds
	 *            list of federated IDs
	 * @return corresponding list of physical IDs
	 */
	List<Long> translateToFederatedIds(List<Long> sourceIds);

}
