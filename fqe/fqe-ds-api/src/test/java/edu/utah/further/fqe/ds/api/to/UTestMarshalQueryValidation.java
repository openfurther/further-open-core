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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static org.junit.Assert.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

import edu.utah.further.core.query.domain.SearchQueryTo;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.fixture.FqeDsApiFixture;

/**
 * Marshals a full {@link SearchQueryTo} and a full {@link QueryContextTo} using
 * validation
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
 * @version May 20, 2010
 */
public final class UTestMarshalQueryValidation extends FqeDsApiFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestMarshalQueryValidation.class);

	/**
	 * SearchQuery schema to validate against.
	 */
	private static final String SEARCHQUERY_VALIDATION_XSD = "edu/utah/further/core/query/schema/further-searchquery-1.0.xsd";

	/**
	 * QueryContext schema to validate against.
	 */
	private static final String QUERYCONTEXT_VALIDATION_XSD = "edu/utah/further/fqe/ds/api/schema/further-querycontext-1.0.xsd";

	/**
	 * Sample query XML.
	 */
	private static final String QUERY_XML = "query-context-with-plan.xml";

	// ========================= DEPENDENCIES ==============================

	/**
	 * The QueryContext containing the query
	 */
	private QueryContext queryContext;

	// ========================= SETUP METHODS =============================

	/**
	 * Setup methods
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Before
	public void setup() throws JAXBException, IOException
	{
		queryContext = xmlService.unmarshalResource(QUERY_XML, QueryContextTo.class);
	}

	// ========================= TEST METHODS ==============================

	/**
	 * Marshals a search query with validation.
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void marshalSearchQueryWithValidation() throws JAXBException, SAXException,
			IOException
	{
		final Resource schema = new ClassPathResource(SEARCHQUERY_VALIDATION_XSD);
		// Only get the first query
		final String result = xmlService.marshal(queryContext.getQuery(), xmlService
				.options()
				.addClass(SearchQueryTo.class)
				.buildContext()
				.setValidationSchema(new StreamSource(schema.getInputStream())));
		if (log.isDebugEnabled())
		{
			log.debug(result);
		}
		assertNotNull(result);
	}

	/**
	 * Marshals a query context with validation.
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void marshalQueryContextWithValidation() throws JAXBException, SAXException,
			IOException
	{
		final Resource schema2 = new ClassPathResource(SEARCHQUERY_VALIDATION_XSD);
		final Resource schema1 = new ClassPathResource(QUERYCONTEXT_VALIDATION_XSD);
		List<Source> sources = newList();
		sources.add(new StreamSource(schema2.getInputStream()));
		sources.add(new StreamSource(schema1.getInputStream()));
		final String result = xmlService.marshal(queryContext, xmlService
				.options()
				.addClass(QueryContextTo.class)
				.addClass(SearchQueryTo.class)
				.buildContext()
				.setValidationSchema(sources));
		if (log.isDebugEnabled())
		{
			log.debug(result);
		}
		assertNotNull(result);
	}
}
