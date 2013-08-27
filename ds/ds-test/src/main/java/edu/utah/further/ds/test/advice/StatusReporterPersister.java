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
package edu.utah.further.ds.test.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.ds.api.service.query.processor.StatusReporter;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A Query Context status implementation which persists the QueryContext to the database.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version January 23, 2012
 */
@Component("statusReporter")
public final class StatusReporterPersister implements StatusReporter
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * QueryContextService for queueing queries into the FQE
	 */
	@Autowired
	@Qualifier("queryContextService")
	private QueryContextService qcService;

	// ========================= IMPL: StatusReporter ======================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.processor.StatusReporter#update(edu.utah.
	 * further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	@Transactional("transactionManagerFqe")
	public void notify(final QueryContext queryContext)
	{
		// Send status to the status route
		qcService.update(queryContext);
	}

	// ========================= GET/SET ==================================
}
