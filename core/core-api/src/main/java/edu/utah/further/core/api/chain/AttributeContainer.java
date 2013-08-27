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
package edu.utah.further.core.api.chain;

import java.util.Map;
import java.util.Set;

import edu.utah.further.core.api.context.Labeled;

/**
 * A request object passed to a chain of handlers. Like <code>ServletRequest</code>, holds
 * a map of input/output attributes that may be manipulated by handlers.
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
 * @version Sep 28, 2009
 */
public interface AttributeContainer
{
	// ========================= METHODS ===================================

	/**
	 * Return the attributes map property. (This is NOT a deep copy of the map.)
	 *
	 * @return the attribute map
	 */
	Map<String, Object> getAttributes();

	/**
	 * Sets the attributes map property
	 *
	 */
	void setAttributes(Map<String, ?> attributes);

	/**
	 * Add attributes, overriding existing key-value pairs.
	 *
	 * @param map
	 *            attributes to overlay over the existing attributes in the container.
	 */
	void addAttributes(Map<String, ?> map);

	/**
	 * Returns the value of the named attribute as a generic type. This method allows
	 * chain handlers to give input/output information in the request. This method returns
	 * <code>null</code> if no attribute of the given name exists.
	 * <p>
	 * Attribute names should follow the naming conventions of {@link CommonStrings}.
	 *
	 * @param <T>
	 *            attribute value type
	 * @param name
	 *            a <code>String</code> specifying the name of the attribute
	 * @return an object of type <code>T</code> containing the value of the attribute, or
	 *         <code>null</code> if the attribute does not exist
	 */
	<T> T getAttribute(String name);

	/**
	 * Returns the value of the named attribute as a generic type. This is a convenience
	 * method that gets an attribute from a {@link Labeled} object by calling its
	 * {@link Labeled#getLabel()} method for the attribute's name.
	 *
	 * @param <T>
	 *            attribute value type
	 * @param label
	 *            an object with a label to use for the attribute name
	 * @return an object of type <code>T</code> containing the value of the attribute, or
	 *         <code>null</code> if the attribute does not exist or if <code>label</code>
	 *         is <code>null</code>
	 */
	<T> T getAttribute(Labeled label);

	/**
	 * Returns an <code>Set</code> containing the names of the attributes available to
	 * this request. This method returns an empty <code>Set</code> if the request has no
	 * attributes available to it.
	 *
	 * @return an <code>Enumeration</code> of strings containing the names of the
	 *         request's attributes
	 */
	Set<String> getAttributeNames();

	/**
	 * Removes all attributes from this container.
	 */
	void removeAllAttributes();

	/**
	 * Removes an attribute from the context of this request. If the attribute is not
	 * found, this method has no effect.
	 * <p>
	 * Attribute names should follow the naming conventions of {@link CommonStrings}.
	 *
	 * @param key
	 *            a <code>String</code> specifying the name of the attribute
	 */
	void removeAttribute(String key);

	/**
	 * Stores an attribute in the context of this request.
	 * <p>
	 * Attribute names should follow the naming conventions of {@link CommonStrings}.
	 *
	 * @param key
	 *            a <code>String</code> specifying the name of the attribute
	 * @param value
	 *            an <code>Object</code> containing the context of the request
	 */
	void setAttribute(String key, Object value);

	/**
	 * Stores an attribute in the context of this request by a Labeled object. This is a
	 * convenience method that gets an attribute from a {@link Labeled} object by calling
	 * its {@link Labeled#getLabel()} method for the attribute's name.
	 * <p>
	 * This method has no effect if the <code>label</code> object is null.
	 *
	 * @param label
	 *            an object with a label to use for the key
	 * @param value
	 *            an <code>Object</code> containing the context of the request
	 */
	void setAttribute(Labeled label, Object value);
}
