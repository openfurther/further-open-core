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
package edu.utah.further.dts.impl.service;

import com.apelon.dts.client.association.AssociationQuery;
import com.apelon.dts.client.concept.NavQuery;
import com.apelon.dts.client.concept.SearchQuery;
import com.apelon.dts.client.namespace.NamespaceQuery;
import com.apelon.dts.tqlutil.TQL;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A mother object for queries and other useful Apelon DTS objects. Must catch all
 * exceptions thrown from the DTS API layer and propagate them to the client code by
 * throwing appropriate {@link ApplicationException} s.
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
 * @version Nov 6, 2008
 */
@Api
public interface DtsObjectFactory
{
	// ========================= METHODS ===================================

	/**
	 * Create a new namespace query object.
	 *
	 * @return a new namespace query instance
	 */
	NamespaceQuery createNamespaceQuery();

	/**
	 * Create a new search query object.
	 *
	 * @return a new search query instance
	 */
	SearchQuery createSearchQuery();

	/**
	 * Create a new navigation query object.
	 *
	 * @return a new navigation query instance
	 */
	NavQuery createNavQuery();

	/**
	 * Create a new association query object.
	 *
	 * @return a new association query instance
	 */
	AssociationQuery createAssociationQuery();

	/**
	 * Create a new TQL query object.
	 *
	 * @return a new TQL query instance
	 */
	TQL createTqlQuery();
}
