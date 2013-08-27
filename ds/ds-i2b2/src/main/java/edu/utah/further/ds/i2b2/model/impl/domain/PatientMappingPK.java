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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.ds.i2b2.model.api.domain.PatientMappingId;

/**
 * Patient Mapping Primary Key Entity
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
public class PatientMappingPK implements PatientMappingId
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5585988995002932798L;

	@Basic(optional = false)
	@Column(name = "PATIENT_IDE")
	private String patientIde;
	@Basic(optional = false)
	@Column(name = "PATIENT_IDE_SOURCE")
	private String patientIdeSource;

	public PatientMappingPK()
	{
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMappingId#getPatientIde()
	 */
	@Override
	public String getPatientIde()
	{
		return patientIde;
	}

	/**
	 * @param patientIde
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMappingId#setPatientIde(java.lang.String)
	 */
	@Override
	public void setPatientIde(String patientIde)
	{
		this.patientIde = patientIde;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMappingId#getPatientIdeSource()
	 */
	@Override
	public String getPatientIdeSource()
	{
		return patientIdeSource;
	}

	/**
	 * @param patientIdeSource
	 * @see edu.utah.further.ds.i2b2.model.api.domain.PatientMappingId#setPatientIdeSource(java.lang.String)
	 */
	@Override
	public void setPatientIdeSource(String patientIdeSource)
	{
		this.patientIdeSource = patientIdeSource;
	}

	/**
	 * @param o
	 * @return
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PatientMappingId that)
	{
		return new CompareToBuilder()
				.append(this.getPatientIde(), that.getPatientIde())
				.append(this.getPatientIdeSource(), that.getPatientIdeSource())
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

		final PatientMappingId that = (PatientMappingId) object;
		return new EqualsBuilder()
				.append(this.getPatientIde(), that.getPatientIde())
				.append(this.getPatientIdeSource(), that.getPatientIdeSource())
				.isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getPatientIde()).append(
				this.getPatientIdeSource()).toHashCode();
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
	public PatientMappingId copy()
	{
		final PatientMappingPK entity = new PatientMappingPK();
		entity.setPatientIde(this.getPatientIde());
		entity.setPatientIdeSource(this.getPatientIdeSource());
		return entity;
	}

}
