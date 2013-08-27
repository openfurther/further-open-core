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
package edu.utah.further.core.chain;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;

import edu.utah.further.core.api.context.Labeled;

/**
 * A generic utility processor that transforms the object in the source attribute to the
 * object in result attribute. Because the {@link UtilityProcessor} (similarly
 * <code>QueryProcessor</code>) annotation must be a class-level annotation to be
 * inheritable by sub-classes and visible to AOP therein, it's a sad reality in which we
 * will potentially have to duplicate this base class under
 * <code>AbstractQueryProcessor</code>.
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
 * @version Oct 1, 2010
 */
public abstract class AbstractUtilityTransformer<T> extends
		AbstractDelegatingUtilityProcessor<T> implements RequestTransformer
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * The name of the attribute to get the Object to be transformed. Not an AttributeName
	 * in order to be flexible but can wire in an AttributeName if needed.
	 */
	private Labeled sourceAttr;

	/**
	 * The name of the attribute of the transformation result. Not an AttributeName in
	 * order to be flexible but can wire in an AttributeName if needed.
	 */
	private Labeled resultAttr;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Validate dependencies .
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		Validate.notNull(sourceAttr, "The source attribute must be set");
		Validate.notNull(resultAttr, "The result attribute must be set");
	}

	// ========================= IMPL: MarshallRequestProcessor ==========

	/**
	 * @return
	 * @see edu.utah.further.core.chain.RequestTransformer#getSourceAttr()
	 */
	@Override
	public Labeled getSourceAttr()
	{
		return sourceAttr;
	}

	/**
	 * Set a new value for the sourceAttr property.
	 *
	 * @param sourceAttr
	 *            the sourceAttr to set
	 */
	@Override
	public void setSourceAttr(final Labeled sourceAttr)
	{
		this.sourceAttr = sourceAttr;
	}

	/**
	 * @return
	 * @see edu.utah.further.core.chain.RequestTransformer#getResultAttr()
	 */
	@Override
	public Labeled getResultAttr()
	{
		return resultAttr;
	}

	/**
	 * Set a new value for the resultAttr property.
	 *
	 * @param resultAttr
	 *            the resultAttr to set
	 */
	@Override
	public void setResultAttr(final Labeled resultAttr)
	{
		this.resultAttr = resultAttr;
	}
}
