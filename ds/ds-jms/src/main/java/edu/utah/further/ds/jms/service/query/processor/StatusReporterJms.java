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
package edu.utah.further.ds.jms.service.query.processor;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.utah.further.ds.api.service.query.processor.StatusReporter;
import edu.utah.further.ds.api.util.DsEndpointNames;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A Search Query status implementation which relays status to a JMS topic or queue. As an
 * example, this implementation as it stands will likely marshal the results and post to a
 * topic.
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
 * @version Feb 8, 2010
 */
@Component("statusReporter")
public final class StatusReporterJms implements StatusReporter
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Producer template for producing status messages. Defined in the CamelContext.
	 */
	@Autowired
	private ProducerTemplate producerTemplate;

	/**
	 * The JMS request endpoint to which status notifications are posted.
	 */
	@EndpointInject(uri = DsEndpointNames.STATUS_MARSHALLER)
	private Endpoint marshalStatusEndpoint;

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
		// Send status to the status route
		producerTemplate.sendBody(marshalStatusEndpoint, queryContext);
	}

	// ========================= GET/SET ==================================
}
