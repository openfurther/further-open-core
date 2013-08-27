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

import edu.utah.further.ds.i2b2.model.api.domain.ProviderDimension;
import edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionIdTo;
import edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo;
import edu.utah.further.ds.i2b2.model.impl.domain.ProviderDimensionEntity;

/**
 * ProviderDimension To
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
@XmlRootElement(name = "ProviderDimension")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProviderDimension", propOrder =
{ "providerDimensionPK", "nameChar", "providerBlob", "updateDate", "downloadDate",
		"importDate", "sourcesystemCd", "uploadId" })
public class ProviderDimensionToImpl implements ProviderDimensionTo
{

	@XmlElement(required = true)
	private ProviderDimensionPKToImpl providerDimensionPK;
	@XmlElement(required = true)
	private String nameChar;
	@XmlElement(required = true)
	private String providerBlob;
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
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#getProviderDimensionPK()
	 */
	@Override
	public ProviderDimensionIdTo getProviderDimensionPK()
	{
		return providerDimensionPK;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#setProviderDimensionPK(edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionIdTo)
	 */
	@Override
	public void setProviderDimensionPK(final ProviderDimensionIdTo value)
	{
		this.providerDimensionPK = (ProviderDimensionPKToImpl) value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#getNameChar()
	 */
	@Override
	public String getNameChar()
	{
		return nameChar;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#setNameChar(java.lang.String)
	 */
	@Override
	public void setNameChar(final String value)
	{
		this.nameChar = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#getProviderBlob()
	 */
	@Override
	public String getProviderBlob()
	{
		return providerBlob;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#setProviderBlob(java.lang.String)
	 */
	@Override
	public void setProviderBlob(final String value)
	{
		this.providerBlob = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#getUpdateDate()
	 */
	@Override
	public XMLGregorianCalendar getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#setUpdateDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setUpdateDate(final XMLGregorianCalendar value)
	{
		this.updateDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#getDownloadDate()
	 */
	@Override
	public XMLGregorianCalendar getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#setDownloadDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setDownloadDate(final XMLGregorianCalendar value)
	{
		this.downloadDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#getImportDate()
	 */
	@Override
	public XMLGregorianCalendar getImportDate()
	{
		return importDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#setImportDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setImportDate(final XMLGregorianCalendar value)
	{
		this.importDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(final String value)
	{
		this.sourcesystemCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#getUploadId()
	 */
	@Override
	public String getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ProviderDimensionTo#setUploadId(java.lang.String)
	 */
	@Override
	public void setUploadId(final String value)
	{
		this.uploadId = value;
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

		final ProviderDimensionTo that = (ProviderDimensionTo) object;
		return new EqualsBuilder().append(this.getProviderDimensionPK(),
				that.getProviderDimensionPK()).isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getProviderDimensionPK()).toHashCode();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public ProviderDimension copy()
	{
		final ProviderDimensionEntity entity = new ProviderDimensionEntity();
		entity.setDownloadDate(this.getDownloadDate() != null ? this
				.getDownloadDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setId(this.getProviderDimensionPK() != null ? this
				.getProviderDimensionPK()
				.copy() : null);
		entity.setImportDate(this.getImportDate() != null ? this
				.getImportDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setNameChar(this.getNameChar());
		entity.setProviderBlob(this.getProviderBlob());
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setUpdateDate(this.getUpdateDate() != null ? this
				.getUpdateDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setUploadId(newBigIntegerNullSafe(this.getUploadId()));
		return entity;
	}

}
