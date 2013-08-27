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
package edu.utah.further.ds.further.model.impl.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * Persistent entity implementation of {@link Encounter}
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
 * @version Sep 2, 2009
 */
@Entity
@Implementation
@Table(name = "FENCOUNTER")
@XmlRootElement(name = "Encounter")
@XmlAccessorType(XmlAccessType.FIELD)
public class Encounter implements PersistentEntity<EncounterId>
{
	// ========================= CONSTANTS ===================================

	@Transient
	private static final long serialVersionUID = -8260450744267742380L;

	// ========================= FIELDS ===================================

	@EmbeddedId
	private EncounterId id;

	@Column(name = "fperson_id")
	private Long personId;

	@Column(name = "FPERSON_COMPOSITE_ID")
	private String personCompositeId;

	@Column(name = "admission_dts")
	private Date admissionDate;

	@Column(name = "admission_yr")
	private Long admissionYear;

	@Column(name = "admission_mon")
	private Long admissionMonth;

	@Column(name = "admission_day")
	private Long admissionDay;

	@Column(name = "discharge_dts")
	private Date dischargeDate;

	@Column(name = "discharge_yr")
	private Long dischargeYear;

	@Column(name = "discharge_mon")
	private Long dischargeMonth;

	@Column(name = "discharge_day")
	private Long dischargeDay;

	@Column(name = "length_of_stay")
	private Long lengthOfStay;

	@Column(name = "length_of_stay_units_nmspc_id")
	private Long lengthOfStayUnitsNamespaceId;

	@Column(name = "length_of_stay_units_cid")
	private String lengthOfStayUnits;

	@Column(name = "admit_source_nmspc_id")
	private Long admitSourceNamespaceId;

	@Column(name = "admit_source_cid")
	private String admitSource;

	@Column(name = "admit_type_nmspc_id")
	private Long admitTypeNamespaceId;

	@Column(name = "admit_type_cid")
	private String admitType;

	@Column(name = "patient_class_nmspc_id")
	private Long patientClassNamespaceId;

	@Column(name = "patient_class_cid")
	private String patientClass;

	@Column(name = "patient_type_nmspc_id")
	private Long patientTypeNamespaceId;

	@Column(name = "patient_type_cid")
	private String patientType;

	@Column(name = "hospital_service_nmspc_id")
	private Long hospitalServiceNamespaceId;

	@Column(name = "hospital_service_cid")
	private String hospitalService;

	@OneToMany(targetEntity = Order.class, fetch = FetchType.LAZY)
	@JoinColumns(
	{
			@JoinColumn(name = "fencounter_id", referencedColumnName = "fencounter_id", insertable = false, updatable = false, nullable = true),
			@JoinColumn(name = "dataset_id", referencedColumnName = "dataset_id", insertable = false, updatable = false, nullable = true) })
	@XmlTransient
	private Collection<Order> orders;

	@OneToMany(targetEntity = Observation.class, fetch = FetchType.LAZY)
	@JoinColumns(
	{
			@JoinColumn(name = "fencounter_id", referencedColumnName = "fencounter_id", insertable = false, updatable = false, nullable = true),
			@JoinColumn(name = "dataset_id", referencedColumnName = "dataset_id", insertable = false, updatable = false, nullable = true) })
	@XmlTransient
	private Collection<Observation> observations;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public EncounterId getId()
	{
		return id;
	}

	/**
	 * Return the personId property.
	 * 
	 * @return the personId
	 */
	public Long getPersonId()
	{
		return personId;
	}

	/**
	 * Set a new value for the personId property.
	 * 
	 * @param personId
	 *            the personId to set
	 */
	public void setPersonId(Long personId)
	{
		this.personId = personId;
	}

	/**
	 * Return the personCompositeId property.
	 * 
	 * @return the personCompositeId
	 */
	public String getPersonCompositeId()
	{
		return personCompositeId;
	}

	/**
	 * Set a new value for the personCompositeId property.
	 * 
	 * @param personCompositeId
	 *            the personCompositeId to set
	 */
	public void setPersonCompositeId(String personCompositeId)
	{
		this.personCompositeId = personCompositeId;
	}

	/**
	 * Return the admissionDate property.
	 * 
	 * @return the admissionDate
	 */
	public Date getAdmissionDate()
	{
		return admissionDate;
	}

	/**
	 * Set a new value for the admissionDate property.
	 * 
	 * @param admissionDate
	 *            the admissionDate to set
	 */
	public void setAdmissionDate(Date admissionDate)
	{
		this.admissionDate = admissionDate;
	}

	/**
	 * Return the admissionYear property.
	 * 
	 * @return the admissionYear
	 */
	public Long getAdmissionYear()
	{
		return admissionYear;
	}

	/**
	 * Set a new value for the admissionYear property.
	 * 
	 * @param admissionYear
	 *            the admissionYear to set
	 */
	public void setAdmissionYear(Long admissionYear)
	{
		this.admissionYear = admissionYear;
	}

	/**
	 * Return the admissionMonth property.
	 * 
	 * @return the admissionMonth
	 */
	public Long getAdmissionMonth()
	{
		return admissionMonth;
	}

	/**
	 * Set a new value for the admissionMonth property.
	 * 
	 * @param admissionMonth
	 *            the admissionMonth to set
	 */
	public void setAdmissionMonth(Long admissionMonth)
	{
		this.admissionMonth = admissionMonth;
	}

	/**
	 * Return the admissionDay property.
	 * 
	 * @return the admissionDay
	 */
	public Long getAdmissionDay()
	{
		return admissionDay;
	}

	/**
	 * Set a new value for the admissionDay property.
	 * 
	 * @param admissionDay
	 *            the admissionDay to set
	 */
	public void setAdmissionDay(Long admissionDay)
	{
		this.admissionDay = admissionDay;
	}

	/**
	 * Return the dischargeDate property.
	 * 
	 * @return the dischargeDate
	 */
	public Date getDischargeDate()
	{
		return dischargeDate;
	}

	/**
	 * Set a new value for the dischargeDate property.
	 * 
	 * @param dischargeDate
	 *            the dischargeDate to set
	 */
	public void setDischargeDate(Date dischargeDate)
	{
		this.dischargeDate = dischargeDate;
	}

	/**
	 * Return the dischargeYear property.
	 * 
	 * @return the dischargeYear
	 */
	public Long getDischargeYear()
	{
		return dischargeYear;
	}

	/**
	 * Set a new value for the dischargeYear property.
	 * 
	 * @param dischargeYear
	 *            the dischargeYear to set
	 */
	public void setDischargeYear(Long dischargeYear)
	{
		this.dischargeYear = dischargeYear;
	}

	/**
	 * Return the dischargeMonth property.
	 * 
	 * @return the dischargeMonth
	 */
	public Long getDischargeMonth()
	{
		return dischargeMonth;
	}

	/**
	 * Set a new value for the dischargeMonth property.
	 * 
	 * @param dischargeMonth
	 *            the dischargeMonth to set
	 */
	public void setDischargeMonth(Long dischargeMonth)
	{
		this.dischargeMonth = dischargeMonth;
	}

	/**
	 * Return the dischargeDay property.
	 * 
	 * @return the dischargeDay
	 */
	public Long getDischargeDay()
	{
		return dischargeDay;
	}

	/**
	 * Set a new value for the dischargeDay property.
	 * 
	 * @param dischargeDay
	 *            the dischargeDay to set
	 */
	public void setDischargeDay(Long dischargeDay)
	{
		this.dischargeDay = dischargeDay;
	}

	/**
	 * Return the lengthOfStay property.
	 * 
	 * @return the lengthOfStay
	 */
	public Long getLengthOfStay()
	{
		return lengthOfStay;
	}

	/**
	 * Set a new value for the lengthOfStay property.
	 * 
	 * @param lengthOfStay
	 *            the lengthOfStay to set
	 */
	public void setLengthOfStay(Long lengthOfStay)
	{
		this.lengthOfStay = lengthOfStay;
	}

	/**
	 * Return the lengthOfStayUnitsNamespaceId property.
	 * 
	 * @return the lengthOfStayUnitsNamespaceId
	 */
	public Long getLengthOfStayUnitsNamespaceId()
	{
		return lengthOfStayUnitsNamespaceId;
	}

	/**
	 * Set a new value for the lengthOfStayUnitsNamespaceId property.
	 * 
	 * @param lengthOfStayUnitsNamespaceId
	 *            the lengthOfStayUnitsNamespaceId to set
	 */
	public void setLengthOfStayUnitsNamespaceId(Long lengthOfStayUnitsNamespaceId)
	{
		this.lengthOfStayUnitsNamespaceId = lengthOfStayUnitsNamespaceId;
	}

	/**
	 * Return the lengthOfStayUnits property.
	 * 
	 * @return the lengthOfStayUnits
	 */
	public String getLengthOfStayUnits()
	{
		return lengthOfStayUnits;
	}

	/**
	 * Set a new value for the lengthOfStayUnits property.
	 * 
	 * @param lengthOfStayUnits
	 *            the lengthOfStayUnits to set
	 */
	public void setLengthOfStayUnits(String lengthOfStayUnits)
	{
		this.lengthOfStayUnits = lengthOfStayUnits;
	}

	/**
	 * Return the admitSourceNamespaceId property.
	 * 
	 * @return the admitSourceNamespaceId
	 */
	public Long getAdmitSourceNamespaceId()
	{
		return admitSourceNamespaceId;
	}

	/**
	 * Set a new value for the admitSourceNamespaceId property.
	 * 
	 * @param admitSourceNamespaceId
	 *            the admitSourceNamespaceId to set
	 */
	public void setAdmitSourceNamespaceId(Long admitSourceNamespaceId)
	{
		this.admitSourceNamespaceId = admitSourceNamespaceId;
	}

	/**
	 * Return the admitSource property.
	 * 
	 * @return the admitSource
	 */
	public String getAdmitSource()
	{
		return admitSource;
	}

	/**
	 * Set a new value for the admitSource property.
	 * 
	 * @param admitSource
	 *            the admitSource to set
	 */
	public void setAdmitSource(String admitSource)
	{
		this.admitSource = admitSource;
	}

	/**
	 * Return the admitTypeNamespaceId property.
	 * 
	 * @return the admitTypeNamespaceId
	 */
	public Long getAdmitTypeNamespaceId()
	{
		return admitTypeNamespaceId;
	}

	/**
	 * Set a new value for the admitTypeNamespaceId property.
	 * 
	 * @param admitTypeNamespaceId
	 *            the admitTypeNamespaceId to set
	 */
	public void setAdmitTypeNamespaceId(Long admitTypeNamespaceId)
	{
		this.admitTypeNamespaceId = admitTypeNamespaceId;
	}

	/**
	 * Return the admitType property.
	 * 
	 * @return the admitType
	 */
	public String getAdmitType()
	{
		return admitType;
	}

	/**
	 * Set a new value for the admitType property.
	 * 
	 * @param admitType
	 *            the admitType to set
	 */
	public void setAdmitType(String admitType)
	{
		this.admitType = admitType;
	}

	/**
	 * Return the patientClassNamespaceId property.
	 * 
	 * @return the patientClassNamespaceId
	 */
	public Long getPatientClassNamespaceId()
	{
		return patientClassNamespaceId;
	}

	/**
	 * Set a new value for the patientClassNamespaceId property.
	 * 
	 * @param patientClassNamespaceId
	 *            the patientClassNamespaceId to set
	 */
	public void setPatientClassNamespaceId(Long patientClassNamespaceId)
	{
		this.patientClassNamespaceId = patientClassNamespaceId;
	}

	/**
	 * Return the patientClass property.
	 * 
	 * @return the patientClass
	 */
	public String getPatientClass()
	{
		return patientClass;
	}

	/**
	 * Set a new value for the patientClass property.
	 * 
	 * @param patientClass
	 *            the patientClass to set
	 */
	public void setPatientClass(String patientClass)
	{
		this.patientClass = patientClass;
	}

	/**
	 * Return the patientTypeNamespaceId property.
	 * 
	 * @return the patientTypeNamespaceId
	 */
	public Long getPatientTypeNamespaceId()
	{
		return patientTypeNamespaceId;
	}

	/**
	 * Set a new value for the patientTypeNamespaceId property.
	 * 
	 * @param patientTypeNamespaceId
	 *            the patientTypeNamespaceId to set
	 */
	public void setPatientTypeNamespaceId(Long patientTypeNamespaceId)
	{
		this.patientTypeNamespaceId = patientTypeNamespaceId;
	}

	/**
	 * Return the patientType property.
	 * 
	 * @return the patientType
	 */
	public String getPatientType()
	{
		return patientType;
	}

	/**
	 * Set a new value for the patientType property.
	 * 
	 * @param patientType
	 *            the patientType to set
	 */
	public void setPatientType(String patientType)
	{
		this.patientType = patientType;
	}

	/**
	 * Return the hospitalServiceNamespaceId property.
	 * 
	 * @return the hospitalServiceNamespaceId
	 */
	public Long getHospitalServiceNamespaceId()
	{
		return hospitalServiceNamespaceId;
	}

	/**
	 * Set a new value for the hospitalServiceNamespaceId property.
	 * 
	 * @param hospitalServiceNamespaceId
	 *            the hospitalServiceNamespaceId to set
	 */
	public void setHospitalServiceNamespaceId(Long hospitalServiceNamespaceId)
	{
		this.hospitalServiceNamespaceId = hospitalServiceNamespaceId;
	}

	/**
	 * Return the hospitalService property.
	 * 
	 * @return the hospitalService
	 */
	public String getHospitalService()
	{
		return hospitalService;
	}

	/**
	 * Set a new value for the hospitalService property.
	 * 
	 * @param hospitalService
	 *            the hospitalService to set
	 */
	public void setHospitalService(String hospitalService)
	{
		this.hospitalService = hospitalService;
	}

	/**
	 * Return the orders property.
	 * 
	 * @return the orders
	 */
	public Collection<Order> getOrders()
	{
		return orders;
	}

	/**
	 * Set a new value for the orders property.
	 * 
	 * @param orders
	 *            the orders to set
	 */
	public void setOrders(Collection<Order> orders)
	{
		this.orders = orders;
	}

	/**
	 * Return the observations property.
	 * 
	 * @return the observations
	 */
	public Collection<Observation> getObservations()
	{
		return observations;
	}

	/**
	 * Set a new value for the observations property.
	 * 
	 * @param observations
	 *            the observations to set
	 */
	public void setObservations(Collection<Observation> observations)
	{
		this.observations = observations;
	}

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(EncounterId id)
	{
		this.id = id;
	}

	// ====================== IMPLEMENTATION: Object =====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;

		final Encounter that = (Encounter) obj;
		return new EqualsBuilder().append(getId(), that.getId()).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toStringExclude(this, new String[]
		{ "orders", "observations" });
	}

}
