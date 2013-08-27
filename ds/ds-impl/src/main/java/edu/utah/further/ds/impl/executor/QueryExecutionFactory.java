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
package edu.utah.further.ds.impl.executor;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.ds.api.executor.QueryExecution;
import edu.utah.further.ds.api.util.DatasourceType;

/**
 * A factory for query execution implementations based on data source type.
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
 * @version Apr 26, 2010
 */
public final class QueryExecutionFactory
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(QueryExecutionFactory.class);

	// ========================= DEPS =================================

	/**
	 * A database query execution. Only a Hibernate implementation is supported for now.
	 */
	private QueryExecution databaseQueryExecution;

	/**
	 * A webservice query execution hopefully technology agnostic.
	 */
	private QueryExecution webserviceQueryExecution;

	// ========================= IMPL: QueryExecutionFactory ================

	/**
	 * Returns an instance of a QueryExecution based on the data source type
	 * 
	 * @param type
	 *            the type of data source
	 * @return a query execution
	 */
	public QueryExecution getInstance(final DatasourceType type)
	{
		switch (type)
		{
			case DATABASE:
			{
				return databaseQueryExecution;
			}

			case WEB_SERVICE:
			{
				return webserviceQueryExecution;
			}

			default:
			{
				throw new UnsupportedOperationException("Unsupported data source type "
						+ type + ", consider implementing query execution for this type.");
			}
		}
	}

	// ========================= GET/SET =================================

	/**
	 * Return the databaseQueryExecution property.
	 * 
	 * @return the databaseQueryExecution
	 */
	public QueryExecution getDatabaseQueryExecution()
	{
		return databaseQueryExecution;
	}

	/**
	 * Set a new value for the databaseQueryExecution property.
	 * 
	 * @param databaseQueryExecution
	 *            the databaseQueryExecution to set
	 */
	public void setDatabaseQueryExecution(final QueryExecution databaseQueryExecution)
	{
		this.databaseQueryExecution = databaseQueryExecution;
	}

	/**
	 * Return the webserviceQueryExecution property.
	 * 
	 * @return the webserviceQueryExecution
	 */
	public QueryExecution getWebserviceQueryExecution()
	{
		return webserviceQueryExecution;
	}

	/**
	 * Set a new value for the webserviceQueryExecution property.
	 * 
	 * @param webserviceQueryExecution
	 *            the webserviceQueryExecution to set
	 */
	public void setWebserviceQueryExecution(final QueryExecution webserviceQueryExecution)
	{
		this.webserviceQueryExecution = webserviceQueryExecution;
	}

}
