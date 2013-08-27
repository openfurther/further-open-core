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
package edu.utah.further.ds.jms.lifecycle;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;

import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.ds.api.service.metadata.MetaDataService;
import edu.utah.further.ds.jms.fixture.DsJmsEndpointNames;
import edu.utah.further.ds.jms.fixture.DsJmsFixture;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.DsState;
import edu.utah.further.fqe.ds.api.util.CommandType;
import edu.utah.further.fqe.ds.api.util.MessageHeader;

/**
 * Unit test (borderline integration test) to test out Apache Camel & Active MQ for the
 * query life cycle.
 * <p>
 * See the *-route.xml file for the definition of data flow.
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
 * @version Jan 28, 2010
 */
public final class UTestCamelQueryLifeCycle extends DsJmsFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestCamelQueryLifeCycle.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Resource resolver.
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Used for producing messages
	 */
	@Autowired
	private ProducerTemplate producerTemplate;

	/**
	 * The Camel Context in order to stop the flow
	 */
	@Autowired
	private CamelContext camelContext;

	/**
	 * The JMS request topic where query requests are posted
	 */
	@EndpointInject(uri = DsJmsEndpointNames.REQUEST)
	private Endpoint requestEndpoint;

	/**
	 * Mock result end point used for assertions.
	 */
	@EndpointInject(uri = DsJmsEndpointNames.RESULT)
	private MockEndpoint resultEndpoint;

	/**
	 * Mock status end point used for assertions.
	 */
	@EndpointInject(uri = DsJmsEndpointNames.STATUS)
	private MockEndpoint statusEndpoint;

	/**
	 * Mock endpoint to send simple messages to in unit tests.
	 */
	@EndpointInject(uri = DsJmsEndpointNames.OUTCOME)
	private MockEndpoint outcomeEndpoint;

	/**
	 * The data source metadata retriever
	 */
	@Autowired
	private MetaDataService dsMetadataRetriever;

	// ========================= SETUP METHODS =============================

	/**
	 * Shut everything down.
	 *
	 * @throws Exception
	 */
	@After
	public void after() throws Exception
	{
		camelContext.stop();
	}

	// ========================= METHODS ===================================

	/**
	 * Triggers the mock query route defined in fqe-ds-jms-test-route-context.xml and
	 * receives the results from the resultTopic.
	 *
	 * @throws Exception
	 */
	@Test
	@DirtiesContext
	public void mockQueryRoute() throws Exception
	{
		// Expect one final result
		resultEndpoint.setExpectedMessageCount(1);

		// Expect a status for each process except the last one
		statusEndpoint.setExpectedMessageCount(6);

		sendDataQuery();

		resultEndpoint.assertIsSatisfied();
		statusEndpoint.assertIsSatisfied();
	}

	/**
	 * Triggers the mock metadata route defined in fqe-ds-jms-test-route-context.xml and
	 * receives the results from the resultTopic.
	 *
	 * @throws Exception
	 */
	@Test
	@DirtiesContext
	public void mockMetadataRoute() throws Exception
	{
		resultEndpoint.setExpectedMessageCount(1);
		statusEndpoint.setExpectedMessageCount(0);

		producerTemplate.sendBodyAndHeader(requestEndpoint, newEmptyDsMetaData(),
				MessageHeader.COMMAND_TYPE.getName(), CommandType.META_DATA.getName());
		
		Thread.sleep(1000);

		resultEndpoint.assertIsSatisfied();
		statusEndpoint.assertIsSatisfied();
	}

	@Test
	@DirtiesContext
	public void mockInactiveRoute() throws Exception
	{
		final DsMetaData metaData = dsMetadataRetriever.getMetaData();
		metaData.setState(DsState.INACTIVE);
		dsMetadataRetriever.setMetaData(metaData);

		// Expect 0 results
		resultEndpoint.setExpectedMessageCount(0);
		statusEndpoint.setExpectedMessageCount(0);

		sendDataQuery();

		resultEndpoint.assertIsSatisfied();
		statusEndpoint.assertIsSatisfied();
	}

	/**
	 * Send a non-JAXB object to a topic
	 *
	 * @throws Exception
	 */
	@Test
	@DirtiesContext
	public void sendNonJaxbBody() throws Exception
	{
		// Expect one final result
		outcomeEndpoint.setExpectedMessageCount(1);

		// Send a number
		final Long entity = new Long(123l);
		producerTemplate.sendBody(outcomeEndpoint, entity);

		resultEndpoint.assertIsSatisfied();
		outcomeEndpoint.assertIsSatisfied();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Helper method to send a data query to the request endpoint
	 *
	 * @throws IOException
	 */
	private void sendDataQuery() throws IOException
	{
		/* Read the QueryContext */
		final Resource resource = applicationContext
				.getResource("classpath:data/query-context-disjunction.xml");
		final String testQueryContext = IoUtil.getFileAsString(resource.getFile());

		// final String testQueryContext = new String(
		// getFileAsBytes("src/test/resources/data/query-context-disjunction.xml"));

		producerTemplate.sendBodyAndHeader(requestEndpoint, testQueryContext,
				MessageHeader.COMMAND_TYPE.getName(), CommandType.DATA_QUERY.getName());
	}

	/**
	 * @return
	 */
	private DsMetaData newEmptyDsMetaData()
	{
		return new DsMetaData();
	}
}
