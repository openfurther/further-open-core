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
package edu.utah.further.security.impl.services;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Date;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.fqe.ds.api.domain.ExportContext;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.security.api.dao.AuditDao;
import edu.utah.further.security.api.domain.AuditableEvent;
import edu.utah.further.security.api.services.AuditService;
import edu.utah.further.security.impl.domain.AuditableEventEntity;

/**
 * Implementation of the audit services
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Shan He {@code <shan.he@utah.edu>}
 * @version May 5, 2011
 */
@Service("auditService")
public class AuditServiceImpl implements AuditService
{

	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AuditServiceImpl.class);

	/**
	 * The constant for "QUERY" event.
	 */
	private static final String QUERY_EVENT = "QUERY";
	
	/**
	 * The constant for "EXPORT" event.
	 */
	private static final String EXPORT_EVENT = "EXPORT";

	// ========================= DEPENDENCIES ==============================

	/**
	 * Handles generic DAO operations and searches.
	 */
	@Autowired
	@Qualifier("auditDao")
	private AuditDao auditDao;

	/**
	 * Marshalling/Unmarshalling service.
	 */
	@Autowired
	private XmlService xmlService;

	// ========================= IMPLEMENTATION: AuditService
	// ===============================

	/**
	 * Save the logical (parent) query context into persistent stores.
	 * 
	 * @param queryContext
	 */
	@Override
	public void logQuery(final QueryContext queryContext)
	{
		if (log.isDebugEnabled())
			log.debug("Logging the query");
		final AuditableEvent queryEvent = new AuditableEventEntity();
		try
		{

			final String queryContextString = xmlService.marshal(queryContext,
					xmlService.options());
			queryEvent.setEventDespription(queryContextString);
		}
		catch (final JAXBException e)
		{
			log.error("Failed to marshal queryContext for logging query", e);
		}
		queryEvent.setDate(new Date());
		queryEvent.setEventType(QUERY_EVENT);
		queryEvent.setEventStatus(queryContext.getState().name());
		queryEvent.setUserId(queryContext.getUserId());
		auditDao.logEvent(queryEvent);
	}

	/**
	 * Save the query result for physical (child) query context into persistent stores.
	 * The query result at this stage is the aggregated number counts.
	 * 
	 * @param queryContext
	 */
	@Override
	public void logQueryResult(final QueryContext queryContext)
	{
		if (log.isTraceEnabled())
		{

			log.trace("Query execution completed. Logging the query result");
			try
			{

				final String queryContextString = xmlService.marshal(queryContext,
						xmlService.options());
				log
						.trace("=============================Audit Log: Child Query Context string:=============================");
				log.trace(queryContextString);
			}
			catch (final JAXBException e)
			{
				log.error("Failed to marshal queryContext for logging query", e);
			}

		}
		// Parent is null using the following method
		// QueryContext parent = queryContext.getParent();
		// There is no ID in the query context
		// long id=queryContext.getId();

		final String dataSource = queryContext.getDataSourceId();
		final String state = queryContext.getState().name();
		final long parentId = ((QueryContextTo) queryContext).getParentId().longValue();
		final long numRecords = queryContext.getNumRecords();
		final String eventDescription = "Parent Query: " + parentId + "; Result: "
				+ numRecords;

		final AuditableEvent queryEvent = new AuditableEventEntity();
		queryEvent.setDate(queryContext.getEndDate());
		queryEvent.setEventSource(dataSource);
		queryEvent.setUserId(queryContext.getUserId());
		queryEvent.setEventStatus(state);
		queryEvent.setEventType(QUERY_EVENT);
		queryEvent.setEventDespription(eventDescription);

		auditDao.logEvent(queryEvent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.security.api.services.AuditService#logExport(edu.utah.further.
	 * fqe.ds.api.domain.ExportContext)
	 */
	@Override
	public void logExportRequest(final ExportContext context)
	{
		final AuditableEvent exportEvent = new AuditableEventEntity();
		exportEvent.setDate(new Date());
		exportEvent.setUserId(context.getUserId());
		exportEvent.setEventType(EXPORT_EVENT);
		exportEvent.setEventDespription("Export requested for query with origin id: " + context.getQueryId());

		auditDao.logEvent(exportEvent);
	}

}
