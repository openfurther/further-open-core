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
 * The persistent class for the DRUG_EXPOSURE_REF database table.
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
@Table(name = "DRUG_EXPOSURE_REF")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DrugExposureRef implements PersistentEntity<String>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DRUG_EXPOSURE_TYPE")
	private String drugExposureType;

	@Column(name = "DRUG_EXPOSURE_TYPE_DESC")
	private String drugExposureTypeDesc;

	@Column(name = "PERSISTENCE_WINDOW")
	private BigDecimal persistenceWindow;

	// bi-directional many-to-one association to DrugEra
	@OneToMany(mappedBy = "drugExposureRef")
	@XmlTransient
	private List<DrugEra> drugEras;

	// bi-directional many-to-one association to DrugExposure
	@OneToMany(mappedBy = "drugExposureRef")
	@XmlTransient
	private List<DrugExposure> drugExposures;

	public DrugExposureRef()
	{
	}

	@Override
	public String getId()
	{
		return this.drugExposureType;
	}

	protected void setId(final String drugExposureType)
	{
		this.drugExposureType = drugExposureType;
	}

	public String getDrugExposureTypeDesc()
	{
		return this.drugExposureTypeDesc;
	}

	public void setDrugExposureTypeDesc(final String drugExposureTypeDesc)
	{
		this.drugExposureTypeDesc = drugExposureTypeDesc;
	}

	public BigDecimal getPersistenceWindow()
	{
		return this.persistenceWindow;
	}

	public void setPersistenceWindow(final BigDecimal persistenceWindow)
	{
		this.persistenceWindow = persistenceWindow;
	}

	public List<DrugEra> getDrugEras()
	{
		return this.drugEras;
	}

	public void setDrugEras(final List<DrugEra> drugEras)
	{
		this.drugEras = drugEras;
	}

	public DrugEra addDrugEra(final DrugEra drugEra)
	{
		getDrugEras().add(drugEra);
		drugEra.setDrugExposureRef(this);

		return drugEra;
	}

	public DrugEra removeDrugEra(final DrugEra drugEra)
	{
		getDrugEras().remove(drugEra);
		drugEra.setDrugExposureRef(null);

		return drugEra;
	}

	public List<DrugExposure> getDrugExposures()
	{
		return this.drugExposures;
	}

	public void setDrugExposures(final List<DrugExposure> drugExposures)
	{
		this.drugExposures = drugExposures;
	}

	public DrugExposure addDrugExposure(final DrugExposure drugExposure)
	{
		getDrugExposures().add(drugExposure);
		drugExposure.setDrugExposureRef(this);

		return drugExposure;
	}

	public DrugExposure removeDrugExposure(final DrugExposure drugExposure)
	{
		getDrugExposures().remove(drugExposure);
		drugExposure.setDrugExposureRef(null);

		return drugExposure;
	}

}