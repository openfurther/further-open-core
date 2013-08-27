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

import static edu.utah.further.core.api.constant.Strings.SPACE_STRING;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.PropertyProjection;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;

import edu.utah.further.core.api.message.ValidationUtil;
import edu.utah.further.core.query.domain.Relation;

/**
 * A custom 'Group By HAVING' projection for use with Hibernate Criteria.
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
final class GroupByHavingProjection extends PropertyProjection
{
	// ========================= CONSTANTS =================================

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -3316795021430206470L;

	// ========================= FIELDS ====================================

	/**
	 * The HAVING projection
	 */
	private final Projection projection;

	/**
	 * The property to group by
	 */
	private final String groupByProperty;

	/**
	 * The optional operator.
	 */
	private final Relation op;

	/**
	 * The optional value.
	 */
	private final Object value;

	// ========================= CONSTRUCTORS ==============================

	// /**
	// * A group-by-having projection.
	// *
	// * @param groupByProperty
	// * the group by property
	// * @param projection
	// * the having projection
	// */
	// public GroupByHavingProjection(final String groupByProperty,
	// final Projection projection)
	// {
	// this(groupByProperty, projection, null, null);
	// }

	/**
	 * A group-by-having projection with an operator and value.
	 *
	 * @param groupByProperty
	 *            the group by property
	 * @param projection
	 *            the having projection
	 * @param op
	 *            an operator to apply to the having
	 * @param value
	 *            the value of a having based on the operator
	 */
	public GroupByHavingProjection(final String groupByProperty,
			final Projection projection, final Relation op, final Object value)
	{
		super(groupByProperty, true);

		ValidationUtil.validateNotNull("groupByProperty", groupByProperty);
		ValidationUtil.validateNotNull("projection", projection);
		ValidationUtil.validateNotNull("op", op);

		this.projection = projection;
		this.groupByProperty = groupByProperty;
		this.op = op;
		this.value = value;
	}

	// ========================= IMPL: PropertyProjection ==============================

	/*
	 * (non-Javadoc)
	 *
	 * @see org.hibernate.criterion.PropertyProjection#toSqlString(org.hibernate.Criteria,
	 * int, org.hibernate.criterion.CriteriaQuery)
	 */
	@Override
	public String toSqlString(final Criteria criteria, final int position,
			final CriteriaQuery criteriaQuery) throws HibernateException
	{
		final Type identifierType = criteriaQuery.getIdentifierType(criteria);
		if (identifierType.isComponentType())
		{
			final StringBuffer sb = new StringBuffer();
			appendComponentIds(sb, (ComponentType) identifierType, criteriaQuery,
					criteria, new Integer(position));
			return sb.toString();
		}
		return super.toSqlString(criteria, position, criteriaQuery);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.hibernate.criterion.PropertyProjection#toGroupSqlString(org.hibernate.Criteria,
	 * org.hibernate.criterion.CriteriaQuery)
	 */
	@Override
	public String toGroupSqlString(final Criteria criteria,
			final CriteriaQuery criteriaQuery) throws HibernateException
	{
		final StringBuffer sb = new StringBuffer(50);

		final Type identifierType = criteriaQuery.getIdentifierType(criteria);
		if (identifierType.isComponentType())
		{
			appendComponentIds(sb, (ComponentType) identifierType, criteriaQuery,
					criteria, null);
		}
		else
		{
			sb.append(criteriaQuery.getColumn(criteria, groupByProperty));
		}

		// Append HAVING clause
		sb.append(" having ");
		sb.append("(");
		// Remove the alias
		final String proj = projection.toSqlString(criteria, 0xCAFEBABE, criteriaQuery);
		sb.append(proj.substring(0, proj.indexOf("as")).trim());
		sb.append(")");

		sb.append(SPACE_STRING)
				.append(op.getSqloperator())
				.append(SPACE_STRING)
				.append(value);

		return sb.toString();
	}

	// ========================= PRIVATE METHODS ==============================

	/**
	 * Private helper method to construct component ids with an optional alias.
	 *
	 * @param sb
	 *            a {@link StringBuffer}
	 * @param componentType
	 *            the {@link ComponentType}
	 * @param criteriaQuery
	 *            the {@link CriteriaQuery}
	 * @param criteria
	 *            the {@link Criteria}
	 * @param position
	 *            the position of the alias, appened if >= 0
	 */
	private void appendComponentIds(final StringBuffer sb,
			final ComponentType componentType, final CriteriaQuery criteriaQuery,
			final Criteria criteria, final Integer position)
	{
		final String[] idProperties = componentType.getPropertyNames();
		int currPos = position != null ? position.intValue() : -1;
		for (int i = 0; i < idProperties.length; i++)
		{
			sb.append(criteriaQuery.getColumn(criteria, groupByProperty + "."
					+ idProperties[i]));

			if (currPos >= 0)
			{
				sb.append(" as y").append(currPos).append('_');
				currPos++;
			}

			if (i + 1 < idProperties.length)
			{
				sb.append(", ");
			}
		}
	}

}
