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
package edu.utah.further.mdr.api.domain.asset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Alias;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * Resource storage type codes. Correspond to column names in the MDR resource table.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Alias("storageCode")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.MDR, name = StorageCode.ENTITY_NAME)
@XmlRootElement(namespace = XmlNamespace.MDR, name = StorageCode.ENTITY_NAME)
public enum StorageCode
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Text resource.
	 */
	RESOURCE_TEXT(false)
	{
	},

	/**
	 * XML document.
	 */
	RESOURCE_XML(false)
	{
	},

	/**
	 * A large text object.
	 */
	RESOURCE_CLOB(false)
	{
	},

	/**
	 * A large binary object.
	 */
	RESOURCE_BLOB(false)
	{
	},

	/**
	 * Resource is stored at a remote URL.
	 */
	RESOURCE_URL(true)
	{
	};

	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "storageCode";

	// ========================= FIELDS ====================================

	/**
	 * Is this a remote resource to the MDR.
	 */
	private final boolean remote;

	/**
	 * @param remote
	 */
	private StorageCode(final boolean remote)
	{
		this.remote = remote;
	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Return the remote property.
	 * 
	 * @return the remote
	 */
	public boolean isRemote()
	{
		return remote;
	}

	// ========================= METHODS ===================================
}
