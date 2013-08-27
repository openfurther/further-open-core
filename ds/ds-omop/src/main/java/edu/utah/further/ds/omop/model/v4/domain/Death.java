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
package edu.utah.further.ds.omop.model.v4.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class for the DEATH database table.
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
 * @version Apr 17, 2013
 */
@Entity
@Table(name = "DEATH")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Death implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PERSON_ID", unique = true, nullable = false)
	private Long personId;

	@Column(name = "CAUSE_OF_DEATH_CONCEPT_ID")
	private BigDecimal causeOfDeathConceptId;

	@Column(name = "CAUSE_OF_DEATH_SOURCE_VALUE", length = 50)
	private String causeOfDeathSourceValue;

	@Temporal(TemporalType.DATE)
	@Column(name = "DEATH_DATE", nullable = false)
	private Date deathDate;

	@Column(name = "DEATH_TYPE_CONCEPT_ID", nullable = false)
	private BigDecimal deathTypeConceptId;

	// bi-directional one-to-one association to PersonEntity
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", nullable = false, insertable = false, updatable = false)
	private Person person;

	/**
	 * Default constructor
	 */
	public Death()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return this.personId;
	}

	protected void setId(final Long personId)
	{
		this.personId = personId;
	}

	public BigDecimal getCauseOfDeathConceptId()
	{
		return this.causeOfDeathConceptId;
	}

	public void setCauseOfDeathConceptId(final BigDecimal causeOfDeathConceptId)
	{
		this.causeOfDeathConceptId = causeOfDeathConceptId;
	}

	public String getCauseOfDeathSourceValue()
	{
		return this.causeOfDeathSourceValue;
	}

	public void setCauseOfDeathSourceValue(final String causeOfDeathSourceValue)
	{
		this.causeOfDeathSourceValue = causeOfDeathSourceValue;
	}

	public Date getDeathDate()
	{
		return this.deathDate;
	}

	public void setDeathDate(final Date deathDate)
	{
		this.deathDate = deathDate;
	}

	public BigDecimal getDeathTypeConceptId()
	{
		return this.deathTypeConceptId;
	}

	public void setDeathTypeConceptId(final BigDecimal deathTypeConceptId)
	{
		this.deathTypeConceptId = deathTypeConceptId;
	}

	public Person getPerson()
	{
		return this.person;
	}

	public void setPerson(final Person person)
	{
		this.person = person;
	}

}