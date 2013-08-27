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
package edu.utah.further.fqe.ds.api.domain;

import edu.utah.further.core.api.state.StateMachineAction;

/**
 * A query context action. Has a unique identifier in this case ({@link QueryActionId}).
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
 * @version May 28, 2009
 */
public enum QueryAction implements
		StateMachineAction<QueryState, QueryAction, QueryContext>
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Add the query to the execution queue.
	 */
	QUEUE
	{
		/**
		 * @param target
		 * @param args
		 * @see edu.utah.further.core.api.state.StateMachineAction#execute(edu.utah.further.core.api.state.StateMachine,
		 *      java.lang.Object[])
		 */
		@Override
		public void execute(final QueryContext target, final Object... args)
		{
			target.queue();
		}
	},

	/**
	 * Start query execution. If query is STOPPED, continue query execution from the place
	 * it was stopped.
	 */
	START
	{
		/**
		 * @param target
		 * @param args
		 * @see edu.utah.further.core.api.state.StateMachineAction#execute(edu.utah.further.core.api.state.StateMachine,
		 *      java.lang.Object[])
		 */
		@Override
		public void execute(final QueryContext target, final Object... args)
		{
			target.start();
		}
	},

	/**
	 * Stop query execution.
	 */
	STOP
	{
		/**
		 * @param target
		 * @param args
		 * @see edu.utah.further.core.api.state.StateMachineAction#execute(edu.utah.further.core.api.state.StateMachine,
		 *      java.lang.Object[])
		 */
		@Override
		public void execute(final QueryContext target, final Object... args)
		{
			target.stop();
		}
	},

	/**
	 * Terminate query execution due to a failed condition.
	 */
	FAIL
	{
		/**
		 * @param target
		 * @param args
		 * @see edu.utah.further.core.api.state.StateMachineAction#execute(edu.utah.further.core.api.state.StateMachine,
		 *      java.lang.Object[])
		 */
		@Override
		public void execute(final QueryContext target, final Object... args)
		{
			target.fail();
		}
	},

	/**
	 * Successfully end query execution.
	 */
	FINISH
	{
		/**
		 * @param target
		 * @param args
		 * @see edu.utah.further.core.api.state.StateMachineAction#execute(edu.utah.further.core.api.state.StateMachine,
		 *      java.lang.Object[])
		 */
		@Override
		public void execute(final QueryContext target, final Object... args)
		{
			target.finish();
		}
	};

	// ========================= IMPLEMENTATION: StateMachineAction ========

	/**
	 * @param target
	 * @param args
	 * @see edu.utah.further.core.api.state.StateMachineAction#execute(edu.utah.further.core.api.state.StateMachine,
	 *      java.lang.Object[])
	 */
	@Override
	public abstract void execute(QueryContext target, Object... args);
}
