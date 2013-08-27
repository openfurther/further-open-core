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
package edu.utah.further.ds.impl.service.query.mock;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.Mock;
import edu.utah.further.core.chain.UtilityProcessor;
import edu.utah.further.ds.api.service.query.logic.AbstractInitializer;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * Data Query initializer - mock implementation.
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
 * @version Feb 2, 2010
 */
@Mock
@Service("initializerMock")
@UtilityProcessor
public class InitializerMock extends AbstractInitializer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(InitializerMock.class);

	// ========================= FIELDS =================================

	/**
	 * Provides mock query processor common utilities.
	 */
	private final MockUtilService common = new MockUtilService("Initialize");

	// ========================= Impl: Initializer =======================

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.AbstractInitializer#initialize(edu.
	 * utah.further.fqe.ds.api.domain.QueryContext,
	 * edu.utah.further.fqe.ds.api.domain.DsMetaData)
	 */
	@Override
	public void initialize(final QueryContext queryContext, final DsMetaData metaData)
	{
		// Decorate with a debugging printout
		common.printTitle();
		super.initialize(queryContext, metaData);
	}

	/**
	 * @param queryContext
	 * @param dsMetaData
	 * @return
	 * @see edu.utah.further.ds.api.service.query.logic.Initializer#canAnswer(edu.utah.further.fqe.ds.api.domain.QueryContext,
	 *      edu.utah.further.fqe.ds.api.domain.DsMetaData)
	 */
	@Override
	public boolean canAnswer(final QueryContext queryContext, final DsMetaData dsMetaData)
	{
		// Can always answer
		return true;
	}
}
