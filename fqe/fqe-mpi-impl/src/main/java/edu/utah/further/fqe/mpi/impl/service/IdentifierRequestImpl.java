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
package edu.utah.further.fqe.mpi.impl.service;

import edu.utah.further.fqe.mpi.api.Identifier;

/**
 * Simple POJO that represents an MPI identifier. It's mainly used to pass around an
 * identifier
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
 * @version Nov 1, 2011
 */
public final class IdentifierRequestImpl implements Identifier
{
	// ========================= CONSTANTS ===================================

	// ========================= FIELDS ===================================

	/**
	 * Name to generate for
	 */
	private String name;

	/**
	 * Attribute to generate for
	 */
	private String attr;

	/**
	 * The source data source namespace id
	 */
	private long sourceNamespaceId;

	/**
	 * Source name to generate for
	 */
	private String sourceName;

	/**
	 * Source attribute to generate for
	 */
	private String sourceAttr;

	/**
	 * Source obj id to generate for
	 */
	private String sourceId;

	/**
	 * Query identifier for this request
	 */
	private String queryId;

	// ========================= GET/SET =================

	/**
	 * Return the name property.
	 * 
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Return the attr property.
	 * 
	 * @return the attr
	 */
	@Override
	public String getAttr()
	{
		return attr;
	}

	/**
	 * Set a new value for the attr property.
	 * 
	 * @param attr
	 *            the attr to set
	 */
	public void setAttr(final String attr)
	{
		this.attr = attr;
	}

	/**
	 * Return the sourceNamespaceId property.
	 * 
	 * @return the sourceNamespaceId
	 */
	@Override
	public long getSourceNamespaceId()
	{
		return sourceNamespaceId;
	}

	/**
	 * Set a new value for the sourceNamespaceId property.
	 * 
	 * @param sourceNamespaceId
	 *            the sourceNamespaceId to set
	 */
	public void setSourceNamespaceId(final long sourceNamespaceId)
	{
		this.sourceNamespaceId = sourceNamespaceId;
	}

	/**
	 * Return the sourceName property.
	 * 
	 * @return the sourceName
	 */
	@Override
	public String getSourceName()
	{
		return sourceName;
	}

	/**
	 * Set a new value for the sourceName property.
	 * 
	 * @param sourceName
	 *            the sourceName to set
	 */
	public void setSourceName(final String sourceName)
	{
		this.sourceName = sourceName;
	}

	/**
	 * Return the sourceAttr property.
	 * 
	 * @return the sourceAttr
	 */
	@Override
	public String getSourceAttr()
	{
		return sourceAttr;
	}

	/**
	 * Set a new value for the sourceAttr property.
	 * 
	 * @param sourceAttr
	 *            the sourceAttr to set
	 */
	public void setSourceAttr(final String sourceAttr)
	{
		this.sourceAttr = sourceAttr;
	}

	/**
	 * Return the sourceId property.
	 * 
	 * @return the sourceId
	 */
	@Override
	public String getSourceId()
	{
		return sourceId;
	}

	/**
	 * Set a new value for the sourceId property.
	 * 
	 * @param sourceId
	 *            the sourceId to set
	 */
	public void setSourceId(final String sourceId)
	{
		this.sourceId = sourceId;
	}

	/**
	 * Return the queryId property.
	 * 
	 * @return the queryId
	 */
	@Override
	public String getQueryId()
	{
		return queryId;
	}

	/**
	 * Set a new value for the queryId property.
	 * 
	 * @param queryId
	 *            the queryId to set
	 */
	public void setQueryId(final String queryId)
	{
		this.queryId = queryId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.mpi.api.Identifier#getVirtualId()
	 */
	@Override
	public Long getVirtualId()
	{
		throw new UnsupportedOperationException(
				"Identifier requests do not contain virtual identifiers");
	}

}
