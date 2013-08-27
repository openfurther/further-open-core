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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * Holds a list of DtsConceptIds for marshalling purposes.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 20, 2010
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsConceptsTo.ENTITY_NAME)
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsConceptsTo.ENTITY_NAME)
public final class DtsConceptsTo
{
	// ========================= CONSTANTS ==============================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "concepts";

	/**
	 * The concept(s) to return
	 */
	@XmlElementRef
	private List<DtsConceptId> conceptIds;

	// ========================= CONSTRUCTORS ============================

	/**
	 * No-argument constructor, required for a JAXB TO/JavaBean.
	 */
	public DtsConceptsTo()
	{
		super();
	}

	/**
	 * @param conceptIds
	 */
	public DtsConceptsTo(final List<DtsConceptId> conceptIds)
	{
		super();
		this.conceptIds = conceptIds;
	}

	// ========================= GET/SET =================================

	/**
	 * Return the conceptIds property.
	 *
	 * @return the conceptIds
	 */
	public List<DtsConceptId> getConceptIds()
	{
		return conceptIds;
	}

	/**
	 * Set a new value for the conceptIds property.
	 *
	 * @param conceptIds
	 *            the conceptIds to set
	 */
	public void setConceptIds(final List<DtsConceptId> conceptIds)
	{
		this.conceptIds = conceptIds;
	}

	/**
	 * Returns the unique concept or throws an exception if it is not unique
	 *
	 * @return
	 */
	public DtsConceptId unique()
	{
		if (conceptIds.size() != 1)
		{
			throw new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND,
					"Non-unique result for concept");
		}
		return conceptIds.get(0);
	}

}
