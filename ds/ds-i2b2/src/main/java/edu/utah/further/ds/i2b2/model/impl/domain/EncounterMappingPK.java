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

import edu.utah.further.ds.i2b2.model.api.domain.EncounterMappingId;

/**
 * Encounter Primary Key Entity
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
public class EncounterMappingPK implements EncounterMappingId
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4454201174194365725L;

	@Basic(optional = false)
	@Column(name = "ENCOUNTER_IDE")
	private String encounterIde;
	@Basic(optional = false)
	@Column(name = "ENCOUNTER_IDE_SOURCE")
	private String encounterIdeSource;

	public EncounterMappingPK()
	{
	}

	public EncounterMappingPK(String encounterIde, String encounterIdeSource)
	{
		this.encounterIde = encounterIde;
		this.encounterIdeSource = encounterIdeSource;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMappingId#getEncounterIde()
	 */
	@Override
	public String getEncounterIde()
	{
		return encounterIde;
	}

	/**
	 * @param encounterIde
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMappingId#setEncounterIde(java.lang.String)
	 */
	@Override
	public void setEncounterIde(String encounterIde)
	{
		this.encounterIde = encounterIde;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMappingId#getEncounterIdeSource()
	 */
	@Override
	public String getEncounterIdeSource()
	{
		return encounterIdeSource;
	}

	/**
	 * @param encounterIdeSource
	 * @see edu.utah.further.ds.i2b2.model.api.domain.EncounterMappingId#setEncounterIdeSource(java.lang.String)
	 */
	@Override
	public void setEncounterIdeSource(String encounterIdeSource)
	{
		this.encounterIdeSource = encounterIdeSource;
	}

	/**
	 * @param o
	 * @return
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(EncounterMappingId that)
	{
		return new CompareToBuilder().append(this.getEncounterIde(),
				that.getEncounterIde()).append(this.getEncounterIdeSource(),
				that.getEncounterIdeSource()).toComparison();
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

		final EncounterMappingId that = (EncounterMappingId) object;
		return new EqualsBuilder()
				.append(this.getEncounterIde(), that.getEncounterIde())
				.append(this.getEncounterIdeSource(), that.getEncounterIdeSource())
				.isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getEncounterIde()).append(
				this.getEncounterIdeSource()).toHashCode();
	}

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
	public EncounterMappingId copy()
	{
		final EncounterMappingId entity = new EncounterMappingPK();
		entity.setEncounterIde(this.getEncounterIde());
		entity.setEncounterIdeSource(this.getEncounterIdeSource());
		return entity;
	}

}
