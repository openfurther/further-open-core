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
package edu.utah.further.dts.ws.api.domain;

import static edu.utah.further.core.api.message.Messages.unsupportedMessage;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.association.EnumAssociationType;
import edu.utah.further.dts.api.domain.namespace.DtsData;
import edu.utah.further.dts.api.to.DtsAssociationToImpl;
import edu.utah.further.dts.api.to.DtsConceptToImpl;

/**
 * A DTS concept transfer object suitable for web services. Includes REST links
 * calculation.
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
@XmlType(namespace = XmlNamespace.DTS, name = DtsConceptToWsImpl.ENTITY_NAME, propOrder =
{ "toItemLink" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsConceptToWsImpl.ENTITY_NAME)
public class DtsConceptToWsImpl extends DtsConceptToImpl
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "concept";

	// ========================= FIELDS ====================================

	/**
	 * REST links.
	 */

	// Assumed to be the owning entity, and already known to the client code so no need to
	// marshal it.
	// @XmlElement(name = "fromItem", nillable = false)
	@XmlTransient
	private String fromItemLink;

	@XmlElement(name = "toItem", nillable = false)
	private String toItemLink;

	/**
	 * Used to build REST links.
	 */
	@XmlTransient
	private final UriBuilder uriBuilder;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for JAXB.
	 */
	public DtsConceptToWsImpl()
	{
		this(null);
	}

	/**
	 * @param uriBuilder
	 *            used to build REST links. Assuming that it has two parameters:
	 *            namespaceId and conceptId, in this order
	 */
	public DtsConceptToWsImpl(final UriBuilder uriBuilder)
	{
		super();
		this.uriBuilder = uriBuilder;
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
				super.toString()).append("fromItemLink", fromItemLink).append(
				"toItemLink", toItemLink).toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public DtsConceptToWsImpl copyFrom(final DtsData other)
	{
		return (DtsConceptToWsImpl) super.copyFrom(other);
	}

	// ========================= METHODS ===================================

	/**
	 * @return
	 * @see edu.utah.further.dts.api.to.DtsSynonymTo1#getFromItemLink()
	 */
	public String getFromItemLink()
	{
		return fromItemLink;
	}

	/**
	 * @param fromItemLink
	 * @see edu.utah.further.dts.api.to.DtsSynonymTo1#setFromItemLink(java.lang.String)
	 */
	public void setFromItemLink(final String fromItemLink)
	{
		this.fromItemLink = fromItemLink;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.to.DtsSynonymTo1#getToItemLink()
	 */
	public String getToItemLink()
	{
		return toItemLink;
	}

	/**
	 * @param toItemLink
	 * @see edu.utah.further.dts.api.to.DtsSynonymTo1#setToItemLink(java.lang.String)
	 */
	public void setToItemLink(final String toItemLink)
	{
		this.toItemLink = toItemLink;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * A factory method of association TOs compatible with this concept TO. A hook for
	 * sub-classes.
	 *
	 * @param type
	 *            association type
	 * @param fromItemAssociation
	 *            If <code>true</code>, this is an association for which we need to output
	 *            only the "fromItem" link. Otherwise, we output only the "toItem" link.
	 *            Applicable only to associations, not to synonyms. In synonyms, this
	 *            argument is ignored and the value <code>true</code> is always used.
	 * @return a new {@link DtsAssociationToImpl} instance
	 * @see edu.utah.further.dts.api.to.DtsConceptToImpl#newDtsAssociationTo(edu.utah.further.dts.api.domain.association.EnumAssociationType,
	 *      boolean)
	 */
	@Override
	protected DtsAssociationToImpl newDtsAssociationTo(final EnumAssociationType type,
			final boolean fromItemAssociation)
	{
		switch (type)
		{
			case ASSOCIATION:
			{
				return new DtsAssociationToWsImpl(fromItemAssociation, uriBuilder);
			}

			case SYNONYM:
			{
				return new DtsSynonymToWsImpl(uriBuilder);
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(type));
			}
		}
	}
}
