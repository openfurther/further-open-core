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
package edu.utah.further.core.data.hibernate.adapter;

import static edu.utah.further.core.api.message.Messages.unsupportedMessage;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;

/**
 * A base class of Hibernate criteria adapters.
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
 * @version Jul 21, 2010
 */
abstract class AbstractCriteriaAdapter implements GenericCriteria
{
	// ========================= FIELDS ====================================

	/**
	 * Criteria type to delegate to.
	 */
	private final CriteriaType type;

	// ========================= METHODS ===================================

	/**
	 * @param type
	 */
	AbstractCriteriaAdapter(final CriteriaType type)
	{
		super();
		this.type = type;
	}

	// ========================= IMPL: CriteriaBase ========================

	/**
	 * Return the criteria type.
	 *
	 * @return the criteria type
	 * @see edu.utah.further.core.data.hibernate.adapter.GenericCriteria#getType()
	 */
	@Override
	public CriteriaType getType()
	{
		return type;
	}

	/**
	 * Return the underlying Hibernate Criteria object, if this object's type is
	 * {@value CriteriaType#CRITERIA}. Otherwise, returns <code>null</code>.
	 *
	 * @return underlying Hibernate Criteria object
	 */
	@Override
	public Criteria getHibernateCriteria()
	{
		throw new UnsupportedOperationException(
				unsupportedMessage("getHibernateCriteria()"));
	}

	/**
	 * Return the underlying Hibernate detached Criteria object, if this object's type is
	 * {@value CriteriaType#SUB_CRITERIA}. Otherwise, returns <code>null</code>.
	 *
	 * @return underlying Hibernate detached Criteria object
	 */
	@Override
	public DetachedCriteria getHibernateDetachedCriteria()
	{
		throw new UnsupportedOperationException(
				unsupportedMessage("getHibernateDetachedCriteria()"));
	}
}
