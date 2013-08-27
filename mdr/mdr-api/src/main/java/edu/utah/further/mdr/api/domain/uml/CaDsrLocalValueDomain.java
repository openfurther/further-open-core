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
package edu.utah.further.mdr.api.domain.uml;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.mdr.api.domain.uml.ClassType.CADSR_LOCAL_VALUE_DOMAIN;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Api;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.to.DtsConceptId;

/**
 * A UML class model element. Supports a single inheritance with no interfaces for now.
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
@Api
public class CaDsrLocalValueDomain extends UmlClass
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * CADSR LVD identifier.
	 */
	private final DtsConceptId conceptId;

	/**
	 * DTS Concept that this LVD represents.
	 */
	private DtsConcept concept;

	/**
	 * List of values of this domain. These are the DTS children of {@link #concept}, if
	 * this domain is mapped (i.e. has an active namespace).
	 */
	private List<DtsConcept> valueSet = newList();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an element.
	 *
	 * @param xmiId
	 *            XMI ID of the element
	 * @param name
	 *            element's name
	 * @param superClass
	 *            super class name
	 * @param id
	 *            concept identifier
	 */
	public CaDsrLocalValueDomain(final String xmiId, final String name,
			final String superClassName, final DtsConceptId id)
	{
		super(xmiId, name, CADSR_LOCAL_VALUE_DOMAIN, superClassName);
		this.conceptId = id;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.appendSuper(super.toString())
				.append("id", conceptId)
				.toString();
	}

	// ========================= IMPLEMENTATION: Visitable<ItemVisitor> ====

	/**
	 * Let a visitor process this item. Part of the Visitor pattern. This calls back the
	 * visitor's <code>visit()</code> method with this item type.
	 *
	 * @param visitor
	 *            item data visitor whose <code>visit()</code> method is invoked
	 */
	@Override
	public void accept(final UmlElementVisitor visitor)
	{
		visitor.visit(this);
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the concept id property.
	 *
	 * @return the concept id
	 */
	public DtsConceptId getConceptId()
	{
		return conceptId;
	}

	/**
	 * Return the concept property.
	 *
	 * @return the concept
	 */
	public DtsConcept getConcept()
	{
		return concept;
	}

	/**
	 * Set a new value for the concept property.
	 *
	 * @param concept
	 *            the concept to set
	 */
	public void setConcept(final DtsConcept concept)
	{
		this.concept = concept;
	}

	/**
	 * Return the valueSet property.
	 *
	 * @return the valueSet
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementImpl#getValueSet()
	 */
	@Override
	public List<DtsConcept> getValueSet()
	{
		return valueSet;
	}

	/**
	 * Set a new value for the valueSet property.
	 *
	 * @param valueSet
	 *            the valueSet to set
	 */
	public void setValueSet(final List<? extends DtsConcept> valueSet)
	{
		this.valueSet = CollectionUtil.<DtsConcept> newList(valueSet);
	}

	// ========================= PRIVATE METHODS ===========================
}
