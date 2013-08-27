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
package edu.utah.further.fqe.ds.api.service.query;

import java.util.List;

import edu.utah.further.core.query.domain.SearchCriterion;

/**
 * This service is responsible for methods that are related to previous completed queries
 * that will be associated with a new query.
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
 * @version Mar 22, 2012
 */
public interface AssociatedResultService
{

	/**
	 * Constructs a {@link SearchCriterion} that can be added to an existing query's
	 * criteria. Implementation determines type of {@link SearchCriterion}
	 * 
	 * @param queryContextId
	 *            the id of the querycontext to get the results from
	 * @param datasourceId
	 *            the data source id this criterion will be built for
	 * @param propertyName
	 *            the name of the property for this criterion
	 * @return
	 */
	List<Long> getAssociatedResult(Long queryContextId, String datasourceId);
}
