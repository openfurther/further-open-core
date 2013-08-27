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
package edu.utah.further.ds.i2b2.model.impl.to;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.ds.i2b2.model.api.to.PatientDimensionIdTo;

/**
 * Patient Dimension Identifier Transfer Object
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author {@code Rick L. Bradshaw <rick.bradshaw@utah.edu>}
 * @version May 10, 2010
 */
@XmlRootElement(name = "PatientDimensionPK")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PatientDimensionPK", propOrder =
{ "patientNum", "queryId" })
public final class PatientDimensionPKToImpl implements PatientDimensionIdTo
{
	// ========================= CONSTANTS =================================

	/**
	 *
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 5770150145484225969L;

	// ========================= FIELDS ====================================

	@XmlElement(required = true)
	private Long patientNum;
	@XmlElement(required = true)
	private String queryId;

	// ========================= IMPL: PatientDimensionIdTo ================

	/**
	 * Return the patientNum property.
	 *
	 * @return the patientNum
	 */
	@Override
	public Long getPatientNum()
	{
		return patientNum;
	}

	/**
	 * Set a new value for the patientNum property.
	 *
	 * @param patientNum
	 *            the patientNum to set
	 */
	@Override
	public void setPatientNum(final Long patientNum)
	{
		this.patientNum = patientNum;
	}

	/**
	 * Return the queryId property.
	 *
	 * @return the queryId
	 */
	@Override
	public String getQueryId()
	{
		return queryId;
	}

	/**
	 * Set a new value for the queryId property.
	 *
	 * @param queryId
	 *            the queryId to set
	 */
	@Override
	public void setQueryId(final String queryId)
	{
		this.queryId = queryId;
	}

	// ========================= IMPL: Object ==============================

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object)
	{
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (getClass() != object.getClass())
			return false;

		final PatientDimensionIdTo that = (PatientDimensionIdTo) object;
		return new EqualsBuilder()
				.append(this.getPatientNum(), that.getPatientNum())
				.append(this.getQueryId(), that.getQueryId())
				.isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(this.getPatientNum())
				.append(this.getQueryId())
				.toHashCode();
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("patientNum", getPatientNum())
				.append("queryId", getQueryId())
				.toString();
	}
}
