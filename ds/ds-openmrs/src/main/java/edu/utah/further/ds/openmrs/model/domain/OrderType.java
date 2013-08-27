package edu.utah.further.ds.openmrs.model.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.utah.further.core.api.data.PersistentEntity;

/**
 * The persistent class for and data transfer object the order_type database table.
 * 
 */
@Entity
@Table(name = "order_type")
@XmlRootElement(name = "OrderType")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderType implements PersistentEntity<Integer>
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "order_type_id")
	private Integer orderTypeId;

	private int creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_retired")
	private Date dateRetired;

	private String description;

	private String name;

	@Column(name = "retire_reason")
	private String retireReason;

	private byte retired;

	@Column(name = "retired_by")
	private int retiredBy;

	private String uuid;

	// bi-directional many-to-one association to Order
	@OneToMany(mappedBy = "orderType")
	@XmlTransient
	private List<Order> orders;

	public OrderType()
	{
	}

	@Override
	public Integer getId()
	{
		return this.orderTypeId;
	}

	public void setId(final Integer orderTypeId)
	{
		this.orderTypeId = orderTypeId;
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

	public Date getDateRetired()
	{
		return this.dateRetired;
	}

	public void setDateRetired(final Date dateRetired)
	{
		this.dateRetired = dateRetired;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getRetireReason()
	{
		return this.retireReason;
	}

	public void setRetireReason(final String retireReason)
	{
		this.retireReason = retireReason;
	}

	public byte getRetired()
	{
		return this.retired;
	}

	public void setRetired(final byte retired)
	{
		this.retired = retired;
	}

	public int getRetiredBy()
	{
		return this.retiredBy;
	}

	public void setRetiredBy(final int retiredBy)
	{
		this.retiredBy = retiredBy;
	}

	public String getUuid()
	{
		return this.uuid;
	}

	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
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
		order.setOrderType(this);

		return order;
	}

	public Order removeOrder(final Order order)
	{
		getOrders().remove(order);
		order.setOrderType(null);

		return order;
	}

}