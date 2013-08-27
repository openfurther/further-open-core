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
package edu.utah.further.core.query.domain;

import static edu.utah.further.core.api.collections.CollectionUtil.addAll;
import static edu.utah.further.core.api.collections.CollectionUtil.toArrayNullSafe;
import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_INTEGER;
import static edu.utah.further.core.api.constant.Constants.MAX_IN;
import static edu.utah.further.core.query.domain.SearchType.COUNT;
import static edu.utah.further.core.query.domain.SearchType.PROPERTY;
import static edu.utah.further.core.query.domain.SearchType.SIMPLE;
import static edu.utah.further.core.query.domain.SearchType.SIZE;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.query.domain.SearchCriterionTo.SearchCriterionBuilder;

/**
 * A mother object that centralizes all public factory methods of the search query API.
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
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Aug 26, 2009
 * @since May 29, 2009
 */
@Utility
public final class SearchCriteria
{
	// ========================= STATIC METHODS =============================

	/**
	 * Create a search query with default options from a criterion tree.
	 * 
	 * @param rootCriterion
	 *            root node of the criteria hierarchy
	 * @return search query instance
	 */
	public static SearchQuery query(final SearchCriterion rootCriterion, final String rootObject)
	{
		return queryBuilder(rootCriterion).setRootObject(rootObject).build();
	}

	/**
	 * Create a search query builder.
	 * 
	 * @param rootCriterion
	 *            root node of the criteria hierarchy
	 * @return search query build; you may use its setter methods to set global query
	 *         options (e.g. sort options) and then call
	 *         {@link SearchQueryBuilderImpl#build()} to obtain a {@link SearchQuery}
	 *         instance
	 */
	public static SearchQueryBuilder queryBuilder(final SearchCriterion rootCriterion)
	{
		return new SearchQueryBuilderImpl(rootCriterion);
	}

	/**
	 * Construct a no-argument search criterion (e.g. is a property non-<code>null</code>
	 * ).
	 * 
	 * @param searchType
	 *            type of search (equals/less than/...)
	 * @param fieldType
	 *            Field type
	 * @param name
	 *            Search field name
	 */
	public static <E> SearchCriterion noArgExpression(final SearchType searchType,
			final String propertyName)
	{
		switch (searchType)
		{
			case IS_NULL:
			case IS_NOT_NULL:
			case IS_EMPTY:
			case IS_NOT_EMPTY:
			{
				return new SearchCriterionBuilder(searchType, 1).setParameters(
						propertyName).build();
			}

			default:
			{
				throw new BusinessRuleException(searchType
						+ " is not a no-argument search type");
			}
		}
	}

	/**
	 * Construct a simple expression criterion.
	 * 
	 * @param relation
	 *            relation between property and value
	 * @param propertyName
	 *            Search field name
	 * @param value
	 *            Field value to search for
	 */
	public static <E> SearchCriterion simpleExpression(final Relation relation,
			final String propertyName, final E value)
	{
		return new SearchCriterionBuilder(SIMPLE, 3).setParameters(relation,
				propertyName, value).build();
	}

	/**
	 * Construct a simple expression criterion.
	 * 
	 * @param relation
	 *            relation between property and value
	 * @param propertyName
	 *            Search field name
	 * @param value
	 *            Field value to search for
	 */
	public static <E> SearchCriterion simpleExpressionIgnoreCase(final Relation relation,
			final String propertyName, final E value)
	{
		return new SearchCriterionBuilder(SIMPLE, 3)
				.setParameters(relation, propertyName, value)
				.setIgnoreCase(Boolean.TRUE)
				.build();
	}

	/**
	 * Construct a string expression criterion. Currently escapeChar and ignoreCase
	 * properties of {@link StringExpression} are ignored upon conversion to a Hibernate
	 * criterion.
	 * 
	 * @param searchType
	 *            type of search (equals/less than/...)
	 * @param fieldType
	 *            Field type
	 * @param propertyName
	 *            Search field name
	 * @param value
	 *            Field value to search for
	 * @param matchType
	 *            Match type (starts with/exact/...)
	 */
	public static SearchCriterion stringExpression(final SearchType searchType,
			final String propertyName, final String value, final MatchType matchType)
	{
		// Currently escapeChar and ignoreCase properties of StringExpression
		// are ignored upon conversion to a Hibernate criterion.
		switch (searchType)
		{
			case LIKE:
			case ILIKE:
			{
				return new SearchCriterionBuilder(searchType, 2)
						.setParameters(propertyName, value)
						.setMatchType(matchType)
						.setEscapeChar(null)
						.setIgnoreCase(Boolean.FALSE)
						.build();
			}

			default:
			{
				throw new BusinessRuleException(searchType
						+ " is not a string expression search type");
			}
		}
	}

	/**
	 * Construct a property expression criterion.
	 * 
	 * @param relation
	 *            relation between property and value
	 * @param propertyName
	 *            Search field name
	 * @param size
	 *            Size value to search for
	 */
	public static SearchCriterion property(final Relation relation,
			final String propertyName, final String otherPropertyName)
	{
		return new SearchCriterionBuilder(PROPERTY, 3).<Object> setParameters(relation,
				propertyName, otherPropertyName).build();
	}

	/**
	 * Construct a property expression criterion.
	 * 
	 * @param relation
	 *            relation between property and value
	 * @param propertyName
	 *            Search field name (a collection-type property)
	 * @param size
	 *            size value
	 */
	public static <E> SearchCriterion size(final Relation relation,
			final String propertyName, final int size)
	{
		return new SearchCriterionBuilder(SIZE, 3).<Object> setParameters(relation,
				propertyName, new Integer(size)).build();
	}

	/**
	 * Construct a property expression criterion.
	 * 
	 * @param searchType
	 *            type of search (equals/less than/...)
	 * @param fieldType
	 *            Field type
	 * @param propertyName
	 *            Search field name (a collection-type property)
	 * @param values
	 *            array of property values
	 */
	public static <E> SearchCriterion collection(final SearchType searchType,
			final String propertyName, final Object... values)
	{
		switch (searchType)
		{
			case IN:
			{
				if (values.length > MAX_IN)
				{
					// Create a DISJUNCTION of several INs to overcome restriction on
					// number of IN parameters
					final SearchCriterion additionalCriterion = junction(SearchType.DISJUNCTION);
					final List<List<Object>> lists = Lists.partition(
							Arrays.asList(values), MAX_IN);
					for (final List<Object> list : lists)
					{
						final Object[] results = list.toArray();
						additionalCriterion.addCriterion(collection(SearchType.IN,
								propertyName, results));
					}
					return additionalCriterion;
				}
				final List<Object> params = CollectionUtil.newList();
				params.add(propertyName);
				addAll(params, values);
				return new SearchCriterionBuilder(searchType, values.length + 1)
						.<Object> setParameters(params)
						.build();

			}

			default:
			{
				throw new BusinessRuleException(searchType
						+ " is not a collection expression search type");
			}
		}
	}

	/**
	 * Construct a property expression criterion.
	 * 
	 * @param searchType
	 *            type of search (equals/less than/...)
	 * @param fieldType
	 *            Field type
	 * @param propertyName
	 *            Search field name (a collection-type property)
	 * @param values
	 *            array of property values
	 */
	public static <E> SearchCriterion collectionSubQuery(final SearchType searchType,
			final String propertyName, final SearchQuery subQuery)
	{
		switch (searchType)
		{
			case IN:
			{
				return new SearchCriterionBuilder(searchType, 1)
						.<Object> setParameters(Arrays.<Object> asList(propertyName))
						.addQuery(subQuery)
						.build();
			}

			default:
			{
				throw new BusinessRuleException(searchType
						+ " is not a collection expression search type");
			}
		}
	}

	/**
	 * Construct a property expression criterion.
	 * 
	 * @param searchType
	 *            type of search (equals/less than/...)
	 * @param fieldType
	 *            Field type
	 * @param propertyName
	 *            Search field name (a collection-type property)
	 * @param values
	 *            list of property values
	 */
	public static <E> SearchCriterion collectionList(final SearchType searchType,
			final String propertyName, final List<?> values)
	{
		return collection(searchType, propertyName, toArrayNullSafe(values));
	}

	/**
	 * Construct a ID-count (select id having count(sub-select expression) op value)
	 * 
	 * @param searchType
	 *            type of search (only <code>COUNT</code> is currently supported)
	 * @param relation
	 *            relation operation (op)
	 * @param value
	 *            value
	 * @param subQuery
	 *            sub-query (expression)
	 * @return count criterion instance
	 */
	public static SearchCriterion count(final Relation relation, final Object value,
			final SearchQuery subQuery)
	{
		return new SearchCriterionBuilder(COUNT, 2)
				.<Object> setParameters(Arrays.<Object> asList(relation, value))
				.addQuery(subQuery)
				.build();
	}

	/**
	 * Construct a unary criterion expression.
	 * 
	 * @param searchType
	 *            type of search (equals/less than/...)
	 * @param criterion
	 *            criterion to operator on
	 */
	public static SearchCriterion unary(final SearchType searchType,
			final SearchCriterion criterion)
	{
		switch (searchType)
		{
			case NOT:
			{
				return new SearchCriterionBuilder(searchType, 0)
						.addCriterion(criterion)
						.build();
			}

			default:
			{
				throw new BusinessRuleException(searchType
						+ " is not a unary criterion search type");
			}
		}
	}

	/**
	 * Construct a binary criterion expression.
	 * 
	 * @param searchType
	 *            type of search (equals/less than/...)
	 * @param lhsCriterion
	 *            left-hand-side criterion to operator on
	 * @param rhsCriterion
	 *            right-hand-side criterion to operator on
	 */
	public static SearchCriterion binary(final SearchType searchType,
			final SearchCriterion lhsCriterion, final SearchCriterion rhsCriterion)
	{
		switch (searchType)
		{
			case AND:
			case OR:
			{
				return new SearchCriterionBuilder(searchType, 2)
						.addCriterion(lhsCriterion)
						.addCriterion(rhsCriterion)
						.build();
			}

			default:
			{
				throw new BusinessRuleException(searchType
						+ " is not a binary criterion search type");
			}

		}
	}

	/**
	 * Construct a junction criterion expression.
	 * 
	 * @param searchType
	 *            type of search (equals/less than/...)
	 */
	public static SearchCriterion junction(final SearchType searchType)
	{
		switch (searchType)
		{
			case CONJUNCTION:
			case DISJUNCTION:
			{
				return new SearchCriterionBuilder(searchType, INVALID_VALUE_INTEGER)
						.build();
			}

			default:
			{
				throw new BusinessRuleException(searchType
						+ " is not a junction criterion search type");
			}

		}
	}

	/**
	 * Construct a junction criterion expression.
	 * 
	 * @param searchType
	 *            type of search (equals/less than/...)
	 */
	public static SearchCriterion set(final SearchType searchType)
	{
		switch (searchType)
		{
			case UNION:
			case INTERSECTION:
			{
				return new SearchCriterionBuilder(searchType, INVALID_VALUE_INTEGER)
						.build();
			}

			default:
			{
				throw new BusinessRuleException(searchType
						+ " is not a set criterion search type");
			}
		}
	}

	/**
	 * Construct an SQL restriction criterion. (In Hibernate, any occurrences of {alias}
	 * will be replaced by the table alias.)
	 * 
	 * @param sql
	 *            SQL query string
	 */
	public static SearchCriterion sql(final String sql)
	{
		return new SearchCriterionBuilder(SearchType.SQL_RESTRICTION, 1).setParameters(
				sql).build();
	}

	/**
	 * Create a ranged criterion from valueA to valueB, <E> must be of the same type
	 * 
	 * @param <E>
	 * @param searchType
	 *            the type of search defined in {@link SearchType}
	 * @param propertyName
	 *            search field
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static <E> SearchCriterion range(final SearchType searchType,
			final String propertyName, final E valueA, final E valueB)
	{
		switch (searchType)
		{
			case BETWEEN:
			{
				return new SearchCriterionBuilder(searchType, 3).<Object> setParameters(
						propertyName, valueA, valueB).build();
			}
			default:
			{
				throw new BusinessRuleException(searchType
						+ " is not a range criterion search type");
			}
		}
	}

	/**
	 * Create a sort criterion.
	 * 
	 * @param field
	 *            the name of the field to sort on
	 * @param sortType
	 *            the order to sort in
	 * @return a search criterion
	 */
	public static SortCriterion sort(final String fieldName, final SortType sortType)
	{
		return new SortCriterionTo(fieldName, sortType);
	}

	// ========================= USEFUL TEMPLATE METHODS ====================

	/**
	 * Add a simple expression (propertyName op propertyValue) under a criterion.
	 * 
	 * @param criterion
	 *            parent criterion to add the expression under
	 * @param relation
	 *            relation operation (op)
	 * @param propertyName
	 *            property name
	 * @param propertyValue
	 *            property value
	 * @return the updated parent criterion, for method chaining
	 */
	public static SearchCriterion addSimpleExpression(final SearchCriterion criterion,
			final Relation relation, final String propertyName, final long propertyValue)
	{
		return SearchCriteria.<Long> addSimpleExpression(criterion, relation,
				propertyName, new Long(propertyValue));
	}

	/**
	 * Add a simple expression (propertyName op propertyValue) under a criterion.
	 * 
	 * @param criterion
	 *            parent criterion to add the expression under
	 * @param relation
	 *            relation operation op
	 * @param propertyName
	 *            property name
	 * @param propertyValue
	 *            property value
	 * @return the updated parent criterion, for method chaining
	 */
	public static <E> SearchCriterion addSimpleExpression(
			final SearchCriterion criterion, final Relation relation,
			final String propertyName, final E propertyValue)
	{
		return criterion.addCriterion(simpleExpression(relation, propertyName,
				propertyValue));
	}
}
