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

import java.lang.annotation.Inherited;

import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * A convenient base class for query processors.
 * <p>
 * First, it passes on the {@link QueryProcessor} annotation to its sub-classes, to be
 * picked up by status monitor aspects. Note that the Java specification of the
 * {@link Inherited} annotation only ensures that extending sub-classes inherit
 * class-level annotations. Interface annotations are NOT inherited, neither are
 * method-level annotations.
 * <p>
 * Second, it supports an optional delegate object (controlled via {@link #getDelegate()}
 * and {@link #setDelegate(Object)}) that carries out the processing logic on behalf of
 * the processor. If the delegate implements {@link SubChainBuilder},
 * {@link AbstractUtilityProcessor} assumes that the delegate builds a
 * sub-processing-chain to carry its task, and adapts accordingly.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 26, 2010
 */
@UtilityProcessor
// This class is public only so that the class-level annotation is visible to AOP aspects
// when proxying its sub-classes
public abstract class AbstractUtilityProcessor<T> extends AbstractRequestProcessor
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * An optional builder of a processing sub-chain created by this query processor.
	 */
	private T delegate;

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString()
	{
		return (getSubChainBuilder() == null) ? super.toString() : getSubChainBuilder()
				.build()
				.toString();
	}

	// ========================= IMPLEMENTATION: IndentedPrintable =========

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.api.chain.IndentedPrintable#toString(int)
	 */
	@Override
	public final String toString(final int depth)
	{
		return (getSubChainBuilder() == null) ? super.toString() : getSubChainBuilder()
				.build()
				.toString(depth);
	}

	// ========================= GET / SET =================================

	/**
	 * Set a new value for the delegate property.
	 *
	 * @param delegate
	 *            the delegate to set
	 */
	public void setDelegate(final T delegate)
	{
		this.delegate = delegate;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Return a sub-chain builder delegate property. If this processor does not require a
	 * sub-chain, return <code>null</code> (the default).
	 *
	 * @return the delegate
	 */
	protected final SubChainBuilder getSubChainBuilder()
	{
		return isSubChainBuilder(delegate) ? (SubChainBuilder) delegate : null;
	}

	/**
	 * Return the processsing delegate property.
	 *
	 * @return the delegate
	 */
	protected T getDelegate()
	{
		return delegate;
	}

	/**
	 * @param subChainBuilder
	 * @return
	 */
	private boolean isSubChainBuilder(final Object subChainBuilder)
	{
		return ReflectionUtil.instanceOf(subChainBuilder, SubChainBuilder.class);
	}
}
