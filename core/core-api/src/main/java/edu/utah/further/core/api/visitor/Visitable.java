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
 * An object that accept a visitor in a visitor pattern. Uses a call-back method (
 * {@link #accept(Visitor)}) that calls {@link Visitor#visit(Visitable)}. This interface
 * is parameterized by the type of the visitor object, so that only certain (as well as
 * multiple-type) visitors are allowed in certain visitables.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 21, 2008
 */
public interface Visitable<T extends Visitable<T, V>, V extends Visitor<T, V>>
{
	// ========================= METHDOS ====================================

	/**
	 * Allow a visitor to access and process this object. calls
	 * <code>visitable.accept(this)</code>). Uses a call-back method (
	 * <code>accept(visitor)</code> calls <code>visitor.visit(this)</code>).
	 *
	 * @param <T>
	 *            specific visitor type; this method may be implemented for multiple
	 *            classes of {@link Visitable}.
	 * @param visitor
	 *            visitor accepted by this object
	 */
	void accept(V visitor);
}
