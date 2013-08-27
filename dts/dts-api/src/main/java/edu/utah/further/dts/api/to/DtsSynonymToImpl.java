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
package edu.utah.further.dts.api.to;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.dts.api.domain.association.EnumAssociationType.SYNONYM;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.domain.association.DtsSynonym;
import edu.utah.further.dts.api.domain.concept.DtsConcept;

/**
 * A DTS synonum implementation and a transfer object.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------<br>
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 8, 2008
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsSynonymToImpl.ENTITY_NAME, propOrder =
{ "localAddition", "preferred" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsSynonymToImpl.ENTITY_NAME)
public class DtsSynonymToImpl extends DtsAssociationToImpl implements DtsSynonymTo
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	@SuppressWarnings("hiding")
	static final String ENTITY_NAME = "synonymTo";

	// ========================= FIELDS ====================================

	/**
	 * Data fields: Synonym.
	 */

	@XmlElement(name = "localAddition", nillable = false)
	private boolean localAddition;

	@XmlElement(name = "preferred", nillable = false)
	private boolean preferred;

	/**
	 * Associations of this object with other entities.
	 */

	/**
	 * The synonym's concept identifier.
	 */
	@XmlTransient
	private DtsConceptUniqueId conceptId;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * An explicit no-argument constructor is required for JAXB.
	 */
	public DtsSynonymToImpl()
	{
		super(SYNONYM, true);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).appendSuper(
				super.toString()).append("localAddition", localAddition).append(
				"preferred", preferred).toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public DtsSynonymToImpl copyFrom(final DtsAssociation other)
	{
		if (other == null)
		{
			return this;
		}
		super.copyFrom(other);
		if (ReflectionUtil.instanceOf(other, DtsSynonym.class))
		{
			final DtsSynonym that = (DtsSynonym) other;
			this.localAddition = that.isLocalAddition();
			this.preferred = that.isPreferred();

			final DtsConcept concept = that.getConcept();
			setConceptId((concept == null) ? null : concept.getAsUniqueId());
		}

		return this;
	}

	// ========================= IMPLEMENTATION: DtsSynonym ================

	/**
	 * @return
	 * @see com.apelon.dts.client.association.Synonym#getConcept()
	 */
	@Override
	public DtsConcept getConcept()
	{
		return getFromItem();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.association.Synonym#isLocalAddition()
	 */
	@Override
	public boolean isLocalAddition()
	{
		return localAddition;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.association.Synonym#isPreferred()
	 */
	@Override
	public boolean isPreferred()
	{
		return preferred;
	}

	// /**
	// * @param arg0
	// * @see
	// com.apelon.dts.client.association.Synonym#setConcept(com.apelon.dts.client.concept.DTSConcept)
	// */
	// public void setConcept(DTSConcept arg0)
	// {
	// synonym.setConcept(arg0);
	// }

	/**
	 * @param setLocalAddition
	 * @see com.apelon.dts.client.association.Synonym#setLocalAddition(boolean)
	 */
	@Override
	public void setLocalAddition(final boolean setLocalAddition)
	{
		this.localAddition = setLocalAddition;
	}

	/**
	 * @param preferred
	 * @see com.apelon.dts.client.association.Synonym#setPreferred(boolean)
	 */
	@Override
	public void setPreferred(final boolean preferred)
	{
		this.preferred = preferred;
	}

	// ========================= IMPLEMENTATION: DtsSynonymTo ==============

	/**
	 * Return the conceptId property.
	 *
	 * @return the conceptId
	 */
	@Override
	public DtsConceptUniqueId getConceptId()
	{
		return conceptId;
	}

	/**
	 * Set a new value for the conceptId property.
	 *
	 * @param conceptId the conceptId to set
	 */
	@Override
	public void setConceptId(final DtsConceptUniqueId conceptId)
	{
		this.conceptId = conceptId;
	}

	// ========================= PRIVATE METHODS ===========================
}
