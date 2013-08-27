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
import java.util.Map;

import edu.utah.further.mdr.api.domain.uml.UmlElement;

/**
 * An abstraction for FURTHeR view models of some entity.A view model provides the data
 * structure required to render the model; it does not provide the physical view
 * technology. A {@link ViewModel} is a directed graph of {@link ViewNode}s, one per
 * {@link UmlElement}.
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
public interface ViewModel<K, N extends ViewNode<K, E, N>, E extends ViewElement>
{
	// ========================= METHODS ===================================

	/**
	 * Return the list of nodes in the view model's graph, keyed by their corresponding
	 * elements.
	 *
	 * @return this model's UML-element-to-view-node map
	 */
	Map<K, N> getNodeMap();

	/**
	 * Return the node of a particular view element.
	 *
	 * @param element
	 *            a UML element
	 * @return element's view node
	 */
	N getNode(K element);

	/**
	 * Return the number of nodes in this model.
	 *
	 * @return number of nodes
	 */
	int getSize();

	/**
	 * Add a new view node. If the child is already in the children collection, it is
	 * overridden.
	 *
	 * @param node
	 *            view node to add
	 */
	void addNode(N node);

	/**
	 * Add a collection of nodes to the model.
	 *
	 * @param nodes
	 *            nodes to add
	 */
	void addNodes(Collection<? extends N> nodes);

	/**
	 * Remove node from the model.
	 *
	 * @param node
	 *            node to remove
	 */
	void removeNode(N node);
}