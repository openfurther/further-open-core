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
package edu.utah.further.mdr.ws.api.to;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.TimestampAdapter;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.mdr.api.domain.asset.ActivationInfo;

/**
 * TO of an activation/deactivation information object for an MDR resource. Because it is
 * an embeddable entity, we do not implement JAXB annotations here.
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
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.MDR, name = ActivationInfoToImpl.ENTITY_NAME, propOrder =
{ "activationDate", "deactivationDate" })
@XmlRootElement(namespace = XmlNamespace.MDR, name = ActivationInfoToImpl.ENTITY_NAME)
public class ActivationInfoToImpl implements ActivationInfo
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "activationInfo";

	// ========================= FIELDS ====================================

	/**
	 * Resource activation date.
	 */
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	@XmlElement(name = "activationDate", required = false)
	private Timestamp activationDate;

	/**
	 * Resource de-activation date.
	 */
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	@XmlElement(name = "deactivationDate", required = false)
	private Timestamp deactivationDate;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor, no fields set.
	 */
	public ActivationInfoToImpl()
	{
		super();
	}

	/**
	 * A copy-constructor.
	 *
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public ActivationInfoToImpl(final ActivationInfo other)
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
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("activationDate", activationDate)
				.append("deactivationDate", deactivationDate)
				.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.ActivationInfo#copyFrom(edu.utah.further.mdr.api.domain.asset.ActivationInfo)
	 */
	@Override
	public ActivationInfoToImpl copyFrom(final ActivationInfo other)
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

	// ========================= IMPLEMENTATION: ActivationInfo ============

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
