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
package edu.utah.further.fqe.mpi.impl.service;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.fqe.mpi.api.service.IdentifierService;
import edu.utah.further.fqe.mpi.impl.domain.IdentifierEntity;

/**
 * IdentifierService related tests
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
 * @version Oct 30, 2013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/fqe-mpi-impl-test-context-annotation.xml",
		"/fqe-mpi-impl-test-context-identifier-datasource.xml" })
public class UTestIdentifierService
{
	/**
	 * For persisting some test identifiers.
	 */
	@Autowired
	private Dao identifierDao;

	/**
	 * The service to test
	 */
	@Autowired
	IdentifierService identifierService;

	@Before
	public void setup()
	{
		final IdentifierEntity idOne = new IdentifierEntity();
		idOne.setCommonId(new Long(1));
		idOne.setQueryId("A");
		idOne.setVirtualId(new Long(1));

		final IdentifierEntity idTwo = new IdentifierEntity();
		idTwo.setQueryId("A");
		idTwo.setCommonId(new Long(2));
		idTwo.setVirtualId(new Long(2));

		final IdentifierEntity idThree = new IdentifierEntity();
		idThree.setCommonId(new Long(1));
		idThree.setQueryId("B");
		idThree.setVirtualId(new Long(3));

		final IdentifierEntity idFour = new IdentifierEntity();
		idFour.setCommonId(new Long(2));
		idFour.setQueryId("B");
		idFour.setVirtualId(new Long(4));

		identifierDao.save(idOne);
		identifierDao.save(idTwo);
		identifierDao.save(idThree);
		identifierDao.save(idFour);
	}

	/**
	 * Results expected like: {1=[1, 3], 2=[2, 4]}
	 */
	@Test
	public void getCommonToVirtualIdMap()
	{
		final Map<Long, Set<Long>> mapping = identifierService.getCommonIdToVirtualIdMap(
				Arrays.asList("A", "B"), true);

		assertTrue(mapping.get(new Long(1)).size() == 2);
		assertTrue(mapping.get(new Long(2)).size() == 2);
	}
}
