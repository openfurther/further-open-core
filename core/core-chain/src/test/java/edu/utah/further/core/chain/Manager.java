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

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static org.apache.commons.logging.LogFactory.getLog;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.chain.AbstractRequestProcessor;

/**
 * A manager in a chain of responsibility in a company.
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
final class Manager extends AbstractRequestProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log log = getLog(Manager.class);

	// ========================= FIELDS ====================================

	/**
	 * Manager's title.
	 */
	private final String title;

	/**
	 * Maximum allowable purchase by this manager.
	 */
	private final double allowable;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Construct a manager handler.
	 * 
	 * @param title
	 *            manager's title
	 * @param allowable
	 *            maximum allowed purchase amount that this manager can approve
	 */
	public Manager(final String title, final double allowable)
	{
		super();
		this.title = title;
		this.allowable = allowable;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("name",
				getName()).append("allowable", allowable).toString();
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.chain.AbstractRequestHandler#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Cast to a friendlier version
		final PurchaseRequest purchaseRequest = (PurchaseRequest) request;
		final double amount = purchaseRequest.getAmount();
		if (amount < allowable)
		{
			request.setAttribute(ChainNames.APPROVED, title);
			// logger.info(title + " will approve $" + amount);
			return true;
		}
		return false;
	}

	/**
	 * Returns the name of this handler.
	 * 
	 * @return the name of this handler
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return title;
	}
}
