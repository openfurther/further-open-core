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
package edu.utah.further.fqe.impl.service.query;

import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;
import edu.utah.further.ds.further.model.impl.domain.Person;
import edu.utah.further.fqe.api.service.query.AggregationService;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResults;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.service.results.ResultDataService;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.ds.api.to.ResultContextTo;
import edu.utah.further.fqe.ds.api.to.ResultContextToImpl;
import edu.utah.further.fqe.impl.domain.QueryContextEntity;
import edu.utah.further.fqe.impl.fixture.FqeImplUtestFixture;
import edu.utah.further.fqe.mpi.api.service.IdentifierService;

/**
 * ...
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Nov 1, 2013
 */
public class UTestAggregationService extends FqeImplUtestFixture
{
	/**
	 * Actual aggregation service that we are testing.
	 */
	@Autowired
	private AggregationService aggregationService;

	/**
	 * Mock identifier service
	 */
	@Autowired
	private IdentifierService identifierService;

	/**
	 * Mock result data service
	 */
	@Autowired
	private ResultDataService resultDataService;

	/**
	 * Actual dao tied to in memory db
	 */
	@Autowired
	private Dao dao;

	/**
	 * A parent/federated {@link QueryContext} that will be setup at the beginning of the
	 * test and passed to the aggregationservice we're testing.
	 */
	private QueryContext parent;

	/**
	 * Setup all the mocks
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setup()
	{
		final ResultContextTo resultContext = new ResultContextToImpl();
		resultContext.setRootEntityClass(Person.class.getCanonicalName());

		parent = QueryContextToImpl.newInstanceWithExecutionId();

		final Long id = dao.save(QueryContextEntity.newCopy(parent));
		parent = dao.getById(QueryContextEntity.class, id);

		final QueryContext childOne = QueryContextToImpl
				.newChildInstance(QueryContextToImpl.newCopy(parent));
		childOne.setResultContext(resultContext);
		final QueryContext childTwo = QueryContextToImpl
				.newChildInstance(QueryContextToImpl.newCopy(parent));
		childTwo.setResultContext(resultContext);

		dao.save(QueryContextEntity.newCopy(childOne));
		dao.save(QueryContextEntity.newCopy(childTwo));

		// the SUM, including duplications, is ids (1,2,3)
		final List<Long> idsInSum = Arrays.asList(new Long(1), new Long(2), new Long(3));

		expect(identifierService.getVirtualIdentifiers(anyObject(List.class)))
				.andStubReturn(idsInSum);

		// ids (1,3) are the same and have a common identifier (1337)
		final Map<Long, Set<Long>> commonToVirtual = new HashMap<>();
		final Set<Long> virtuals = new HashSet<>();
		virtuals.add(new Long(1));
		virtuals.add(new Long(3));
		commonToVirtual.put(new Long(1337), virtuals);

		expect(
				identifierService.getCommonIdToVirtualIdMap(anyObject(List.class),
						anyBoolean())).andStubReturn(commonToVirtual);

		// id (2) is unresolved
		final List<Long> unresolved = Arrays.asList(new Long(2));

		expect(identifierService.getUnresolvedVirtualIdentifiers(anyObject(List.class)))
				.andStubReturn(unresolved);

		final List<Map<String, Object>> results = new ArrayList<>();

		final Map<String, Object> rowOne = new HashMap<>();
		rowOne.put("fieldName", "MockOne");
		rowOne.put("fieldCount", new Long(1111));

		final Map<String, Object> rowTwo = new HashMap<>();
		rowTwo.put("fieldName", "MockTwo");
		rowTwo.put("fieldCount", new Long(2222));

		results.add(rowOne);
		results.add(rowTwo);

		expect(
				resultDataService.getQueryResults(anyObject(String.class),
						anyObject(List.class))).andStubReturn(results);

		replay(identifierService);
		replay(resultDataService);
	}

	/**
	 * Stub several dependencies and generate an expected aggregated result / histogram
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void generate() throws JAXBException
	{
		final AggregatedResults aggregatedResults = aggregationService
				.generatedAggregatedResults(parent);
		final XmlService xmlService = new XmlServiceImpl();
		final String result = xmlService.marshal(aggregatedResults);
		System.out.println(result);
	}
}
