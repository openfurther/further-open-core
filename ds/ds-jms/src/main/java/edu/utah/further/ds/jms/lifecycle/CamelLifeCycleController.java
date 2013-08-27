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
package edu.utah.further.ds.jms.lifecycle;

import static org.apache.commons.lang.Validate.notNull;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.ds.api.lifecycle.LifeCycle;
import edu.utah.further.ds.api.lifecycle.controller.LifeCycleController;
import edu.utah.further.fqe.ds.api.service.CommandTrigger;
import edu.utah.further.fqe.ds.api.util.CommandType;
import edu.utah.further.fqe.ds.api.util.MessageHeader;

/**
 * A life cycle controller that routes requests to different life cycles based on their
 * {@link CommandType} header. {@link LifeCycleController} is coupled to the Apache Camel
 * API and is not meant to be overridden by data source developers (while
 * {@link CommandTrigger} implementations are not, and intended as the extension hooks for
 * user-defined life cycles) to control routing requests to implementation life cycles.
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
 * @version Feb 2, 2010
 */
public final class CamelLifeCycleController implements LifeCycleController
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(CamelLifeCycleController.class);

	// ========================= FIELDS ====================================

	/**
	 * The query life cycle implementation.
	 */
	private Map<String, LifeCycle<?, ?>> lifecycleMap;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate that some life cycle map has been wired to this controller.
	 */
	@PostConstruct
	protected void validateDependencies()
	{
		notNull(lifecycleMap, "lifecycleMap");
	}

	// ========================= IMPL: LifeCycleController ==================

	/**
	 * @param commandTypeLabel
	 * @param input
	 * @return
	 * @see edu.utah.further.ds.api.lifecycle.controller.LifeCycleController#triggerCommand(java.lang.String,
	 *      java.io.Serializable)
	 */
	@Override
	// Optional here, yet a useful annotation
	@Handler
	public Object triggerCommand(
			@Header(MessageHeader.COMMAND_TYPE_NAME) final String commandTypeLabel,
			@Body final Serializable input)
	{
		return triggerCommandHelper(commandTypeLabel, input);
	}

	// ========================= GET / SET =====================================

	/**
	 * Set a new value for the lifecycleMap property.
	 *
	 * @param lifecycleMap
	 *            the lifecycleMap to set
	 */
	public void setLifeCycleMap(final Map<String, LifeCycle<?, ?>> lifecycleMap)
	{
		this.lifecycleMap = lifecycleMap;
	}

	// ========================= PRIVATE METHODS ===============================

	/**
	 * Captures the wild-card as in Bloch's "Effective Java" book, page 140.
	 *
	 * @param <I>
	 * @param commandTypeLabel
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <I extends Serializable> Object triggerCommandHelper(
			final String commandTypeLabel, final I input)
	{
		// Get raw-type life cycle entry from map and run validations
		final LifeCycle<?, ?> lifeCycle = lifecycleMap.get(commandTypeLabel);
		if (lifeCycle == null)
		{
			throw new ApplicationException("Unsupported command type " + commandTypeLabel
					+ ". Life cycle map " + lifecycleMap);
		}
		if (input == null)
		{
			throw new ApplicationException("Missing command input object. Command type "
					+ commandTypeLabel);
		}
		final Class<?> supportedInputType = lifeCycle.getInputType();
		if (!ReflectionUtil.instanceOf(input, supportedInputType))
		{
			throw new ApplicationException("The command input object type "
					+ input.getClass() + " does not match the supported "
					+ "life cycle input type " + lifeCycle.getInputType()
					+ " for command type " + commandTypeLabel);
		}

		// Now that we are sure that input's type (I) is the same as the life cycle's type
		// parameter, we cast and receive and unchecked exception that can now be safely
		// suppressed
		return ((LifeCycle<I, ?>) lifeCycle).triggerCommand(input);
	}
}
