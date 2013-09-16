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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;


/**
 * The persistent class for the CONDITION_ERA database table.
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
@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements PersistentEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PERSON_ID")
	private Long personId;

	@Column(name="GENDER_CONCEPT_ID")
	private BigDecimal genderConceptId;

	@Column(name="LOCATION_CONCEPT_ID")
	private BigDecimal locationConceptId;

	@Column(name="RACE_CONCEPT_ID")
	private BigDecimal raceConceptId;

	@Column(name="SOURCE_GENDER_CODE")
	private String sourceGenderCode;

	@Column(name="SOURCE_LOCATION_CODE")
	private String sourceLocationCode;

	@Column(name="SOURCE_PERSON_KEY")
	private String sourcePersonKey;

	@Column(name="SOURCE_RACE_CODE")
	private String sourceRaceCode;

	@Column(name="YEAR_OF_BIRTH")
	private BigDecimal yearOfBirth;

	//bi-directional many-to-one association to ConditionEra
	@OneToMany(mappedBy="person")
	@XmlTransient
	private List<ConditionEra> conditionEras;

	//bi-directional many-to-one association to ConditionOccurrence
	@OneToMany(mappedBy="person")
	@XmlTransient
	private List<ConditionOccurrence> conditionOccurrences;

	//bi-directional many-to-one association to DrugEra
	@OneToMany(mappedBy="person")
	@XmlTransient
	private List<DrugEra> drugEras;

	//bi-directional many-to-one association to DrugExposure
	@OneToMany(mappedBy="person")
	@XmlTransient
	private List<DrugExposure> drugExposures;

	//bi-directional many-to-one association to Observation
	@OneToMany(mappedBy="person")
	@XmlTransient
	private List<Observation> observations;

	//bi-directional many-to-one association to ObservationPeriod
	@OneToMany(mappedBy="person")
	@XmlTransient
	private List<ObservationPeriod> observationPeriods;

	//bi-directional many-to-one association to ProcedureOccurrence
	@OneToMany(mappedBy="person")
	@XmlTransient
	private List<ProcedureOccurrence> procedureOccurrences;

	//bi-directional many-to-one association to VisitOccurrence
	@OneToMany(mappedBy="person")
	@XmlTransient
	private List<VisitOccurrence> visitOccurrences;

	public Person() {
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId() {
		return this.personId;
	}

	protected void setId(final Long personId) {
		this.personId = personId;
	}

	public BigDecimal getGenderConceptId() {
		return this.genderConceptId;
	}

	public void setGenderConceptId(final BigDecimal genderConceptId) {
		this.genderConceptId = genderConceptId;
	}

	public BigDecimal getLocationConceptId() {
		return this.locationConceptId;
	}

	public void setLocationConceptId(final BigDecimal locationConceptId) {
		this.locationConceptId = locationConceptId;
	}

	public BigDecimal getRaceConceptId() {
		return this.raceConceptId;
	}

	public void setRaceConceptId(final BigDecimal raceConceptId) {
		this.raceConceptId = raceConceptId;
	}

	public String getSourceGenderCode() {
		return this.sourceGenderCode;
	}

	public void setSourceGenderCode(final String sourceGenderCode) {
		this.sourceGenderCode = sourceGenderCode;
	}

	public String getSourceLocationCode() {
		return this.sourceLocationCode;
	}

	public void setSourceLocationCode(final String sourceLocationCode) {
		this.sourceLocationCode = sourceLocationCode;
	}

	public String getSourcePersonKey() {
		return this.sourcePersonKey;
	}

	public void setSourcePersonKey(final String sourcePersonKey) {
		this.sourcePersonKey = sourcePersonKey;
	}

	public String getSourceRaceCode() {
		return this.sourceRaceCode;
	}

	public void setSourceRaceCode(final String sourceRaceCode) {
		this.sourceRaceCode = sourceRaceCode;
	}

	public BigDecimal getYearOfBirth() {
		return this.yearOfBirth;
	}

	public void setYearOfBirth(final BigDecimal yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public List<ConditionEra> getConditionEras() {
		return this.conditionEras;
	}

	public void setConditionEras(final List<ConditionEra> conditionEras) {
		this.conditionEras = conditionEras;
	}

	public ConditionEra addConditionEra(final ConditionEra conditionEra) {
		getConditionEras().add(conditionEra);
		conditionEra.setPerson(this);

		return conditionEra;
	}

	public ConditionEra removeConditionEra(final ConditionEra conditionEra) {
		getConditionEras().remove(conditionEra);
		conditionEra.setPerson(null);

		return conditionEra;
	}

	public List<ConditionOccurrence> getConditionOccurrences() {
		return this.conditionOccurrences;
	}

	public void setConditionOccurrences(final List<ConditionOccurrence> conditionOccurrences) {
		this.conditionOccurrences = conditionOccurrences;
	}

	public ConditionOccurrence addConditionOccurrence(final ConditionOccurrence conditionOccurrence) {
		getConditionOccurrences().add(conditionOccurrence);
		conditionOccurrence.setPerson(this);

		return conditionOccurrence;
	}

	public ConditionOccurrence removeConditionOccurrence(final ConditionOccurrence conditionOccurrence) {
		getConditionOccurrences().remove(conditionOccurrence);
		conditionOccurrence.setPerson(null);

		return conditionOccurrence;
	}

	public List<DrugEra> getDrugEras() {
		return this.drugEras;
	}

	public void setDrugEras(final List<DrugEra> drugEras) {
		this.drugEras = drugEras;
	}

	public DrugEra addDrugEra(final DrugEra drugEra) {
		getDrugEras().add(drugEra);
		drugEra.setPerson(this);

		return drugEra;
	}

	public DrugEra removeDrugEra(final DrugEra drugEra) {
		getDrugEras().remove(drugEra);
		drugEra.setPerson(null);

		return drugEra;
	}

	public List<DrugExposure> getDrugExposures() {
		return this.drugExposures;
	}

	public void setDrugExposures(final List<DrugExposure> drugExposures) {
		this.drugExposures = drugExposures;
	}

	public DrugExposure addDrugExposure(final DrugExposure drugExposure) {
		getDrugExposures().add(drugExposure);
		drugExposure.setPerson(this);

		return drugExposure;
	}

	public DrugExposure removeDrugExposure(final DrugExposure drugExposure) {
		getDrugExposures().remove(drugExposure);
		drugExposure.setPerson(null);

		return drugExposure;
	}

	public List<Observation> getObservations() {
		return this.observations;
	}

	public void setObservations(final List<Observation> observations) {
		this.observations = observations;
	}

	public Observation addObservation(final Observation observation) {
		getObservations().add(observation);
		observation.setPerson(this);

		return observation;
	}

	public Observation removeObservation(final Observation observation) {
		getObservations().remove(observation);
		observation.setPerson(null);

		return observation;
	}

	public List<ObservationPeriod> getObservationPeriods() {
		return this.observationPeriods;
	}

	public void setObservationPeriods(final List<ObservationPeriod> observationPeriods) {
		this.observationPeriods = observationPeriods;
	}

	public ObservationPeriod addObservationPeriod(final ObservationPeriod observationPeriod) {
		getObservationPeriods().add(observationPeriod);
		observationPeriod.setPerson(this);

		return observationPeriod;
	}

	public ObservationPeriod removeObservationPeriod(final ObservationPeriod observationPeriod) {
		getObservationPeriods().remove(observationPeriod);
		observationPeriod.setPerson(null);

		return observationPeriod;
	}

	public List<ProcedureOccurrence> getProcedureOccurrences() {
		return this.procedureOccurrences;
	}

	public void setProcedureOccurrences(final List<ProcedureOccurrence> procedureOccurrences) {
		this.procedureOccurrences = procedureOccurrences;
	}

	public ProcedureOccurrence addProcedureOccurrence(final ProcedureOccurrence procedureOccurrence) {
		getProcedureOccurrences().add(procedureOccurrence);
		procedureOccurrence.setPerson(this);

		return procedureOccurrence;
	}

	public ProcedureOccurrence removeProcedureOccurrence(final ProcedureOccurrence procedureOccurrence) {
		getProcedureOccurrences().remove(procedureOccurrence);
		procedureOccurrence.setPerson(null);

		return procedureOccurrence;
	}

	public List<VisitOccurrence> getVisitOccurrences() {
		return this.visitOccurrences;
	}

	public void setVisitOccurrences(final List<VisitOccurrence> visitOccurrences) {
		this.visitOccurrences = visitOccurrences;
	}

	public VisitOccurrence addVisitOccurrence(final VisitOccurrence visitOccurrence) {
		getVisitOccurrences().add(visitOccurrence);
		visitOccurrence.setPerson(this);

		return visitOccurrence;
	}

	public VisitOccurrence removeVisitOccurrence(final VisitOccurrence visitOccurrence) {
		getVisitOccurrences().remove(visitOccurrence);
		visitOccurrence.setPerson(null);

		return visitOccurrence;
	}

}