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
package edu.utah.further.ds.further.model.impl.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.PersistentEntity;

/**
 * ...
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version May 9, 2012
 */
@Entity
@Implementation
@Table(name = "FPERSON_ASSOC")
@XmlRootElement(name = "PersonAssociation")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonAssociation implements
		PersistentEntity<PersonAssociationId>
{

	// ========================= CONSTANTS ===================================

	private static final long serialVersionUID = -7203623752091273982L;

	// ========================= FIELDS ======================================

	@EmbeddedId
	private PersonAssociationId id;

	@Column(name = "FPERSON_LS_ID")
	private Long personLsId;

	@Column(name = "ASSOCIATION_CID")
	private String associationConceptId;

	@Column(name = "FPERSON_RS_ID")
	private Long personRsId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.discrete.HasIdentifier#getId()
	 */
	@Override
	public PersonAssociationId getId()
	{
		return id;
	}

	/**
	 * Set a new value for the id property.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(PersonAssociationId id)
	{
		this.id = id;
	}

	/**
	 * Return the personLsId property.
	 * 
	 * @return the personLsId
	 */
	public Long getPersonLsId()
	{
		return personLsId;
	}

	/**
	 * Set a new value for the personLsId property.
	 * 
	 * @param personLsId
	 *            the personLsId to set
	 */
	public void setPersonLsId(Long personLsId)
	{
		this.personLsId = personLsId;
	}

	/**
	 * Return the associationConceptId property.
	 * 
	 * @return the associationConceptId
	 */
	public String getAssociationConceptId()
	{
		return associationConceptId;
	}

	/**
	 * Set a new value for the associationConceptId property.
	 * 
	 * @param associationConceptId
	 *            the associationConceptId to set
	 */
	public void setAssociationConceptId(String associationConceptId)
	{
		this.associationConceptId = associationConceptId;
	}

	/**
	 * Return the personRsId property.
	 * 
	 * @return the personRsId
	 */
	public Long getPersonRsId()
	{
		return personRsId;
	}

	/**
	 * Set a new value for the personRsId property.
	 * 
	 * @param personRsId
	 *            the personRsId to set
	 */
	public void setPersonRsId(Long personRsId)
	{
		this.personRsId = personRsId;
	}

}
