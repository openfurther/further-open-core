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
package edu.utah.further.core.api.xml;

import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * A JAXB adapter of a {@link Timestamp} type. The XML type is {@link Long}.
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
 * @version Jul 8, 2009
 */
public final class TimestampAdapter extends XmlAdapter<Long, Timestamp>
{
	// ========================= IMPLEMENTATION: XmlAdapter ================

	/**
	 * Unmarshal an XML long value into a {@link Timestamp}.
	 *
	 * @param value
	 *            {@link Long} value from an XML file
	 * @return corresponding {@link Timestamp} object
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Timestamp unmarshal(final Long value)
	{
		return (value == null) ? null : new Timestamp(value.longValue());
	}

	/**
	 * Marshal a {@link Timestamp} object.
	 *
	 * @param value
	 *            timestamp
	 * @return corresponding {@link Long} value
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public Long marshal(final Timestamp value)
	{
		return (value == null) ? null : new Long(value.getTime());
	}
}
