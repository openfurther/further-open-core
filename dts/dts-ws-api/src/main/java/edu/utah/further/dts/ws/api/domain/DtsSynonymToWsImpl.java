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
import edu.utah.further.dts.api.to.DtsConceptUniqueId;
import edu.utah.further.dts.api.to.DtsSynonymToImpl;

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
@XmlType(namespace = XmlNamespace.DTS, name = DtsSynonymToWsImpl.ENTITY_NAME, propOrder =
{ "conceptLink" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsSynonymToWsImpl.ENTITY_NAME)
public class DtsSynonymToWsImpl extends DtsSynonymToImpl implements DtsSynonymToWs
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "synonym";

	// ========================= FIELDS ====================================

	/**
	 * REST links.
	 */

	@XmlElement(name = "concept", nillable = false)
	private String conceptLink;

	/**
	 * Used to build REST links.
	 */
	@XmlTransient
	private final UriBuilder uriBuilder;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * An explicit no-argument constructor is required for JAXB.
	 */
	public DtsSynonymToWsImpl()
	{
		this(null);
	}

	/**
	 * Initialize an association TO.
	 *
	 * @param uriBuilder
	 *            used to build REST links. Assuming that it has two parameters:
	 *            namespaceId and conceptId, in this order
	 */
	public DtsSynonymToWsImpl(final UriBuilder uriBuilder)
	{
		super();
		this.uriBuilder = uriBuilder;
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public DtsSynonymToWsImpl copyFrom(final DtsAssociation other)
	{
		return (DtsSynonymToWsImpl) super.copyFrom(other);
	}

	// ========================= IMPLEMENTATION: DtsAssociationTo ==========

	/**
	 * @param conceptId
	 * @see edu.utah.further.dts.api.to.DtsAssociationToImpl#setFromItemId(edu.utah.further.dts.api.to.DtsConceptUniqueId)
	 */
	@Override
	@SuppressWarnings("boxing")
	public void setFromItemId(final DtsConceptUniqueId conceptId)
	{
		super.setConceptId(conceptId);
		// Build REST link
		if ((conceptId != null) && (uriBuilder != null))
		{
			final URI uri = uriBuilder.build(conceptId.getNamespaceId(), conceptId
					.getId());
			setConceptLink(uri.toString());
		}
	}

	// ========================= IMPLEMENTATION: DtsAssociationToWs ========



	/**
	 * Return the conceptLink property.
	 *
	 * @return the conceptLink
	 */
	@Override
	public String getConceptLink()
	{
		return conceptLink;
	}

	/**
	 * Set a new value for the conceptLink property.
	 *
	 * @param conceptLink the conceptLink to set
	 */
	@Override
	public void setConceptLink(final String conceptLink)
	{
		this.conceptLink = conceptLink;
	}

	// ========================= PRIVATE METHODS ===========================
}
