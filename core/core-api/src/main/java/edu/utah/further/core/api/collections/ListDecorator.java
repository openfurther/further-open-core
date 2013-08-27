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
import edu.utah.further.core.api.lang.Final;

/**
 * A general-purpose decorator of a list. Wraps a list object and delegates all methods to
 * it by default.
 * <p>
 * This class is meant to be sub-classed to achieve the effect of
 * <code>List<? extends A & B></code> that is not directly permitted by the Java Generics
 * framework. The framework does mandate sub-classing this class with specific type
 * parameters, as in the following example:
 * 
 * <pre>
 * final class ConcreteListWrapper&lt;E extends HasIdentifier&lt;?&gt; &amp; SomeQueryIdentifier&lt;?&gt;&gt;
 * 		extends ListWrapper&lt;E&gt;
 * {
 * 	&#064;SuppressWarnings(&quot;unchecked&quot;)
 * 	public static ConcreteListWrapper&lt;?&gt; newInstance(final List&lt;?&gt; list)
 * 	{
 * 		return (ConcreteListWrapper&lt;?&gt;) new ConcreteListWrapper().setList(list);
 * 	}
 * </pre>
 * 
 * You can extend any number of type parameters; however, you can also pass any
 * {@link List} to the above <code>ConcreteListWrapper</code>'s factory method. A safer
 * alternative that is hard-coded to two generic parameters is {@link List2}, which only
 * permits <code>List<? extends A></code> or <code>List<? extends B></code> to be passed
 * to its constructor. For most purposes, {@link List2} should be preferred for this
 * reason.
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
public class ListDecorator<E> extends CollectionDeocrator<E>
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * The wrapped list. Call {@link #setList(List)} before using it.
	 */
	@Final
	public List<E> list;

	// ========================= METHODS ===================================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Set a new value for the list property. Do not call except in sub-class factory
	 * methods.
	 * 
	 * @param list
	 *            the list to set
	 * @return this, for method chaining
	 * @see edu.utah.further.core.api.collections.CollectionDeocrator#setCollection(java.util.List)
	 */
	protected ListDecorator<E> setList(final List<E> list)
	{
		super.setCollection(list);
		this.list = list;
		return this;
	}
}
