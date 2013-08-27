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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.concept.DtsConcept;

/**
 * A DTS web service transfer object that holds a list of concepts. It seems that JAXB has
 * problems returning the exposed list from a web method, so we wrap it with this root
 * entity.
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
 * @version Jan 20, 2011
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsConceptList.ENTITY_NAME, propOrder =
{ "conceptList" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsConceptList.ENTITY_NAME)
public class DtsConceptList
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "conceptList";

	// ========================= METHODS ===================================

	/**
	 * A concept collection.
	 */
	@Final
	@XmlElement(name = "concept", required = false)
	private final List<DtsConceptToWsImpl> conceptList;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct am empty concept list.
	 */
	public DtsConceptList()
	{
		this.conceptList = newList();
	}

	/**
	 * @param conceptList
	 */
	public DtsConceptList(final List<DtsConcept> conceptList)
	{
		this();

		for (final DtsConcept concept : conceptList)
		{
			addConcept(concept);
		}
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("conceptList",
				conceptList).toString();
	}

	// ========================= METHODS ===================================

	/**
	 * Return the concept list property.
	 * <p>
	 * Note: the original list is returned, so its internals can be manipulated by a
	 * client. We could return an immutable copy of the list, but do not do it, for better
	 * performance.
	 *
	 * @return the concept list
	 */
	public List<DtsConceptToWsImpl> getConceptList()
	{
		final List<DtsConceptToWsImpl> copy = CollectionUtil
				.<DtsConceptToWsImpl> newList();
		for (final DtsConcept concept : conceptList)
		{
			copy.add(new DtsConceptToWsImpl().copyFrom(concept));
		}
		return copy;
	}

	/**
	 * Add a concept to concept list.
	 *
	 * @param concept
	 *            concept to add
	 * @return <code>true</code>
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addConcept(final DtsConcept concept)
	{
		return conceptList.add(new DtsConceptToWsImpl().copyFrom(concept));
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int getSize()
	{
		return conceptList.size();
	}

}
