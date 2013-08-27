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
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.chain.ChainRequestImpl;
import edu.utah.further.core.chain.MarshallRequestProcessor;
import edu.utah.further.core.chain.RequestHandlerBuilder;
import edu.utah.further.core.xml.fixture.CoreXmlFixture;

/**
 * Simple unit test for {@link MarshallRequestProcessorImpl}
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
public final class UTestMarshallRequestProcessor extends CoreXmlFixture
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The {@link MarshallRequestProcessorImpl} to test
	 */
	private final MarshallRequestProcessor marshallRp = new MarshallRequestProcessorImpl();

	/**
	 * Books to marshal
	 */
	private Collection books;

	// ========================= METHODS ===================================

	/**
	 * Set up object graph to marshal.
	 *
	 * @throws DatatypeConfigurationException
	 */
	@Before
	public void setup() throws DatatypeConfigurationException
	{
		marshallRp.setSourceAttr(RequestAttributes.OBJECT_ATTR);
		marshallRp.setResultAttr(RequestAttributes.RESULT_ATTR);
		// Manual DI
		((MarshallRequestProcessorImpl) marshallRp)
				.setSchemaAttr(RequestAttributes.SCHEMA_ATTR);

		final ObjectFactory objFactory = new ObjectFactory();
		books = objFactory.createCollection();

		final Collection.Books booksType = objFactory.createCollectionBooks();
		final List<BookType> bookList = booksType.getBook();

		final BookType book = objFactory.createBookType();
		book.setItemId("307");
		book.setName("JAXB today and beyond");
		book.setDescription("This is an intermediate book on JAXB");
		book.setISBN(987665L);
		book.setPrice("45$");
		book.setPublicationDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(
				new GregorianCalendar(2000, 2, 2)));
		book.setBookCategory("other");

		final BookType.Promotion promotionType = objFactory.createBookTypePromotion();
		promotionType.setDiscount("5% off regular price");
		book.setPromotion(promotionType);

		final BookType.Authors authorsType = objFactory.createBookTypeAuthors();
		final List<String> authorList = authorsType.getAuthorName();
		authorList.add("Richard K");
		book.setAuthors(authorsType);

		bookList.add(book);
		books.setBooks(booksType);
	}

	/**
	 * Test the marshalling request processor.
	 *
	 * @throws IOException
	 */
	@Test
	public void marshallRp() throws IOException
	{
		final RequestHandlerBuilder chainBuilder = chainBuilder();
		chainBuilder.addProcessor(marshallRp);
		final ChainRequest chainRequest = new ChainRequestImpl();

		final Resource schema = new ClassPathResource(BOOKS_XSD);

		// Set the object and schema
		chainRequest.setAttribute(RequestAttributes.OBJECT_ATTR, books);
		chainRequest.setAttribute(RequestAttributes.SCHEMA_ATTR,
				new StreamSource(schema.getInputStream()));

		final RequestHandler chain = chainBuilder.build();
		chain.handle(chainRequest);
		assertThat(chainRequest.getAttribute(RequestAttributes.RESULT_ATTR),
				notNullValue());
	}
}
