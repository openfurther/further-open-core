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
package edu.utah.further.ds.api.lifecycle.controller;

import java.io.Serializable;

import edu.utah.further.fqe.ds.api.service.CommandTrigger;
import edu.utah.further.fqe.ds.api.util.CommandType;

/**
 * Routes the various FQE commands into their appropriate life cycles in this data source
 * implementation: a query life cycle, a data source meta-data query life cycle, an MPI
 * query life cycle, etc.
 * <p>
 * Implementations may choose how they control each life cycle (e.g. using Apache Camel or
 * some other routing framework) separately from how this class route incoming commands.
 * <p>
 * This class is not meant to be overridden by data source developers; instead, they
 * should override life cycles by implementing new {@link CommandTrigger}s and hook them
 * up within a {@link LifeCycle}.
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
public interface LifeCycleController
{
	// ========================= METHODS ===================================

	/**
	 * Route an FQE command to the appropriate life cycle.
	 * 
	 * @param commandTypeLabel
	 *            A label corresponding to an FQE command type. Allows a user-defined
	 *            command type that is not yet supported by the FQE's {@link CommandType}
	 *            but might be added in the future based on feedback from data source
	 *            developers
	 * @param input
	 *            encapsulates optional command input arguments
	 * @return life cycle result (either synchronous final result or asynchronous
	 *         acknowledgment)
	 */
	Object triggerCommand(String commandType, final Serializable input);
}
