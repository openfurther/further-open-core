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
package edu.utah.further.fqe.ds.api.to;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.fixture.FqeDsApiFixture;

/**
 * Tests relating to marshalling {@link StatusesMetaDataToImpl}
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
 * @version Mar 22, 2010
 */
public final class UTestMarshalStatusesMetaData extends FqeDsApiFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestMarshalQueryContext.class);

	/**
	 * XML file with expected marshaled query statuses.
	 */
	private static final String QUERY_STATUSES_XML = "query-statuses.xml";

	// ========================= FIELDS ====================================

	/**
	 * A fixed date to set in statuses.
	 */
	private Date curr;

	// ========================= SETUP METHODS =============================

	/**
	 * Fix system time.
	 */
	@Before
	public void fixSystemTime()
	{
		TimeService.fixSystemTime(10000);
		curr = TimeService.getDate();
	}

	/**
	 * Important: remember to restore system time.
	 */
	@After
	public void restoreSystemTime()
	{
		TimeService.reset();
	}

	// ========================= METHODS ===================================

	/**
	 * Test marshaling the wrapper class for multiple statuses.
	 *
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public void marshalStatuses() throws JAXBException, IOException
	{
		final StatusesMetaDataToImpl statuses = new StatusesMetaDataToImpl();
		final List<StatusMetaDataToImpl> statusesList = Arrays.asList(
				newStatusMetaData(1L, "Status 1", "DS1"),
				newStatusMetaData(2L, "Status 2", "DS2"));
		statuses.setStatuses(statusesList);
		marshallingTest(QUERY_STATUSES_XML, statuses);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param queryContextId
	 * @param status
	 * @param dataSourceId
	 * @return
	 */
	private StatusMetaDataToImpl newStatusMetaData(final long queryContextId,
			final String status, final String dataSourceId)
	{
		final StatusMetaDataToImpl statusMetadata = new StatusMetaDataToImpl();
		statusMetadata.setStatus(status);
		statusMetadata.setStatusDate(curr);
		statusMetadata.setDataSourceId(dataSourceId);
		final QueryContext qc = QueryContextToImpl.newInstance(queryContextId);
		statusMetadata.setQueryContext(qc);
		return statusMetadata;
	}
}
