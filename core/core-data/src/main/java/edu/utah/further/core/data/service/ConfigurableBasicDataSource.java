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
package edu.utah.further.core.data.service;

import static edu.utah.further.core.api.message.Messages.unsupportedOperationMessage;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.core.util.io.ClasspathUtil.getThreadClassLoader;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A wrapper around a {@link BasicDataSource} that allows easy configuration from a
 * properties file, whose format looks like that (properties' names should be exactly the
 * way they are below, values will change, of course, depending on your case):
 * <blockquote>
 * 
 * <pre>
 * datasource.url=jdbc:hsqldb:mem:cs
 * datasource.driver=org.hsqldb.jdbcDriver
 * datasource.user=sa
 * datasource.password=
 * # SQL implementation Hibernate dialect
 * hibernate.dialect=org.hibernate.dialect.HSQLDialect
 * </pre>
 * 
 * </blockquote>
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
 * @version Oct 24, 2008
 */
// May need to unfinalize if will be using Spring DI or proxies on this class in the
// future
public final class ConfigurableBasicDataSource implements DataSource, Named
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ConfigurableBasicDataSource.class);

	/**
	 * Default data source name.
	 */
	private static final String DEFAULT_NAME = "Configurable Data Source";

	/**
	 * Centralizes properties names used by this bean.
	 */
	private static abstract class PROPERTIES
	{
		private static String PREFIX = "datasource.";
		static String URL = PREFIX + "url";
		static String DRIVER = PREFIX + "driver";
		static String USER = PREFIX + "user";
		static String PASSWORD = PREFIX + "password";
	}

	// ========================= FIELDS ====================================

	/**
	 * The output set of annotated classes.
	 */
	private final BasicDataSource dataSource;

	/**
	 * An optional name of this data source.
	 */
	private String name = DEFAULT_NAME;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a wrapped data source.
	 * 
	 * @param basicDataSource
	 *            data source managed internally
	 */
	public ConfigurableBasicDataSource(final BasicDataSource basicDataSource)
	{
		super();
		this.dataSource = basicDataSource;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("name", getName())
				.append("url", getUrl())
				.append("driver", getDriverClassName())
				.append("username", getUsername())
				.toString();
	}

	// ========================= IMPLEMENTATION: Named =====================

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	// ========================= IMPLEMENTATION: BasicDataSource ===========

	/**
	 * @param propertyName
	 * @param propertyValue
	 * @see org.apache.commons.dbcp.BasicDataSource#addConnectionProperty(java.lang.String,
	 *      java.lang.String)
	 */
	public void addConnectionProperty(final String propertyName,
			final String propertyValue)
	{
		dataSource.addConnectionProperty(propertyName, propertyValue);
	}

	/**
	 * @throws SQLException
	 * @see org.apache.commons.dbcp.BasicDataSource#close()
	 */
	public void close() throws SQLException
	{
		dataSource.close();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see org.apache.commons.dbcp.BasicDataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException
	{
		// Work around Oracle MBean initialization bug.
		// @see
		// http://www.robert-stupp.de/blog/2008/01/oracle_malicious_11g_data_source_implementation.html
		// -- we only implement an error-swallow instead, because it seems that the
		// returned connection from dataSource.getConnection() is fine.
		try
		{
			if (log.isWarnEnabled())
			{
				log
						.trace("Note: if this is an oracle driver, it may throw and error after this line that can be ignored.");
			}
			return dataSource.getConnection();
		}
		catch (final Throwable e)
		{
			if (log.isInfoEnabled())
			{
				log
						.info("Driver threw an error. If this is an oracle driver, it may be a bug in the oracle JDBC client that is ignored here as the connection is usually fine nevertheless.");
				final String loader = getThreadClassLoader().toString();
				log.info("Current thread class loader toString() returns: " + loader);
				log.info("Error message: " + e.getMessage());
			}
			return dataSource.getConnection();
		}
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 * @see org.apache.commons.dbcp.BasicDataSource#getConnection(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Connection getConnection(final String username, final String password)
			throws SQLException
	{
		return dataSource.getConnection(username, password);
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getDefaultAutoCommit()
	 */
	public boolean getDefaultAutoCommit()
	{
		return dataSource.getDefaultAutoCommit();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getDefaultReadOnly()
	 */
	public boolean getDefaultReadOnly()
	{
		return dataSource.getDefaultReadOnly();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getDefaultTransactionIsolation()
	 */
	public int getDefaultTransactionIsolation()
	{
		return dataSource.getDefaultTransactionIsolation();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getDriverClassName()
	 */
	public String getDriverClassName()
	{
		return dataSource.getDriverClassName();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see org.apache.commons.dbcp.BasicDataSource#getLoginTimeout()
	 */
	@Override
	public int getLoginTimeout() throws SQLException
	{
		return dataSource.getLoginTimeout();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see org.apache.commons.dbcp.BasicDataSource#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException
	{
		return dataSource.getLogWriter();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getMaxActive()
	 */
	public int getMaxActive()
	{
		return dataSource.getMaxActive();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getMaxIdle()
	 */
	public int getMaxIdle()
	{
		return dataSource.getMaxIdle();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getMaxWait()
	 */
	public long getMaxWait()
	{
		return dataSource.getMaxWait();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getMinEvictableIdleTimeMillis()
	 */
	public long getMinEvictableIdleTimeMillis()
	{
		return dataSource.getMinEvictableIdleTimeMillis();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getMinIdle()
	 */
	public int getMinIdle()
	{
		return dataSource.getMinIdle();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getNumActive()
	 */
	public int getNumActive()
	{
		return dataSource.getNumActive();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getNumIdle()
	 */
	public int getNumIdle()
	{
		return dataSource.getNumIdle();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getNumTestsPerEvictionRun()
	 */
	public int getNumTestsPerEvictionRun()
	{
		return dataSource.getNumTestsPerEvictionRun();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getPassword()
	 */
	public String getPassword()
	{
		return dataSource.getPassword();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getTestOnBorrow()
	 */
	public boolean getTestOnBorrow()
	{
		return dataSource.getTestOnBorrow();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getTestOnReturn()
	 */
	public boolean getTestOnReturn()
	{
		return dataSource.getTestOnReturn();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getTestWhileIdle()
	 */
	public boolean getTestWhileIdle()
	{
		return dataSource.getTestWhileIdle();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getTimeBetweenEvictionRunsMillis()
	 */
	public long getTimeBetweenEvictionRunsMillis()
	{
		return dataSource.getTimeBetweenEvictionRunsMillis();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getUrl()
	 */
	public String getUrl()
	{
		return dataSource.getUrl();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getUsername()
	 */
	public String getUsername()
	{
		return dataSource.getUsername();
	}

	/**
	 * @return
	 * @see org.apache.commons.dbcp.BasicDataSource#getValidationQuery()
	 */
	public String getValidationQuery()
	{
		return dataSource.getValidationQuery();
	}

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException
	{
		// return dataSource.isWrapperFor(iface);
		return false;
	}

	/**
	 * @param defaultAutoCommit
	 * @see org.apache.commons.dbcp.BasicDataSource#setDefaultAutoCommit(boolean)
	 */
	public void setDefaultAutoCommit(final boolean defaultAutoCommit)
	{
		dataSource.setDefaultAutoCommit(defaultAutoCommit);
	}

	/**
	 * @param defaultReadOnly
	 * @see org.apache.commons.dbcp.BasicDataSource#setDefaultReadOnly(boolean)
	 */
	public void setDefaultReadOnly(final boolean defaultReadOnly)
	{
		dataSource.setDefaultReadOnly(defaultReadOnly);
	}

	/**
	 * @param defaultTransactionIsolation
	 * @see org.apache.commons.dbcp.BasicDataSource#setDefaultTransactionIsolation(int)
	 */
	public void setDefaultTransactionIsolation(final int defaultTransactionIsolation)
	{
		dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
	}

	/**
	 * @param driverClassName
	 * @see org.apache.commons.dbcp.BasicDataSource#setDriverClassName(java.lang.String)
	 */
	public void setDriverClassName(final String driverClassName)
	{
		dataSource.setDriverClassName(driverClassName);
	}

	/**
	 * @param loginTimeout
	 * @throws SQLException
	 * @see org.apache.commons.dbcp.BasicDataSource#setLoginTimeout(int)
	 */
	@Override
	public void setLoginTimeout(final int loginTimeout) throws SQLException
	{
		dataSource.setLoginTimeout(loginTimeout);
	}

	/**
	 * @param logWriter
	 * @throws SQLException
	 * @see org.apache.commons.dbcp.BasicDataSource#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(final PrintWriter logWriter) throws SQLException
	{
		dataSource.setLogWriter(logWriter);
	}

	/**
	 * @param maxActive
	 * @see org.apache.commons.dbcp.BasicDataSource#setMaxActive(int)
	 */
	public void setMaxActive(final int maxActive)
	{
		dataSource.setMaxActive(maxActive);
	}

	/**
	 * @param maxIdle
	 * @see org.apache.commons.dbcp.BasicDataSource#setMaxIdle(int)
	 */
	public void setMaxIdle(final int maxIdle)
	{
		dataSource.setMaxIdle(maxIdle);
	}

	/**
	 * @param maxWait
	 * @see org.apache.commons.dbcp.BasicDataSource#setMaxWait(long)
	 */
	public void setMaxWait(final long maxWait)
	{
		dataSource.setMaxWait(maxWait);
	}

	/**
	 * @param minEvictableIdleTimeMillis
	 * @see org.apache.commons.dbcp.BasicDataSource#setMinEvictableIdleTimeMillis(long)
	 */
	public void setMinEvictableIdleTimeMillis(final long minEvictableIdleTimeMillis)
	{
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
	}

	/**
	 * @param minIdle
	 * @see org.apache.commons.dbcp.BasicDataSource#setMinIdle(int)
	 */
	public void setMinIdle(final int minIdle)
	{
		dataSource.setMinIdle(minIdle);
	}

	/**
	 * @param numTestsPerEvictionRun
	 * @see org.apache.commons.dbcp.BasicDataSource#setNumTestsPerEvictionRun(int)
	 */
	public void setNumTestsPerEvictionRun(final int numTestsPerEvictionRun)
	{
		dataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
	}

	/**
	 * @param password
	 * @see org.apache.commons.dbcp.BasicDataSource#setPassword(java.lang.String)
	 */
	public void setPassword(final String password)
	{
		dataSource.setPassword(password);
	}

	/**
	 * @param testOnBorrow
	 * @see org.apache.commons.dbcp.BasicDataSource#setTestOnBorrow(boolean)
	 */
	public void setTestOnBorrow(final boolean testOnBorrow)
	{
		dataSource.setTestOnBorrow(testOnBorrow);
	}

	/**
	 * @param testOnReturn
	 * @see org.apache.commons.dbcp.BasicDataSource#setTestOnReturn(boolean)
	 */
	public void setTestOnReturn(final boolean testOnReturn)
	{
		dataSource.setTestOnReturn(testOnReturn);
	}

	/**
	 * @param testWhileIdle
	 * @see org.apache.commons.dbcp.BasicDataSource#setTestWhileIdle(boolean)
	 */
	public void setTestWhileIdle(final boolean testWhileIdle)
	{
		dataSource.setTestWhileIdle(testWhileIdle);
	}

	/**
	 * @param timeBetweenEvictionRunsMillis
	 * @see org.apache.commons.dbcp.BasicDataSource#setTimeBetweenEvictionRunsMillis(long)
	 */
	public void setTimeBetweenEvictionRunsMillis(final long timeBetweenEvictionRunsMillis)
	{
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
	}

	/**
	 * @param url
	 * @see org.apache.commons.dbcp.BasicDataSource#setUrl(java.lang.String)
	 */
	public void setUrl(final String url)
	{
		dataSource.setUrl(url);
	}

	/**
	 * @param username
	 * @see org.apache.commons.dbcp.BasicDataSource#setUsername(java.lang.String)
	 */
	public void setUsername(final String username)
	{
		dataSource.setUsername(username);
	}

	/**
	 * @param validationQuery
	 * @see org.apache.commons.dbcp.BasicDataSource#setValidationQuery(java.lang.String)
	 */
	public void setValidationQuery(final String validationQuery)
	{
		dataSource.setValidationQuery(validationQuery);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.CommonDataSource#getParentLogger()
	 */
	@Override
	public java.util.logging.Logger getParentLogger()
			throws SQLFeatureNotSupportedException
	{
		return java.util.logging.Logger
				.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
	}

	/**
	 * @param <T>
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException
	{
		throw new ApplicationException(unsupportedOperationMessage("unwrap()"));
	}

	// ========================= DEPENDENCY INJECTION ======================

	/**
	 * Set a new value for the properties property.
	 * 
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final Properties properties)
	{
		// Set the data sources' properties according the properties object;
		// basically, we are following the Adapter Pattern.
		dataSource.setUrl((String) properties.get(PROPERTIES.URL));
		dataSource.setDriverClassName((String) properties.get(PROPERTIES.DRIVER));
		dataSource.setUsername((String) properties.get(PROPERTIES.USER));
		dataSource.setPassword((String) properties.get(PROPERTIES.PASSWORD));
	}

	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}
}
