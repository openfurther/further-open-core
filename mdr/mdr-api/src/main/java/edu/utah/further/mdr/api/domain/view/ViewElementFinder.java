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
package edu.utah.further.mdr.api.domain.view;

import edu.utah.further.dts.api.to.DtsConceptId;

/**
 * An abstraction for search modules that find applicable view element by DTS Concept ID.
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
 * @version Aug 17, 2009
 */
public interface ViewElementFinder<K, N extends ViewNode<K, E, N>, E extends ViewElement>
{
	// ========================= METHODS ===================================

	/**
	 * Finds a {@link ViewElement} given a {@link ViewModel} and a {@link DtsConceptId}.
	 * Returns null if not found.
	 *
	 * @param viewModel
	 * @param conceptId
	 * @return view element
	 */
	E findViewElementByDtsConceptId(ViewModel<K, N, E> viewModel, DtsConceptId conceptId);
}
