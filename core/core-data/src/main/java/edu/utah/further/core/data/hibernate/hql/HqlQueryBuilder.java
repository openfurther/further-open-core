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
package edu.utah.further.core.data.hibernate.hql;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * A builder of an HQL query from string snippets that represent the main query and
 * selection criteria.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 28, 2009
 */
@Api
public interface HqlQueryBuilder
{
	// ========================= METHODS ===================================

	/**
	 * Append the main query.
	 *
	 * @param query
	 *            HQL query string
	 * @return this object, for method call chaining
	 */
	HqlQueryBuilder appendQuery(String query);

	/**
	 * Append a <code>WHERE</code>-clause criterion.
	 *
	 * @param criterion
	 *            <code>WHERE</code>-clause criterion
	 * @return this object, for method call chaining
	 */
	HqlQueryBuilder appendCriterion(String criterion);

	/**
	 * Add a parametric entity entry.
	 *
	 * @param name
	 *            entry name in the query
	 * @param object
	 *            persistent entity
	 * @return this object, for method call chaining
	 */
	HqlQueryBuilder setEntity(String name, PersistentEntity<?> object);

	/**
	 * Produce the HQL query.
	 *
	 * @param session
	 *            Hibernate session
	 * @return HQL query
	 */
	Query toHqlQuery(Session session);
}
