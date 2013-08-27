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

import java.math.BigDecimal;
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
 * The persistent class for the CONDITION_OCCURENCE_REF database table.
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
@Table(name = "CONDITION_OCCURRENCE_REF")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionOccurrenceRef implements PersistentEntity<String>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONDITION_OCCURRENCE_TYPE")
	private String conditionOccurrenceType;

	@Column(name = "CONDITION_OCCURRENCE_POSITION")
	private String conditionOccurrencePosition;

	@Column(name = "CONDITION_OCCURRENCE_TYPE_DESC")
	private String conditionOccurrenceTypeDesc;

	@Column(name = "PERSISTENCE_WINDOW")
	private BigDecimal persistenceWindow;

	// bi-directional many-to-one association to ConditionEra
	@OneToMany(mappedBy = "conditionOccurrenceRef")
	@XmlTransient
	private List<ConditionEra> conditionEras;

	// bi-directional many-to-one association to ConditionOccurrence
	@OneToMany(mappedBy = "conditionOccurrenceRef")
	@XmlTransient
	private List<ConditionOccurrence> conditionOccurrences;

	public ConditionOccurrenceRef()
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
		return this.conditionOccurrenceType;
	}

	protected void setId(final String conditionOccurrenceType)
	{
		this.conditionOccurrenceType = conditionOccurrenceType;
	}

	public String getConditionOccurrencePosition()
	{
		return this.conditionOccurrencePosition;
	}

	public void setConditionOccurrencePosition(final String conditionOccurrencePosition)
	{
		this.conditionOccurrencePosition = conditionOccurrencePosition;
	}

	public String getConditionOccurrenceTypeDesc()
	{
		return this.conditionOccurrenceTypeDesc;
	}

	public void setConditionOccurrenceTypeDesc(final String conditionOccurrenceTypeDesc)
	{
		this.conditionOccurrenceTypeDesc = conditionOccurrenceTypeDesc;
	}

	public BigDecimal getPersistenceWindow()
	{
		return this.persistenceWindow;
	}

	public void setPersistenceWindow(final BigDecimal persistenceWindow)
	{
		this.persistenceWindow = persistenceWindow;
	}

	public List<ConditionEra> getConditionEras()
	{
		return this.conditionEras;
	}

	public void setConditionEras(final List<ConditionEra> conditionEras)
	{
		this.conditionEras = conditionEras;
	}

	public ConditionEra addConditionEra(final ConditionEra conditionEra)
	{
		getConditionEras().add(conditionEra);
		conditionEra.setConditionOccurrenceRef(this);

		return conditionEra;
	}

	public ConditionEra removeConditionEra(final ConditionEra conditionEra)
	{
		getConditionEras().remove(conditionEra);
		conditionEra.setConditionOccurrenceRef(null);

		return conditionEra;
	}

	public List<ConditionOccurrence> getConditionOccurrences()
	{
		return this.conditionOccurrences;
	}

	public void setConditionOccurrences(final List<ConditionOccurrence> conditionOccurrences)
	{
		this.conditionOccurrences = conditionOccurrences;
	}

	public ConditionOccurrence addConditionOccurrence(
			final ConditionOccurrence conditionOccurrence)
	{
		getConditionOccurrences().add(conditionOccurrence);
		conditionOccurrence.setConditionOccurrenceRef(this);

		return conditionOccurrence;
	}

	public ConditionOccurrence removeConditionOccurrence(
			final ConditionOccurrence conditionOccurrence)
	{
		getConditionOccurrences().remove(conditionOccurrence);
		conditionOccurrence.setConditionOccurrenceRef(null);

		return conditionOccurrence;
	}

}