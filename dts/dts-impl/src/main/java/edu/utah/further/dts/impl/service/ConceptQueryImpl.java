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
package edu.utah.further.dts.impl.service;

import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_INTEGER;
import static edu.utah.further.core.api.text.StringUtil.isValidInteger;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.dts.api.domain.namespace.DtsAttributeSet.DEFAULT_ALL_ATTRIBUTES;
import static org.apache.commons.lang.StringUtils.isBlank;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.dts.api.domain.namespace.DtsAttributeSet;
import edu.utah.further.dts.api.service.ConceptQuery;
import edu.utah.further.dts.api.to.DtsConceptId;

/**
 * A DTS concept query. Uses the builder pattern. Not currently in use.
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
 * @version Dec 12, 2008
 */
@SuppressWarnings("deprecation")
@Implementation
@Deprecated
public class ConceptQueryImpl implements ConceptQuery
{
	// ========================= FIELDS: OPTIONAL ==========================

	/**
	 * DTS namespace ID.
	 */
	private int namespaceId = INVALID_VALUE_INTEGER;

	/**
	 * DTS namespace's name.
	 */
	private String namespace;

	/**
	 * Concept name.
	 */
	private String conceptName;

	/**
	 * DTS concept property name.
	 */
	private String propertyName;

	/**
	 * DTS concept property value.
	 */
	private String propertyValue;

	/**
	 * Attribute set to return with the concept;
	 */
	private DtsAttributeSet attributeSet = DEFAULT_ALL_ATTRIBUTES;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("namespaceId", namespaceId)
				.append("namespace", namespace)
				.append("propertyName", propertyName)
				.append("propertyValue", propertyValue)
				.toString();
	}

	// ========================= IMPLEMENTATION: Builder ===================

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#build()
	 */
	@Override
	public ConceptQuery build()
	{
		// Add validation here if needed
		if (isValid())
		{
			throw new UnsupportedOperationException("Malformed query: " + this);
		}

		return this;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getNamespaceId()
	 */
	@Override
	public int getNamespaceId()
	{
		return namespaceId;
	}

	/**
	 * @param namespaceId
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setNamespaceId(int)
	 */
	@Override
	public void setNamespaceId(final int namespaceId)
	{
		this.namespaceId = namespaceId;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return namespace;
	}

	/**
	 * @param namespace
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setNamespace(java.lang.String)
	 */
	@Override
	public void setNamespace(final String namespace)
	{
		this.namespace = namespace;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getConceptName()
	 */
	@Override
	public String getConceptName()
	{
		return conceptName;
	}

	/**
	 * @param conceptName
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setConceptName(java.lang.String)
	 */
	@Override
	public void setConceptName(final String conceptName)
	{
		this.conceptName = conceptName;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getPropertyName()
	 */
	@Override
	public String getPropertyName()
	{
		return propertyName;
	}

	/**
	 * @param propertyName
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setPropertyName(java.lang.String)
	 */
	@Override
	public void setPropertyName(final String propertyName)
	{
		this.propertyName = propertyName;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getPropertyValue()
	 */
	@Override
	public String getPropertyValue()
	{
		return propertyValue;
	}

	/**
	 * @param propertyValue
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setPropertyValue(java.lang.String)
	 */
	@Override
	public void setPropertyValue(final String propertyValue)
	{
		this.propertyValue = propertyValue;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getAttributeSet()
	 */
	@Override
	public DtsAttributeSet getAttributeSet()
	{
		return attributeSet;
	}

	/**
	 * @param attributeSet
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setAttributeSet(edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	@Override
	public void setAttributeSet(final DtsAttributeSet attributeSet)
	{
		this.attributeSet = attributeSet;
	}

	/**
	 * @param conceptId
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setDtsConceptId(edu.utah.further.dts.api.to.DtsConceptId)
	 */
	@Override
	public void setDtsConceptId(final DtsConceptId conceptId)
	{
		setNamespace(conceptId.getNamespace());
		setPropertyName(conceptId.getPropertyName());
		setPropertyValue(conceptId.getPropertyValue());
	}

	// ========================= METHODS ===================================

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#isValid()
	 */
	@Override
	public boolean isValid()
	{
		return hasNamespaceInformation() && hasPropertyInformation();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private boolean hasNamespaceInformation()
	{
		return isValidInteger(namespaceId) || (namespace != null);
	}

	/**
	 * @return
	 */
	private boolean hasPropertyInformation()
	{
		return isBlank(propertyName) && isBlank(propertyValue);
	}
}
