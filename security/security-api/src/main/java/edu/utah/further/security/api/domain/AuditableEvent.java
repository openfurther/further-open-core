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
package edu.utah.further.security.api.domain;

import java.util.Date;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * An interface designed for any auditable event in FURTHeR.  
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
 * @version May 4, 2011
 */

public interface AuditableEvent extends PersistentEntity<Long>
{
	
	// ========================= METHODS ===================================
	
	/**
	 * Return the event type, including login, logout, query, etc.
	 * @return event type
	 */
	String getEventType ();
	
	/**
	 * Set the event type.
	 * @param eventType
	 */
	void setEventType(String eventType);

	/**
	 * Return the date and time the event happened
	 * @return date
	 */
	Date getDate();

	/**
	 * Set the date and time the event happened
	 * @param date
	 */
	void setDate (Date date);
	
	/**
	 * Return the status of the event. It has different values depending on the event type. For login/logout events, it would be successful/failed. 
	 * For query events, it would be queued, completed, or failed.
	 * @return event status
	 */
	String getEventStatus ();

	/**
	 * Set the status of the event. It has different values depending on the event type. For login/logout events, it would be successful/failed. 
	 * For query events, it would be queued, completed, or failed.
	 * @param status
	 */
	void setEventStatus (String status);

	/**
	 * Return the source of the event. It has different values depending on the event type. For login/logout events, it would be the requesting 
	 * application, such as i2b2, FURTHeR Portal, or CAS when the user directly logs in through CAS. For query events, it would be the data source
	 * name, such as UPDBL, UUDEW or null when the parent query is logged.  
	 * @return event source
	 */
	String getEventSource();
	
	/**
	 * Set the source of the event. It has different values depending on the event type. For login/logout events, it would be the requesting 
	 * application, such as i2b2, FURTHeR Portal, or CAS when the user directly logs in through CAS. For query events, it would be the data source
	 * name, such as UPDBL, UUDEW or null when the logical (parent) query is logged.
	 * @param eventSource
	 */
	void setEventSource(String eventSource);
	
	
	/**
	 * Return the user id responsible for the event.
	 * @return user id
	 */
	String getUserId ();
	
	/**
	 * Set the user id responsible for the event.
	 * @param userId
	 */
	void setUserId (String userId);

	/**
	 * This is mainly for query events auditing.
	 * Return {@link QueryContext}} in XML format when it is a logical query event; Return the query result (in aggregated number counts) and its parent 
	 * query id when it is a physical query event. 
	 * @return event description
	 */
	String getEventDescription();

	/**
	 * Set the event description, maily for query events.
	 * @param eventDescription
	 */
	void setEventDespription(String eventDescription);

	/**
	 * Return the authorization/regulatory body (UU, IHC, UDOH, etc.) after integration with e-IRBs in the future. It is null for now.
	 * @return authorization body
	 */
	String getAuthorizationBody();
	
	
	/**
	 * Set the authorization/regulatory body (UU, IHC, UDOH, etc.) after integration with e-IRBs in the future.
	 * @param authorizationBody
	 */
	void setAuthorizationBody(String authorizationBody);
	
	/**
	 * Get the authorization details. This could be the IRB number and a brief extract of the IRB review decision.
	 */
	String getAuthorizationDetail();
	
	/**
	 * Set the authorization details. This could be the IRB number and a brief extract of the IRB review decision.
	 * @param authorizationDetail
	 */
	void setAuthorizationDetail(String authorizationDetail);
	
}
