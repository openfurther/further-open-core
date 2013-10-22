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

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import edu.utah.further.fqe.ds.api.domain.IdentityResolutionType;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.mpi.api.IdentityResolutionStrategy;
import edu.utah.further.fqe.mpi.api.service.IdentityResolutionService;

/**
 * Default implementation which chooses an {@link IdentityResolutionStrategy} based on the
 * {@link IdentityResolutionType}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Oct 22, 2013
 */
public class IdentityResolutionServiceImpl implements IdentityResolutionService
{

	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(MethodHandles.lookup().lookupClass());

	// ========================= FIELDS =================================

	/**
	 * A map of identity resolution strategies keyed by resolution type
	 */
	private Map<IdentityResolutionType, IdentityResolutionStrategy> identityResolversMap;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.mpi.api.service.IdentityResolutionService#resolveIdentities
	 * (edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public void resolveIdentities(final QueryContext queryContext)
	{
		Validate.notNull(queryContext.getIdentityResolutionType(),
				"Identity resolution type required for QueryContext " + queryContext);

		if (queryContext.getResultContext().getNumRecords() < 1)
		{
			log.info("No identities to resolve, ignoring request.");
			return;
		}

		final IdentityResolutionStrategy strategy = identityResolversMap.get(queryContext
				.getIdentityResolutionType());

		log.info("Apply identity resolution stratetgy " + strategy + " for query "
				+ queryContext);

		Validate.notNull(strategy, "No strategy found for resolving identity type "
				+ queryContext.getIdentityResolutionType());

		strategy.doIdentityResolution(queryContext);
	}

	/**
	 * Return the identityResolversMap property.
	 * 
	 * @return the identityResolversMap
	 */
	public Map<IdentityResolutionType, IdentityResolutionStrategy> getIdentityResolversMap()
	{
		return identityResolversMap;
	}

	/**
	 * Set a new value for the identityResolversMap property.
	 * 
	 * @param identityResolversMap
	 *            the identityResolversMap to set
	 */
	public void setIdentityResolversMap(
			final Map<IdentityResolutionType, IdentityResolutionStrategy> identityResolversMap)
	{
		this.identityResolversMap = identityResolversMap;
	}

}
