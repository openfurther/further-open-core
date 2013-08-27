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
package edu.utah.further.ds.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

import edu.utah.further.core.api.time.TimeService;
import edu.utah.further.ds.api.domain.LogicalQuery;

/**
 * Entity which contains the logical SearchQuery and the target namespace for term
 * translation. This entity represents the data needed to "request" a term translation.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Nov 24, 2009
 */
@Entity
@NamedNativeQuery(name = LogicalQuery.BUILD_QUERY, query = "call GET_PHYSICAL_QUERY(?, :parameters)", callable = true, readOnly = true, resultClass = PhysicalQueryEntity.class)
@Table(name = "QUERY_DEF")
public class LogicalQueryEntity implements LogicalQuery
{
	// ========================= CONSTANTS =================================

	/**
	 * Generated Serial UID
	 */
	private static final long serialVersionUID = 2393681853791287718L;

	/**
	 * The id of the query.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUERY_ID_SEQ")
	@SequenceGenerator(name = "QUERY_ID_SEQ", sequenceName = "QUERY_ID_SEQ")
	@Column(name = "query_id")
	private Long id;

	/**
	 * The name of the query.
	 */
	@Column(name = "query_nm")
	private String queryName;

	/**
	 * The query xml.
	 */
	@Column(name = "query_xml")
	@Type(type = "xml-type")
	private Document queryXml;

	/**
	 * The create date of this "request".
	 */
	@Column(name = "create_dts")
	private Date createDate = null;

	/**
	 * The user id of who requested this term translation.
	 */
	@Column(name = "created_by_user_id")
	private String createUserId;

	// ========================= IMPL: PersistentEntity ========================

	/**
	 * @return
	 * @see edu.utah.further.ds.api.domain.LogicalQuery#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	// ========================= GET/SET =================================

	/**
	 * @return
	 * @see edu.utah.further.ds.api.domain.LogicalQuery#getQueryXml()
	 */
	@Override
	public Document getQueryXml()
	{
		return queryXml;
	}

	/**
	 * @param queryXml
	 * @see edu.utah.further.ds.api.domain.LogicalQuery#setQueryXml(org.w3c.dom.Document)
	 */
	@Override
	public void setQueryXml(final Document queryXml)
	{
		this.queryXml = queryXml;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.api.domain.LogicalQuery#getQueryName()
	 */
	@Override
	public String getQueryName()
	{
		return queryName;
	}

	/**
	 * @param queryName
	 * @see edu.utah.further.ds.api.domain.LogicalQuery#setQueryName(java.lang.String)
	 */
	@Override
	public void setQueryName(final String queryName)
	{
		this.queryName = queryName;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.api.domain.LogicalQuery#getCreateDate()
	 */
	@Override
	public Date getCreateDate()
	{
		return (createDate != null) ? createDate : TimeService.getDate();
	}

	/**
	 * @param createDate
	 * @see edu.utah.further.ds.api.domain.LogicalQuery#setCreateDate(java.util.Date)
	 */
	@Override
	public void setCreateDate(final Date createDate)
	{
		this.createDate = createDate;
	}

	/**
	 * @return
	 * @see edu.utah.further.ds.api.domain.LogicalQuery#getCreateUserId()
	 */
	@Override
	public String getCreateUserId()
	{
		return createUserId;
	}

	/**
	 * @param createUserId
	 * @see edu.utah.further.ds.api.domain.LogicalQuery#setCreateUserId(java.lang.String)
	 */
	@Override
	public void setCreateUserId(final String createUserId)
	{
		this.createUserId = createUserId;
	}

}
