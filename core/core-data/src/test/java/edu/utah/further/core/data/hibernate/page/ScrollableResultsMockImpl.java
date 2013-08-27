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
package edu.utah.further.core.data.hibernate.page;

import static edu.utah.further.core.api.lang.CoreUtil.newUnsupportedOperationExceptionNotImplementedYet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.type.Type;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * A mock implementation of Hibernate's {@link ScrollableResults} for unit testing. Backed
 * by a list.
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
 * @version Sep 26, 2010
 */
final class ScrollableResultsMockImpl implements ScrollableResults
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Points to the current result set row to be returned.
	 */
	private int cursor;

	/**
	 * Interal result set to loop over. Passed to the c-tor.
	 */
	private final List<? extends PersistentEntity<?>> resultSet;

	/**
	 * A flag indicating whether the result set has been closed or not.
	 */
	private boolean closed = false;

	/**
	 * Size of {@link #resultSet}.
	 */
	private final int size;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param resultSet
	 */
	public ScrollableResultsMockImpl(final List<? extends PersistentEntity<?>> resultSet)
	{
		super();
		this.resultSet = resultSet;
		this.size = this.resultSet.size();
		beforeFirst();
	}

	// ========================= IMPL: ScrollableResults ===================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#next()
	 */
	@Override
	public boolean next() throws HibernateException
	{
		return scroll(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#previous()
	 */
	@Override
	public boolean previous() throws HibernateException
	{
		return scroll(-1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#scroll(int)
	 */
	@Override
	public boolean scroll(final int i) throws HibernateException
	{
		return setRowNumber(cursor + i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#last()
	 */
	@Override
	public boolean last() throws HibernateException
	{
		return setRowNumber(size - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#first()
	 */
	@Override
	public boolean first() throws HibernateException
	{
		return setRowNumber(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#beforeFirst()
	 */
	@Override
	public void beforeFirst() throws HibernateException
	{
		cursor = Constants.INVALID_VALUE_INTEGER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#afterLast()
	 */
	@Override
	public void afterLast() throws HibernateException
	{
		checkIfClosed();
		cursor = size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#isFirst()
	 */
	@Override
	public boolean isFirst() throws HibernateException
	{
		checkIfClosed();
		return (cursor == 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#isLast()
	 */
	@Override
	public boolean isLast() throws HibernateException
	{
		checkIfClosed();
		return (cursor == size - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#close()
	 */
	@Override
	public void close() throws HibernateException
	{
		checkIfClosed();
		closed = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#get()
	 */
	@Override
	public Object[] get() throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/**
	 * Argument i has no effect. Returns a single entity per row.
	 * 
	 * @see org.hibernate.ScrollableResults#get(int)
	 */
	@Override
	public Object get(final int i) throws HibernateException
	{
		checkIfClosed();
		if (isResultExists())
		{
			return resultSet.get(cursor);
		}
		throw new IllegalStateException("No result at current location " + cursor
				+ "; result set size " + size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getType(int)
	 */
	@Override
	public Type getType(final int i)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getInteger(int)
	 */
	@Override
	public Integer getInteger(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getLong(int)
	 */
	@Override
	public Long getLong(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getFloat(int)
	 */
	@Override
	public Float getFloat(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getBoolean(int)
	 */
	@Override
	public Boolean getBoolean(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getDouble(int)
	 */
	@Override
	public Double getDouble(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getShort(int)
	 */
	@Override
	public Short getShort(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getByte(int)
	 */
	@Override
	public Byte getByte(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getCharacter(int)
	 */
	@Override
	public Character getCharacter(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getBinary(int)
	 */
	@Override
	public byte[] getBinary(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getText(int)
	 */
	@Override
	public String getText(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getBlob(int)
	 */
	@Override
	public Blob getBlob(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getClob(int)
	 */
	@Override
	public Clob getClob(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getString(int)
	 */
	@Override
	public String getString(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getBigInteger(int)
	 */
	@Override
	public BigInteger getBigInteger(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getDate(int)
	 */
	@Override
	public Date getDate(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getLocale(int)
	 */
	@Override
	public Locale getLocale(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getCalendar(int)
	 */
	@Override
	public Calendar getCalendar(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getTimeZone(int)
	 */
	@Override
	public TimeZone getTimeZone(final int col) throws HibernateException
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#getRowNumber()
	 */
	@Override
	public int getRowNumber() throws HibernateException
	{
		checkIfClosed();
		return cursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.ScrollableResults#setRowNumber(int)
	 */
	@Override
	public boolean setRowNumber(final int rowNumber) throws HibernateException
	{
		checkIfClosed();
		cursor = Math.max(Constants.INVALID_VALUE_INTEGER, Math.min(rowNumber, size));
		return isResultExists();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Throw an exception if the result set is already closed.
	 */
	private void checkIfClosed()
	{
		if (closed)
		{
			throw new IllegalStateException("Cannot operate on a closed result set!");
		}
	}

	/**
	 * @return
	 */
	private boolean isResultExists()
	{
		return ((cursor >= 0) && (cursor < size));
	}
}
