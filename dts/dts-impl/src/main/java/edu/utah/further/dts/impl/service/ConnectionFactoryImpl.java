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
package edu.utah.further.dts.impl.service;

import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_INTEGER;
import static edu.utah.further.core.api.constant.Strings.MASKED_PASSWORD;
import static edu.utah.further.core.api.discrete.EnumUtil.valueOfNullSafe;
import static edu.utah.further.core.api.text.StringUtil.getAsInteger;
import static edu.utah.further.dts.api.service.ConfigurationPropertyName.CONNECTION_TYPE;
import static edu.utah.further.dts.api.service.ConfigurationPropertyName.HOST;
import static edu.utah.further.dts.api.service.ConfigurationPropertyName.INSTANCE;
import static edu.utah.further.dts.api.service.ConfigurationPropertyName.PASSWORD;
import static edu.utah.further.dts.api.service.ConfigurationPropertyName.PORT;
import static edu.utah.further.dts.api.service.ConfigurationPropertyName.USER;
import static org.apache.commons.lang.Validate.notNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.apelon.apelonserver.client.ApelonException;
import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.apelonserver.client.ServerConnectionJDBC;
import com.apelon.apelonserver.client.ServerConnectionSecureSocket;
import com.apelon.apelonserver.client.ServerConnectionSocket;
import com.apelon.apelonserver.client.TransientSCSocket;

import edu.utah.further.core.api.io.LogLevel;
import edu.utah.further.core.api.io.SystemOutputStream;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.api.lang.CoreUtil;
import edu.utah.further.core.util.io.SuppressSystemStream;
import edu.utah.further.core.util.io.SuppressSystemStreams;
import edu.utah.further.dts.api.service.ConnectionFactory;
import edu.utah.further.dts.api.service.DtsServerConnection;

/**
 * An Apelon DTS connection factory implementation. Note: a query server is NOT set on the
 * connection and is the responsibility of client classes of the produced connection
 * object.
 * <p>
 * This class' purpose is to maintain a thread-local DTS connection model and logical
 * "session scope" with artitrary scope nesting (using the <code>depth</code> variable).
 * It delegates to internal {@link ServerConnection} builder to build the Apelon object.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 3, 2008
 */
@Service("connectionFactory")
@SuppressSystemStreams(
{ @SuppressSystemStream(stream = SystemOutputStream.OUT, level = LogLevel.INFO),
		@SuppressSystemStream(stream = SystemOutputStream.ERR, level = LogLevel.ERROR) })
public class ConnectionFactoryImpl implements ConnectionFactory
{
	// ========================= CONSTANTS =================================

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
	private static final Logger log = getLogger(ConnectionFactoryImpl.class);

	/**
	 * Wraps a DTS connection for use in a {@link ThreadLocal} pool.
	 */
	private class ConnectionContext
	{
		// Counts how many connections have been requested within each other
		// (enabled nested sessions)
		int depth = 0;
		DtsServerConnection connection;
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
		@SuppressWarnings("synthetic-access")
		@Override
		protected synchronized ConnectionContext initialValue()
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
	private Properties config;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Run initialization procedures.
	 */
	@PostConstruct
	private void postConstruct()
	{
		notNull(config, "DTS connection configuration not found");
	}

	// ========================= IMPLEMENTATION: ConnectionFactory =========

	/**
	 * Initialize a DTS connection session. Sessions may be (properly only, of course)
	 * nested; only one connection is started and stopped for the most outer session. This
	 * corresponds to Spring's transaction <code>Propagation.SUPPORTS</code>.
	 *
	 * @see edu.utah.further.dts.api.service.ConnectionFactory#startSession()
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
	 * Checks if there is currently an open DTS session.
	 *
	 * @return <code>true</code> if and only if the DTS connection bound to the current
	 *         context (=thread) is non-<code>null</code>
	 * @see edu.utah.further.dts.api.service.ConnectionFactory#isSessionOpen()
	 */
	@Override
	public boolean isSessionOpen()
	{
		final ConnectionContext c = threadLocal.get();
		return c.connection != null;
	}

	/**
	 * Return the DTS connection bound to the current context (= thread). A DTS session
	 * must be started before this method is called.
	 *
	 * @return the DTS connection in the current context
	 * @throws IllegalStateException
	 *             if this method is called outside a DTS session
	 * @see edu.utah.further.dts.api.service.ConnectionFactory#getCurrentConnection()
	 */
	@Override
	public DtsServerConnection getCurrentConnection()
	{
		final ConnectionContext c = threadLocal.get();
		if (c.connection == null)
		{
			log.error("Attempted to access a DTS connection outside a session");
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
	 *
	 * @see edu.utah.further.dts.api.service.ConnectionFactory#closeSession()
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

	/**
	 * Return the connection configuration properties. This is a defensive copy of the
	 * internal {@link Properties} object held by this service, with the connection
	 * password starred out to prevent a security breach when using this method to throw
	 * an exception and propagate it to a web service response.
	 *
	 * @return the connection configuration {@link Properties} object
	 * @see edu.utah.further.dts.api.service.ConnectionFactory#getConfig()
	 */
	@Override
	public Properties getConfig()
	{
		final Properties copy = CoreUtil.copyProperties(config);
		copy.setProperty(PASSWORD, MASKED_PASSWORD);
		return copy;
	}

	// ========================= GETTERS & SETTERS =========================

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
	 * Establish a connection to the DTS. Returns our API's server connection around an
	 * Apelon connection instance, which corresponds to the type specified in the
	 * configuration property <code>connectionType</code>. This property must be
	 * convertible to the enum type {@link ConnectionType}.
	 *
	 * @param config
	 *            configuration properties
	 * @return a ready DTS connection
	 */
	private static DtsServerConnection getConnection(final Properties config)
	{
		// Read properties
		final ServerConnection apelonConnection = new ApelonConnectionBuilder(

		// Required properties for all connection types
				config.getProperty(CONNECTION_TYPE), config.getProperty(HOST),
				config.getProperty(PORT))

		// Optional properties required only by some connection types
				.setUser(config.getProperty(USER))
				.setPassword(config.getProperty(PASSWORD))
				.setInstance(config.getProperty(INSTANCE))

				// Build an Apelon connection object
				.build();

		// Wrap it with our object
		return new DtsServerConnectionImpl(apelonConnection);
	}

	/**
	 * An Apelon connection builder. Instantiates the Apelon implementation corresponding
	 * to the configuration properties.
	 */
	private static class ApelonConnectionBuilder implements Builder<ServerConnection>
	{
		private final ConnectionType connectionType;
		private final String host;
		private final int port;
		private String user;
		private String password;
		private String instance;

		/**
		 * @param connectionTypeStr
		 * @param hostStr
		 * @param portStr
		 */
		public ApelonConnectionBuilder(final String connectionTypeStr,
				final String hostStr, final String portStr)
		{
			super();
			this.connectionType = setConnectionType(connectionTypeStr);
			this.host = hostStr;
			this.port = setPort(portStr);
		}

		/**
		 * Set a new value for the connectionType property.
		 *
		 * @param connectionTypeStr
		 * @return
		 */
		private ConnectionType setConnectionType(final String connectionTypeStr)
		{
			final ConnectionType configConnectionType = valueOfNullSafe(
					ConnectionType.class, connectionTypeStr);
			if (configConnectionType == null)
			{
				throw new UnsupportedOperationException("Unrecognized connection type "
						+ connectionTypeStr);
			}
			return configConnectionType;
		}

		/**
		 * Set a new value for the port property.
		 *
		 * @param portStr
		 *            the port to set
		 * @return
		 */
		private int setPort(final String portStr)
		{
			final int configPort = getAsInteger(portStr);
			if (configPort == INVALID_VALUE_INTEGER)
			{
				throw new UnsupportedOperationException("Invalid port " + portStr);
			}
			return configPort;
		}

		/**
		 * Set a new value for the user property.
		 *
		 * @param user
		 *            the user to set
		 * @return
		 */
		public ApelonConnectionBuilder setUser(final String user)
		{
			this.user = user;
			return this;
		}

		/**
		 * Set a new value for the password property.
		 *
		 * @param password
		 *            the password to set
		 */
		public ApelonConnectionBuilder setPassword(final String password)
		{
			this.password = password;
			return this;
		}

		/**
		 * Set a new value for the instance property.
		 *
		 * @param instance
		 *            the instance to set
		 */
		public ApelonConnectionBuilder setInstance(final String instance)
		{
			this.instance = instance;
			return this;
		}

		/**
		 * @return
		 * @see edu.utah.further.core.api.lang.Builder#build()
		 */
		@Override
		public ServerConnection build()
		{
			try
			{
				return getConnection();
			}
			catch (final ApelonException e)
			{
				log.error("Could not establish a DTS connection: " + e.getMessage());
				// e.printStackTrace();
				return null;
			}
		}

		private ServerConnection getConnection() throws ApelonException
		{
			switch (connectionType)
			{
				// To open an insecure socket connection...
				case SOCKET:
				{
					return new ServerConnectionSocket(host, port);
				}

					// To open a secure socket connection...
				case SECURE_SOCKET:
				{
					return new ServerConnectionSecureSocket(host, port, user, password);
				}

					// To open a transient socket connection ...
				case TRANSIENT_SOCKET:
				{
					return new TransientSCSocket(host, port);
				}

					// To open an 'in-process' connection via JDBC ...
				case JDBC:
				{
					return new ServerConnectionJDBC(user, password, host, port, instance);
				}

				default:
				{
					throw new UnsupportedOperationException(
							"Unrecognized connection type " + connectionType);
				}
			}
		}
	}
}
