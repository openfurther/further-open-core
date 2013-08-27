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
package edu.utah.further.core.util.log;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.text.StringUtil;

/**
 * A {@link JDBCAppender} that also filters log messages.
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
 * @version May 2, 2011
 */
public final class FilteredJdbcAppender extends AppenderSkeleton
{
	// ========================= CONSTANTS =================================

	/**
	 * Delimiter for regexs in the <code>patterns</code> string. If one of your patterns
	 * contains this delimiter, this class won't work at present.
	 * <p>
	 * TODO: make delimiter settable and before the <code>patterns</code> setter is
	 * invoked.
	 */
	private static final String PATTERN_DELIMITER = "@";

	// ========================= FIELDS ====================================

	/**
	 * List of regular expressions of fully-qualified bean class names to be auto-proxied.
	 * Maybe needed for Log4J property configuration via a JavaBean get / set standard.
	 */
	private final List<Pattern> includePatternList = CollectionUtil.newList();

	/**
	 * Unclear if Log4J can use JavaBeans on lists, so also keep a simple type for the
	 * input include patterns property.
	 */
	private String includePatterns;

	/**
	 * The Log4J appender to be wrapped by this decorator. Specific implementation type,
	 * because we need to delegate get/set.
	 */
	private final JDBCAppender jdbcAppender;

	/**
	 * Determines how logging events are filtered.
	 */
	private EventFilter filter = new EventFilterRegex();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param patterns
	 *            a delimited
	 */
	public FilteredJdbcAppender()
	{
		this.jdbcAppender = new JDBCAppender();
	}

	// ========================= IMPL: AppenderSkeleton ====================

	/**
	 *
	 * @see org.apache.log4j.jdbc.JDBCAppender#close()
	 */
	@Override
	public void close()
	{
		jdbcAppender.close();
	}

	/**
	 * @param layout
	 * @see org.apache.log4j.AppenderSkeleton#setLayout(org.apache.log4j.Layout)
	 */
	@Override
	public void setLayout(final Layout layout)
	{
		jdbcAppender.setLayout(layout);
	}

	/**
	 * @return
	 * @see org.apache.log4j.jdbc.JDBCAppender#requiresLayout()
	 */
	@Override
	public boolean requiresLayout()
	{
		return jdbcAppender.requiresLayout();
	}

	/**
	 * @param event
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	protected void append(final LoggingEvent event)
	{
		if (filter.isLoggable(event))
		{
			jdbcAppender.doAppend(event);
		}
	}

	// ========================= GET & SET =================================

	/**
	 * Set a new value for the filter property.
	 *
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(final EventFilter filter)
	{
		this.filter = filter;
	}

	/**
	 * Return the includePatterns property.
	 *
	 * @return the includePatterns
	 */
	public List<Pattern> getIncludePatternList()
	{
		return includePatternList;
	}

	/**
	 * Return the includePatterns property.
	 *
	 * @return the includePatterns
	 */
	public String getIncludePatterns()
	{
		return includePatterns;
	}

	/**
	 * Set a new value for the includePatterns property.
	 *
	 * @param includePatterns
	 *            the includePatterns to set
	 */
	public void setIncludePatterns(final String includePatterns)
	{
		this.includePatterns = includePatterns;
		final String[] parts = StringUtil.split(includePatterns, PATTERN_DELIMITER);
		this.includePatternList.clear();
		for (final String part : parts)
		{
			this.includePatternList.add(Pattern.compile(part));
		}
		((EventFilterRegex) filter).setIncludePatterns(includePatternList);
	}

	/**
	 * @param s
	 * @see org.apache.log4j.jdbc.JDBCAppender#setSql(java.lang.String)
	 */
	public void setSql(final String s)
	{
		jdbcAppender.setSql(s);
	}

	/**
	 * @return
	 * @see org.apache.log4j.jdbc.JDBCAppender#getSql()
	 */
	public String getSql()
	{
		return jdbcAppender.getSql();
	}

	/**
	 * @param user
	 * @see org.apache.log4j.jdbc.JDBCAppender#setUser(java.lang.String)
	 */
	public void setUser(final String user)
	{
		jdbcAppender.setUser(user);
	}

	/**
	 * @param url
	 * @see org.apache.log4j.jdbc.JDBCAppender#setURL(java.lang.String)
	 */
	public void setURL(final String url)
	{
		jdbcAppender.setURL(url);
	}

	/**
	 * @param password
	 * @see org.apache.log4j.jdbc.JDBCAppender#setPassword(java.lang.String)
	 */
	public void setPassword(final String password)
	{
		jdbcAppender.setPassword(password);
	}

	/**
	 * @param newBufferSize
	 * @see org.apache.log4j.jdbc.JDBCAppender#setBufferSize(int)
	 */
	public void setBufferSize(final int newBufferSize)
	{
		jdbcAppender.setBufferSize(newBufferSize);
	}

	/**
	 * @return
	 * @see org.apache.log4j.jdbc.JDBCAppender#getUser()
	 */
	public String getUser()
	{
		return jdbcAppender.getUser();
	}

	/**
	 * @return
	 * @see org.apache.log4j.jdbc.JDBCAppender#getURL()
	 */
	public String getURL()
	{
		return jdbcAppender.getURL();
	}

	/**
	 * @return
	 * @see org.apache.log4j.jdbc.JDBCAppender#getPassword()
	 */
	public String getPassword()
	{
		return jdbcAppender.getPassword();
	}

	/**
	 * @return
	 * @see org.apache.log4j.jdbc.JDBCAppender#getBufferSize()
	 */
	public int getBufferSize()
	{
		return jdbcAppender.getBufferSize();
	}

	/**
	 * @param driverClass
	 * @see org.apache.log4j.jdbc.JDBCAppender#setDriver(java.lang.String)
	 */
	public void setDriver(final String driverClass)
	{
		jdbcAppender.setDriver(driverClass);
	}

	// ========================= PRIVATE TYPES =============================


}