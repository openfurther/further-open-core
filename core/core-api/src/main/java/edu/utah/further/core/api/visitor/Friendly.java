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
package edu.utah.further.core.api.visitor;

/**
 * A friendly class that allows some {@link Friend} classes to access its private part.
 * Part of a fine-grained-control friend pattern for Java. Based on
 * http://macchiato.com/columns/Durable7.html
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
 * @version Sep 16, 2009
 */
public interface Friendly<K, D extends Friendly<K, D, F>, F extends Friend<K, D, F>>
{
	// ========================= METHDOS ====================================

	/**
	 * Allow a friend class to access the private parts of this class. Uses a call-back
	 * mechanism: {@link #giveKeyTo(Friend)} calls the friend's
	 * {@link Friend#receiveKey(Friendly)}, where the argument is an instance of the
	 * private part of this class.
	 *
	 * @param friend
	 *            visitor accepted by this object
	 */
	void giveKeyTo(F friend);
}
