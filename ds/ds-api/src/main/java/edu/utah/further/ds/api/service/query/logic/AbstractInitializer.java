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
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import edu.utah.further.core.api.chain.AttributeContainer;
import edu.utah.further.core.api.chain.AttributeContainerImpl;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.ds.api.service.query.AnswerableService;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.to.ResultContextToImpl;

/**
 * Abstract Query Processor provides default behavior.
 * <p>
 * Unlike other Query Processors, the initializer is somewhat special in that it has a
 * required behavior. {@link AbstractInitializer} provides that required behavior and
 * hooks in order for the data source flow to execute.
 * <p>
 * This class was built for extension.
 * <ul>
 * <li>call canAnswer to determine whether or not this data source can answer this query</li>
 * <li>check if the data source is INACTIVE, if so, terminate</li>
 * <li>create a child QueryContext from the federated QueryContext and set the parent to
 * the federated QueryContext</li>
 * <li>inject any attributes for future Query Processors</li>
 * </ul>
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
public abstract class AbstractInitializer implements Initializer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AbstractInitializer.class);

	// ========================= FIELDS ====================================

	/**
	 * The union (more precisely, overlay in the list order) of {@link #containers}.
	 */
	private final AttributeContainer mainContainer = new AttributeContainerImpl();

	/**
	 * Service for determing whether or not a data source can answer a query
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

	/**
	 * Add all key-value pairs loaded from the properties file as request attributes.
	 * 
	 * @return
	 * @see edu.utah.further.ds.api.service.query.processor.AbstractInitializerQp#getInjectedAttributes()
	 */
	@Override
	public Map<String, Object> getInjectedAttributes()
	{
		return mainContainer.getAttributes();
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

	// ========================= GET & SET =================================

	/**
	 * Set a new value for the containers property.
	 * 
	 * @param containers
	 *            the containers to set
	 */
	public void setContainers(final List<AttributeContainer> containers)
	{
		for (final AttributeContainer container : containers)
		{
			addContainer(container);
		}
	}

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

	/**
	 * @param <T>
	 * @param label
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttribute(edu.utah.further.core.api.context.Labeled)
	 */
	@Override
	public <T> T getAttribute(final Labeled label)
	{
		return mainContainer.<T> getAttribute(label);
	}

	/**
	 * @param label
	 * @param value
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttribute(edu.utah.further.core.api.context.Labeled,
	 *      java.lang.Object)
	 */
	@Override
	public void setAttribute(final Labeled label, final Object value)
	{
		mainContainer.setAttribute(label, value);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttributes()
	 */
	@Override
	public Map<String, Object> getAttributes()
	{
		return mainContainer.getAttributes();
	}

	/**
	 * @param attributes
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttributes(java.util.Map)
	 */
	@Override
	public void setAttributes(final Map<String, ?> attributes)
	{
		mainContainer.setAttributes(attributes);
	}

	/**
	 * @param map
	 * @see edu.utah.further.core.api.chain.AttributeContainer#addAttributes(java.util.Map)
	 */
	@Override
	public void addAttributes(final Map<String, ?> map)
	{
		mainContainer.addAttributes(map);
	}

	/**
	 * @param <T>
	 * @param name
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttribute(java.lang.String)
	 */
	@Override
	public <T> T getAttribute(final String name)
	{
		return mainContainer.<T> getAttribute(name);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.chain.AttributeContainer#getAttributeNames()
	 */
	@Override
	public Set<String> getAttributeNames()
	{
		return mainContainer.getAttributeNames();
	}

	/**
	 * 
	 * @see edu.utah.further.core.api.chain.AttributeContainer#removeAllAttributes()
	 */
	@Override
	public void removeAllAttributes()
	{
		mainContainer.removeAllAttributes();
	}

	/**
	 * @param key
	 * @see edu.utah.further.core.api.chain.AttributeContainer#removeAttribute(java.lang.String)
	 */
	@Override
	public void removeAttribute(final String key)
	{
		mainContainer.removeAttribute(key);
	}

	/**
	 * @param key
	 * @param value
	 * @see edu.utah.further.core.api.chain.AttributeContainer#setAttribute(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void setAttribute(final String key, final Object value)
	{
		mainContainer.setAttribute(key, value);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Returns a new instance of the {@link ResultContext} for this data source. A hook
	 * for sub-classes.
	 * 
	 * @return new {@link ResultContext} instance
	 */
	protected ResultContext getResultContext()
	{
		return new ResultContextToImpl();
	}

	/**
	 * Overlay an attribute container over the current main attribute container.
	 * 
	 * @param container
	 *            attribute container to add (overlay) to this object
	 */
	private void addContainer(final AttributeContainer container)
	{
		this.mainContainer.addAttributes(container.getAttributes());
	}
}
