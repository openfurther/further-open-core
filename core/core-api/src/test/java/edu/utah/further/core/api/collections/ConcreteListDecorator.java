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
import edu.utah.further.core.api.discrete.HasIdentifier;

/**
 * A list wrapper that gets around a Java generics restriction:
 * <code>List<? extends HasIdentifier<?> & SomeQueryIdentifier<?>></code> is not
 * compilable. Unfortunately, a special class needs to be written for every concrete A and
 * B to achieve <code>List<? extends A & B></code>.
 * <p>
 * See also http://www.chrononaut.org/showyourwork/?p=52#comments
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
 * @version Oct 2, 2009
 */
@Api
final class ConcreteListDecorator<E extends HasIdentifier<?> & SomeQueryIdentifier<?>>
		extends ListDecorator<E>
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * A factory method that captures the wildcard bounds of ConcreteListDecorator.
	 * Generates unchecked warning, but lets the <?> acts as
	 * <code><? extends HasIdentifier<?> & SomeQueryIdentifier<?>></code>.
	 *
	 * @param list
	 *            list to wrap. <i>Its element must be of the effect type
	 *            <code><? extends HasIdentifier<?> & SomeQueryIdentifier<?>></code>, or
	 *            this method will return an instance that may break client code!</i>
	 * @return list wrapper instance
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ConcreteListDecorator<?> newInstance(final List<?> list)
	{
		return (ConcreteListDecorator<?>) new ConcreteListDecorator().setList(list);
	}
}
