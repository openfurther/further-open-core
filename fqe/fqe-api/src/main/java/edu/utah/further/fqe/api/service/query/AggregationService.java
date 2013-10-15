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
package edu.utah.further.fqe.api.service.query;

import java.util.Map;

import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResult;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResults;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.service.results.ResultType;

/**
 * A data source result set aggregation service implementation. Relies on a Hibernate
 * persistent layer of {@link QueryContext}s.
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
 * @version Jun 1, 2010
 */
public interface AggregationService
{
	// ========================= METHODS ===================================

	/**
	 * Update/aggregate the federated query contexts parent with the results of a child.
	 * 
	 * @param child
	 *            data source query context. Assumed to be persistent and have a
	 *            persistent parent
	 */
	void updateParentState(QueryContext child);

	/**
	 * Update the parent result set count to be the sum of its children counts.
	 * 
	 * @param parent
	 *            federated query context. Assumed to be persistent
	 */
	void updateCounts(QueryContext parent);

	/**
	 * Generate post-query result views (union, intersection, etc.)
	 * 
	 * @param parent
	 *            federated query context. Assumed to be persistent
	 */
	void generateResultViews(QueryContext parent);

	/**
	 * Generate aggregated count histograms, broken down by different demographic
	 * categories.
	 * 
	 * @param federatedQueryContext
	 * @param resultType
	 * @param intersectionIndex
	 * @return aggregated result object, holds multiple histograms (once per demographic
	 *         category)
	 */
	AggregatedResults generatedAggregatedResults(QueryContext federatedQueryContext);

	/**
	 * Generate aggregated count histograms, broken down by different demographic
	 * categories.
	 * 
	 * @param federatedQueryContext
	 * @param resultType
	 * @param intersectionIndex
	 * @return aggregated result object, holds multiple histograms (once per demographic
	 *         category)
	 */
	AggregatedResult generatedAggregatedResult(QueryContext federatedQueryContext,
			ResultType resultType, int intersectionIndex);

	/**
	 * Add missing data Category to aggregated demographic results 
	 *
	 *@param results
	 *
	 *@return results with missing data
	 * 
	 */
	AggregatedResults addMissingDataEntries (AggregatedResults results, Map<ResultType, ResultContext> resultViews );
	
	/**
	 * Scrub positive counts that are smaller than the mask boundary value.
	 * 
	 * @param result
	 *            raw counts
	 * @return scrubbed results
	 */
	AggregatedResults scrubResults(AggregatedResults result);

	/**
	 * Dependency-inject a count scrub threshold.
	 * 
	 * @param resultMaskBoundary
	 *            new count scrub threshold
	 */
	void setResultMaskBoundary(int resultMaskBoundary);

	/**
	 * Set a new value for the resultMaskOther property.
	 * 
	 * @param resultMaskOther
	 *            the resultMaskOther to set
	 */
	void setResultMaskOther(String resultMaskOther);
}