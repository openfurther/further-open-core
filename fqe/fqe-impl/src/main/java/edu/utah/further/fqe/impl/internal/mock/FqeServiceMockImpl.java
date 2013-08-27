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
package edu.utah.further.fqe.impl.internal.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.context.Mock;
import edu.utah.further.fqe.api.service.route.FqeService;
import edu.utah.further.fqe.ds.api.domain.Data;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.DsState;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A client implementation that sends requests to the FQE camel route and waits for a
 * response. This class provides Java API facade that can be called without directly
 * interacting with Camel (the interaction is done within this class).
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
 * @version Aug 7, 2009
 */
@Implementation
@Mock
@Component("fqeService")
public class FqeServiceMockImpl implements FqeService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(FqeServiceMockImpl.class);

	// ========================= FIELDS ===================================

	// ========================= DEPENEDENCIES ============================

	// ========================= METHODS ================================

	// ========================= IMPLEMENTATION: FqeService ===============

	/**
	 * @param logicalQueryContext
	 * @return
	 * @see edu.utah.further.fqe.ds.api.service.CommandTrigger#triggerCommand(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public QueryContext triggerQuery(final QueryContext logicalQueryContext)
	{
		return null;
	}

	/**
	 * @param queryContext
	 * @see edu.utah.further.fqe.api.service.route.FqeService#stopQuery(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void stopQuery(final QueryContext queryContext)
	{
		// Method stub
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.api.service.route.FqeService#status()
	 */
	@Override
	public Data status()
	{
		return null;
	}

	/**
	 * @param dataSourceId
	 * @return
	 * @see edu.utah.further.fqe.api.service.route.FqeService#status(java.lang.String)
	 */
	@Override
	public Data status(final String dataSourceId)
	{
		return null;
	}

	/**
	 * @param dataSourceId
	 * @param newState
	 * @return
	 * @see edu.utah.further.fqe.api.service.route.FqeService#updateState(java.lang.String,
	 *      edu.utah.further.fqe.ds.api.domain.DsState)
	 */
	@Override
	public DsMetaData updateState(final String dataSourceId, final DsState newState)
	{
		return null;
	}
}
