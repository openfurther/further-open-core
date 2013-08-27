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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * A request handler chain - Classic CoR (Chain of Responsibility Pattern). This is also a
 * composite pattern of {@link AbstractRequestHandler}s.
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
class RequestHandlerChainImpl extends AbstractRequestHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(RequestHandlerChainImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * List of handlers in their order of invocation.
	 */
	protected final List<RequestHandler> handlerList = CollectionUtil.newList();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a request handler chain from handlers.
	 * 
	 * @param handlerList
	 *            List of handlers in their order of invocation in the chain
	 * @param nextNode
	 *            The next node to invoke in a super-chain after this chain is completed.
	 *            If <code>null</code>, the request processing chain will terminate after
	 *            this handler handles the request.
	 */
	RequestHandlerChainImpl(final List<? extends RequestHandler> handlerList,
			final RequestHandler nextNode)
	{
		super.setNextNode(nextNode);
		CollectionUtil.setListElements(this.handlerList, handlerList);
		Validate.notNull("handlerList", "handler list");

		RequestHandler prevNode = null;

		// Initialize the chain node references
		for (int i = 0; i < this.handlerList.size() - 1; i++)
		{
			final RequestHandler currNode = this.handlerList.get(i);
			currNode.setNextNode(this.handlerList.get(i + 1));
			currNode.setPrevNode(prevNode);
			prevNode = currNode;
		}

		// Set previous node of super chain to the last node in this chain
		super.setPrevNode(prevNode);
	}

	// ========================= IMPLEMENTATION: Visitable =================

	/**
	 * Let a visitor process this item. Part of the Visitor pattern. This calls back the
	 * visitor's <code>visit()</code> method with this item type.
	 * 
	 * @param visitor
	 *            item data visitor whose <code>visit()</code> method is invoked
	 * @see edu.utah.further.core.api.visitor.Visitable#accept(edu.utah.further.core.api.visitor.Visitor)
	 */
	@Override
	public void accept(final RequestHandlerVisitor visitor)
	{
		visitor.visit(this);
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.chain.AbstractRequestHandler#handle(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public final boolean handle(final ChainRequest request)
	{
		
		// Invoke this chain; the result indicates whether super-chain
		// processing should be terminated
		return handlerList.get(0).handle(request);
	}

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return "Chain";
	}
}
