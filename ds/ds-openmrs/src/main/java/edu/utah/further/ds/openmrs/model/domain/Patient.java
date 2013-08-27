package edu.utah.further.ds.openmrs.model.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class and data transfer object for the patient database table.
 * 
 */
@Entity
@XmlRootElement(name = "Patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class Patient implements PersistentEntity<Integer>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "patient_id")
	private Integer patientId;

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

	private int tribe;

	@Column(name = "void_reason")
	private String voidReason;

	private byte voided;

	@Column(name = "voided_by")
	private int voidedBy;

	// bi-directional many-to-one association to Encounter
	@OneToMany(mappedBy = "patient")
	@XmlTransient
	private List<Encounter> encounters;

	// bi-directional many-to-one association to Order
	@OneToMany(mappedBy = "patient")
	@XmlTransient
	private List<Order> orders;

	// bi-directional one-to-one association to Person
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	@XmlElement(name="Person")
	private Person person;

	public Patient()
	{
	}

	@Override
	public Integer getId()
	{
		return this.patientId;
	}

	public void setId(final Integer patientId)
	{
		this.patientId = patientId;
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

	public int getTribe()
	{
		return this.tribe;
	}

	public void setTribe(final int tribe)
	{
		this.tribe = tribe;
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

	public List<Encounter> getEncounters()
	{
		return this.encounters;
	}

	public void setEncounters(final List<Encounter> encounters)
	{
		this.encounters = encounters;
	}

	public Encounter addEncounter(final Encounter encounter)
	{
		getEncounters().add(encounter);
		encounter.setPatient(this);

		return encounter;
	}

	public Encounter removeEncounter(final Encounter encounter)
	{
		getEncounters().remove(encounter);
		encounter.setPatient(null);

		return encounter;
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
		order.setPatient(this);

		return order;
	}

	public Order removeOrder(final Order order)
	{
		getOrders().remove(order);
		order.setPatient(null);

		return order;
	}

	public Person getPerson()
	{
		return this.person;
	}

	public void setPerson(final Person person)
	{
		this.person = person;
	}

}