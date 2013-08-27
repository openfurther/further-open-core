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

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.ds.api.ws.WsDsClient;
import edu.utah.further.ds.api.ws.WsDsResponse;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * An executor which executes a {@link WebServiceDatasource}
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
@Service("webserviceExecutor")
public class WebServiceExecutor extends AbstractNonDelegatingUtilityProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(WebServiceExecutor.class);

//	@Autowired
//	private XmlService xmlService;

	// ========================= IMPLEMENTATION: RequestHandler ==============

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.
	 * api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Wrap the request
		final WebServiceExecReq webServiceExecReq = new WebServiceExecReq(request);

		// Get the client for invoking the WS
		final WsDsClient wsDsClient = webServiceExecReq.getWebServiceDatasource();

		// Set the QueryXml for use by the WsDsClient
		final QueryContext queryContext = webServiceExecReq.getQueryContext();

//		try
//		{
//			queryContext.setQueryXml(xmlService.marshal(queryContext.getQuery(),
//					xmlService.options().addClass(SearchQueryTo.class).buildContext()));
//		}
//		catch (final JAXBException e)
//		{
//			throw new ApplicationException(
//					"Unable to marshal SearchQuery XML for use by Web Service Executor, cannot continue",
//					e);
//		}

		// Ensure there is a client to call the web service
		Validate.notNull(wsDsClient,
				"A web service data source client is required, ensure one is set under attribute "
						+ AttributeName.WS_DS_CLIENT.getLabel());

		final WsDsResponse response = wsDsClient.invoke(queryContext,
				webServiceExecReq.isPaged());

		// Invoke the WS, set the result
		webServiceExecReq.setResult(response.getResponse());

		return false;
	}
}
