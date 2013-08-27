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
package edu.utah.further.ds.impl.service.metadata;

import static org.apache.commons.lang.Validate.notNull;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.chain.AbstractRequestProcessor;
import edu.utah.further.ds.api.service.metadata.MetaDataService;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;

/**
 * A simple mock implementation that updates a data source's meta data.
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
 * @version Aug 14, 2009
 */
public class UpdateMetaDataProcessor extends AbstractRequestProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(UpdateMetaDataProcessor.class);

	// ========================= FIELDS ====================================

	/**
	 * Manages the data source's meta data.
	 */
	@Autowired
	private MetaDataService metaDataService;

	// ========================= IMPLEMENTATION: RequestProcessor ==========

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.chain.AbstractRequestProcessor#process(edu.utah.further
	 * .core.api.chain.ChainRequest)
	 */
	@Override
	public final boolean process(final ChainRequest request)
	{
		// Read input parameters; contain updated DS state to set
		final DsMetaData inputParameters = request.getAttribute(AttributeName.META_DATA);
		notNull(inputParameters,
				"Update state input body was not set on the input camel exchange for this command!");

		// Update meta data in the data source
		final DsMetaData updateMetaData = metaDataService.getMetaData();
		updateMetaData.setState(inputParameters.getState());
		metaDataService.setMetaData(updateMetaData);

		// Save results in the request
		request.setAttribute(AttributeName.META_DATA, metaDataService.getMetaData());
		return true;
	}

	// ========================= GETTERS & SETTERS =========================

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

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Shut down.
	 */
	@PreDestroy
	protected void destroy()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Shutting down data service");
		}
	}

}
