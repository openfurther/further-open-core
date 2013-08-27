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

import static edu.utah.further.dts.api.domain.namespace.DtsDataType.ASSOCIATION_TYPE;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.apelon.dts.client.association.AssociationType;
import com.apelon.dts.client.association.ItemsConnected;
import com.apelon.dts.client.association.Purpose;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.dts.api.domain.association.DtsAssociationType;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.namespace.DtsDataType;
import edu.utah.further.dts.impl.domain.AbstractDtsData;

/**
 * A DTS concept association type that wraps an Apelon object.
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
public class DtsAssociationTypeImpl extends AbstractDtsData implements DtsAssociationType
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "associationType";

	// ========================= FIELDS ====================================

	/**
	 * The Apelon DTS association to be wrapped.
	 */
	private final AssociationType associationType;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A default c-tor. Required by JAXB.
	 */
	public DtsAssociationTypeImpl()
	{
		super();
		this.associationType = null;
	}

	/**
	 * Wrap an Apelon concept with our API.
	 * 
	 * @param associationType
	 *            Apelon association type
	 */
	public DtsAssociationTypeImpl(final AssociationType associationType)
	{
		this(ASSOCIATION_TYPE, associationType);
	}

	/**
	 * Wrap an Apelon concept with our API.
	 * 
	 * @param type
	 *            DTS object type of the Apelon concept
	 * @param associationType
	 *            Apelon association type
	 */
	protected DtsAssociationTypeImpl(final DtsDataType type,
			final AssociationType associationType)
	{
		super(type, associationType);
		this.associationType = associationType;
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
		if (!ReflectionUtil.instanceOf(o, DtsAssociationTypeImpl.class))
		{
			return false;
		}

		final DtsAssociationTypeImpl that = (DtsAssociationTypeImpl) o;
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
		return (associationType == null) ? null : associationType.toString();
	}

	// ========================= IMPLEMENTATION: DtsData ===================

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getChildren()
	 */
	@Override
	public List<DtsConcept> getChildren()
	{
		return CollectionUtil.newList();
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getHasChildren()
	 */
	@Override
	public boolean getHasChildren()
	{
		return false;
	}

	// ========================= IMPLEMENTATION: DtsAssociationType ========

	/**
	 * @return
	 * @see com.apelon.dts.client.association.AssociationType#getInverseName()
	 */
	@Override
	public String getInverseName()
	{
		return associationType.getInverseName();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.association.AssociationType#getItemsConnected()
	 */
	public ItemsConnected getItemsConnected()
	{
		return associationType.getItemsConnected();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#getNamespaceId()
	 */
	@Override
	public int getNamespaceId()
	{
		return associationType.getNamespaceId();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.association.AssociationType#getPurpose()
	 */
	public Purpose getPurpose()
	{
		return associationType.getPurpose();
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.common.DTSObject#setCode(java.lang.String)
	 */
	@Override
	public void setCode(final String arg0)
	{
		associationType.setCode(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.common.DTSObject#setId(int)
	 */
	@Override
	public void setId(final int arg0)
	{
		associationType.setId(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.association.AssociationType#setInverseName(java.lang.String)
	 */
	public void setInverseName(final String arg0)
	{
		associationType.setInverseName(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.association.AssociationType#setItemsConnected(com.apelon.dts.client.association.ItemsConnected)
	 */
	public void setItemsConnected(final ItemsConnected arg0)
	{
		associationType.setItemsConnected(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.common.DTSObject#setName(java.lang.String)
	 */
	@Override
	public void setName(final String arg0)
	{
		associationType.setName(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.common.DTSObject#setNamespaceId(int)
	 */
	@Override
	public void setNamespaceId(final int arg0)
	{
		associationType.setNamespaceId(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.association.AssociationType#setPurpose(com.apelon.dts.client.association.Purpose)
	 */
	public void setPurpose(final Purpose arg0)
	{
		associationType.setPurpose(arg0);
	}

	// ========================= PRIVATE METHODS ===========================
}
