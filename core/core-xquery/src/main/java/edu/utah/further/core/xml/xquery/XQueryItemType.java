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
package edu.utah.further.core.xml.xquery;

import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.xquery.XQItemType;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.xml.stax.XmlElementType;

/**
 * An enumeration of the supported XQuery variable types that can be externally binded to
 * values in Java code.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version May 19, 2010
 */
@Api
public enum XQueryItemType
{
	// ========================= CONSTANTS =================================

	ATOMIC(false, XQItemType.XQITEMKIND_ATOMIC),

	ATTRIBUTE(false, XQItemType.XQITEMKIND_ATTRIBUTE),

	COMMENT(false, XQItemType.XQITEMKIND_COMMENT),

	DOCUMENT(false, XQItemType.XQITEMKIND_DOCUMENT),

	DOCUMENT_ELEMENT(false, XQItemType.XQITEMKIND_DOCUMENT_ELEMENT),

	DOCUMENT_SCHEMA_ELEMENT(false, XQItemType.XQITEMKIND_DOCUMENT_SCHEMA_ELEMENT),

	ELEMENT(false, XQItemType.XQITEMKIND_ELEMENT),

	ITEM(false, XQItemType.XQITEMKIND_ITEM),

	NODE(false, XQItemType.XQITEMKIND_NODE),

	PI(false, XQItemType.XQITEMKIND_PI),

	TEXT(false, XQItemType.XQITEMKIND_TEXT),

	SCHEMA_ELEMENT(false, XQItemType.XQITEMKIND_SCHEMA_ELEMENT),

	SCHEMA_ATTRIBUTE(false, XQItemType.XQITEMKIND_SCHEMA_ATTRIBUTE),

	UNTYPED(true, XQItemType.XQBASETYPE_UNTYPED),

	ANYTYPE(true, XQItemType.XQBASETYPE_ANYTYPE),

	ANYSIMPLETYPE(true, XQItemType.XQBASETYPE_ANYSIMPLETYPE),

	ANYATOMICTYPE(true, XQItemType.XQBASETYPE_ANYATOMICTYPE),

	UNTYPEDATOMIC(true, XQItemType.XQBASETYPE_UNTYPEDATOMIC),

	DAYTIMEDURATION(true, XQItemType.XQBASETYPE_DAYTIMEDURATION),

	YEARMONTHDURATION(true, XQItemType.XQBASETYPE_YEARMONTHDURATION),

	ANYURI(true, XQItemType.XQBASETYPE_ANYURI),

	BASE64BINARY(true, XQItemType.XQBASETYPE_BASE64BINARY),

	BOOLEAN(true, XQItemType.XQBASETYPE_BOOLEAN),

	DATE(true, XQItemType.XQBASETYPE_DATE),

	INT(true, XQItemType.XQBASETYPE_INT),

	INTEGER(true, XQItemType.XQBASETYPE_INTEGER),

	SHORT(true, XQItemType.XQBASETYPE_SHORT),

	LONG(true, XQItemType.XQBASETYPE_LONG),

	DATETIME(true, XQItemType.XQBASETYPE_DATETIME),

	DECIMAL(true, XQItemType.XQBASETYPE_DECIMAL),

	DOUBLE(true, XQItemType.XQBASETYPE_DOUBLE),

	DURATION(true, XQItemType.XQBASETYPE_DURATION),

	FLOAT(true, XQItemType.XQBASETYPE_FLOAT),

	GDAY(true, XQItemType.XQBASETYPE_GDAY),

	GMONTH(true, XQItemType.XQBASETYPE_GMONTH),

	GMONTHDAY(true, XQItemType.XQBASETYPE_GMONTHDAY),

	GYEAR(true, XQItemType.XQBASETYPE_GYEAR),

	GYEARMONTH(true, XQItemType.XQBASETYPE_GYEARMONTH),

	HEXBINARY(true, XQItemType.XQBASETYPE_HEXBINARY),

	NOTATION(true, XQItemType.XQBASETYPE_NOTATION),

	QNAME(true, XQItemType.XQBASETYPE_QNAME),

	STRING(true, XQItemType.XQBASETYPE_STRING),

	TIME(true, XQItemType.XQBASETYPE_TIME),

	BYTE(true, XQItemType.XQBASETYPE_BYTE),

	NONPOSITIVE_INTEGER(true, XQItemType.XQBASETYPE_NONPOSITIVE_INTEGER),

	NONNEGATIVE_INTEGER(true, XQItemType.XQBASETYPE_NONNEGATIVE_INTEGER),

	NEGATIVE_INTEGER(true, XQItemType.XQBASETYPE_NEGATIVE_INTEGER),

	POSITIVE_INTEGER(true, XQItemType.XQBASETYPE_POSITIVE_INTEGER),

	UNSIGNED_LONG(true, XQItemType.XQBASETYPE_UNSIGNED_LONG),

	UNSIGNED_INT(true, XQItemType.XQBASETYPE_UNSIGNED_INT),

	UNSIGNED_SHORT(true, XQItemType.XQBASETYPE_UNSIGNED_SHORT),

	UNSIGNED_BYTE(true, XQItemType.XQBASETYPE_UNSIGNED_BYTE),

	NORMALIZED_STRING(true, XQItemType.XQBASETYPE_NORMALIZED_STRING),

	TOKEN(true, XQItemType.XQBASETYPE_TOKEN),

	LANGUAGE(true, XQItemType.XQBASETYPE_LANGUAGE),

	NAME(true, XQItemType.XQBASETYPE_NAME),

	NCNAME(true, XQItemType.XQBASETYPE_NCNAME),

	NMTOKEN(true, XQItemType.XQBASETYPE_NMTOKEN),

	ID(true, XQItemType.XQBASETYPE_ID),

	IDREF(true, XQItemType.XQBASETYPE_IDREF),

	ENTITY(true, XQItemType.XQBASETYPE_ENTITY),

	IDREFS(true, XQItemType.XQBASETYPE_IDREFS),

	ENTITIES(true, XQItemType.XQBASETYPE_ENTITIES),

	NMTOKENS(true, XQItemType.XQBASETYPE_NMTOKENS);

	// ========================= CONSTANTS ====================================

	/**
	 * Caches the {@link XMLStreamConstants}-valuet-our-enum-type mapping.
	 */
	private static final Map<Integer, XmlElementType> instances = CollectionUtil.newMap();

	static
	{
		for (final XmlElementType instance : XmlElementType.values())
		{
			instances.put(new Integer(instance.getXmlStreamConstant()), instance);
		}
	}

	// ========================= FIELDS =======================================

	/**
	 * Is item a base type of an item type.
	 */
	private final boolean baseType;

	/**
	 * Namespace's name.
	 */
	private final int xqueryItemConstant;

	// ========================= CONSTRUCTORS =================================

	/**
	 * Construct an XQuery item type.
	 *
	 * @param baseType
	 * @param xqueryItemConstant
	 */
	private XQueryItemType(final boolean baseType, final int xqueryItemConstant)
	{
		this.baseType = baseType;
		this.xqueryItemConstant = xqueryItemConstant;
	}

	// ========================= METHODS ======================================

	/**
	 * A factory method.
	 *
	 * @param xqueryItemConstant
	 *            {@link XMLStreamConstants} constant value
	 * @return corresponding enum constant
	 */
	public static XmlElementType valueOf(final int xqueryItemConstant)
	{
		return instances.get(new Integer(xqueryItemConstant));
	}

	// ========================= GET & SET ====================================

	/**
	 * Return the baseType property.
	 *
	 * @return the baseType
	 */
	public boolean isBaseType()
	{
		return baseType;
	}

	/**
	 * Return the xqueryItemConstant property.
	 *
	 * @return the xqueryItemConstant
	 */
	public int getXqueryItemConstant()
	{
		return xqueryItemConstant;
	}
}
