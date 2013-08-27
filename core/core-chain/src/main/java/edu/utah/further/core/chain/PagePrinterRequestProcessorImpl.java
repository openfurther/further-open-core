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

import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.context.Labeled;

/**
 * A debugging processor that gets the next page of a result and prints it.
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
 * @version Sep 23, 2010
 */
@Qualifier("pagePrinterRequestProcessor")
public class PagePrinterRequestProcessorImpl extends AbstractNonDelegatingUtilityProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(PagePrinterRequestProcessorImpl.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The name of the attribute to get the Object to be marshalled. Not an AttributeName
	 * in order to be flexible but can wire in an AttributeName if needed.
	 */
	private Labeled sourceAttr;

	/**
	 * The name of the attribute to store the pager (page iterator) in. Useful for
	 * debugging. Optional attribute.
	 */
	private Labeled pagerAttr;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate dependencies .
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		Validate.notNull(sourceAttr, "A source attribute must be set");
		Validate.notNull(sourceAttr, "A paging iterator attribute must be set");
	}

	// ========================= IMPL: RequestProcessor =====================

	/**
	 * @param request
	 * @return
	 * @see edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Read the input from the request
		final Object currentPage = request.getAttribute(sourceAttr);
		final Iterator<?> pageIterator = request.getAttribute(pagerAttr);
		final Object nextPage = pageIterator.next();
		if (log.isDebugEnabled())
		{
			log.debug("Current page " + currentPage);
			log.debug("Next page " + nextPage);
		}

		// OK to continue processing chain
		return false;
	}

	// ========================= GET/SET =================================

	/**
	 * Set a new value for the sourceAttr property.
	 *
	 * @param sourceAttr
	 *            the sourceAttr to set
	 */
	public void setSourceAttr(final Labeled sourceAttr)
	{
		this.sourceAttr = sourceAttr;
	}

	/**
	 * Set a new value for the pagerAttr property.
	 *
	 * @param pagerAttr
	 *            the pagerAttr to set
	 */
	public void setPagerAttr(final Labeled pagerAttr)
	{
		this.pagerAttr = pagerAttr;
	}
}
