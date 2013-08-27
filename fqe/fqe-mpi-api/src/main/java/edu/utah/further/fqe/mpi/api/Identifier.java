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
package edu.utah.further.fqe.mpi.api;


/**
 * Represents a Master Patient Index identifier as a combination of various attributes
 * that identify it, where it came from, and what it will be.
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
 * @version Nov 1, 2011
 */
public interface Identifier
{
	/**
	 * Return the name property.
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Return the attr property.
	 * 
	 * @return the attr
	 */
	String getAttr();

	/**
	 * Return the sourceNamespaceId property.
	 * 
	 * @return the sourceNamespaceId
	 */
	long getSourceNamespaceId();

	/**
	 * Return the sourceName property.
	 * 
	 * @return the sourceName
	 */
	String getSourceName();

	/**
	 * Return the sourceAttr property.
	 * 
	 * @return the sourceAttr
	 */
	String getSourceAttr();

	/**
	 * Return the sourceId property.
	 * 
	 * @return the sourceId
	 */
	String getSourceId();
	
	/**
	 * Return the queryId property.
	 * 
	 * @return the queryId
	 */
	String getQueryId();
}
