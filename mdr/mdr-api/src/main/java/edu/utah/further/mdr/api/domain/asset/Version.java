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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.CopyableFrom;

/**
 * An MDR asset version persistent entity.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Mar 20, 2009
 */
public interface Version extends PersistentEntity<Long>, Comparable<Version>,
		CopyableFrom<Version, Version>
{
	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Result of comparing two lookup values. Values are ordered by increasing
	 * <code>version</code> value.
	 *
	 * @param other
	 *            the other {@link Version} object
	 * @return the result of comparison
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	int compareTo(Version other);

	// ========================= METHODS ===================================

	/**
	 * Returns a deep-copy of the argument into this object. This object is usually
	 * constructed with a no-argument constructor first, and then this method is called to
	 * copy fields into it. Note: the identifier is <i>not</i> copied.
	 *
	 * @param other
	 *            object to copy
	 * @return this object, for chaining
	 * @see edu.utah.further.core.misc.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	Version copyFrom(Version other);

	/**
	 * Return the asset property.
	 *
	 * @return the asset
	 */
	Asset getAsset();

	/**
	 * Set a new value for the asset property.
	 *
	 * @param asset
	 *            the asset to set
	 */
	void setAsset(Asset asset);

	/**
	 * Return the version property.
	 *
	 * @return the version
	 */
	Long getVersion();

	/**
	 * Set a new value for the version property.
	 *
	 * @param version
	 *            the version to set
	 */
	void setVersion(Long version);

	/**
	 * Return the description property.
	 *
	 * @return the description
	 */
	String getUpdateDescription();

	/**
	 * Set a new value for the description property.
	 *
	 * @param description
	 *            the description to set
	 */
	void setUpdateDescription(String description);

	/**
	 * Return the resourceSet property.
	 *
	 * @return the resourceSet
	 */
	Set<Resource> getResourceSet();

	/**
	 * Set a new value for the resourceSet property.
	 *
	 * @param resourceSet
	 *            the resourceSet to set
	 */
	void setResourceSet(Set<? extends Resource> resourceSet);

	/**
	 * @param e
	 * @return
	 * @see java.util.Set#add(java.lang.Object)
	 */
	void addResource(Resource e);

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	void addResources(Collection<? extends Resource> c);

	/**
	 * @param o
	 * @return
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	void removeResource(Resource o);

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#removeAll(java.util.Collection)
	 */
	void removeResources(Collection<? extends Resource> c);

	/**
	 * Return the updatedDate property.
	 *
	 * @return the updatedDate
	 */
	Timestamp getUpdatedDate();

	/**
	 * Set a new value for the updatedDate property.
	 *
	 * @param updatedDate
	 *            the updatedDate to set
	 */
	void setUpdatedDate(Timestamp updatedDate);

	/**
	 * Return the updatedByUserId property.
	 *
	 * @return the updatedByUserId
	 */
	String getUpdatedByUserId();

	/**
	 * Set a new value for the updatedByUserId property.
	 *
	 * @param updatedByUserId
	 *            the updatedByUserId to set
	 */
	void setUpdatedByUserId(String updatedByUserId);

	/**
	 * Return the propertiesXml property.
	 *
	 * @return the propertiesXml
	 */
	String getPropertiesXml();

	/**
	 * Set a new value for the propertiesXml property.
	 *
	 * @param propertiesXml
	 *            the propertiesXml to set
	 */
	void setPropertiesXml(String propertiesXml);

	/**
	 * Return the status property.
	 *
	 * @return the status
	 */
	LookupValue getStatus();

	/**
	 * Set a new value for the status property.
	 *
	 * @param status
	 *            the status to set
	 */
	void setStatus(LookupValue status);
}