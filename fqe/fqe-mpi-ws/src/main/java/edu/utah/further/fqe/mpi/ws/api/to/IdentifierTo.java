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
package edu.utah.further.fqe.mpi.ws.api.to;

import static org.slf4j.LoggerFactory.getLogger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;

import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * An identifier entity transfer object, used by identifier REST services.
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
 * @version Jul 8, 2010
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "id", namespace = XmlNamespace.FQE)
public final class IdentifierTo
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(IdentifierTo.class);

	// ========================= FIELDS ====================================
	/**
	 * The identifier
	 */
	@XmlElement(name = "value", namespace = XmlNamespace.FQE)
	private Long value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor - required by a JavaBean.
	 */
	public IdentifierTo()
	{
		// Empty
	}

	/**
	 * @param value
	 */
	public IdentifierTo(final Long value)
	{
		super();
		this.value = value;
	}

	// ========================= GET/SET ====================================

	/**
	 * Return the value property.
	 *
	 * @return the value
	 */
	public Long getValue()
	{
		return value;
	}

	/**
	 * Set a new value for the value property.
	 *
	 * @param value
	 *            the value to set
	 */
	public void setValue(final Long value)
	{
		this.value = value;
	}

}
