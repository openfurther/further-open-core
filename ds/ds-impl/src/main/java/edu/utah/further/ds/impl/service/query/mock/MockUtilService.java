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
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.context.Mock;
import edu.utah.further.ds.api.service.metadata.MetaDataService;

/**
 * A utility class used by all query processor mock implementations.
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
public class MockUtilService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(MockUtilService.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Manages the data source's meta data.
	 */
	@Autowired
	private MetaDataService metaDataService;

	// ========================= FIELDS ====================================

	/**
	 * Name of this mock processor, for debugging print-outs.
	 */
	private final String name;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param name
	 */
	public MockUtilService(final String name)
	{
		super();
		this.name = name;
	}

	/**
	 * Returns the name of this mock sqp
	 * 
	 * @return
	 */
	public String getName()
	{
		return "[Mock] " + name;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param searchQuery
	 * @return
	 */
	protected final void printTitle()
	{
		if (log.isDebugEnabled())
		{
			log.debug("###################################");
			log.debug(getName());
			log.debug("###################################");
		}
	}

	// /**
	// * Set a new status reported by a data source in a query context using the data
	// * source's identifier.
	// *
	// * @param queryContext
	// * child query context
	// * @param dataQueryStatus
	// * new status
	// * @return the generated status message
	// */
	// protected final void setCurrentStatus(final QueryContext queryContext,
	// final DataQueryStatus dataQueryStatus)
	// {
	// StatusMessageUtil.setCurrentStatus(queryContext, metaDataService.getId(),
	// dataQueryStatus);
	// }

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the metaDataService property.
	 * 
	 * @return the metaDataService
	 */
	protected final MetaDataService getMetaDataService()
	{
		return metaDataService;
	}

	/**
	 * Set a new value for the metaDataService property.
	 * 
	 * @param metaDataService
	 *            the metaDataService to set
	 */
	public void setMetaDataService(final MetaDataService metaDataService)
	{
		this.metaDataService = metaDataService;
	}
}
