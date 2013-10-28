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
package edu.utah.further.ds.openmrs.model.v1_9.domain;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.xml.jaxb.adapter.BooleanIntegerAdapter;

/**
 * The persistent class and data transfer object for the drug_order database table.
 * 
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Sep 3, 2013
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

	@XmlJavaTypeAdapter(BooleanIntegerAdapter.class)
	private Boolean complex;

	private Double dose;

	@Column(name = "drug_inventory_id")
	private Long drugInventoryId;

	@Column(name = "equivalent_daily_dose")
	private Double equivalentDailyDose;

	private String frequency;

	@XmlJavaTypeAdapter(BooleanIntegerAdapter.class)
	private Boolean prn;

	private Long quantity;

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

	public Boolean getComplex()
	{
		return this.complex;
	}

	public void setComplex(final Boolean complex)
	{
		this.complex = complex;
	}

	public Double getDose()
	{
		return this.dose;
	}

	public void setDose(final Double dose)
	{
		this.dose = dose;
	}

	public Long getDrugInventoryId()
	{
		return this.drugInventoryId;
	}

	public void setDrugInventoryId(final Long drugInventoryId)
	{
		this.drugInventoryId = drugInventoryId;
	}

	public Double getEquivalentDailyDose()
	{
		return this.equivalentDailyDose;
	}

	public void setEquivalentDailyDose(final Double equivalentDailyDose)
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

	public Boolean getPrn()
	{
		return this.prn;
	}

	public void setPrn(final Boolean prn)
	{
		this.prn = prn;
	}

	public Long getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(final Long quantity)
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