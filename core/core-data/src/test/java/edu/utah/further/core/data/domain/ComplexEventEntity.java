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
package edu.utah.further.core.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * A complex entity which contains relationships.
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
 * @version Sep 22, 2009
 */
@Entity
@Table(name = "Event")
public class ComplexEventEntity implements PersistentEntity<Long>
{
	// ========================= CONSTANTS =================================

	/**
	 * Serial ID
	 */
	@Transient
	private static final long serialVersionUID = 7578943207653776849L;

	// ========================= FIELDS ====================================

	/**
	 * Id of the event
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * The event name
	 */
	@Column(name = "event")
	private String eventName;

	@ManyToOne
	@JoinColumn(name = "person_id")
	private ComplexPersonEntity person;

	// ========================= IMPL: PersistentEntity ====================

	/**
	 * @return the id
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= GET & SET =================================

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * @return the eventName
	 */
	public String getEventName()
	{
		return eventName;
	}

	/**
	 * @param eventName
	 *            the eventName to set
	 */
	public void setEventName(final String eventName)
	{
		this.eventName = eventName;
	}

	/**
	 * @return the person
	 */
	public ComplexPersonEntity getPerson()
	{
		return person;
	}

	/**
	 * @param person
	 *            the person to set
	 */
	public void setPerson(final ComplexPersonEntity person)
	{
		this.person = person;
	}

}
