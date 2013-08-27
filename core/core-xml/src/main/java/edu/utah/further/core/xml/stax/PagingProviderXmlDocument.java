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
package edu.utah.further.core.xml.stax;

import javax.xml.stream.XMLStreamReader;

import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.page.IterableType;
import edu.utah.further.core.api.collections.page.Pager;
import edu.utah.further.core.api.collections.page.PagingProvider;
import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.context.Labeled;

/**
 * Contains pager factory methods for iterable XML objects.
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
 * @version Sep 21, 2010
 */
@Service("pagingProviderXmlDocument")
public final class PagingProviderXmlDocument implements PagingProvider<XMLStreamReader>
{
	// ========================= IMPL: PagingProvider ======================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.collections.page.PagingProvider#getIterableType()
	 */
	@Override
	public Labeled getIterableType()
	{
		return IterableType.XML_STREAM;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.collections.page.PagingProvider#getObjectType()
	 */
	@Override
	public Class<XMLStreamReader> getObjectType()
	{
		return XMLStreamReader.class;
	}

	/**
	 * @param list
	 * @param pagingStrategy
	 * @return
	 */
	@Override
	public Pager<?> newPager(final XMLStreamReader iterable,
			final PagingStrategy pagingStrategy)
	{
		return new XmlStreamPager(iterable, pagingStrategy);
	}

	// ========================= PRIVATE METHODS ===========================
}
