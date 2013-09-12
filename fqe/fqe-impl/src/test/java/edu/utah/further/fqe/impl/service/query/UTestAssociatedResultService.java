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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.fqe.impl.fixture.FqeImplUtestFixture.newQueryContextEntity;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.service.query.AssociatedResultService;
import edu.utah.further.fqe.mpi.api.IdentifierService;

/**
 * Test the implementation of the associated result service to ensure that parent qc's and
 * child qc's are handled properly.
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
 * @version Mar 26, 2012
 */
public class UTestAssociatedResultService
{
	/**
	 * The {@link AssociatedResultService} to test
	 */
	private final AssociatedResultService associatedResultService = new AssociatedResultServiceImpl();

	/**
	 * Mocked querycontext service
	 */
	private final QueryContextService mockQueryContextService = createMock(QueryContextService.class);

	/**
	 * Mocked result service
	 */
	private final IdentifierService mockIdentifierService = createMock(IdentifierService.class);

	/**
	 * Setup methods
	 */
	@Before
	public void setup()
	{
		// These mocks are explicitly set otherwise we get the wrong QCService from
		// autowiring due to some of the reuse.
		((AssociatedResultServiceImpl) associatedResultService)
				.setQcService(mockQueryContextService);
		((AssociatedResultServiceImpl) associatedResultService)
				.setIdentifierService(mockIdentifierService);
	}

	/**
	 * Test getting an associated result from a parent qc
	 */
	@Test
	public void getAssociatedResultsFromParent()
	{
		final QueryContext parent = newQueryContextEntity();
		expect(mockQueryContextService.findById(new Long(1L))).andReturn(parent);
		final List<QueryContext> children = newList();
		children.add(newQueryContextEntity());
		children.add(newQueryContextEntity());
		expect(mockQueryContextService.findChildren(parent)).andReturn(children);
		expect(mockIdentifierService.translateIds(isA(List.class), eq("UUEDW")))
				.andReturn(CollectionUtil.<Long> newList());
		expect(mockIdentifierService.getVirtualIdentifiers(isA(List.class))).andReturn(
				CollectionUtil.<Long> newList());
		replay(mockQueryContextService, mockIdentifierService);
		associatedResultService.getAssociatedResult(new Long(1L), "UUEDW");
		verify(mockQueryContextService, mockIdentifierService);
	}

	/**
	 * Test getting an associated result from a child qc
	 */
	@Test
	public void getAssociatedResultsFromChild()
	{
		final QueryContext child = newQueryContextEntity();
		child.setParent(newQueryContextEntity());
		expect(mockQueryContextService.findById(new Long(1L))).andReturn(child);
		expect(mockIdentifierService.translateIds(isA(List.class), eq("UUEDW")))
				.andReturn(CollectionUtil.<Long> newList());
		expect(mockIdentifierService.getVirtualIdentifiers(isA(List.class))).andReturn(
				CollectionUtil.<Long> newList());
		replay(mockQueryContextService, mockIdentifierService);
		associatedResultService.getAssociatedResult(new Long(1L), "UUEDW");
		verify(mockQueryContextService, mockIdentifierService);
	}
}
