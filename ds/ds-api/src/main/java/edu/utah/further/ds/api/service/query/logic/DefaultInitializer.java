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
package edu.utah.further.ds.api.service.query.logic;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import edu.utah.further.core.api.chain.AttributeContainer;
import edu.utah.further.core.api.chain.AttributeContainerImpl;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.ds.api.service.query.AnswerableService;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.IdentityResolutionType;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.to.ResultContextToImpl;

/**
 * The default implementation of the {@link Initializer} responsible for determining
 * whether or not the query can be answered, whether or not the user is allowed to answer
 * the query, and the setup of attributes used throughout the chain request.
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
 * @version Apr 13, 2010
 */
public class DefaultInitializer implements Initializer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(DefaultInitializer.class);

	// ========================= FIELDS ====================================

	/**
	 * The union (more precisely, overlay in the list order) of {@link #containers}.
	 */
	private final AttributeContainer mainContainer = new AttributeContainerImpl();

	/**
	 * Service for determining whether or not a data source can answer a query
	 */
	private AnswerableService answerableService;

	/**
	 * An access decision manager for determining authorization
	 */
	private AccessDecisionManager accessDecisionManager;

	// ========================= IMPL: Initializer =========================

	/**
	 * Delegates to implementation of {@link AnswerableService}
	 * 
	 * @see edu.utah.further.ds.api.service.query.logic.Initializer#canAnswer(edu.utah.further.fqe.ds.api.domain.QueryContext,
	 *      edu.utah.further.fqe.ds.api.domain.DsMetaData)
	 */
	@Override
	public boolean canAnswer(final QueryContext queryContext, final DsMetaData dsMetaData)
	{
		Validate.notNull(answerableService, "AnswerableService");
		return answerableService.canAnswer(queryContext, dsMetaData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.Initializer#initialize(edu.utah.further
	 * .fqe.ds.api.domain.QueryContext, edu.utah.further.fqe.ds.api.domain.DsMetaData)
	 */
	@Override
	public void initialize(final QueryContext queryContext, final DsMetaData metaData)
	{
		// Provide hook to create result context
		queryContext.setResultContext(getResultContext());

		// Get how this adapter should resolve identities
		final IdentityResolutionType idResolutionType = mainContainer
				.getAttribute(AttributeName.ID_RESOLUTION_TYPE);

		queryContext.setIdentityResolutionType(idResolutionType);

		// Start processing immediately
		queryContext.queue();
		queryContext.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.Initializer#authorize(org.springframework
	 * .security.core.Authentication, edu.utah.further.fqe.ds.api.domain.QueryContext,
	 * edu.utah.further.fqe.ds.api.domain.DsMetaData)
	 */
	@Override
	public void authorize(final Authentication authentication,
			final QueryContext queryContext, final DsMetaData metaData)
	{
		if (accessDecisionManager == null)
		{
			log.warn("No access decision manager found. "
					+ "No access decisions will be made");
			return;
		}

		// Need to have at least one config attribute even though it's null otherwise
		// voters will not be called upon.
		final List<ConfigAttribute> attributes = CollectionUtil.newList();
		attributes.add(null);

		accessDecisionManager.decide(authentication, this, attributes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.ds.api.service.query.logic.Initializer#getAttributes()
	 */
	@Override
	public Map<String, Object> getAttributes()
	{
		return mainContainer.getAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.Initializer#setAttributes(java.util
	 * .Map)
	 */
	@Override
	public void setAttributes(final Map<String, Object> attributes)
	{
		mainContainer.addAttributes(attributes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.logic.Initializer#setNamedAttributes(java
	 * .util.Map)
	 */
	@Override
	public void setNamedAttributes(final Map<AttributeName, Object> attributes)
	{
		for (final Map.Entry<AttributeName, Object> entry : attributes.entrySet())
		{
			mainContainer.setAttribute(entry.getKey().getLabel(), entry.getValue());
		}
	}

	// ========================= GET & SET =================================

	/**
	 * Set a new value for the answerableService property.
	 * 
	 * @param answerableService
	 *            the answerableService to set
	 */
	public void setAnswerableService(final AnswerableService answerableService)
	{
		this.answerableService = answerableService;
	}

	/**
	 * Set a new value for the accessDecisionManager property.
	 * 
	 * @param accessDecisionManager
	 *            the accessDecisionManager to set
	 */
	public void setAccessDecisionManager(final AccessDecisionManager accessDecisionManager)
	{
		this.accessDecisionManager = accessDecisionManager;
	}

	// ========================= METHODS ===================================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Returns a new instance of the {@link ResultContext} for this data source. A hook
	 * for sub-classes.
	 * 
	 * @return new {@link ResultContext} instance
	 */
	protected ResultContext getResultContext()
	{
		final ResultContext resultContext = mainContainer
				.getAttribute(AttributeName.RESULT_CONTEXT);
		if (resultContext == null)
		{
			log.info("No preconfigured ResultContext found, creating default");
			return new ResultContextToImpl();
		}

		return resultContext;
	}
}
