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

import static edu.utah.further.dts.ws.api.domain.ViewType.MACHINE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * Commonly used web service call options.
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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = OptionsImpl.ENTITY_NAME, propOrder =
{ "view" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = OptionsImpl.ENTITY_NAME)
public class OptionsImpl implements Options
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "options";

	// ========================= NESTED TYPES ==============================

	// ========================= FIELDS ====================================

	/**
	 * Source namespace concept.
	 */
	@XmlElement(name = "view", required = false)
	private ViewType view = MACHINE;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A no-argument JavaBean constructor.
	 */
	public OptionsImpl()
	{
		super();
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	// ========================= GETTERS & SETTERS =========================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 * @see edu.utah.further.dts.ws.api.domain.Options#getView()
	 */
	@Override
	public ViewType getView()
	{
		return view;
	}

	/**
	 * @param view
	 * @see edu.utah.further.dts.ws.api.domain.Options#setView(edu.utah.further.dts.ws.api.domain.ViewType)
	 */
	@Override
	public void setView(final ViewType view)
	{
		this.view = view;
	}
}
