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
package edu.utah.further.mdr.common.asset;

import static edu.utah.further.core.util.io.LoggingUtil.tracePrintAndCenter;
import static edu.utah.further.core.ws.HttpUtil.getHttpGetResponseBody;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.rowset.serial.SerialBlob;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.text.PlaceHolderResolver;
import edu.utah.further.core.ws.HttpResponseTo;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.StorageCode;
import edu.utah.further.mdr.api.service.asset.MdrNames;
import edu.utah.further.mdr.api.service.asset.ResourceContentService;
import edu.utah.further.mdr.api.util.MdrUtil;

/**
 * Gets and sets a resource content on a {@link Resource} class based on the storage type.
 * Provides other resource content utilities (e.g. content filtering).
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
@Service("resourceContentService")
public class ResourceContentServiceImpl implements ResourceContentService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ResourceContentServiceImpl.class);

	/**
	 * An empty properties object.
	 */
	private static final Properties EMPTY_PROPERTIES = new Properties();

	/**
	 * HTTP timeout [seconds] when retrieving URL MDR resources.
	 */
	private static final int HTTP_TIMEOUT_SECS = 10;

	// ========================= DEPENDENCIES ==============================

	// ========================= FIELDS ====================================

	// ========================= GETTERS & SETTERS =========================

	// ========================= IMPLEMENTATION: ResourceContentService ====

	/**
	 * Return the resource's content as a string.
	 * 
	 * @param resource
	 *            resource
	 * @return content string
	 */
	@Override
	public String getResourceContentAsString(final Resource resource)
	{
		final byte[] content = getResourceContent(resource);
		// If content is null, this might be a remote URL resource and the URL may be down
		return (content == null) ? null : new String(content);
	}

	/**
	 * @param resource
	 * @return
	 * @see edu.utah.further.mdr.api.service.asset.ResourceContentService#getResourceContent(edu.utah.further.mdr.api.domain.asset.Resource)
	 */
	@SuppressWarnings("resource")
	// InputStream resource is close in finally block
	@Override
	public byte[] getResourceContent(final Resource resource)
	{
		final StorageCode storageCode = resource.getStorageCode();
		if (storageCode == null)
		{
			if (log.isWarnEnabled())
			{
				log.warn("Resource with no storage code! " + resource);
			}
			return null;
		}

		final String clob = resource.getClob();
		switch (storageCode)
		{
			case RESOURCE_BLOB:
			{
				InputStream inputStream = null;
				try
				{
					inputStream = resource.getBlob().getBinaryStream();
					final byte[] bytes = FileCopyUtils.copyToByteArray(inputStream); // IoUtil.readBytesFromStream(inputStream);
					return bytes;
				}
				catch (final Exception e)
				{
					throw new IllegalStateException(
							"Error reading resource BLOB content", e);
				}
				finally
				{
					try
					{
						inputStream.close();
					}
					catch (final IOException e)
					{
						// ignore
					}
				}
			}

			case RESOURCE_CLOB:
			{
				return clob.getBytes();
			}

			case RESOURCE_TEXT:
			{
				return resource.getText().getBytes();
			}

			case RESOURCE_XML:
			{
				return resource.getXml().getBytes();
			}

			case RESOURCE_URL:
			{
				// Get the resource from its URL property and cache it in the CLOB storage
				// field
				if (clob == null)
				{
					resource.setClob(getUrlResourceContent(resource, storageCode));
				}
				final String updatedClob = resource.getClob();
				return (updatedClob == null) ? null : updatedClob.getBytes();
			}

			default:
			{
				throw new UnsupportedOperationException("Unrecognized storage code: "
						+ storageCode);
			}
		}
	}

	/**
	 * @param storageCode
	 * @param content
	 * @return
	 * @see edu.utah.further.mdr.api.service.asset.ResourceContentService#toStorageType(edu.utah.further.mdr.api.domain.asset.StorageCode,
	 *      byte[])
	 */
	@Override
	public Object toStorageType(final StorageCode storageCode, final byte[] content)
	{
		if (storageCode == null)
		{
			// Do nothing
			return null;
		}

		switch (storageCode)
		{
			case RESOURCE_BLOB:
			{
				try
				{
					return new SerialBlob(content);
				}
				catch (final Exception e)
				{
					e.printStackTrace();
					return null;
				}
			}

			case RESOURCE_CLOB:
			case RESOURCE_TEXT:
			case RESOURCE_XML:
			{
				return new String(content);
			}

			default:
			{
				throw new UnsupportedOperationException("Unrecognized storage code: "
						+ storageCode);
			}
		}
	}

	/**
	 * @param resource
	 * @param content
	 * @see edu.utah.further.mdr.api.service.asset.ResourceContentService#setResourceContent(edu.utah.further.mdr.api.domain.asset.Resource,
	 *      byte[])
	 */
	@Override
	public void setResourceContent(final Resource resource, final byte[] content)
	{
		final StorageCode storageCode = resource.getStorageCode();
		if (storageCode == null)
		{
			// Do nothing
			return;
		}

		switch (storageCode)
		{
			case RESOURCE_BLOB:
			{
				try
				{
					resource.setBlob(new SerialBlob(content));
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
				return;
			}

			case RESOURCE_CLOB:
				// Assuming all remote-URL resources are big text documents
			case RESOURCE_URL:
			{
				resource.setClob(new String(content));
				return;
			}

			case RESOURCE_TEXT:
			{
				resource.setText(new String(content));
				return;
			}

			case RESOURCE_XML:
			{
				resource.setXml(new String(content));
				return;
			}

			default:
			{
				throw new UnsupportedOperationException("Unrecognized storage code: "
						+ storageCode);
			}
		}
	}

	/**
	 * Filter a resource's content using the property place holder resolver. Right now
	 * only XQuery files are filtered; more complex rules can be incorporated in the
	 * future (and perhaps stored in the MDR).
	 * 
	 * @param resolver
	 *            resolves property place-holders in resource storage
	 * @see edu.utah.further.mdr.api.service.asset.ResourceContentService#filterResourceContent(edu.utah.further.mdr.api.domain.asset.Resource,
	 *      edu.utah.further.core.api.text.PlaceHolderResolver)
	 */
	@Override
	public void filterResourceContent(final Resource resource,
			final PlaceHolderResolver resolver)
	{
		filterResourceContent(resource, resolver, EMPTY_PROPERTIES);
	}

	/**
	 * Filter a resource's content using the property place holder resolver. Right now
	 * only XQuery files are filtered; more complex rules can be incorporated in the
	 * future (and perhaps stored in the MDR).
	 * 
	 * @param resource
	 *            resource to filter
	 * @param resolver
	 *            resolves property place-holders in resource storage
	 * @param overriddenPlaceholders
	 *            map of key-value place holders to use. Is overlaid over the resolver's
	 *            default place holder map, potentially overriding the resolver's default
	 *            values with identical keys in both maps
	 * @see edu.utah.further.mdr.api.service.asset.ResourceContentService#filterResourceContent(edu.utah.further.mdr.api.domain.asset.Resource,
	 *      edu.utah.further.core.api.text.PlaceHolderResolver)
	 */
	@Override
	public void filterResourceContent(final Resource resource,
			final PlaceHolderResolver resolver, final Properties overridenPlaceholders)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Filtering content of XQuery resource " + resource);
		}
		if (MdrUtil.isResourceTypeLabelContains(resource, MdrNames.XQUERY))
		{
			final String content = getResourceContentAsString(resource);
			final String filteredContent = resolver.resolvePlaceholders(content,
					overridenPlaceholders);
			if (log.isTraceEnabled())
			{
				tracePrintAndCenter(log, "Original content");
				log.trace(content.toString());
				tracePrintAndCenter(log, "Filtered content");
				log.trace(filteredContent.toString());
			}
			if (filteredContent != null)
			{
				setResourceContent(resource, filteredContent.getBytes());
			}
		}
	}

	/**
	 * Filter a resource's meta data fields (e.g. URL) using the property place holder
	 * resolver. Currently filteres only the URL field in URL-type resources.
	 * 
	 * @param resource
	 *            resource to filter
	 * @param resolver
	 *            resolves property place-holders in resource storage
	 * @see edu.utah.further.mdr.api.service.asset.ResourceContentService#filterResourceMetadata(edu.utah.further.mdr.api.domain.asset.Resource,
	 *      edu.utah.further.core.api.text.PlaceHolderResolver)
	 */
	@Override
	public void filterResourceMetadata(final Resource resource,
			final PlaceHolderResolver resolver)
	{
		switch (resource.getStorageCode())
		{
			case RESOURCE_URL:
			{
				final String filteredUrl = resolver.resolvePlaceholders(
						resource.getUrl(), EMPTY_PROPERTIES);
				resource.setUrl(filteredUrl);
				break;
			}

			default:
			{
				// Do nothing
			}
		}

	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Open a remote HTTP connection and retrieve a resource's content from a URL.
	 * 
	 * @param url
	 *            absolute remote URL
	 * @return HTTP response bytes
	 */
	private static HttpResponseTo getResourceContentFromUrl(final String url)
	{
		try
		{
			if (log.isDebugEnabled())
			{
				log.debug("Trying to download resource from url " + url);
			}
			return getHttpGetResponseBody(url, HTTP_TIMEOUT_SECS);
		}
		catch (final WsException e)
		{
			switch (e.getCode())
			{
				case TRANSPORT_ERROR:
				{
					// Remote resource URL is not up, do not crash
					return null;
				}

				default:
				{
					throw new ApplicationException(
							"Web service error reading resource content from remote URL "
									+ url, e);
				}
			}
		}
		catch (final Exception e)
		{
			throw new ApplicationException(
					"Error reading resource content from remote URL " + url, e);
		}
	}

	/**
	 * @param resource
	 * @param storageCode
	 * @return
	 */
	private HttpResponseTo getRemoteResourceViaHttp(final Resource resource,
			final StorageCode storageCode)
	{
		switch (storageCode)
		{
			case RESOURCE_URL:
			{
				// final String filteredUrl =
				// resolver.resolvePlaceholders(resource.getUrl());
				final String filteredUrl = resource.getUrl();
				return getResourceContentFromUrl(filteredUrl);
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized remote resource storage code: " + storageCode);
			}
		}
	}

	/**
	 * @param resource
	 * @param storageCode
	 * @return
	 */
	private String getUrlResourceContent(final Resource resource,
			final StorageCode storageCode)
	{
		final HttpResponseTo response = getRemoteResourceViaHttp(resource, storageCode);
		if (response == null)
		{
			return null;
		}
		// Forward mime type from HTTP response. If not found, use text
		// as a default.
		final MediaType httpMediaType = response.getMediaType();
		final String mimeTypeString = (httpMediaType == null) ? "text/plain"
				: httpMediaType.toString();
		resource.setMimeType(mimeTypeString);

		// Copy HTTP body to the CLOB storage field
		return new String(response.getResponseBody());
	}
}
