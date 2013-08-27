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
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.exception.BusinessRuleException;

/**
 * A factory of Hibernate criteria API adapters.
 * <p>
 * ----------------------------------------------------------------------------
 * -------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * ----------------------------------------------------------------------------
 * -------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jul 21, 2010
 */
public final class GenericCriteriaFactory {
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Create an appropriate new Hibernate criteria instance, for the given
	 * entity class, or a superclass of an entity class, with the given alias.
	 * <p>
	 * This method may be replaced by a Builder pattern in the future to support
	 * more construction argument combinations.
	 * 
	 * @param criteriaType
	 *            type of criteria to instantiate
	 * @param persistentClass
	 *            a class, which is persistent, or has persistent subclasses
	 * @param session
	 *            (optional) Hibernate session, if criteria should be attached
	 *            to one
	 * @param alias
	 *            (optional) entity alias
	 * @return criteria adapter instance
	 */
	public static GenericCriteria criteria(final CriteriaType criteriaType,
			final Class<? extends PersistentEntity<?>> clazz,
			final Object... args) {
		final Session session = (args.length < 1) ? null : (Session) args[0];
		final String alias = (args.length < 2) ? null : (String) args[1];
		switch (criteriaType) {
		case CRITERIA: {
			if (session == null) {
				throw new IllegalStateException(
						"Can't create CRITERIA, session is null!");
			}
			final Criteria hibernateCriteria = (alias == null) ? session
					.createCriteria(clazz) : session.createCriteria(clazz,
					alias);
			return newAdapter(hibernateCriteria);
		}

		case SUB_CRITERIA: {
			final DetachedCriteria hibernateDetachedCriteria = (alias == null) ? DetachedCriteria
					.forClass(clazz) : DetachedCriteria.forClass(clazz, alias);
			return newAdapter(hibernateDetachedCriteria);
		}

		default: {
			throw new BusinessRuleException(unsupportedMessage(criteriaType));
		}
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param delegate
	 * @return
	 */
	private static GenericCriteria newAdapter(final Criteria delegate) {
		return new CriteriaAdapter(delegate);
	}

	/**
	 * @param delegate
	 * @return
	 */
	private static GenericCriteria newAdapter(final DetachedCriteria delegate) {
		return new DetachedCriteriaAdapter(delegate);
	}
}
