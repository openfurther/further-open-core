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
package edu.utah.further.core.query.domain;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * A manually-written JAXB Object factory that allows making the implementation classes
 * package-private.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jul 21, 2010
 */
@XmlRegistry
public final class ObjectFactory
{
	// ========================= CONSTANTS =================================

	// private static final QName _Price_QNAME = new QName("", "Price");
	// private static final QName _Name_QNAME = new QName("", "Name");

	// ========================= METHODS ===================================

	/**
	 * @return
	 */
	public SearchQueryTo createSearchQueryTo()
	{
		return SearchQueryTo.newInstance();
	}

	/**
	 * @return
	 */
	public SearchCriterionTo createSearchCriterionTo()
	{
		return SearchCriterionTo.newInstance();
	}

	/**
	 * @return
	 */
	public SortCriterionTo createSortCriterionTo()
	{
		return SortCriterionTo.newInstance();
	}

	/**
	 * @return
	 */
	public SearchOptionsTo createSearchOptionsTo()
	{
		return SearchOptionsTo.newInstance();
	}

	// @XmlElementDecl(namespace = "", name = "Price")
	// public JAXBElement<String> createSortCriterion(final String value)
	// {
	// return new JAXBElement<String>(_Price_QNAME, String.class, null, value);
	// }
	//
	// @XmlElementDecl(namespace = "", name = "Name")
	// public JAXBElement<String> createName(final String value)
	// {
	// return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
	// }

}
