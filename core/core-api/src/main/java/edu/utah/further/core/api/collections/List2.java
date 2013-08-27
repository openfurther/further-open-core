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
package edu.utah.further.core.api.collections;

import java.util.List;

import edu.utah.further.core.api.context.Api;

/**
 * Imitates <code>List<? extends A & B></code> that is not permitted by the Java Generics
 * framework.
 * <p>
 * Note that one cannot pass any <code>List<?></code> to {@link List2}<A,B>, but you must
 * pass a list of type that extends BOTH A, B, so this class is a little more type-safer
 * than sub-classing {@link ForwardingList}. However, sub-classing {@link ForwardingList} allows
 * extending any number of generic parameters whereas this object is hard-coded for two.
 * <p>
 * Similar implementations for three or more parameters can of course be added in the
 * future.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 13, 2008
 */
@Api
public final class List2<A, B>
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The wrapped list - type parameter #1 view.
	 */
	public final List<? extends A> list1;

	/**
	 * The wrapped list - type parameter #2 view.
	 */
	public final List<? extends B> list2;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize all views provided by this object.
	 * 
	 * @param list1
	 *            wrapped list - type parameter #1 view
	 * @param list2
	 *            wrapped list - type parameter #2 view
	 */
	private List2(final List<? extends A> list1, final List<? extends B> list2)
	{
		super();
		this.list1 = list1;
		this.list2 = list2;
	}

	/**
	 * Return a new wrapper around a list of a type that extends both A and B.
	 * 
	 * @param <A>
	 *            first generic parameter
	 * @param <B>
	 *            second generic parameter
	 * @param list
	 *            must be of type E where E extends BOTH A and B
	 * @return a wrapper view of the list that permits accessing it as List<A>
	 *         (wrapper.list1) and as List<B> (wrapper.list2)
	 */
	@SuppressWarnings("unchecked")
	public static <A, B> List2<A, B> newInstance1(final List<? extends A> list)
	{
		return new List2<>(list, (List<? extends B>) list);
	}

	/**
	 * Return a new wrapper around a list of a type that extends both A and B.
	 * 
	 * @param <A>
	 *            first generic parameter
	 * @param <B>
	 *            second generic parameter
	 * @param list
	 *            must be of type E where E extends BOTH A and B
	 * @return a wrapper view of the list that permits accessing it as List<A>
	 *         (wrapper.list1) and as List<B> (wrapper.list2)
	 */
	@SuppressWarnings("unchecked")
	public static <A, B> List2<A, B> newInstance2(final List<? extends B> list)
	{
		return new List2<>((List<? extends A>) list, list);
	}

	// ========================= PRIVATE METHODS ===========================

}
