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
package edu.utah.further.ds.omop.model.v4.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class for the DRUG_COST database table.
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
 * @version Apr 17, 2013
 */
@Entity
@Table(name = "DRUG_COST")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DrugCost implements PersistentEntity<Long>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DRUG_COST_ID", unique = true, nullable = false)
	private Long drugCostId;

	@Column(name = "AVERAGE_WHOLESALE_PRICE", precision = 8, scale = 2)
	private BigDecimal averageWholesalePrice;

	@Column(name = "DISPENSING_FEE", precision = 8, scale = 2)
	private BigDecimal dispensingFee;

	@Column(name = "INGREDIENT_COST", precision = 8, scale = 2)
	private BigDecimal ingredientCost;

	@Column(name = "PAID_BY_COORDINATION_BENEFITS", precision = 8, scale = 2)
	private BigDecimal paidByCoordinationBenefits;

	@Column(name = "PAID_BY_PAYER", precision = 8, scale = 2)
	private BigDecimal paidByPayer;

	@Column(name = "PAID_COINSURANCE", precision = 8, scale = 2)
	private BigDecimal paidCoinsurance;

	@Column(name = "PAID_COPAY", precision = 8, scale = 2)
	private BigDecimal paidCopay;

	@Column(name = "PAID_TOWARD_DEDUCTIBLE", precision = 8, scale = 2)
	private BigDecimal paidTowardDeductible;

	@Column(name = "PAYER_PLAN_PERIOD_ID")
	private BigDecimal payerPlanPeriodId;

	@Column(name = "TOTAL_OUT_OF_POCKET", precision = 8, scale = 2)
	private BigDecimal totalOutOfPocket;

	@Column(name = "TOTAL_PAID", precision = 8, scale = 2)
	private BigDecimal totalPaid;

	// bi-directional many-to-one association to DrugExposureEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DRUG_EXPOSURE_ID", nullable = false)
	private DrugExposure drugExposure;

	/**
	 * Default constructor
	 */
	public DrugCost()
	{
	}

	@Override
	public Long getId()
	{
		return this.drugCostId;
	}

	public BigDecimal getAverageWholesalePrice()
	{
		return this.averageWholesalePrice;
	}

	public void setAverageWholesalePrice(final BigDecimal averageWholesalePrice)
	{
		this.averageWholesalePrice = averageWholesalePrice;
	}

	public BigDecimal getDispensingFee()
	{
		return this.dispensingFee;
	}

	public void setDispensingFee(final BigDecimal dispensingFee)
	{
		this.dispensingFee = dispensingFee;
	}

	public BigDecimal getIngredientCost()
	{
		return this.ingredientCost;
	}

	public void setIngredientCost(final BigDecimal ingredientCost)
	{
		this.ingredientCost = ingredientCost;
	}

	public BigDecimal getPaidByCoordinationBenefits()
	{
		return this.paidByCoordinationBenefits;
	}

	public void setPaidByCoordinationBenefits(final BigDecimal paidByCoordinationBenefits)
	{
		this.paidByCoordinationBenefits = paidByCoordinationBenefits;
	}

	public BigDecimal getPaidByPayer()
	{
		return this.paidByPayer;
	}

	public void setPaidByPayer(final BigDecimal paidByPayer)
	{
		this.paidByPayer = paidByPayer;
	}

	public BigDecimal getPaidCoinsurance()
	{
		return this.paidCoinsurance;
	}

	public void setPaidCoinsurance(final BigDecimal paidCoinsurance)
	{
		this.paidCoinsurance = paidCoinsurance;
	}

	public BigDecimal getPaidCopay()
	{
		return this.paidCopay;
	}

	public void setPaidCopay(final BigDecimal paidCopay)
	{
		this.paidCopay = paidCopay;
	}

	public BigDecimal getPaidTowardDeductible()
	{
		return this.paidTowardDeductible;
	}

	public void setPaidTowardDeductible(final BigDecimal paidTowardDeductible)
	{
		this.paidTowardDeductible = paidTowardDeductible;
	}

	public BigDecimal getPayerPlanPeriodId()
	{
		return this.payerPlanPeriodId;
	}

	public void setPayerPlanPeriodId(final BigDecimal payerPlanPeriodId)
	{
		this.payerPlanPeriodId = payerPlanPeriodId;
	}

	public BigDecimal getTotalOutOfPocket()
	{
		return this.totalOutOfPocket;
	}

	public void setTotalOutOfPocket(final BigDecimal totalOutOfPocket)
	{
		this.totalOutOfPocket = totalOutOfPocket;
	}

	public BigDecimal getTotalPaid()
	{
		return this.totalPaid;
	}

	public void setTotalPaid(final BigDecimal totalPaid)
	{
		this.totalPaid = totalPaid;
	}

	public DrugExposure getDrugExposure()
	{
		return this.drugExposure;
	}

	public void setDrugExposure(final DrugExposure drugExposure)
	{
		this.drugExposure = drugExposure;
	}

}