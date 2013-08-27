package edu.utah.further.ds.openmrs.model.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class and data transfer object for the encounter database table.
 * 
 */
@Entity
@XmlRootElement(name = "Encounter")
@XmlAccessorType(XmlAccessType.FIELD)
public class Encounter implements PersistentEntity<Integer>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "encounter_id")
	private Integer encounterId;

	@Column(name = "changed_by")
	private int changedBy;

	private int creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_changed")
	private Date dateChanged;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_voided")
	private Date dateVoided;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "encounter_datetime")
	private Date encounterDatetime;

	@Column(name = "form_id")
	private int formId;

	@Column(name = "location_id")
	private int locationId;

	private String uuid;

	@Column(name = "visit_id")
	private int visitId;

	@Column(name = "void_reason")
	private String voidReason;

	private byte voided;

	@Column(name = "voided_by")
	private int voidedBy;

	// bi-directional many-to-one association to Patient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	@XmlTransient
	private Patient patient;

	// bi-directional many-to-one association to EncounterType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_type")
	@XmlTransient
	private EncounterType encounterType;

	// bi-directional many-to-one association to Observation
	@OneToMany(mappedBy = "encounter")
	@XmlTransient
	private List<Observation> observations;

	// bi-directional many-to-one association to Order
	@OneToMany(mappedBy = "encounter")
	@XmlTransient
	private List<Order> orders;

	public Encounter()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Integer getId()
	{
		return this.encounterId;
	}

	public void setId(final Integer encounterId)
	{
		this.encounterId = encounterId;
	}

	public int getChangedBy()
	{
		return this.changedBy;
	}

	public void setChangedBy(final int changedBy)
	{
		this.changedBy = changedBy;
	}

	public int getCreator()
	{
		return this.creator;
	}

	public void setCreator(final int creator)
	{
		this.creator = creator;
	}

	public Date getDateChanged()
	{
		return this.dateChanged;
	}

	public void setDateChanged(final Date dateChanged)
	{
		this.dateChanged = dateChanged;
	}

	public Date getDateCreated()
	{
		return this.dateCreated;
	}

	public void setDateCreated(final Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	public Date getDateVoided()
	{
		return this.dateVoided;
	}

	public void setDateVoided(final Date dateVoided)
	{
		this.dateVoided = dateVoided;
	}

	public Date getEncounterDatetime()
	{
		return this.encounterDatetime;
	}

	public void setEncounterDatetime(final Date encounterDatetime)
	{
		this.encounterDatetime = encounterDatetime;
	}

	public int getFormId()
	{
		return this.formId;
	}

	public void setFormId(final int formId)
	{
		this.formId = formId;
	}

	public int getLocationId()
	{
		return this.locationId;
	}

	public void setLocationId(final int locationId)
	{
		this.locationId = locationId;
	}

	public String getUuid()
	{
		return this.uuid;
	}

	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
	}

	public int getVisitId()
	{
		return this.visitId;
	}

	public void setVisitId(final int visitId)
	{
		this.visitId = visitId;
	}

	public String getVoidReason()
	{
		return this.voidReason;
	}

	public void setVoidReason(final String voidReason)
	{
		this.voidReason = voidReason;
	}

	public byte getVoided()
	{
		return this.voided;
	}

	public void setVoided(final byte voided)
	{
		this.voided = voided;
	}

	public int getVoidedBy()
	{
		return this.voidedBy;
	}

	public void setVoidedBy(final int voidedBy)
	{
		this.voidedBy = voidedBy;
	}

	public Patient getPatient()
	{
		return this.patient;
	}

	public void setPatient(final Patient patient)
	{
		this.patient = patient;
	}

	public EncounterType getEncounterTypeBean()
	{
		return this.encounterType;
	}

	public void setEncounterTypeBean(final EncounterType encounterTypeBean)
	{
		this.encounterType = encounterTypeBean;
	}

	public List<Observation> getObs()
	{
		return this.observations;
	}

	public void setObs(final List<Observation> obs)
	{
		this.observations = obs;
	}

	public Observation addOb(final Observation ob)
	{
		getObs().add(ob);
		ob.setEncounter(this);

		return ob;
	}

	public Observation removeOb(final Observation ob)
	{
		getObs().remove(ob);
		ob.setEncounter(null);

		return ob;
	}

	public List<Order> getOrders()
	{
		return this.orders;
	}

	public void setOrders(final List<Order> orders)
	{
		this.orders = orders;
	}

	public Order addOrder(final Order order)
	{
		getOrders().add(order);
		order.setEncounter(this);

		return order;
	}

	public Order removeOrder(final Order order)
	{
		getOrders().remove(order);
		order.setEncounter(null);

		return order;
	}

}