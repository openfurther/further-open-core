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
package edu.utah.further.ds.i2b2.model.impl.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.ds.i2b2.model.api.domain.ProviderDimensionId;
import edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionIdTo;
import edu.utah.further.ds.i2b2.model.impl.domain.ProviderDimensionPK;

/**
 * Provider Dimension PK To
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author {@code Rick L. Bradshaw <rick.bradshaw@utah.edu>}
 * @version April 15, 2010
 */
@XmlRootElement(name = "ProviderDimensionPK")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProviderDimensionPK", propOrder =
{ "providerId", "providerPath" })
public class ProviderDimensionPKToImpl implements ProviderDimensionIdTo
{

	@XmlElement(required = true)
	private String providerId;
	@XmlElement(required = true)
	private String providerPath;

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionIdTo#getProviderId()
	 */
	@Override
	public String getProviderId()
	{
		return providerId;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionIdTo#setProviderId(java.lang.String)
	 */
	@Override
	public void setProviderId(String value)
	{
		this.providerId = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionIdTo#getProviderPath()
	 */
	@Override
	public String getProviderPath()
	{
		return providerPath;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionIdTo#setProviderPath(java.lang.String)
	 */
	@Override
	public void setProviderPath(String value)
	{
		this.providerPath = value;
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
