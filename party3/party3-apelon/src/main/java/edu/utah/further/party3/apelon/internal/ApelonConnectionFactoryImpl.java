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
package edu.utah.further.party3.apelon.internal;

import static edu.utah.further.party3.apelon.internal.ApelonConnectionFactory.ConfigurationPropertyName.CONNECTION_TYPE;
import static edu.utah.further.party3.apelon.internal.ApelonConnectionFactory.ConfigurationPropertyName.HOST;
import static edu.utah.further.party3.apelon.internal.ApelonConnectionFactory.ConfigurationPropertyName.INSTANCE;
import static edu.utah.further.party3.apelon.internal.ApelonConnectionFactory.ConfigurationPropertyName.PASSWORD;
import static edu.utah.further.party3.apelon.internal.ApelonConnectionFactory.ConfigurationPropertyName.PORT;
import static edu.utah.further.party3.apelon.internal.ApelonConnectionFactory.ConfigurationPropertyName.USER;
import static java.lang.Integer.parseInt;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.apelon.apelonserver.client.ApelonException;
import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.apelonserver.client.ServerConnectionJDBC;
import com.apelon.apelonserver.client.ServerConnectionSecureSocket;
import com.apelon.apelonserver.client.ServerConnectionSocket;
import com.apelon.apelonserver.client.TransientSCSocket;
import com.apelon.dts.client.common.DTSHeader;

/**
 * A DTS connection factory.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 3, 2008
 */
public final class ApelonConnectionFactoryImpl implements ApelonConnectionFactory
{
	// ========================= CONSTANTS =================================

	/**
	 * Invalid value of an integer field. May serve as a useful default value or when an
	 * index is not found in an array.
	 */
	private static final int INVALID_VALUE_INTEGER = -1;

	/**
	 * Supported connection methods.
	 */
	public static enum ConnectionType
	{
		SOCKET, SECURE_SOCKET, TRANSIENT_SOCKET, JDBC
	}

	/**
	 * A logger that helps identify this class' printouts.
	 */
	protected static final Logger log = getLogger(ApelonConnectionFactoryImpl.class);

	/**
	 * Wraps a DTS connection for use in a {@link ThreadLocal} pool.
	 */
	protected class ConnectionContext
	{
		// Counts how many connections have been requested within each other
		// (enabled nested sessions)
		int depth = 0;
		ServerConnection connection;
	}

	// ========================= FIELDS ====================================

	/**
	 * A thread-local pool of DTS connections.
	 */
	private final ThreadLocal<ConnectionContext> threadLocal = new ThreadLocal<ConnectionContext>()
	{
		/**
		 * @return
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected ConnectionContext initialValue()
		{
			if (log.isDebugEnabled())
			{
				log.debug("Initializing session tracking in thread " + this);
			}
			return new ConnectionContext();
		}

	};

	// ========================= DEPENDENCIES ==============================

	/**
	 * DTS configuration properties.
	 */
	protected Properties config;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a DTS connection factory. Does not set the config property.
	 */
	public ApelonConnectionFactoryImpl()
	{
		// Nothing to do
	}

	/**
	 * Create a DTS connection factory.
	 * 
	 * @param configFileName
	 *            configuration properties file name on the classpath.
	 */
	public ApelonConnectionFactoryImpl(final String configFileName)
	{
		setConfigFromFileName(configFileName);
	}

	// ========================= IMPLEMENTATION: ConnectionFactory =========

	/**
	 * Initialize a DTS connection session. Sessions may be (properly only, of course)
	 * nested; only one connection is started and stopped for the most outer session. This
	 * corresponds to Spring's transaction <code>Propagation.SUPPORTS</code>.
	 * 
	 * @see edu.utah.further.party3.apelon.internal.internal.ApelonConnectionFactory.api.service.ConnectionFactory#startSession()
	 */
	@Override
	public void startSession()
	{
		final ConnectionContext c = threadLocal.get();
		if (c.depth == 0)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Starting connection in thread " + threadLocal);
			}
			// Allows sessions to be nested
			c.connection = getConnection(config);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("Marking nested session (" + c.depth + ")");
			}
		}
		c.depth++;

		// finally
		// {
		// // The following *may* be needed to prevent memory leaks; see
		// // http://crazybob.org/2006/07/hard-core-java-threadlocal.html
		// threadLocal.remove();
		// }
	}

	/**
	 * Return the DTS connection bound to the current context (= thread). A DTS session
	 * must be started before this method is called.
	 * 
	 * @return the DTS connection in the current context
	 * @throws IllegalStateException
	 *             if this method is called outside a DTS session
	 * @see edu.utah.further.party3.apelon.internal.internal.ApelonConnectionFactory.api.service.ConnectionFactory#getCurrentConnection()
	 */
	@Override
	public ServerConnection getCurrentConnection()
	{
		final ConnectionContext c = threadLocal.get();
		if (c.connection == null)
		{
			throw new IllegalStateException(
					"Attempted to access a DTS connection outside a session");
		}
		return c.connection;
		// finally
		// {
		// // The following *may* be needed to prevent memory leaks; see
		// // http://crazybob.org/2006/07/hard-core-java-threadlocal.html
		// threadLocal.remove();
		// }
	}

	/**
	 * Close the current connection.
	 */
	@Override
	public void closeSession()
	{
		final ConnectionContext c = threadLocal.get();
		c.depth--;
		if (c.depth == 0)
		{
			try
			{
				if (log.isDebugEnabled())
				{
					log.debug("Closing connection in thread " + threadLocal);
				}
				c.connection.close();
			}
			catch (final Exception ignored)
			{
			}
			threadLocal.remove();
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("Closing nested session (" + c.depth + ")");
			}
		}
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the connection configuration properties. This is a defensive copy of the
	 * internal {@link Properties} object held by this service, with the connection
	 * password starred out to prevent a security breach when using this method to throw
	 * an exception and propagate it to a web service response.
	 * 
	 * @return the connection configuration {@link Properties} object
	 */
	public Properties getConfig()
	{
		final Properties copy = new Properties();
		for (final String key : copy.stringPropertyNames())
		{
			copy.setProperty(key, copy.getProperty(key));
		}
		copy.setProperty(ApelonConnectionFactory.ConfigurationPropertyName.PASSWORD,
				"*****");
		return copy;
	}

	/**
	 * Set a new value for the config property.
	 * 
	 * @param config
	 *            the config to set
	 */
	public void setConfig(final Properties config)
	{
		this.config = config;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Establish a connection to the DTS. Returns our API's server connection.
	 * 
	 * @param config
	 *            configuration properties
	 * @return a ready DTS connection
	 */
	protected static ServerConnection getConnection(final Properties config)
	{
		return getApelonConnection(config);
	}

	/**
	 * Establish a connection to the Apelon DTS. A factory method that instantiates the
	 * connection according to the type specified in the configuration property
	 * <code>connectionType</code>, which must be convertible to the enum type
	 * {@link ConnectionType}.
	 * 
	 * @param config
	 *            configuration properties
	 * @return a ready DTS connection
	 */
	protected static ServerConnection getApelonConnection(final Properties config)
	{
		// Read properties
		final String connectionTypeStr = config.getProperty(CONNECTION_TYPE);
		final ConnectionType connectionType = valueOfNullSafe(ConnectionType.class,
				connectionTypeStr);
		if (connectionType == null)
		{
			throw new UnsupportedOperationException("Unrecognized connection type "
					+ connectionTypeStr);
		}

		final String host = config.getProperty(HOST);
		final int port = getConfigPort(config);
		final String user = config.getProperty(USER);
		final String password = config.getProperty(PASSWORD);
		final String instance = config.getProperty(INSTANCE);

		// *************************************************************************
		// Open a connection
		// *************************************************************************
		try
		{
			switch (connectionType)
			{
			// To open an unsecure socket connection...
				case SOCKET:
				{
					return getConnectionSocket(host, port);
				}

				// To open a secure socket connection...
				case SECURE_SOCKET:
				{
					return getConnectionSecureSocket(host, port, user, password);
				}

				// To open a transient socket connection ...
				case TRANSIENT_SOCKET:
				{
					return getConnectionTransientSocket(host, port);
				}

				// To open an 'in-process' connection via JDBC ...
				case JDBC:
				{
					return getConnectionJDBC(user, password, host, instance, port);
				}

				default:
				{
					throw new UnsupportedOperationException(
							"Unrecognized connection type " + connectionType);
				}
			}
		}
		catch (final ApelonException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This is an example of creating an 'in-process' ServerConnection, where client and
	 * server would all reside withing one JVM instance.
	 * 
	 * @param user
	 *            Database user
	 * @param password
	 *            Database password
	 * @param host
	 *            Database host machine
	 * @param instance
	 *            Oracle instance (or SQL Server database name)
	 * @param port
	 *            Database port
	 * @return ServerConnectionJDBC
	 * @throws ClassNotFoundException
	 */
	private static ServerConnectionJDBC getConnectionJDBC(final String user,
			final String password, final String host, final String instance,
			final int port)
	{
		// Create and return our ServerConnectionJDBC.
		final ServerConnectionJDBC connJDBC = new ServerConnectionJDBC(user, password,
				host, port, instance);

		try
		{
			connJDBC.setQueryServer(com.apelon.dts.server.OntylogConceptServer.class,
					DTSHeader.ONTYLOGCONCEPTSERVER_HEADER);
		}
		catch (final ClassNotFoundException e)
		{
			log.error("Server Query class not found: " + e.getMessage());
			e.printStackTrace();
		}

		return connJDBC;
	}

	/**
	 * Creating a SocketConnection simply requires the host and port number of the DTS
	 * Server.
	 * 
	 * @param host
	 *            Host name (or IP address) of machine DTS Server is running on
	 * @param port
	 *            Port that DTS Server is running on
	 * @return ServerConnectionSocket
	 * @throws ApelonException
	 */
	private static ServerConnectionSocket getConnectionSocket(final String host,
			final int port) throws ApelonException
	{
		// Pass host and port to ServerConnectionSocket constructor
		return new ServerConnectionSocket(host, port);
	}

	/**
	 * Creates a ServerConnectionSecureSocket - Used when DTS Server has authentication
	 * turned on.
	 * 
	 * @param host
	 *            Host name(or IP address) of machine DTS Server is running on
	 * @param port
	 *            Port that DTS Server is running on
	 * @param user
	 *            DTS Username
	 * @param pass
	 *            DTS User Password
	 * @return ServerConnectionSecureSocket
	 * @throws ApelonException
	 */
	private static ServerConnectionSecureSocket getConnectionSecureSocket(
			final String host, final int port, final String user, final String pass)
			throws ApelonException
	{
		// Pass host, port, DTS user, and DTS password
		return new ServerConnectionSecureSocket(host, port, user, pass);
	}

	/**
	 * Creates a TransientSCSocket - Used when you want the connection to be closed in
	 * between calls to the server
	 * 
	 * @param host
	 *            Host name(or IP address) of machine DTS Server is running on
	 * @param port
	 *            Port that DTS Server is running on
	 * @return TransientSCSocket
	 * @throws ApelonException
	 */
	private static TransientSCSocket getConnectionTransientSocket(final String host,
			final int port) throws ApelonException
	{
		// Pass host and port
		return new TransientSCSocket(host, port);
	}

	/**
	 * Convert a {@link String} to {@link Integer}.
	 * 
	 * @param value
	 *            string value
	 * @return integer value. If fail to convert, returns
	 *         <code>INVALID_VALUE_INTEGER</code>
	 */
	private static int getAsInteger(final String value)
	{
		try
		{
			return parseInt(value);
		}
		catch (final Exception e)
		{
			return INVALID_VALUE_INTEGER;
		}
	}

	/**
	 * Returns the enum constant of the specified enum type with the specified name. The
	 * name must match exactly an identifier used to declare an enum constant in this
	 * type. If it does not, this method returns <code>null</code>.
	 * 
	 * @param enumType
	 *            the Class object of the enum type from which to return a constant
	 * @param name
	 *            the name of the constant to return
	 * @return the enum constant of the specified enum type with the specified name or
	 *         <code>null</code> if not found
	 * @see {link Enum.valueOf}
	 */
	private static <E extends Enum<E>> E valueOfNullSafe(Class<E> enumType, String name)
	{
		try
		{
			return Enum.valueOf(enumType, name);
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}

	/**
	 * Run initialization procedures.
	 * 
	 * @see edu.utah.further.portal.beans.BaseBackingBean#postConstruct()
	 */
	@PostConstruct
	protected void postConstruct()
	{
		if (config == null)
		{
			throw new IllegalStateException("DTS connection configuration not found");
		}
	}

	/**
	 * @param configFileName
	 */
	@SuppressWarnings("resource")
	// Resource is closed in finally block
	private void setConfigFromFileName(final String configFileName)
	{
		config = new Properties();
		InputStream is = null;
		try
		{
			is = new ClassPathResource(configFileName).getInputStream();
			config.load(is);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				// ignore
			}
		}
	}

	/**
	 * @param config
	 * @return
	 */
	private static int getConfigPort(final Properties config)
	{
		final String portStr = config.getProperty(PORT);
		final int port = getAsInteger(portStr);
		if (port == INVALID_VALUE_INTEGER)
		{
			throw new UnsupportedOperationException("Invalid port " + portStr);
		}
		return port;
	}
}
