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

import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

import edu.utah.further.core.query.domain.Relation;

/**
 * A class for custom developed Hibernate Projections.
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
public final class CustomProjections
{
	// ========================= IMPL: CustomProjections ==============================

	/**
	 * Group by having with a general relation.
	 *
	 * @param groupByProperty
	 * @param projection
	 * @param op
	 * @param value
	 * @return
	 */
	public static final Projection groupByHaving(final String groupByProperty,
			final Projection projection, final Relation op, final Object value)
	{
		return new GroupByHavingProjection(groupByProperty, projection, op, value);
	}

	/**
	 * Group by having equal some value.
	 *
	 * @param groupByProperty
	 * @param projection
	 * @param value
	 * @return
	 */
	public static final Projection groupByHavingEq(final String groupByProperty,
			final Projection projection, final Object value)
	{
		return groupByHaving(groupByProperty, projection, Relation.EQ, value);
	}

	/**
	 * Visits a collection expression.
	 */
	public static Projection countProjection(final String propertyName,
			final boolean distinct)
	{
		return distinct ? Projections.countDistinct(propertyName) : Projections
				.count(propertyName);
	}
}
