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
package edu.utah.further.mdr.ws.impl.service.rest;

import static edu.utah.further.core.api.constant.ErrorCode.INVALID_INPUT_ARGUMENT_VALUE;
import static edu.utah.further.core.api.constant.ErrorCode.PROPERTY_NOT_FOUND;
import static edu.utah.further.core.api.constant.Strings.VIRTUAL_DIRECTORY;
import static edu.utah.further.core.cxf.util.JaxRsUtil.toSingleValuedMap;
import static edu.utah.further.mdr.ws.impl.util.MdrWsNames.SOAP_ASSET_SERVICE_SPRING_NAME;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.api.ws.HttpHeader;
import edu.utah.further.core.ws.HttpUtil;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.StorageCode;
import edu.utah.further.mdr.api.service.asset.AssetService;
import edu.utah.further.mdr.api.service.asset.ResourceContentService;
import edu.utah.further.mdr.api.service.transform.type.DocumentType;
import edu.utah.further.mdr.api.service.transform.type.TransformType;
import edu.utah.further.mdr.api.to.asset.AttributeTranslationResultTo;
import edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest;
import edu.utah.further.mdr.ws.api.service.soap.AssetServiceSoap;
import edu.utah.further.mdr.ws.api.to.AssetAssociationToImpl;
import edu.utah.further.mdr.ws.api.to.AssetAssociationToList;
import edu.utah.further.mdr.ws.api.to.AssetToImpl;
import edu.utah.further.mdr.ws.api.to.AttributeTranslationResultToList;
import edu.utah.further.mdr.ws.api.to.ResourceToImpl;

/**
 * Terminology search/query RESTful web service implementation.
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
 * @version Oct 10, 2008
 */
@Implementation
@Service("mdrAssetServiceRest")
// Redundant -- @Path already appears in the interface, but required for FUR-1332.
@Path(AssetServiceRest.PATH)
public class AssetServiceRestImpl implements AssetServiceRest
{
	// ========================= FIELDS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AssetServiceRestImpl.class);

	/**
	 * Double slash in path strings.
	 */
	@SuppressWarnings("unused")
	private static final String DOUBLE_SLASH = VIRTUAL_DIRECTORY + VIRTUAL_DIRECTORY;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes asset service.
	 */
	@Autowired
	private AssetService assetService;

	/**
	 * Handles resource content I/O.
	 */
	@Autowired
	private ResourceContentService resourceContentService;

	// ========================= FIELDS ====================================

	/**
	 * SOAP version of this service that allows complex input data types. We delegate most
	 * of the implementation to this object.
	 */
	@Autowired
	@Qualifier(SOAP_ASSET_SERVICE_SPRING_NAME)
	private AssetServiceSoap assetServiceSoap;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: AssetServiceRest ==========

	/**
	 * @param id
	 * @return
	 * @throws WsException
	 * @see edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest#findAssetById(java.lang.Long)
	 */
	@Override
	public AssetToImpl findAssetById(final Long id) throws WsException
	{
		return assetServiceSoap.findAssetById(id);
	}

	/**
	 * @param id
	 * @return
	 * @throws WsException
	 * @see edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest#findResourceById(java.lang.Long)
	 */
	@Override
	public ResourceToImpl findResourceById(final Long id) throws WsException
	{
		return assetServiceSoap.findResourceById(id);
	}

	/**
	 * @param path
	 * @return
	 * @throws WsException
	 * @see edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest#getActiveResourceContentByPath(java.lang.String)
	 */
	// public String getActiveResourceContentByPath(final List<PathSegment> path)
	@Override
	public String getActiveResourceContentByPath(final String path) throws WsException
	{
		return assetService.getActiveResourceContentByPath(path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest#getTransformedResource
	 * (edu.utah.further.mdr.api.service.transform.type.DocumentType, java.lang.String,
	 * edu.utah.further.mdr.api.service.transform.type.TransformType,
	 * javax.ws.rs.core.PathSegment)
	 */
	@Override
	public String getTransformedResource(final DocumentType docType,
			final String docPath, final TransformType transType,
			final PathSegment transPathAndParams)
	{
		return assetService.getTransformedResource(docType, docPath, transType,
				transPathAndParams.getPath(),
				toSingleValuedMap(transPathAndParams.getMatrixParameters()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest#getUniqueAttributeTranslation
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AttributeTranslationResultTo getUniqueAttributeTranslation(
			final String sourceAttribute, final String sourceNamespace,
			final String targetNamespace)
	{
		AttributeTranslationResultTo result = null;

		// Not very clean but @PathParam doesn't seem to differentiate types so we have to
		// check if the source and target are numbers instead of Strings.
		if (NumberUtils.isNumber(sourceNamespace)
				&& NumberUtils.isNumber(targetNamespace))
		{
			result = assetService.getUniqueAttributeTranslation(sourceAttribute, Integer
					.valueOf(sourceNamespace)
					.intValue(), Integer.valueOf(targetNamespace).intValue());
		}
		else
		{
			result = assetService.getUniqueAttributeTranslation(sourceAttribute,
					sourceNamespace, targetNamespace);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest#getAttributeTranslations
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AttributeTranslationResultToList getAttributeTranslation(
			final String sourceAttribute, final String sourceNamespace,
			final String targetNamespace)
	{
		List<AttributeTranslationResultTo> results = new ArrayList<>();
		// Not very clean but @PathParam doesn't seem to differentiate types so we have to
		// check if the source and target are numbers instead of Strings.
		if (NumberUtils.isNumber(sourceNamespace)
				&& NumberUtils.isNumber(targetNamespace))
		{
			results = assetService.getAttributeTranslation(sourceAttribute, Integer
					.valueOf(sourceNamespace)
					.intValue(), Integer.valueOf(targetNamespace).intValue());
		}
		else
		{
			results = assetService.getAttributeTranslation(sourceAttribute,
					sourceNamespace, targetNamespace);
		}

		return new AttributeTranslationResultToList(results);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest#findAssetAssocationById
	 * (java.lang.Long)
	 */
	@Override
	public AssetAssociationToImpl findAssetAssocationById(final Long id)
	{
		return assetServiceSoap.findAssetAssocationById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest#findAssetAssociation(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public AssetAssociationToList findAssetAssociation(final String side,
			final String label) throws WsException
	{
		return assetServiceSoap.findAssetAssociationBySideAndLabel(side, label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest#findAssetAssociation(
	 * java.lang.String, java.lang.Long, java.lang.String)
	 */
	@Override
	public AssetAssociationToList findAssetAssociation(final String side,
			final String association, final String label) throws WsException
	{
		return assetServiceSoap.findAssetAssociationBySideLabelAndAssociation(side,
				label, association);
	}

	/**
	 * @param path
	 * @return
	 */
	public static String toStringList(final List<PathSegment> path)
	{
		// TODO: move to core-cxf as a utility
		final List<String> pathParts = CollectionUtil.newList();
		for (final PathSegment segment : path)
		{
			pathParts.add(segment.getPath());
		}
		return StringUtil.chain(pathParts, Strings.VIRTUAL_DIRECTORY);
	}

	/**
	 * @param id
	 * @return
	 * @see edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest#getResourceStorage(java.lang.Long)
	 */
	@Override
	public Response getResourceStorage(final Long id) throws WsException
	{
		if (log.isDebugEnabled())
		{
			log.debug("Getting resource storage field by ID " + id);
		}

		// ==================================================
		// Find the resource and validate its fields
		// ==================================================
		final Resource resource = findResourceById(id);
		final StorageCode storageCode = resource.getStorageCode();
		final String mimeType = resource.getMimeType();
		if (log.isDebugEnabled())
		{
			log.debug("Resource ID " + id + ": storage code " + storageCode
					+ " MIME-type " + mimeType);
		}
		validateStorageCodeFields(storageCode, mimeType);

		// ==================================================
		// Build the response
		// ==================================================
		final ResponseBuilder builder = Response.ok();
		builder.entity(resourceContentService.getResourceContent(resource));
		builder.type(HttpUtil.newMediaType(resource.getMimeType()));
		final Response response = builder.build();
		// Attach storage code as custom response header
		response.getMetadata().add(HttpHeader.RESOURCE_STORAGE_CODE.getName(),
				storageCode.toString());
		// httpResponse.getOutputStream().write(resourceContentService.getResourceContent(resource));
		return response;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param storageCode
	 * @param mimeType
	 */
	private void validateStorageCodeFields(final StorageCode storageCode,
			final String mimeType) throws WsException
	{
		if (storageCode == null)
		{
			throw new WsException(PROPERTY_NOT_FOUND,
					"Missing storage code for the resource with the specified ID");
		}
		if (!storageCode.isRemote())
		{
			// If remote resource, mime type will be set when retrieving resource's
			// content from the remote provider, so don't validate here
			if (mimeType == null)
			{
				throw new WsException(PROPERTY_NOT_FOUND,
						"Missing mime type for the resource with the specified ID");
			}
			if (!mimeType.matches(HttpUtil.MIME_TYPE_REGEX))
			{
				throw new WsException(INVALID_INPUT_ARGUMENT_VALUE,
						"Invalid mime type for the resource with the specified ID");
			}
		}
	}
}
