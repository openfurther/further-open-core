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
package edu.utah.further.core.util.composite;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * A named grid node implementation.
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
 * @version Nov 8, 2008
 */
public class CompositeSource extends AbstractSource
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(CompositeSource.class);

	// ========================= FIELDS ====================================

	/**
	 * List of sources of this composite source.
	 */
	private final List<Source> sources;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param name
	 * @param description
	 * @param url
	 * @param admin
	 * @param sources
	 */
	public CompositeSource(final String name, final String description, final String url,
			final InternetAddress admin)
	{
		this(name, description, url, admin, CollectionUtil.<Source> newList());
	}

	/**
	 * @param name
	 * @param description
	 * @param url
	 * @param admin
	 * @param sources
	 */
	public CompositeSource(final String name, final String description, final String url,
			final InternetAddress admin, final List<Source> sources)
	{
		super(name, description, url, admin);
		this.sources = sources;
	}

	// ========================= IMPLEMENTATION: Object ====================

	// TODO: implement final equals() and final hashCode() by name or ID.

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("sources",
				sources).toString();
	}

	// ========================= IMPLEMENTATION: Source ====================

	/**
	 * @return
	 * @see edu.utah.further.core.util.composite.Source#executeQuery()
	 */
	@Override
	public Result executeQuery()
	{
		final List<String> allData = newList();
		for (final Source source : sources)
		{
			final Result result = source.executeQuery();
			allData.addAll(result.getData());
		}
		return new ResultImpl(allData);
	}

	// ========================= METHODS ===================================

	/**
	 * @param arg0
	 * @return
	 */
	public void add(final Source arg0)
	{
		sources.add(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 */
	public void addAll(final Collection<? extends Source> arg0)
	{
		sources.addAll(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 */
	public void remove(final Source arg0)
	{
		sources.remove(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 */
	public void removeAll(final Collection<? extends Source> arg0)
	{
		sources.removeAll(arg0);
	}

	// ========================= PRIVATE METHODS ===========================

}
