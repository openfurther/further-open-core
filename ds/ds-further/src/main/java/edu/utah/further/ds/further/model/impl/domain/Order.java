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
 * Persistent entity implementation of {@link Order}
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
@Table(name = "FORDER_FACT")
@XmlRootElement(name = "Order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements PersistentEntity<OrderId>
{
	// ========================= CONSTANTS ===================================

	@Transient
	private static final long serialVersionUID = -8495069353945562251L;

	// ========================= FIELDS ===================================

	@EmbeddedId
	private OrderId id;

	@Column(name = "fperson_id")
	private Long personId;

	@Column(name = "FPERSON_COMPOSITE_ID")
	private String personCompositeId;

	@Column(name = "fencounter_id")
	private Long encounterId;

	@Column(name = "fprovider_id")
	private Long providerId;

	@Column(name = "priority_nmspc_id")
	private Long priorityNamespaceId;

	@Column(name = "priority_cid")
	private String priority;

	@Column(name = "order_dts")
	private Date dateTime;

	@Column(name = "discontinue_dts")
	private Date discontinueDateTime;

	@Column(name = "order_status_nmspc_id")
	private Long statusNamespaceId;

	@Column(name = "order_status_cid")
	private String status;

	@Column(name = "order_type_nmspc_id")
	private Long typeNamespaceId;

	@Column(name = "order_type_cid")
	private String type;

	@Column(name = "start_dts")
	private Date startDate;

	@Column(name = "stop_dts")
	private Date stopDate;

	@Column(name = "order_item_nmspc_id")
	private Long orderItemNamespaceId;

	@Column(name = "order_item_cid")
	private String orderItem;

	@Column(name = "order_item_qty")
	private Long doseQuantity;

	@Column(name = "order_item_qty_units_nmspc_id")
	private Long doseQuantityUnitOfMeasureNamespaceId;

	@Column(name = "order_item_qty_units_cid")
	private String doseQuantityUnitOfMeasure;

	@Column(name = "order_item_form_nmspc_id")
	private Long doseFormNamespaceId;

	@Column(name = "order_item_form_cid")
	private String doseForm;

	@Column(name = "route_nmspc_id")
	private Long routeNamespaceId;

	@Column(name = "route_cid")
	private String route;

	@Column(name = "specimen_type_nmspc_id")
	private Long specimenTypeNamespaceId;

	@Column(name = "specimen_type_cid")
	private String specimenType;

	@Column(name = "specimen_source_nmspc_id")
	private Long specimenSourceNamespaceId;

	@Column(name = "specimen_source_cid")
	private String specimenSource;

	@Column(name = "duration")
	private Long intendedDuration;

	@Column(name = "duration_units_nmspc_id")
	private Long durationUnitOfTimeNamespaceId;

	@Column(name = "duration_units_cid")
	private String durationUnitOfTime;

	@Column(name = "fspecimen_id")
	private Long specimenId;

	@Column(name = "dummy_null")
	private Long dummyNull;

	@OneToMany(targetEntity = Observation.class, fetch = FetchType.LAZY)
	@JoinColumns(
	{
			@JoinColumn(name = "forder_id", referencedColumnName = "forder_id", insertable = false, updatable = false, nullable = true),
			@JoinColumn(name = "dataset_id", referencedColumnName = "dataset_id", insertable = false, updatable = false, nullable = true) })
	@XmlTransient
	private Collection<Observation> observations;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public OrderId getId()
	{
		return id;
	}

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(OrderId id)
	{
		this.id = id;
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
	 * Return the encounterId property.
	 * 
	 * @return the encounterId
	 */
	public Long getEncounterId()
	{
		return encounterId;
	}

	/**
	 * Set a new value for the encounterId property.
	 * 
	 * @param encounterId
	 *            the encounterId to set
	 */
	public void setEncounterId(Long encounterId)
	{
		this.encounterId = encounterId;
	}

	/**
	 * Return the providerId property.
	 * 
	 * @return the providerId
	 */
	public Long getProviderId()
	{
		return providerId;
	}

	/**
	 * Set a new value for the providerId property.
	 * 
	 * @param providerId
	 *            the providerId to set
	 */
	public void setProviderId(Long providerId)
	{
		this.providerId = providerId;
	}

	/**
	 * Return the priorityNamespaceId property.
	 * 
	 * @return the priorityNamespaceId
	 */
	public Long getPriorityNamespaceId()
	{
		return priorityNamespaceId;
	}

	/**
	 * Set a new value for the priorityNamespaceId property.
	 * 
	 * @param priorityNamespaceId
	 *            the priorityNamespaceId to set
	 */
	public void setPriorityNamespaceId(Long priorityNamespaceId)
	{
		this.priorityNamespaceId = priorityNamespaceId;
	}

	/**
	 * Return the priority property.
	 * 
	 * @return the priority
	 */
	public String getPriority()
	{
		return priority;
	}

	/**
	 * Set a new value for the priority property.
	 * 
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(String priority)
	{
		this.priority = priority;
	}

	/**
	 * Return the dateTime property.
	 * 
	 * @return the dateTime
	 */
	public Date getDateTime()
	{
		return dateTime;
	}

	/**
	 * Set a new value for the dateTime property.
	 * 
	 * @param dateTime
	 *            the dateTime to set
	 */
	public void setDateTime(Date dateTime)
	{
		this.dateTime = dateTime;
	}

	/**
	 * Return the discontinueDateTime property.
	 * 
	 * @return the discontinueDateTime
	 */
	public Date getDiscontinueDateTime()
	{
		return discontinueDateTime;
	}

	/**
	 * Set a new value for the discontinueDateTime property.
	 * 
	 * @param discontinueDateTime
	 *            the discontinueDateTime to set
	 */
	public void setDiscontinueDateTime(Date discontinueDateTime)
	{
		this.discontinueDateTime = discontinueDateTime;
	}

	/**
	 * Return the statusNamespaceId property.
	 * 
	 * @return the statusNamespaceId
	 */
	public Long getStatusNamespaceId()
	{
		return statusNamespaceId;
	}

	/**
	 * Set a new value for the statusNamespaceId property.
	 * 
	 * @param statusNamespaceId
	 *            the statusNamespaceId to set
	 */
	public void setStatusNamespaceId(Long statusNamespaceId)
	{
		this.statusNamespaceId = statusNamespaceId;
	}

	/**
	 * Return the status property.
	 * 
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Set a new value for the status property.
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * Return the typeNamespaceId property.
	 * 
	 * @return the typeNamespaceId
	 */
	public Long getTypeNamespaceId()
	{
		return typeNamespaceId;
	}

	/**
	 * Set a new value for the typeNamespaceId property.
	 * 
	 * @param typeNamespaceId
	 *            the typeNamespaceId to set
	 */
	public void setTypeNamespaceId(Long typeNamespaceId)
	{
		this.typeNamespaceId = typeNamespaceId;
	}

	/**
	 * Return the type property.
	 * 
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Set a new value for the type property.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Return the startDate property.
	 * 
	 * @return the startDate
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * Set a new value for the startDate property.
	 * 
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * Return the stopDate property.
	 * 
	 * @return the stopDate
	 */
	public Date getStopDate()
	{
		return stopDate;
	}

	/**
	 * Set a new value for the stopDate property.
	 * 
	 * @param stopDate
	 *            the stopDate to set
	 */
	public void setStopDate(Date stopDate)
	{
		this.stopDate = stopDate;
	}

	/**
	 * Return the orderItemNamespaceId property.
	 * 
	 * @return the orderItemNamespaceId
	 */
	public Long getOrderItemNamespaceId()
	{
		return orderItemNamespaceId;
	}

	/**
	 * Set a new value for the orderItemNamespaceId property.
	 * 
	 * @param orderItemNamespaceId
	 *            the orderItemNamespaceId to set
	 */
	public void setOrderItemNamespaceId(Long orderItemNamespaceId)
	{
		this.orderItemNamespaceId = orderItemNamespaceId;
	}

	/**
	 * Return the orderItem property.
	 * 
	 * @return the orderItem
	 */
	public String getOrderItem()
	{
		return orderItem;
	}

	/**
	 * Set a new value for the orderItem property.
	 * 
	 * @param orderItem
	 *            the orderItem to set
	 */
	public void setOrderItem(String orderItem)
	{
		this.orderItem = orderItem;
	}

	/**
	 * Return the doseQuantity property.
	 * 
	 * @return the doseQuantity
	 */
	public Long getDoseQuantity()
	{
		return doseQuantity;
	}

	/**
	 * Set a new value for the doseQuantity property.
	 * 
	 * @param doseQuantity
	 *            the doseQuantity to set
	 */
	public void setDoseQuantity(Long doseQuantity)
	{
		this.doseQuantity = doseQuantity;
	}

	/**
	 * Return the doseQuantityUnitOfMeasureNamespaceId property.
	 * 
	 * @return the doseQuantityUnitOfMeasureNamespaceId
	 */
	public Long getDoseQuantityUnitOfMeasureNamespaceId()
	{
		return doseQuantityUnitOfMeasureNamespaceId;
	}

	/**
	 * Set a new value for the doseQuantityUnitOfMeasureNamespaceId property.
	 * 
	 * @param doseQuantityUnitOfMeasureNamespaceId
	 *            the doseQuantityUnitOfMeasureNamespaceId to set
	 */
	public void setDoseQuantityUnitOfMeasureNamespaceId(
			Long doseQuantityUnitOfMeasureNamespaceId)
	{
		this.doseQuantityUnitOfMeasureNamespaceId = doseQuantityUnitOfMeasureNamespaceId;
	}

	/**
	 * Return the doseQuantityUnitOfMeasure property.
	 * 
	 * @return the doseQuantityUnitOfMeasure
	 */
	public String getDoseQuantityUnitOfMeasure()
	{
		return doseQuantityUnitOfMeasure;
	}

	/**
	 * Set a new value for the doseQuantityUnitOfMeasure property.
	 * 
	 * @param doseQuantityUnitOfMeasure
	 *            the doseQuantityUnitOfMeasure to set
	 */
	public void setDoseQuantityUnitOfMeasure(String doseQuantityUnitOfMeasure)
	{
		this.doseQuantityUnitOfMeasure = doseQuantityUnitOfMeasure;
	}

	/**
	 * Return the doseFormNamespaceId property.
	 * 
	 * @return the doseFormNamespaceId
	 */
	public Long getDoseFormNamespaceId()
	{
		return doseFormNamespaceId;
	}

	/**
	 * Set a new value for the doseFormNamespaceId property.
	 * 
	 * @param doseFormNamespaceId
	 *            the doseFormNamespaceId to set
	 */
	public void setDoseFormNamespaceId(Long doseFormNamespaceId)
	{
		this.doseFormNamespaceId = doseFormNamespaceId;
	}

	/**
	 * Return the doseForm property.
	 * 
	 * @return the doseForm
	 */
	public String getDoseForm()
	{
		return doseForm;
	}

	/**
	 * Set a new value for the doseForm property.
	 * 
	 * @param doseForm
	 *            the doseForm to set
	 */
	public void setDoseForm(String doseForm)
	{
		this.doseForm = doseForm;
	}

	/**
	 * Return the routeNamespaceId property.
	 * 
	 * @return the routeNamespaceId
	 */
	public Long getRouteNamespaceId()
	{
		return routeNamespaceId;
	}

	/**
	 * Set a new value for the routeNamespaceId property.
	 * 
	 * @param routeNamespaceId
	 *            the routeNamespaceId to set
	 */
	public void setRouteNamespaceId(Long routeNamespaceId)
	{
		this.routeNamespaceId = routeNamespaceId;
	}

	/**
	 * Return the route property.
	 * 
	 * @return the route
	 */
	public String getRoute()
	{
		return route;
	}

	/**
	 * Set a new value for the route property.
	 * 
	 * @param route
	 *            the route to set
	 */
	public void setRoute(String route)
	{
		this.route = route;
	}

	/**
	 * Return the specimenTypeNamespaceId property.
	 * 
	 * @return the specimenTypeNamespaceId
	 */
	public Long getSpecimenTypeNamespaceId()
	{
		return specimenTypeNamespaceId;
	}

	/**
	 * Set a new value for the specimenTypeNamespaceId property.
	 * 
	 * @param specimenTypeNamespaceId
	 *            the specimenTypeNamespaceId to set
	 */
	public void setSpecimenTypeNamespaceId(Long specimenTypeNamespaceId)
	{
		this.specimenTypeNamespaceId = specimenTypeNamespaceId;
	}

	/**
	 * Return the specimenType property.
	 * 
	 * @return the specimenType
	 */
	public String getSpecimenType()
	{
		return specimenType;
	}

	/**
	 * Set a new value for the specimenType property.
	 * 
	 * @param specimenType
	 *            the specimenType to set
	 */
	public void setSpecimenType(String specimenType)
	{
		this.specimenType = specimenType;
	}

	/**
	 * Return the specimenSourceNamespaceId property.
	 * 
	 * @return the specimenSourceNamespaceId
	 */
	public Long getSpecimenSourceNamespaceId()
	{
		return specimenSourceNamespaceId;
	}

	/**
	 * Set a new value for the specimenSourceNamespaceId property.
	 * 
	 * @param specimenSourceNamespaceId
	 *            the specimenSourceNamespaceId to set
	 */
	public void setSpecimenSourceNamespaceId(Long specimenSourceNamespaceId)
	{
		this.specimenSourceNamespaceId = specimenSourceNamespaceId;
	}

	/**
	 * Return the specimenSource property.
	 * 
	 * @return the specimenSource
	 */
	public String getSpecimenSource()
	{
		return specimenSource;
	}

	/**
	 * Set a new value for the specimenSource property.
	 * 
	 * @param specimenSource
	 *            the specimenSource to set
	 */
	public void setSpecimenSource(String specimenSource)
	{
		this.specimenSource = specimenSource;
	}

	/**
	 * Return the intendedDuration property.
	 * 
	 * @return the intendedDuration
	 */
	public Long getIntendedDuration()
	{
		return intendedDuration;
	}

	/**
	 * Set a new value for the intendedDuration property.
	 * 
	 * @param intendedDuration
	 *            the intendedDuration to set
	 */
	public void setIntendedDuration(Long intendedDuration)
	{
		this.intendedDuration = intendedDuration;
	}

	/**
	 * Return the durationUnitOfTimeNamespaceId property.
	 * 
	 * @return the durationUnitOfTimeNamespaceId
	 */
	public Long getDurationUnitOfTimeNamespaceId()
	{
		return durationUnitOfTimeNamespaceId;
	}

	/**
	 * Set a new value for the durationUnitOfTimeNamespaceId property.
	 * 
	 * @param durationUnitOfTimeNamespaceId
	 *            the durationUnitOfTimeNamespaceId to set
	 */
	public void setDurationUnitOfTimeNamespaceId(Long durationUnitOfTimeNamespaceId)
	{
		this.durationUnitOfTimeNamespaceId = durationUnitOfTimeNamespaceId;
	}

	/**
	 * Return the durationUnitOfTime property.
	 * 
	 * @return the durationUnitOfTime
	 */
	public String getDurationUnitOfTime()
	{
		return durationUnitOfTime;
	}

	/**
	 * Set a new value for the durationUnitOfTime property.
	 * 
	 * @param durationUnitOfTime
	 *            the durationUnitOfTime to set
	 */
	public void setDurationUnitOfTime(String durationUnitOfTime)
	{
		this.durationUnitOfTime = durationUnitOfTime;
	}

	/**
	 * Return the specimenId property.
	 * 
	 * @return the specimenId
	 */
	public Long getSpecimenId()
	{
		return specimenId;
	}

	/**
	 * Set a new value for the specimenId property.
	 * 
	 * @param specimenId
	 *            the specimenId to set
	 */
	public void setSpecimenId(Long specimenId)
	{
		this.specimenId = specimenId;
	}

	/**
	 * Return the dummyNull property.
	 * 
	 * @return the dummyNull
	 */
	public Long getDummyNull()
	{
		return dummyNull;
	}

	/**
	 * Set a new value for the dummyNull property.
	 * 
	 * @param dummyNull
	 *            the dummyNull to set
	 */
	public void setDummyNull(Long dummyNull)
	{
		this.dummyNull = dummyNull;
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

		final Order that = (Order) obj;
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
		{ "observations" });
	}
}
