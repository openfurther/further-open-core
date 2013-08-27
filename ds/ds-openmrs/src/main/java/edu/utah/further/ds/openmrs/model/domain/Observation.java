package edu.utah.further.ds.openmrs.model.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class and data transfer object for the obs database table.
 * 
 */
@Entity
@Table(name = "obs")
@XmlRootElement(name = "Observation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Observation implements PersistentEntity<Integer>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "obs_id")
	private Integer obsId;

	@Column(name = "accession_number")
	private String accessionNumber;

	private String comments;

	@Column(name = "concept_id")
	private int conceptId;

	private int creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_voided")
	private Date dateVoided;

	@Column(name = "location_id")
	private int locationId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "obs_datetime")
	private Date obsDatetime;

	private String uuid;

	@Column(name = "value_boolean")
	private byte valueBoolean;

	@Column(name = "value_coded")
	private int valueCoded;

	@Column(name = "value_coded_name_id")
	private int valueCodedNameId;

	@Column(name = "value_complex")
	private String valueComplex;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "value_datetime")
	private Date valueDatetime;

	@Column(name = "value_drug")
	private int valueDrug;

	@Column(name = "value_group_id")
	private int valueGroupId;

	@Column(name = "value_modifier")
	private String valueModifier;

	@Column(name = "value_numeric")
	private double valueNumeric;

	@Lob
	@Column(name = "value_text")
	private String valueText;

	@Column(name = "void_reason")
	private String voidReason;

	private byte voided;

	@Column(name = "voided_by")
	private int voidedBy;

	// bi-directional many-to-one association to Encounter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id")
	@XmlTransient
	private Encounter encounter;

	// bi-directional many-to-one association to Order
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	@XmlTransient
	private Order order;

	// bi-directional many-to-one association to Person
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	@XmlTransient
	private Person person;

	public Observation()
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
		return this.obsId;
	}

	public void setId(final Integer obsId)
	{
		this.obsId = obsId;
	}

	public String getAccessionNumber()
	{
		return this.accessionNumber;
	}

	public void setAccessionNumber(final String accessionNumber)
	{
		this.accessionNumber = accessionNumber;
	}

	public String getComments()
	{
		return this.comments;
	}

	public void setComments(final String comments)
	{
		this.comments = comments;
	}

	public int getConceptId()
	{
		return this.conceptId;
	}

	public void setConceptId(final int conceptId)
	{
		this.conceptId = conceptId;
	}

	public int getCreator()
	{
		return this.creator;
	}

	public void setCreator(final int creator)
	{
		this.creator = creator;
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

	public int getLocationId()
	{
		return this.locationId;
	}

	public void setLocationId(final int locationId)
	{
		this.locationId = locationId;
	}

	public Date getObsDatetime()
	{
		return this.obsDatetime;
	}

	public void setObsDatetime(final Date obsDatetime)
	{
		this.obsDatetime = obsDatetime;
	}

	public String getUuid()
	{
		return this.uuid;
	}

	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
	}

	public byte getValueBoolean()
	{
		return this.valueBoolean;
	}

	public void setValueBoolean(final byte valueBoolean)
	{
		this.valueBoolean = valueBoolean;
	}

	public int getValueCoded()
	{
		return this.valueCoded;
	}

	public void setValueCoded(final int valueCoded)
	{
		this.valueCoded = valueCoded;
	}

	public int getValueCodedNameId()
	{
		return this.valueCodedNameId;
	}

	public void setValueCodedNameId(final int valueCodedNameId)
	{
		this.valueCodedNameId = valueCodedNameId;
	}

	public String getValueComplex()
	{
		return this.valueComplex;
	}

	public void setValueComplex(final String valueComplex)
	{
		this.valueComplex = valueComplex;
	}

	public Date getValueDatetime()
	{
		return this.valueDatetime;
	}

	public void setValueDatetime(final Date valueDatetime)
	{
		this.valueDatetime = valueDatetime;
	}

	public int getValueDrug()
	{
		return this.valueDrug;
	}

	public void setValueDrug(final int valueDrug)
	{
		this.valueDrug = valueDrug;
	}

	public int getValueGroupId()
	{
		return this.valueGroupId;
	}

	public void setValueGroupId(final int valueGroupId)
	{
		this.valueGroupId = valueGroupId;
	}

	public String getValueModifier()
	{
		return this.valueModifier;
	}

	public void setValueModifier(final String valueModifier)
	{
		this.valueModifier = valueModifier;
	}

	public double getValueNumeric()
	{
		return this.valueNumeric;
	}

	public void setValueNumeric(final double valueNumeric)
	{
		this.valueNumeric = valueNumeric;
	}

	public String getValueText()
	{
		return this.valueText;
	}

	public void setValueText(final String valueText)
	{
		this.valueText = valueText;
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

	public Encounter getEncounter()
	{
		return this.encounter;
	}

	public void setEncounter(final Encounter encounter)
	{
		this.encounter = encounter;
	}

	public Order getOrder()
	{
		return this.order;
	}

	public void setOrder(final Order order)
	{
		this.order = order;
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