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
package edu.utah.further.core.xml.jaxb.adapter;

import static org.slf4j.LoggerFactory.getLogger;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;

/**
 * An adapter that removes all leading and trailing space and whitespace characters from
 * an XML field when converting it to a {@link String} field.
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
 * @version Jun 23, 2010
 * @see http 
 *      ://stackoverflow.com/questions/359315/how-to-configure-jaxb-so-it-trims-whitespaces
 *      -when-unmarshalling-tag-value
 */
public final class TrimmedNormalizedStringAdapter extends XmlAdapter<String, String>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(TrimmedNormalizedStringAdapter.class);

	// ========================= FIELDS ====================================

	/**
	 * A standard JAXB adapter to convert whitespaces to spaces.
	 */
	private final XmlAdapter<String, String> whitespaceTrimmer = new javax.xml.bind.annotation.adapters.NormalizedStringAdapter();

	// ========================= IMPL: XmlAdapter ==========================

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(final String text)
	{
		return text.trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public String unmarshal(final String value) throws Exception
	{
		// Be defensive
		if (value == null)
		{
			return null;
		}
		final String filtered = whitespaceTrimmer.unmarshal(value).trim();
		if (log.isDebugEnabled())
		{
			log.debug("Raw:      " + value);
			log.debug("Filtered: " + filtered);
		}
		return filtered;
	}
}
