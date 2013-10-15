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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.utah.further.core.api.lang.Final;
import edu.utah.further.fqe.ds.api.domain.AbstractResultContextKey;
import edu.utah.further.fqe.ds.api.domain.ResultContextKey;
import edu.utah.further.fqe.ds.api.service.results.ResultType;

/**
 * An result context map-indexing key (NOT the Hibernate entity identifier). Also serves
 * as a map entry (i.e. the key has a reference to its value). A little contrieved, to
 * work around FUR-1348.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 19, 2010
 */
@Entity
@Table(name = "result_views")
public class ResultContextKeyEntity extends AbstractResultContextKey implements
		ResultContextEntry
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity.
	 */
	@Id
	@GeneratedValue
	@Column(name = "view_id")
	@Final
	private Long id;

	/**
	 * For federated result sets: the type of result set (sum/intersection/...).
	 */
	@Enumerated(value = EnumType.STRING)
	@Column(name = "type", nullable = true, length = 20)
	@Final
	private ResultType type = ResultType.SUM;

	/**
	 * Key's corresponding value in the result view map.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private ResultContextEntity value;

	// ========================= FIELDS - ASSOCIATIONS =====================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor for Hibernate use only, no fields set.
	 */
	protected ResultContextKeyEntity()
	{
		super();
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 */
	public ResultContextKeyEntity(final ResultType type)
	{
		super();
		this.type = type;
	}

	/**
	 * A copy-constructor factory method.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 * @return a deep copy of <code>other</code>
	 */
	public static ResultContextKeyEntity newCopy(final ResultContextKey other)
	{
		return new ResultContextKeyEntity(other.getType());
	}

	// ========================= IMPLEMENTATION: Object ====================

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

	// ========================= IMPLEMENTATION: ResultContextKey ==========

	/**
	 * Return the type property.
	 *
	 * @return the type
	 */
	@Override
	public ResultType getType()
	{
		return type;
	}

	// ========================= IMPLEMENTATION: MutablePublicMapEntry =====

	/**
	 * Return the value property.
	 *
	 * @return the value
	 */
	@Override
	public ResultContextEntity getValue()
	{
		return value;
	}

	/**
	 * Set a new value for the value property.
	 *
	 * @param value
	 *            the value to set
	 */
	@Override
	public void setValue(final ResultContextEntity value)
	{
		this.value = value;
	}

	/**
	 * @param key
	 * @see edu.utah.further.core.api.collections.MutablePublicMapEntry#setKey(java.lang.Object)
	 */
	@Override
	public void setKey(final ResultContextKey key)
	{
		throw new UnsupportedOperationException(
				"Key is immutable in this case, because this class is both the entry and the key");
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.collections.PublicMapEntry#getKey()
	 */
	@Override
	public ResultContextKey getKey()
	{
		return this;
	}
}
