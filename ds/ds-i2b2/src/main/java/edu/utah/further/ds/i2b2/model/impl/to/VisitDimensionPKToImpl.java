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

import static edu.utah.further.core.api.math.ArithmeticUtil.newBigIntegerNullSafe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.ds.i2b2.model.api.domain.VisitDimensionId;
import edu.utah.further.ds.i2b2.model.api.to.VisitDimensionIdTo;
import edu.utah.further.ds.i2b2.model.impl.domain.VisitDimensionPK;

/**
 * Visit Dimension PK To
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
 * @version April 15, 2010
 */
@XmlRootElement(name = "VisitDimensionPK")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VisitDimensionPK", propOrder =
{ "encounterNum", "patientNum" })
public class VisitDimensionPKToImpl implements VisitDimensionIdTo
{

	@XmlElement(required = true)
	private String encounterNum;
	@XmlElement(required = true)
	private String patientNum;

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionIdTo#getEncounterNum()
	 */
	@Override
	public String getEncounterNum()
	{
		return encounterNum;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionIdTo#setEncounterNum(java.lang.String)
	 */
	@Override
	public void setEncounterNum(final String value)
	{
		this.encounterNum = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionIdTo#getPatientNum()
	 */
	@Override
	public String getPatientNum()
	{
		return patientNum;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionIdTo#setPatientNum(java.lang.String)
	 */
	@Override
	public void setPatientNum(final String value)
	{
		this.patientNum = value;
	}

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
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public VisitDimensionId copy()
	{
		final VisitDimensionPK entity = new VisitDimensionPK();
		entity.setEncounterNum(newBigIntegerNullSafe(this.getEncounterNum()));
		entity.setPatientNum(newBigIntegerNullSafe(this.getPatientNum()));
		return entity;
	}

}
