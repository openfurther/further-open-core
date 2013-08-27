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
package edu.utah.further.core.xml.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import edu.utah.further.core.api.constant.Strings;

/**
 * A JAXB adapter of an optional integer attribute.
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
 * @version Aug 18, 2010
 */
public final class AbstractAttributeAdapter extends XmlAdapter<String, Integer>
{
	// ========================= IMPLEMENTATION: XmlAdapter ================

	/**
	 * @param valueType
	 * @return
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Integer unmarshal(final String valueType)
	{
		return (valueType == null) ? null : new Integer(valueType);
	}

	/**
	 * @param boundType
	 * @return
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(final Integer boundType)
	{
		return (boundType == null) ? Strings.EMPTY_STRING : boundType.toString();
	}
}
