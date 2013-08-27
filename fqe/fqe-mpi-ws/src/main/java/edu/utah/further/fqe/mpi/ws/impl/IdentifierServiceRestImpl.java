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
package edu.utah.further.fqe.mpi.ws.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.fqe.mpi.api.IdentifierService;
import edu.utah.further.fqe.mpi.impl.service.IdentifierRequestImpl;
import edu.utah.further.fqe.mpi.ws.api.IdentifierServiceRest;
import edu.utah.further.fqe.mpi.ws.api.to.IdentifierTo;

/**
 * REST implementation of identifier service
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 8, 2010
 */
@Service("identifierServiceRest")
public final class IdentifierServiceRestImpl implements IdentifierServiceRest
{

	/**
	 * The identifier service
	 */
	@Autowired
	private IdentifierService identifierService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.ws.IdentifierServiceRest#generateNew()
	 */
	@Override
	public IdentifierTo generateNew()
	{
		return new IdentifierTo(identifierService.generateNewId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.mpi.ws.api.IdentifierServiceRest#generateId(java.lang.String,
	 * java.lang.String, java.lang.Long, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public IdentifierTo generateId(final String name, final String attr,
			final Long srcNamespace, final String srcName, final String srcAttr,
			final String srcId, final String queryId)
	{
		final IdentifierRequestImpl idRequest = new IdentifierRequestImpl();
		idRequest.setName(name);
		idRequest.setAttr(attr);
		idRequest.setSourceNamespaceId(srcNamespace.longValue());
		idRequest.setSourceName(srcName);
		idRequest.setSourceAttr(srcAttr);
		idRequest.setSourceId(srcId);
		idRequest.setQueryId(queryId);
		return new IdentifierTo(identifierService.generateId(idRequest));
	}

}
