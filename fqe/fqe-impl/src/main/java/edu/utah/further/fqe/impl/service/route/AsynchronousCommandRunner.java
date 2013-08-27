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
package edu.utah.further.fqe.impl.service.route;

import static edu.utah.further.core.util.io.LoggingUtil.debugPrintBigTitle;
import static org.apache.commons.lang.Validate.notNull;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.fqe.ds.api.util.CommandType;
import edu.utah.further.fqe.ds.api.util.MessageHeader;

/**
 * An FQE asynchronous command runner. Allows setting the message headers, body and
 * request via a builder, which creates a fully configured
 * {@link AsynchronousCommandRunner} instance with the <code>Builder#build()</code> call.
 * The command is then run using the {@link #run()} method.
 * <p>
 * Example of usage:
 * 
 * <pre>
 * return new SynchronousCommandRunner.Builder()
 * 		.commandType(DATA_QUERY)
 * 		.body(someQueryContextInstance)
 * 		.requestEndpoint(request)
 * 		.build()
 * 		.run();
 * </pre>
 * 
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Mar 8, 2010
 */
final class AsynchronousCommandRunner extends AbstractCommandRunner<Object>
{
	// ========================= COSNTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(AsynchronousCommandRunner.class);

	// ========================= FIELDS ====================================

	// ========================= NESTED TYPES ==============================

	/**
	 * A {@link AsynchronousCommandRunner} builder.
	 */
	public static class Builder implements
			edu.utah.further.core.api.lang.Builder<AsynchronousCommandRunner>
	{
		// ========================= FIELDS ====================================

		private CommandType commandType;
		private ProducerTemplate producerTemplate;
		private Endpoint requestEndpoint;

		private final Map<String, Object> headers = CollectionUtil.newMap();
		private Object body;

		// ========================= IMPL: Builder =============================

		/**
		 * Builds the list command output and returns it.
		 * 
		 * @return the list command output, based on all information gathered by this
		 *         builder instance
		 */
		@Override
		public AsynchronousCommandRunner build()
		{
			// Validate that mandatory parameters were set
			notNull(commandType, "A command type must be specified");
			notNull(requestEndpoint, "A request endpoint must be specified");

			// The fully configured runner instance
			@SuppressWarnings("synthetic-access")
			final AsynchronousCommandRunner runner = new AsynchronousCommandRunner(
					commandType, requestEndpoint, producerTemplate);
			runner.addHeaders(headers);
			runner.setBody(body);
			return runner;
		}

		// ========================= GETTERS & SETTERS =========================

		/**
		 * Set a new value for the commandType property.
		 * 
		 * @param commandType
		 *            the commandType to set
		 */
		public Builder commandType(@SuppressWarnings("hiding") final CommandType commandType)
		{
			this.commandType = commandType;
			return this;
		}

		/**
		 * Set a new value for the requestEndpoint property.
		 * 
		 * @param requestEndpoint
		 *            the requestEndpoint to set
		 * @return this object, for method chaining
		 */
		public Builder requestEndpoint(@SuppressWarnings("hiding") final Endpoint requestEndpoint)
		{
			this.requestEndpoint = requestEndpoint;
			return this;
		}
		
		/**
		 * Set a new value for the producerTemplate property.
		 * 
		 * @param producerTemplate
		 *            the producerTemplate to set
		 * @return this object, for method chaining
		 */
		public Builder producerTemplate(@SuppressWarnings("hiding") final ProducerTemplate producerTemplate)
		{
			this.producerTemplate = producerTemplate;
			return this;
		}

		/**
		 * Set a new value for the body property.
		 * 
		 * @param body
		 *            the body to set
		 * @return this object, for method chaining
		 */
		public Builder body(@SuppressWarnings("hiding") final Object body)
		{
			this.body = body;
			return this;
		}

		/**
		 * @param key
		 * @param value
		 * @return
		 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
		 */
		public Builder setHeader(final MessageHeader key, final Object value)
		{
			headers.put(key.getName(), value);
			return this;
		}

	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param commandType
	 * @param requestEndpoint
	 */
	private AsynchronousCommandRunner(final CommandType commandType,
			final Endpoint requestEndpoint, final ProducerTemplate producerTemplate)
	{
		super(commandType, requestEndpoint, producerTemplate);
	}

	// ========================= IMPL: AbstractCommandRunner ===============

	/**
	 * Run an asynchronous command.
	 * 
	 * @return <code>null</code>, always
	 * @see edu.utah.further.fqe.impl.service.route.AbstractCommandRunner#run()
	 */
	@Override
	public Object run()
	{
		// Create query context
		if (log.isTraceEnabled())
		{
			debugPrintBigTitle(log, "Running async command " + getCommandType());
		}

		// Trigger query flow
		getProducerTemplate().sendBodyAndHeaders(getRequestEndpoint(), getBody(),
				getHeaders());

		// No response expected for an asynchronous command
		return null;
	}

	// ========================= PRIVATE METHODS ===========================
}
