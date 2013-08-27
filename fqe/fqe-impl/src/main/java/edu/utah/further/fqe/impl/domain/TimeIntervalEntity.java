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
package edu.utah.further.fqe.impl.domain;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.core.api.time.TimeUtil.getTimestampNullSafe;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.lang.CopyableFrom;
import edu.utah.further.core.api.time.TimeUtil;
import edu.utah.further.fqe.ds.api.domain.ImmutableTimeInterval;
import edu.utah.further.fqe.ds.api.domain.TimeInterval;

/**
 * A persistent entity for a time interval. Unclear at this point whether this can be
 * reused by different JAXB entities with field name overrides.
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
 * @resource Mar 19, 2009
 */
@Embeddable
public class TimeIntervalEntity implements TimeInterval,
		CopyableFrom<ImmutableTimeInterval, TimeIntervalEntity>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "timeInterval";

	// ========================= FIELDS ====================================

	/**
	 * Starting point of this time interval.
	 */
	@Column(name = "start_date", nullable = true)
	private Timestamp start;

	/**
	 * Ending point of this time interval.
	 */
	@Column(name = "end_date", nullable = true)
	private Timestamp end;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public TimeIntervalEntity()
	{
		super();
	}

	/**
	 * A copy-constructor.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public TimeIntervalEntity(final ImmutableTimeInterval other)
	{
		this();
		copyFrom(other);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("start", start)
				.append("end", end)
				.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public TimeIntervalEntity copyFrom(final ImmutableTimeInterval other)
	{
		if (other == null)
		{
			return this;
		}

		// Identifier is not copied

		// Deep-copy fields
		setStart(other.getStart());
		setEnd(other.getEnd());

		// Deep-copy collection references but soft-copy their elements

		return this;
	}

	// ========================= IMPLEMENTATION: TimeInterval ==============

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.ImmutableTimeInterval#getStart()
	 */
	@Override
	public Timestamp getStart()
	{
		return start;
	}

	/**
	 * @param start
	 * @see edu.utah.further.fqe.ds.api.domain.ImmutableTimeInterval#setStart(java.sql.Timestamp)
	 */
	@Override
	public void setStart(final Timestamp start)
	{
		this.start = getTimestampNullSafe(start);
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.ImmutableTimeInterval#getEnd()
	 */
	@Override
	public Timestamp getEnd()
	{
		return end;
	}

	/**
	 * @param end
	 * @see edu.utah.further.fqe.ds.api.domain.ImmutableTimeInterval#setEnd(java.sql.Timestamp)
	 */
	@Override
	public void setEnd(final Timestamp end)
	{
		this.end = getTimestampNullSafe(end);
	}

	/**
	 * Return the duration of the interval (end-start) in milliseconds. If start or end
	 * are <code>null</code>, returns <code>null</code>.
	 *
	 * @return interval duration in milliseconds
	 */
	@Override
	public Long getDuration()
	{
		return TimeUtil.getDuration(getStart(), getEnd());
	}
}
