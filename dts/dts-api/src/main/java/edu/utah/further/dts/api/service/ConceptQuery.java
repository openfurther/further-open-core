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
package edu.utah.further.dts.api.service;

import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.dts.api.domain.namespace.DtsAttributeSet;
import edu.utah.further.dts.api.to.DtsConceptId;

@Deprecated
public interface ConceptQuery extends Builder<ConceptQuery>
{
	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getNamespaceId()
	 */
	int getNamespaceId();

	/**
	 * @param namespaceId
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setNamespaceId(int)
	 */
	void setNamespaceId(int namespaceId);

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getNamespace()
	 */
	String getNamespace();

	/**
	 * @param namespace
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setNamespace(java.lang.String)
	 */
	void setNamespace(String namespace);

	/**
	 * Return the conceptName property.
	 *
	 * @return the conceptName
	 */
	String getConceptName();

	/**
	 * Set a new value for the conceptName property.
	 *
	 * @param conceptName
	 *            the conceptName to set
	 */
	void setConceptName(String conceptName);

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getPropertyName()
	 */
	String getPropertyName();

	/**
	 * @param propertyName
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setPropertyName(java.lang.String)
	 */
	void setPropertyName(String propertyName);

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getPropertyValue()
	 */
	String getPropertyValue();

	/**
	 * @param propertyValue
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setPropertyValue(java.lang.String)
	 */
	void setPropertyValue(String propertyValue);

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#getAttributeSet()
	 */
	DtsAttributeSet getAttributeSet();

	/**
	 * @param attributeSet
	 * @see edu.utah.further.dts.api.service.ConceptQuery#setAttributeSet(edu.utah.further.dts.api.domain.namespace.DtsAttributeSet)
	 */
	void setAttributeSet(DtsAttributeSet attributeSet);

	/**
	 * Set fields of a concept ID.
	 *
	 * @param conceptId
	 *            concept ID
	 */
	void setDtsConceptId(DtsConceptId conceptId);

	/**
	 * @return
	 * @see edu.utah.further.dts.api.service.ConceptQuery#isValid()
	 */
	boolean isValid();
}