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
package edu.utah.further.ds.api.domain;

import java.util.Date;

import org.w3c.dom.Document;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * a FURTHeR logial query.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Mar 24, 2010
 */
public interface LogicalQuery extends PersistentEntity<Long>
{
	// ========================= CONSTANTS =================================

	/**
	 * The name of the named query that builds translates the logical query
	 */
	String BUILD_QUERY = "term.trans.buildQuery";

	// ========================= METHODS ===================================

	/**
	 * @return the queryXml
	 */
	Document getQueryXml();

	/**
	 * @param queryXml
	 *            the queryXml to set
	 */
	void setQueryXml(Document queryXml);

	/**
	 * @return the queryName
	 */
	String getQueryName();

	/**
	 * @param queryName
	 *            the queryName to set
	 */
	void setQueryName(String queryName);

	/**
	 * @return the createDate
	 */
	Date getCreateDate();

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	void setCreateDate(Date createDate);

	/**
	 * @return the createUserId
	 */
	String getCreateUserId();

	/**
	 * @param createUserId
	 *            the createUserId to set
	 */
	void setCreateUserId(String createUserId);
}