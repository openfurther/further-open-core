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

import static edu.utah.further.dts.api.domain.namespace.DtsDataType.NAMESPACE;
import static edu.utah.further.dts.impl.util.DtsUtil.toOurNamespaceType;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.apelon.dts.client.namespace.Authority;
import com.apelon.dts.client.namespace.ContentVersion;
import com.apelon.dts.client.namespace.Namespace;

import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.domain.namespace.DtsNamespaceType;
import edu.utah.further.dts.impl.util.DtsUtil;

/**
 * A DTS concept implementation and transfer object. Wraps an Apelon DTS namespace object.
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
public class DtsNamespaceImpl extends AbstractDtsData implements DtsNamespace, Serializable
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = -7749236165937472229L;

	// ========================= FIELDS ====================================

	/**
	 * The Apelon DTS concept to be wrapped.
	 */
	private final Namespace namespace;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default c-tor, creates an empty namespace entity.
	 */
	public DtsNamespaceImpl()
	{
		super(NAMESPACE);
		this.namespace = null;
	}

	/**
	 * Apelon <code>Namespace</code> adapter.
	 *
	 * @param type
	 * @param namespace
	 */
	public DtsNamespaceImpl(final Namespace namespace)
	{
		super(NAMESPACE, namespace);
		this.namespace = namespace;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * A must for custom JSF converters to work.
	 *
	 * @param o
	 *            another namespace to compare this object with
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#equals(java.lang.Object)
	 * @see http://www.nuwanbando.com/?p=75
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
		if (!ReflectionUtil.instanceOf(o, DtsNamespaceImpl.class))
		{
			return false;
		}

		final DtsNamespaceImpl that = (DtsNamespaceImpl) o;
		return new EqualsBuilder().append(getName(), that.getName()).isEquals();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(getName()).toHashCode();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return (namespace == null) ? new ToStringBuilder(this)
				.append(getName())
				.toString() : namespace.toString();
	}

	// ========================= IMPLEMENTATION: DtsNamespace ==============

	/**
	 * @return
	 * @throws CloneNotSupportedException
	 * @see com.apelon.dts.client.namespace.Namespace#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return namespace.clone();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#getAuthority()
	 */
	public Authority getAuthority()
	{
		return namespace.getAuthority();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#getContentVersion()
	 */
	public ContentVersion getContentVersion()
	{
		return namespace.getContentVersion();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#getLinkedNamespaceId()
	 */
	@Override
	public int getLinkedNamespaceId()
	{
		return namespace.getLinkedNamespaceId();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#getNamespaceType()
	 */
	@Override
	public DtsNamespaceType getNamespaceType()
	{
		return DtsUtil.toOurNamespaceType(namespace.getNamespaceType());
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsNamespace#getDtsNamespaceType()
	 */
	public DtsNamespaceType getDtsNamespaceType()
	{
		return toOurNamespaceType(namespace.getNamespaceType());
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#getRelativeId()
	 */
	@Override
	public int getRelativeId()
	{
		return namespace.getRelativeId();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#isLocal()
	 */
	@Override
	public boolean isLocal()
	{
		return namespace.isLocal();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#isWritable()
	 */
	@Override
	public boolean isWritable()
	{
		return namespace.isWritable();
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.namespace.Namespace#setAuthority(com.apelon.dts.client.namespace.Authority)
	 */
	public void setAuthority(final Authority arg0)
	{
		namespace.setAuthority(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.namespace.Namespace#setCode(java.lang.String)
	 */
	@Override
	public void setCode(final String arg0)
	{
		namespace.setCode(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.namespace.Namespace#setContentVersion(com.apelon.dts.client.namespace.ContentVersion)
	 */
	public void setContentVersion(final ContentVersion arg0)
	{
		namespace.setContentVersion(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.namespace.Namespace#setId(int)
	 */
	@Override
	public void setId(final int arg0)
	{
		namespace.setId(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.namespace.Namespace#setName(java.lang.String)
	 */
	@Override
	public void setName(final String arg0)
	{
		namespace.setName(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.namespace.Namespace#setRelativeId(int)
	 */
	@Override
	public void setRelativeId(final int arg0)
	{
		namespace.setRelativeId(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.namespace.Namespace#setWritable(boolean)
	 */
	@Override
	public void setWritable(final boolean arg0)
	{
		namespace.setWritable(arg0);
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsNamespace#getAsNamespace()
	 */
	public Namespace getAsNamespace()
	{
		return namespace;
	}
}
