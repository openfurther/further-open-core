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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.springframework.util.FileCopyUtils;

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
public class OracleXmlTypeAsString implements UserType, Serializable
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1l;

	private static final Class<String> returnedClass = String.class;

	// =================================================================
	// To avoid missing Dialect mapping for _SQL_TYPECODE (2007) when hbm2ddl
	// is used. Experimental change from _SQL_TYPECODE to CLOB. If doesn't work,
	// revert to _SQL_TYPECODE.
	// =================================================================
	// private static final int[] SQL_TYPES = new int[]
	// { _SQL_TYPECODE };
	private static final int[] SQL_TYPES = new int[]
	{ Types.CLOB };

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(OracleXmlTypeAsString.class);

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
	public Class<String> returnedClass()
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
	public String assemble(final Serializable _cached, final Object _owner)
			throws HibernateException
	{
		try
		{
			return (String) _cached;
		}
		catch (final Exception e)
		{
			throw new HibernateException("Could not assemble String", e);
		}
	}

	/**
	 * @param _obj
	 * @return
	 * @throws HibernateException
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	@Override
	public String disassemble(final Object _obj) throws HibernateException
	{
		try
		{
			return (String) _obj;
		}
		catch (final Exception e)
		{
			throw new HibernateException("Could not disassemble String", e);
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
	public String replace(final Object _orig, final Object _tar, final Object _owner)
	{
		return deepCopy(_orig);
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
	public String nullSafeGet(final ResultSet rs, final String[] names, final Object owner)
			throws HibernateException, SQLException
	{
		// Support both the old and new Oracle XDK code due to FUR-1350. This code could
		// be improved by the visitor pattern.
		if (ReflectionUtil.instanceOf(rs.getObject(names[0]), SQLXML.class))
		{
			// New (11.2) XDK code
			return nullSafeGetSqlType(rs, names, owner);
		}
		// New (11.1) XDK code
		return nullSafeGetXmlType(rs, names, owner);
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
	public String deepCopy(final Object value) throws HibernateException
	{
		return (value == null) ? null : (String) value;
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

	// ========================= PRIVATE METHODS ===========================

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
	private String nullSafeGetSqlType(final ResultSet rs, final String[] names,
			final Object owner) throws HibernateException, SQLException
	{
		// Retrieve the XMLType field
		final SQLXML xmlType = getResultSetAsSqlXml(rs, names);

		if (xmlType == null)
		{
			return null;
		}

		final String result = xmlType.getString();
		// Call to free to release resources
		xmlType.free();
		return result;
	}

	/**
	 * A useful result-set-to-XMLType conversion utility.
	 * 
	 * @param rs
	 *            result set
	 * @param names
	 * @return XMLType
	 * @throws SQLException
	 */
	static SQLXML getResultSetAsSqlXml(final ResultSet rs, final String[] names)
			throws SQLException
	{
		return (SQLXML) rs.getObject(names[0]);
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
	private String nullSafeGetXmlType(final ResultSet rs, final String[] names,
			final Object owner) throws HibernateException, SQLException
	{
		// Retrieve the XMLType field
		final Object xmlType = getResultSetAsXmlType(rs, names);
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

		if (log.isDebugEnabled())
		{
			log.debug("Starting XMLType CLOB transfer, length "
					+ ReflectionUtil.invoke(lengthMethod, clob));
		}
		final Method getAsciiStreamMethod = ReflectionUtil.getMethod("getAsciiStream",
				clob);

		try (final InputStream reader = (InputStream) ReflectionUtil.invoke(
				getAsciiStreamMethod, clob))
		{
			final byte[] bytes = FileCopyUtils.copyToByteArray(reader);
			// IoUtil.readBytesFromStream(new BufferedInputStream(
			// reader), IoUtil.getDelayInMillisSystemProperty());
			final String string = new String(bytes);
			if (log.isDebugEnabled())
			{
				log.debug("Converted XMLType to a string, length " + string.length());
			}
			return string;
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * A useful result-set-to-XMLType conversion utility.
	 * 
	 * @param rs
	 *            result set
	 * @param names
	 * @return XMLType
	 * @throws SQLException
	 */
	static Object getResultSetAsXmlType(final ResultSet rs, final String[] names)
			throws SQLException
	{
		/* final OPAQUE opaque = (OPAQUE) rs.getObject(names[0]); */

		final Object opaque = rs.getObject(names[0]);

		/*
		 * final XMLType xmlType = (opaque == null) ? null :
		 * (ReflectionUtil.instanceOf(opaque, XMLType.class) ? (XMLType) opaque :
		 * XMLType.createXML(opaque));
		 */

		Object xmlType = null;
		if (opaque != null)
		{
			final Class<?> xmlTypeClass = ReflectionUtil.loadClass(OracleXmlType.XML_TYPE_CLASS);

			if (ReflectionUtil.instanceOf(opaque, xmlTypeClass))
			{
				xmlType = xmlTypeClass.cast(opaque);
			}
			else
			{
				final Class<?> opaqueTypeClass = ReflectionUtil.loadClass(OracleXmlType.OPAQUE_TYPE_CLASS);

				try
				{

					final Method createXMLMethod = ReflectionUtil.getMethod("createXML",
							xmlTypeClass, opaqueTypeClass);
					xmlType = createXMLMethod.invoke(null, opaque);
				}
				catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e)
				{
					throw new RuntimeException("Unable to invoke 'createXML' method", e);
				}
			}
		}

		return xmlType;
	}
}
