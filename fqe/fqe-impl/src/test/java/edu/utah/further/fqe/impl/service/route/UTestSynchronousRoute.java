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
package edu.utah.further.fqe.impl.service.route;

import static edu.utah.further.core.api.text.StringUtil.getAsNameList;
import static edu.utah.further.core.test.util.AssertUtil.assertCollectionsEqualInAnyOrder;
import static edu.utah.further.core.util.io.LoggingUtil.debugPrintAndCenter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import edu.utah.further.core.camel.EndpointConsumer;
import edu.utah.further.core.camel.ExchangeMatcherHeader;
import edu.utah.further.core.camel.FutureExchange;
import edu.utah.further.core.camel.FutureExchangeSingleMatcher;
import edu.utah.further.fqe.api.service.route.FqeService;
import edu.utah.further.fqe.ds.api.domain.Data;
import edu.utah.further.fqe.ds.api.util.FqeEndpointNames;
import edu.utah.further.fqe.ds.api.util.MessageHeader;
import edu.utah.further.fqe.impl.fixture.FqeImplRouteFixture;

/**
 * Tests the syncrhonous FQE/DS route, e.g. collecting metadata from all data sources.
 * <p>
 * See the fqe-impl-jtest-route-sync-context.xml context file to inspect the data flow for
 * this test.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Feb 24, 2010
 * @see https://wiki.chpc.utah.edu/display/further/Federated+Query+Engine+Architecture
 */
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public final class UTestSynchronousRoute extends FqeImplRouteFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestSynchronousRoute.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Used for producing messages
	 */
	@Autowired
	private ProducerTemplate producerTemplate;

	/**
	 * Mock result end point used for assertions.
	 */
	@EndpointInject(uri = FqeEndpointNames.REQUEST)
	private Endpoint requestEndpoint;

	/**
	 * Final FQE results are received and cached by this service.
	 */
	@Autowired
	private EndpointConsumer endpointConsumer;

	/**
	 * Federated query service in order to trigger a query
	 */
	@Autowired
	private FqeService fqeService;

	// ========================= SETUP METHODS =============================

	// ========================= TESTING METHODS ===========================

	/**
	 * Requests meta data from all data sources, listens for results on an
	 * {@link EndpointConsumer} and retrieves the future exchange from the consumer.
	 */
	@Test
	@DirtiesContext
	public void getMetaDataDirectFlow()
	{
		// Create query context
		debugPrintAndCenter(log, "Init query context");
		final QueryContextMock queryContext = new QueryContextMock(
				MessageContext.FEDERATED, TEST_MESSAGE);

		// Initialize result listener
		debugPrintAndCenter(log, "Start listening to exchanges");
		final FutureExchange futureExchange = new FutureExchangeSingleMatcher(
				endpointConsumer, new ExchangeMatcherHeader(MessageHeader.COMMAND_ID
						.getName(), queryContext.getQueryId()));

		// Trigger query flow
		debugPrintAndCenter(log, "Init query flow");
		producerTemplate.sendBodyAndHeaders(requestEndpoint, queryContext.getBody(),
				queryContext.getHeaders());

		// Could run in-between operations here in a real implementation...

		// Poll for results
		debugPrintAndCenter(log, "Poll");
		final Exchange result = futureExchange.poll(50L, 2000L, TimeUnit.SECONDS);

		// Clean up to prevent memory leaks due to dead future exchange references in
		// EndpointConsumer
		debugPrintAndCenter(log, "Cancel");
		futureExchange.cancel(true);

		// Check request-reply results
		assertNotNull("No federated result was returned", result);
		final Data federatedData = result.getIn().getBody(Data.class);
		assertFederatedMetaDataResults(federatedData);
	}

	/**
	 * Requests meta data from all data sources using an {@link FqeService} call, listens
	 * for results on an {@link EndpointConsumer} and retrieves the future exchange from
	 * the consumer.
	 */
	@Test
	@DirtiesContext
	public void getMetaDataFqeCall()
	{
		final Data federatedResult = fqeService.status();
		assertFederatedMetaDataResults(federatedResult);
	}

	// ========================= PRIVATE TYPES =============================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param federatedData
	 */
	private void assertFederatedMetaDataResults(final Data federatedData)
	{
		assertNotNull("Federated data entity was not unmarshalled", federatedData);
		assertEquals(getNumDataSources(), federatedData.getNumChildren());
		final List<String> expectedBodies = getAsNameList(dataSources);
		assertCollectionsEqualInAnyOrder(expectedBodies, getAsNameList(federatedData
				.getChildren()));
	}
}
