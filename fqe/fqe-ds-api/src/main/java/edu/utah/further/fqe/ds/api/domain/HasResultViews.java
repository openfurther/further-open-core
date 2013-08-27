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
package edu.utah.further.fqe.ds.api.domain;

import java.util.Map;

import edu.utah.further.fqe.ds.api.results.ResultType;

/**
 * An object that holds a query result's federated join aggregated counts.
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
 * @version Mar 30, 2011
 */
public interface HasResultViews
{
	// ========================= METHODS ===================================

	/**
	 * Remove all result views.
	 */
	void clearResultViews();

	/**
	 * @param key
	 * @return
	 */
	ResultContext removeResultView(ResultContextKey key);

	/**
	 * @param type
	 * @param intersectionIndex
	 * @param resultContext
	 * @return
	 */
	ResultContext addResultView(ResultType type, Integer intersectionIndex,
			ResultContext resultContext);

	/**
	 * @param other
	 */
	void setResultViews(Map<? extends ResultContextKey, ? extends ResultContext> other);

	/**
	 * @param key
	 * @return
	 */
	ResultContext getResultView(ResultContextKey key);

	/**
	 * Return a resultContext by key.
	 *
	 * @return a resultContext
	 */
	ResultContext getResultView(ResultType type, Integer intersectionIndex);

	/**
	 * @return
	 */
	Map<ResultContextKey, ResultContext> getResultViews();
}
