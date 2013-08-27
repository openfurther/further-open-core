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
 * The persistent class for the PAYER_PLAN_PERIOD database table.
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
@Table(name = "PAYER_PLAN_PERIOD")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PayerPlanPeriod implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PAYER_PLAN_PERIOD_ID", unique = true, nullable = false)
	private Long payerPlanPeriodId;

	@Column(name = "FAMILY_SOURCE_VALUE", length = 50)
	private String familySourceValue;

	@Temporal(TemporalType.DATE)
	@Column(name = "PAYER_PLAN_PERIOD_END_DATE", nullable = false)
	private Date payerPlanPeriodEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "PAYER_PLAN_PERIOD_START_DATE", nullable = false)
	private Date payerPlanPeriodStartDate;

	@Column(name = "PAYER_SOURCE_VALUE", length = 50)
	private String payerSourceValue;

	@Column(name = "PLAN_SOURCE_VALUE", length = 50)
	private String planSourceValue;

	// bi-directional many-to-one association to PersonEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", nullable = false)
	private Person person;

	/**
	 * Default constructor
	 */
	public PayerPlanPeriod()
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
		return this.payerPlanPeriodId;
	}

	public String getFamilySourceValue()
	{
		return this.familySourceValue;
	}

	public void setFamilySourceValue(final String familySourceValue)
	{
		this.familySourceValue = familySourceValue;
	}

	public Date getPayerPlanPeriodEndDate()
	{
		return this.payerPlanPeriodEndDate;
	}

	public void setPayerPlanPeriodEndDate(final Date payerPlanPeriodEndDate)
	{
		this.payerPlanPeriodEndDate = payerPlanPeriodEndDate;
	}

	public Date getPayerPlanPeriodStartDate()
	{
		return this.payerPlanPeriodStartDate;
	}

	public void setPayerPlanPeriodStartDate(final Date payerPlanPeriodStartDate)
	{
		this.payerPlanPeriodStartDate = payerPlanPeriodStartDate;
	}

	public String getPayerSourceValue()
	{
		return this.payerSourceValue;
	}

	public void setPayerSourceValue(final String payerSourceValue)
	{
		this.payerSourceValue = payerSourceValue;
	}

	public String getPlanSourceValue()
	{
		return this.planSourceValue;
	}

	public void setPlanSourceValue(final String planSourceValue)
	{
		this.planSourceValue = planSourceValue;
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