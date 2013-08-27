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
package edu.utah.further.core.ws.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import edu.utah.further.core.ws.WsUtil;

/**
 * Test web service utilities.
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
 * @version Nov 6, 2008
 */
public final class UTestWsUtil
{
	// ========================= CONSTANTS =================================

	private static class MyServiceSoap
	{
	}

	private static class MyServiceRest
	{
	}

	// ========================= METHODS ===================================

	/**
	 * Test SOAP web service class path conventions.
	 */
	@Test
	public void webServiceSoapPath()
	{
		assertThat(WsUtil.getWebServiceClassPath(MyServiceSoap.class), is("my"));
	}

	/**
	 * Test REST web service class path conventions.
	 */
	@Test
	public void webServiceRestPath()
	{
		assertThat(WsUtil.getWebServiceClassPath(MyServiceRest.class), is("my"));
	}

	// ========================= PRIVATE METHODS ===========================
}
