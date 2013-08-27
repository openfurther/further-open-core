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
package edu.utah.further.core.api.scope;

/**
 * All data sources and terminology standards reside within a scope called a namespace;
 * this service is for retrieving a namespace's name or identifier. Namespaces
 * disambiguate common terms within a standard or a data source. They're also used to
 * represent a particular standard or a particular data source. Implementations of this
 * service can be driven by a database, a properties file (default), or a terminology server (which
 * uses the exact same idea of a namespace).
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 22, 2013
 */
public interface NamespaceService
{
	/**
	 * Return the name of the namespace
	 * 
	 * @param namespace
	 * @return
	 */
	String getNamespaceName(Namespace namespace);

	/**
	 * Return the identifier that represents this namespace
	 * 
	 * @param namespace
	 * @return
	 */
	int getNamespaceId(Namespace namespace);
}
