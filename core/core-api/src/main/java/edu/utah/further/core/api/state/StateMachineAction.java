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
package edu.utah.further.core.api.state;

/**
 * Describes all permissible actions (state transition) of a state machine. Typically, at
 * any given state only a subset of the actions is possible.
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
 * @version Sep 16, 2009
 */
public interface StateMachineAction<S extends State<S, A, M>, A extends StateMachineAction<S, A, M>, M extends StateMachine<S, A, M>>
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Perform a state machine action operation. May be state-transitional. Is responsible
	 * for updating the machine's state.
	 *
	 * @param state
	 *            machine state machine object to invoke action on
	 * @param args
	 *            optional arguments the of action execution method
	 */
	void execute(M target, Object... args);
}
