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
package edu.utah.further.fqe.ds.api.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.ds.api.domain.AbstractResultContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;

/**
 * A result set meta data transfer object.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Mar 19, 2009
 */
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "id", "rootEntityClass", "transferObjectClass", "numRecords" })
@XmlRootElement(namespace = XmlNamespace.FQE, name = ResultContextToImpl.ENTITY_NAME)
public class ResultContextToImpl extends AbstractResultContext implements ResultContextTo
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "resultContext";

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity. Also serves as a foreign key into the
	 * database table containing the result set rows.
	 */
	@XmlAttribute(name = "id", required = false)
	private Long id;

	/**
	 * Result set's root entity class' fully qualified name. Uniquely identifies the
	 * database table containing the result set rows.
	 */
	@XmlElement(name = "rootEntityClass", required = true, namespace = XmlNamespace.FQE)
	private String rootEntityClass;

	/**
	 * Result set's transfer object class fully qualified name.
	 */
	@XmlElement(name = "transferObjectClass", required = true, namespace = XmlNamespace.FQE)
	private String transferObjectClass;

	/**
	 * Number of records matched by this query.
	 */
	@XmlElement(name = "numRecords", required = false, namespace = XmlNamespace.FQE)
	private long numRecords = Constants.INVALID_VALUE_LONG;

	// ========================= FIELDS - ASSOCIATIONS =====================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A copy-constructor factory method.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 * @return a deep copy of <code>other</code>
	 */
	public static ResultContextToImpl newCopy(final ResultContext other)
	{
		return new ResultContextToImpl().copyFrom(other);
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public ResultContextToImpl copyFrom(final ResultContext other)
	{
		if (other == null)
		{
			return this;
		}

		// Deep-copy primitive fields and IDs, but do not deep-copy entity references
		this.id = other.getId();
		setTransferObjectClass(other.getTransferObjectClass());
		setRootEntityClass(other.getRootEntityClass());
		setNumRecords(other.getNumRecords());
		setResult(other.getResult());

		return this;
	}

	// ========================= IMPLEMENTATION: HasIdentifier =============

	/**
	 * @return
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= IMPLEMENTATION: ResultContext =============

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.ResultContext#getRootEntityClass()
	 */
	@Override
	public String getRootEntityClass()
	{
		return rootEntityClass;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.ResultContext#getTransferObjectClass()
	 */
	@Override
	public String getTransferObjectClass()
	{
		return transferObjectClass;
	}

	/**
	 * @return
	 * @see edu.utah.further.fqe.ds.api.domain.ResultContext#getNumRecords()
	 */
	@Override
	public long getNumRecords()
	{
		return numRecords;
	}

	/**
	 * @param numRecords
	 * @see edu.utah.further.fqe.ds.api.domain.ResultContext#setNumRecords(long)
	 */
	@Override
	public void setNumRecords(final long numRecords)
	{
		this.numRecords = numRecords;
	}

	// ========================= IMPLEMENTATION: ResultContextTo ===========

	/**
	 * @param rootEntityClass
	 * @see edu.utah.further.fqe.ds.api.to.ResultContextTo#setRootEntityClass(java.lang.String)
	 */
	@Override
	public void setRootEntityClass(final String rootEntityClass)
	{
		this.rootEntityClass = rootEntityClass;
	}

	/**
	 * @param transferObjectClass
	 * @see edu.utah.further.fqe.ds.api.to.ResultContextTo#setTransferObjectClass(java.lang.String)
	 */
	@Override
	public void setTransferObjectClass(final String transferObjectClass)
	{
		this.transferObjectClass = transferObjectClass;
	}
}
