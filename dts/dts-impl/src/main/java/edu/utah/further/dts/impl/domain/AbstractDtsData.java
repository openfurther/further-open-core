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
package edu.utah.further.dts.impl.domain;

import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_INTEGER;
import static edu.utah.further.core.api.message.ValidationUtil.validateNotNull;

import java.util.List;

import javax.annotation.PostConstruct;

import com.apelon.dts.client.common.DTSObject;
import com.apelon.dts.client.namespace.Namespace;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.namespace.DtsData;
import edu.utah.further.dts.api.domain.namespace.DtsDataType;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.impl.util.DtsImplResourceLocator;

/**
 * A base implementation of a FURTHeR DTS object. Its c-tors act adapters for different
 * Apelon types (<code>Namespace</code>, <code>DTSObject</code>) that have common API but
 * are not implemented like so inside Apelon.
 * <p>
 * TODO: Use non-public factory methods instead of c-tor telescoping?
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 17, 2008
 */
@Implementation
public abstract class AbstractDtsData implements DtsData
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * DTS Data type of this object.
	 */
	private final DtsDataType type;

	/**
	 * Concept's ID within its namespace. Copied from {@link #concept} and cached.
	 */
	private final int id;

	/**
	 * Concept's name. Copied from {@link #concept} and cached.
	 */
	private final String name;

	/**
	 * Concept's code. Copied from {@link #concept} and cached.
	 */
	private final String code;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes DTS business operations.
	 */
	protected DtsOperationService dos;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an empty DTS data entity.
	 */
	protected AbstractDtsData()
	{
		this(null);
	}

	/**
	 * @param type
	 */
	protected AbstractDtsData(final DtsDataType type)
	{
		this(type, INVALID_VALUE_INTEGER, null, null);
	}

	/**
	 * Apelon <code>Namespace</code> adapter.
	 *
	 * @param type
	 * @param namespace
	 */
	protected AbstractDtsData(final DtsDataType type, final Namespace namespace)
	{
		this(type, namespace.getId(), namespace.getName(), namespace.getCode());
	}

	/**
	 * Apelon <code>DTSObject</code> adapter.
	 *
	 * @param type
	 * @param dtsObject
	 */
	protected AbstractDtsData(final DtsDataType type, final DTSObject dtsObject)
	{
		this(type, dtsObject.getId(), dtsObject.getName(), dtsObject.getCode());
	}

	/**
	 * @param type
	 * @param id
	 * @param name
	 * @param code
	 */
	private AbstractDtsData(final DtsDataType type, final int id, final String name,
			final String code)
	{
		super();
		this.dos = DtsImplResourceLocator.getInstance().getDtsOperationService();
		this.type = type;
		this.id = id;
		this.name = name;
		this.code = code;
	}

	/**
	 * Validate dependencies.
	 */
	@PostConstruct
	protected void afterPropertiesSet()
	{
		validateNotNull("DOS", dos);
	}

	// ========================= IMPLEMENTATION: Named =====================

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	// ========================= IMPLEMENTATION: DtsData ===================

	/**
	 * Return the type property.
	 *
	 * @return the type
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getType()
	 */
	@Override
	public final DtsDataType getType()
	{
		return type;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getChildren()
	 */
	public List<DtsConcept> getChildren()
	{
		// Looking only for proper children
		return dos.getChildren(this);
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getHasChildren()
	 */
	public boolean getHasChildren()
	{
		return dos.isHasChildren(this);
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#getCode()
	 */
	@Override
	public String getCode()
	{
		return code;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#getId()
	 */
	@Override
	public int getId()
	{
		return id;
	}

	// ========================= PRIVATE METHODS ===========================
}
