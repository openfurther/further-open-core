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
package edu.utah.further.core.data.logging;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.slf4j.Logger;

/**
 * A JDBC prepared {@link CallableStatement} decorator that logs executed SQL statements.
 * This is only a stub. TODO: add parameter logging by name.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 17, 2011
 */
final class LoggingCallableStatement extends LoggingPreparedStatement implements
		CallableStatement
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(LoggingCallableStatement.class);

	// ========================= FIELDS ====================================

	private final CallableStatement statement;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param statement
	 * @param query
	 */
	public static LoggingCallableStatement newLoggingCallableStatement(
			final CallableStatement statement, final ImmutableLoggingOptions options,
			final String query)
	{
		return new LoggingCallableStatement(statement, options, query);
	}

	/**
	 * @param statement
	 * @param query
	 */
	private LoggingCallableStatement(final CallableStatement statement,
			final ImmutableLoggingOptions options, final String query)
	{
		super(statement, options, query);
		this.statement = statement;
	}

	// ========================= IMPL: CallableStatement ===================

	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(int, int)
	 */
	@Override
	public void registerOutParameter(final int parameterIndex, final int sqlType)
			throws SQLException
	{
		statement.registerOutParameter(parameterIndex, sqlType);
	}

	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @param scale
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(int, int, int)
	 */
	@Override
	public void registerOutParameter(final int parameterIndex, final int sqlType,
			final int scale) throws SQLException
	{
		statement.registerOutParameter(parameterIndex, sqlType, scale);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#wasNull()
	 */
	@Override
	public boolean wasNull() throws SQLException
	{
		return statement.wasNull();
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getString(int)
	 */
	@Override
	public String getString(final int parameterIndex) throws SQLException
	{
		return statement.getString(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(final int parameterIndex) throws SQLException
	{
		return statement.getBoolean(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getByte(int)
	 */
	@Override
	public byte getByte(final int parameterIndex) throws SQLException
	{
		return statement.getByte(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getShort(int)
	 */
	@Override
	public short getShort(final int parameterIndex) throws SQLException
	{
		return statement.getShort(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getInt(int)
	 */
	@Override
	public int getInt(final int parameterIndex) throws SQLException
	{
		return statement.getInt(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getLong(int)
	 */
	@Override
	public long getLong(final int parameterIndex) throws SQLException
	{
		return statement.getLong(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getFloat(int)
	 */
	@Override
	public float getFloat(final int parameterIndex) throws SQLException
	{
		return statement.getFloat(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getDouble(int)
	 */
	@Override
	public double getDouble(final int parameterIndex) throws SQLException
	{
		return statement.getDouble(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @param scale
	 * @return
	 * @throws SQLException
	 * @deprecated
	 * @see java.sql.CallableStatement#getBigDecimal(int, int)
	 */
	@Deprecated
	@Override
	public BigDecimal getBigDecimal(final int parameterIndex, final int scale)
			throws SQLException
	{
		return statement.getBigDecimal(parameterIndex, scale);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getBytes(int)
	 */
	@Override
	public byte[] getBytes(final int parameterIndex) throws SQLException
	{
		return statement.getBytes(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getDate(int)
	 */
	@Override
	public Date getDate(final int parameterIndex) throws SQLException
	{
		return statement.getDate(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getTime(int)
	 */
	@Override
	public Time getTime(final int parameterIndex) throws SQLException
	{
		return statement.getTime(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(final int parameterIndex) throws SQLException
	{
		return statement.getTimestamp(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getObject(int)
	 */
	@Override
	public Object getObject(final int parameterIndex) throws SQLException
	{
		return statement.getObject(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException
	{
		return statement.getBigDecimal(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @param map
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getObject(int, java.util.Map)
	 */
	@Override
	public Object getObject(final int parameterIndex, final Map<String, Class<?>> map)
			throws SQLException
	{
		return statement.getObject(parameterIndex, map);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getRef(int)
	 */
	@Override
	public Ref getRef(final int parameterIndex) throws SQLException
	{
		return statement.getRef(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getBlob(int)
	 */
	@Override
	public Blob getBlob(final int parameterIndex) throws SQLException
	{
		return statement.getBlob(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getClob(int)
	 */
	@Override
	public Clob getClob(final int parameterIndex) throws SQLException
	{
		return statement.getClob(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getArray(int)
	 */
	@Override
	public Array getArray(final int parameterIndex) throws SQLException
	{
		return statement.getArray(parameterIndex);
	}

	/**
	 * @param parameterIndex
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getDate(int, java.util.Calendar)
	 */
	@Override
	public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException
	{
		return statement.getDate(parameterIndex, cal);
	}

	/**
	 * @param parameterIndex
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getTime(int, java.util.Calendar)
	 */
	@Override
	public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException
	{
		return statement.getTime(parameterIndex, cal);
	}

	/**
	 * @param parameterIndex
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getTimestamp(int, java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(final int parameterIndex, final Calendar cal)
			throws SQLException
	{
		return statement.getTimestamp(parameterIndex, cal);
	}

	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @param typeName
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(int, int, java.lang.String)
	 */
	@Override
	public void registerOutParameter(final int parameterIndex, final int sqlType,
			final String typeName) throws SQLException
	{
		statement.registerOutParameter(parameterIndex, sqlType, typeName);
	}

	/**
	 * @param parameterName
	 * @param sqlType
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int)
	 */
	@Override
	public void registerOutParameter(final String parameterName, final int sqlType)
			throws SQLException
	{
		statement.registerOutParameter(parameterName, sqlType);
	}

	/**
	 * @param parameterName
	 * @param sqlType
	 * @param scale
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int, int)
	 */
	@Override
	public void registerOutParameter(final String parameterName, final int sqlType,
			final int scale) throws SQLException
	{
		statement.registerOutParameter(parameterName, sqlType, scale);
	}

	/**
	 * @param parameterName
	 * @param sqlType
	 * @param typeName
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int,
	 *      java.lang.String)
	 */
	@Override
	public void registerOutParameter(final String parameterName, final int sqlType,
			final String typeName) throws SQLException
	{
		statement.registerOutParameter(parameterName, sqlType, typeName);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getURL(int)
	 */
	@Override
	public URL getURL(final int parameterIndex) throws SQLException
	{
		return statement.getURL(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @param val
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setURL(java.lang.String, java.net.URL)
	 */
	@Override
	public void setURL(final String parameterName, final URL val) throws SQLException
	{
		statement.setURL(parameterName, val);
	}

	/**
	 * @param parameterName
	 * @param sqlType
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int)
	 */
	@Override
	public void setNull(final String parameterName, final int sqlType)
			throws SQLException
	{
		statement.setNull(parameterName, sqlType);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
	 */
	@Override
	public void setBoolean(final String parameterName, final boolean x)
			throws SQLException
	{
		statement.setBoolean(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setByte(java.lang.String, byte)
	 */
	@Override
	public void setByte(final String parameterName, final byte x) throws SQLException
	{
		statement.setByte(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setShort(java.lang.String, short)
	 */
	@Override
	public void setShort(final String parameterName, final short x) throws SQLException
	{
		statement.setShort(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setInt(java.lang.String, int)
	 */
	@Override
	public void setInt(final String parameterName, final int x) throws SQLException
	{
		statement.setInt(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setLong(java.lang.String, long)
	 */
	@Override
	public void setLong(final String parameterName, final long x) throws SQLException
	{
		statement.setLong(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setFloat(java.lang.String, float)
	 */
	@Override
	public void setFloat(final String parameterName, final float x) throws SQLException
	{
		statement.setFloat(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setDouble(java.lang.String, double)
	 */
	@Override
	public void setDouble(final String parameterName, final double x) throws SQLException
	{
		statement.setDouble(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBigDecimal(java.lang.String,
	 *      java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(final String parameterName, final BigDecimal x)
			throws SQLException
	{
		statement.setBigDecimal(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setString(java.lang.String, java.lang.String)
	 */
	@Override
	public void setString(final String parameterName, final String x) throws SQLException
	{
		statement.setString(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBytes(java.lang.String, byte[])
	 */
	@Override
	public void setBytes(final String parameterName, final byte[] x) throws SQLException
	{
		statement.setBytes(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date)
	 */
	@Override
	public void setDate(final String parameterName, final Date x) throws SQLException
	{
		statement.setDate(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time)
	 */
	@Override
	public void setTime(final String parameterName, final Time x) throws SQLException
	{
		statement.setTime(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(final String parameterName, final Timestamp x)
			throws SQLException
	{
		statement.setTimestamp(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
	 *      java.io.InputStream, int)
	 */
	@Override
	public void setAsciiStream(final String parameterName, final InputStream x,
			final int length) throws SQLException
	{
		statement.setAsciiStream(parameterName, x, length);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
	 *      java.io.InputStream, int)
	 */
	@Override
	public void setBinaryStream(final String parameterName, final InputStream x,
			final int length) throws SQLException
	{
		statement.setBinaryStream(parameterName, x, length);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param targetSqlType
	 * @param scale
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object, int,
	 *      int)
	 */
	@Override
	public void setObject(final String parameterName, final Object x,
			final int targetSqlType, final int scale) throws SQLException
	{
		statement.setObject(parameterName, x, targetSqlType, scale);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param targetSqlType
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object, int)
	 */
	@Override
	public void setObject(final String parameterName, final Object x,
			final int targetSqlType) throws SQLException
	{
		statement.setObject(parameterName, x, targetSqlType);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setObject(final String parameterName, final Object x) throws SQLException
	{
		statement.setObject(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
	 *      java.io.Reader, int)
	 */
	@Override
	public void setCharacterStream(final String parameterName, final Reader reader,
			final int length) throws SQLException
	{
		statement.setCharacterStream(parameterName, reader, length);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param cal
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date,
	 *      java.util.Calendar)
	 */
	@Override
	public void setDate(final String parameterName, final Date x, final Calendar cal)
			throws SQLException
	{
		statement.setDate(parameterName, x, cal);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param cal
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time,
	 *      java.util.Calendar)
	 */
	@Override
	public void setTime(final String parameterName, final Time x, final Calendar cal)
			throws SQLException
	{
		statement.setTime(parameterName, x, cal);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param cal
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setTimestamp(java.lang.String, java.sql.Timestamp,
	 *      java.util.Calendar)
	 */
	@Override
	public void setTimestamp(final String parameterName, final Timestamp x,
			final Calendar cal) throws SQLException
	{
		statement.setTimestamp(parameterName, x, cal);
	}

	/**
	 * @param parameterName
	 * @param sqlType
	 * @param typeName
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void setNull(final String parameterName, final int sqlType,
			final String typeName) throws SQLException
	{
		statement.setNull(parameterName, sqlType, typeName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getString(java.lang.String)
	 */
	@Override
	public String getString(final String parameterName) throws SQLException
	{
		return statement.getString(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(final String parameterName) throws SQLException
	{
		return statement.getBoolean(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getByte(java.lang.String)
	 */
	@Override
	public byte getByte(final String parameterName) throws SQLException
	{
		return statement.getByte(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getShort(java.lang.String)
	 */
	@Override
	public short getShort(final String parameterName) throws SQLException
	{
		return statement.getShort(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getInt(java.lang.String)
	 */
	@Override
	public int getInt(final String parameterName) throws SQLException
	{
		return statement.getInt(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getLong(java.lang.String)
	 */
	@Override
	public long getLong(final String parameterName) throws SQLException
	{
		return statement.getLong(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(final String parameterName) throws SQLException
	{
		return statement.getFloat(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(final String parameterName) throws SQLException
	{
		return statement.getDouble(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getBytes(java.lang.String)
	 */
	@Override
	public byte[] getBytes(final String parameterName) throws SQLException
	{
		return statement.getBytes(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(final String parameterName) throws SQLException
	{
		return statement.getDate(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getTime(java.lang.String)
	 */
	@Override
	public Time getTime(final String parameterName) throws SQLException
	{
		return statement.getTime(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getTimestamp(java.lang.String)
	 */
	@Override
	public Timestamp getTimestamp(final String parameterName) throws SQLException
	{
		return statement.getTimestamp(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(final String parameterName) throws SQLException
	{
		return statement.getObject(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getBigDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getBigDecimal(final String parameterName) throws SQLException
	{
		return statement.getBigDecimal(parameterName);
	}

	/**
	 * @param parameterName
	 * @param map
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getObject(java.lang.String, java.util.Map)
	 */
	@Override
	public Object getObject(final String parameterName, final Map<String, Class<?>> map)
			throws SQLException
	{
		return statement.getObject(parameterName, map);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getRef(java.lang.String)
	 */
	@Override
	public Ref getRef(final String parameterName) throws SQLException
	{
		return statement.getRef(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getBlob(java.lang.String)
	 */
	@Override
	public Blob getBlob(final String parameterName) throws SQLException
	{
		return statement.getBlob(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getClob(java.lang.String)
	 */
	@Override
	public Clob getClob(final String parameterName) throws SQLException
	{
		return statement.getClob(parameterName);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getArray(java.lang.String)
	 */
	@Override
	public Array getArray(final String parameterName) throws SQLException
	{
		return statement.getArray(parameterName);
	}

	/**
	 * @param parameterName
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getDate(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Date getDate(final String parameterName, final Calendar cal)
			throws SQLException
	{
		return statement.getDate(parameterName, cal);
	}

	/**
	 * @param parameterName
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getTime(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Time getTime(final String parameterName, final Calendar cal)
			throws SQLException
	{
		return statement.getTime(parameterName, cal);
	}

	/**
	 * @param parameterName
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getTimestamp(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(final String parameterName, final Calendar cal)
			throws SQLException
	{
		return statement.getTimestamp(parameterName, cal);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getURL(java.lang.String)
	 */
	@Override
	public URL getURL(final String parameterName) throws SQLException
	{
		return statement.getURL(parameterName);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getRowId(int)
	 */
	@Override
	public RowId getRowId(final int parameterIndex) throws SQLException
	{
		return statement.getRowId(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getRowId(java.lang.String)
	 */
	@Override
	public RowId getRowId(final String parameterName) throws SQLException
	{
		return statement.getRowId(parameterName);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setRowId(java.lang.String, java.sql.RowId)
	 */
	@Override
	public void setRowId(final String parameterName, final RowId x) throws SQLException
	{
		statement.setRowId(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param value
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNString(java.lang.String, java.lang.String)
	 */
	@Override
	public void setNString(final String parameterName, final String value)
			throws SQLException
	{
		statement.setNString(parameterName, value);
	}

	/**
	 * @param parameterName
	 * @param value
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNCharacterStream(java.lang.String,
	 *      java.io.Reader, long)
	 */
	@Override
	public void setNCharacterStream(final String parameterName, final Reader value,
			final long length) throws SQLException
	{
		statement.setNCharacterStream(parameterName, value, length);
	}

	/**
	 * @param parameterName
	 * @param value
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNClob(java.lang.String, java.sql.NClob)
	 */
	@Override
	public void setNClob(final String parameterName, final NClob value)
			throws SQLException
	{
		statement.setNClob(parameterName, value);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setClob(java.lang.String, java.io.Reader, long)
	 */
	@Override
	public void setClob(final String parameterName, final Reader reader, final long length)
			throws SQLException
	{
		statement.setClob(parameterName, reader, length);
	}

	/**
	 * @param parameterName
	 * @param inputStream
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBlob(java.lang.String, java.io.InputStream,
	 *      long)
	 */
	@Override
	public void setBlob(final String parameterName, final InputStream inputStream,
			final long length) throws SQLException
	{
		statement.setBlob(parameterName, inputStream, length);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNClob(java.lang.String, java.io.Reader, long)
	 */
	@Override
	public void setNClob(final String parameterName, final Reader reader,
			final long length) throws SQLException
	{
		statement.setNClob(parameterName, reader, length);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNClob(int)
	 */
	@Override
	public NClob getNClob(final int parameterIndex) throws SQLException
	{
		return statement.getNClob(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNClob(java.lang.String)
	 */
	@Override
	public NClob getNClob(final String parameterName) throws SQLException
	{
		return statement.getNClob(parameterName);
	}

	/**
	 * @param parameterName
	 * @param xmlObject
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setSQLXML(java.lang.String, java.sql.SQLXML)
	 */
	@Override
	public void setSQLXML(final String parameterName, final SQLXML xmlObject)
			throws SQLException
	{
		statement.setSQLXML(parameterName, xmlObject);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getSQLXML(int)
	 */
	@Override
	public SQLXML getSQLXML(final int parameterIndex) throws SQLException
	{
		return statement.getSQLXML(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getSQLXML(java.lang.String)
	 */
	@Override
	public SQLXML getSQLXML(final String parameterName) throws SQLException
	{
		return statement.getSQLXML(parameterName);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNString(int)
	 */
	@Override
	public String getNString(final int parameterIndex) throws SQLException
	{
		return statement.getNString(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNString(java.lang.String)
	 */
	@Override
	public String getNString(final String parameterName) throws SQLException
	{
		return statement.getNString(parameterName);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNCharacterStream(int)
	 */
	@Override
	public Reader getNCharacterStream(final int parameterIndex) throws SQLException
	{
		return statement.getNCharacterStream(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getNCharacterStream(final String parameterName) throws SQLException
	{
		return statement.getNCharacterStream(parameterName);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getCharacterStream(int)
	 */
	@Override
	public Reader getCharacterStream(final int parameterIndex) throws SQLException
	{
		return statement.getCharacterStream(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getCharacterStream(final String parameterName) throws SQLException
	{
		return statement.getCharacterStream(parameterName);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBlob(java.lang.String, java.sql.Blob)
	 */
	@Override
	public void setBlob(final String parameterName, final Blob x) throws SQLException
	{
		statement.setBlob(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setClob(java.lang.String, java.sql.Clob)
	 */
	@Override
	public void setClob(final String parameterName, final Clob x) throws SQLException
	{
		statement.setClob(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
	 *      java.io.InputStream, long)
	 */
	@Override
	public void setAsciiStream(final String parameterName, final InputStream x,
			final long length) throws SQLException
	{
		statement.setAsciiStream(parameterName, x, length);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
	 *      java.io.InputStream, long)
	 */
	@Override
	public void setBinaryStream(final String parameterName, final InputStream x,
			final long length) throws SQLException
	{
		statement.setBinaryStream(parameterName, x, length);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
	 *      java.io.Reader, long)
	 */
	@Override
	public void setCharacterStream(final String parameterName, final Reader reader,
			final long length) throws SQLException
	{
		statement.setCharacterStream(parameterName, reader, length);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
	 *      java.io.InputStream)
	 */
	@Override
	public void setAsciiStream(final String parameterName, final InputStream x)
			throws SQLException
	{
		statement.setAsciiStream(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
	 *      java.io.InputStream)
	 */
	@Override
	public void setBinaryStream(final String parameterName, final InputStream x)
			throws SQLException
	{
		statement.setBinaryStream(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
	 *      java.io.Reader)
	 */
	@Override
	public void setCharacterStream(final String parameterName, final Reader reader)
			throws SQLException
	{
		statement.setCharacterStream(parameterName, reader);
	}

	/**
	 * @param parameterName
	 * @param value
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNCharacterStream(java.lang.String,
	 *      java.io.Reader)
	 */
	@Override
	public void setNCharacterStream(final String parameterName, final Reader value)
			throws SQLException
	{
		statement.setNCharacterStream(parameterName, value);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setClob(java.lang.String, java.io.Reader)
	 */
	@Override
	public void setClob(final String parameterName, final Reader reader)
			throws SQLException
	{
		statement.setClob(parameterName, reader);
	}

	/**
	 * @param parameterName
	 * @param inputStream
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBlob(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void setBlob(final String parameterName, final InputStream inputStream)
			throws SQLException
	{
		statement.setBlob(parameterName, inputStream);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNClob(java.lang.String, java.io.Reader)
	 */
	@Override
	public void setNClob(final String parameterName, final Reader reader)
			throws SQLException
	{
		statement.setNClob(parameterName, reader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getObject(int, java.lang.Class)
	 */
	@Override
	public <T> T getObject(final int parameterIndex, final Class<T> type)
			throws SQLException
	{
		return statement.getObject(parameterIndex, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.CallableStatement#getObject(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T getObject(final String parameterName, final Class<T> type)
			throws SQLException
	{
		return statement.getObject(parameterName, type);
	}

}
