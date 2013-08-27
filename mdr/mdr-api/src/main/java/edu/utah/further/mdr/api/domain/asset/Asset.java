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
import java.util.SortedSet;

import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.CopyableFrom;

/**
 * An MDR asset abstraction.
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
public interface Asset extends PersistentEntity<Long>, MutableActivationInfo,
		CopyableFrom<Asset, Asset>, Labeled
{
	// ========================= METHODS ===================================

	/**
	 * Return the namespace property.
	 *
	 * @return the namespace
	 */
	Asset getNamespace();

	/**
	 * Set a new value for the namespace property.
	 *
	 * @param namespace
	 *            the namespace to set
	 */
	void setNamespace(Asset namespace);

	/**
	 * Return the type property.
	 *
	 * @return the type
	 */
	Asset getType();

	/**
	 * Set a new value for the type property.
	 *
	 * @param type
	 *            the type to set
	 */
	void setType(Asset type);

	/**
	 * Set a new value for the label property.
	 *
	 * @param label
	 *            the label to set
	 */
	void setLabel(String label);

	/**
	 * Return the description property.
	 *
	 * @return the description
	 */
	String getDescription();

	/**
	 * Set a new value for the description property.
	 *
	 * @param description
	 *            the description to set
	 */
	void setDescription(String description);

	/**
	 * Return the activationInfo property.
	 *
	 * @return the activationInfo
	 */
	ActivationInfo getActivationInfo();

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getActivationDate()
	 */
	@Override
	Timestamp getActivationDate();

	/**
	 * @param activationDate
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setActivationDate(java.sql.Timestamp)
	 */
	@Override
	void setActivationDate(Timestamp activationDate);

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getDeactivationDate()
	 */
	@Override
	Timestamp getDeactivationDate();

	/**
	 * @param deactivationDate
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#setDeactivationDate(java.sql.Timestamp)
	 */
	@Override
	void setDeactivationDate(Timestamp deactivationDate);

	/**
	 * Return the versionSet property.
	 *
	 * @return the versionSet
	 */
	SortedSet<Version> getVersionSet();

	/**
	 * Set a new value for the versionSet property.
	 *
	 * @param versionSet
	 *            the versionSet to set
	 */
	void setVersionSet(SortedSet<? extends Version> versionSet);

	/**
	 * @param e
	 * @return
	 * @see java.util.Set#add(java.lang.Object)
	 */
	void addVersion(Version e);

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	void addVersions(Collection<? extends Version> c);

	/**
	 * @param o
	 * @return
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	void removeVersion(Version o);

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#removeAll(java.util.Collection)
	 */
	void removeVersions(Collection<? extends Version> c);

}