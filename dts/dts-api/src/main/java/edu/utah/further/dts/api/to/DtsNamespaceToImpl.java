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

import static edu.utah.further.core.api.message.Messages.unsupportedOperationMessage;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.dts.api.domain.namespace.DtsDataType.NAMESPACE;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.namespace.DtsData;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.domain.namespace.DtsNamespaceType;

/**
 * A DTS concept implementation and a transfer object. Wraps an Apelon DTS ontylogy
 * concept object.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------<br>
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 8, 2008
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsNamespaceToImpl.ENTITY_NAME, propOrder =
{ "hasChildren", "writable", "relativeId", "local", "linkedNamespaceId", "namespaceType" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsNamespaceToImpl.ENTITY_NAME)
public class DtsNamespaceToImpl extends AbstractDtsDataTo implements DtsNamespaceTo
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	@SuppressWarnings("hiding")
	static final String ENTITY_NAME = "namespace";

	// ========================= FIELDS ====================================

	/**
	 * Data fields: ListComposite<DtsConcept>.
	 */

	/**
	 * Does this concept have children.
	 */
	@XmlElement(name = "hasChildren", nillable = false)
	private boolean hasChildren;

	/**
	 * Data fields: namespace.
	 */

	@XmlElement(name = "writable", required = false)
	private boolean writable;

	@XmlElement(name = "relativeId", required = false)
	private int relativeId;

	@XmlElement(name = "local", required = false)
	private boolean local;

	@XmlElement(name = "linkedNamespaceId", required = false)
	private int linkedNamespaceId;

	@XmlElement(name = "namespaceType", required = false)
	private DtsNamespaceType namespaceType;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for JAXB.
	 */
	public DtsNamespaceToImpl()
	{
		super(NAMESPACE);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 *            object to copy from
	 */
	public DtsNamespaceToImpl(final DtsNamespace other)
	{
		this();
		copyFrom(other);
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
		if (!ReflectionUtil.instanceOf(o, DtsNamespaceToImpl.class))
		{
			return false;
		}

		final DtsNamespaceToImpl that = (DtsNamespaceToImpl) o;
		return new EqualsBuilder()
				.append(getId(), that.getId())
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
		return new HashCodeBuilder().append(getId()).append(getName()).toHashCode();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("namespaceId", getId())
				.append("name", getName())
				.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public DtsNamespaceToImpl copyFrom(final DtsData other)
	{
		if (other == null)
		{
			return this;
		}
		super.copyFrom(other);

		if (ReflectionUtil.instanceOf(other, DtsNamespace.class))
		{
			final DtsNamespace that = (DtsNamespace) other;

			this.hasChildren = that.getHasChildren();
			this.linkedNamespaceId = that.getLinkedNamespaceId();
			this.local = that.isLocal();
			this.namespaceType = that.getNamespaceType();
			this.relativeId = that.getRelativeId();
			this.writable = that.isWritable();
		}
		return this;
	}

	// ========================= IMPLEMENTATION: DtsCompositeTo ============

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getChildren()
	 */
	@Override
	public List<DtsConcept> getChildren()
	{
		throw new ApplicationException(unsupportedOperationMessage("getChildren()"));
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getHasChildren()
	 */
	@Override
	public boolean getHasChildren()
	{
		return hasChildren;
	}

	/**
	 * @param hasChildren
	 * @see edu.utah.further.dts.api.to.DtsDataTo#setHasChildren(boolean)
	 */
	@Override
	public void setHasChildren(final boolean hasChildren)
	{
		this.hasChildren = hasChildren;
	}

	// ========================= IMPLEMENTATION: DtsNamespaceTo ============

	/**
	 * Return the writable property.
	 * 
	 * @return the writable
	 */
	@Override
	public boolean isWritable()
	{
		return writable;
	}

	/**
	 * Set a new value for the writable property.
	 * 
	 * @param writable
	 *            the writable to set
	 */
	@Override
	public void setWritable(final boolean writable)
	{
		this.writable = writable;
	}

	/**
	 * Return the relativeId property.
	 * 
	 * @return the relativeId
	 */
	@Override
	public int getRelativeId()
	{
		return relativeId;
	}

	/**
	 * Set a new value for the relativeId property.
	 * 
	 * @param relativeId
	 *            the relativeId to set
	 */
	@Override
	public void setRelativeId(final int relativeId)
	{
		this.relativeId = relativeId;
	}

	/**
	 * Return the local property.
	 * 
	 * @return the local
	 */
	@Override
	public boolean isLocal()
	{
		return local;
	}

	/**
	 * Set a new value for the local property.
	 * <p>
	 * <i>Note: JavaBeans-compliant, not mandated by the {@link DtsNamespaceTo}
	 * interface.</i>
	 * 
	 * @param local
	 *            the local to set
	 */
	public void setLocal(final boolean local)
	{
		this.local = local;
	}

	/**
	 * Return the linkedNamespaceId property.
	 * 
	 * @return the linkedNamespaceId
	 */
	@Override
	public int getLinkedNamespaceId()
	{
		return linkedNamespaceId;
	}

	/**
	 * Set a new value for the linkedNamespaceId property.
	 * <p>
	 * <i>Note: JavaBeans-compliant, not mandated by the {@link DtsNamespaceTo}
	 * interface.</i>
	 * 
	 * @param linkedNamespaceId
	 *            the linkedNamespaceId to set
	 */
	public void setLinkedNamespaceId(final int linkedNamespaceId)
	{
		this.linkedNamespaceId = linkedNamespaceId;
	}

	/**
	 * Return the namespaceType property.
	 * 
	 * @return the namespaceType
	 */
	@Override
	public DtsNamespaceType getNamespaceType()
	{
		return namespaceType;
	}

	/**
	 * Set a new value for the namespaceType property.
	 * <p>
	 * <i>Note: JavaBeans-compliant, not mandated by the {@link DtsNamespaceTo}
	 * interface.</i>
	 * 
	 * @param namespaceType
	 *            the namespaceType to set
	 */
	public void setNamespaceType(final DtsNamespaceType namespaceType)
	{
		this.namespaceType = namespaceType;
	}

}
