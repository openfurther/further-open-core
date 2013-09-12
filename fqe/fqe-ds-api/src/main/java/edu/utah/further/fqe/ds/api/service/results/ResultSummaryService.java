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
package edu.utah.further.fqe.ds.api.service.results;

import java.util.List;

/**
 * Result service for displaying summary or aggregated results.
 * 
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Sep 16, 2013
 */
public interface ResultSummaryService
{
	// ========================= METHODS ===================================

	/**
	 * Generate a union result from the list of query identifiers. This is used to display
	 * the overall results depending on the {@link ResultType}
	 * 
	 * @param queryIds
	 *            list of DQC IDs to join
	 * @param resultType
	 *            join result type
	 * @return union result
	 */
	Long join(List<String> queryIds, ResultType resultType);
}
