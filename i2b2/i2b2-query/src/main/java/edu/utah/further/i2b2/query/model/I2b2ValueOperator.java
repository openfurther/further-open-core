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
package edu.utah.further.i2b2.query.model;

import static edu.utah.further.i2b2.query.util.I2b2QueryUtil.parseBetweenValue;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchType;

/**
 * Represents and I2b2 value operator in an {@link I2b2QueryValueConstraint}
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
 * @version Aug 26, 2009
 */
@XmlAccessorType(XmlAccessType.FIELD)
public enum I2b2ValueOperator
{
	/**
	 * Equals
	 */
	EQ
	{
		@Override
		public SearchCriterion createExpression(final String propertyName, final String value)
		{
			return SearchCriteria.simpleExpression(Relation.EQ, propertyName,
					isNumberFormat(value));
		}
	},

	/**
	 * Less Than
	 */
	LT
	{
		@Override
		public SearchCriterion createExpression(final String propertyName, final String value)
		{
			return SearchCriteria.simpleExpression(Relation.LT, propertyName,
					isNumberFormat(value));
		}
	},

	/**
	 * Less Than or Equal To
	 */
	LE
	{
		@Override
		public SearchCriterion createExpression(final String propertyName, final String value)
		{
			return SearchCriteria.simpleExpression(Relation.LE, propertyName,
					isNumberFormat(value));
		}
	},

	/**
	 * Greater Than
	 */
	GT
	{
		@Override
		public SearchCriterion createExpression(final String propertyName, final String value)
		{
			return SearchCriteria.simpleExpression(Relation.GT, propertyName,
					isNumberFormat(value));
		}
	},

	/**
	 * Greater Than or Equal To
	 */
	GE
	{
		@Override
		public SearchCriterion createExpression(final String propertyName, final String value)
		{
			return SearchCriteria.simpleExpression(Relation.GE, propertyName,
					isNumberFormat(value));
		}
	},

	/**
	 * Between
	 */
	BETWEEN
	{
		@SuppressWarnings("boxing")
		@Override
		public SearchCriterion createExpression(final String propertyName, final String value)
		{
			final List<Integer> values = parseBetweenValue(value);
			return SearchCriteria.range(SearchType.BETWEEN, propertyName, values
					.get(0)
					.longValue(), values.get(1).longValue());
		}
	};

	// ========================= METHODS =================================

	/**
	 * Creates an expression for this value operator
	 * 
	 * @param propertyName
	 *            the property in the expression
	 * @param value
	 *            the value of the expression
	 * @return {@link SearchCriterion} which represents the expression
	 */
	public abstract SearchCriterion createExpression(String propertyName, String value);

	/**
	 * Checks if value is a number, and returns a Long if it is
	 * 
	 * @param value
	 * @return value
	 */
	private static Object isNumberFormat(final String value)
	{
		try
		{
			return Long.valueOf(value);
		}
		catch (final NumberFormatException e)
		{
			return value;
		}
	}
}
