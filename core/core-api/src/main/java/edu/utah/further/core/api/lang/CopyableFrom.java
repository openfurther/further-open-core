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
package edu.utah.further.core.api.lang;

import edu.utah.further.core.api.context.Api;

/**
 * Defines a deep copy method for a type.
 * <p>
 * Classes that implement this interface typically also implement
 * {@link PubliclyCloneable}, which might become a super-interface of this interface in
 * the future.
 * <p>
 * Implementing this interface makes it easy to add a static copy constructor if you
 * already have a default constructor. Example:
 * 
 * <pre>
 * public static StatusMetaDataEntity newCopy(final StatusMetaData other)
 * {
 * 	return new StatusMetaDataEntity().copyFrom(other);
 * }
 * </pre>
 * 
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
public interface CopyableFrom<F, T extends F> // extends PubliclyCloneable<T> // problem
// with parameter constraints. Would have
// like to have T extends F &
// PubliclyCloneable<T> but can do only
// one of the two.
{
	// ========================= METHODS ===================================

	/**
	 * Returns a deep-copy of the argument into this object. This object is usually
	 * constructed with a no-argument constructor prior to this method, and then this
	 * method is called to copy fields from <code>other</code> into it. Think of this as a
	 * non-static copy-constructor.
	 * 
	 * @param other
	 *            object to copy (of type <code>F</code>)
	 * @return this object, for method chaining (of type <code>T</code>)
	 */
	T copyFrom(F other);
}
