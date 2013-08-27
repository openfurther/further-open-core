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
package edu.utah.further.core.data.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor;

/**
 * An abstraction for native JDBC connection extraction that eliminates the need for
 * compile-scope dependency on specific database API such as Oracle.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Juergen Hoeller (Changes by Sloan Seaman)
 * @since 1.1.5
 * @see com.mchange.v2.c3p0.C3P0ProxyConnection#rawConnectionOperation
 * @see SimpleNativeJdbcExtractor
 * @version Mar 19, 2009
 */
public interface ConnectionNativeExtractor
{
	// ========================= METHODS ===================================

	/**
	 * Retrieve the current JDBC Connection for use in Hibernate converters that need
	 * hands-on access to it.
	 *
	 * @return JDBC connection
	 * @throws SQLException
	 *             upon JDBC access error
	 */
	Connection getNativeConnection(Connection con) throws SQLException;
}
