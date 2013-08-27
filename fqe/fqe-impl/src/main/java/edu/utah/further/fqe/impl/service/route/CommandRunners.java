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

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang.Validate;

import edu.utah.further.core.api.context.Utility;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.util.MessageHeader;
import edu.utah.further.fqe.impl.util.FqeImplResourceLocator;

/**
 * A mother object that produces common {@link CommandRunner} templates..
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) Oren E. Livne, Ph.D., University of Utah<br>
 * Email: {@code <oren.livne@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-213-3713<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Dec 20, 2009
 */
@Utility
public final class CommandRunners
{
	// ========================= METHODS ====================================

	/**
	 * Send a data query to a single data source, if queryContext.dataSourceId is set; if
	 * not, query is sent to all data sources.
	 * 
	 * @param queryContext
	 *            query to send
	 * @return command runner
	 */
	public static CommandRunner<?> dataQuery(final QueryContext queryContext,
			final ProducerTemplate producerTemplate)
	{
		Validate.notNull(producerTemplate);

		// Trigger query execution
		return new AsynchronousCommandRunner.Builder()
				.commandType(queryContext.getQueryType().getCommandType())
				.setHeader(MessageHeader.DATA_SOURCE_ID, queryContext.getDataSourceId())
				.body(queryContext)
				.requestEndpoint(FqeImplResourceLocator.getInstance().getMarshalRequest())
				.producerTemplate(producerTemplate)
				.build();
	}
}
