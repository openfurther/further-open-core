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
package edu.utah.further.core.data.domain;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.Final;

/**
 * A simple persistent entity.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Entity
@Table(name = "Simple")
public class SimpleEntity implements PersistentEntity<Long>
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
	@Final
	private Long id;

	/**
	 * Age in years.
	 */
	@Column(name = "age", nullable = true)
	private Integer age;

	/**
	 * Text field.
	 */
	@Column(name = "comment", nullable = true)
	private String comment;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final SimpleEntity that = (SimpleEntity) obj;
		return new EqualsBuilder().append(this.id, that.id).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(id).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("id", id)
				.append("age", age)
				.append("comment", comment)
				.toString();
	}

	// ========================= IMPLEMENTATION: PersistentEntity ==========

	/**
	 * @return
	 * @see edu.utah.further.core.util.data.PersistentEntity#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= IMPLEMENTATION: Person ====================

	/**
	 * Return the age property.
	 * 
	 * @return the age
	 */
	public Integer getAge()
	{
		return age;
	}

	/**
	 * Set a new value for the age property.
	 * 
	 * @param age
	 *            the age to set
	 */
	public void setAge(final Integer age)
	{
		this.age = age;
	}

	/**
	 * Return the comment property.
	 * 
	 * @return the comment
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * Set a new value for the comment property.
	 * 
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(final String comment)
	{
		this.comment = comment;
	}

	// ========================= METHODS ===================================

	/**
	 * Set a new value for the id property. Not supposed to exist for a persistent entity,
	 * but convenient for testing.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}
}
