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
package edu.utah.further.ds.impl.executor.db.hibernate.criteria;

import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.ds.impl.executor.db.hibernate.HibernateExecReq;

/**
 * An executor which takes a Hibernate {@link GenericCriteria} from a previous
 * {@link RequestProcessor} and applies a result transformer to programmatically post
 * filter of result. Results are filtered by distinct root entity. Note that for large
 * results sets with large joins of collections, this method of returning the distinct
 * root entity is much slower in the beginning versus a true SQL distinct by root entity
 * ID and then an additional execution to load the root entity (assuming relationships are
 * lazy) and then additionaly queries to select specific collections. (see
 * {@link HibernateDistinctIdExecutor} and {@link HibernateLoadByIdExecutor})
 *
 * Note that this {@link RequestProcessor} does not execute the Hibernate Criteria, see
 * {@link HibernateCriteriaListExecutor}
 *
 * This executor expects the following to be set (see {@link HibernateExecReq}) {@literal
 * <ul>
 * <li>Criteria</li> * </ul> * }
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
 * @version Oct 1, 2009
 */
@Service("hibernateDistinctRootExecutor")
public class HibernateDistinctRootExecutor extends AbstractNonDelegatingUtilityProcessor
{
	// ========================= IMPLEMENTATION: RequestHandler ==============

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.chain.AbstractRequestHandler#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		final HibernateExecReq execReq = new HibernateExecReq(request);
		final GenericCriteria criteria = execReq.getResult();
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		execReq.setResult(criteria);
		return false;
	}

}
