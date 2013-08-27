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
package edu.utah.further.fqe.ds.api.to;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;

/**
 * A wrapper for a list of {@link StatusMetaDataToImpl}s.
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
 * @version Mar 19, 2010
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = XmlNamespace.FQE, name = StatusesMetaDataToImpl.ENTITY_NAME)
public final class StatusesMetaDataToImpl
{
	// ========================= CONSTANTS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(StatusesMetaDataToImpl.class);

	/**
	 * Entity name
	 */
	public static final String ENTITY_NAME = "statuses";

	// ========================= FIELDS =======================================

	/**
	 * List of status messages. Unfortunately, FUR-948 introduces too much complication,
	 * so the use a TO implementation type array stands better.
	 */
	@XmlElement(name = "statusMetaData", namespace = XmlNamespace.FQE)
	private StatusMetaDataToImpl[] statuses;

	// ========================= CONSTRUCTORS ====================================

	/**
	 * Default constructor
	 */
	public StatusesMetaDataToImpl()
	{
		super();
	}

	/**
	 * @param statuses
	 */
	public StatusesMetaDataToImpl(final List<? extends StatusMetaData> statuses)
	{
		super();
		setStatuses(statuses);
		if (log.isTraceEnabled())
		{
			log.trace(this.toString());
		}
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("statuses",
				statuses).toString();
	}

	// ========================= GET/SET ======================================

	/**
	 * @return the statuses
	 */
	public List<StatusMetaData> getStatuses()
	{
		return CollectionUtil.<StatusMetaData> newList(statuses);
	}

	/**
	 * @param statuses
	 *            the statuses to set
	 */
	public void setStatuses(final List<? extends StatusMetaData> statuses)
	{
		this.statuses = statuses.toArray(new StatusMetaDataToImpl[statuses.size()]);
	}

}
