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
package edu.utah.further.core.math.schedule;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.core.api.observer.DataMessage;

/**
 * An observer pattern message that signals a change in a task's state.
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
 * @version May 29, 2009
 */
public final class JobEvent<V> extends DataMessage<JobRunnerNotifier<V>, V>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// ========================= NESTED TYPES ==============================

	/**
	 * Runner event type.
	 */
	public enum Type
	{
		/**
		 * Triggered right before a job is started.
		 */
		BEFORE_STARTED,

		/**
		 * Triggered right after a job is started.
		 */
		AFTER_STARTED,

		/**
		 * Triggered right before a job completed (or received a stop command).
		 */
		BEFORE_COMPLETED,

		/**
		 * Triggered right after a job completed (or received a stop command).
		 */
		AFTER_COMPLETED,

		/**
		 * An entire scheduled job graph is completed (or received a stop command).
		 */
		SCHEDULER_COMPLETED;
	}

	// ========================= FIELDS ====================================

	/**
	 * The type of this message
	 */
	private final Type type;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param subject
	 * @param type
	 */
	public static <V> JobEvent<V> newEvent(final JobRunnerNotifier<V> subject,
			final Type type, final V entity)
	{
		return new JobEvent<>(subject, type, entity);
	}

	/**
	 * @param subject
	 * @param type
	 */
	private JobEvent(final JobRunnerNotifier<V> subject, final Type type, final V entity)
	{
		super(subject);
		this.type = type;
		this.entity = entity;
	}

	// ========================= IMPL: Object ==============================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		@SuppressWarnings("unchecked")
		final JobEvent<V> that = (JobEvent<V>) obj;
		return new EqualsBuilder()
				.append(this.type, that.type)
				.append(this.getEntity(), that.getEntity())
				.isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(type).append(getEntity()).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Job " + getEntity() + " " + type.name().toLowerCase();
	}

	// ========================= METHODS ===================================

	/**
	 * @return
	 * @see edu.utah.further.core.util.registry.SimpleDataMessage#getType()
	 */
	public Type getType()
	{
		return type;
	}

	// ========================= IMPL: DataMessage =========================

	/**
	 * @param entity
	 */
	@Override
	public void setEntity(final V entity)
	{
		throw new UnsupportedOperationException("This data message is immutable");
	}
}
