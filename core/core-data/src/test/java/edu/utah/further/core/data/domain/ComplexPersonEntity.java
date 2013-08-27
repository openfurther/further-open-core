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

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "Person")
public class ComplexPersonEntity implements PersistentEntity<Long>
{
	// ========================= CONSTANTS =================================

	/**
	 * Serial ID.
	 */
	@Transient
	private static final long serialVersionUID = -2801577767291743796L;

	// ========================= FIELDS ====================================

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "person", targetEntity = ComplexEventEntity.class)
	private Collection<ComplexEventEntity> events;

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
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the events
	 */
	public Collection<ComplexEventEntity> getEvents()
	{
		return events;
	}

	/**
	 * @param events
	 *            the events to set
	 */
	public void setEvents(final Collection<ComplexEventEntity> events)
	{
		this.events = events;
	}

}
