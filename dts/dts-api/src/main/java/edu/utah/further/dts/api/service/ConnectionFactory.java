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
package edu.utah.further.dts.api.service;

import java.util.Properties;

/**
 * A DTS connection factory. Note: a query server is NOT set on the connection and is the
 * responsibility of client classes of the produced connection object.
 * <p>
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
public interface ConnectionFactory
{
	// ========================= METHODS ===================================

	/**
	 * Initialize a DTS connection session. Sessions may be (properly only, of course)
	 * nested; only one connection is started and stopped for the most outer session. This
	 * corresponds to Spring's transaction <code>Propagation.SUPPORTS</code>.
	 */
	void startSession();

	/**
	 * Checks if there is currently an open DTS session.
	 *
	 * @return <code>true</code> if and only if the DTS connection bound to the current
	 *         context (=thread) is non-<code>null</code>
	 * @see edu.utah.further.dts.api.service.ConnectionFactory#getCurrentConnection()
	 */
	boolean isSessionOpen();

	/**
	 * Return the DTS connection bound to the current context (= thread). A DTS session
	 * must be started before this method is called.
	 *
	 * @return the DTS connection in the current context
	 * @throws IllegalStateException
	 *             if this method is called outside a DTS session
	 */
	DtsServerConnection getCurrentConnection();

	/**
	 * Close the current connection.
	 */
	void closeSession();

	/**
	 * Return the connection configuration properties. This is a defensive copy of the
	 * internal {@link Properties} object held by this service, with the connection
	 * password starred out to prevent a security breach when using this method to throw
	 * an exception and propagate it to a web service response.
	 *
	 * @return the connection configuration {@link Properties} object
	 */
	Properties getConfig();
}
