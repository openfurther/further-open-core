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
package edu.utah.further.mdr.ws.api.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.ws.Documentation;
import edu.utah.further.core.api.ws.ExamplePath;
import edu.utah.further.core.api.ws.HttpHeader;
import edu.utah.further.mdr.api.domain.asset.AssetAssociation;
import edu.utah.further.mdr.api.service.asset.MdrNames;
import edu.utah.further.mdr.api.service.transform.type.DocumentType;
import edu.utah.further.mdr.api.service.transform.type.TransformType;
import edu.utah.further.mdr.api.to.asset.AttributeTranslationResultTo;
import edu.utah.further.mdr.ws.api.to.AssetAssociationToImpl;
import edu.utah.further.mdr.ws.api.to.AssetAssociationToList;
import edu.utah.further.mdr.ws.api.to.AssetToImpl;
import edu.utah.further.mdr.ws.api.to.AttributeTranslationResultToList;
import edu.utah.further.mdr.ws.api.to.ResourceToImpl;

/**
 * REST search/query web services.
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
 * @version Feb 18, 2009
 */
@Path(AssetServiceRest.PATH)
@Produces("application/xml")
@Documentation(name = "MDR search services", description = "Restful MDR search services")
public interface AssetServiceRest
{
	// ========================= CONSTANTS =================================

	/**
	 * Web service base path of this class.
	 */
	static final String PATH = "/asset";

	// ========================= METHODS ===================================

	/**
	 * Find an asset by unique identifier.
	 * 
	 * @param id
	 *            asset unique identifier to look for
	 * @return persistent asset domain entity, if found. If not found, returns
	 *         <code>null</code>
	 */
	@GET
	@Documentation(name = "Find asset by ID", description = "Return the metadata of "
			+ "an MDR asset by ID.")
	@Path("/asset/{id}")
	@ExamplePath("asset/2")
	AssetToImpl findAssetById(
			@PathParam("id") @Documentation(description = "asset identifier") Long id)
			throws WsException;

	/**
	 * Find a resource by unique identifier.
	 * 
	 * @param id
	 *            resource unique identifier to look for
	 * @return persistent asset domain entity, if found. If not found, returns
	 *         <code>null</code>
	 */
	@GET
	@Documentation(name = "Find resource by asset ID", description = "Return the metadata of "
			+ "an MDR resource by asset ID.")
	@Path("/resource/{id}")
	@ExamplePath("resource/14")
	ResourceToImpl findResourceById(
			@PathParam("id") @Documentation(description = "asset identifier") Long id)
			throws WsException;

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
	@GET
	@Documentation(name = "Get XML resource filtered content by path", description = "Return the "
			+ "content of a resource by MDR path. MDR place-holders of the form ${key} are replaced"
			+ " with their replacement values in the response.")
	@Path("/resource/path/{path:.*}")
	@ExamplePath("/resource/path/fqe/further/xq/constants.xq")
	String getActiveResourceContentByPath(
	// @PathParam("path") @Documentation(description = "resource path") List<PathSegment>
	// path)
			@PathParam("path") @Documentation(description = "resource path") String path)
			throws WsException;

	/**
	 * Get a resource's content by unique identifier. A response header with the name
	 * {@link HttpHeader#RESOURCE_STORAGE_CODE} is added, whose value is the resource's
	 * storage code.
	 * 
	 * @param id
	 *            resource unique identifier to look for
	 * @return web service response whose entity and MIME type match the resource's
	 *         storage code, if the resource found. If not found, returns
	 *         <code>null</code>
	 * @param UnsupportedOperationException
	 *            if invoked a client implementation of this interface. Use
	 *            {@link #getResourceStorageByIdAsResource(Long)} instead in clients
	 */
	@GET
	@Documentation(name = "Get resource content by ID", description = "Return a "
			+ "response whose entity and MIME type match the resource's storage code, "
			+ "if the resource found. If not found, returns <code>null</code>. "
			+ "A response header with the name {@link HttpHeader#RESOURCE_STORAGE_CODE}"
			+ " is added, whose value is the resource's storage code.")
	@Path("/resource/{id}/storage")
	@ExamplePath("resource/14/storage")
	Response getResourceStorage(
			@PathParam("id") @Documentation(description = "asset identifier") Long id)
			throws WsException;

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
	@GET
	@Documentation(name = "Get transformed resource", description = "Return the result of applying the transformation resource to the document resource")
	@Path("/resource/transform/{docType}/{docPath:.+}/transformer/{transformerType}/{transPath:.+}")
	@ExamplePath("")
	String getTransformedResource(@PathParam("docType") DocumentType docType,
			@PathParam("docPath") String docPath,
			@PathParam("transformerType") TransformType transType,
			@PathParam("transPath") PathSegment transPathAndParams) throws WsException;

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
	@GET
	@Documentation(name = "Unique attribute translation", description = "Return the unique result of an attribute from a source namespace to a target namespace")
	@Path("/association/unique/attribute/{sourceAttribute}/{sourceNamespace}/{targetNamespace}")
	@ExamplePath("association/unique/attribute/race/FURTHeR/UUEDW")
	AttributeTranslationResultTo getUniqueAttributeTranslation(
			@PathParam("sourceAttribute") String sourceAttribute,
			@PathParam("sourceNamespace") String sourceNamespace,
			@PathParam("targetNamespace") String targetNamespace);
	
	/**
	 * Returns a list of result attribute translation
	 * 
	 * @param sourceAttribute
	 *            The source attribute to translate
	 * @param sourceNamespace
	 *            The source namespace to translate from (DTS namespaceId or name). If DtsNamespaceId, the label for the MDR asset for the namespace must equal the dts name.
	 * @param targetNamespace
	 *            The target namespace to translate to (DTS namespaceId or name). If DtsNamespaceId, the label for the MDR asset for the namespace must equal the dts name.
	 * @return
	 */
	@GET
	@Documentation(name = "Attribute translations", description = "Return one or more attribute translations from source namespace to a target namespace")
	@Path("/association/attribute/{sourceAttribute}/{sourceNamespace}/{targetNamespace}")
	@ExamplePath("association/attribute/race/FURTHeR/UUEDW")
	AttributeTranslationResultToList getAttributeTranslation(
			@PathParam("sourceAttribute") String sourceAttribute,
			@PathParam("sourceNamespace") String sourceNamespace,
			@PathParam("targetNamespace") String targetNamespace);
	
	/**
	 * Find an {@link AssetAssociation} by id.
	 * 
	 * @param id the id of the asset assocation
	 * @return an {@link AssetAssociation} if one exists
	 */
	@GET
	@Documentation(name = "Find asset association by id", description = "Return an asset assocation based on the id")
	@Path("/association/{id}")
	@ExamplePath("association/10")
	AssetAssociationToImpl findAssetAssocationById(@PathParam("id") Long id);

	/**
	 * Find an {@link AssetAssociation} by left/right label.
	 * 
	 * @param side
	 *            the side of the association (left/right)
	 * @param label
	 *            the label of the association
	 * @return an {@link AssetAssociation} if one exists
	 */
	@GET
	@Documentation(name = "Find asset association by left/right side asset label", description = "Return an asset assocation based on the left/right side asset label")
	@Path("/association/{side}/{label}")
	@ExamplePath("association/right/gender")
	AssetAssociationToList findAssetAssociation(@PathParam("side") String side,
			@PathParam("label") String label) throws WsException;

	/**
	 * Find an {@link AssetAssociation} by left/right label and id.
	 * 
	 * @param side
	 *            the side of the association (left/right)
	 * @param label
	 *            the label of the association
	 * @param id
	 *            the id of the assocation
	 * @return an {@link AssetAssociation} if one exists
	 */
	@GET
	@Documentation(name = "Find asset association by left/right side asset label and association", description = "Return an asset assocation based on the left/right side asset label and association")
	@Path("/association/{side}/{association}/{label}")
	@ExamplePath("association/right/hasAttribute/race/")
	AssetAssociationToList findAssetAssociation(@PathParam("side") final String side,
			@PathParam("association") String association, @PathParam("label") String label) throws WsException;
}
