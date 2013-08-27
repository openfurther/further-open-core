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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * Supported relations between comparable objects.
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
@XmlType(namespace = XmlNamespace.CORE_QUERY, name = "RelationType")
@XmlEnum(String.class)
public enum Relation
{
	// ========================= SIMPLE EXPRESSIONS ========================

	/**
	 * Apply an "equal" constraint to the named property.
	 *
	 * @param propertyName
	 * @param value
	 */
	@XmlEnumValue("EQ")
	EQ("="),

	/**
	 * Apply a "not equal" constraint to the named property.
	 *
	 * @param propertyName
	 * @param value
	 */
	@XmlEnumValue("NE")
	NE("<>"),

	/**
	 * Apply a "greater than" constraint to the named property.
	 *
	 * @param propertyName
	 * @param value
	 */
	@XmlEnumValue("GT")
	GT(">"),

	/**
	 * Apply a "less than" constraint to the named property.
	 *
	 * @param propertyName
	 * @param value
	 */
	@XmlEnumValue("LT")
	LT("<"),

	/**
	 * Apply a "less than or equal" constraint to the named property.
	 *
	 * @param propertyName
	 * @param value
	 */
	@XmlEnumValue("LE")
	LE("<="),

	/**
	 * Apply a "greater than or equal" constraint to the named property.
	 *
	 * @param propertyName
	 * @param value
	 */
	@XmlEnumValue("GE")
	GE(">=");

	// ========================= CONSTANTS =================================

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "relation";

	// ========================= FIELDS ====================================

	/**
	 * Standard SQL operator of this relation.
	 */
	private final String sqlOperator;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param sqlOperator
	 */
	private Relation(final String sqlOperator)
	{
		this.sqlOperator = sqlOperator;
	}

	// ========================= GET & SET =================================

	/**
	 * Return the sqloperator property.
	 *
	 * @return the sqloperator
	 */
	public String getSqloperator()
	{
		return sqlOperator;
	}

}
