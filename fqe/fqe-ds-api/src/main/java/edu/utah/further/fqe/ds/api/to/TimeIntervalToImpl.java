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
import static edu.utah.further.core.api.time.TimeUtil.getTimestampNullSafe;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.time.TimeUtil;
import edu.utah.further.core.api.xml.TimestampAdapter;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.ds.api.domain.ImmutableTimeInterval;

/**
 * A transfer object for a time interval. Unclear at this point whether this can be reused
 * by different JAXB entities with field name overrides.
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
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "start", "end" })
@XmlRootElement(namespace = XmlNamespace.FQE, name = TimeIntervalToImpl.ENTITY_NAME)
public class TimeIntervalToImpl implements TimeIntervalTo
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
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	@XmlElement(name = "start_date", required = false)
	private Timestamp start;

	/**
	 * Ending point of this time interval.
	 */
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	@XmlElement(name = "end_date", required = false)
	private Timestamp end;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public TimeIntervalToImpl()
	{
		super();
	}

	/**
	 * @param start
	 * @param end
	 */
	public TimeIntervalToImpl(final Timestamp start, final Timestamp end)
	{
		this();
		this.start = start;
		this.end = end;
	}
	
	/**
	 * @param start
	 * @param end
	 */
	public TimeIntervalToImpl(final Date start, final Date end)
	{
		this();
		this.start = getTimestampNullSafe(start);
		this.end = getTimestampNullSafe(end);
	}

	/**
	 * A copy-constructor.
	 * 
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public TimeIntervalToImpl(final ImmutableTimeInterval other)
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
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("start", start)
				.append("end", end);
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	public TimeIntervalToImpl copyFrom(final ImmutableTimeInterval other)
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
	 * Return the start property.
	 * 
	 * @return the start
	 */
	@Override
	public Timestamp getStart()
	{
		return start;
	}

	/**
	 * Set a new value for the start property.
	 * 
	 * @param start
	 *            the start to set
	 */
	@Override
	public void setStart(final Timestamp start)
	{
		this.start = getTimestampNullSafe(start);
	}

	/**
	 * Return the end property.
	 * 
	 * @return the end
	 */
	@Override
	public Timestamp getEnd()
	{
		return end;
	}

	/**
	 * Set a new value for the end property.
	 * 
	 * @param end
	 *            the end to set
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
