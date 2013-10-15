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
package edu.utah.further.fqe.ds.api.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.ds.api.domain.AbstractResultContextKey;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.domain.ResultContextKey;
import edu.utah.further.fqe.ds.api.service.results.ResultType;

/**
 * A convenient JAXB entity that holds the key fields of a result context, for map
 * indexing. This can be thought of as a view of a {@link ResultContext} object.
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
 * @version Oct 19, 2010
 */
@Implementation
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "type" })
@XmlRootElement(namespace = XmlNamespace.FQE, name = ResultContextKeyToImpl.ENTITY_NAME)
public final class ResultContextKeyToImpl extends AbstractResultContextKey
{
	// ========================= CONSTANTS =================================

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "resultContextKey";

	// ========================= FIELDS ====================================

	/**
	 * Type of result set (sum/intersection/...).
	 */
	@XmlAttribute(name = "type", required = true)
	private ResultType type;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for JAXB.
	 */
	public ResultContextKeyToImpl()
	{
		super();
	}

	/**
	 * @param other
	 */
	public ResultContextKeyToImpl(final ResultContextKey other)
	{
		this(other.getType());
	}

	/**
	 * @param type
	 * @param intersectionIndex
	 */
	public ResultContextKeyToImpl(final ResultType type)
	{
		super();
		this.type = type;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the type property.
	 *
	 * @return the type
	 */
	@Override
	public ResultType getType()
	{
		return type;
	}
}
