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
package edu.utah.further.core.data.oracle.type;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;

import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * This converter can only read an Oracle XMLTYPE into a String. It is faster than
 * {@link OracleXmlType}.
 * <p>
 * Required Oracle jars: xdb.jar xmlparserv2.jar (Used by <code>XmlType</code>).
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>} - improvements
 * @version Mar 20, 2009
 */
public class OracleXmlTypeLength implements UserType, Serializable
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(OracleXmlType.class);

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1l;

	private static final Class<Long> returnedClass = Long.class;

	private static final int[] SQL_TYPES = new int[]
	{ Types.NUMERIC };

	// ========================= IMPLEMENTATION: UserType ==================

	/**
	 * @return
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	@Override
	public int[] sqlTypes()
	{
		return SQL_TYPES;
	}

	/**
	 * @return
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	@Override
	public Class<Long> returnedClass()
	{
		return returnedClass;
	}

	/**
	 * @param _obj
	 * @return
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode(final Object _obj)
	{
		return _obj.hashCode();
	}

	/**
	 * @param _cached
	 * @param _owner
	 * @return
	 * @throws HibernateException
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable,
	 *      java.lang.Object)
	 */
	@Override
	public Long assemble(final Serializable _cached, final Object _owner)
			throws HibernateException
	{
		try
		{
			return (Long) _cached;
		}
		catch (final Exception e)
		{
			throw new HibernateException("Could not assemble XMLType length", e);
		}
	}

	/**
	 * @param _obj
	 * @return
	 * @throws HibernateException
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	@Override
	public Long disassemble(final Object _obj) throws HibernateException
	{
		try
		{
			return (Long) _obj;
		}
		catch (final Exception e)
		{
			throw new HibernateException("Could not disassemble XMLType length", e);
		}
	}

	/**
	 * @param _orig
	 * @param _tar
	 * @param _owner
	 * @return
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Long replace(final Object _orig, final Object _tar, final Object _owner)
	{
		return (Long) _orig;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws HibernateException
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equals(final Object arg0, final Object arg1) throws HibernateException
	{
		return (arg0 == null) ? (arg1 == null) : arg0.equals(arg1);
	}

	/**
	 * Retrieve an {@link XMLType} from the result set and convert it to a string.
	 * 
	 * @param rs
	 *            SQL result set
	 * @param names
	 *            the column names
	 * @param owner
	 *            field owner = its containing entity
	 * @return XML field as string
	 * @throws HibernateException
	 * @throws SQLException
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet,
	 *      java.lang.String[], java.lang.Object)
	 */
	@Override
	public Long nullSafeGet(final ResultSet rs, final String[] names, final Object owner)
			throws HibernateException, SQLException
	{
		// Retrieve the XMLType field. Support both the old and new Oracle XDK code due to
		// FUR-1350. This code could
		// be improved by the visitor pattern.
		if (ReflectionUtil.instanceOf(rs.getObject(names[0]), SQLXML.class))
		{
			// New (11.2) XDK code
			final SQLXML xmlType = OracleXmlTypeAsString.getResultSetAsSqlXml(rs, names);
			// Not terribly efficient: reads the whole string to determine its length.
			// TODO: replace by more efficient calls that only get the length
			final String string = (xmlType == null) ? null : xmlType.getString();
			return (string == null) ? null : new Long(string.length());
		}
		// New (11.1) XDK code
		final Object xmlType = OracleXmlTypeAsString.getResultSetAsXmlType(rs, names);
		if (xmlType == null)
		{
			return null;
		}

		// Always treat XMLType as a CLOB (seems safest from experiments with
		// different connection bandwidths). Read the character stream into a string.

		/* final CLOB clob = xmlType.getClobVal(); */
		final Method getClobValMethod = ReflectionUtil.getMethod("getClobVal", xmlType);
		final Object clob = ReflectionUtil.invoke(getClobValMethod, xmlType);

		final Method lengthMethod = ReflectionUtil.getMethod("length", clob);

		return (clob == null) ? null : (Long) ReflectionUtil.invoke(lengthMethod, clob);

	}

	/**
	 * @param st
	 * @param value
	 * @param index
	 * @throws HibernateException
	 * @throws SQLException
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement,
	 *      java.lang.Object, int)
	 */
	@Override
	public void nullSafeSet(final PreparedStatement st, final Object value,
			final int index) throws HibernateException, SQLException
	{
		throw new UnsupportedOperationException("This is a read-only field");
	}

	/**
	 * @param value
	 * @return
	 * @throws HibernateException
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	@Override
	public Long deepCopy(final Object value) throws HibernateException
	{
		return (value == null) ? null : (Long) value;
	}

	/**
	 * @return
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	@Override
	public boolean isMutable()
	{
		return false;
	}

	// ========================= IMPLEMENTATION: TrimmedStringType =========

}
