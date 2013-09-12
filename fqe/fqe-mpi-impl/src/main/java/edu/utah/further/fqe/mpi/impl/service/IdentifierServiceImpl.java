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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.data.util.SqlUtil;
import edu.utah.further.fqe.mpi.api.Identifier;
import edu.utah.further.fqe.mpi.api.IdentifierService;
import edu.utah.further.fqe.mpi.impl.domain.IdentifierEntity;

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
	private Dao daoFqeMpi;

	/**
	 * Virtual repository jdbc template for querying
	 */
	@Autowired
	private JdbcTemplate jdbcTemplateFqeMpi;

	/**
	 * A 'simpler' version of JdbcTemplate
	 */
	private SimpleJdbcTemplate simpleJdbcTemplate;

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
	@Transactional
	public Long generateId(final Identifier id)
	{
		final IdentifierEntity idEntity = IdentifierEntity.newCopy(id);

		final List<IdentifierEntity> existingId = daoFqeMpi.findByExample(idEntity,
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
		daoFqeMpi.save(idEntity);

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
		// TODO: Lookup dataSourceId numeric identifier - dataSourceId is currently string
		// like "UUEDW"
		final Long dataSourceNumericId = Long.valueOf(dataSourceId);

		final List<Long> args = virtualFederatedIds;
		args.add(0, dataSourceNumericId);

		// Our input is actually virtual federated ids and not federated ids - we have to
		// translate to the actual federated id before we can do a lookup.
		final List<Long> translatedVirtualIds = jdbcTemplateFqeMpi
				.queryForList(
						"SELECT fed_obj_id FROM virtual_obj_id_map WHERE "
								+ "src_obj_nmspc_id = ? AND "
								+ SqlUtil.unlimitedInValues(virtualFederatedIds,
										"virtual_obj_id"), args.toArray(), Long.class);

		if (translatedVirtualIds.size() == 0)
		{
			throw new ApplicationException(
					"Unable to find any common federated ids from virtual federated ids");
		}

		return translatedVirtualIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.mpi.api.IdentifierService#getIdentifiers(java.util.List)
	 */
	@Override
	public List<Long> getVirtualIdentifiers(final List<String> queryIds)
	{
		final List<Map<String, Object>> results = getSimpleJdbcTemplate()
				.queryForList(
						"SELECT virtual_obj_id FROM virtual_obj_id_map WHERE query_id IN :queryIds",
						Collections.singletonMap("queryIds", queryIds));

		final List<Long> ids = new ArrayList<>();
		for (final Map<String, Object> result : results)
		{
			ids.add((Long) result.get("virtual_obj_id"));
		}

		return ids;
	}

	/**
	 * Return the daoFqeMpi property.
	 * 
	 * @return the daoFqeMpi
	 */
	public Dao getDaoFqeMpi()
	{
		return daoFqeMpi;
	}

	/**
	 * Set a new value for the daoFqeMpi property.
	 * 
	 * @param daoFqeMpi
	 *            the daoFqeMpi to set
	 */
	public void setDaoFqeMpi(final Dao daoFqeMpi)
	{
		this.daoFqeMpi = daoFqeMpi;
	}

	/**
	 * Return the jdbcTemplateFqeMpi property.
	 * 
	 * @return the jdbcTemplateFqeMpi
	 */
	public JdbcTemplate getJdbcTemplateFqeMpi()
	{
		return jdbcTemplateFqeMpi;
	}

	/**
	 * Set a new value for the jdbcTemplateFqeMpi property.
	 * 
	 * @param jdbcTemplateFqeMpi
	 *            the jdbcTemplateFqeMpi to set
	 */
	public void setJdbcTemplateFqeMpi(final JdbcTemplate jdbcTemplateFqeMpi)
	{
		this.jdbcTemplateFqeMpi = jdbcTemplateFqeMpi;
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
			setSimpleJdbcTemplate(new SimpleJdbcTemplate(jdbcTemplateFqeMpi));
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
