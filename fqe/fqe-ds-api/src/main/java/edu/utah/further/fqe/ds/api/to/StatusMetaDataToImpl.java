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

import static edu.utah.further.fqe.ds.api.domain.StatusMetaData.PROPERTY_STATUS_DATE;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.ds.api.domain.AbstractStatusMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;

/**
 * Transfer Object (XML Representation) implementation of {@link StatusMetaData}.
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
 * @version Jan 26, 2010
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = XmlNamespace.FQE, name = StatusMetaDataToImpl.ENTITY_NAME)
@XmlType(name = "", propOrder =
{ "id", "status", PROPERTY_STATUS_DATE, "duration", "dataSourceId", "queryContextId" })
public class StatusMetaDataToImpl extends AbstractStatusMetaData
{
	// ========================= CONSTANTS =================================

	/**
	 * Dummy serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(StatusMetaDataToImpl.class);

	/**
	 * The name of the element
	 */
	public static final String ENTITY_NAME = "statusMetaData";

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of the status message.
	 */
	@XmlElement(name = "id", required = false, namespace = XmlNamespace.FQE)
	private Long id;

	/**
	 * The unique identifier of the data source generating this status message.
	 */
	@XmlElement(name = "dataSourceId", required = false, namespace = XmlNamespace.FQE)
	@Final
	private String dataSourceId;

	/**
	 * A String representing the current status
	 */
	@XmlElement(name = "statusMsg", required = false, namespace = XmlNamespace.FQE)
	private String status;

	/**
	 * Date at which this status was generated. Defaults to when object was created.
	 */
	@XmlElement(name = PROPERTY_STATUS_DATE, required = false, namespace = XmlNamespace.FQE)
	private Date statusDate;

	/**
	 * Optional duration of the process reporting this status, in milliseconds. Can be
	 * <code>0</code> if this status is not associated with a point in time rather than an
	 * interval.
	 */
	@XmlElement(name = "duration", required = false, namespace = XmlNamespace.FQE)
	private long duration;

	// ========================= FIELDS - ASSOCIATIONS =====================

	/**
	 * A reference back to the owning query context entity. Indirectly marshaled via
	 * {@link #queryContextId}.
	 *
	 */
	@XmlTransient
	private QueryContextTo queryContext;

	/**
	 * Link to this status' owning {@link QueryContext}'s ID, if this is a child
	 * {@link QueryContext} produced by a data source. TOs do not support deep copy of an
	 * {@link QueryContext} entity because it is expensive (and in principle, the entity
	 * tree may have cycles).
	 */
	@XmlElement(name = "queryContextId", required = false, namespace = XmlNamespace.FQE)
	private Long queryContextId;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Generates a new blank {@link StatusMetaData} instance.
	 *
	 * @return a {@link StatusMetaData} without a message
	 */
	public static StatusMetaData newInstance()
	{
		return new StatusMetaDataToImpl();
	}

	/**
	 * Generates a new {@link StatusMetaData} message instance.
	 *
	 * @param dataSourceId
	 *            data source identifier
	 * @param message
	 *            the status message
	 * @return a {@link StatusMetaData} containing the message.
	 */
	public static StatusMetaData newInstance(final String dataSourceId,
			final String message)
	{
		final StatusMetaData statusMetaData = newInstance();
		statusMetaData.setDataSourceId(dataSourceId);
		statusMetaData.setStatus(message);
		return statusMetaData;
	}

	/**
	 * Generates a new {@link StatusMetaData} message instance with a duration.
	 *
	 * @param dataSourceId
	 *            data source identifier
	 * @param message
	 *            the status message
	 * @param durationMillis
	 *            duration of the process described by this status, in milliseconds.
	 * @return a {@link StatusMetaData} containing the message.
	 */
	public static StatusMetaData newInstance(final String dataSourceId,
			final String message, final long durationMillis)
	{
		final StatusMetaData statusMetaData = newInstance();
		statusMetaData.setDataSourceId(dataSourceId);
		statusMetaData.setStatus(message);
		statusMetaData.setDuration(durationMillis);
		return statusMetaData;
	}

	/**
	 * Generates a new {@link StatusMetaData} message instance from a {@link Named} type
	 *
	 * @param dataSourceId
	 *            data source identifier
	 * @param named
	 *            the named element
	 * @return a {@link StatusMetaData} containing the message.
	 */
	public static StatusMetaData newInstance(final String dataSourceId, final Named named)
	{
		return newInstance(dataSourceId, named.getName());
	}

	/**
	 * A copy-constructor factory method.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 * @return a deep copy of <code>other</code>
	 */
	public static StatusMetaDataToImpl newCopy(final StatusMetaData other)
	{
		return new StatusMetaDataToImpl().copyFrom(other);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.misc.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public StatusMetaDataToImpl copyFrom(final StatusMetaData other)
	{
		if (other == null)
		{
			return this;
		}

		// Copy fields
		this.id = other.getId();
		setDataSourceId(other.getDataSourceId());
		setStatus(other.getStatus());
		setStatusDate(other.getStatusDate());
		setDuration(other.getDuration());
		setQueryContextId(other.getQueryContextId());

		if (log.isTraceEnabled())
		{
			log.trace("copyFrom()");
			log.trace("other: " + other);
			log.trace("this : " + this);
		}
		return this;
	}

	// ========================= IMPLEMENTATION: StatusMetaData =========

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

	/**
	 * Return the queryContext property.
	 *
	 * @return the queryContext
	 */
	@Override
	public QueryContextTo getQueryContext()
	{
		return queryContext;
	}

	/**
	 * Set a new value for the queryContext property.
	 *
	 * @param queryContext
	 *            the queryContext to set
	 */
	@Override
	public void setQueryContext(final QueryContext queryContext)
	{
		this.queryContext = QueryContextToImpl.newCopy(queryContext);
		if (queryContext != null)
		{
			this.queryContextId = queryContext.getId();
		}
	}

	/**
	 * Return the owning query context's ID property. Doesn't have a public setter because
	 * it is managed by {@link #setQueryContext(QueryContext)}.
	 *
	 * @return the queryContextId
	 * @see edu.utah.further.fqe.ds.api.domain.AbstractStatusMetaData#getQueryContextId()
	 */
	@Override
	public Long getQueryContextId()
	{
		return queryContextId;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * FOR JAXB USE ONLY!!!
	 * <p>
	 * Set a new value for the queryContextId property.
	 *
	 * @param queryContextId
	 *            the queryContextId to set
	 */
	private void setQueryContextId(final Long queryContextId)
	{
		this.queryContextId = queryContextId;
	}
}
