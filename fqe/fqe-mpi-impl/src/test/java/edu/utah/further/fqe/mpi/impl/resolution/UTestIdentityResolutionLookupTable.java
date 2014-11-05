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
package edu.utah.further.fqe.mpi.impl.resolution;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.mpi.api.IdentityResolutionStrategy;
import edu.utah.further.fqe.mpi.impl.domain.IdentifierEntity;
import edu.utah.further.fqe.mpi.impl.domain.LookupEntity;

/**
 * Test resolving common query identifiers across data sources using a lookup table.
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
 * @version Oct 23, 2013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/fqe-mpi-impl-test-context-annotation.xml",
		"/fqe-mpi-impl-test-context-identifier-datasource.xml",
		"/fqe-mpi-impl-test-context-lookup-datasource.xml" })
public class UTestIdentityResolutionLookupTable
{
	/**
	 * For persisting some test identifiers.
	 */
	@Autowired
	private Dao identifierDao;

	/**
	 * For persisting some test lookup data.
	 */
	@Autowired
	private Dao lookupDao;

	/**
	 * The resolution strategy we're testing.
	 */
	@Autowired
	private IdentityResolutionStrategy identityResolutionLookupTable;

	/**
	 * Test query 1
	 */
	private QueryContext queryContextOne;

	/**
	 * Test query 2
	 */
	private QueryContext queryContextTwo;

	/**
	 * Setup a mock set of identifiers across two queries along with a lookup table that
	 * matches identifiers across queries.
	 */
	@Before
	public void setup()
	{
		final QueryContext parent = QueryContextToImpl.newInstance(1);

		final QueryContext queryContext1 = QueryContextToImpl
				.newInstanceWithExecutionId();
		queryContext1.setTargetNamespaceId(new Long(1));
		queryContext1.setParent(parent);

		final QueryContext queryContext2 = QueryContextToImpl
				.newInstanceWithExecutionId();
		queryContext2.setTargetNamespaceId(new Long(2));
		queryContext2.setParent(parent);

		this.queryContextOne = queryContext1;
		this.queryContextTwo = queryContext2;

		final IdentifierEntity idOne = new IdentifierEntity();
		idOne.setSourceId("1234");
		idOne.setQueryId(queryContext1.getExecutionId());
		idOne.setVirtualId(new Long(1));

		final IdentifierEntity idTwo = new IdentifierEntity();
		idTwo.setSourceId("4567");
		idTwo.setQueryId(queryContext1.getExecutionId());
		idTwo.setVirtualId(new Long(2));

		final IdentifierEntity idThree = new IdentifierEntity();
		idThree.setSourceId("9876");
		idThree.setQueryId(queryContext2.getExecutionId());
		idThree.setVirtualId(new Long(3));

		final IdentifierEntity idFour = new IdentifierEntity();
		idFour.setSourceId("7890");
		idFour.setQueryId(queryContext2.getExecutionId());
		idFour.setVirtualId(new Long(4));

		identifierDao.save(idOne);
		identifierDao.save(idTwo);
		identifierDao.save(idThree);
		identifierDao.save(idFour);

		// 1234 from namespace 1 == 9876 from namespace 2

		final LookupEntity lookUpOne = new LookupEntity();
		lookUpOne.setId(UUID.randomUUID().toString());
		lookUpOne.setSourceId(new Long(1234));
		lookUpOne.setNamespaceId(new Long(1));
		lookUpOne.setCommonId(new Long(1));

		final LookupEntity lookUpTwo = new LookupEntity();
		lookUpTwo.setId(UUID.randomUUID().toString());
		lookUpTwo.setNamespaceId(new Long(2));
		lookUpTwo.setSourceId(new Long(9876));
		lookUpTwo.setCommonId(new Long(1));

		lookupDao.save(lookUpOne);
		lookupDao.save(lookUpTwo);
	}

	/**
	 * Resolve identifies of two queries using a lookup table strategy.
	 */
	@Test
	public void resolve()
	{
		identityResolutionLookupTable.doIdentityResolution(queryContextOne);
		identityResolutionLookupTable.doIdentityResolution(queryContextTwo);

		final List<IdentifierEntity> identifiers = identifierDao
				.findAll(IdentifierEntity.class);
		for (final IdentifierEntity identifier : identifiers)
		{
			if ("1234".equals(identifier.getSourceId())
					|| "9876".equals(identifier.getSourceId()))
			{
				assertTrue(identifier.getCommonId().equals(new Long(1)));
			}
			else
			{
				assertTrue(identifier.getCommonId() == null);
			}
		}
	}
}
