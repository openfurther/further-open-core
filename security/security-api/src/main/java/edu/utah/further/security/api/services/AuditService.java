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
package edu.utah.further.security.api.services;

import edu.utah.further.fqe.ds.api.domain.ExportContext;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * An interface providing read and write operations on auditable events.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Shan He {@code <shan.he@utah.edu>}
 * @version May 4, 2011
 */
public interface AuditService
{

	// =======================================METHODS===========================================

	/**
	 * Save the logical (parent) query context into persistent stores.
	 * 
	 * @param queryContext
	 */
	void logQuery(QueryContext queryContext);

	/**
	 * Save the query result for physical (child) query context into persistent stores.
	 * The query result at this stage is the aggregated number counts.
	 * 
	 * @param queryContext
	 */
	void logQueryResult(QueryContext queryContext);

	/**
	 * Logs a requested data export - only logs the details of the export and not the
	 * query or the results. Refer to the query log event to get more details about the
	 * query. Main purpose is for reporting how many exports have occurred. TODO: if
	 * exported data is ever PHI, we must get more strict with what is logged on export.
	 * 
	 * @param context
	 */
	void logExportRequest(ExportContext context);

}
