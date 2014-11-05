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
package edu.utah.further.fqe.mpi.impl.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedSingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.data.util.SqlUtil;
import edu.utah.further.fqe.mpi.api.IdTranslationProvider;
import edu.utah.further.fqe.mpi.api.Identifier;
import edu.utah.further.fqe.mpi.api.service.IdentifierService;
import edu.utah.further.fqe.mpi.impl.domain.IdentifierEntity;
import edu.utah.further.dts.api.util.HardcodedNamespace;

/**
 * Identifier service which generates arbitrary new identifiers or uses the passed
 * criteria to determine if an id already exists, otherwise creating a new identifier. The
 * identifiers are valid for the life of the JVM.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Nov 1, 2011
 */
@Service("identifierService")
public final class IdentifierServiceImpl implements IdentifierService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(IdentifierServiceImpl.class);

	// ========================= FIELDS =================================

	/**
	 * Ideally we could use a UUID but a number will be most compatible as an identifier
	 * as more likely than not the database field will be numeric. Therefore, we use an
	 * AtomicLong as a portable sequence generator since not all databases do sequences in
	 * the same fashion. Realistically we only care about uniqueness within a given set of
	 * query ids (a set because of the parent->child relationship between federated and
	 * individual query identifiers). AtomicLong ensures uniqueness for the life of the
	 * JVM.
	 */
	private final AtomicLong sequencer = new AtomicLong(0);

	/**
	 * A data access object for querying for the federated ID
	 */
	@Autowired
	private Dao identifierDao;

	/**
	 * SessionFactory to create HQL queries
	 */
	@Autowired
	private SessionFactory identifierSessionFactory;

	/**
	 * Virtual repository jdbc template for querying
	 */
	@Autowired
	private JdbcTemplate identifierJdbcTemplate;

	/**
	 * A 'simpler' version of JdbcTemplate
	 */
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	/**
	 * Id Translation providers
	 * 
	 * Unfortunately this dependency must be a named {@link Resource} for proper
	 * injection: translationProviders
	 */
	@Resource(name = "translationProviders")
	private Map<String, IdTranslationProvider> translatorProviders;

	// =================== IMPL:IdentifierService =================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mpi.api.IdentifierService#generateNewId()
	 */
	@Override
	public Long generateNewId()
	{
		return new Long(sequencer.getAndIncrement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mpi.api.IdentifierService#generateId(java.lang.Object)
	 */
	@Override
	@Transactional(value = "identifierTransactionManager")
	public Long generateId(final Identifier id)
	{
		final IdentifierEntity idEntity = IdentifierEntity.newCopy(id);

		final List<IdentifierEntity> existingId = identifierDao.findByExample(idEntity,
				false, "createDate", "createdBy");

		// We should only ever return 1 because the criteria for each identifier should be
		// unique
		if (existingId.size() > 1)
		{
			if (log.isErrorEnabled())
			{
				log.error("There was a problem with the requested identifier. Found "
						+ existingId.size() + " but expected 1.");
			}

			throw new ApplicationException(
					"Unexpected number of identifiers found for id " + id);
		}

		// We've already encountered this id before and have a virtual id for it
		if (existingId.size() == 1)
		{
			if (log.isTraceEnabled())
			{
				log.trace("Found existing identifier, "
						+ "returning already generated virtual id");
			}
			return existingId.get(0).getVirtualId();
		}

		if (log.isTraceEnabled())
		{
			log.trace("Did not find existing identifier based on given id.");
		}

		if (log.isTraceEnabled())
		{
			log.trace("No common federated identifier "
					+ "exists to use for lookup, creating new virtual id.");
		}

		final Long virtualId = new Long(sequencer.getAndIncrement());

		idEntity.setVirtualId(virtualId);

		if (log.isTraceEnabled())
		{
			log.trace("Persisting identifier for future lookups.");
		}

		// Persist all details and the generated virtual id
		identifierDao.save(idEntity);

		return idEntity.getVirtualId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.mpi.api.IdTranslationProvider#translateIds(java.util.List,
	 * java.lang.Class)
	 */
	@Override
	public List<Long> translateIds(final List<Long> virtualFederatedIds,
			final String dataSourceId)
	{
		final Long dataSourceNumericId;
		
		try
		{
			// TODO: Lookup dataSourceId numeric identifier - dataSourceId is currently string
			// like "UUEDW" - convert any - to _, and uppercase to match Enum
			dataSourceNumericId = new Long(HardcodedNamespace.valueOf(dataSourceId.replaceAll("-", "_").toUpperCase()).getId());
		}
		catch(final Exception ex)
		{
			throw new ApplicationException(
					"Unable to convert Datasource Id to numeric value: " + dataSourceId);
		}
		
		final List<Long> args = virtualFederatedIds;
		
			final String stmt = "SELECT fed_obj_id FROM virtual_obj_id_map WHERE "
					+ "src_obj_nmspc_id = ? AND fed_obj_id IS NOT NULL AND "
					+ SqlUtil.unlimitedInValues(virtualFederatedIds,
						"virtual_obj_id");
		
		args.add(0, dataSourceNumericId);

		// Our input is actually virtual federated ids and not federated ids - we have to
		// translate to the actual federated id before we can do a lookup.
		final List<Long> translatedVirtualIds = identifierJdbcTemplate
				.queryForList(stmt, args.toArray(), Long.class);

		if (translatedVirtualIds.size() == 0)
		{
			throw new ApplicationException(
					"Unable to find any common federated ids from virtual federated ids");
		}

		return translatorProviders
				.get(HardcodedNamespace.valueOf(dataSourceId.replaceAll("-", "_").toUpperCase()).getName())
				.translateToSourceIds(translatedVirtualIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.mpi.api.service.IdentifierService#getIdentifiers(java.util
	 * .List)
	 */
	@Override
	public List<Long> getVirtualIdentifiers(final List<String> queryIds)
	{
		return getSimpleJdbcTemplate()
				.query("SELECT virtual_obj_id FROM virtual_obj_id_map WHERE query_id IN (:queryIds)",
						(RowMapper<Long>) new ParameterizedSingleColumnRowMapper<Long>(),
						Collections.singletonMap("queryIds", queryIds));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.mpi.api.service.IdentifierService#getIdentifiers(java.util
	 * .List)
	 */
	@Override
	public List<Long> getSourceIdentifiers(final List<String> queryIds)
	{
		return getSimpleJdbcTemplate()
				.query("SELECT CAST(TRIM(src_obj_id) AS BIGINT) FROM virtual_obj_id_map WHERE query_id IN (:queryIds) AND LENGTH(TRIM(src_obj_id)) > 0",
						(RowMapper<Long>) new ParameterizedSingleColumnRowMapper<Long>(),
						Collections.singletonMap("queryIds", queryIds));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.mpi.api.service.IdentifierService#getUnresolvedVirtualIdentifiers
	 * (java.util.List)
	 */
	@Override
	public List<Long> getUnresolvedVirtualIdentifiers(final List<String> queryIds)
	{
		final List<Identifier> identifiers = getUnresolvedIdentifiers(queryIds);
		final List<Long> virtualIdentifiers = new ArrayList<>();
		for (final Identifier identifier : identifiers)
		{
			virtualIdentifiers.add(identifier.getVirtualId());
		}

		return virtualIdentifiers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.mpi.api.service.IdentifierService#getUnresolvedIdentifiers
	 * (java.lang.String)
	 */
	@Override
	@Transactional(value = "identifierTransactionManager")
	public List<Identifier> getUnresolvedIdentifiers(final String queryId)
	{
		return getUnresolvedIdentifiers(Arrays.asList(queryId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.mpi.api.service.IdentifierService#getUnresolvedIdentifiers
	 * (java.util.List)
	 */
	@Override
	@Transactional(value = "identifierTransactionManager")
	public List<Identifier> getUnresolvedIdentifiers(final List<String> queryIds)
	{
		Validate.notNull(queryIds, "queryId is required for identity resolution");

		// get all identifiers that have a null federated id so we can fill them in
		final Query identifierQuery = identifierSessionFactory
				.getCurrentSession()
				.createQuery(
						"from IdentifierEntity as identifier where "
								+ "identifier.commonId is null "
								+ "and identifier.queryId IN (:queryIds)");
		identifierQuery.setParameterList("queryIds", queryIds);

		return identifierQuery.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.mpi.api.service.IdentifierService#updateSavedIdentifiers(java
	 * .util.List)
	 */
	@Override
	public void updateSavedIdentifiers(
			final List<? extends PersistentEntity<?>> identifiers)
	{
		for (final PersistentEntity<?> identifier : identifiers)
		{
			identifierDao.update(identifier);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.mpi.api.service.IdentifierService#getCommonIdToVirtualIdMap
	 * (java.util.List, boolean)
	 */
	@Override
	@Transactional(value = "identifierTransactionManager")
	public Map<Long, Set<Long>> getCommonIdToVirtualIdMap(final List<String> queryIds,
			final boolean orderedVirtualIds)
	{
		Validate.notNull(queryIds,
				"queryIds are required for create a common id to virtual id mapping");

		// get all identifiers that have a null federated id so we can fill them in
		final Query identifierQuery = identifierSessionFactory
				.getCurrentSession()
				.createQuery(
						"SELECT DISTINCT "
								+ "new map(identifier.commonId as common, identifier.virtualId as virtual) "
								+ "from IdentifierEntity as identifier "
								+ "where identifier.commonId is not null "
								+ "and identifier.queryId IN (:queryIds)");
		identifierQuery.setParameterList("queryIds", queryIds);

		final List<Map<String, Long>> results = identifierQuery.list();
		final Map<Long, Set<Long>> commonToVirtualMap = new HashMap<>();
		for (final Map<String, Long> result : results)
		{
			final Long common = result.get("common");
			if (commonToVirtualMap.containsKey(common))
			{
				commonToVirtualMap.get(common).add(result.get("virtual"));
			}
			else
			{
				Set<Long> virtuals = null;

				if (orderedVirtualIds)
				{
					virtuals = new TreeSet<>();
				}
				else
				{
					virtuals = new HashSet<>();
				}

				virtuals.add(result.get("virtual"));

				commonToVirtualMap.put(common, virtuals);
			}
		}

		return commonToVirtualMap;
	}

	/**
	 * Return the identifierDao property.
	 * 
	 * @return the identifierDao
	 */
	public Dao getIdentifierDao()
	{
		return identifierDao;
	}

	/**
	 * Set a new value for the identifierDao property.
	 * 
	 * @param identifierDao
	 *            the identifierDao to set
	 */
	public void setIdentifierDao(final Dao identifierDao)
	{
		this.identifierDao = identifierDao;
	}

	/**
	 * Return the identifierJdbcTemplate property.
	 * 
	 * @return the identifierJdbcTemplate
	 */
	public JdbcTemplate getIdentifierJdbcTemplate()
	{
		return identifierJdbcTemplate;
	}

	/**
	 * Set a new value for the identifierJdbcTemplate property.
	 * 
	 * @param identifierJdbcTemplate
	 *            the identifierJdbcTemplate to set
	 */
	public void setIdentifierJdbcTemplate(final JdbcTemplate identifierJdbcTemplate)
	{
		this.identifierJdbcTemplate = identifierJdbcTemplate;
	}

	/**
	 * Return the simpleJdbcTemplate property.
	 * 
	 * @return the simpleJdbcTemplate
	 */
	public SimpleJdbcTemplate getSimpleJdbcTemplate()
	{
		if (simpleJdbcTemplate == null)
		{
			setSimpleJdbcTemplate(new SimpleJdbcTemplate(identifierJdbcTemplate));
		}

		return simpleJdbcTemplate;
	}

	/**
	 * Set a new value for the simpleJdbcTemplate property.
	 * 
	 * @param simpleJdbcTemplate
	 *            the simpleJdbcTemplate to set
	 */
	private void setSimpleJdbcTemplate(final SimpleJdbcTemplate simpleJdbcTemplate)
	{
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
