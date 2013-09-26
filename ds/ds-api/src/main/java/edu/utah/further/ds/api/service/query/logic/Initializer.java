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
package edu.utah.further.ds.api.service.query.logic;

import java.util.Map;

import org.springframework.security.core.Authentication;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * The Search Query Initializer is responsible for initializing the QueryContext for a
 * data source data query flow, and executing any pre-processing such as loading artifacts
 * from the MDR, or modifying the QueryContext for execution of the given data source.
 * <p>
 * Implementations should be aware that implementations of this class may be used by
 * multiple threads as multiple data sources are processed and will likely need to
 * consider a thread safe implementation or a new instantiation for each use.
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
 * @version Jan 26, 2010
 */
public interface Initializer
{
	// ========================= METHODS ===================================

	/**
	 * Initializes the data source's query context.
	 * 
	 * @param queryContext
	 *            this data source's query context. When passed into this method, is only
	 *            a basic copy of the federated (parent) query context. Manipulated inside
	 *            this method
	 * @param dsMetaData
	 *            Metadata about that data source
	 * @throws ApplicationException
	 *             if initialization fails or this query is not applicable for this data
	 *             source.
	 */
	void initialize(QueryContext queryContext, DsMetaData metaData);

	/**
	 * Authorizes this query request to execute
	 * 
	 * @param authentication
	 *            the populated authentication object which contains a users roles,
	 *            privileges, and properties.
	 * @param queryContext
	 *            this data source's query context
	 * @param metaData
	 *            Metadata about that data source
	 */
	void authorize(Authentication authentication, QueryContext queryContext,
			DsMetaData metaData);

	/**
	 * Whether or not this data source can answer this query or not
	 * 
	 * @param queryContext
	 *            the data source query context
	 * @param dsMetaData
	 *            Metadata about that data source
	 * @return true if the data source can answer this query or false if it cannot
	 */
	boolean canAnswer(QueryContext queryContext, DsMetaData dsMetaData);
	
	/**
	 * Returns the initial attributes used to seed the request chain
	 *
	 * @return the attributes
	 */
	public Map<String, Object> getAttributes();

	/**
	 * Sets the initial attributes used to seed the request chain
	 *
	 * @param attributes the attributes to set
	 */
	public void setAttributes(final Map<String, Object> attributes);
	
	/**
	 * Sets the initial attributes used to seed the request chain
	 *
	 * @param attributes the attributes to set
	 */
	public void setNamedAttributes(final Map<AttributeName, Object> attributes);
}
