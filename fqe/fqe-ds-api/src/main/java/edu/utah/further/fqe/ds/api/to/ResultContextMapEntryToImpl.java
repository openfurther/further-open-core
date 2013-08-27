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

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.collections.MutablePublicMapEntry;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.ds.api.domain.ResultContext;

// ============================
// JAXB annotations
// ============================
/**
 * Used in adapting a {@link ResultContext} {@link Map} to a JAXB entity.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @see http
 *      ://download.oracle.com/javase/6/docs/api/javax/xml/bind/annotation/adapters/XmlAdapter
 *      .html
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Nov 2, 2010
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "key", "value" })
@XmlRootElement(namespace = XmlNamespace.FQE, name = ResultContextMapEntryToImpl.ENTITY_NAME)
public class ResultContextMapEntryToImpl implements MutablePublicMapEntry<ResultContextKeyToImpl, ResultContextToImpl>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "resultContextEntry";

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity. Also serves as a foreign key into the
	 * database table containing the result set rows.
	 */
	@XmlElement(name = "key", required = true, namespace = XmlNamespace.FQE)
	private ResultContextKeyToImpl key;

	/**
	 * Result set's root entity class' fully qualified name. Uniquely identifies the
	 * database table containing the result set rows.
	 */
	@XmlElement(name = "resultContext", required = true, namespace = XmlNamespace.FQE)
	private ResultContextToImpl value;

	// ========================= IMPL: MutablePublicMapEntry ===============

	/**
	 * Return the key property.
	 *
	 * @return the key
	 */
	@Override
	public ResultContextKeyToImpl getKey()
	{
		return key;
	}

	/**
	 * Set a new value for the key property.
	 *
	 * @param key the key to set
	 */
	@Override
	public void setKey(final ResultContextKeyToImpl key)
	{
		this.key = key;
	}

	/**
	 * Return the value property.
	 *
	 * @return the value
	 */
	@Override
	public ResultContextToImpl getValue()
	{
		return value;
	}

	/**
	 * Set a new value for the value property.
	 *
	 * @param value the value to set
	 */
	@Override
	public void setValue(final ResultContextToImpl value)
	{
		this.value = value;
	}

}
