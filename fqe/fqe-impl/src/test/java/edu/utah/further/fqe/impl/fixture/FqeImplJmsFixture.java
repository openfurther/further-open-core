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
package edu.utah.further.fqe.impl.fixture;

import static edu.utah.further.core.camel.CamelUtil.getHeader;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.discrete.HasQueryIdentifier;
import edu.utah.further.core.camel.CamelUtil;
import edu.utah.further.fqe.ds.api.domain.Data;
import edu.utah.further.fqe.ds.api.util.MessageHeader;
import edu.utah.further.fqe.impl.service.route.DataSourceMock;

/**
 * Camel unit test fixture.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 17, 2008
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class FqeImplJmsFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(FqeImplJmsFixture.class);

	/**
	 * A text to send.
	 */
	protected static final String TEST_MESSAGE = "Test message";

	// ========================= DEPENDENCIES ==============================

	/**
	 * The Camel Context in order to stop the flow
	 */
	@Autowired
	private CamelContext camelContext;

	/**
	 * For manual processing to cross-validate against.
	 */
	@Autowired
	@Qualifier("dataSource1")
	protected DataSourceMock dataSource1;

	/**
	 * For manual processing to cross-validate against.
	 */
	@Autowired
	@Qualifier("dataSource2")
	protected DataSourceMock dataSource2;

	/**
	 * Number of simulated data sources.
	 */
	@Resource(name = "dataSources")
	protected List<Data> dataSources;

	// ========================= SETUP METHODS =============================

	/**
	 * Shut everything down. Otherwise some camel exceptions are thrown.
	 * 
	 * @throws Exception
	 */
	// @After // Otherwise camel context is stopped after one test method, routes are
	// stopped, and subsequent methods cannot use them
	public void shutdownCamel() throws Exception
	{
		try
		{
			camelContext.stop();
		}
		catch (final Exception ignored)
		{
			// Ignore
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param endpoint
	 * @param index
	 * @param body
	 */
	protected static void assertGroupedExchangeBodiesEqual(final MockEndpoint endpoint,
			final int index, final String... body)
	{
		final List<Exchange> group = CamelUtil.getGroupedExchangeList(endpoint, index);
		assertEquals(Arrays.asList(body), CamelUtil.getExchangeInBodies(group));
	}

	/**
	 * @param expectedQueryId
	 * @param actualExchange
	 */
	protected static final void assertQueryIdEquals(final String expectedQueryId,
			final Exchange actualExchange)
	{
		assertEquals(expectedQueryId, getHeader(actualExchange.getIn(),
				MessageHeader.COMMAND_ID));
	}

	/**
	 * @param expectedQueryContext
	 * @param actualExchange
	 */
	protected static final void assertQueryIdEquals(
			final HasQueryIdentifier<String> expectedQueryContext,
			final Exchange actualExchange)
	{
		assertQueryIdEquals(expectedQueryContext.getQueryId(), actualExchange);
	}

	/**
	 * Process a message by all data sources and return their results. Do not count on the
	 * order of list elements returned from this method.
	 * 
	 * @param inputString
	 *            input message
	 * @return data source processing results
	 */
	protected final List<String> getDataSourceProcessingResultsInAnyOrder(
			final String inputString)
	{
		final List<String> processedMessages = CollectionUtil.newList();
		processedMessages.add(dataSource1.process(inputString));
		processedMessages.add(dataSource2.process(inputString));
		return processedMessages;
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	protected final int getNumDataSources()
	{
		return dataSources.size();
	}
}
