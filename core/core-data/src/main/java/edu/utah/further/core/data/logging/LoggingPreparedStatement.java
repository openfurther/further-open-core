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
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.text.StringUtil;

/**
 * A JDBC prepared {@link PreparedStatement} decorator that logs executed SQL statements.
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
@SuppressWarnings("boxing")
class LoggingPreparedStatement extends LoggingStatement implements PreparedStatement
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(LoggingPreparedStatement.class);

	/**
	 * SQL prepared statement parameter symbol.
	 */
	private static final String PARAMETER_SYMBOL = "?";

	// ========================= FIELDS ====================================

	private final PreparedStatement statement;
	protected final String query;

	/**
	 * Keep track of parameter values set in this statement.
	 */
	private final Map<Integer, Object> parameterValues = CollectionUtil.newMap();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param statement
	 * @param query
	 */
	public static LoggingPreparedStatement newLoggingPreparedStatement(
			final PreparedStatement statement, final ImmutableLoggingOptions options,
			final String query)
	{
		return new LoggingPreparedStatement(statement, options, query);
	}

	/**
	 * @param statement
	 * @param query
	 */
	protected LoggingPreparedStatement(final PreparedStatement statement,
			final ImmutableLoggingOptions options, final String query)
	{
		super(statement, options);
		this.statement = statement;
		this.query = query;
	}

	// ========================= IMPL: Statement ===========================

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	@Override
	public ResultSet executeQuery() throws SQLException
	{
		logPreparedStatementSubstituteParameters();
		return statement.executeQuery();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	@Override
	public int executeUpdate() throws SQLException
	{
		logPreparedStatementSubstituteParameters();
		return statement.executeUpdate();
	}

	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNull(int, int)
	 */
	@Override
	public void setNull(final int parameterIndex, final int sqlType) throws SQLException
	{
		parameterValues.put(parameterIndex, null);
		statement.setNull(parameterIndex, sqlType);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	@Override
	public void setBoolean(final int parameterIndex, final boolean x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setBoolean(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setByte(int, byte)
	 */
	@Override
	public void setByte(final int parameterIndex, final byte x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setByte(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setShort(int, short)
	 */
	@Override
	public void setShort(final int parameterIndex, final short x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setShort(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setInt(int, int)
	 */
	@Override
	public void setInt(final int parameterIndex, final int x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setInt(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setLong(int, long)
	 */
	@Override
	public void setLong(final int parameterIndex, final long x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setLong(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setFloat(int, float)
	 */
	@Override
	public void setFloat(final int parameterIndex, final float x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setFloat(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setDouble(int, double)
	 */
	@Override
	public void setDouble(final int parameterIndex, final double x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setDouble(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(final int parameterIndex, final BigDecimal x)
			throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setBigDecimal(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setString(int, java.lang.String)
	 */
	@Override
	public void setString(final int parameterIndex, final String x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setString(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBytes(int, byte[])
	 */
	@Override
	public void setBytes(final int parameterIndex, final byte[] x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setBytes(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
	 */
	@Override
	public void setDate(final int parameterIndex, final Date x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setDate(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
	 */
	@Override
	public void setTime(final int parameterIndex, final Time x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setTime(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x)
			throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setTimestamp(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
	 */
	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x,
			final int length) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setAsciiStream(parameterIndex, x, length);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @deprecated
	 * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
	 */
	@Deprecated
	@Override
	public void setUnicodeStream(final int parameterIndex, final InputStream x,
			final int length) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setUnicodeStream(parameterIndex, x, length);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
	 */
	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x,
			final int length) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setBinaryStream(parameterIndex, x, length);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#clearParameters()
	 */
	@Override
	public void clearParameters() throws SQLException
	{
		parameterValues.clear();
		statement.clearParameters();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param targetSqlType
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
	 */
	@Override
	public void setObject(final int parameterIndex, final Object x,
			final int targetSqlType) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setObject(parameterIndex, x, targetSqlType);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
	 */
	@Override
	public void setObject(final int parameterIndex, final Object x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setObject(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#execute()
	 */
	@Override
	public boolean execute() throws SQLException
	{
		logPreparedStatementSubstituteParameters();
		return statement.execute();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	@Override
	public void addBatch() throws SQLException
	{
		statement.addBatch();
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
	 */
	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader,
			final int length) throws SQLException
	{
		parameterValues.put(parameterIndex, reader);
		statement.setCharacterStream(parameterIndex, reader, length);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
	 */
	@Override
	public void setRef(final int parameterIndex, final Ref x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setRef(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
	 */
	@Override
	public void setBlob(final int parameterIndex, final Blob x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setBlob(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
	 */
	@Override
	public void setClob(final int parameterIndex, final Clob x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setClob(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
	 */
	@Override
	public void setArray(final int parameterIndex, final Array x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setArray(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#getMetaData()
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException
	{
		return statement.getMetaData();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param cal
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
	 */
	@Override
	public void setDate(final int parameterIndex, final Date x, final Calendar cal)
			throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setDate(parameterIndex, x, cal);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param cal
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
	 */
	@Override
	public void setTime(final int parameterIndex, final Time x, final Calendar cal)
			throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setTime(parameterIndex, x, cal);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param cal
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp,
	 *      java.util.Calendar)
	 */
	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x,
			final Calendar cal) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setTimestamp(parameterIndex, x, cal);
	}

	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @param typeName
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
	 */
	@Override
	public void setNull(final int parameterIndex, final int sqlType, final String typeName)
			throws SQLException
	{
		parameterValues.put(parameterIndex, null);
		statement.setNull(parameterIndex, sqlType, typeName);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
	 */
	@Override
	public void setURL(final int parameterIndex, final URL x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setURL(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException
	{
		return statement.getParameterMetaData();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
	 */
	@Override
	public void setRowId(final int parameterIndex, final RowId x) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setRowId(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
	 */
	@Override
	public void setNString(final int parameterIndex, final String value)
			throws SQLException
	{
		parameterValues.put(parameterIndex, value);
		statement.setNString(parameterIndex, value);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value,
			final long length) throws SQLException
	{
		parameterValues.put(parameterIndex, value);
		statement.setNCharacterStream(parameterIndex, value, length);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
	 */
	@Override
	public void setNClob(final int parameterIndex, final NClob value) throws SQLException
	{
		parameterValues.put(parameterIndex, value);
		statement.setNClob(parameterIndex, value);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
	 */
	@Override
	public void setClob(final int parameterIndex, final Reader reader, final long length)
			throws SQLException
	{
		parameterValues.put(parameterIndex, reader);
		statement.setClob(parameterIndex, reader, length);
	}

	/**
	 * @param parameterIndex
	 * @param inputStream
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
	 */
	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream,
			final long length) throws SQLException
	{
		parameterValues.put(parameterIndex, inputStream);
		statement.setBlob(parameterIndex, inputStream, length);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
	 */
	@Override
	public void setNClob(final int parameterIndex, final Reader reader, final long length)
			throws SQLException
	{
		parameterValues.put(parameterIndex, reader);
		statement.setNClob(parameterIndex, reader, length);
	}

	/**
	 * @param parameterIndex
	 * @param xmlObject
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
	 */
	@Override
	public void setSQLXML(final int parameterIndex, final SQLXML xmlObject)
			throws SQLException
	{
		parameterValues.put(parameterIndex, xmlObject);
		statement.setSQLXML(parameterIndex, xmlObject);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param targetSqlType
	 * @param scaleOrLength
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
	 */
	@Override
	public void setObject(final int parameterIndex, final Object x,
			final int targetSqlType, final int scaleOrLength) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, long)
	 */
	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x,
			final long length) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setAsciiStream(parameterIndex, x, length);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, long)
	 */
	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x,
			final long length) throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setBinaryStream(parameterIndex, x, length);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader,
			final long length) throws SQLException
	{
		parameterValues.put(parameterIndex, reader);
		statement.setCharacterStream(parameterIndex, reader, length);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
	 */
	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x)
			throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setAsciiStream(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
	 */
	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x)
			throws SQLException
	{
		parameterValues.put(parameterIndex, x);
		statement.setBinaryStream(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader)
			throws SQLException
	{
		parameterValues.put(parameterIndex, reader);
		statement.setCharacterStream(parameterIndex, reader);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value)
			throws SQLException
	{
		parameterValues.put(parameterIndex, value);
		statement.setNCharacterStream(parameterIndex, value);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
	 */
	@Override
	public void setClob(final int parameterIndex, final Reader reader)
			throws SQLException
	{
		parameterValues.put(parameterIndex, reader);
		statement.setClob(parameterIndex, reader);
	}

	/**
	 * @param parameterIndex
	 * @param inputStream
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
	 */
	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream)
			throws SQLException
	{
		parameterValues.put(parameterIndex, inputStream);
		statement.setBlob(parameterIndex, inputStream);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
	 */
	@Override
	public void setNClob(final int parameterIndex, final Reader reader)
			throws SQLException
	{
		parameterValues.put(parameterIndex, reader);
		statement.setNClob(parameterIndex, reader);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Log prepared statement. Report query only, with substituted parameter values.
	 *
	 * @throws SQLException
	 */
	private void logPreparedStatementSubstituteParameters() throws SQLException
	{
		if (log.isInfoEnabled())
		{
			log.info(substituteParameters(parameterValues));
		}
	}

	/**
	 * Log prepared statement. Report parameters separately from the query.
	 *
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	private void logPreparedStatement() throws SQLException
	{
		if (log.isInfoEnabled())
		{
			log.info("Executing prepared statement: "
					+ substituteParameters(parameterValues));
			log.info("Executing prepared statement: " + query);
			final ParameterMetaData parameterMetaData = statement.getParameterMetaData();
			for (int i = 1; i <= parameterMetaData.getParameterCount(); i++)
			{
				log.info("Parameter " + i + ": type "
						+ parameterMetaData.getParameterType(i) + " value "
						+ parameterValues.get(i));
			}
		}
	}

	/**
	 * Substitute parameter values within the prepared SQL statement.
	 *
	 * @param parameters
	 *            a map of parameter index to value
	 * @return query string with <code>'?'</code> symbols replaced with parameter values
	 */
	private String substituteParameters(final Map<Integer, Object> parameters)
	{
		// Find largest parameter index
		int size = -1;
		for (final Integer p : parameters.keySet())
		{
			final int pValue = p.intValue();
			if (pValue > size)
			{
				size = pValue;
			}
		}

		// Replace arguments by values
		String evaluated = query;
		for (int i = 1; i <= size; i++)
		{
			final Object parameterValue = StringUtil.quote(StringUtil
					.getNullSafeToString(parameters.get(i)));
			evaluated = StringUtils.replaceOnce(evaluated, PARAMETER_SYMBOL,
					StringUtil.getNullSafeToString(parameterValue));
		}
		// Escape quote characters; useful when a JDBCAppender INSERTs the string
		// "evaluated" to a database
		if (options.isEscapeQuotes())
		{
			evaluated = evaluated.replaceAll("'", "''");
		}
		return evaluated;
	}
}
