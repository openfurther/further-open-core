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
package edu.utah.further.mdr.impl.service.asset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.AssetAssociation;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.ResourceType;
import edu.utah.further.mdr.api.service.asset.AssetService;
import edu.utah.further.mdr.api.to.asset.AttributeTranslationResultTo;

/**
 * A hybrid implementation that utilizes the classpath to fetch resources by path but uses
 * another asset service implementation (e.g. a database driven implementation) to fetch
 * asset associations.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 8, 2013
 */
@Service("assetServiceHybrid")
public class AssetServiceHybridClasspathImpl extends AssetServiceClasspathImpl
{

	// ========================= DEPENDENCIES ==============================

	/**
	 * A concrete asset service connected to a MDR.
	 */
	@Autowired
	private AssetService assetService;

	// ========================= OVERRIDDEN ==============================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#
	 * getAttributeTranslation(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<AttributeTranslationResultTo> getAttributeTranslation(
			final String sourceAttribute, final String sourceNamespace,
			final String targetNamespace)
	{
		return assetService.getAttributeTranslation(sourceAttribute, sourceNamespace,
				targetNamespace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#findAssetById
	 * (java.lang.Long)
	 */
	@Override
	public Asset findAssetById(final Long id)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#findAssetsByLikeLabel
	 * (java.lang.String)
	 */
	@Override
	public List<Asset> findAssetsByLikeLabel(final String label)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#findResourceById
	 * (java.lang.Long)
	 */
	@Override
	public Resource findResourceById(final Long id)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#
	 * findResourcesByLikeTypeLabel(java.lang.String)
	 */
	@Override
	public List<Resource> findResourcesByLikeTypeLabel(final String typeLabel)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#findResourcesByTypeId
	 * (java.lang.Long)
	 */
	@Override
	public List<Resource> findResourcesByTypeId(final Long typeId)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#
	 * findResourceTypesByLikeTypeLabel(java.lang.String)
	 */
	@Override
	public List<ResourceType> findResourceTypesByLikeTypeLabel(final String typeLabel)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#
	 * getAttributeTranslation(java.lang.String, int, int)
	 */
	@Override
	public List<AttributeTranslationResultTo> getAttributeTranslation(
			final String sourceAttribute, final int sourceNamespaceId,
			final int targetNamespaceId)
	{
		return assetService.getAttributeTranslation(sourceAttribute, sourceNamespaceId,
				targetNamespaceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#
	 * getUniqueAttributeTranslation(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AttributeTranslationResultTo getUniqueAttributeTranslation(
			final String sourceAttribute, final String sourceNamespace,
			final String targetNamespace)
	{
		return assetService.getUniqueAttributeTranslation(sourceAttribute,
				sourceNamespace, targetNamespace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#
	 * getUniqueAttributeTranslation(java.lang.String, int, int)
	 */
	@Override
	public AttributeTranslationResultTo getUniqueAttributeTranslation(
			final String sourceAttribute, final int sourceNamespaceId,
			final int targetNamespaceId)
	{
		return assetService.getUniqueAttributeTranslation(sourceAttribute,
				sourceNamespaceId, targetNamespaceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#
	 * findAssetAssocationById(java.lang.Long)
	 */
	@Override
	public AssetAssociation findAssetAssocationById(final Long assetAssocationId)
	{
		return assetService.findAssetAssocationById(assetAssocationId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#findAssetAssociation
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public List<AssetAssociation> findAssetAssociation(final String side,
			final String label)
	{
		return assetService.findAssetAssociation(side, label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.impl.service.asset.AssetServiceClasspathImpl#findAssetAssociation
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<AssetAssociation> findAssetAssociation(final String side,
			final String label, final String association)
	{
		return assetService.findAssetAssociation(side, label, association);
	}

	// ========================= GET/SET ==============================

	/**
	 * Return the assetService property.
	 * 
	 * @return the assetService
	 */
	public AssetService getAssetService()
	{
		return assetService;
	}

	/**
	 * Set a new value for the assetService property.
	 * 
	 * @param assetService
	 *            the assetService to set
	 */
	public void setAssetService(final AssetService assetService)
	{
		this.assetService = assetService;
	}

}
