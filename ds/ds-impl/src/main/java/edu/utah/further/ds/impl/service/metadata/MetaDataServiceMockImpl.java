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

import static edu.utah.further.core.util.io.LoggingUtil.debugPrintBigTitle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;

/**
 * A simple mock implementation of data source meta data management.
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
public class MetaDataServiceMockImpl extends AbstractMetaDataService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(MetaDataServiceMockImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * This data source's metadata information
	 */
	private DsMetaData metaData;

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
		request.setAttribute(AttributeName.META_DATA, getMetaData());
		return true;
	}

	// ========================= IMPL: HasIdentifier<String> ==============

	/**
	 * Returns a unique identifier/symbol of the data source.
	 * 
	 * @return data source unique identifier
	 * @see edu.utah.further.ds.api.service.metadata.MetaDataService#getId()
	 */
	@Override
	public String getId()
	{
		return getMetaData().getName();
	}

	// ========================= IMPLEMENTATION: DsMetaDataRetriever =======

	/**
	 * A data source meta data retrieval implementation hook. Must return a non-
	 * <code>null</code> object.
	 * 
	 * @return this data source's meta data
	 * @see edu.utah.further.ds.api.service.MetaDataService#getMetaData()
	 */
	@Override
	public DsMetaData getMetaData()
	{
		debugPrintBigTitle(log, metaData.toString());
		// Make a defensive copy
		return new DsMetaData(metaData);
	}

	/**
	 * @param dsMetaData
	 * @see edu.utah.further.ds.api.service.metadata.MetaDataService#setMetaData(edu.utah.further.fqe.ds.api.domain.DsMetaData)
	 */
	@Override
	synchronized public void setMetaData(final DsMetaData dsMetaData)
	{
		this.metaData = dsMetaData;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @throws Exception
	 */
	@PostConstruct
	protected void afterPropertiesSet() throws Exception
	{
		if (log.isDebugEnabled())
		{
			log.debug("Initializing data service " + getMetaData().getName() + ": "
					+ getMetaData().getDescription());
		}
	}

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
