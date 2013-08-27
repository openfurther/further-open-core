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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.ds.i2b2.model.api.domain.VisitDimensionId;

/**
 * Visit Dimension Primary Key Entity
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
public class VisitDimensionPK implements VisitDimensionId
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7581826848658466470L;

	@Basic(optional = false)
	@Column(name = "ENCOUNTER_NUM")
	private BigInteger encounterNum;
	@Basic(optional = false)
	@Column(name = "PATIENT_NUM")
	private BigInteger patientNum;

	public VisitDimensionPK()
	{
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimensionId#getEncounterNum()
	 */
	@Override
	public BigInteger getEncounterNum()
	{
		return encounterNum;
	}

	/**
	 * @param encounterNum
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimensionId#setEncounterNum(java.math.BigInteger)
	 */
	@Override
	public void setEncounterNum(BigInteger encounterNum)
	{
		this.encounterNum = encounterNum;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimensionId#getPatientNum()
	 */
	@Override
	public BigInteger getPatientNum()
	{
		return patientNum;
	}

	/**
	 * @param patientNum
	 * @see edu.utah.further.ds.i2b2.model.api.domain.VisitDimensionId#setPatientNum(java.math.BigInteger)
	 */
	@Override
	public void setPatientNum(BigInteger patientNum)
	{
		this.patientNum = patientNum;
	}

	/**
	 * @param o
	 * @return
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(VisitDimensionId that)
	{
		return new CompareToBuilder()
				.append(this.getEncounterNum(), that.getEncounterNum())
				.append(this.getPatientNum(), that.getPatientNum())
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

		final VisitDimensionId that = (VisitDimensionId) object;
		return new EqualsBuilder()
				.append(this.getEncounterNum(), that.getEncounterNum())
				.append(this.getPatientNum(), that.getPatientNum())
				.isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getEncounterNum()).append(
				this.getPatientNum()).toHashCode();
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
	public VisitDimensionId copy()
	{
		final VisitDimensionPK entity = new VisitDimensionPK();
		entity.setEncounterNum(this.getEncounterNum());
		entity.setPatientNum(this.getPatientNum());
		return entity;
	}

}
