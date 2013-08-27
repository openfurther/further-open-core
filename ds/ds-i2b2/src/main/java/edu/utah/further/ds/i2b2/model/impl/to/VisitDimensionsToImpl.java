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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo;
import edu.utah.further.ds.i2b2.model.api.to.VisitDimensionsTo;

/**
 * Visit Dimension To
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
@XmlRootElement(name = "VisitDimensions")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VisitDimensions", propOrder =
{ "visitDimension" })
public class VisitDimensionsToImpl implements VisitDimensionsTo
{

	@XmlElement(name = "VisitDimension")
	private List<VisitDimensionToImpl> visitDimension;

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionsTo#getVisitDimension()
	 */
	@Override
	public List<VisitDimensionTo> getVisitDimension()
	{
		return this.visitDimension != null ? CollectionUtil
				.<VisitDimensionTo> newList(this.visitDimension) : CollectionUtil
				.<VisitDimensionTo> newList();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object)
	{
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (getClass() != object.getClass())
			return false;

		final VisitDimensionsTo that = (VisitDimensionsTo) object;
		return new EqualsBuilder().append(this.getVisitDimension(),
				that.getVisitDimension()).isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getVisitDimension()).toHashCode();
	}

}
