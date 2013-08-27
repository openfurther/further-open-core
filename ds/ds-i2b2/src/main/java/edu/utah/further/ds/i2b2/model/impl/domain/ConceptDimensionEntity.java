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

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension;

/**
 * Concept Dimension Entity
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
@Entity
@Table(name = "CONCEPT_DIMENSION")
public class ConceptDimensionEntity implements PersistentEntity<String>, ConceptDimension
{
	// ========================= CONSTANTS =================================

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4680976073995081638L;

	// ========================= FIELDS ====================================

	/**
	 * Concept Path in the i2b2 terminology tree.
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "CONCEPT_PATH")
	private String id;

	@Basic(optional = false)
	@Column(name = "CONCEPT_CD")
	private String conceptCd;
	@Column(name = "NAME_CHAR")
	private String nameChar;
	@Lob
	@Column(name = "CONCEPT_BLOB")
	private String conceptBlob;
	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	@Column(name = "DOWNLOAD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date downloadDate;
	@Column(name = "IMPORT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date importDate;
	@Column(name = "SOURCESYSTEM_CD")
	private String sourcesystemCd;
	@Column(name = "UPLOAD_ID")
	private BigInteger uploadId;

	// ========================= IMPL: ConceptDimension ====================

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#getId()
	 */
	@Override
	public String getId()
	{
		return id;
	}

	/**
	 * @param conceptPath
	 */
	public void setId(final String conceptPath)
	{
		this.id = conceptPath;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#getConceptCd()
	 */
	@Override
	public String getConceptCd()
	{
		return conceptCd;
	}

	/**
	 * @param conceptCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#setConceptCd(java.lang.String)
	 */
	@Override
	public void setConceptCd(final String conceptCd)
	{
		this.conceptCd = conceptCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#getNameChar()
	 */
	@Override
	public String getNameChar()
	{
		return nameChar;
	}

	/**
	 * @param nameChar
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#setNameChar(java.lang.String)
	 */
	@Override
	public void setNameChar(final String nameChar)
	{
		this.nameChar = nameChar;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#getConceptBlob()
	 */
	@Override
	public String getConceptBlob()
	{
		return conceptBlob;
	}

	/**
	 * @param conceptBlob
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#setConceptBlob(java.lang.String)
	 */
	@Override
	public void setConceptBlob(final String conceptBlob)
	{
		this.conceptBlob = conceptBlob;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#getUpdateDate()
	 */
	@Override
	public Date getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param updateDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#setUpdateDate(java.util.Date)
	 */
	@Override
	public void setUpdateDate(final Date updateDate)
	{
		this.updateDate = updateDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#getDownloadDate()
	 */
	@Override
	public Date getDownloadDate()
	{
		return downloadDate;
	}

	/**
	 * @param downloadDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#setDownloadDate(java.util.Date)
	 */
	@Override
	public void setDownloadDate(final Date downloadDate)
	{
		this.downloadDate = downloadDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#getImportDate()
	 */
	@Override
	public Date getImportDate()
	{
		return importDate;
	}

	/**
	 * @param importDate
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#setImportDate(java.util.Date)
	 */
	@Override
	public void setImportDate(final Date importDate)
	{
		this.importDate = importDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#getSourcesystemCd()
	 */
	@Override
	public String getSourcesystemCd()
	{
		return sourcesystemCd;
	}

	/**
	 * @param sourcesystemCd
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#setSourcesystemCd(java.lang.String)
	 */
	@Override
	public void setSourcesystemCd(final String sourcesystemCd)
	{
		this.sourcesystemCd = sourcesystemCd;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#getUploadId()
	 */
	@Override
	public BigInteger getUploadId()
	{
		return uploadId;
	}

	/**
	 * @param uploadId
	 * @see edu.utah.further.ds.i2b2.model.api.domain.ConceptDimension#setUploadId(java.math.BigInteger)
	 */
	@Override
	public void setUploadId(final BigInteger uploadId)
	{
		this.uploadId = uploadId;
	}

	// ========================= IMPL: Object ==============================

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

		final ConceptDimensionEntity that = (ConceptDimensionEntity) object;
		return new EqualsBuilder().append(this.getId(), that.getId()).isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.getId()).toHashCode();
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

	// ========================= IMPL: PubliclyCloneable ===================

	/**
	 * @return
	 * @see edu.utah.further.core.api.lang.PubliclyCloneable#copy()
	 */
	@Override
	public ConceptDimension copy()
	{
		final ConceptDimensionEntity entity = new ConceptDimensionEntity();
		entity.setId(this.getId());
		entity.setConceptCd(this.getConceptCd());
		entity.setNameChar(this.getNameChar());
		entity.setConceptBlob(this.getConceptBlob());
		entity.setUpdateDate(this.getUpdateDate());
		entity.setDownloadDate(this.getDownloadDate());
		entity.setImportDate(this.getImportDate());
		entity.setSourcesystemCd(this.getSourcesystemCd());
		entity.setUploadId(this.getUploadId());
		return entity;
	}
}
