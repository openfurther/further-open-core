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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.ExportContext;
import edu.utah.further.fqe.ds.api.fixture.FqeDsApiFixture;

/**
 * Marshal and unmarshal the export context
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
 * @version Sep 28, 2012
 */
public class UTestMarshalExportContextTo extends FqeDsApiFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestMarshalExportContextTo.class);

	/**
	 * Test file for asserting correct marshalling
	 */
	private static final String EXPORT_CONTEXT_TEST_FILE = "export-context.xml";

	// ========================= FIELDS ==============================

	private ExportContext exportContext;

	@Before
	public void setup()
	{
		final SearchCriterion criterion = SearchCriteria.simpleExpression(Relation.EQ,
				"property", "value");
		final SearchQuery searchQuery = SearchCriteria.query(criterion, "Root");

		@SuppressWarnings("hiding")
		final ExportContext exportContext = new ExportContextTo();
		exportContext.setQueryId(new Long(1));
		exportContext.setUserId("1234");
		exportContext.addFilter(searchQuery);

		this.exportContext = exportContext;
	}

	// ========================= TEST METHODS ==============================

	/**
	 * Tests marshalling an export context
	 * 
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public void marshall() throws JAXBException, IOException
	{
		final String marshalled = xmlService.marshal(exportContext);
		assertThat(marshalled, containsString(exportContext.getUserId()));

		final List<Object> parameters = (List<Object>) exportContext
				.getFilters()
				.get(0)
				.getRootCriterion()
				.getParameters();
		assertThat(marshalled, containsString(parameters.get(0).toString()));
		assertThat(marshalled, containsString(parameters.get(1).toString()));
		assertThat(marshalled, containsString(parameters.get(2).toString()));
	}

	/**
	 * Tests unmarshalling an export context
	 * 
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public void unmarshall() throws JAXBException, IOException
	{
		final ExportContext unmarshalledExportContext = xmlService.unmarshalResource(
				EXPORT_CONTEXT_TEST_FILE, ExportContextTo.class);

		assertThat(unmarshalledExportContext.getQueryId(), is(exportContext.getQueryId()));
		assertThat(unmarshalledExportContext.getUserId(), is(exportContext.getUserId()));
		final List<SearchQuery> expectedQueries = unmarshalledExportContext.getFilters();
		final List<SearchQuery> queries = unmarshalledExportContext.getFilters();
		// Only expect 1
		assertThat(new Integer(expectedQueries.size()), is(new Integer(1)));
		assertThat(expectedQueries.get(0).getRootCriterion(), is(queries
				.get(0)
				.getRootCriterion()));
	}
}
