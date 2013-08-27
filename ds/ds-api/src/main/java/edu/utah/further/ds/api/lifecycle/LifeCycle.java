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
package edu.utah.further.ds.api.lifecycle;

import java.io.Serializable;
import java.util.List;

import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.fqe.ds.api.service.CommandTrigger;

/**
 * An abstract support class of a data source life cycle that can be wired with a list of
 * request processors that execute the life cycle's business logic. The
 * {@link #triggerCommand(Serializable)} call triggers the life cycle. The argument
 * contains optional input parameters, e.g. search query criteria.
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
 * @version Feb 10, 2010
 */
public interface LifeCycle<I, O> extends CommandTrigger<I, O>
{
	// ========================= METHODS ===================================

	/**
	 * Return the supported input type of this life cycle.
	 *
	 * @return the supported input type
	 */
	Class<I> getInputType();

	/**
	 * @param requestProcessors
	 * @see edu.utah.further.ds.impl.lifecycle.LifeCycle#setRequestProcessors(java.util.List)
	 */
	void setRequestProcessors(List<? extends RequestProcessor> requestProcessors);
}