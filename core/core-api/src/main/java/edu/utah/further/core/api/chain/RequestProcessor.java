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
 * A {@link ChainRequest} processor. Can set or modify attributes inside the request to be
 * read by other processors down a chain of processing. Used in conjunction with
 * {@link AbstractRequestHandler}; see comments on the {@link #process(ChainRequest)}
 * method below. {@link AbstractRequestHandler} handles chain logic and
 * {@link RequestProcessor} processes the business logic of processing a request within a
 * {@link AbstractRequestHandler}.
 * <p>
 * The decision to decouple the two classes was driven in part by the need to proxy the
 * {@link #process(ChainRequest)} method using an AOP aspect, which cannot be done if it
 * is implemented by {@link AbstractRequestHandler} as well as
 * {@link AbstractRequestHandler#handle(ChainRequest)}, because of self-invocation
 * limitations of the Spring AOP framework explained in
 * http://static.springsource.org/spring/docs/2.5.x/reference
 * /aop.html#aop-understanding-aop-proxies .
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
public interface RequestProcessor extends Named, IndentedPrintable
{
	// ========================= METHODS ===================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Process the request. Can set or modify attributes inside the request to be read by
	 * other processors down a chain of processing.
	 * 
	 * @param request
	 *            the request object
	 * @return a flag indicates whether this processor fully handled the request. If it is
	 *         <code>true</code>, the request processing chain may terminate. This
	 *         decision is made by the collaborating class {@link AbstractRequestHandler},
	 *         which delegates to a {@link RequestProcessor} object to do the processing
	 *         part and decides whether to continue the chain based on the return type of
	 *         this method and the contents of the updated <code>request</code> argument
	 */
	boolean process(ChainRequest request);

	/**
	 * Get a reference to the handler handling this processor.
	 * 
	 * @return the RequestHandler
	 */
	RequestHandler getRequestHandler();

	/**
	 * Get a reference to the handler handling this processor.
	 * 
	 * @return the RequestHandler
	 */
	void setRequestHandler(RequestHandler requestHandler);

	/**
	 * This method will be called if this processor is interrupted.
	 */
	void handleInterrupt();
}
