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

import edu.utah.further.fqe.ds.api.util.CommandType;

/**
 * An enumerated value specifying the type of query lifecycle to execute. Really just a subset
 * of (@link CommandType)s that are relevant to QueryContexts.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version Apr 2, 2012
 */
@XmlType(name = "")
public enum QueryType
{
	/**
	 * 
	 * Default data query which returns a set of patients that are then translated into the FuRTHER model
	 */
	DATA_QUERY,
	
	/**
	 * 
	 * Returns nothing but a count of rows which match the query sent to the datasource 
	 */
	COUNT_QUERY;
	
	/**
	 * Maps QueryType to an (@link CommandType) value for datasource JMS request
	 * @return
	 */
	public CommandType getCommandType() {
		
		return CommandType.valueOf(toString());
	}
}
