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
package edu.utah.further.mdr.ws.impl.service.soap;

import static edu.utah.further.core.api.constant.ErrorCode.INVALID_INPUT_ARGUMENT_VALUE;
import static edu.utah.further.core.api.constant.ErrorCode.RESOURCE_NOT_FOUND;
import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.mdr.ws.impl.util.MdrWsNames.SOAP_ASSET_SERVICE_INTERFACE;
import static edu.utah.further.mdr.ws.impl.util.MdrWsNames.SOAP_ASSET_SERVICE_SPRING_NAME;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.math.NumberUtils.LONG_ZERO;
import static org.slf4j.LoggerFactory.getLogger;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.text.TextTemplate;
import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.service.asset.AssetService;
import edu.utah.further.mdr.ws.api.service.soap.AssetServiceSoap;
import edu.utah.further.mdr.ws.api.to.AssetAssociationToImpl;
import edu.utah.further.mdr.ws.api.to.AssetAssociationToList;
import edu.utah.further.mdr.ws.api.to.AssetToImpl;
import edu.utah.further.mdr.ws.api.to.ResourceToImpl;

/**
 * Terminology search/query web services implementation.
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
@Service(SOAP_ASSET_SERVICE_SPRING_NAME)
@WebService(endpointInterface = SOAP_ASSET_SERVICE_INTERFACE)
public class AssetServiceSoapImpl implements AssetServiceSoap
{
	// ========================= FIELDS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AssetServiceSoapImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes asset service.
	 */
	@Autowired
	private AssetService assetService;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: TranslateService ========

	/**
	 * @param id
	 * @return
	 * @throws WsException
	 * @see edu.utah.further.mdr.ws.api.service.soap.AssetServiceSoap#findAssetById(java.lang.Long)
	 */
	@Override
	public AssetToImpl findAssetById(final Long id) throws WsException
	{
		// Validate input arguments
		validateId(id);

		if (log.isDebugEnabled())
		{
			log.debug("Finding asset by ID " + id);
		}
		final Asset asset = assetService.findAssetById(id);
		if (asset == null)
		{
			throw new WsException(RESOURCE_NOT_FOUND,
					"No asset was found with the specified ID");
		}
		// Copy business layer result to a TO for XML serialization
		return new AssetToImpl().copyFrom(asset);
	}

	/**
	 * @param id
	 * @return
	 * @throws WsException
	 * @see edu.utah.further.mdr.ws.api.service.soap.AssetServiceSoap#findResourceById(java.lang.Long)
	 */
	@Override
	public ResourceToImpl findResourceById(final Long id) throws WsException
	{
		// Validate input arguments
		validateId(id);

		if (log.isDebugEnabled())
		{
			log.debug("Finding resource by ID " + id);
		}
		final Resource resource = assetService.findResourceById(id);
		if (resource == null)
		{
			throw new WsException(RESOURCE_NOT_FOUND,
					"No resource was found with the specified ID");
		}
		// Copy business layer result to a TO for XML serialization
		return new ResourceToImpl().copyFrom(resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.ws.api.service.soap.AssetServiceSoap#findAssetAssocationById
	 * (java.lang.Long)
	 */
	@Override
	public AssetAssociationToImpl findAssetAssocationById(final Long id)
	{
		return new AssetAssociationToImpl().copyFrom(assetService
				.findAssetAssocationById(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.ws.api.service.soap.AssetServiceSoap#findAssetAssociation(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public AssetAssociationToList findAssetAssociationBySideAndLabel(final String side,
			final String label) throws WsException
	{
		return new AssetAssociationToList(assetService.findAssetAssociation(side, label));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.ws.api.service.soap.AssetServiceSoap#findAssetAssociation(
	 * java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public AssetAssociationToList findAssetAssociationBySideLabelAndAssociation(final String side,
			final String label, final String association) throws WsException
	{
		return new AssetAssociationToList(assetService.findAssetAssociation(side, label,
				association));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Validate input arguments to {@link #translateConcept(DtsConceptPair)}.
	 * 
	 * @param source
	 * @param targetPropertyName
	 * @param options
	 */
	private void validateId(final Long id) // throws WsException
	{
		final TextTemplate invalidArgumentMessage = new TextTemplate("Illegal ID %ID%",
				asList("ID"));
		if ((id == null) || (id.compareTo(LONG_ZERO) <= 0))
		{
			throw new ApplicationException(INVALID_INPUT_ARGUMENT_VALUE,
					invalidArgumentMessage.evaluate(asList(EMPTY_STRING + id)));
		}
	}
}
