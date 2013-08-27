package edu.utah.further.ds.openmrs.model.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;


/**
 * The persistent class and data transfer object for the person database table.
 * 
 */
@Entity
@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements PersistentEntity<Integer> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="person_id")
	private Integer personId;

	@Temporal(TemporalType.DATE)
	private Date birthdate;

	@Column(name="birthdate_estimated")
	private byte birthdateEstimated;

	@Column(name="cause_of_death")
	private int causeOfDeath;

	@Column(name="changed_by")
	private int changedBy;

	private int creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_changed")
	private Date dateChanged;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_voided")
	private Date dateVoided;

	private byte dead;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="death_date")
	private Date deathDate;

	private String gender;

	private String uuid;

	@Column(name="void_reason")
	private String voidReason;

	private byte voided;

	@Column(name="voided_by")
	private int voidedBy;

	//bi-directional many-to-one association to Observation
	@OneToMany(mappedBy="person")
	@XmlTransient
	private List<Observation> observations;

	//bi-directional one-to-one association to Patient
	@OneToOne(mappedBy="person", fetch=FetchType.LAZY)
	@XmlTransient
	private Patient patient;

	public Person() {
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Integer getId() {
		return this.personId;
	}

	public void setId(final Integer personId) {
		this.personId = personId;
	}

	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(final Date birthdate) {
		this.birthdate = birthdate;
	}

	public byte getBirthdateEstimated() {
		return this.birthdateEstimated;
	}

	public void setBirthdateEstimated(final byte birthdateEstimated) {
		this.birthdateEstimated = birthdateEstimated;
	}

	public int getCauseOfDeath() {
		return this.causeOfDeath;
	}

	public void setCauseOfDeath(final int causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}

	public int getChangedBy() {
		return this.changedBy;
	}

	public void setChangedBy(final int changedBy) {
		this.changedBy = changedBy;
	}

	public int getCreator() {
		return this.creator;
	}

	public void setCreator(final int creator) {
		this.creator = creator;
	}

	public Date getDateChanged() {
		return this.dateChanged;
	}

	public void setDateChanged(final Date dateChanged) {
		this.dateChanged = dateChanged;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(final Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateVoided() {
		return this.dateVoided;
	}

	public void setDateVoided(final Date dateVoided) {
		this.dateVoided = dateVoided;
	}

	public byte getDead() {
		return this.dead;
	}

	public void setDead(final byte dead) {
		this.dead = dead;
	}

	public Date getDeathDate() {
		return this.deathDate;
	}

	public void setDeathDate(final Date deathDate) {
		this.deathDate = deathDate;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}

	public String getVoidReason() {
		return this.voidReason;
	}

	public void setVoidReason(final String voidReason) {
		this.voidReason = voidReason;
	}

	public byte getVoided() {
		return this.voided;
	}

	public void setVoided(final byte voided) {
		this.voided = voided;
	}

	public int getVoidedBy() {
		return this.voidedBy;
	}

	public void setVoidedBy(final int voidedBy) {
		this.voidedBy = voidedBy;
	}

	public List<Observation> getObservations() {
		return this.observations;
	}

	public void setObservations(final List<Observation> obs) {
		this.observations = obs;
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

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(final Patient patient) {
		this.patient = patient;
	}

}