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
package edu.utah.further.dts.api.domain.namespace;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;

import java.util.List;
import java.util.Map;

import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.domain.association.DtsSynonym;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.concept.DtsProperty;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;

/**
 * An adapter of {@link DtsNamespace} to {@link DtsConcept} that allows it to participate
 * in DTS tree walks. Delegates all namespace methods to the wrapped namespace object, and
 * behaves like a stub in all other {@link DtsConcept} methods.
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
 * @version Jul 1, 2009
 */
public class DtsNamespaceAdapter implements DtsConcept
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * The object to be wrapped.
	 */
	private final DtsNamespace namespace;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Adapt a namespace to an ontology concept.
	 * 
	 * @param namespace
	 *            object to wrap
	 */
	public DtsNamespaceAdapter(final DtsNamespace namespace)
	{
		super();
		this.namespace = namespace;
	}

	// ========================= IMPLEMENTATION: DtsNamespaceAdapter =======

	/**
	 * @return
	 * @see edu.utah.further.core.api.tree.ListComposite#getChildren()
	 */
	@Override
	public List<DtsConcept> getChildren()
	{
		return namespace.getChildren();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getCode()
	 */
	@Override
	public String getCode()
	{
		return namespace.getCode();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.tree.ListComposite#getHasChildren()
	 */
	@Override
	public boolean getHasChildren()
	{
		return namespace.getHasChildren();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getId()
	 */
	@Override
	public int getId()
	{
		return namespace.getId();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsNamespace#getLinkedNamespaceId()
	 */
	public int getLinkedNamespaceId()
	{
		return namespace.getLinkedNamespaceId();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return namespace.getName();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsNamespace#getNamespaceType()
	 */
	public DtsNamespaceType getNamespaceType()
	{
		return namespace.getNamespaceType();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsNamespace#getRelativeId()
	 */
	public int getRelativeId()
	{
		return namespace.getRelativeId();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getType()
	 */
	@Override
	public DtsDataType getType()
	{
		// return namespace.getType();
		return DtsDataType.CONCEPT;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsNamespace#isLocal()
	 */
	public boolean isLocal()
	{
		return namespace.isLocal();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsNamespace#isWritable()
	 */
	public boolean isWritable()
	{
		return namespace.isWritable();
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#setCode(java.lang.String)
	 */
	@Override
	public void setCode(final String arg0)
	{
		namespace.setCode(arg0);
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#setId(int)
	 */
	@Override
	public void setId(final int arg0)
	{
		namespace.setId(arg0);
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#setName(java.lang.String)
	 */
	@Override
	public void setName(final String arg0)
	{
		namespace.setName(arg0);
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.namespace.DtsNamespace#setRelativeId(int)
	 */
	public void setRelativeId(final int arg0)
	{
		namespace.setRelativeId(arg0);
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.namespace.DtsNamespace#setWritable(boolean)
	 */
	public void setWritable(final boolean arg0)
	{
		namespace.setWritable(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#addDefiningConcept(edu.utah.further.dts.api.domain.concept.DtsConcept)
	 */
	@Override
	public boolean addDefiningConcept(final DtsConcept arg0)
	{
		// Method stub
		return false;
	}

	/**
	 * @param arg0
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#containsDefiningConcept(edu.utah.further.dts.api.domain.concept.DtsConcept)
	 */
	@Override
	public boolean containsDefiningConcept(final DtsConcept arg0)
	{
		// Method stub
		return false;
	}

	/**
	 * @param arg0
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#deleteDefiningConcept(edu.utah.further.dts.api.domain.concept.DtsConcept)
	 */
	@Override
	public boolean deleteDefiningConcept(final DtsConcept arg0)
	{
		// Method stub
		return false;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedDefiningConcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedDefiningConcepts()
	{
		// Method stub
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedHasSubs()
	 */
	@Override
	public boolean getFetchedHasSubs()
	{
		// Method stub
		return false;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedHasSups()
	 */
	@Override
	public boolean getFetchedHasSups()
	{
		// Method stub
		return false;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedPrimitive()
	 */
	@Override
	public boolean getFetchedPrimitive()
	{
		// Method stub
		return false;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedSubconcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedSubconcepts()
	{
		// Method stub
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedSuperconcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedSuperconcepts()
	{
		// Method stub
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getNumSpecifiedInverseRoles()
	 */
	@Override
	public int getNumSpecifiedInverseRoles()
	{
		// Method stub
		return 0;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getNumSpecifiedRoles()
	 */
	@Override
	public int getNumSpecifiedRoles()
	{
		// Method stub
		return 0;
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedHasSubs(boolean)
	 */
	@Override
	public void setFetchedHasSubs(final boolean arg0)
	{
		// Method stub

	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedHasSups(boolean)
	 */
	@Override
	public void setFetchedHasSups(final boolean arg0)
	{
		// Method stub

	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedPrimitive(boolean)
	 */
	@Override
	public void setFetchedPrimitive(final boolean arg0)
	{
		// Method stub

	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedSubconcepts(java.util.List)
	 */
	@Override
	public void setFetchedSubconcepts(final List<? extends DtsConcept> arg0)
	{
		// Method stub

	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedSuperconcepts(java.util.List)
	 */
	@Override
	public void setFetchedSuperconcepts(final List<? extends DtsConcept> arg0)
	{
		// Method stub

	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setNumSpecifiedInverseRoles(int)
	 */
	@Override
	public void setNumSpecifiedInverseRoles(final int arg0)
	{
		// Method stub

	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setNumSpecifiedRoles(int)
	 */
	@Override
	public void setNumSpecifiedRoles(final int arg0)
	{
		// Method stub

	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedPreferredTerm()
	 */
	@Override
	public DtsSynonym getFetchedPreferredTerm()
	{
		// Method stub
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedSynonyms()
	 */
	@Override
	public List<DtsSynonym> getFetchedSynonyms()
	{
		// Method stub
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getMatchString()
	 */
	@Override
	public String getMatchString()
	{
		// Method stub
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getNumSpecifiedConceptAssociations()
	 */
	@Override
	public int getNumSpecifiedConceptAssociations()
	{
		// Method stub
		return 0;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getNumSpecifiedInverseConceptAssociations()
	 */
	@Override
	public int getNumSpecifiedInverseConceptAssociations()
	{
		// Method stub
		return 0;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getNumSpecifiedSynonyms()
	 */
	@Override
	public int getNumSpecifiedSynonyms()
	{
		// Method stub
		return 0;
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setNumSpecifiedConceptAssociations(int)
	 */
	@Override
	public void setNumSpecifiedConceptAssociations(final int arg0)
	{
		// Method stub

	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setNumSpecifiedInverseConceptAssociations(int)
	 */
	@Override
	public void setNumSpecifiedInverseConceptAssociations(final int arg0)
	{
		// Method stub

	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setNumSpecifiedSynonyms(int)
	 */
	@Override
	public void setNumSpecifiedSynonyms(final int arg0)
	{
		// Method stub

	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject#getNumSpecifiedProperties()
	 */
	@Override
	public int getNumSpecifiedProperties()
	{
		// Method stub
		return 0;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject#getProperties()
	 */
	@Override
	public Map<String, List<DtsProperty>> getProperties()
	{
		return null;
	}

	/**
	 * @param propertyName
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject#getProperty(java.lang.String)
	 */
	@Override
	public List<DtsProperty> getProperty(final String propertyName)
	{
		// Method stub
		return null;
	}

	/**
	 * @param propertyName
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject#getPropertyValue(java.lang.String)
	 */
	@Override
	public List<String> getPropertyValue(final String propertyName)
	{
		// Method stub
		return null;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsObject#getNamespaceId()
	 */
	@Override
	public int getNamespaceId()
	{
		return namespace.getId();
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.namespace.DtsObject#setNamespaceId(int)
	 */
	@Override
	public void setNamespaceId(final int arg0)
	{
		namespace.setId(arg0);
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedConceptAssociations()
	 */
	@Override
	public List<DtsAssociation> getFetchedConceptAssociations()
	{
		return newList();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedInverseConceptAssociations()
	 */
	@Override
	public List<DtsAssociation> getFetchedInverseConceptAssociations()
	{
		return newList();
	}

	/**
	 * Return a unique identifier view of the concept.
	 * 
	 * @return a unique identifier object corresponding to this object
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getAsUniqueId()
	 */
	@Override
	public DtsConceptUniqueId getAsUniqueId()
	{
		return new DtsConceptUniqueId(getNamespaceId(), getId());
	}
}
