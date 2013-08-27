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
package edu.utah.further.core.chain;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;

import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.api.lang.CoreUtil;

/**
 * A {@link RequestHandler} builder for building request handler chains.
 * <p>
 * Processor setting take precedence over handlers if set.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}, Oren E. Livne
 *         {@code <oren.livne@utah.edu>}
 * @version Feb 2, 2010
 */
public final class RequestHandlerBuilder implements Builder<RequestHandler>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(RequestHandlerBuilder.class);

	// ========================= NESTED TYPES ==============================

	/**
	 * Supported {@link RequestHandler} implementations.
	 */
	public static enum Type
	{
		SIMPLE, CHAIN, LOOP
	}

	// ========================= FIELDS ====================================

	/**
	 * Required parameter: {@link RequestHandler} type.
	 */
	private final Type type;

	/**
	 * Request handlers to build a composite (chain/loop) implementation from, if set.
	 */
	private final List<RequestHandler> handlers = newList();

	/**
	 * The single processor to build a <code>SIMPLE</code> request handler from.
	 */
	private RequestProcessor processor = null;

	/**
	 * The next node in the chain..
	 */
	private RequestHandler nextNode = null;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a builder with required parameters.
	 *
	 * @param type
	 *            {@link RequestHandler} implementation type
	 */
	public RequestHandlerBuilder(final Type type)
	{
		super();
		this.type = type;
	}

	// ========================= Impl: Builder<RequestHandler> =============

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public RequestHandler build()
	{
		switch (type)
		{
			case SIMPLE:
			{
				return new RequestHandlerSimpleImpl(processor);
			}

			case CHAIN:
			{
				return new RequestHandlerChainImpl(handlers, nextNode);
			}

			case LOOP:
			{
				throw CoreUtil.newUnsupportedOperationExceptionNotImplementedYet();
			}

			default:
			{
				throw new UnsupportedOperationException(
						"Unrecognized RequestHandler type: " + type);
			}
		}
	}

	// ========================= STATIC METHODS ============================

	/**
	 * Return a new instance of a simple, single-processor request handler. This is a
	 * convenient factory method instead of calling the full-fledged builder class to
	 * construct a simple request handler.
	 *
	 * @param processor
	 *            request processor
	 * @return simple request handler instance
	 */
	public static RequestHandler simple(final RequestProcessor processor)
	{
		return new RequestHandlerSimpleImpl(processor);
	}

	/**
	 * Return a new instance of a request handler chain builder.
	 *
	 * @return request handler chain with no processors
	 */
	public static RequestHandlerBuilder chainBuilder()
	{
		return new RequestHandlerBuilder(Type.CHAIN);
	}

	/**
	 * Return a new instance of a request handler chain with processors.
	 *
	 * @param processors
	 *            list of processors
	 * @return chain instance
	 */
	public static RequestHandler chain(final List<? extends RequestProcessor> processors)
	{
		return chainBuilder().setProcessors(processors).build();
	}

	/**
	 * Return a new instance of a request handler chain with processors.
	 *
	 * @param processors
	 *            list of processors
	 * @return chain instance
	 */
	public static RequestHandler chain(final RequestProcessor... processors)
	{
		return chainBuilder().setProcessors(processors).build();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Add a request processor.
	 *
	 * @param aProcessor
	 *            a request processor
	 * @return this, for method chaining
	 */
	public RequestHandlerBuilder addProcessor(final RequestProcessor aProcessor)
	{
		return addHandler(new RequestHandlerSimpleImpl(aProcessor));
	}

	/**
	 * Set a new value for the processors property.
	 *
	 * @param processorArray
	 *            the processors to set
	 * @return this, for method chaining
	 */
	@SafeVarargs
	public final <T extends RequestProcessor> RequestHandlerBuilder setProcessors(
			final T... processorArray)
	{
		return setProcessors(CollectionUtil.newList(processorArray));
	}

	/**
	 * Set a new value for the processors property.
	 *
	 * @param processors
	 *            the processors to set
	 * @return this, for method chaining
	 */
	public RequestHandlerBuilder setProcessors(
			final List<? extends RequestProcessor> processors)
	{
		return setHandlers(toHandlerList(processors));
	}

	/**
	 * Set a single processor for the request handler. Appropriate for the
	 * <code>SIMPLE</code> implementation.
	 *
	 * @param processor
	 *            the processors to set
	 * @return this, for method chaining
	 */
	public RequestHandlerBuilder setProcessor(final RequestProcessor processor)
	{
		this.processor = processor;
		return this;
	}

	/**
	 * Add a request handler.
	 *
	 * @param handler
	 *            a request handler
	 * @return this, for method chaining
	 */
	public RequestHandlerBuilder addHandler(final RequestHandler handler)
	{
		handlers.add(handler);
		return this;
	}

	/**
	 * Set a new value for the handlers property.
	 *
	 * @param handlers
	 *            the handlers to set
	 * @return this, for method chaining
	 */
	public RequestHandlerBuilder setHandlers(final List<? extends RequestHandler> handlers)
	{
		CollectionUtil.setListElements(this.handlers, handlers);
		return this;
	}

	/**
	 * Set a new value for the nextNode property.
	 *
	 * @param nextNode
	 *            the nextNode to set
	 * @return this, for method chaining
	 */
	public RequestHandlerBuilder setNextNode(final RequestHandler nextNode)
	{
		this.nextNode = nextNode;
		return this;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * A utility method to wrap a processors by handles.
	 *
	 * @param processorList
	 *            processor list
	 * @return corresponding handler list
	 */
	public static final List<RequestHandler> toHandlerList(
			final List<? extends RequestProcessor> processorList)
	{
		final List<RequestHandler> wrappingHandlerList = CollectionUtil.newList();
		for (final RequestProcessor processor : processorList)
		{
			wrappingHandlerList.add(simple(processor));
		}
		return wrappingHandlerList;
	}
}
