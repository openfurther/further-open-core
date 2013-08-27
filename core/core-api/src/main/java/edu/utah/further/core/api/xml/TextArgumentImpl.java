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

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Implementation;

/**
 * A service meta data transfer object. A useful utility class for both services and
 * clients.
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
@Implementation
@XmlAccessorType(FIELD)
@XmlType(namespace = XmlNamespace.CORE_API, name = TextArgumentImpl.ENTITY_NAME, propOrder =
{ "text" })
@XmlRootElement(namespace = XmlNamespace.CORE_API, name = TextArgumentImpl.ENTITY_NAME)
public final class TextArgumentImpl implements TextArgument
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "textArgument";

	// ========================= NESTED TYPES ==============================

	// ========================= FIELDS ====================================

	/**
	 * Text data of this argument.
	 */
	@XmlElement(name = "text", required = false)
	private String text;

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	// ========================= IMPLEMENTATION: Named =====================

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getText()
	{
		return text;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set a new value for the name property.
	 *
	 * @param text
	 *            the name to set
	 */
	@Override
	public void setText(final String text)
	{
		this.text = text;
	}

	// ========================= PRIVATE METHODS ===========================
}
