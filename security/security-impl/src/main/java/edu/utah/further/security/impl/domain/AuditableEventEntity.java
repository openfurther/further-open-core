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
package edu.utah.further.security.impl.domain;

//import static org.slf4j.LoggerFactory.getLogger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import edu.utah.further.core.api.lang.Final;
import edu.utah.further.security.api.domain.AuditableEvent;

/**
 *  A {@link AuditableEvent } persistent entity implementation.
 *  Currently we only audit query events in this table. The login/logout events are currently logged in another table due to the usage of some open
 *  source auditing tools for CAS.
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

//============================
//Hibernate annotations
//============================
@Entity
@Table(name = "audit_log")
public class AuditableEventEntity implements AuditableEvent
{
	

	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;
	

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity.
	 * 
	 */
	@Id
	@GeneratedValue
	@Column(name = "audit_log_id")
	@Final
	Long id;
	
	
	/**
	 * The type of the event. For query events, it would be "query".
	 */
	@Column(name = "event_type_cd", updatable = false)
	private String eventType;
	
	/**
	 * The date for the event
	 */

	@Column(name = "event_dts", updatable = false)
	private Date eventDate;
	
	/**
	 * The status of the event. For query events, it would be "queued, completed, or failed".
	 */
	@Column(name = "event_status_cd", updatable = false)
	private String eventStatus;
	
	/**
	 * The source of the event. For query events, it would be the data source name.
	 */

	@Column(name = "event_source", updatable = false)
	private String eventSource;


	/**
	 * The description of the event.
	 */
	@Column(name = "event_dsc", updatable = false)
	@Lob
	private String eventDescription;
	
	
	/**
	 * The user id responsible for the event.
	 */
	@Column(name = "user_id", updatable = false)
	private String userId;
	
	/**
	 * The authorization/regulatory body (UU, IHC, UDOH, etc.) after integration with e-IRBs in the future. It is null for now.
	 */
	@Column(name = "authorization_body", updatable = false)
	private String authoriztionBody;
	
	/**
	 * The authorization details. This could be the IRB number and a brief extract of the IRB review decision. It is null for now.
	 */
	@Column(name = "authorization_detail", updatable = false)
	private String authorizationDetail;


	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public AuditableEventEntity(){
		super();
	}

	// ========================= IMPLEMENTATION: HasIdentifier =============

	/**
	 * @return
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= IMPLEMENTATION: QueryContext ==============

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#getDate()
	 */
	@Override
	public Date getDate()
	{
		return this.eventDate;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#getEventDescription()
	 */
	@Override
	@Lob
	public String getEventDescription()
	{
		return this.eventDescription;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#getEventSource()
	 */
	@Override
	public String getEventSource()
	{
		return this.eventSource;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#getEventStatus()
	 */
	@Override
	public String getEventStatus()
	{
		return this.eventStatus;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#getEventType()
	 */
	@Override
	public String getEventType()
	{
		return this.eventType;
	}


	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#getUserId()
	 */
	@Override
	public String getUserId()
	{
		return this.userId;
	}


	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#setDate(java.util.Date)
	 */
	@Override
	public void setDate(final Date date)
	{
		this.eventDate=date;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#setEventDespription(java.lang.String)
	 */
	@Override
	@Lob
	public void setEventDespription(final String eventDescription)
	{
		this.eventDescription=eventDescription;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#setEventSource(java.lang.String)
	 */
	@Override
	public void setEventSource(final String eventSource)
	{
		this.eventSource=eventSource;

	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#setEventStatus(java.lang.String)
	 */
	@Override
	public void setEventStatus(final String status)
	{
		this.eventStatus=status;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#setEventType(java.lang.String)
	 */
	@Override
	public void setEventType(final String eventType)
	{
		this.eventType=eventType;
	}


	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#setUserId(java.lang.String)
	 */
	@Override
	public void setUserId(final String userId)
	{
		this.userId=userId;
	}

	
	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#getAuthorizationBody()
	 */
	@Override
	public String getAuthorizationBody()
	{
		return this.authoriztionBody;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#getAuthorizationDetail()
	 */
	@Override
	public String getAuthorizationDetail()
	{
		return this.authorizationDetail;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#setAuthorizationBody(java.lang.String)
	 */
	@Override
	public void setAuthorizationBody(String authorizationBody)
	{	
		this.authoriztionBody=authorizationBody;
		
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.security.api.audit.domain.AuditableEvent#setAuthorizationDetail(java.lang.String)
	 */
	@Override
	public void setAuthorizationDetail(String authorizationDetail)
	{
		this.authorizationDetail=authorizationDetail;
	}


}
