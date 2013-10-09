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
package edu.utah.further.ds.api.results;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A standard XML wrapper for processing all results without having to create separate xml
 * wrapper classes for every domain object.
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
 * @version Oct 9, 2013
 */
@XmlRootElement(name = "ResultList")
public class ResultList
{

	// ========================= FIELDS ===================================

	@XmlAnyElement
	private final List<?> resultList;

	// ========================= CONSTRUCTORS =============================

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private ResultList()
	{
		throw new UnsupportedOperationException("Default constructor not supported");
	}

	/**
	 * @param resultList
	 */
	public ResultList(final List<?> resultList)
	{
		super();
		this.resultList = resultList;
	}

	// ========================= GET/SET ==================================

	/**
	 * Return the resultList property.
	 * 
	 * @return the resultList
	 */
	public List<?> getResultList()
	{
		return resultList;
	}
}
