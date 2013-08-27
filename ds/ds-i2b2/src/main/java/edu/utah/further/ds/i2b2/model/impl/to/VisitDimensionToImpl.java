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

import static edu.utah.further.core.api.math.ArithmeticUtil.newBigIntegerNullSafe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.utah.further.ds.i2b2.model.api.domain.VisitDimension;
import edu.utah.further.ds.i2b2.model.api.to.VisitDimensionIdTo;
import edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo;
import edu.utah.further.ds.i2b2.model.impl.domain.VisitDimensionEntity;

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
@XmlRootElement(name = "VisitDimension")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VisitDimension", propOrder =
{ "visitDimensionPK", "activeStatusCd", "startDate", "endDate", "inoutCd", "locationCd",
		"locationPath", "visitBlob", "updateDate", "downloadDate", "importDate",
		"sourcesystemCd", "uploadId" })
public class VisitDimensionToImpl implements VisitDimensionTo
{

	@XmlElement(required = true)
	private VisitDimensionPKToImpl visitDimensionPK;
	@XmlElement(required = true)
	private String activeStatusCd;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar startDate;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar endDate;
	@XmlElement(required = true)
	private String inoutCd;
	@XmlElement(required = true)
	private String locationCd;
	@XmlElement(required = true)
	private String locationPath;
	@XmlElement(required = true)
	private String visitBlob;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar updateDate;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar downloadDate;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	private XMLGregorianCalendar importDate;
	@XmlElement(required = true)
	private String sourcesystemCd;
	@XmlElement(required = true)
	private String uploadId;

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getVisitDimensionPK()
	 */
	@Override
	public VisitDimensionIdTo getVisitDimensionPK()
	{
		return visitDimensionPK;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setVisitDimensionPK(edu.utah.further.ds.i2b2.model.api.to.VisitDimensionIdTo)
	 */
	@Override
	public void setVisitDimensionPK(final VisitDimensionIdTo value)
	{
		this.visitDimensionPK = (VisitDimensionPKToImpl) value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getActiveStatusCd()
	 */
	@Override
	public String getActiveStatusCd()
	{
		return activeStatusCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setActiveStatusCd(java.lang.String)
	 */
	@Override
	public void setActiveStatusCd(final String value)
	{
		this.activeStatusCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getStartDate()
	 */
	@Override
	public XMLGregorianCalendar getStartDate()
	{
		return startDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setStartDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setStartDate(final XMLGregorianCalendar value)
	{
		this.startDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getEndDate()
	 */
	@Override
	public XMLGregorianCalendar getEndDate()
	{
		return endDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setEndDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setEndDate(final XMLGregorianCalendar value)
	{
		this.endDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getInoutCd()
	 */
	@Override
	public String getInoutCd()
	{
		return inoutCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setInoutCd(java.lang.String)
	 */
	@Override
	public void setInoutCd(final String value)
	{
		this.inoutCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getLocationCd()
	 */
	@Override
	public String getLocationCd()
	{
		return locationCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setLocationCd(java.lang.String)
	 */
	@Override
	public void setLocationCd(final String value)
	{
		this.locationCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getLocationPath()
	 */
	@Override
	public String getLocationPath()
	{
		return locationPath;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setLocationPath(java.lang.String)
	 */
	@Override
	public void setLocationPath(final String value)
	{
		this.locationPath = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getVisitBlob()
	 */
	@Override
	public String getVisitBlob()
	{
		return visitBlob;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setVisitBlob(java.lang.String)
	 */
	@Override
	public void setVisitBlob(final String value)
	{
		this.visitBlob = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getUpdateDate()
	 */
	@Override
	public XMLGregorianCalendar getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setUpdateDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setUpdateDate(final XMLGregorianCalendar value)
	{
		this.updateDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getDownloadDate()
	 */
	@Override
	public XMLGregorianCalendar getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setDownloadDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setDownloadDate(final XMLGregorianCalendar value)
	{
		this.downloadDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getImportDate()
	 */
	@Override
	public XMLGregorianCalendar getImportDate()
	{
		return importDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setImportDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setImportDate(final XMLGregorianCalendar value)
	{
		this.importDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(final String value)
	{
		this.sourcesystemCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#getUploadId()
	 */
	@Override
	public String getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.VisitDimensionTo#setUploadId(java.lang.String)
	 */
	@Override
	public void setUploadId(final String value)
	{
		this.uploadId = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.i2b2.model.api.to.VisitDimensionTo#toEntity()
	 */

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

		final VisitDimensionTo that = (VisitDimensionTo) object;
		return new EqualsBuilder().append(this.getVisitDimensionPK(),
				that.getVisitDimensionPK()).isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getVisitDimensionPK()).toHashCode();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public VisitDimension copy()
	{
		final VisitDimensionEntity entity = new VisitDimensionEntity();
		entity.setActiveStatusCd(this.getActiveStatusCd());
		entity.setDownloadDate(this.getDownloadDate() != null ? this
				.getDownloadDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setEndDate(this.getEndDate() != null ? this
				.getEndDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setId(this.getVisitDimensionPK() != null ? this
				.getVisitDimensionPK()
				.copy() : null);
		entity.setImportDate(this.getImportDate() != null ? this
				.getImportDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setInoutCd(this.getInoutCd());
		entity.setLocationCd(this.getLocationCd());
		entity.setLocationPath(this.getLocationPath());
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setStartDate(this.getStartDate() != null ? this
				.getStartDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setUpdateDate(this.getUpdateDate() != null ? this
				.getUpdateDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setUploadId(newBigIntegerNullSafe(this.getUploadId()));
		entity.setVisitBlob(this.getVisitBlob());
		return entity;
	}

}
