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
package edu.utah.further.core.xml.xquery.basex;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.Validate;
import org.basex.BaseXServer;
import org.basex.core.Context;
import org.basex.core.MainProp;
import org.springframework.context.ApplicationContext;
import org.springframework.util.DigestUtils;

/**
 * Managed basex server with options for setting up a username/password on startup and
 * 'disabling' the admin password by setting it to a random String.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jun 3, 2013
 */
public class BaseXServerBean
{
	/**
	 * The host which the server listens on
	 */
	private String host;

	/**
	 * The port the server listens on
	 */
	private Integer serverPort;

	/**
	 * The username that will be setup when the server starts.
	 */
	private String defaultUsername;

	/**
	 * The password that will be set for the defaultUsername when the server starts.
	 */
	private String defaultPassword;

	/**
	 * Whether or not to suppress BaseX's logging; default suppressed.
	 */
	private boolean suppressLogging = true;

	/**
	 * BaseX server context to set server options
	 */
	private final Context context = new Context();

	/**
	 * The basex server
	 */
	private BaseXServer server;

	// ========================= PRIVATE =================================

	/**
	 * Starts the server; called after all properties have been set
	 * 
	 * @throws IOException
	 */
	@PostConstruct
	private void start() throws IOException
	{
		Validate.notNull(host);
		Validate.notNull(serverPort);

		context.mprop.set(MainProp.SERVERPORT, serverPort.intValue());
		context.mprop.set(MainProp.SERVERHOST, host);

		Validate.notNull(defaultUsername);
		Validate.notNull(defaultPassword);

		sanitize();

		// Commands to run on startup
		final StringBuilder sbCmd = new StringBuilder();

		sbCmd.append("-c");
		sbCmd.append(dropUser());
		sbCmd.append(createUser());
		sbCmd.append(changeAdminPassword());

		// Watch for trailing semicolons in the command list. It's not a command
		// terminator, it's a command separator.

		final List<String> args = new ArrayList<>();
		args.add(sbCmd.toString());

		if (suppressLogging)
		{
			final StringBuilder sbLog = new StringBuilder();
			sbLog.append("-z");
			args.add(sbLog.toString());
		}

		// Server starts by default
		server = new BaseXServer(context, args.toArray(new String[args.size()]));
	}

	/**
	 * Set the default admin/admin password to a random 130 bit String
	 * 
	 * @return
	 */
	private String changeAdminPassword()
	{
		final StringBuilder sb = new StringBuilder();
		// Set the admin password to a random 130 bit String
		sb.append("ALTER USER admin "
				+ DigestUtils.md5DigestAsHex((new BigInteger(130, new SecureRandom())
						.toString(32)).getBytes()));
		return sb.toString();
	}

	/**
	 * Drops a user - used to ensure the user doesn't exist before creating them.
	 * 
	 * @return
	 */
	private String dropUser()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("DROP USER ");
		sb.append(defaultUsername);
		sb.append(";");
		return sb.toString();
	}

	/**
	 * Create a new user to login with based off of {@link #defaultUsername} and
	 * {@link #defaultPassword}
	 * 
	 * @return
	 */
	private String createUser()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("CREATE USER ");
		sb.append(defaultUsername);
		sb.append(" ");
		sb.append(DigestUtils.md5DigestAsHex(defaultPassword.getBytes()));
		sb.append(";");
		final String output = sb.toString();
		return output;
	}

	/**
	 * Sanitize input just in case
	 */
	private void sanitize()
	{
		final char blank = '\0';

		defaultUsername.replace(';', blank);
		defaultPassword.replace(';', blank);
	}

	/**
	 * Stops the server; called when the {@link ApplicationContext} is being destroyed
	 * 
	 * @throws IOException
	 */
	@PreDestroy
	private void stop() throws IOException
	{
		if (server != null)
		{
			server.stop();
		}
	}

	// ========================= GET/SET =================================

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
	 * Return the serverPort property.
	 * 
	 * @return the serverPort
	 */
	public Integer getServerPort()
	{
		return serverPort;
	}

	/**
	 * Set a new value for the serverPort property.
	 * 
	 * @param serverPort
	 *            the serverPort to set
	 */
	public void setServerPort(final Integer serverPort)
	{
		this.serverPort = serverPort;
	}

	/**
	 * Return the defaultUsername property.
	 * 
	 * @return the defaultUsername
	 */
	public String getDefaultUsername()
	{
		return defaultUsername;
	}

	/**
	 * Set a new value for the defaultUsername property.
	 * 
	 * @param defaultUsername
	 *            the defaultUsername to set
	 */
	public void setDefaultUsername(final String defaultUsername)
	{
		this.defaultUsername = defaultUsername;
	}

	/**
	 * Return the defaultPassword property.
	 * 
	 * @return the defaultPassword
	 */
	public String getDefaultPassword()
	{
		return defaultPassword;
	}

	/**
	 * Set a new value for the defaultPassword property.
	 * 
	 * @param defaultPassword
	 *            the defaultPassword to set
	 */
	public void setDefaultPassword(final String defaultPassword)
	{
		this.defaultPassword = defaultPassword;
	}

	/**
	 * Return the server property.
	 * 
	 * @return the server
	 */
	public BaseXServer getServer()
	{
		return server;
	}

	/**
	 * Set a new value for the server property.
	 * 
	 * @param server
	 *            the server to set
	 */
	public void setServer(final BaseXServer server)
	{
		this.server = server;
	}

	/**
	 * Return the suppressLogging property.
	 * 
	 * @return the suppressLogging
	 */
	public boolean isSuppressLogging()
	{
		return suppressLogging;
	}

	/**
	 * Set a new value for the suppressLogging property.
	 * 
	 * @param suppressLogging
	 *            the suppressLogging to set
	 */
	public void setSuppressLogging(final boolean suppressLogging)
	{
		this.suppressLogging = suppressLogging;
	}

}
