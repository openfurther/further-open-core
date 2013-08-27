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
package edu.utah.further.core.util.registry;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static org.slf4j.LoggerFactory.getLogger;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

/**
 * A named grid node impoementation.
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
public abstract class NodeImpl implements Node
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(NodeImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * Name of this source.
	 */
	private final String name;

	/**
	 * Long description.
	 */
	private final String description;

	/**
	 * Source URL.
	 */
	private final String url;

	/**
	 * Admin's address.
	 */
	private final InternetAddress admin;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param name
	 * @param description
	 * @param url
	 * @param admin
	 */
	public NodeImpl(String name, String description, String url, InternetAddress admin)
	{
		super();
		this.name = name;
		this.description = description;
		this.url = url;
		this.admin = admin;
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
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("name", name).append(
				"description", description).append("url", url).append("admin", admin)
				.toString();
	}

	// ========================= IMPLEMENTATION: Named =====================

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.util.registry.Source#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.util.registry.Source#getMainContact()
	 */
	@Override
	public InternetAddress getMainContact()
	{
		return admin;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.util.registry.Source#getUrl()
	 */
	@Override
	public String getUrl()
	{
		return url;
	}

	// ========================= IMPLEMENTATION: Subject ===================

	// ========================= IMPLEMENTATION: Observer ==================

	// ========================= PRIVATE METHODS ===========================

}
