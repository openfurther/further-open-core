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
package edu.utah.further.fqe.ds.api.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A lightweight transfer object of a {@link QueryContextIdentifier}
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
 * @version Dec 15, 2010
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = XmlNamespace.FQE, name = QueryContextIdentifierTo.ENTITY_NAME)
@XmlType(name = "", propOrder =
{ "id" })
public class QueryContextIdentifierTo implements QueryContextIdentifier
{
	// ========================= CONSTANTS =================================

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "queryContextId";

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity.
	 */
	@XmlElement(name = "query_id", required = false, namespace = XmlNamespace.FQE)
	private Long id;

	// ========================= IMPLEMENTATION: HasSettableIdentifier ==============

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.discrete.HasSettableIdentifier#setId(java.lang.Comparable
	 * )
	 */
	@Override
	public void setId(final Long id)
	{
		this.id = id;

	}

	// ========================= IMPLEMENTATION: HasIdentifier ==============

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

}
