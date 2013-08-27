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
package edu.utah.further.dts.ws.api.domain;

import static edu.utah.further.core.api.constant.ErrorCode.MISSING_INPUT_ARGUMENT;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Alias;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * Types of views of a web service result.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Alias("viewType")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = ViewType.ENTITY_NAME)
@XmlRootElement(namespace = XmlNamespace.DTS, name = ViewType.ENTITY_NAME)
public enum ViewType
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Machine view of the result. It is typically succinct, to minimize network traffic.
	 * It is often the default value of view properties.
	 */
	MACHINE,

	/**
	 * Human view of the result set. May include some debugging fields or nicer formats.
	 */
	HUMAN,

	/**
	 * Full information of the result set.
	 */
	FULL;

	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "viewType";

	public static ViewType fromString(final String s)
	{
		try
		{
			return ViewType.valueOf(s);
		}
		catch (final Throwable e)
		{
			throw new ApplicationException(MISSING_INPUT_ARGUMENT,
					"Cannot convert value " + s + " to " + ViewType.class.getSimpleName());
		}
	}
}
