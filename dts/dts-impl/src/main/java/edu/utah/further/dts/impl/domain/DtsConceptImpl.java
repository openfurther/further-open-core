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
package edu.utah.further.dts.impl.domain;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static edu.utah.further.core.api.message.Messages.unsupportedOperationMessage;
import static edu.utah.further.dts.api.domain.namespace.DtsDataType.CONCEPT;
import static edu.utah.further.dts.api.domain.namespace.DtsDataType.CONCEPT_CHILD;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.apelon.dts.client.attribute.DTSRole;
import com.apelon.dts.client.attribute.Kind;
import com.apelon.dts.client.attribute.RoleGroup;
import com.apelon.dts.client.concept.ConceptChild;
import com.apelon.dts.client.concept.OntylogConcept;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.to.DtsConceptTo;

/**
 * A DTS concept implementation and a transfer object. Wraps an Apelon DTS ontylogy
 * concept object. Cannot be marshalled and marshalled into XML using JAXB; use the
 * separate transfer object {@link DtsConceptTo}.
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
 * @version Dec 8, 2008
 */
@Implementation
public class DtsConceptImpl extends RawConceptImpl
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * The Apelon DTS concept to be wrapped.
	 */
	private final OntylogConcept concept;

	// ========================= CONSTRUCTORS ==============================

	/**
	 *
	 */
	public DtsConceptImpl()
	{
		super();
		this.concept = null;
	}

	/**
	 * @param concept
	 */
	public DtsConceptImpl(final OntylogConcept concept)
	{
		super(
				(concept != null) && instanceOf(concept, ConceptChild.class) ? CONCEPT_CHILD
						: CONCEPT, concept);
		this.concept = concept;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @param o
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object o)
	{
		// return namespace.equals(arg0);
		if (o == null)
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}

		// Works only because this method is final!!
		// if (getClass() != o.getClass())
		if (!ReflectionUtil.instanceOf(o, RawConceptImpl.class))
		{
			return false;
		}

		final RawConceptImpl that = (RawConceptImpl) o;
		return new EqualsBuilder()
				.append(getNamespaceId(), that.getNamespaceId())
				.append(getName(), that.getName())
				.isEquals();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder()
				.append(getNamespaceId())
				.append(getName())
				.toHashCode();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return (concept == null) ? null : concept.toString() + "[namespaceId="
				+ getNamespaceId() + "]";
	}

	// ========================= IMPLEMENTATION: DtsConcept ================

	// ========================= IMPLEMENTATION: DtsConcept ================

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#addDefiningConcept(com.apelon.dts.client.concept.OntylogConcept)
	 */
	@Override
	public boolean addDefiningConcept(final DtsConcept arg0)
	{
		throw new ApplicationException(
				unsupportedOperationMessage("addDefiningConcept()"));
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedDefiningConcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedDefiningConcepts()
	{
		final OntylogConcept[] concepts = concept.getFetchedDefiningConcepts();
		return toDtsConceptList(concepts);
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedHasSubs()
	 */
	@Override
	public boolean getFetchedHasSubs()
	{
		return concept.getFetchedHasSubs();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedHasSups()
	 */
	@Override
	public boolean getFetchedHasSups()
	{
		return concept.getFetchedHasSups();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedInverseRoles()
	 */
	public DTSRole[] getFetchedInverseRoles()
	{
		return concept.getFetchedInverseRoles();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedPrimitive()
	 */
	@Override
	public boolean getFetchedPrimitive()
	{
		return concept.getFetchedPrimitive();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedSubconcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedSubconcepts()
	{
		return toDtsConceptList(concept.getFetchedSubconcepts());
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedSuperconcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedSuperconcepts()
	{
		return toDtsConceptList(concept.getFetchedSuperconcepts());
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getNumSpecifiedInverseRoles()
	 */
	@Override
	public int getNumSpecifiedInverseRoles()
	{
		return concept.getNumberOfSpecifiedInverseRoles();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getNumberOfSpecifiedRoles()
	 */
	@Override
	public int getNumSpecifiedRoles()
	{
		return concept.getNumberOfSpecifiedRoles();
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedHasSubs(boolean)
	 */
	@Override
	public void setFetchedHasSubs(final boolean arg0)
	{
		concept.setFetchedHasSubs(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedHasSups(boolean)
	 */
	@Override
	public void setFetchedHasSups(final boolean arg0)
	{
		concept.setFetchedHasSups(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedPrimitive(boolean)
	 */
	@Override
	public void setFetchedPrimitive(final boolean arg0)
	{
		concept.setFetchedPrimitive(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setNumberOfSpecifiedInverseRoles(int)
	 */
	@Override
	public void setNumSpecifiedInverseRoles(final int arg0)
	{
		concept.setNumberOfSpecifiedInverseRoles(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setNumberOfSpecifiedRoles(int)
	 */
	@Override
	public void setNumSpecifiedRoles(final int arg0)
	{
		concept.setNumberOfSpecifiedRoles(arg0);
	}

	// ========================= YET UNSUPPORTED METHODS ===================

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#addDefiningRole(com.apelon.dts.client.attribute.DTSRole)
	 */
	public boolean addDefiningRole(final DTSRole arg0)
	{
		return concept.addDefiningRole(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#addInverseRole(com.apelon.dts.client.attribute.DTSRole)
	 */
	public boolean addInverseRole(final DTSRole arg0)
	{
		return concept.addInverseRole(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#addRole(com.apelon.dts.client.attribute.DTSRole)
	 */
	public boolean addRole(final DTSRole arg0)
	{
		return concept.addRole(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#containsDefiningRole(com.apelon.dts.client.attribute.DTSRole)
	 */
	public boolean containsDefiningRole(final DTSRole arg0)
	{
		return concept.containsDefiningRole(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#containsInverseRole(com.apelon.dts.client.attribute.DTSRole)
	 */
	public boolean containsInverseRole(final DTSRole arg0)
	{
		return concept.containsInverseRole(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#containsRole(com.apelon.dts.client.attribute.DTSRole)
	 */
	public boolean containsRole(final DTSRole arg0)
	{
		return concept.containsRole(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#deleteDefiningRole(com.apelon.dts.client.attribute.DTSRole)
	 */
	public boolean deleteDefiningRole(final DTSRole arg0)
	{
		return concept.deleteDefiningRole(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#deleteInverseRole(com.apelon.dts.client.attribute.DTSRole)
	 */
	public boolean deleteInverseRole(final DTSRole arg0)
	{
		return concept.deleteInverseRole(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#deleteRole(com.apelon.dts.client.attribute.DTSRole)
	 */
	public boolean deleteRole(final DTSRole arg0)
	{
		return concept.deleteRole(arg0);
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getDefiningRoleGroups()
	 */
	public RoleGroup[] getDefiningRoleGroups()
	{
		return concept.getDefiningRoleGroups();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getAsOntylogyConcept()
	 */
	public OntylogConcept getAsOntylogyConcept()
	{
		return concept;
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedKind(com.apelon.dts.client.attribute.Kind)
	 */
	public void setFetchedKind(final Kind arg0)
	{
		concept.setFetchedKind(arg0);
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getRoleGroups()
	 */
	public RoleGroup[] getRoleGroups()
	{
		return concept.getRoleGroups();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedRoles()
	 */
	public DTSRole[] getFetchedRoles()
	{
		return concept.getFetchedRoles();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedKind()
	 */
	public Kind getFetchedKind()
	{
		return concept.getFetchedKind();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedDefiningRoles()
	 */
	public DTSRole[] getFetchedDefiningRoles()
	{
		return concept.getFetchedDefiningRoles();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param concepts
	 * @return
	 */
	private List<DtsConcept> toDtsConceptList(final OntylogConcept[] concepts)
	{
		final List<DtsConcept> list = newList();
		for (final OntylogConcept c : concepts)
		{
			list.add(new DtsConceptImpl(c));
		}
		return list;
	}
}
