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
package edu.utah.further.mdr.ws.impl.util;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.Header;
import org.slf4j.Logger;
import org.springframework.util.FileCopyUtils;

import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.discrete.EnumUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.ws.HttpHeader;
import edu.utah.further.core.ws.HttpResponseTo;
import edu.utah.further.core.ws.HttpUtil;
import edu.utah.further.mdr.api.domain.asset.StorageCode;
import edu.utah.further.mdr.api.service.asset.ResourceContentService;
import edu.utah.further.mdr.ws.api.to.ResourceToImpl;

/**
 * Terminology web services - utilities.
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
@Utility
public final class MdrWsUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(MdrWsUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private MdrWsUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Convert an HTTP response to a resource.
	 * 
	 * @param response
	 * @return
	 */
	public static ResourceToImpl toResource(final HttpResponseTo response,
			final ResourceContentService resourceContentService)
	{
		final Header storageCodeHeader = response
				.getResponseHeader(HttpHeader.RESOURCE_STORAGE_CODE.getName());
		final StorageCode storageCode = EnumUtil.valueOfNullSafe(StorageCode.class,
				storageCodeHeader.getValue());

		// Prepare return type
		final ResourceToImpl resource = new ResourceToImpl();
		resource.setStorageCode(storageCode);
		resourceContentService.setResourceContent(resource, response.getResponseBody());
		resource.setMimeType(response.getMediaType().toString());
		return resource;
	}

	/**
	 * Convert a JAX-RS response to a resource.
	 * 
	 * @param response
	 *            CXF response
	 * @param resourceContentService
	 *            handles resource content I/O
	 * @return resource transfer object
	 */
	public static ResourceToImpl toResource(final Response response,
			final ResourceContentService resourceContentService)
	{
		// Ready header
		final String storageCodeHeader = (String) response
				.getMetadata()
				.get(HttpHeader.RESOURCE_STORAGE_CODE.getName())
				.get(0);
		final StorageCode storageCode = EnumUtil.valueOfNullSafe(StorageCode.class,
				storageCodeHeader);
		final String mimeType = (String) response
				.getMetadata()
				.get(HttpHeader.CONTENT_TYPE.getName())
				.get(0);

		// Build the response
		final ResourceToImpl resource = new ResourceToImpl();
		resource.setStorageCode(storageCode);
		resource.setMimeType(HttpUtil.newMediaType(mimeType).toString());
		setResourceContent(response, resourceContentService, resource);
		return resource;
	}

	/**
	 * @param response
	 * @param resourceContentService
	 * @param resource
	 */
	private static void setResourceContent(final Response response,
			final ResourceContentService resourceContentService,
			final ResourceToImpl resource)
	{
		try
		{
			// Sometimes the resource content (= the response entity) that comes out of
			// CXF is a byte[], while some other times it is an HTTP input stream. Deal
			// with both cases.
			final Object entity = response.getEntity();
			final byte[] content = ReflectionUtil.instanceOf(entity, InputStream.class) ?
			// IoUtil.readBytesFromStream(new BufferedInputStream((InputStream) entity))
			FileCopyUtils.copyToByteArray((InputStream) entity)
					: (byte[]) entity;
			resourceContentService.setResourceContent(resource, content);
		}
		catch (final IOException e)
		{
			throw new ApplicationException("Could not read response content", e);
		}
	}

	// /**
	// * Convert an HTTP MIME type to a resource storage code.
	// *
	// * @param mediaType
	// * MIME type
	// * @return corresponding storage code
	// * @throws UnsupportedOperationException
	// * if no corresponding storage code exists
	// */
	// private static StorageCode toStorageCode(final MediaType mediaType)
	// {
	// if (MediaType.APPLICATION_OCTET_STREAM.equals(mediaType))
	// {
	// return StorageCode.RESOURCE_BLOB;
	// }
	// else if (MediaType.TEXT_PLAIN.equals(mediaType))
	// {
	// return StorageCode.RESOURCE_CLOB;
	// }
	// else if (MediaType.TEXT_PLAIN.equals(mediaType))
	// {
	// return StorageCode.RESOURCE_TEXT;
	// }
	// else if (MediaType.APPLICATION_XML_TYPE.equals(mediaType))
	// {
	// return StorageCode.RESOURCE_XML;
	// }
	// else
	// {
	// throw new UnsupportedOperationException("Unsupported media type " + mediaType
	// + ": no corresponding resource storage code exists");
	// }
	// }
}
