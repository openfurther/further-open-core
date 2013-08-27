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

import static edu.utah.further.core.chain.RequestHandlerBuilder.simple;
import static edu.utah.further.core.chain.RequestHandlerBuilder.toHandlerList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;

import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * A request handler closed-loop chain. To terminate the chain, one of the chain pieces
 * must signal that the request has been handled. This is usually the task of a dedicated
 * termination criteria handler.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Sep 28, 2009
 */
class RequestHandlerLoopImpl extends RequestHandlerChainImpl
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(RequestHandlerLoopImpl.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a request handler loop from processors.
	 *
	 * @param start
	 *            start handler for the loop; initializes loop counters
	 * @param body
	 *            A non-null List of loop body handlers in their order of invocation
	 * @param end
	 *            end handler for the loop; specifies the termination criterion
	 * @param nextNode
	 *            The next node to invoke in a super-chain after this chain is completed.
	 *            If <code>null</code>, the request processing chain will terminate after
	 *            this handler handles the request.
	 */
	public RequestHandlerLoopImpl(final RequestProcessor start,
			final List<? extends RequestProcessor> body, final RequestProcessor end,
			final RequestHandler nextNode)
	{
		super(createHandlerList(simple(start), toHandlerList(body), simple(end)),
				nextNode);
	}

	/**
	 * Create a request handler loop from handlers.
	 *
	 * @param start
	 *            start handler for the loop; initializes loop counters
	 * @param body
	 *            A non-null List of loop body handlers in their order of invocation
	 * @param end
	 *            end handler for the loop; specifies the termination criterion
	 * @param nextNode
	 *            The next node to invoke in a super-chain after this chain is completed.
	 *            If <code>null</code>, the request processing chain will terminate after
	 *            this handler handles the request.
	 */
	public RequestHandlerLoopImpl(final RequestHandler start,
			final List<? extends RequestHandler> body, final RequestHandler end,
			final RequestHandler nextNode)
	{
		super(createHandlerList(start, body, end), nextNode);
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * Returns the name of this handler.
	 *
	 * @return the name of this handler
	 */
	@Override
	public String getName()
	{
		return "Loop";
	}

	// ========================= METHODS ===================================

	/**
	 * Initialize handler loop. Useful when the chain depends on instance parameters and
	 * cannot be determined in static block.
	 *
	 * @param start
	 *            start handler for the loop; initializes loop counters
	 * @param body
	 *            A non-null List of loop body handlers in their order of invocation
	 * @param end
	 *            end handler for the loop; specifies the termination criterion
	 * @return handlerArray
	 */
	private static List<RequestHandler> createHandlerList(final RequestHandler start,
			final List<? extends RequestHandler> body, final RequestHandler end)
	{
		final int bodySize = body.size();
		final RequestHandler[] handlerArray = new RequestHandler[bodySize + 2];

		// Initialize start handler references
		int count = 0;
		handlerArray[count] = start;
		handlerArray[count].setNextNode(body.get(0));
		count++;

		// Initialize body handler references
		for (int i = 0; i <= bodySize - 1; i++)
		{
			handlerArray[count] = body.get(i);
			// The last body handler points to the end handler
			handlerArray[count].setNextNode((i == bodySize - 1) ? end : body.get(i + 1));
			count++;
		}

		// Initialize end handler references; closes the loop. Note that
		// it goes back to the body, so the start handler is invoked only once
		// throughout the loop.
		handlerArray[count] = end;
		handlerArray[count].setNextNode(body.get(0));

		return CollectionUtil.newList(handlerArray);
	}
}
