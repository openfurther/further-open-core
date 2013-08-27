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
package edu.utah.further.core.api.chain;

import edu.utah.further.core.api.context.Named;

/**
 * A base class for handlers in a Classic CoR (Chain of Responsibility Pattern), i.e., the
 * request is handled by only one of the handlers in the chain at a time, and it decides
 * where to forward the request next. Use {@link RequestHandlers} to instantiate the
 * proper implementation of this interface.
 * <p>
 * This class' sole purpose is to deal with chain processing logic (linear flow,
 * branching, looping, etc.); it delegates to a {@link RequestProcessor} to do the
 * business logic of processing the request.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Feb 4, 2010
 */
public interface RequestHandler extends Named, IndentedPrintable
{
	// ========================= METHODS ===================================

	/**
	 * Starting point of chain processing from this node's viewpoint, called by client or
	 * a predecessor node. Calls {@link #process(ChainRequest)} on this node, and decide
	 * whether to continue the chain. If the next node is not <code>null</code> and this
	 * node did not handle the request, calls {@link #handle(ChainRequest)} on next node
	 * to continue the chain.
	 * <p>
	 * Note that this default implementation forces a linear request processing flow of
	 * handlers. To generalize to branching, before/after decoration like Servlet's
	 * doFilter() or Struts 2 interceptors, overriding this method in sub-classes that
	 * need this functionality.
	 * 
	 * @param request
	 *            the request object
	 * @return a boolean indicates whether this handler fully handled the request
	 */
	boolean handle(ChainRequest request);

	/**
	 * Return the next node in the chain.
	 * 
	 * @return the nextNode the node after this one
	 */
	RequestHandler getNextNode();

	/**
	 * Return the current node in the chain
	 * 
	 * @return the currentNode the node being handled
	 */
	RequestHandler getCurrentNode();

	/**
	 * Set the next node in the chain
	 * 
	 * @param nextNode
	 *            the nextNode to set
	 */
	void setNextNode(RequestHandler nextNode);

	/**
	 * Return the previous node in the chain.
	 * 
	 * @return the prevNode the node before this one
	 */
	RequestHandler getPrevNode();

	/**
	 * Set the previous node in the chain
	 * 
	 * @param prevNode
	 *            the prevNode to set
	 */
	void setPrevNode(RequestHandler prevNode);

	/**
	 * This method is called when the chain is interrupted, typically in an in order
	 * fashion. Implement this method if you need to do anything after its been
	 * interrupted (e.g. clean up)
	 */
	void handleInterrupt();
}