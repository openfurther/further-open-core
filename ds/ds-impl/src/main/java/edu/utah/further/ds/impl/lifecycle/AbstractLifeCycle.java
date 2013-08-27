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
package edu.utah.further.ds.impl.lifecycle;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.chain.RequestHandlerBuilder.chain;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.ds.api.lifecycle.LifeCycle;

/**
 * An abstract support class of a data source life cycle that can be wired with a list of
 * request processors that execute the life cycle's business logic.
 * <p>
 * This class is built for extension.
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
 * @version Feb 12, 2010
 */
public abstract class AbstractLifeCycle<I, O> implements LifeCycle<I, O>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractLifeCycle.class);

	// ========================= FIELDS ====================================

	/**
	 * Supported command input type.
	 */
	private final Class<I> inputType;

	/**
	 * The list of request processors of the query flow chain.
	 */
	private List<RequestProcessor> requestProcessors = newList();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param inputType
	 */
	public AbstractLifeCycle(final Class<I> inputType)
	{
		super();
		this.inputType = inputType;
	}

	/**
	 * @throws Exception
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		Validate.notNull("requestProcessors",
				"A request processor list must be set to define a life cycle");
	}

	// ========================= Impl: CommandTrigger ======================

	/**
	 * Return the supported input type of this life cycle.
	 *
	 * @return the supported input type
	 * @see edu.utah.further.ds.api.lifecycle.LifeCycle#getInputType()
	 */
	@Override
	public final Class<I> getInputType()
	{
		return inputType;
	}

	// ========================= GETTERS & SETTERS ==========================

	/**
	 * @param requestProcessors
	 * @see edu.utah.further.ds.api.lifecycle.LifeCycle#setRequestProcessors(java.util.List)
	 */
	@Override
	public final void setRequestProcessors(
			final List<? extends RequestProcessor> requestProcessors)
	{
		CollectionUtil.setListElements(this.requestProcessors, requestProcessors);
	}

	// ========================= PRIVATE METHODS ===============================

	/**
	 * Builds a request handler chain from the list of request processors.
	 * <p>
	 * This method should not be overridden and is declared final to preserve the
	 * integrity of this class' logic.
	 *
	 * @return a {@link RequestHandler} chain instance
	 */
	protected final RequestHandler buildChain()
	{
		final RequestHandler requestHandlerChain = chain(requestProcessors);
		if (log.isTraceEnabled())
		{
			log.trace("Original chain: " + requestHandlerChain);
		}

		return requestHandlerChain;
	}
}
