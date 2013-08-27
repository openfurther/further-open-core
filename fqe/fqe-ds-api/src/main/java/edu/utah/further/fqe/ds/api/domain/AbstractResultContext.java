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
package edu.utah.further.fqe.ds.api.domain;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.DefaultImplementation;

/**
 * A base class of {@link ResultContext} implementations.
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
 * @version Jun 4, 2010
 */
@DefaultImplementation(ResultContext.class)
@XmlTransient
public abstract class AbstractResultContext implements ResultContext
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * A convenient direct reference to this query's result set. Must be manually set on
	 * this object.
	 */
	@XmlTransient
	private Object result;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.ResultContext#toString()
	 */
	@Override
	public final String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("id", getId())
				.append("rootEntityClass", getRootEntityClass())
				.append("transferObjectClass", getTransferObjectClass())
				.append("numRecords", getNumRecords())
				.toString();
	}

	// ========================= IMPLEMENTATION: ResultContext =============

	/**
	 * Return the raw query result type.
	 *
	 * @return the result
	 */
	@Override
	public Object getResult()
	{
		return result;
	}

	/**
	 * Set a new value for the entityList property.
	 *
	 * @param entityList
	 *            the entityList to set
	 */
	@Override
	public void setResult(final Object result)
	{
		this.result = result;
	}
}
