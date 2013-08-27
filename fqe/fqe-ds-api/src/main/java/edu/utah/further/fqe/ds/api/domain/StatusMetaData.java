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

import java.util.Date;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.CopyableFrom;

/**
 * Represents the current status of a given QueryContext as it is passed through the data
 * source lifecycle. This is different that the {@link QueryState} in that there could be
 * several different status' for one given {@link QueryState}. Typically this information
 * will be conveyed to the UI or auditing/logging.
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
public interface StatusMetaData extends PersistentEntity<Long>,
		CopyableFrom<StatusMetaData, StatusMetaData>
{
	// ========================= CONSTANTS =================================

	/**
	 * State date property JAXB and database column name.
	 */
	String PROPERTY_STATUS_DATE = "statusDate";

	// ========================= METHODS ===================================

	/**
	 * Return the unique identifier of the data source that generated this this message.
	 *
	 * @return the dataSourceId
	 */
	String getDataSourceId();

	/**
	 * Set a new value for the dataSourceId property.
	 *
	 * @param dataSourceId
	 *            the dataSourceId to set
	 */
	void setDataSourceId(String dataSourceId);

	/**
	 * Returns the status message.
	 *
	 * @return a message describing the current status of the context
	 */
	String getStatus();

	/**
	 * Sets the status message.
	 *
	 * @param status
	 *            a message representing the status of the context
	 */
	void setStatus(String status);

	/**
	 * Explicitly sets the instance in time for when this status was set. Most callers
	 * need not use this method however it is provided if a time is needed to be
	 * explicitly set. Calling {@link #getStatusDate()} will return the time at which the
	 * status was created by default.
	 *
	 * @param statusDate
	 *            status date to set
	 */
	void setStatusDate(Date statusDate);

	/**
	 * Gets the instance in time when this status was set
	 *
	 * @return the instance in time when this status was set
	 */
	Date getStatusDate();

	/**
	 * Return the status date in milliseconds since the beginning of the epoch.
	 *
	 * @return status date in milliseconds
	 */
	Long getStatusTime();

	/**
	 * Return the duration of the process reporting this status, in milliseconds. Can be
	 * <code>0</code> if this status is not associated with a point in time rather than an
	 * interval.
	 *
	 * @return the duration
	 */
	long getDuration();

	/**
	 * Set a new value for the duration property.
	 *
	 * @param duration
	 *            the duration to set
	 */
	void setDuration(final long duration);

	/**
	 * Return the queryContext property.
	 *
	 * @return the queryContext
	 */
	QueryContext getQueryContext();

	/**
	 * Set a new value for the queryContext property.
	 *
	 * @param queryContext
	 *            the queryContext to set
	 */
	void setQueryContext(QueryContext queryContext);

	/**
	 * Return the owning query context's ID property. Doesn't have a public setter because
	 * it is managed by {@link #setQueryContext(QueryContext)}.
	 *
	 * @return the owning query context ID
	 */
	Long getQueryContextId();
}
