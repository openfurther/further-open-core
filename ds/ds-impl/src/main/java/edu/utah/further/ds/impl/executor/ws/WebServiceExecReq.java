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
package edu.utah.further.ds.impl.executor.ws;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.ds.api.request.QueryExecutionRequest;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.ds.api.ws.WsDsClient;

/**
 * A wrapper for typed retrieval of {@link ChainRequest} attributes involved in a web
 * service execution request.
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
 * @version Jun 28, 2010
 */
public final class WebServiceExecReq extends QueryExecutionRequest
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(WebServiceExecReq.class);

	// ========================= CONSTRUCTORS =======================

	/**
	 * @param chainRequest
	 */
	public WebServiceExecReq(final ChainRequest chainRequest)
	{
		super(chainRequest);
	}

	// ========================= GET/SET =================================

	/**
	 * Return the webServiceDatasource property.
	 *
	 * @return the webServiceDatasource
	 */
	public WsDsClient getWebServiceDatasource()
	{
		return getChainRequest().getAttribute(AttributeName.WS_DS_CLIENT);
	}

	/**
	 * Set a new value for the webServiceDatasource property.
	 *
	 * @param webServiceDatasource
	 *            the webServiceDatasource to set
	 */
	public void setWebServiceDatasource(final WsDsClient webServiceDatasource)
	{
		getChainRequest().setAttribute(AttributeName.WS_DS_CLIENT, webServiceDatasource);
	}

	/**
	 * Return the userId property.
	 *
	 * @return the userId
	 */
	public String getUserId()
	{
		return getQueryContext().getUserId();
	}

	/**
	 * Return if the request is paged.
	 *
	 * @return the userId
	 */
	public boolean isPaged()
	{
		// return (getChainRequest().getAttribute(AttributeName.PAGING_TYPE) != null) ?
		// true : false;
		// Due to large throughput, always page
		return true;
	}
}
