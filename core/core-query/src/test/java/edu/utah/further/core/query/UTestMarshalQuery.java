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
package edu.utah.further.core.query;

import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.query.domain.SearchCriteria.collection;
import static edu.utah.further.core.query.domain.SearchCriteria.count;
import static edu.utah.further.core.query.domain.SearchCriteria.junction;
import static edu.utah.further.core.query.domain.SearchCriteria.queryBuilder;
import static edu.utah.further.core.query.domain.SearchCriteria.simpleExpression;
import static edu.utah.further.core.query.domain.SearchCriteria.stringExpression;
import static edu.utah.further.core.query.domain.SearchType.CONJUNCTION;
import static edu.utah.further.core.query.domain.SearchType.DISJUNCTION;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import com.sun.xml.bind.api.JAXBRIContext;

import edu.utah.further.core.api.lang.CoreUtil;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.core.query.domain.MatchType;
import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchType;
import edu.utah.further.core.query.domain.SortType;
import edu.utah.further.core.query.fixture.CoreQueryFixture;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.core.xml.jaxb.JaxbConfigurationFactoryBean;

/**
 * A unit test of marshalling and unmarshalling {@link SearchQuery} to XML using JAXB.
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
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings("boxing")
public final class UTestMarshalQuery extends CoreQueryFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestMarshalQuery.class);

	/**
	 * Test file name.
	 */
	private static final String QUERY_DISJUNCTION_XML = "query-disjunction.xml";

	/**
	 * Test file name.
	 */
	private static final String QUERY_COUNT_XML = "query-count.xml";

	/**
	 * JAXB Configuration.
	 */
	private static final Map<String, Object> CORE_QUERY_JAXB_CONFIG = JaxbConfigurationFactoryBean.DEFAULT_JAXB_CONFIG;

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	// ========================= SETUP METHODS =============================

	@Before
	public void setup()
	{
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
		
		/* Override the base */
		CORE_QUERY_JAXB_CONFIG.put(JAXBRIContext.DEFAULT_NAMESPACE_REMAP,
				XmlNamespace.CORE_QUERY);
	}

	// ========================= TESTING METHODS ===========================

	/**
	 * Marshal a simple criterion that is not wrapped by a {@link SearchQuery} object.
	 */
	@Test
	public void marshalRawSimpleCriterion() throws Exception
	{
		final SearchCriterion criterion = simpleExpression(Relation.EQ, "age", 40);
		xmlService.marshalAndPrint(criterion);
	}

	/**
	 * Marshal a simple query object.
	 */
	@Test
	public void marshalQuerySimpleCriterion() throws Exception
	{
		final SearchCriterion criterion = simpleExpression(Relation.EQ, "age", 40);
		final SearchQuery query = queryBuilder(criterion)
				.setFirstResult(1)
				.setMaxResults(2)
				.setRootObject("Person")
				.build();
		xmlService.marshalAndPrint(query);
	}

	/**
	 * Marshal a complex query object.
	 */
	@Test
	public void marshalQueryDisjunction() throws Exception
	{
		final SearchCriterion orCrit = junction(DISJUNCTION);
		orCrit.addCriterion(simpleExpression(Relation.EQ, "age", 40));
		final SearchCriterion andCrit = junction(CONJUNCTION);
		andCrit.addCriterion(simpleExpression(Relation.GT, "age", 50));
		andCrit.addCriterion(simpleExpression(Relation.LT, "age", 60));
		andCrit.addCriterion(stringExpression(SearchType.LIKE, "comment", "keyword",
				MatchType.CONTAINS));
		orCrit.addCriterion(andCrit);

		final SearchQuery query = queryBuilder(orCrit)
				.setFirstResult(1)
				.setMaxResults(2)
				.setRootObject("Person")
				.addSortCriterion(SearchCriteria.sort("age", SortType.ASCENDING))
				.build();
		final String marshalled = xmlService.marshal(query);
		
		final String expected = IoUtil.getInputStreamAsString(CoreUtil
				.getResourceAsStream(QUERY_DISJUNCTION_XML));
		
		final DetailedDiff diff = new DetailedDiff(new Diff(expected, marshalled));
		assertTrue("XML is different" + diff.getAllDifferences(), diff.similar());
	}

	/**
	 * Unmarshal a complex query object.
	 */
	@Test
	public void unmarshalQueryDisjunction() throws Exception
	{
		final SearchQuery result = xmlService.unmarshalResource(QUERY_DISJUNCTION_XML,
				SearchQuery.class);
		final SearchCriterion rootCriterion = result.getRootCriterion();
		assertEquals(DISJUNCTION, rootCriterion.getSearchType());
		assertEquals(2, rootCriterion.getCriteria().size());
		assertEquals("Person", result.getRootObjectName());

		if (log.isDebugEnabled())
		{
			log.debug(EMPTY_STRING
					+ rootCriterion.getCriteria().get(0).getParameters().size());
		}

		for (final Object object : rootCriterion.getCriteria().get(0).getParameters())
		{
			assertNotNull(object);
		}
		if (log.isDebugEnabled())
		{
			log.debug(result.toString());
		}

	}

	/**
	 * Unmarshal an {@link SearchType#IN}-criterion query.
	 */
	@Test
	public void marshalQueryIn() throws JAXBException
	{
		final SearchCriterion criterion = collection(SearchType.IN, "cool", "Value 1",
				"Value 2");
		final String marshalledString = xmlService.marshal(criterion);
		assertThat(marshalledString, containsString("Value 1"));
		assertThat(marshalledString, containsString("Value 2"));
		assertThat(marshalledString, containsString("IN"));
	}

	/**
	 * Unmarshal an {@link SearchType#COUNT}-criterion query.
	 * 
	 * @throws JAXBException
	 * @throws IOException 
	 * @throws SAXException 
	 */
	@Test
	public void marshalQueryCount() throws JAXBException, SAXException, IOException
	{
		final SearchCriterion criterion = count(Relation.EQ, 1234,
				queryBuilder(simpleExpression(Relation.GT, "property", "value"))
						.setRootObject("Person")
						.addAlias("Observation", "Observation", "observations")
						.build());

		final SearchQuery query = queryBuilder(criterion).setRootObject("Person").build();

		final String marshalled = xmlService.marshal(query);
		
		final String expected = IoUtil.getInputStreamAsString(CoreUtil
				.getResourceAsStream(QUERY_COUNT_XML));
		
		final DetailedDiff diff = new DetailedDiff(new Diff(expected, marshalled));
		assertTrue("XML is different" + diff.getAllDifferences(), diff.similar());
	}

	// ========================= PRIVATE METHODS ===========================
}
