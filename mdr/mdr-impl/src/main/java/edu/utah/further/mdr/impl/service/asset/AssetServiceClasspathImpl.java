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

import static edu.utah.further.core.api.lang.CoreUtil.newUnsupportedOperationExceptionNotImplementedYet;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.core.api.text.PlaceHolderResolver;
import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.AssetAssociation;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.ResourceAdapter;
import edu.utah.further.mdr.api.domain.asset.ResourceType;
import edu.utah.further.mdr.api.domain.asset.ResourceTypeAdapter;
import edu.utah.further.mdr.api.domain.asset.StorageCode;
import edu.utah.further.mdr.api.service.transform.type.DocumentType;
import edu.utah.further.mdr.api.service.transform.type.TransformType;
import edu.utah.further.mdr.api.to.asset.AttributeTranslationResultTo;
import edu.utah.further.mdr.common.asset.AbstractAssetService;

/**
 * A mock Asset service implementation that returns resources on the classpath. Deals XML
 * resources only for now.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}, N. Dustin Schultz
 *         {@code <dustin.schultz@utah.edu>}
 * 
 * @version Apr 23, 2010
 */
@Implementation
@Service("assetServiceClassPath")
@Final
public class AssetServiceClasspathImpl extends AbstractAssetService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AssetServiceClasspathImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Resolves property place-holders in resource storage.
	 */
	@Autowired
	private PlaceHolderResolver resolver;

	/**
	 * Class path location under which MDR resources are stored.
	 */
	private List<org.springframework.core.io.Resource> prefixes;

	// ========================= CONSTRUCTORS ==============================

	// ========================= FIELDS ====================================

	// ========================= IMPL: AssetService ========================

	/**
	 * @param id
	 * @return
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findAssetById(java.lang.Long)
	 */
	@Override
	public Asset findAssetById(final Long id)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/**
	 * @param label
	 * @return
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findAssetsByLikeLabel(java.lang.String)
	 */
	@Override
	public List<Asset> findAssetsByLikeLabel(final String label)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/**
	 * @param id
	 * @return
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findResourceById(java.lang.Long)
	 */
	@Override
	public Resource findResourceById(final Long id)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/**
	 * @param typeLabel
	 * @return
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findResourcesByLikeTypeLabel(java.lang.String)
	 */
	@Override
	public List<Resource> findResourcesByLikeTypeLabel(final String typeLabel)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/**
	 * @param typeId
	 * @return
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findResourcesByTypeId(java.lang.Long)
	 */
	@Override
	public List<Resource> findResourcesByTypeId(final Long typeId)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/**
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#getResourceContent(java.lang.Class,
	 *      java.lang.Long)
	 */
	@Override
	public <T> T getResourceContent(final Class<T> clazz, final Long id)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/**
	 * Find resource types that match (LIKE) a resource type label.
	 * 
	 * @param typeLabel
	 *            resource type label (description) substring
	 * @return list matching resource types
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findResourceTypesByLikeTypeLabel(java.lang.Long)
	 */
	@Override
	public List<ResourceType> findResourceTypesByLikeTypeLabel(final String typeLabel)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/**
	 * Finds the resource under the {@link #prefixes} classpath URL.
	 * 
	 * @param path
	 *            resource's relative path under {@link #prefixes}
	 * @return MDR resource entity
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#getActiveResourceByPath(java.lang.String)
	 */
	@Override
	public Resource getActiveResourceByPath(final String path)
	{
		final org.springframework.core.io.Resource springResource = locateSpringResource(path);
		if (springResource == null)
		{
			throw new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND,
					"Could not locate resource in any location with path " + path);
		}

		// Convert to an MDR resource entity
		final Resource resource = toMdrResource(springResource);

		// Filter resource content
		getResourceContentService().filterResourceContent(resource, resolver);

		return resource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#getTransformedResource(edu.
	 * utah.further.mdr.api.service.transform.type.DocumentType, java.lang.String,
	 * edu.utah.further.mdr.api.service.transform.type.TransformType, java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public String getTransformedResource(final DocumentType docType,
			final String docPath, final TransformType transType, final String transPath,
			final Map<String, String> params)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#getAttributeTranslation(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<AttributeTranslationResultTo> getAttributeTranslation(
			final String sourceAttribute, final String sourceNamespace,
			final String targetNamespace)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#getAttributeTranslation(java
	 * .lang.String, int, int)
	 */
	@Override
	public List<AttributeTranslationResultTo> getAttributeTranslation(
			final String sourceAttribute, final int sourceNamespaceId,
			final int targetNamespaceId)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#getUniqueAttributeTranslation
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AttributeTranslationResultTo getUniqueAttributeTranslation(
			final String sourceAttribute, final String sourceNamespace,
			final String targetNamespace)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#getUniqueAttributeTranslation
	 * (java.lang.String, int, int)
	 */
	@Override
	public AttributeTranslationResultTo getUniqueAttributeTranslation(
			final String sourceAttribute, final int sourceNamespaceId,
			final int targetNamespaceId)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#findAssetAssocationById(java
	 * .lang.Long)
	 */
	@Override
	public AssetAssociation findAssetAssocationById(final Long assetAssocationId)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#findAssetAssociation(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public List<AssetAssociation> findAssetAssociation(final String side,
			final String label)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#findAssetAssociation(java.lang
	 * .String, java.lang.String, java.lang.Long)
	 */
	@Override
	public List<AssetAssociation> findAssetAssociation(final String side,
			final String label, final String association)
	{
		throw newUnsupportedOperationExceptionNotImplementedYet();
	}

	/**
	 * @param path
	 * @return
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#getActiveResourceByPathHelper(java.lang.String)
	 */
	@Override
	public Resource getActiveResourceByPathHelper(final String path)
	{
		throw new UnsupportedOperationException(
				"Only supported by DAO-based implementations");
	}

	// ========================= GET & SET =================================

	/**
	 * Set a new value for the prefixes property.
	 * 
	 * @param prefixes
	 *            the prefixes to set
	 */
	public void setPrefixes(final List<org.springframework.core.io.Resource> prefixes)
	{
		this.prefixes = prefixes;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Convert a spring file resource into an XQuery-type XML-type MDR resource.
	 * 
	 * @param springResource
	 * @throws IOException
	 */
	private Resource toMdrResource(
			final org.springframework.core.io.Resource springResource)
	{
		return new ClasspathResource(springResource);
	}

	/**
	 * @param path
	 * @return
	 */
	private org.springframework.core.io.Resource locateSpringResource(final String path)
	{
		for (final org.springframework.core.io.Resource location : prefixes)
		{
			final org.springframework.core.io.Resource springResource = locateSpringResource(
					location, path);
			if (springResource != null)
			{
				return springResource;
			}
		}
		return null;
	}

	/**
	 * @param prefix
	 * @param path
	 * @return
	 */
	private org.springframework.core.io.Resource locateSpringResource(
			final org.springframework.core.io.Resource prefix, final String path)
	{
		try
		{
			final org.springframework.core.io.Resource springResource = prefix
					.createRelative(path);
			// getFile().getAbsolutePath() will cause issues with classpath resources
			final String absolutePath = springResource.getURL().toString();
			if (log.isDebugEnabled())
			{
				log.debug("Getting resource by class-path " + quote(absolutePath));
			}
			return springResource;
		}
		catch (final IOException e)
		{
			return null;
		}
	}

	class ClasspathResource extends ResourceAdapter
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		final org.springframework.core.io.Resource springResource;

		/**
		 * @param springResource
		 */
		public ClasspathResource(final org.springframework.core.io.Resource springResource)
		{
			super();
			this.springResource = springResource;
		}

		@SuppressWarnings("serial")
		@Override
		public ResourceType getType()
		{
			return new ResourceTypeAdapter()
			{
				@Override
				public String getLabel()
				{
					return springResource.getFilename();
				}
			};
		}

		@Override
		public StorageCode getStorageCode()
		{
			// TODO: Replace content type detection with something more sophisticated like
			// Apache Tika for better coverage.

			final String fileExt = FilenameUtils.getExtension(springResource
					.getFilename());

			switch (fileExt)
			{
				case "xml":
				case "xq":
				case "xquery":
				case "xqm":
				case "xmi":
					return StorageCode.RESOURCE_XML;
				case "txt":
					return StorageCode.RESOURCE_TEXT;
				default:
					throw new ApplicationException(
							"Unknown storage code for file extension: " + fileExt);
			}
		}

		@Override
		public String getPath()
		{
			try
			{
				return springResource.getURL().toString();
			}
			catch (final IOException e)
			{
				throw new ApplicationException("Unable to retrieve resource path", e);
			}
		}

		@Override
		public String getUrl()
		{
			try
			{
				return springResource.getURL().getPath();
			}
			catch (final IOException e)
			{
				throw new ApplicationException("Unable to retrieve resource URL", e);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.utah.further.mdr.api.domain.asset.ResourceAdapter#getClob()
		 */
		@Override
		public String getClob()
		{
			return resourceAsString();
		}

		@Override
		public String getText()
		{
			return resourceAsString();
		}

		@Override
		public String getXml()
		{
			return resourceAsString();
		}

		private String resourceAsString()
		{
			try
			{
				final String resourceString = IoUtil
						.getInputStreamAsString(springResource.getInputStream());
				return resolver.resolvePlaceholders(resourceString);
			}
			catch (final IOException e)
			{
				throw new ApplicationException("Unable to get XML as String", e);
			}
		}
	};
}
