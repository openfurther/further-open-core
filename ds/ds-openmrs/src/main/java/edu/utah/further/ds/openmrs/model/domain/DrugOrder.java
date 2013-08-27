package edu.utah.further.ds.openmrs.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class and data transfer object for the drug_order database table.
 * 
 */
@Entity
@Table(name = "drug_order")
@XmlRootElement(name = "DrugOrder")
@XmlAccessorType(XmlAccessType.FIELD)
public class DrugOrder implements PersistentEntity<Integer>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "order_id")
	private Integer orderId;

	private byte complex;

	private double dose;

	@Column(name = "drug_inventory_id")
	private int drugInventoryId;

	@Column(name = "equivalent_daily_dose")
	private double equivalentDailyDose;

	private String frequency;

	private byte prn;

	private int quantity;

	private String units;

	// bi-directional one-to-one association to Order
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	@XmlTransient
	private Order order;

	public DrugOrder()
	{
	}

	@Override
	public Integer getId()
	{
		return this.orderId;
	}

	public void setId(final Integer orderId)
	{
		this.orderId = orderId;
	}

	public byte getComplex()
	{
		return this.complex;
	}

	public void setComplex(final byte complex)
	{
		this.complex = complex;
	}

	public double getDose()
	{
		return this.dose;
	}

	public void setDose(final double dose)
	{
		this.dose = dose;
	}

	public int getDrugInventoryId()
	{
		return this.drugInventoryId;
	}

	public void setDrugInventoryId(final int drugInventoryId)
	{
		this.drugInventoryId = drugInventoryId;
	}

	public double getEquivalentDailyDose()
	{
		return this.equivalentDailyDose;
	}

	public void setEquivalentDailyDose(final double equivalentDailyDose)
	{
		this.equivalentDailyDose = equivalentDailyDose;
	}

	public String getFrequency()
	{
		return this.frequency;
	}

	public void setFrequency(final String frequency)
	{
		this.frequency = frequency;
	}

	public byte getPrn()
	{
		return this.prn;
	}

	public void setPrn(final byte prn)
	{
		this.prn = prn;
	}

	public int getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(final int quantity)
	{
		this.quantity = quantity;
	}

	public String getUnits()
	{
		return this.units;
	}

	public void setUnits(final String units)
	{
		this.units = units;
	}

	public Order getOrder()
	{
		return this.order;
	}

	public void setOrder(final Order order)
	{
		this.order = order;
	}

}