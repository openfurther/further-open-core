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

import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.to.DtsAssociationToImpl;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;

/**
 * A DTS association transfer object suitable for web services. Includes REST links
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
@XmlType(namespace = XmlNamespace.DTS, name = DtsAssociationToWsImpl.ENTITY_NAME, propOrder =
{ "fromItemLink", "toItemLink" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsAssociationToWsImpl.ENTITY_NAME)
public class DtsAssociationToWsImpl extends DtsAssociationToImpl implements
		DtsAssociationToWs
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "association";

	// ========================= FIELDS ====================================

	/**
	 * REST links.
	 */

	@XmlElement(name = "fromItem", nillable = false)
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
	 * An explicit no-argument constructor is required for JAXB.
	 */
	public DtsAssociationToWsImpl()
	{
		this(true, null);
	}

	/**
	 * Initialize an association TO.
	 *
	 * @param fromItemAssociation
	 *            If <code>true</code>, this is an association for which we need to output
	 *            only the "fromItem" link. Otherwise, we output only the "toItem" link
	 * @param uriBuilder
	 *            used to build REST links. Assuming that it has two parameters:
	 *            namespaceId and conceptId, in this order
	 */
	public DtsAssociationToWsImpl(final boolean fromItemAssociation,
			final UriBuilder uriBuilder)
	{
		super(fromItemAssociation);
		this.uriBuilder = uriBuilder;
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public DtsAssociationToWsImpl copyFrom(final DtsAssociation other)
	{
		return (DtsAssociationToWsImpl) super.copyFrom(other);
	}

	// ========================= IMPLEMENTATION: DtsAssociationTo ==========

	/**
	 * @param fromItemId
	 * @see edu.utah.further.dts.api.to.DtsAssociationToImpl#setFromItemId(edu.utah.further.dts.api.to.DtsConceptUniqueId)
	 */
	@Override
	@SuppressWarnings("boxing")
	public void setFromItemId(final DtsConceptUniqueId fromItemId)
	{
		super.setFromItemId(fromItemId);
		// Build REST link
		if ((fromItemId != null) && (uriBuilder != null))
		{
			final URI uri = uriBuilder.build(fromItemId.getNamespaceId(), fromItemId
					.getId());
			setFromItemLink(uri.toString());
		}
	}

	/**
	 * @param toItemId
	 * @see edu.utah.further.dts.api.to.DtsAssociationToImpl#setToItemId(edu.utah.further.dts.api.to.DtsConceptUniqueId)
	 */
	@Override
	@SuppressWarnings("boxing")
	public void setToItemId(final DtsConceptUniqueId toItemId)
	{
		super.setToItemId(toItemId);
		// Build REST link
		if ((toItemId != null) && (uriBuilder != null))
		{
			final URI uri = uriBuilder.build(toItemId.getNamespaceId(), toItemId.getId());
			setToItemLink(uri.toString());
		}
	}

	// ========================= IMPLEMENTATION: DtsAssociationToWs ========

	/**
	 * @return
	 * @see edu.utah.further.dts.api.to.DtsSynonymTo1#getFromItemLink()
	 */
	@Override
	public String getFromItemLink()
	{
		return fromItemLink;
	}

	/**
	 * @param fromItemLink
	 * @see edu.utah.further.dts.api.to.DtsSynonymTo1#setFromItemLink(java.lang.String)
	 */
	@Override
	public void setFromItemLink(final String fromItemLink)
	{
		this.fromItemLink = fromItemLink;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.to.DtsSynonymTo1#getToItemLink()
	 */
	@Override
	public String getToItemLink()
	{
		return toItemLink;
	}

	/**
	 * @param toItemLink
	 * @see edu.utah.further.dts.api.to.DtsSynonymTo1#setToItemLink(java.lang.String)
	 */
	@Override
	public void setToItemLink(final String toItemLink)
	{
		this.toItemLink = toItemLink;
	}

	// ========================= PRIVATE METHODS ===========================
}
