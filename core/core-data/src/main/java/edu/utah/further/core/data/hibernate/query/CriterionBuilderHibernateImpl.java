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

import static edu.utah.further.core.api.message.Messages.unsupportedMessage;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.eqProperty;
import static org.hibernate.criterion.Restrictions.ge;
import static org.hibernate.criterion.Restrictions.geProperty;
import static org.hibernate.criterion.Restrictions.gt;
import static org.hibernate.criterion.Restrictions.gtProperty;
import static org.hibernate.criterion.Restrictions.ilike;
import static org.hibernate.criterion.Restrictions.isEmpty;
import static org.hibernate.criterion.Restrictions.isNotEmpty;
import static org.hibernate.criterion.Restrictions.isNotNull;
import static org.hibernate.criterion.Restrictions.isNull;
import static org.hibernate.criterion.Restrictions.le;
import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.lt;
import static org.hibernate.criterion.Restrictions.ne;
import static org.hibernate.criterion.Restrictions.neProperty;
import static org.hibernate.criterion.Restrictions.not;
import static org.hibernate.criterion.Restrictions.sizeEq;
import static org.hibernate.criterion.Restrictions.sizeGe;
import static org.hibernate.criterion.Restrictions.sizeGt;
import static org.hibernate.criterion.Restrictions.sizeLe;
import static org.hibernate.criterion.Restrictions.sizeLt;
import static org.hibernate.criterion.Restrictions.sizeNe;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.criterion.Subqueries;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.query.domain.MatchType;
import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchOptions;
import edu.utah.further.core.query.domain.SearchType;

/**
 * Converts {@link SearchCriterion} objects to Hibernate {@link Criterion} objects.
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
@Implementation
final class CriterionBuilderHibernateImpl implements Builder<Criterion>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(CriterionBuilderHibernateImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * Original search criterion to convert.
	 */
	private final SearchCriterion criterion;

	/**
	 * Hibernate criterion target object.
	 */
	private Criterion result;

	/**
	 * Sub-criteria of {@link #criterion}, already converted to Hibernate by
	 * post-traversal visiting of the criteria tree in
	 * {@link QueryBuilderHibernateImpl#convertTree()}.
	 */
	private final List<Criterion> convertedCriteria;

	/**
	 * Sub-queries of {@link #criterion}, already converted to Hibernate by post-traversal
	 * visiting of the criteria tree in {@link QueryBuilderHibernateImpl#convertTree()}.
	 */
	private final List<DetachedCriteria> convertedQueries;

	/**
	 * Metadata about root entity, useful for looking at fields. Currently unused, may be
	 * useful in the future.
	 */
	private final ClassMetadata classMetadata;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Convert a search criterion to a Hibernate criterion.
	 * 
	 * @param searchQuery
	 *            main search query that criterion belongs to
	 * @param criterion
	 *            search criterion to convert
	 * @param args
	 *            Criterion constructor arguments required to build {@link #result}
	 */
	public static Criterion convert(final SearchCriterion criterion,
			final List<Criterion> convertedCriteria,
			final List<DetachedCriteria> convertedQueries,
			final ClassMetadata classMetadata)
	{
		return new CriterionBuilderHibernateImpl(criterion, convertedCriteria,
				convertedQueries, classMetadata).build();
	}

	/**
	 * Construct a factory.
	 * 
	 * @param searchQuery
	 *            main search query that criterion belongs to
	 * @param criterion
	 *            search criterion to convert
	 * @param args
	 *            Criterion constructor arguments required to build {@link #result}
	 */
	private CriterionBuilderHibernateImpl(final SearchCriterion criterion,
			final List<Criterion> convertedCriteria,
			final List<DetachedCriteria> convertedQueries,
			final ClassMetadata classMetadata)
	{
		super();
		this.classMetadata = classMetadata;
		this.criterion = criterion;
		this.convertedCriteria = convertedCriteria;
		this.convertedQueries = convertedQueries;
	}

	// ========================= IMPLEMENTATION: Builder<Criterion> ========

	/**
	 * Convert a search criterion to a Hibernate Criterion.
	 * 
	 * @param criterion
	 *            criterion to visit
	 * @param args
	 *            Criterion constructor arguments required to build {@link #result}
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public Criterion build()
	{
		final SearchType searchType = criterion.getSearchType();
		switch (searchType)
		{
			case AND:
			case OR:
			{
				visitBinary();
				break;
			}

			case COUNT:
			{
				visitCountExpression();
				break;
			}

			case IN:
			{
				visitCollectionExpression();
				break;
			}

			case BETWEEN:
			{
				visitInterval();
				break;
			}

			case CONJUNCTION:
			case DISJUNCTION:
			{
				visitJunction();
				break;
			}

			case IS_NULL:
			case IS_NOT_NULL:
			case IS_EMPTY:
			case IS_NOT_EMPTY:
			{
				visitNoArgExpression();
				break;
			}

			case PROPERTY:
			{
				visitPropertyExpression();
				break;
			}

			case SIMPLE:
			{
				visitSimpleExpression();
				break;
			}

			case SIZE:
			{
				visitSizeExpression();
				break;
			}

			case LIKE:
			case ILIKE:
			{
				visitStringExpression();
				break;
			}

			case NOT:
			{
				visitUnary();
				break;
			}

			case SQL_RESTRICTION:
			{
				visitSql();
				break;
			}

			case UNION:
			{
				// TODO: implement this properly
				visitBinary();
				break;
			}

			case INTERSECTION:
			{
				// TODO: implement this properly
				visitBinary();
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(searchType));
			}
		}

		// Add global validation here if necessary

		return result;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 *
	 */
	private void visitBinary()
	{
		final SearchType searchType = criterion.getSearchType();
		switch (searchType)
		{
			case INTERSECTION:
			case AND:
			{
				result = Restrictions.and(convertedCriteria.get(0),
						convertedCriteria.get(1));
				break;
			}

			case UNION:
			case OR:
			{
				result = Restrictions.or(convertedCriteria.get(0),
						convertedCriteria.get(1));
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(searchType));
			}
		}
	}

	/**
	 * Visits a collection expression.
	 */
	public void visitCollectionExpression()
	{
		final SearchType searchType = criterion.getSearchType();
		final String propertyName = (String) criterion.getParameter(0);
		final List<?> values = criterion.getParameters();
		switch (searchType)
		{
			case IN:
			{
				if (convertedQueries != null && convertedQueries.size() > 0)
				{
					result = Subqueries.propertyIn(propertyName, convertedQueries
							.get(0)
							.setProjection(Property.forName(propertyName)));
				}
				else
				{
					result = Restrictions.in(propertyName,
							values.subList(1, values.size()));
				}
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(searchType));
			}
		}
	}

	/**
	 * Corresponds to the sub-select
	 * 
	 * <pre>
	 * sub-criterion + group-by-having count([distinct] id)
	 * op count
	 * </pre>
	 * 
	 * .
	 * 
	 * @see <code>LTestHibernateCount</code>
	 */
	public void visitCountExpression()
	{
		// TODO: move projection code to ProjectionFactoryHibernateImpl for more
		// generic sub-select support (after we add more sub-select types in
		// addition to COUNT)
		final String idPropertyName = classMetadata.getIdentifierPropertyName();
		final CountProjectionParams projectionParams = CountProjectionParams.newInstance(
				this, criterion);
		final DetachedCriteria subQuery = createCountSubQuery().setProjection(
				CustomProjections.groupByHaving(idPropertyName, CustomProjections
						.countProjection(projectionParams.propertyName,
								projectionParams.distinct), projectionParams.relation,
						projectionParams.value));

		// Main criterion: ID in (result of sub-query)
		result = Subqueries.propertyIn(classMetadata.getIdentifierPropertyName(),
				subQuery);
	}

	/**
	 * @param <E>
	 * @param criterion
	 * @see #visit(edu.utah.further.core.search.IntervalExpression)
	 */
	private void visitInterval()
	{
		final String propertyName = (String) criterion.getParameter(0);
		final Object low = criterion.getParameter(1);
		final Object high = criterion.getParameter(2);
		result = Restrictions.between(propertyName, low, high);
	}

	/**
	 * @param criterion
	 * @see #visit(edu.utah.further.core.data.search.JunctionCriterionImpl)
	 */
	public void visitJunction()
	{
		final SearchType searchType = criterion.getSearchType();
		switch (searchType)
		{
			case CONJUNCTION:
			{
				result = Restrictions.conjunction();
				break;
			}

			case DISJUNCTION:
			{
				result = Restrictions.disjunction();
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(searchType));
			}
		}

		// Add junction arguments
		for (final Criterion c : convertedCriteria)
		{
			((Junction) result).add(c);
		}
	}

	/**
	 * @param <E>
	 * @param criterion
	 * @see edu.utah.further.core.data.search.SearchCriterionVisitorImpl#visit(edu.utah.further.core.data.search.NoArgExpressionImpl)
	 */
	public <E> void visitNoArgExpression()
	{
		final SearchType searchType = criterion.getSearchType();
		final String propertyName = (String) criterion.getParameter(0);
		switch (searchType)
		{
			case IS_NULL:
			{
				result = isNull(propertyName);
				break;
			}

			case IS_NOT_NULL:
			{
				result = isNotNull(propertyName);
				break;
			}

			case IS_EMPTY:
			{
				result = isEmpty(propertyName);
				break;
			}

			case IS_NOT_EMPTY:
			{
				result = isNotEmpty(propertyName);
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(searchType));
			}
		}
	}

	/**
	 * @param <E>
	 * @param criterion
	 * @see #visit(edu.utah.further.core.search.PropertyExpression)
	 */
	public <E> void visitPropertyExpression()
	{
		final Relation relation = (Relation) criterion.getParameter(0);
		final String propertyName = (String) criterion.getParameter(1);
		final String otherPropertyName = (String) criterion.getParameter(2);
		switch (relation)
		{
			case EQ:
			{
				result = eqProperty(propertyName, otherPropertyName);
				break;
			}

			case NE:
			{
				result = neProperty(propertyName, otherPropertyName);
				break;
			}

			case GT:
			{
				result = gtProperty(propertyName, otherPropertyName);
				break;
			}

			case GE:
			{
				result = geProperty(propertyName, otherPropertyName);
				break;
			}

			case LT:
			{
				result = Restrictions.ltProperty(propertyName, otherPropertyName);
				break;
			}

			case LE:
			{
				result = Restrictions.leProperty(propertyName, otherPropertyName);
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(relation));
			}
		}
	}

	/**
	 * @param <E>
	 * @param criterion
	 * @see #visit(edu.utah.further.core.search.SimpleExpression)
	 */
	public <E> void visitSimpleExpression()
	{
		final Relation relation = (Relation) criterion.getParameter(0);
		final String propertyName = (String) criterion.getParameter(1);
		final Object value = criterion.getParameter(2);

		switch (relation)
		{
			case EQ:
			{
				result = eq(propertyName, value);
				break;
			}

			case NE:
			{
				result = ne(propertyName, value);
				break;
			}

			case GT:
			{
				result = gt(propertyName, value);
				break;
			}

			case GE:
			{
				result = ge(propertyName, value);
				break;
			}

			case LT:
			{
				result = lt(propertyName, value);
				break;
			}

			case LE:
			{
				result = le(propertyName, value);
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(relation));
			}
		}

		final SearchOptions options = criterion.getOptions();
		if (options != null)
		{
			if (options.isIgnoreCase().booleanValue())
			{
				((SimpleExpression) result).ignoreCase();
			}
		}
	}

	/**
	 * @param <E>
	 * @param criterion
	 * @see #visit(edu.utah.further.core.search.SizeExpression)
	 */
	public <E> void visitSizeExpression()
	{
		final Relation relation = (Relation) criterion.getParameter(0);
		final String propertyName = (String) criterion.getParameter(1);
		final int size = ((Integer) criterion.getParameter(2)).intValue();
		switch (relation)
		{
			case EQ:
			{
				result = sizeEq(propertyName, size);
				break;
			}

			case NE:
			{
				result = sizeNe(propertyName, size);
				break;
			}

			case GT:
			{
				result = sizeGt(propertyName, size);
				break;
			}

			case GE:
			{
				result = sizeGe(propertyName, size);
				break;
			}

			case LT:
			{
				result = sizeLt(propertyName, size);
				break;
			}

			case LE:
			{
				result = sizeLe(propertyName, size);
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(relation));
			}
		}
	}

	/**
	 * @param criterion
	 * @see #visit(net.StringExpressionImpl.core.search.StringExpression)
	 */
	public void visitStringExpression()
	{
		final SearchType searchType = criterion.getSearchType();
		final String propertyName = (String) criterion.getParameter(0);
		final String value = (String) criterion.getParameter(1);
		final MatchMode matchMode = toHibernateMatchMode(criterion.getMatchType());
		switch (searchType)
		{
			case LIKE:
			{
				result = like(propertyName, value, matchMode);
				break;
			}

			case ILIKE:
			{
				result = ilike(propertyName, value, matchMode);
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(searchType));
			}
		}
	}

	/**
	 * @param criterion
	 * @see #visit(net.UnaryCriterionImpl.core.search.UnaryCriterion)
	 */
	public void visitUnary()
	{
		final SearchType searchType = criterion.getSearchType();
		switch (searchType)
		{
			case NOT:
			{
				result = not(convertedCriteria.get(0));
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(searchType));
			}
		}
	}

	/**
	 * @param criterion
	 * @see #visit(net.SQLCriterionImpl.core.search.SQLCriterion)
	 */
	public void visitSql()
	{
		final String sql = (String) criterion.getParameter(0);
		result = Restrictions.sqlRestriction(sql);
	}

	/**
	 * Convert match type to Hibernate match mode
	 * 
	 * @param matchType
	 *            match type
	 * @return corresponding Hibernate match mode
	 */
	private static MatchMode toHibernateMatchMode(final MatchType matchType)
	{
		switch (matchType)
		{
			case EXACT:
			{
				return MatchMode.EXACT;
			}

			case STARTS_WITH:
			{
				return MatchMode.START;
			}

			case ENDS_WITH:
			{
				return MatchMode.END;
			}

			case CONTAINS:
			{
				return MatchMode.ANYWHERE;
			}
		}

		return null;
	}

	/**
	 * Build the ID sub-query of a count search type.
	 * 
	 * @return sub-query
	 */
	private DetachedCriteria createCountSubQuery()
	{
		final SearchType searchType = criterion.getSearchType();
		switch (searchType)
		{
			case COUNT:
			{
				return convertedQueries.get(0);
			}
			default:
			{
				throw new BusinessRuleException(unsupportedMessage(searchType));
			}
		}
	}

	/**
	 * Holds a count search type's projection parameters.
	 */
	private static class CountProjectionParams
	{
		/**
		 * Property to count.
		 */
		private final String propertyName;

		/**
		 * Count distinct property values or all values.
		 */
		private final boolean distinct;

		/**
		 * Relation to apply to the count
		 */
		private final Relation relation;

		/**
		 * Right-hand operand of {@link #relation}.
		 */
		private final Object value;

		/**
		 * @param propertyName
		 * @param distinct
		 * @param relation
		 * @param value
		 */
		private CountProjectionParams(final String propertyName, final boolean distinct,
				final Relation relation, final Object value)
		{
			super();
			this.propertyName = propertyName;
			this.distinct = distinct;
			this.relation = relation;
			this.value = value;
		}

		/**
		 * @param builder
		 * @param criterion
		 * @return
		 */
		public static CountProjectionParams newInstance(
				final CriterionBuilderHibernateImpl builder,
				final SearchCriterion criterion)
		{
			final SearchType searchType = criterion.getSearchType();
			switch (searchType)
			{
				case COUNT:
				{
					// Expression: count(subselect op value)
					// Senseless count distinct IDs - that would always yield count=1
					// because we are grouping on IDs
					return new CountProjectionParams(
							builder.classMetadata.getIdentifierPropertyName(), false,
							(Relation) criterion.getParameter(0),
							criterion.getParameter(1));
				}
				default:
				{
					throw new BusinessRuleException(unsupportedMessage(searchType));
				}
			}
		}
	}
}
