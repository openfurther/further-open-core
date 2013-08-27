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

import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.fqe.ds.api.domain.AbstractStatusMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;

/**
 * A persistent implementation of a query status message.
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
 * @version Mar 17, 2010
 */
@Entity
@Table(name = "status_meta_data")
public class StatusMetaDataEntity extends AbstractStatusMetaData
{
	// ========================= CONSTANTS =================================

	/**
	 * Dummy serial UID.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * This entity's unique identifier.
	 */
	@Id
	@GeneratedValue
	@Column
	private Long id;

	/**
	 * The unique identifier of the data source that generated this status message.
	 */
	@Column(name = "datasourceid")
	@Final
	private String dataSourceId;

	/**
	 * Status message.
	 */
	@Column
	private String status;

	/**
	 * Status date.
	 */
	@Column(name = "status_date")
	private Date statusDate;

	/**
	 * Optional duration of the process reporting this status, in milliseconds. Can be
	 * <code>0</code> if this status is not associated with a point in time rather than an
	 * interval.
	 */
	@Column(name = "duration")
	private long duration = 0l;

	/**
	 * A reference back to the owning query context entity.
	 */
	@ManyToOne
	@JoinColumn(name = "querycontext")
	private QueryContextEntity queryContext;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for Hibernate.
	 */
	protected StatusMetaDataEntity()
	{
		super();
	}

	/**
	 * Creates a blank persistent status entity.
	 *
	 * @return a new blank entity
	 */
	public static StatusMetaDataEntity newInstance()
	{
		return new StatusMetaDataEntity();
	}

	/**
	 * A copy-constructor factory method.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 * @return a deep copy of <code>other</code>
	 */
	public static StatusMetaDataEntity newCopy(final StatusMetaData other)
	{
		return new StatusMetaDataEntity().copyFrom(other);
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public StatusMetaDataEntity copyFrom(final StatusMetaData other)
	{
		if (other == null)
		{
			return this;
		}

		// Deep-copy primitive fields and IDs, but do not deep-copy entity references
		// Do not alter the ID of this entity if it's already set
		if (getId() == null)
		{
			this.id = other.getId();
		}
		setDataSourceId(other.getDataSourceId());
		setStatus(other.getStatus());
		setStatusDate(other.getStatusDate());
		setDuration(other.getDuration());

		if (instanceOf(other, StatusMetaDataEntity.class))
		{
			setQueryContext(((StatusMetaDataEntity) other).getQueryContext());
		}

		return this;
	}

	// ========================= IMPLEMENTATION: StatusMetaData ==============

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * Return the dataSourceId property.
	 *
	 * @return the dataSourceId
	 */
	@Override
	public String getDataSourceId()
	{
		return dataSourceId;
	}

	/**
	 * Set a new value for the dataSourceId property.
	 *
	 * @param dataSourceId
	 *            the dataSourceId to set
	 */
	@Override
	public void setDataSourceId(final String dataSourceId)
	{
		this.dataSourceId = dataSourceId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.fqe.ds.api.domain.StatusMetaData#getStatus()
	 */
	@Override
	public String getStatus()
	{
		return status;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.fqe.ds.api.domain.StatusMetaData#setStatus(java.lang.String)
	 */
	@Override
	public void setStatus(final String status)
	{
		this.status = status;
		if (this.statusDate == null)
		{
			this.statusDate = TimeService.getDate();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.fqe.ds.api.domain.StatusMetaData#getStatusTime()
	 */
	@Override
	public Date getStatusDate()
	{
		return statusDate;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.fqe.ds.api.domain.StatusMetaData#setStatusTime(java.util.Date)
	 */
	@Override
	public void setStatusDate(final Date statusDate)
	{
		this.statusDate = statusDate;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.fqe.ds.api.domain.StatusMetaData#getDuration()
	 */
	@Override
	public long getDuration()
	{
		return duration;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.fqe.ds.api.domain.StatusMetaData#setDuration(long)
	 */
	@Override
	public void setDuration(final long duration)
	{
		this.duration = duration;
	}

	/**
	 * @return the queryContext
	 */
	@Override
	public QueryContextEntity getQueryContext()
	{
		return queryContext;
	}

	/**
	 * @param queryContext
	 *            the queryContext to set. Must be of type {@link QueryContextEntity}!
	 */
	@Override
	public void setQueryContext(final QueryContext queryContext)
	{
		this.queryContext = (QueryContextEntity) queryContext;
	}
}
