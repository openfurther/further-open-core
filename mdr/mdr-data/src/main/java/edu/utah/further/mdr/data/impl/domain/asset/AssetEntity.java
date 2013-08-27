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
package edu.utah.further.mdr.data.impl.domain.asset;

import static edu.utah.further.core.api.collections.CollectionUtil.newSortedSet;

import java.util.SortedSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.Version;
import edu.utah.further.mdr.data.common.domain.asset.AbstractAssetEntity;
import edu.utah.further.mdr.data.common.domain.asset.AbstractVersionEntity;

/**
 * An MDR asset persistent entity.
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
 * @version Mar 19, 2009
 */
// ============================
// Hibernate annotations
// ============================
@Entity
@Table(name = "ASSET")
public class AssetEntity extends AbstractAssetEntity
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS: ASSOCIATIONS ======================

	/**
	 * Link to this asset's namespace entity.
	 */
	@OneToOne
	@JoinColumn(name = "ASSET_NAMESPACE_ASSET_ID")
	private AssetEntity namespace;

	/**
	 * Link to this asset's asset type entity.
	 */
	@OneToOne
	@JoinColumn(name = "ASSET_TYPE_ASSET_ID")
	private AssetEntity type;

	/**
	 * Set of versions of this asset. Only one can be active at any given time.
	 */
	@OneToMany(mappedBy = "asset", fetch = FetchType.EAGER, targetEntity = VersionEntity.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Version> versionSet = newSortedSet();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for Hibernate.
	 */
	public AssetEntity()
	{
		super();
	}

	/**
	 * A copy constructor.
	 * 
	 * @param other
	 *            other entity to copy from
	 */
	public AssetEntity(final Asset other)
	{
		super(other);
	}

	// ========================= IMPLEMENTATION: Asset =====================

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getNamespace()
	 */
	@Override
	public Asset getNamespace()
	{
		return namespace;
	}

	/**
	 * Set a new namespace association.
	 * 
	 * @param namespace
	 *            must be of type {@link AssetEntity}!
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#setNamespace(edu.utah.further.mdr.api.domain.asset.Asset)
	 */
	@Override
	public void setNamespace(final Asset namespace)
	{
		this.namespace = (AssetEntity) namespace;
	}

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getType()
	 */
	@Override
	public Asset getType()
	{
		return type;
	}

	/**
	 * Set a new asset type association.
	 * 
	 * @param type
	 *            must be of type {@link AssetEntity}!
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#setType(edu.utah.further.mdr.api.domain.asset.Asset)
	 */
	@Override
	public void setType(final Asset type)
	{
		this.type = (AssetEntity) type;
	}

	// ========================= IMPL: AbstractAssetEntity =================

	/**
	 * Must return an actual reference to the version set, not a defensive copy.
	 * 
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Asset#getVersionSet()
	 */
	@Override
	protected SortedSet<Version> getVersionSetInternal()
	{
		return versionSet;
	}

	/**
	 * A factory method of an empty version entity, of the type associated with this asset
	 * entity.
	 * 
	 * @return empty version entity instance
	 */
	@Override
	protected AbstractVersionEntity newVersionEntity()
	{
		return new VersionEntity();
	}

}
