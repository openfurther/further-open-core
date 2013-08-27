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
package edu.utah.further.fqe.impl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.fqe.ds.api.domain.AbstractResultContext;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;

/**
 * A {@link QueryContext}'s result set information placeholder. Allows to decouple the
 * details of the query context and result set data objects.
 * <p>
 * In the future, we might want to add more meta data fields, e.g. date of the last update
 * of this result set, if a query is allowed to be re-run multiple times.
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
// Hibernate annotations
// ============================
@Entity
@Table(name = "result_context")
public class ResultContextEntity extends AbstractResultContext
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity. Also serves as a foreign key into the
	 * database table containing the result set rows.
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	@Final
	private Long id;

	/**
	 * Result set's root entity class' fully qualified name. Uniquely identifies the
	 * database table containing the result set rows.
	 */
	@Final
	@Column(name = "root_entity_class", nullable = true)
	private String rootEntityClass = Strings.EMPTY_STRING;

	/**
	 * The fully qualified transfer object (TO) class.
	 */
	@Column(name = "transfer_obj_class", nullable = true)
	private String transferObjectClass = Strings.EMPTY_STRING;

	/**
	 * Number of records matched by this query.
	 */
	@Column(name = "numrecords", nullable = true)
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
	public static ResultContextEntity newCopy(final ResultContext other)
	{
		return new ResultContextEntity().copyFrom(other);
	}

	// ========================= IMPLEMENTATION: Object ====================

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public ResultContextEntity copyFrom(final ResultContext other)
	{
		if (other == null)
		{
			return this;
		}

		// Deep-copy primitive fields and IDs, but do not deep-copy entity references
		// Do not alter the ID of this entity if it's already set
		if (getId() == null)
		{
			this.id = other.getId();
		}
		// Avoid null values for non-primitive fields so that Hibernate does not throw
		// Data integrity exceptions
		this.rootEntityClass = (other.getRootEntityClass() == null ? Strings.EMPTY_STRING
				: other.getRootEntityClass());
		this.transferObjectClass = (other.getTransferObjectClass() == null ? Strings.EMPTY_STRING
				: other.getTransferObjectClass());
		setNumRecords(other.getNumRecords());
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

	/*
	 * (non-Javadoc)
	 *
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

	// ========================= PRIVATE METHODS ===========================
}
