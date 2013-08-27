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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.DefaultImplementation;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.time.TimeUtil;

/**
 * A convenient base class for {@link StatusMetaData} implementations.
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
 * @version Jun 9, 2010
 */
@DefaultImplementation(StatusMetaData.class)
@XmlTransient
public abstract class AbstractStatusMetaData implements StatusMetaData
{
	// ========================= CONSTANTS =================================

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Return the hash code of this object. includes only the creation date field because
	 * it is the only reliable unique identifier guaranteed to be non-<code>null</code>.
	 * 
	 * @return hash code of this object
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!ReflectionUtil.instanceOf(obj, AbstractStatusMetaData.class))
			return false;

		final AbstractStatusMetaData that = (AbstractStatusMetaData) obj;
		return new EqualsBuilder().append(getId(), that.getId()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("id", getId())
				.append("dataSourceId", getDataSourceId())
				.append("status", getStatus())
				.append("statusTime", getStatusTime())
				.append(PROPERTY_STATUS_DATE, getStatusDate())
				.toString();
	}

	// ========================= IMPL: StatusMetaData ======================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.StatusMetaData#getQueryContextId()
	 */
	@Override
	public Long getQueryContextId()
	{
		return StringUtil.getIdNullSafe(getQueryContext());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.StatusMetaData#getStatusTime()
	 */
	@Override
	public Long getStatusTime()
	{
		// This allows sub-classes to use any Date sub-class (e.g. Timestamp) and still
		// retain a uniform date interface as a Long. Also useful in equals(), hashCode().
		return TimeUtil.getDateAsTime(getStatusDate());
	}
}