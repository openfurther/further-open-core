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
package edu.utah.further.dts.impl.domain.association;

import static edu.utah.further.dts.api.domain.association.EnumAssociationType.ASSOCIATION;
import static edu.utah.further.dts.impl.util.DtsUtil.newDtsConcept;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.apelon.dts.client.association.Association;
import com.apelon.dts.client.association.ConceptAssociation;
import com.apelon.dts.client.association.QualifiedAssociation;
import com.apelon.dts.client.concept.DTSConcept;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.domain.association.DtsAssociationType;
import edu.utah.further.dts.api.domain.association.EnumAssociationType;
import edu.utah.further.dts.api.domain.attribute.DtsQualifier;
import edu.utah.further.dts.api.domain.concept.DtsConcept;

/**
 * A DTS concept association implementation. Wraps an Apelon DTS association object.
 * Currently only concept associations are supported.
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
public class DtsAssociationImpl implements DtsAssociation
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Type-safe association type.
	 */
	private final EnumAssociationType enumAssociationType;

	/**
	 * The Apelon DTS association to be wrapped.
	 */
	private final Association association;

	/**
	 * A link to the association's to-concept counterpart in our APIs.
	 */
	private final DtsConcept fromConcept;

	/**
	 * A link to the association's from-concept counterpart in our APIs.
	 */
	private final DtsConcept toConcept;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A default c-tor. Required by JAXB.
	 */
	public DtsAssociationImpl()
	{
		this(null, null, null, null);
	}

	/**
	 * Wrap an Apelon concept association with our API.
	 * 
	 * @param synonym
	 *            Apelon association
	 */
	public DtsAssociationImpl(final ConceptAssociation association)
	{
		this(ASSOCIATION, association, newDtsConcept(association.getFromConcept()),
				newDtsConcept(association.getToConcept()));
	}

	/**
	 * Wrap an Apelon uni-directional association with our API. The to-item is set to
	 * <code>null</code>.
	 * 
	 * @param synonym
	 *            Apelon association
	 */
	public DtsAssociationImpl(final Association association)
	{
		this(ASSOCIATION, association, newDtsConcept((DTSConcept) association
				.getFromItem()), null);
	}

	/**
	 * Wrap an Apelon uni-directional association with our API. The to-item is set to
	 * <code>null</code>.
	 * 
	 * @param enumAssociationType
	 *            type-safe association type of this object
	 * @param synonym
	 *            Apelon association
	 */
	protected DtsAssociationImpl(final EnumAssociationType enumAssociationType,
			final Association association)
	{
		this(enumAssociationType, association, newDtsConcept((DTSConcept) association
				.getFromItem()), null);
	}

	/**
	 * @param enumAssociationType
	 *            type-safe association type of this object
	 * @param association
	 * @param fromConcept
	 * @param toConcept
	 */
	protected DtsAssociationImpl(final EnumAssociationType enumAssociationType,
			final Association association, final DtsConcept fromConcept,
			final DtsConcept toConcept)
	{
		super();
		this.enumAssociationType = enumAssociationType;
		this.association = association;
		this.fromConcept = fromConcept;
		this.toConcept = toConcept;
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
		if (!ReflectionUtil.instanceOf(o, DtsAssociationImpl.class))
		{
			return false;
		}

		final DtsAssociationImpl that = (DtsAssociationImpl) o;
		return new EqualsBuilder().append(association, that.association).isEquals();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(association).toHashCode();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return (association == null) ? null : association.toString();
	}

	// ========================= IMPLEMENTATION: DtsAssociation ============

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.association.DtsAssociation#getEnumAssociationType()
	 */
	@Override
	public EnumAssociationType getEnumAssociationType()
	{
		return enumAssociationType;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.association.DtsAssociation#getFromItem()
	 */
	@Override
	public DtsConcept getFromItem()
	{
		return fromConcept;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.association.DtsAssociation#getToItem()
	 */
	@Override
	public DtsConcept getToItem()
	{
		return toConcept;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.association.Association#getAssociationType()
	 */
	@Override
	public DtsAssociationType getAssociationType()
	{
		return new DtsAssociationTypeImpl(association.getAssociationType());
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.association.Association#getName()
	 */
	@Override
	public String getName()
	{
		return association.getName();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.association.Association#getValue()
	 */
	@Override
	public String getValue()
	{
		return association.getValue();
	}

	/**
	 * @return
	 * @see DtsAssociation#containsQualifier(DtsQualifier)
	 */
	@Override
	public boolean containsQualifier(final DtsQualifier qualifier)
	{
		if (qualifier == null || !(association instanceof QualifiedAssociation))
		{
			return false;
		}
		return ((QualifiedAssociation) association)
				.containsQualifier(((DtsQualifierImpl)qualifier).getQualifier());
	}

	// ========================= PRIVATE METHODS ===========================

}
