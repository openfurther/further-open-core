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
package edu.utah.further.mdr.data.common.domain.asset;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.mdr.api.domain.asset.ActivationInfo;

/**
 * Holds activation/deactivation information for an MDR resource.
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
 * @resource Mar 19, 2009
 */
@Embeddable
public class ActivationInfoEntity implements ActivationInfo, Serializable
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * Resource activation date.
	 */
	@Column(nullable = true)
	private Timestamp activationDate;

	/**
	 * Resource de-activation date.
	 */
	@Column(nullable = true)
	private Timestamp deactivationDate;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public ActivationInfoEntity()
	{
		super();
	}

	/**
	 * A copy-constructor.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public ActivationInfoEntity(final ActivationInfo other)
	{
		this();
		copyFrom(other);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("activationDate", activationDate).append("activationDate",
						activationDate);
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.ActivationInfo#copyFrom(edu.utah.further.mdr.api.domain.asset.ActivationInfo)
	 */
	@Override
	public ActivationInfoEntity copyFrom(final ActivationInfo other)
	{
		if (other == null)
		{
			return this;
		}

		// Identifier is not copied

		// Deep-copy fields
		final Timestamp otherActivationDate = other.getActivationDate();
		if (otherActivationDate != null)
		{
			this.activationDate = new Timestamp(otherActivationDate.getTime());
		}

		final Timestamp otherDeactivationDate = other.getDeactivationDate();
		if (otherDeactivationDate != null)
		{
			this.deactivationDate = new Timestamp(otherDeactivationDate.getTime());
		}

		// Deep-copy collection references but soft-copy their elements

		return this;
	}

	// ========================= IMPLEMENTATION: Resource ==================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.ActivationInfo#getActivationDate()
	 */
	@Override
	public Timestamp getActivationDate()
	{
		return activationDate;
	}

	/**
	 * @param activationDate
	 * @see edu.utah.further.mdr.api.domain.asset.ActivationInfo#setActivationDate(java.sql.Timestamp)
	 */
	@Override
	public void setActivationDate(final Timestamp activationDate)
	{
		this.activationDate = activationDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.ActivationInfo#getDeactivationDate()
	 */
	@Override
	public Timestamp getDeactivationDate()
	{
		return deactivationDate;
	}

	/**
	 * @param deactivationDate
	 * @see edu.utah.further.mdr.api.domain.asset.ActivationInfo#setDeactivationDate(java.sql.Timestamp)
	 */
	@Override
	public void setDeactivationDate(final Timestamp deactivationDate)
	{
		this.deactivationDate = deactivationDate;
	}
}
