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
package edu.utah.further.ds.impl.advice;

import org.springframework.stereotype.Component;

import edu.utah.further.core.api.context.Mock;
import edu.utah.further.ds.api.service.query.processor.StatusReporter;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Mock Search Query status. This class is final and should not be advised like a normal
 * query processor, otherwise this will create an infinite recursion in status message
 * emission.
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
 * @version Feb 4, 2010
 */
@Component("statusReporter")
@Mock
public final class StatusReporterMock implements StatusReporter
{
	// ========================= CONSTANTS =================================

	// ========================= IMPL: StatusReporter ======================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.processor.StatusReporter#update(edu.utah.
	 * further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void notify(final QueryContext queryContext)
	{
		// Do nothing
	}
}
