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

import java.util.Map;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A {@link SearchQuery} translator which (ideally) uses MDR artifacts to translate terms
 * (which in turn call DTS), that is query terms or fields, to the appropriate destination
 * terms and format.
 * <p>
 * Implementations should be aware that this class may be used by multiple threads as
 * multiple data sources are processed and will likely need to consider a thread safe
 * implementation or a new instantiation for each use.
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
 * @version Sep 9, 2009
 */
public interface QueryTranslator
{
	// ========================= METHODS ===================================

	/**
	 * Translates terms in the SearchQuery to the destination terms.
	 * 
	 * @param queryContext
	 *            the QueryContext which contains the SearchQuery and the target
	 * @param attributes
	 *            A map of key-value pairs if needed for result translation
	 * @return a new object ready for execution
	 * @throws ApplicationException
	 *             if the translation fails
	 */
	<T> T translate(QueryContext queryContext, Map<String, Object> attributes);
}
