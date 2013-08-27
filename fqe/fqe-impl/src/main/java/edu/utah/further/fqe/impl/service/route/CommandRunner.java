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

/**
 * An abstraction of FQE command runners. Holds command parameters and can run the
 * command. Parameterized by the optional command return type. If no return type is
 * expected, simply use <code>T=</code>{@link Object} and return <code>null</code> from
 * {@link #run()}.
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
 * @version Mar 26, 2010
 */
public interface CommandRunner<T>
{
	// ========================= METHODS ===================================

	/**
	 * Run the FQE command described by this object. If no return type is expected, use
	 * <code>T=</code> {@link Object} and return <code>null</code> from {@link #run()}.
	 * 
	 * @return command return type, or <code>null</code>, if no return type is expected
	 */
	T run();
}