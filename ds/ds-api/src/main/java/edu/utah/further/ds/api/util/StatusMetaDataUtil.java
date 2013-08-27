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
package edu.utah.further.ds.api.util;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.text.TextTemplate;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;
import edu.utah.further.fqe.ds.api.to.StatusMetaDataToImpl;

/**
 * Common utility class related to status messages.
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
 * @version Feb 9, 2010
 */
@Utility
public final class StatusMetaDataUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StatusMetaDataUtil.class);

	/**
	 * Status message format.
	 */
	private static final TextTemplate STATUS_MESSAGE_TEMPLATE = new TextTemplate(
			"%processor% %status%", Arrays.asList("processor", "status"));

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Private constructor
	 */
	private StatusMetaDataUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Set a new data source status in a query context utilizing a standard
	 * {@link DataQueryStatus} message.
	 *
	 * @param queryContext
	 *            query context to update
	 * @param dsMetaData
	 *            data source meta data object
	 * @param processorName
	 *            chain request processor name
	 * @param statusType
	 *            status type (success/failure)
	 * @param durationMillis
	 *            duration of the process described by this status, in milliseconds
	 */
	public static void setCurrentStatus(final QueryContext queryContext,
			final DsMetaData dsMetaData, final String processorName,
			final StatusType statusType, final long durationMillis)
	{
		// Format message
		final String message = STATUS_MESSAGE_TEMPLATE.evaluate(Arrays.asList(
				processorName, statusType.getName()));
		setCurrentStatus(queryContext, dsMetaData.getName(), message, durationMillis);
	}

	/**
	 * Set a new data source status in a query context.
	 *
	 * @param queryContext
	 *            query context to update
	 * @param dataSourceId
	 *            data source identifier
	 * @param message
	 *            status textual description
	 * @param durationMillis
	 *            duration of the process described by this status, in milliseconds
	 * @return the generated status message
	 */
	public static void setCurrentStatus(final QueryContext queryContext,
			final String dataSourceId, final String message, final long durationMillis)
	{
		final StatusMetaData newCurrentStatus = StatusMetaDataToImpl.newInstance(
				dataSourceId, message, durationMillis);
		queryContext.setCurrentStatus(newCurrentStatus);
	}
}
