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
package edu.utah.further.dts.ws.mock;

import java.util.Properties;

import edu.utah.further.core.api.context.Mock;
import edu.utah.further.dts.api.service.ConnectionFactory;
import edu.utah.further.dts.api.service.DtsServerConnection;

/**
 * A DTS connection factory mock implementation.
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
@Mock
public final class ConnectionFactoryMockImpl implements ConnectionFactory
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

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
		// Method stub
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConnectionFactory#getConfig()
	 */
	@Override
	public Properties getConfig()
	{
		return new Properties();
	}

	/**
	 * Always returns <code>true</code>. Note: is incompatible with the result of
	 * {@link #getCurrentConnection()} with respect to the general
	 * {@link ConnectionFactory} API expectations.
	 * 
	 * @return <code>true</code>
	 * @see edu.utah.further.dts.api.service.ConnectionFactory#isSessionOpen()
	 */
	@Override
	public boolean isSessionOpen()
	{
		return false;
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
		return null;
	}

	/**
	 * Close the current connection.
	 */
	@Override
	public void closeSession()
	{
		// Method stub
	}
}
