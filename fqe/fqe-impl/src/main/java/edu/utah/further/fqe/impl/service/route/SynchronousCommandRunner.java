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

import static edu.utah.further.core.util.io.LoggingUtil.debugPrintAndCenter;
import static edu.utah.further.core.util.io.LoggingUtil.debugPrintBigTitle;
import static org.apache.commons.lang.Validate.notNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.camel.CamelUtil;
import edu.utah.further.core.camel.ExchangeMatcherHeader;
import edu.utah.further.core.camel.FutureExchange;
import edu.utah.further.core.camel.FutureExchangeSingleMatcher;
import edu.utah.further.fqe.ds.api.util.CommandType;
import edu.utah.further.fqe.ds.api.util.MessageHeader;
import edu.utah.further.fqe.impl.util.FqeImplResourceLocator;

/**
 * An FQE synchronous command runner. Allows setting the message headers, request endpoint
 * and polling parameters via a builder, which creates a fully configured
 * {@link SynchronousCommandRunner} instance with the {@link Builder#build()} call. The
 * command is then run using the {@link #run()} method.
 * <p>
 * Example of usage:
 * 
 * <pre>
 * return new SynchronousCommandRunner.Builder&lt;DsMetaData&gt;()
 * 		.commandType(REMOTE_CONTROL)
 * 		.setHeader(MessageHeader.DATA_SOURCE_ID, dataSourceId)
 * 		.requestEndpoint(request)
 * 		.poll(50L, 2000L, TimeUnit.SECONDS)
 * 		.returnType(DsMetaData.class)
 * 		.fallbackValue(new DsMetaData())
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
final class SynchronousCommandRunner<T> extends AbstractCommandRunner<T>
{
	// ========================= COSNTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(SynchronousCommandRunner.class);

	// ========================= FIELDS ====================================

	/**
	 * Return type from the response queue.
	 */
	private final Class<T> returnType;

	/**
	 * Default value to return if no response is received.
	 */
	private final T fallbackValue;

	/**
	 * Polling response period (will poll for response every {@link #period} {@link #unit}
	 * time units.
	 */
	private final long period;

	/**
	 * Total period to wait for a response.
	 */
	private final long timeout;

	/**
	 * Polling time unit.
	 */
	private final TimeUnit unit;

	// ========================= NESTED TYPES ==============================

	/**
	 * A {@link SynchronousCommandRunner} builder.
	 */
	public static class Builder<T> implements
			edu.utah.further.core.api.lang.Builder<SynchronousCommandRunner<T>>
	{
		// ========================= FIELDS ====================================

		private CommandType commandType;
		private Class<T> returnType;
		private T fallbackValue;
		private Endpoint requestEndpoint;
		private ProducerTemplate producerTemplate;

		private long period = 50L;
		private long timeout = 2000L;
		private TimeUnit unit = TimeUnit.SECONDS;

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
		public SynchronousCommandRunner<T> build()
		{
			// Validate that mandatory parameters were set
			notNull(commandType, "A command type must be specified");
			notNull(returnType, "A retur type must be specified");
			notNull(fallbackValue, "An fall-back value must be specified");
			notNull(requestEndpoint, "An request endpoint must be specified");
			notNull(producerTemplate, "A ProducerTemplate must be specified");

			// The fully configured runner instance
			final SynchronousCommandRunner<T> runner = new SynchronousCommandRunner<>(
					commandType, returnType, fallbackValue, requestEndpoint,
					producerTemplate, period, timeout, unit);
			runner.addHeaders(headers);
			runner.setBody(body);
			return runner;
		}

		// ========================= GETTERS & SETTERS =========================

		/**
		 * Set a polling period.
		 * 
		 * @param period
		 * @param timeout
		 * @param unit
		 * @return
		 */
		public Builder<T> poll(@SuppressWarnings("hiding") final long period,
				@SuppressWarnings("hiding") final long timeout,
				@SuppressWarnings("hiding") final TimeUnit unit)
		{
			Validate.isTrue(period > 0, "Polling period must be positive");
			Validate.isTrue(timeout > 0, "Time out period must be positive");
			Validate.isTrue(timeout >= period,
					"Polling period cannot be larger than timeout period");
			this.period = period;
			this.timeout = timeout;
			this.unit = unit;
			return this;
		}

		/**
		 * Set a new value for the commandType property.
		 * 
		 * @param commandType
		 *            the commandType to set
		 */
		public Builder<T> commandType(
				@SuppressWarnings("hiding") final CommandType commandType)
		{
			this.commandType = commandType;
			return this;
		}

		/**
		 * Set a new value for the returnType property.
		 * 
		 * @param returnType
		 *            the returnType to set
		 */
		public Builder<T> returnType(@SuppressWarnings("hiding") final Class<T> returnType)
		{
			this.returnType = returnType;
			return this;
		}

		/**
		 * Set a new value for the fallbackValue property.
		 * 
		 * @param fallbackValue
		 *            the fallbackValue to set
		 */
		public Builder<T> fallbackValue(@SuppressWarnings("hiding") final T fallbackValue)
		{
			this.fallbackValue = fallbackValue;
			return this;
		}

		/**
		 * Set a new value for the requestEndpoint property.
		 * 
		 * @param requestEndpoint
		 *            the requestEndpoint to set
		 * @return this object, for method chaining
		 */
		public Builder<T> requestEndpoint(
				@SuppressWarnings("hiding") final Endpoint requestEndpoint)
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
		public Builder<T> producerTemplate(
				@SuppressWarnings("hiding") final ProducerTemplate producerTemplate)
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
		public Builder<T> body(@SuppressWarnings("hiding") final Object body)
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
		public Builder<T> setHeader(final MessageHeader key, final Object value)
		{
			headers.put(key.getName(), value);
			return this;
		}

	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param commandType
	 * @param returnType
	 * @param fallbackValue
	 * @param requestEndpoint
	 * @param period
	 * @param timeout
	 * @param unit
	 */
	private SynchronousCommandRunner(final CommandType commandType,
			final Class<T> returnType, final T fallbackValue,
			final Endpoint requestEndpoint, final ProducerTemplate producerTemplate,
			final long period, final long timeout, final TimeUnit unit)
	{
		super(commandType, requestEndpoint, producerTemplate);
		this.returnType = returnType;
		this.fallbackValue = fallbackValue;
		this.period = period;
		this.timeout = timeout;
		this.unit = unit;
	}

	// ========================= IMPL: AbstractCommandRunner ===============

	/**
	 * @return
	 * @see edu.utah.further.fqe.impl.service.route.AbstractCommandRunner#run()
	 */
	@Override
	public T run()
	{
		// Create query context
		debugPrintBigTitle(log, "Running sync command " + getCommandType());

		// Initialize result listener
		debugPrintAndCenter(log, "Start listening to exchanges");
		final FutureExchange futureExchange = new FutureExchangeSingleMatcher(
				FqeImplResourceLocator.getInstance().getEndpointConsumer(),
				new ExchangeMatcherHeader(MessageHeader.COMMAND_ID.getName(),
						getQueryId()));

		// Trigger query flow
		debugPrintAndCenter(log, "Init query flow");
		getProducerTemplate().sendBodyAndHeaders(getRequestEndpoint(), getBody(),
				getHeaders());

		// Could run in-between operations here in a real implementation...

		// Poll for results
		debugPrintAndCenter(log, "Poll");
		final Exchange result = futureExchange.poll(period, timeout, unit);

		// Clean up to prevent memory leaks due to dead future exchange references in
		// EndpointConsumer
		debugPrintAndCenter(log, "Cancel");
		futureExchange.cancel(true);

		// Return results, or an empty Data if no results were returned
		return CamelUtil.getMessageBody(result, returnType, fallbackValue);
	}

	// ========================= PRIVATE METHODS ===========================
}
