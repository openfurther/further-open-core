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
package edu.utah.further.ds.i2b2.model.impl.domain;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId;

/**
 * Observation Fact Primary Key Entity
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Rick L. Bradshaw <rick.bradshaw@utah.edu>}
 * @version April 15, 2010
 */
@Embeddable
public class ObservationFactPK implements ObservationFactId
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5371052282291032190L;

	@Basic(optional = false)
	@Column(name = "ENCOUNTER_NUM")
	private BigInteger encounterNum;
	@Basic(optional = false)
	@Column(name = "CONCEPT_CD")
	private String conceptCd;
	@Basic(optional = false)
	@Column(name = "PROVIDER_ID")
	private String providerId;
	@Basic(optional = false)
	@Column(name = "START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Basic(optional = false)
	@Column(name = "MODIFIER_CD")
	private String modifierCd;

	public ObservationFactPK()
	{
	}

	public ObservationFactPK(BigInteger encounterNum, String conceptCd,
			String providerId, Date startDate, String modifierCd)
	{
		this.encounterNum = encounterNum;
		this.conceptCd = conceptCd;
		this.providerId = providerId;
		this.startDate = startDate;
		this.modifierCd = modifierCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId#getEncounterNum()
	 */
	@Override
	public BigInteger getEncounterNum()
	{
		return encounterNum;
	}

	/**
	 * @param encounterNum
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId#setEncounterNum(java.math.BigInteger)
	 */
	@Override
	public void setEncounterNum(BigInteger encounterNum)
	{
		this.encounterNum = encounterNum;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId#getConceptCd()
	 */
	@Override
	public String getConceptCd()
	{
		return conceptCd;
	}

	/**
	 * @param conceptCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId#setConceptCd(java.lang.String)
	 */
	@Override
	public void setConceptCd(String conceptCd)
	{
		this.conceptCd = conceptCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId#getProviderId()
	 */
	@Override
	public String getProviderId()
	{
		return providerId;
	}

	/**
	 * @param providerId
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId#setProviderId(java.lang.String)
	 */
	@Override
	public void setProviderId(String providerId)
	{
		this.providerId = providerId;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId#getStartDate()
	 */
	@Override
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId#setStartDate(java.util.Date)
	 */
	@Override
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId#getModifierCd()
	 */
	@Override
	public String getModifierCd()
	{
		return modifierCd;
	}

	/**
	 * @param modifierCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ObservationFactId#setModifierCd(java.lang.String)
	 */
	@Override
	public void setModifierCd(String modifierCd)
	{
		this.modifierCd = modifierCd;
	}

	/**
	 * @param o
	 * @return
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ObservationFactId that)
	{
		return new CompareToBuilder()
				.append(this.getConceptCd(), that.getConceptCd())
				.append(this.getEncounterNum(), that.getEncounterNum())
				.append(this.getModifierCd(), that.getModifierCd())
				.append(this.getProviderId(), that.getProviderId())
				.append(this.getStartDate(), that.getStartDate())
				.toComparison();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object)
	{
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (getClass() != object.getClass())
			return false;

		final ObservationFactId that = (ObservationFactId) object;
		return new EqualsBuilder()
				.append(this.getConceptCd(), that.getConceptCd())
				.append(this.getEncounterNum(), that.getEncounterNum())
				.append(this.getModifierCd(), that.getModifierCd())
				.append(this.getProviderId(), that.getProviderId())
				.append(this.getStartDate(), that.getStartDate())
				.isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getConceptCd()).append(
				this.getEncounterNum()).append(this.getModifierCd()).append(
				this.getProviderId()).append(this.getStartDate()).toHashCode();
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public ObservationFactId copy()
	{
		final ObservationFactPK entity = new ObservationFactPK();
		entity.setConceptCd(this.getConceptCd());
		entity.setEncounterNum(this.getEncounterNum());
		entity.setModifierCd(this.getModifierCd());
		entity.setProviderId(this.getProviderId());
		entity.setStartDate(this.getStartDate());
		return entity;
	}

}
