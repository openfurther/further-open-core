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
package edu.utah.further.fqe.mpi.impl.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.CopyableFrom;
import edu.utah.further.fqe.mpi.api.Identifier;

/**
 * Represents a virtual identifier that ensures a unique and repeatable identifier across
 * several data source identifiers.
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
 * @version Nov 1, 2011
 */
@Entity
@Table(name = "VIRTUAL_OBJ_ID_MAP")
public class IdentifierEntity implements Identifier, PersistentEntity<Long>,
		CopyableFrom<Identifier, IdentifierEntity>
{

	// ========================= CONSTANTS =================================

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 7258398284096621207L;

	// ========================= FIELDS ===================================

	/**
	 * Primary key
	 */
	@Id
	@GeneratedValue
	@Column(name = "virtual_obj_id_map_id")
	private Long id;

	/**
	 * The virtual federated id
	 */
	@GeneratedValue
	@Column(name = "virtual_obj_id")
	private Long virtualId;

	/**
	 * Name to generate for
	 */
	@Column(name = "virtual_obj_name")
	private String name;

	/**
	 * Attribute to generate for
	 */
	@Column(name = "virtual_obj_attr")
	private String attr;

	/**
	 * The source data source namespace id
	 */
	@Column(name = "src_obj_nmspc_id")
	private long sourceNamespaceId;

	/**
	 * Source name to generate for
	 */
	@Column(name = "src_obj_name")
	private String sourceName;

	/**
	 * Source attribute to generate for
	 */
	@Column(name = "src_obj_attr")
	private String sourceAttr;

	/**
	 * Source obj id to generate for
	 */
	@Column(name = "src_obj_id")
	private String sourceId;

	/**
	 * A common object id if one exists for the object. Eg. two patients were later
	 * determined to be the same
	 */
	@Column(name = "fed_obj_id")
	private Long commonId;

	/**
	 * What this identifier was created by
	 */
	@Column(name = "created_by_cd")
	private final String createdBy = IdentifierEntity.class.getName();

	/**
	 * The date this was created
	 */
	@Column(name = "create_dts")
	private final Date createDate = new Date();

	/**
	 * The date this was revoked
	 */
	@Column(name = "deactivate_dts")
	private Date deactivatedDate;

	/**
	 * The query identifier associated with this identifier
	 */
	@Column(name = "query_id")
	private String queryId;

	// ========================= IMPL: HasId =================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= IMPL: CopyableFrom =================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public IdentifierEntity copyFrom(final Identifier other)
	{
		final IdentifierEntity identifier = new IdentifierEntity();
		identifier.setName(other.getName());
		identifier.setAttr(other.getAttr());
		identifier.setSourceNamespaceId(other.getSourceNamespaceId());
		identifier.setSourceName(other.getSourceName());
		identifier.setSourceAttr(other.getSourceAttr());
		identifier.setSourceId(other.getSourceId());
		identifier.setQueryId(other.getQueryId());
		return identifier;
	}

	/**
	 * Static copy method
	 * 
	 * @param other
	 *            the Identifier to copy from
	 * @return a new instance as a copy of the other
	 */
	public static IdentifierEntity newCopy(final Identifier other)
	{
		return new IdentifierEntity().copyFrom(other);
	}

	// ========================= GET/SET =================

	/**
	 * Return the virtualId property.
	 * 
	 * @return the virtualId
	 */
	public Long getVirtualId()
	{
		return virtualId;
	}

	/**
	 * Set a new value for the virtualId property.
	 * 
	 * @param virtualId
	 *            the virtualId to set
	 */
	public void setVirtualId(final Long virtualId)
	{
		this.virtualId = virtualId;
	}

	/**
	 * Return the name property.
	 * 
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Return the attr property.
	 * 
	 * @return the attr
	 */
	@Override
	public String getAttr()
	{
		return attr;
	}

	/**
	 * Set a new value for the attr property.
	 * 
	 * @param attr
	 *            the attr to set
	 */
	public void setAttr(final String attr)
	{
		this.attr = attr;
	}

	/**
	 * Return the sourceNamespaceId property.
	 * 
	 * @return the sourceNamespaceId
	 */
	@Override
	public long getSourceNamespaceId()
	{
		return sourceNamespaceId;
	}

	/**
	 * Set a new value for the sourceNamespaceId property.
	 * 
	 * @param sourceNamespaceId
	 *            the sourceNamespaceId to set
	 */
	public void setSourceNamespaceId(final long sourceNamespaceId)
	{
		this.sourceNamespaceId = sourceNamespaceId;
	}

	/**
	 * Return the sourceName property.
	 * 
	 * @return the sourceName
	 */
	@Override
	public String getSourceName()
	{
		return sourceName;
	}

	/**
	 * Set a new value for the sourceName property.
	 * 
	 * @param sourceName
	 *            the sourceName to set
	 */
	public void setSourceName(final String sourceName)
	{
		this.sourceName = sourceName;
	}

	/**
	 * Return the sourceAttr property.
	 * 
	 * @return the sourceAttr
	 */
	@Override
	public String getSourceAttr()
	{
		return sourceAttr;
	}

	/**
	 * Set a new value for the sourceAttr property.
	 * 
	 * @param sourceAttr
	 *            the sourceAttr to set
	 */
	public void setSourceAttr(final String sourceAttr)
	{
		this.sourceAttr = sourceAttr;
	}

	/**
	 * Return the sourceId property.
	 * 
	 * @return the sourceId
	 */
	@Override
	public String getSourceId()
	{
		return sourceId;
	}

	/**
	 * Set a new value for the sourceId property.
	 * 
	 * @param sourceId
	 *            the sourceId to set
	 */
	public void setSourceId(final String sourceId)
	{
		this.sourceId = sourceId;
	}

	/**
	 * Return the commonId property.
	 * 
	 * @return the commonId
	 */
	public Long getCommonId()
	{
		return commonId;
	}

	/**
	 * Set a new value for the commonId property.
	 * 
	 * @param commonId
	 *            the commonId to set
	 */
	public void setCommonId(final Long commonId)
	{
		this.commonId = commonId;
	}

	/**
	 * Return the createDate property.
	 * 
	 * @return the createDate
	 */
	public Date getCreateDate()
	{
		return createDate;
	}

	/**
	 * Return the createdBy property.
	 * 
	 * @return the createdBy
	 */
	public String getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * Return the deactivatedDate property.
	 * 
	 * @return the deactivatedDate
	 */
	public Date getDeactivatedDate()
	{
		return deactivatedDate;
	}

	/**
	 * Set a new value for the deactivatedDate property.
	 * 
	 * @param deactivatedDate
	 *            the deactivatedDate to set
	 */
	public void setDeactivatedDate(final Date deactivatedDate)
	{
		this.deactivatedDate = deactivatedDate;
	}

	/**
	 * Return the queryId property.
	 * 
	 * @return the queryId
	 */
	@Override
	public String getQueryId()
	{
		return queryId;
	}

	/**
	 * Set a new value for the queryId property.
	 * 
	 * @param queryId
	 *            the queryId to set
	 */
	public void setQueryId(final String queryId)
	{
		this.queryId = queryId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "IdentifierEntity [id=" + id + ", virtualId=" + virtualId + ", name="
				+ name + ", attr=" + attr + ", sourceNamespaceId=" + sourceNamespaceId
				+ ", sourceName=" + sourceName + ", sourceAttr=" + sourceAttr
				+ ", sourceId=" + sourceId + ", commonId=" + commonId + ", createdBy="
				+ createdBy + ", createDate=" + createDate + ", deactivatedDate="
				+ deactivatedDate + ", queryId=" + queryId + "]";
	}

}
