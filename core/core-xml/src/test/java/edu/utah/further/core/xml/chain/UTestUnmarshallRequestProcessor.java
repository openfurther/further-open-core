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
package edu.utah.further.core.xml.chain;

import static edu.utah.further.core.chain.RequestHandlerBuilder.chainBuilder;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.chain.ChainRequestImpl;
import edu.utah.further.core.chain.RequestHandlerBuilder;
import edu.utah.further.core.xml.fixture.CoreXmlFixture;

/**
 * Unit test for {@link UnmarshallRequestProcessorImpl}
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version May 20, 2010
 */
public final class UTestUnmarshallRequestProcessor extends CoreXmlFixture
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The {@link UnmarshallRequestProcessorImpl}
	 */
	private final UnmarshallRequestProcessorImpl unmarshallRp = new UnmarshallRequestProcessorImpl();

	// ========================= METHODS ===================================

	/**
	 * Set up unmarshalling services.
	 */
	@Before
	public void setUp()
	{
		unmarshallRp.setSourceAttr(RequestAttributes.SOURCE_ATTR);
		unmarshallRp.setResultAttr(RequestAttributes.RESULT_ATTR);
		unmarshallRp.setJaxbPackageAttr(RequestAttributes.JAXB_PKG_ATTR);
	}

	/**
	 * Tests unmarshalling RP
	 * 
	 * @throws IOException
	 */
	@Test
	public void unmarshallRp() throws IOException
	{
		final RequestHandlerBuilder chainBuilder = chainBuilder();
		chainBuilder.addProcessor(unmarshallRp);
		final ChainRequest chainRequest = new ChainRequestImpl();

		final Resource books = new ClassPathResource(BOOKS_XML);

		chainRequest.setAttribute(RequestAttributes.SOURCE_ATTR, books.getInputStream());
		chainRequest.setAttribute(RequestAttributes.JAXB_PKG_ATTR,
				UnmarshallRequestProcessorImpl.class.getPackage().getName());
		final RequestHandler chain = chainBuilder.build();
		chain.handle(chainRequest);
		assertThat(chainRequest.getAttribute(RequestAttributes.RESULT_ATTR),
				notNullValue());
	}
}
