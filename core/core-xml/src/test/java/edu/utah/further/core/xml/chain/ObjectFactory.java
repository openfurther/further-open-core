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
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.20 at 10:19:07 AM MDT 
//

package edu.utah.further.core.xml.chain;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element
 * interface generated in the edu.utah.further.examples.jaxb package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java
 * representation for XML content. The Java representation of XML content can consist of
 * schema derived interfaces and classes representing the binding of schema type
 * definitions, element declarations and model groups. Factory methods for each of these
 * are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory
{

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema
	 * derived classes for package: edu.utah.further.examples.jaxb
	 * 
	 */
	public ObjectFactory()
	{
	}

	/**
	 * Create an instance of {@link Collection }
	 * 
	 */
	public Collection createCollection()
	{
		return new Collection();
	}

	/**
	 * Create an instance of {@link BookType.Authors }
	 * 
	 */
	public BookType.Authors createBookTypeAuthors()
	{
		return new BookType.Authors();
	}

	/**
	 * Create an instance of {@link BookType }
	 * 
	 */
	public BookType createBookType()
	{
		return new BookType();
	}

	/**
	 * Create an instance of {@link BookType.Promotion }
	 * 
	 */
	public BookType.Promotion createBookTypePromotion()
	{
		return new BookType.Promotion();
	}

	/**
	 * Create an instance of {@link Collection.Books }
	 * 
	 */
	public Collection.Books createCollectionBooks()
	{
		return new Collection.Books();
	}

}
