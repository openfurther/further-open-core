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
package edu.utah.further.core.data.hibernate.query;

import java.util.List;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchType;

/**
 * Handles the conversion from SearchQuery to Hibernate Criteria which involves
 * projections in the Hibernate Criteria. A query can be a mix of restrictions, added in
 * {@link CriterionBuilderHibernateImpl} and a mix of projections.
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
 * @version Oct 30, 2009
 */
@Implementation
public final class ProjectionFactoryHibernateImpl implements Builder<ProjectionList>
{
	// ========================= FIELDS =================================

	/**
	 * The {@link ProjectionList} to which projections are added.
	 */
	private final ProjectionList projectionList = Projections.projectionList();

	/**
	 * Useful class which contains data about the root entity
	 */
	@SuppressWarnings("unused")
	// Will be useful in the future here
	private final ClassMetadata classMetadata;

	// ========================= CONSTRUCTORS =================================

	/**
	 * @param criterion
	 */
	public ProjectionFactoryHibernateImpl(final SearchCriterion criterion,
			final ClassMetadata classMetadata)
	{
		super();
		this.classMetadata = classMetadata;
		visit(criterion);
	}

	// ========================= IMPL: Builder<ProjectionList> =================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.misc.Builder#build()
	 */
	@Override
	public ProjectionList build()
	{
		return (projectionList.getLength() == 0) ? null : projectionList;
	}

	// ========================= PRIVATE METHODS =================================

	/**
	 * Visits the {@link SearchCriterion} tree in post-traversal order.
	 *
	 * @param searchCriterion
	 */
	private void visit(final SearchCriterion searchCriterion)
	{
		// Visit my children first
		final List<SearchCriterion> children = searchCriterion.getCriteria();
		if (children != null)
		{
			for (final SearchCriterion criterion : children)
			{
				visit(criterion);
			}
		}

		// Now handle me
		handle(searchCriterion.getSearchType(), searchCriterion);
	}

	/**
	 * A helper method to add a projection on behalf of a leaf criterion.
	 *
	 * @param searchType
	 * @param criterion
	 */
	private void handle(final SearchType searchType, final SearchCriterion criterion)
	{
		switch (searchType)
		{
			// case COUNT:
			// {
			// visitContainsCount(criterion);
			// break;
			// }

			default:
			{
				// Nothing to do
				break;
			}
		}
	}

	// /**
	// * Visits an {@link SearchType#COUNT} search type and adds a custom 'group by
	// having'
	// * projection. When using this, only the identifier of the entity is selected
	// because
	// * the group by is done by the identifier.
	// * <p>
	// * Note that this projection is to be applied to the sub-select of the COUNT
	// * criterion.
	// *
	// * @param criterion
	// */
	// private void visitContainsCount(final SearchCriterion criterion)
	// {
	// final boolean distinct = ((Boolean) criterion.getParameter(0)).booleanValue();
	// final Relation relation = (Relation) criterion.getParameter(1);
	// final Object value = criterion.getParameter(2);
	// final String idPropertyName = classMetadata.getIdentifierPropertyName();
	// projectionList.add(CustomProjections.groupByHaving(idPropertyName,
	// CustomProjections.countProjection(idPropertyName, distinct), relation,
	// value));
	// }

}
