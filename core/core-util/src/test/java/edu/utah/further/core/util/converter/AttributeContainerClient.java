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
package edu.utah.further.core.util.converter;

import java.util.List;

import edu.utah.further.core.api.chain.AttributeContainer;
import edu.utah.further.core.api.chain.AttributeContainerImpl;
import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * A simple client bean of {@link AttributeContainer}s that exposes the overlay of all
 * containers. For property editor tests.
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
 * @version Aug 18, 2010
 */
public final class AttributeContainerClient
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Request attributes placed/removed by handlers.
	 */
	private final List<AttributeContainer> containers = CollectionUtil.newList();

	/**
	 * The union (more precisely, overlay in the list order) of {@link #containers}.
	 */
	private final AttributeContainer mainContainer = new AttributeContainerImpl();

	// ========================= GET & SET =================================

	/**
	 * Return the containers property.
	 *
	 * @return the containers
	 */
	public List<AttributeContainer> getContainers()
	{
		return containers;
	}

	/**
	 * Set a new value for the containers property.
	 *
	 * @param containers
	 *            the containers to set
	 */
	public void setContainers(final List<AttributeContainer> containers)
	{
		this.containers.clear();
		for (final AttributeContainer container : containers)
		{
			addContainer(container);
		}
	}

	/**
	 * Return the mainContainer property.
	 *
	 * @return the mainContainer
	 */
	public AttributeContainer getMainContainer()
	{
		return mainContainer;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param container
	 */
	private void addContainer(final AttributeContainer container)
	{
		this.containers.add(container);
		// Overlay over current main container
		this.mainContainer.addAttributes(container.getAttributes());
	}

}
