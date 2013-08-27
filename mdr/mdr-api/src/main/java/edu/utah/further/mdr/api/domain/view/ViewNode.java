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
package edu.utah.further.mdr.api.domain.view;

import java.util.Collection;

import edu.utah.further.core.api.tree.Composite;

/**
 * A node of the view model graph. Contains a list of links (edges) to its neighboring
 * nodes so that we can navigate the graph, and an object of type {@link ViewElement}
 * (typically a JavaBean) that contains the information required to render the node.
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
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Aug 5, 2009
 */
public interface ViewNode<K, T extends ViewElement, N extends ViewNode<K, T, N>> extends
		Composite<N>
{
	// ========================= METHODS ===================================

	/**
	 * Return the key that this view node is for. Nodes are keyed by key objects in a
	 * {@link UmlViewModel}.
	 *
	 * @return key of this object
	 */
	K getKey();

	/**
	 * Implementations provide the means to set the key for this {@link ViewNode}.
	 *
	 * @param key
	 *            key to set for this node
	 */
	void setKey(K key);

	/**
	 * Return the associated view element that contains the information required to render
	 * this node.
	 *
	 * @return this node's element view data object
	 */
	T getViewElement();

	/**
	 * Set the new associated view element that contains the information required to
	 * render this node.
	 *
	 * @param viewElement
	 *            view element to set
	 * @return this node's element view data object
	 */
	void setViewElement(T viewElement);

	/**
	 * Add a new link to a node. If the child is already in the children collection, it is
	 * overridden.
	 *
	 * @param node
	 *            node to link to
	 */
	void addChild(N node);

	/**
	 * Add a collection of links to this node.
	 *
	 * @param links
	 *            links to add
	 */
	void addChildren(Collection<? extends N> links);

	/**
	 * Disconnect the link from this node to a node.
	 *
	 * @param node
	 *            node to remove from this node's linked list
	 */
	void removeChild(N node);
}