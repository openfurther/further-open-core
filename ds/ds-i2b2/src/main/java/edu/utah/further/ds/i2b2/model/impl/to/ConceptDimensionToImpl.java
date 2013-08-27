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

import edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension;
import edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo;
import edu.utah.further.ds.i2b2.model.impl.domain.ConceptDimensionEntity;

/**
 * Concept Dimension To
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
@XmlRootElement(name = "ConceptDimension")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConceptDimension", propOrder =
{ "conceptCd", "conceptPath", "nameChar", "conceptBlob", "updateDate", "downloadDate",
		"importDate", "sourcesystemCd", "uploadId" })
public class ConceptDimensionToImpl implements ConceptDimensionTo
{
	@XmlElement(required = true)
	private String conceptCd;
	@XmlElement(required = true)
	private String conceptPath;
	@XmlElement(required = true)
	private String nameChar;
	@XmlElement(required = true)
	private String conceptBlob;
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
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#getConceptCd()
	 */
	@Override
	public String getConceptCd()
	{
		return conceptCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#setConceptCd(java.lang.String)
	 */
	@Override
	public void setConceptCd(final String value)
	{
		this.conceptCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#getConceptPath()
	 */
	@Override
	public String getConceptPath()
	{
		return conceptPath;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#setConceptPath(java.lang.String)
	 */
	@Override
	public void setConceptPath(final String value)
	{
		this.conceptPath = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#getNameChar()
	 */
	@Override
	public String getNameChar()
	{
		return nameChar;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#setNameChar(java.lang.String)
	 */
	@Override
	public void setNameChar(final String value)
	{
		this.nameChar = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#getConceptBlob()
	 */
	@Override
	public String getConceptBlob()
	{
		return conceptBlob;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#setConceptBlob(java.lang.String)
	 */
	@Override
	public void setConceptBlob(final String value)
	{
		this.conceptBlob = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#getUpdateDate()
	 */
	@Override
	public XMLGregorianCalendar getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#setUpdateDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setUpdateDate(final XMLGregorianCalendar value)
	{
		this.updateDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#getDownloadDate()
	 */
	@Override
	public XMLGregorianCalendar getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#setDownloadDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setDownloadDate(final XMLGregorianCalendar value)
	{
		this.downloadDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#getImportDate()
	 */
	@Override
	public XMLGregorianCalendar getImportDate()
	{
		return importDate;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#setImportDate(javax.xml.datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setImportDate(final XMLGregorianCalendar value)
	{
		this.importDate = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(final String value)
	{
		this.sourcesystemCd = value;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#getUploadId()
	 */
	@Override
	public String getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param value
	 * @see edu.utah.further.ds.i2b2.model.api.to.ConceptDimensionTo#setUploadId(java.lang.String)
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

		final ConceptDimensionTo that = (ConceptDimensionTo) object;
		return new EqualsBuilder()
				.append(this.getConceptPath(), that.getConceptPath())
				.isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getConceptPath()).toHashCode();
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public ConceptDimension copy()
	{
		final ConceptDimensionEntity entity = new ConceptDimensionEntity();
		entity.setId(this.getConceptPath());
		entity.setConceptCd(this.getConceptCd());
		entity.setNameChar(this.getNameChar());
		entity.setConceptBlob(this.getConceptBlob());
		entity.setUpdateDate(this.getUpdateDate() != null ? this
				.getUpdateDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setDownloadDate(this.getDownloadDate() != null ? this
				.getDownloadDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setImportDate(this.getImportDate() != null ? this
				.getImportDate()
				.toGregorianCalendar()
				.getTime() : null);
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setUploadId(newBigIntegerNullSafe(this.getUploadId()));
		return entity;
	}

}
