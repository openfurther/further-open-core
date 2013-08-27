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
package edu.utah.further.ds.omop.model.v2.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class for the PROC_OCCURRENCE_REF database table.
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
 * @version Apr 24, 2013
 */
@Entity
@Table(name = "PROC_OCCURRENCE_REF")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcedureOccurrenceRef implements PersistentEntity<String>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROCEDURE_OCCURRENCE_TYPE")
	private String procedureOccurrenceType;

	@Column(name = "PROCEDURE_OCCURRENCE_POSITION")
	private String procedureOccurrencePosition;

	@Column(name = "PROCEDURE_OCCURRENCE_TYP_DESC")
	private String procedureOccurrenceTypDesc;

	// bi-directional many-to-one association to ProcedureOccurrence
	@OneToMany(mappedBy = "procOccurrenceRef")
	@XmlTransient
	private List<ProcedureOccurrence> procedureOccurrences;

	public ProcedureOccurrenceRef()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public String getId()
	{
		return this.procedureOccurrenceType;
	}

	protected void setId(final String procedureOccurrenceType)
	{
		this.procedureOccurrenceType = procedureOccurrenceType;
	}

	public String getProcedureOccurrencePosition()
	{
		return this.procedureOccurrencePosition;
	}

	public void setProcedureOccurrencePosition(final String procedureOccurrencePosition)
	{
		this.procedureOccurrencePosition = procedureOccurrencePosition;
	}

	public String getProcedureOccurrenceTypDesc()
	{
		return this.procedureOccurrenceTypDesc;
	}

	public void setProcedureOccurrenceTypDesc(final String procedureOccurrenceTypDesc)
	{
		this.procedureOccurrenceTypDesc = procedureOccurrenceTypDesc;
	}

	public List<ProcedureOccurrence> getProcedureOccurrences()
	{
		return this.procedureOccurrences;
	}

	public void setProcedureOccurrences(final List<ProcedureOccurrence> procedureOccurrences)
	{
		this.procedureOccurrences = procedureOccurrences;
	}

	public ProcedureOccurrence addProcedureOccurrence(
			final ProcedureOccurrence procedureOccurrence)
	{
		getProcedureOccurrences().add(procedureOccurrence);
		procedureOccurrence.setProcOccurrenceRef(this);

		return procedureOccurrence;
	}

	public ProcedureOccurrence removeProcedureOccurrence(
			final ProcedureOccurrence procedureOccurrence)
	{
		getProcedureOccurrences().remove(procedureOccurrence);
		procedureOccurrence.setProcOccurrenceRef(null);

		return procedureOccurrence;
	}

}