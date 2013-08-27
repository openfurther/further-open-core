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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class for the VISIT_OCCURRENCE database table.
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
@Table(name = "VISIT_OCCURRENCE")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VisitOccurrence implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "VISIT_OCCURRENCE_ID", unique = true, nullable = false)
	private Long visitOccurrenceId;

	@Column(name = "CARE_SITE_ID")
	private BigDecimal careSiteId;

	@Column(name = "PLACE_OF_SERVICE_CONCEPT_ID", nullable = false)
	private BigDecimal placeOfServiceConceptId;

	@Column(name = "PLACE_OF_SERVICE_SOURCE_VALUE", length = 50)
	private String placeOfServiceSourceValue;

	@Temporal(TemporalType.DATE)
	@Column(name = "VISIT_END_DATE", nullable = false)
	private Date visitEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "VISIT_START_DATE", nullable = false)
	private Date visitStartDate;

	// bi-directional many-to-one association to PersonEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", nullable = false)
	private Person person;

	/**
	 * Default Constructor
	 */
	public VisitOccurrence()
	{
	}

	@Override
	public Long getId()
	{
		return this.visitOccurrenceId;
	}

	public BigDecimal getCareSiteId()
	{
		return this.careSiteId;
	}

	public void setCareSiteId(final BigDecimal careSiteId)
	{
		this.careSiteId = careSiteId;
	}

	public BigDecimal getPlaceOfServiceConceptId()
	{
		return this.placeOfServiceConceptId;
	}

	public void setPlaceOfServiceConceptId(final BigDecimal placeOfServiceConceptId)
	{
		this.placeOfServiceConceptId = placeOfServiceConceptId;
	}

	public String getPlaceOfServiceSourceValue()
	{
		return this.placeOfServiceSourceValue;
	}

	public void setPlaceOfServiceSourceValue(final String placeOfServiceSourceValue)
	{
		this.placeOfServiceSourceValue = placeOfServiceSourceValue;
	}

	public Date getVisitEndDate()
	{
		return this.visitEndDate;
	}

	public void setVisitEndDate(final Date visitEndDate)
	{
		this.visitEndDate = visitEndDate;
	}

	public Date getVisitStartDate()
	{
		return this.visitStartDate;
	}

	public void setVisitStartDate(final Date visitStartDate)
	{
		this.visitStartDate = visitStartDate;
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