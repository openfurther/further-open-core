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
package edu.utah.further.ds.i2b2.model.impl.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.ds.i2b2.model.api.domain.ProviderDimensionId;

/**
 * Provider Dimension Primary Key Entity
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Rick L. Bradshaw <rick.bradshaw@utah.edu>}
 * @version April 15, 2010
 */
@Embeddable
public class ProviderDimensionPK implements ProviderDimensionId
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8601696565768650009L;

	@Basic(optional = false)
	@Column(name = "PROVIDER_ID")
	private String providerId;
	@Basic(optional = false)
	@Column(name = "PROVIDER_PATH")
	private String providerPath;

	public ProviderDimensionPK()
	{
	}

	public ProviderDimensionPK(String providerId, String providerPath)
	{
		this.providerId = providerId;
		this.providerPath = providerPath;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimensionId#getProviderId()
	 */
	@Override
	public String getProviderId()
	{
		return providerId;
	}

	/**
	 * @param providerId
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimensionId#setProviderId(java.lang.String)
	 */
	@Override
	public void setProviderId(String providerId)
	{
		this.providerId = providerId;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimensionId#getProviderPath()
	 */
	@Override
	public String getProviderPath()
	{
		return providerPath;
	}

	/**
	 * @param providerPath
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ProviderDimensionId#setProviderPath(java.lang.String)
	 */
	@Override
	public void setProviderPath(String providerPath)
	{
		this.providerPath = providerPath;
	}

	/**
	 * @param o
	 * @return
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ProviderDimensionId that)
	{
		return new CompareToBuilder()
				.append(this.getProviderId(), that.getProviderId())
				.append(this.getProviderPath(), that.getProviderPath())
				.toComparison();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object)
	{
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (getClass() != object.getClass())
			return false;

		final ProviderDimensionId that = (ProviderDimensionId) object;
		return new EqualsBuilder()
				.append(this.getProviderId(), that.getProviderId())
				.append(this.getProviderPath(), that.getProviderPath())
				.isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getProviderId()).append(
				this.getProviderPath()).toHashCode();
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public ProviderDimensionId copy()
	{
		final ProviderDimensionPK entity = new ProviderDimensionPK();
		entity.setProviderId(this.getProviderId());
		entity.setProviderPath(this.getProviderPath());
		return entity;
	}

}
