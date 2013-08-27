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

import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Api;

/**
 * FQL (Federated Query Language) operators - type of search (exact match / like match /
 * etc.). Some of the search types (e.g. <code>EQUALS</code>) support a general object as
 * the value of the search field. Other types are well defined ONLY for
 * <i>string</i>-valued search fields, or for collections (e.g. <code>IN_SET</code>).
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
 * @version Oct 13, 2008
 */
@Api
@XmlType(name = "")
public enum SearchType
{
	// ========================= CONSTANTS =================================

	// ========================= NO-ARGUMENT EXPRESSIONS ===================

	/**
	 * Apply an "is null" constraint to the named property.
	 */
	IS_NULL,

	/**
	 * Apply an "is not null" constraint to the named property.
	 */
	IS_NOT_NULL,

	/**
	 * Constrain a collection-valued property to be empty.
	 */
	IS_EMPTY,

	/**
	 * Constrain a collection-valued property to be non-empty.
	 */
	IS_NOT_EMPTY,

	// ========================= SIMPLE EXPRESSIONS ========================

	/**
	 * Constrain by a simple scalar property expression
	 * equality/inequality/comparison/...).
	 */
	SIMPLE,

	// ========================= INTERVAL EXPRESSIONS ======================

	/**
	 * Apply a "between" constraint to the named property.
	 * <p>
	 * Note: BETWEEN is a SQL standard, but different underlying databases may treat it
	 * differently (including or excluding the boundary values). When the search framework
	 * is implemented using a database, make sure to check what implementation your
	 * database is using.
	 * <p>
	 * If you are unsure about inclusion/exclusion, consider replacing BETWEEN by a
	 * conjunction of two relation operations for deterministic results (e.g.
	 * <code>(lo <= ) AND (<= hi)</code>).
	 *
	 * @param propertyName
	 * @param lo
	 *            value
	 * @param hi
	 *            value
	 *
	 * @see http://www.w3schools.com/sql/sql_between.asp
	 */
	BETWEEN,

	// ========================= STRING EXPRESSIONS ========================

	/**
	 * Apply a "like" constraint to the named property.
	 *
	 * @param propertyName
	 * @param value
	 */
	LIKE,

	/**
	 * A case-insensitive "like", similar to Postgres <tt>ilike</tt> operator.
	 *
	 * @param propertyName
	 * @param value
	 */
	ILIKE,

	// ========================= PROPERTY EXPRESSIONS ======================

	/**
	 * Apply a relation constraint between two properties.
	 */
	PROPERTY,

	// ========================= SIZE EXPRESSIONS ==========================

	/**
	 * Constrain a collection valued property by size.
	 */
	SIZE,

	// ========================= COLLECTION EXPRESSIONS ====================

	/**
	 * Apply an "in" constraint to the named property.
	 *
	 * @param propertyName
	 * @param values
	 */
	IN,

	/**
	 * Search for entity identifiers that satisfy <code>count(expression) op value</code>.
	 * <p>
	 * This is particularly handy when <code>expression</code> represents the logic
	 * <code>(collectionProperty contains a set of values)</code>, allowing to do an
	 * "all-equals" operation on a join column (e.g. find all persons that have diagnosis
	 * A, B AND C -- to be interpreted as <code>person.diagnosisCollection</code> contains
	 * the set <code>{A,B,C}</code> -- without running into an empty-set operation like
	 * <code>(diagnosis = A) AND
	 * (diagnosis = B) and (diagnosis = C)</code>.
	 *
	 * @param expression
	 *            an expression whose value is the set of values to search for. This
	 *            expression usually represents the logic (collectionProperty contains a
	 *            set of values).
	 * @param op
	 *            a relation property for the count
	 * @param value
	 *            count value
	 */
	COUNT,

	// ========================= UNARY LOGICAL EXPRESSIONS =================
	/**
	 * Return the negation of an expression.
	 *
	 * @param expression
	 */
	NOT,

	// ========================= BINARY LOGICAL EXPRESSIONS ================

	/**
	 * Return the conjunction of two expressions.
	 *
	 * @param lhs
	 * @param rhs
	 */
	AND,

	/**
	 * Return the disjunction of two expressions.
	 *
	 * @param lhs
	 * @param rhs
	 */
	OR,

	// ========================= MULTINARY (COMPOSITE) EXPRESSIONS =========

	/**
	 * Group expressions together in a single conjunction (A and B and C).
	 */
	CONJUNCTION,

	/**
	 * Group expressions together in a single disjunction (A or B or C).
	 */
	DISJUNCTION,

	// ========================= SET =====================================

	/**
	 * Return the union of a group of expressions.
	 */
	UNION,

	/**
	 * Return the intersection of a group of expressions.
	 */
	INTERSECTION,

	// ========================= OTHER =====================================

	/**
	 * Apply a constraint expressed in SQL, with the given JDBC parameters. Any
	 * occurrences of <tt>{alias}</tt> will be replaced by the table alias. This is like
	 * Hibernate's <code>SQLRestriction</code> type, but does not allow parametric
	 * replacements. Also, getPropertyName() of the corresponding criterion object is
	 * regarded as the SQL condition.
	 */
	SQL_RESTRICTION;

	// ========================= FIELDS ====================================

	// ========================= METHODS ===================================

}
