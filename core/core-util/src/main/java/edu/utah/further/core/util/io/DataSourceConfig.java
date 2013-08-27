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
package edu.utah.further.core.util.io;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Properties;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.discrete.EnumUtil;

/**
 * A placeholder of database connection configuration properties.
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
 * @version Feb 3, 2011
 */
public final class DataSourceConfig implements Named
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(DataSourceConfig.class);

	/**
	 * Default name.
	 */
	private static final String DEFAULT_NAME = "Configurable Data Source";

	/**
	 * Centralizes properties names used by this bean.
	 */
	private static abstract class PROPERTIES
	{
		private static String PREFIX = "datasource.";
		static String URL = "url";
		static String DRIVER = "driver";
		static String USER = "user";
		static String PASSWORD = "password";
		static String PORT = "port";
	}

	// ========================= FIELDS ====================================

	/**
	 * An optional name of this data source.
	 */
	private String name = DEFAULT_NAME;

	/**
	 * Host name, parsed from JDBC URL.
	 */
	private String host;

	/**
	 * Full connection URL.
	 */
	private String url;

	/**
	 * Port.
	 */
	private String port;

	/**
	 * JDBC driver class name.
	 */
	private String driverClassName;

	/**
	 * Database type.
	 */
	private DataSourceType dataSourceType;

	/**
	 * Connection user name.
	 */
	private String username;

	/**
	 * Connection password.
	 */
	private String password;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Instantiate an empty config.
	 */
	public DataSourceConfig()
	{
		super();
	}

	/**
	 * Load a config from a Properties object using the standard placeholder prefix.
	 *
	 * @param properties
	 *            properties object
	 */
	public DataSourceConfig(final Properties properties)
	{
		this();
		setProperties(properties);
	}

	/**
	 * Load a config from a Properties object using a custom placeholder prefix.
	 *
	 * @param properties
	 *            properties object
	 * @param prefix
	 *            prefix
	 */
	public DataSourceConfig(final Properties properties, final String prefix)
	{
		this();
		setProperties(properties, prefix);
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
				.append("host", getHost())
				.append("port", getPort())
				.append("username", getUsername())
				.append("driver", getDriverClassName())
				.append("url", getUrl())
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

	// ========================= GET & SET =================================
	/**
	 * @param url
	 * @see org.apache.commons.dbcp.BasicDataSource#setUrl(java.lang.String)
	 */
	public void setUrl(final String url)
	{
		this.url = url;

		final String[] parts = url.split("\\:");
		for (int i = 0; i < parts.length; i++)
		{
			if (parts[i].startsWith("//"))
			{
				host = parts[i].replaceFirst("//", "");
			}
		}

		setDataSourceType(parts[1]);
	}

	/**
	 * Set a new value for the dataSourceType property.
	 *
	 * @param dataSourceType
	 *            the dataSourceType to set
	 */
	public void setDataSourceType(final String dataSourceType)
	{
		DataSourceType value = EnumUtil.valueOfNullSafe(DataSourceType.class,
				dataSourceType);
		if (value == null)
		{
			value = DataSourceType.other;
		}
		setDataSourceType(value);
	}

	/**
	 * Set a new value for the dataSourceType property.
	 *
	 * @param dataSourceType
	 *            the dataSourceType to set
	 */
	public void setDataSourceType(final DataSourceType dataSourceType)
	{
		this.dataSourceType = dataSourceType;
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

	/**
	 * Return the host property.
	 *
	 * @return the host
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * Set a new value for the host property.
	 *
	 * @param host
	 *            the host to set
	 */
	public void setHost(final String host)
	{
		this.host = host;
	}

	/**
	 * Return the port property.
	 *
	 * @return the port
	 */
	public String getPort()
	{
		return port;
	}

	/**
	 * Set a new value for the port property.
	 *
	 * @param port
	 *            the port to set
	 */
	public void setPort(final String port)
	{
		this.port = port;
	}

	/**
	 * Return the driverClassName property.
	 *
	 * @return the driverClassName
	 */
	public String getDriverClassName()
	{
		return driverClassName;
	}

	/**
	 * Set a new value for the driverClassName property.
	 *
	 * @param driverClassName
	 *            the driverClassName to set
	 */
	public void setDriverClassName(final String driverClassName)
	{
		this.driverClassName = driverClassName;
	}

	/**
	 * Return the url property.
	 *
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * Return the username property.
	 *
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Set a new value for the username property.
	 *
	 * @param username
	 *            the username to set
	 */
	public void setUsername(final String username)
	{
		this.username = username;
	}

	/**
	 * Return the password property.
	 *
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Set a new value for the password property.
	 *
	 * @param password
	 *            the password to set
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * Return the dataSourceType property.
	 *
	 * @return the dataSourceType
	 */
	public DataSourceType getDataSourceType()
	{
		return dataSourceType;
	}

	// ========================= METHODS ===================================

	/**
	 * Set a new value for the properties property.
	 *
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final Properties properties)
	{
		setProperties(properties, PROPERTIES.PREFIX);
	}

	/**
	 * Set a new value for the properties property.
	 *
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final Properties properties, final String prefix)
	{
		// Set the data sources' properties according the properties object;
		// basically, we are following the Adapter Pattern.
		setUrl((String) properties.get(prefix + PROPERTIES.URL));
		setDriverClassName((String) properties.get(prefix + PROPERTIES.DRIVER));
		setUsername((String) properties.get(prefix + PROPERTIES.USER));
		setPassword((String) properties.get(prefix + PROPERTIES.PASSWORD));
		setPort((String) properties.get(prefix + PROPERTIES.PORT));
	}
}
