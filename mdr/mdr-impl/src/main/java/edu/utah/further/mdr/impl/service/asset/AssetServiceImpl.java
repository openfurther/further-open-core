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

import static edu.utah.further.core.query.domain.SearchCriteria.addSimpleExpression;
import static edu.utah.further.core.query.domain.SearchCriteria.junction;
import static edu.utah.further.core.query.domain.SearchCriteria.simpleExpression;
import static edu.utah.further.core.query.domain.SearchCriteria.simpleExpressionIgnoreCase;
import static edu.utah.further.core.query.domain.SearchCriteria.stringExpression;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.query.domain.MatchType;
import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchEngine;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchType;
import edu.utah.further.core.xml.xquery.XQueryService;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.mdr.api.annotation.Filtered;
import edu.utah.further.mdr.api.dao.ResourceDao;
import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.AssetAssociation;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.domain.asset.ResourceType;
import edu.utah.further.mdr.api.service.transform.type.DocumentType;
import edu.utah.further.mdr.api.service.transform.type.TransformType;
import edu.utah.further.mdr.api.to.asset.AttributeTranslationResultTo;
import edu.utah.further.mdr.common.asset.AbstractAssetService;

/**
 * Asset service implementation.
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
@Implementation
@Service("assetService")
@Transactional(readOnly = true)
public class AssetServiceImpl extends AbstractAssetService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AssetServiceImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Performs DAO methods.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;

	/**
	 * Performs DAO methods.
	 */
	@Autowired
	@Qualifier("dao")
	private SearchEngine se;

	/**
	 * Performs DAO methods, eagerly fetching all properties of all entities.
	 */
	@Autowired
	@Qualifier("daoEager")
	private Dao daoEager;

	/**
	 * Performs custom {@link Resource} DAO methods.
	 */
	@Autowired
	@Qualifier("resourceDao")
	private ResourceDao resourceDao;

	/**
	 * XQuery Service
	 */
	@Autowired
	@Qualifier("xqueryService")
	private XQueryService xqueryService;

	@Autowired
	private DtsOperationService dtsOperationService;

	// ========================= FIELDS ====================================

	/**
	 * if <code>true</code>, eagerly fetch all properties of all persistent entities
	 * returned by web methods; otherwise, lazily-initialize all properties except those
	 * that are explicitly annotated to be eagerly fetched.
	 */
	private boolean fetchProperties = false;

	// ========================= METHODS: ASSETS ===========================

	/**
	 * Default constructor
	 */
	public AssetServiceImpl()
	{
	}

	/**
	 * Find an asset by unique identifier.
	 * 
	 * @param id
	 *            asset unique identifier to look for
	 * @return persistent asset domain entity, if found. If not found, returns
	 *         <code>null</code>
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findAssetById(java.lang.Long)
	 */
	@Override
	public Asset findAssetById(final Long id)
	{
		return getDao().findById(Asset.class, id);
	}

	/**
	 * Find assets that match (LIKE) an asset label.
	 * 
	 * @param label
	 *            asset label (description) substring
	 * @return list matching resources
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findAssetsByLikeLabel(java.lang.Long)
	 */
	@Override
	public List<Asset> findAssetsByLikeLabel(final String label)
	{
		return CollectionUtil.<Asset> newList(getDao().findByLikeProperty(Asset.class,
				"label", label));
	}

	// ========================= METHODS: RESOURCES ========================

	/**
	 * Find a resource by unique identifier.
	 * 
	 * @param id
	 *            resource unique identifier to look for
	 * @return persistent resource domain entity, if found. If not found, returns
	 *         <code>null</code>
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findAssetById(java.lang.Long)
	 */
	@Override
	@Filtered
	public Resource findResourceById(final Long id)
	{
		final Resource resource = getDao().findById(Resource.class, id);
		if (log.isDebugEnabled())
		{
			log.debug("Retrieved resource by ID " + id + " resource " + resource);
		}
		return resource;
	}

	/**
	 * Find resources that match a resource type identifier.
	 * 
	 * @param typeId
	 *            resource type identifier to look for
	 * @return list matching resources
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findResourcesByTypeId(java.lang.Long)
	 */
	@Override
	@Filtered
	public List<Resource> findResourcesByTypeId(final Long typeId)
	{
		final List<Resource> resources = CollectionUtil.<Resource> newList(getDao()
				.findByProperty(Resource.class, "type.id", typeId));
		return resources;
	}

	/**
	 * Find resources that match (LIKE) a resource type label.
	 * 
	 * @param typeLabel
	 *            resource type label (description) substring
	 * @return list matching resources
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findResourcesByLikeTypeLabel(java.lang.Long)
	 */
	@Override
	@Filtered
	public List<Resource> findResourcesByLikeTypeLabel(final String typeLabel)
	{
		final List<Resource> resources = CollectionUtil
				.<Resource> newList(findEntitiesByLikeTypeLabel(Resource.class, typeLabel));
		return resources;
	}

	/**
	 * @param path
	 * @return
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#getActiveResourceByPath(java.lang.String)
	 */
	@Override
	public Resource getActiveResourceByPath(final String path)
	{
		// AOP self-invocation pattern
		final Resource resource = getSelf().getActiveResourceByPathHelper(path);
		getResourceContentService().filterResourceContent(resource, getResolver());
		return resource;
	}

	/**
	 * Return the content of a resource by unique resource identifier. The content is
	 * taken from the column that matches the resource's storage code. It is clients
	 * responsibility to pass the correct class. <T> type to cast resource content to
	 * 
	 * @param id
	 *            resource unique identifier to look for
	 * @param clazz
	 *            the type of resource to return
	 * @return resource content field string, if persistent resource entity is found
	 *         found. Otherwise, returns <code>null</code>
	 * @see edu.utah.further.mdr.api.service.asset.AssetService#findAssetById(java.lang.Long)
	 * @throws ClassCastException
	 *             if the cast to type <code>T</code> fails
	 */
	@Override
	public <T> T getResourceContent(final Class<T> type, final Long id)
	{
		final Resource resource = getDao().findById(Resource.class, id);
		// Don't bother filtering resource fields, because we only return its content

		// TODO: use mime type in corresponding web service to stream this object to a web
		// client if it is big.

		return (resource == null) ? null : type.cast(getResourceContentService()
				.toStorageType(resource.getStorageCode(),
						getResourceContentService().getResourceContent(resource)));
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
		return CollectionUtil.<ResourceType> newList(getDao().findByLikeProperty(
				ResourceType.class, "label", typeLabel));
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
		try (InputStream docStream = getActiveResourceInputstreamByPath(docPath);
				InputStream transStream = getActiveResourceInputstreamByPath(transPath))
		{

			switch (transType)
			{
				case XQUERY:
					return xQueryTransformation(docStream, transStream, params);
				default:
					throw new UnsupportedOperationException(docType
							+ " is currently not supported");
			}
		}
		catch (final IOException e)
		{
			throw new ApplicationException(
					"Problem reading document or transformation stream");
		}
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
		final SearchCriterion criterion = junction(SearchType.CONJUNCTION);
		criterion.addCriterion(simpleExpression(Relation.EQ, "association",
				"translatesTo"));
		criterion
				.addCriterion(simpleExpression(Relation.EQ, "leftAsset", sourceAttribute));
		criterion.addCriterion(simpleExpressionIgnoreCase(Relation.EQ, "leftNamespace",
				sourceNamespace));
		criterion.addCriterion(simpleExpressionIgnoreCase(Relation.EQ, "rightNamespace",
				targetNamespace));

		final List<AttributeTranslationResultTo> results = CollectionUtil.newList();
		final List<AssetAssociation> assetAssociations = se.search(
				AssetAssociation.class,
				SearchCriteria.query(criterion, AssetAssociation.class.getSimpleName()));

		for (final AssetAssociation assetAssociation : assetAssociations)
		{
			final Map<String, String> properties = assetAssociation.getPropertiesAsMap();

			final AttributeTranslationResultTo result = new AttributeTranslationResultTo();
			result.setTranslatedAttribute(assetAssociation.getRightAsset());
			result.setAttributeProperties(properties);
			results.add(result);
		}

		return results;
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
		final String source = dtsOperationService
				.findNamespaceNameById(sourceNamespaceId);
		final String target = dtsOperationService
				.findNamespaceNameById(targetNamespaceId);

		return getAttributeTranslation(sourceAttribute, source, target);
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
		final List<AttributeTranslationResultTo> results = getAttributeTranslation(
				sourceAttribute, sourceNamespace, targetNamespace);

		if (results.size() < 1)
		{
			throw new ApplicationException("No translations found");
		}

		if (results.size() > 1)
		{
			throw new ApplicationException(
					"More than one translations found, expected unique result");
		}

		return results.get(0);
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
		final String source = dtsOperationService
				.findNamespaceNameById(sourceNamespaceId);
		final String target = dtsOperationService
				.findNamespaceNameById(targetNamespaceId);

		return getUniqueAttributeTranslation(sourceAttribute, source, target);
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
		return daoEager.getById(AssetAssociation.class, assetAssocationId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#findAssetAssociationBySideAndLabel
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public List<AssetAssociation> findAssetAssociation(final String side,
			final String label)
	{
		final String propertyName = getAssetPropertyNameBySide(side);

		final List<AssetAssociation> associations = daoEager.findByProperty(
				AssetAssociation.class, propertyName, label);

		handleAssociationErrors(label, associations);

		return associations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.service.asset.AssetService#findAssetAssociation(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<AssetAssociation> findAssetAssociation(final String side,
			final String label, final String association)
	{
		final String propertyName = getAssetPropertyNameBySide(side);

		final SearchCriterion criterion = junction(SearchType.CONJUNCTION);
		addSimpleExpression(criterion, Relation.EQ, "association", association);
		addSimpleExpression(criterion, Relation.EQ, propertyName, label);

		final List<AssetAssociation> associations = se.search(AssetAssociation.class,
				SearchCriteria.query(criterion, AssetAssociation.class.getName()));

		handleAssociationErrors(label, associations);

		return associations;
	}

	/**
	 * A helper method whose purpose is to filter the resource using AOP.
	 * 
	 * @param path
	 * @return
	 */
	@Override
	@Filtered
	public Resource getActiveResourceByPathHelper(final String path)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Getting active resource by path " + StringUtil.quote(path));
		}
		final Resource resource = resourceDao.getActiveResourceByPath(path);
		return resource;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the dao property.
	 * 
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(final Dao dao)
	{
		this.dao = dao;
	}

	/**
	 * Set a new value for the daoEager property.
	 * 
	 * @param daoEager
	 *            the daoEager to set
	 */
	public void setDaoEager(final Dao daoEager)
	{
		this.daoEager = daoEager;
	}

	/**
	 * Set a new value for the resourceDao property.
	 * 
	 * @param resourceDao
	 *            the resourceDao to set
	 */
	public void setResourceDao(final ResourceDao resourceDao)
	{
		this.resourceDao = resourceDao;
	}

	/**
	 * Set a new value for the fetchProperties property.
	 * 
	 * @param fetchProperties
	 *            the fetchProperties to set
	 */
	public void setFetchProperties(final boolean fetchProperties)
	{
		this.fetchProperties = fetchProperties;
	}

	/**
	 * Set a new value for the xqueryService property.
	 * 
	 * @param xqueryService
	 *            the xqueryService to set
	 */
	public void setXqueryService(final XQueryService xqueryService)
	{
		this.xqueryService = xqueryService;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Find entities with a <code>type.label</code> property that match (LIKE) a resource
	 * type label.
	 * 
	 * @param domainClass
	 *            entity type
	 * @param typeLabel
	 *            resource type label (description) substring
	 * @return list matching entities
	 */
	private <T extends PersistentEntity<Long>> List<T> findEntitiesByLikeTypeLabel(
			final Class<T> domainClass, final String typeLabel)
	{
		final SearchCriterion criterion = stringExpression(SearchType.LIKE, "type.label",
				typeLabel, MatchType.CONTAINS);
		final SearchQuery criteria = SearchCriteria.queryBuilder(criterion)
		// Strangely, Hibernate says it cannot resolve "type.label" when the following
		// alias is not added, although it does not require it when searching on
		// "type.id".
				.addAlias("", "type", "type")
				.build();
		return ((SearchEngine) getDao()).search(domainClass, criteria);
	}

	/**
	 * Return the DAO dependency. The implementation depends on the
	 * {@link #fetchProperties} flag.
	 * 
	 * @return the dao
	 */
	private Dao getDao()
	{
		return fetchProperties ? daoEager : dao;
	}

	/**
	 * @param docStream
	 * @param transStream
	 * @param parameters
	 * @return
	 */
	private String xQueryTransformation(final InputStream docStream,
			final InputStream transStream, final Map<String, String> parameters)
	{
		return xqueryService.executeIntoString(transStream, docStream, parameters);
	}

	/**
	 * Returns the propertyName to use depending on the side of the association argument
	 * used (right/left).
	 * 
	 * @param side
	 *            the side of the association
	 * @return
	 */
	private String getAssetPropertyNameBySide(final String side)
	{
		final String sideLower = side.toLowerCase();
		String propertyName = null;
		switch (sideLower)
		{
			case "right":
				propertyName = "rightAsset";
				break;
			case "left":
				propertyName = "leftAsset";
				break;
			default:
				throw new ApplicationException("Unknown association side: " + side);
		}
		return propertyName;
	}

	/**
	 * Handle errors from results of asset association queries.
	 * 
	 * @param label
	 * @param associations
	 */
	private void handleAssociationErrors(final String label,
			final List<AssetAssociation> associations)
	{
		if (associations == null || associations.size() <= 0)
		{
			throw new ApplicationException("Association not found");
		}
	}
}
