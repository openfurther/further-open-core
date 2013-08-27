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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.results.ResultService;
import edu.utah.further.fqe.ds.api.service.query.AssociatedResultService;
import edu.utah.further.fqe.mpi.api.IdentifierService;

/**
 * A default implementation of the {@link AssociatedResultServiceImpl}
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
 * @version Mar 22, 2012
 */
@Service("associatedResultService")
@Transactional
public class AssociatedResultServiceImpl implements AssociatedResultService
{

	/**
	 * Gets info about {@link QueryContext}'s
	 */
	@Autowired
	private QueryContextService qcService;

	/**
	 * Retrieves completed results
	 */
	@Autowired
	private ResultService resultService;

	/**
	 * Identifier service for translation federated identifiers to physical data source
	 * identifiers
	 */
	@Autowired
	private IdentifierService identifierService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.ds.api.service.query.AssociatedResultService#getAssociatedResult
	 * (java.lang.Long, java.lang.String)
	 */
	@Override
	public List<Long> getAssociatedResult(final Long queryContextId, final String datasourceId)
	{
		final QueryContext queryContext = qcService.findById(queryContextId);
		final List<String> queryIds = newList();
		if (queryContext.getParent() == null)
		{
			// We're a parent (yay)
			final List<QueryContext> children = qcService.findChildren(queryContext);
			assertNotNull(children);
			for (final QueryContext child : children)
			{
				queryIds.add(child.getExecutionId());
			}
			assertTrue(queryIds.size() > 0);
		}
		else
		{
			// We're a child QC - only get our queryId
			queryIds.add(queryContext.getExecutionId());
		}

		return identifierService.translateIds(resultService.getQueryResultIdentifiers(queryIds),
				datasourceId);
	}

	/**
	 * Return the qcService property.
	 * 
	 * @return the qcService
	 */
	public QueryContextService getQcService()
	{
		return qcService;
	}

	/**
	 * Set a new value for the qcService property.
	 * 
	 * @param qcService
	 *            the qcService to set
	 */
	public void setQcService(final QueryContextService qcService)
	{
		this.qcService = qcService;
	}

	/**
	 * Return the resultService property.
	 * 
	 * @return the resultService
	 */
	public ResultService getResultService()
	{
		return resultService;
	}

	/**
	 * Set a new value for the resultService property.
	 * 
	 * @param resultService
	 *            the resultService to set
	 */
	public void setResultService(final ResultService resultService)
	{
		this.resultService = resultService;
	}

	/**
	 * Return the identifierService property.
	 * 
	 * @return the identifierService
	 */
	public IdentifierService getIdentifierService()
	{
		return identifierService;
	}

	/**
	 * Set a new value for the identifierService property.
	 * 
	 * @param identifierService
	 *            the identifierService to set
	 */
	public void setIdentifierService(final IdentifierService identifierService)
	{
		this.identifierService = identifierService;
	}

}
