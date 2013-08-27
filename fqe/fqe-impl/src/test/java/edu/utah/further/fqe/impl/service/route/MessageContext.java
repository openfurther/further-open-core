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
 * Types of messages relayed between the FQE and DS's. Useful as JMS header values.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) Oren E. Livne, Ph.D., University of Utah<br>
 * Email: {@code <oren.livne@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Dec 27, 2009
 */
public enum MessageContext
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * A federated command message.
	 */
	FEDERATED,

	/**
	 * A data source response message.
	 */
	DATA_SOURCE,

	/**
	 * An aggregated response message from an FQE aggregator.
	 */
	AGGREGATED;

	// ========================= CONSTANTS =================================

	/**
	 * Camel JMS header for the message context = the sender of the message (FQE
	 * request/FQE aggregator result/DS). For testing only.
	 * <p>
	 * Note: may be deprecated in the near future, so use sparingly.
	 */
	@edu.utah.further.core.api.context.Constants
	public abstract class Constants
	{
		public static final String HEADER_NAME = "messageContext";
	}
}
