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
package edu.utah.further.core.api.exception;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.CoreApiFixture;
import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.lang.CoreUtil;

/**
 * Tests marshalling {@link WsException}
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
 * @version Jul 19, 2013
 */
public class UTestJaxbWsException extends CoreApiFixture
{

	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestJaxbWsException.class);

	/**
	 * JAXB-annotated class package.
	 */
	private static final String PACKAGE = WsException.class.getPackage().getName();

	// ========================= TESTING METHODS ===========================

	/**
	 * Marshal a WsException
	 */
	@Test
	public void marshalWsException() throws Exception
	{
		final WsException entity = newWsException();
		final String s = testMarshalling(entity, PACKAGE);
		assertThat(s, containsString(":wsException"));
		assertThat(s, containsString(":wsException>"));
		assertThat(s, containsString("INTERNAL_ERROR"));
	}

	/**
	 * Unmarshal a WsException set.
	 */
	@Test
	public void unmarshalWsException() throws Exception
	{
		final JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE);
		final Unmarshaller u = jaxbContext.createUnmarshaller();
		WsException entity;
		try (final InputStream is = CoreUtil.getResourceAsStream("ws-exception.xml"))
		{
			entity = (WsException) u.unmarshal(is);
		}

		assertTrue(ErrorCode.INTERNAL_ERROR.equals(entity.getCode()));
		assertThat(entity.getMessage(), containsString("myValue"));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private static WsException newWsException()
	{
		return new WsException("myValue");
	}
}
