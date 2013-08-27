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

/**
 * A simple {@link ViewNode} which does not maintain any edges; a disconnected graph. This
 * class is built for extension and easy implementation of {@link ViewNode}
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Aug 6, 2009
 */
public abstract class SimpleViewNode<K, T extends ViewElement, N extends SimpleViewNode<K,T,N>> implements ViewNode<K, T, N>
{
	// ========================= FIELDS ====================================

	/**
	 * Key of this object.
	 */
	private K key;

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Implementations provide the means to get the {@link ViewElement} for this
	 * {@link ViewNode}.
	 *
	 * @return
	 * @see edu.utah.further.mdr.api.domain.view.ViewNode#getViewElement()
	 */
	@Override
	public abstract T getViewElement();

	/**
	 * Implementations provide the means to set the {@link ViewElement} for this
	 * {@link ViewNode}
	 *
	 * @param viewElement
	 *            view element to link this node to
	 * @see edu.utah.further.mdr.api.domain.view.ViewNode#setViewElement(edu.utah.further.mdr.api.domain.view.ViewElement)
	 */
	@Override
	public abstract void setViewElement(T viewElement);

	// ========================= IMPLEMENTATION: ViewNode ==================

	/**
	 * Unsupported operation, subclasses which need this functionality can override.
	 *
	 * @param node
	 * @see edu.utah.further.mdr.api.domain.view.ViewNode#addChild(edu.utah.further.mdr.api.domain.view.ViewNode)
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void addChild(final N node)
	{
		throw new UnsupportedOperationException(
				"addChild not supported by SimpleViewNode");
	}

	/**
	 * Unsupported operation, subclasses which need this functionality can override.
	 *
	 * @param links
	 * @see edu.utah.further.mdr.api.domain.view.ViewNode#addChildren(java.util.Collection)
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void addChildren(final Collection<? extends N> links)
	{
		throw new UnsupportedOperationException(
				"addChildren not support by SimpleViewNode");

	}

	/**
	 * Unsupported operation, subclasses which need this functionality can override.
	 *
	 * @param node
	 * @see edu.utah.further.mdr.api.domain.view.ViewNode#removeChild(edu.utah.further.mdr.api.domain.view.ViewNode)
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void removeChild(final N node)
	{
		throw new UnsupportedOperationException(
				"removeChild not supported by SimpleViewNode");
	}

	/**
	 * Implementations provide the means to get the key for this {@link ViewNode}.
	 *
	 * @return key of this node
	 * @see edu.utah.further.mdr.api.domain.view.ViewNode#getViewElement()
	 */
	@Override
	public K getKey()
	{
		return key;
	}

	/**
	 * Implementations provide the means to set the key for this {@link ViewNode}.
	 *
	 * @param key
	 *            key to set for this node
	 * @see edu.utah.further.mdr.api.domain.view.ViewNode#setViewElement(edu.utah.further.mdr.api.domain.view.ViewElement)
	 */
	@Override
	public void setKey(final K key)
	{
		this.key = key;
	}

	// ========================= IMPLEMENATION: Composite ==================

	/**
	 * Unsupported operation, subclasses which need this functionality can override.
	 *
	 * @return
	 * @see edu.utah.further.core.api.tree.Composite#getChildren()
	 * @throws UnsupportedOperationException
	 */
	@Override
	public Collection<N> getChildren()
	{
		throw new UnsupportedOperationException(
				"getChildren not supported by SimpleViewNode");
	}

	/**
	 * Unsupported operation, subclasses which need this functionality can override.
	 *
	 * @return
	 * @see edu.utah.further.core.api.tree.Composite#getHasChildren()
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean getHasChildren()
	{
		throw new UnsupportedOperationException(
				"getHasChildren not supported by SimpleViewNode");
	}

	// ========================= PRIVATE METHODS: =========================
}
