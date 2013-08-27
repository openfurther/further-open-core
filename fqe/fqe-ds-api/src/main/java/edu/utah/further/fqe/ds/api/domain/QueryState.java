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

import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.state.State;

/**
 * Query context state. Each state implements its handling functions within the
 * corresponding enumerated constant inner class.
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
@XmlType(name = "")
public enum QueryState implements State<QueryState, QueryAction, QueryContext>
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * The query's context is created and transient.
	 */
	SUBMITTED,

	/**
	 * The query's context has been persisted to the database. The query is queued for
	 * processing by the FQE.
	 */
	QUEUED,

	/**
	 * The query is currently running.
	 */
	EXECUTING,

	// /**
	// * A sub-state of {@link #EXECUTING} for the execution phase of translating the
	// * logical query to physical queries. In principle, this needs to be a separate
	// class
	// * and may have different values for each data source. TODO: move to a separate
	// state
	// * class and make it data-source-dependent.
	// */
	// TRANSLATING_QUERY,
	//
	// /**
	// * A sub-state of {@link #EXECUTING} for the execution phase of translating the
	// * physical result sets to logical result set. In principle, this needs to be a
	// * separate class and may have different values for each data source. TODO: move to
	// a
	// * separate state class and make it data-source-dependent.
	// */
	// TRANSLATING_RESULT,

	/**
	 * Query execution is stopped. This is a recoverable state.
	 */
	STOPPED,

	/**
	 * Query execution is complete. This is an unrecoverable state.
	 */
	COMPLETED,

	/**
	 * Query execution failed. This is an unrecoverable state.
	 */
	FAILED,

	/**
	 * A invalid state where the query cannot be considered for any of the other states.
	 */
	INVALID;
}
