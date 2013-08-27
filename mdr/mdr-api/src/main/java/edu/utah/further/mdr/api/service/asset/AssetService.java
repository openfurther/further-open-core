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
package edu.utah.further.mdr.api.service.asset;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.mdr.api.annotation.Filtered;
import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.AssetAssociation;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.ResourceType;
import edu.utah.further.mdr.api.service.transform.type.DocumentType;
import edu.utah.further.mdr.api.service.transform.type.TransformType;
import edu.utah.further.mdr.api.to.asset.AttributeTranslationResultTo;

/**
 * Centralizes MDR {@link Asset} entity CRUD services.
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
 * @version Mar 23, 2009
 */
public interface AssetService
{
	// ========================= METHODS: ASSETS ===========================

	/**
	 * Find an asset by unique identifier.
	 * 
	 * @param id
	 *            asset unique identifier to look for
	 * @return persistent asset domain entity, if found. If not found, returns
	 *         <code>null</code>
	 */
	Asset findAssetById(Long id);

	// /**
	// * Find assets that match a asset type identifier.
	// *
	// * @param typeId
	// * asset type identifier to look for
	// * @return list matching assets
	// */
	// List<Asset> findAssetsByTypeId(Long typeId);

	/**
	 * Find assets that match (LIKE) an asset label.
	 * 
	 * @param label
	 *            asset label (description) substring
	 * @return list matching resources
	 */
	List<Asset> findAssetsByLikeLabel(String label);

	// ========================= METHODS: RESOURCES (lazy-initialized, find*() methods)

	/**
	 * Find a resource by unique identifier.
	 * 
	 * @param id
	 *            resource unique identifier to look for
	 * @return persistent asset domain entity, if found. If not found, returns
	 *         <code>null</code>
	 */
	@Filtered
	Resource findResourceById(Long id);

	/**
	 * Find resources that match a resource type identifier.
	 * 
	 * @param typeId
	 *            resource type identifier to look for
	 * @return list matching resources
	 */
	@Filtered
	List<Resource> findResourcesByTypeId(Long typeId);

	/**
	 * Find resources that match (LIKE) a resource type label.
	 * 
	 * @param typeLabel
	 *            resource type label (description) substring
	 * @return list matching resources
	 */
	@Filtered
	List<Resource> findResourcesByLikeTypeLabel(String typeLabel);

	/**
	 * Find resource types that match (LIKE) a resource type label.
	 * 
	 * @param typeLabel
	 *            resource type label (description) substring
	 * @return list matching resource types
	 */
	List<ResourceType> findResourceTypesByLikeTypeLabel(String typeLabel);

	// ========================= METHODS: Eagerly-fetched resources (get*() methods)

	/**
	 * Return the currently active resource as an {@link InputStream} with a specified MDR
	 * path. Implementations must always eagerly fetch the resource's storage fields here.
	 * <p>
	 * The returned resource's storage field will be filtered according to the resource's
	 * type. Currently supported rules:
	 * <ul>
	 * <li>If resource type = {@link MdrNames#XQUERY}, filter place-holders
	 * 
	 * @param path
	 *            MDR path
	 * @return active resource, if found. If not found, returns <code>null</code>
	 */
	@Filtered
	InputStream getActiveResourceInputstreamByPath(String path);

	/**
	 * Return the currently active resource with a specified MDR path. Implementations
	 * must always eagerly fetch the resource's storage fields here.
	 * <p>
	 * The returned resource's storage field will be filtered according to the resource's
	 * type. Currently supported rules:
	 * <ul>
	 * <li>If resource type = {@link MdrNames#XQUERY}, filter place-holders
	 * 
	 * @param path
	 *            MDR path
	 * @return active resource, if found. If not found, returns <code>null</code>
	 */
	@Filtered
	Resource getActiveResourceByPath(String path);

	/**
	 * Return the currently active resource with a specified MDR path. Implementations
	 * must always eagerly fetch the resource's storage fields here.
	 * <p>
	 * The returned resource's storage field will be filtered according to the resource's
	 * type and converted to a string (so it better not be binary!). Currently supported
	 * rules:
	 * <ul>
	 * <li>If resource type = {@link MdrNames#XQUERY}, filter place-holders
	 * 
	 * @param path
	 *            MDR path
	 * @return active resource content, if found. If not found, returns <code>null</code>
	 */
	String getActiveResourceContentByPath(String path);

	/**
	 * A helper method whose purpose is to filter the resource using AOP.
	 * 
	 * @param path
	 * @return
	 */
	@Filtered
	Resource getActiveResourceByPathHelper(String path);

	/**
	 * Return the content of a resource by unique resource identifier. The content is
	 * taken from the column that matches the resource's storage code.
	 * 
	 * @param clazz
	 *            type of resource content to return (String/some XML type/CLOB/BLOB/...).
	 *            Must comply with the resource's storage code.
	 * @param id
	 *            resource unique identifier to look for
	 * @return resource content field, if persistent resource entity is found found.
	 *         Otherwise, returns <code>null</code>
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findAssetById(java.lang.Long)
	 */
	<T> T getResourceContent(Class<T> clazz, Long id);

	/**
	 * Gets a resource from the MDR and transforms that resource using another resource
	 * from the MDR ultimately returning the application of the transformer artifact to
	 * the document artifact.
	 * 
	 * @param docType
	 *            The type of document to transform
	 * @param docPath
	 *            The path to the document
	 * @param transType
	 *            The type of transformer
	 * @param transPath
	 *            The path to the transformer
	 * @param params
	 *            optional parameters to pass to the transformation
	 * @return the transformed document
	 * @throws WsException
	 */
	String getTransformedResource(DocumentType docType, String docPath,
			TransformType transType, String transPath, Map<String, String> params);

	/**
	 * Translate a source attribute from a source namespace to a target namespace
	 * returning the translated attribute and data type as a key/value pair.
	 * 
	 * @param sourceAttribute
	 *            The source attribute to translate
	 * @param sourceNamespace
	 *            The source namespace to translate from
	 * @param targetNamespace
	 *            The target namespace to translate to
	 * @return
	 */
	List<AttributeTranslationResultTo> getAttributeTranslation(String sourceAttribute,
			String sourceNamespace, String targetNamespace);
	
	/**
	 * Translate a source attribute from a source namespace to a target namespace
	 * returning the translated attribute and data type as a key/value pair.
	 * 
	 * @param sourceAttribute
	 *            The source attribute to translate
	 * @param sourceNamespace
	 *            The source namespace to translate from
	 * @param targetNamespace
	 *            The target namespace to translate to
	 * @return
	 */
	List<AttributeTranslationResultTo> getAttributeTranslation(String sourceAttribute,
			int sourceNamespaceId, int targetNamespaceId);

	/**
	 * Returns a unique result attribute translation or errors if non-unique
	 * 
	 * @param sourceAttribute
	 *            The source attribute to translate
	 * @param sourceNamespace
	 *            The source namespace to translate from
	 * @param targetNamespace
	 *            The target namespace to translate to
	 * @return
	 */
	AttributeTranslationResultTo getUniqueAttributeTranslation(String sourceAttribute,
			String sourceNamespace, String targetNamespace);
	
	/**
	 * Returns a unique result attribute translation or errors if non-unique
	 * 
	 * @param sourceAttribute
	 *            The source attribute to translate
	 * @param sourceNamespace
	 *            The source namespace identifier to translate from
	 * @param targetNamespace
	 *            The target namespace identifier to translate to
	 * @return
	 */
	AttributeTranslationResultTo getUniqueAttributeTranslation(String sourceAttribute,
			int sourceNamespaceId, int targetNamespaceId);
	
	/**
	 * Find an asset association by it's identifier.
	 * 
	 * @param assetAssocationId the asset association identifier
	 * @return An {@link AssetAssociation} if one exists.
	 */
	AssetAssociation findAssetAssocationById(Long assetAssocationId);

	/**
	 * Find an asset association by what side it is on and its label.
	 * 
	 * @param side
	 *            the side of the association, usually right or left
	 * @param label
	 *            the label the search for
	 * @return An {@link AssetAssociation} if one exists.
	 */
	List<AssetAssociation> findAssetAssociation(String side, String label);

	/**
	 * Find an asset association by what side it is on, its label, and it's association
	 * 
	 * @param side
	 *            the side of the association, usually right or left
	 * @param label
	 *            the label the search for
	 * @param association
	 *            the association
	 * @return An {@link AssetAssociation} if one exists.
	 */
	List<AssetAssociation> findAssetAssociation(String side, String label, String association);

}
