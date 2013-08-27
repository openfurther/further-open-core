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
package edu.utah.further.core.metadata.to;

import static edu.utah.further.core.api.collections.CollectionUtil.newSet;
import static edu.utah.further.core.api.collections.CollectionUtil.toSet;

import java.util.Set;

import edu.utah.further.core.api.tree.Composite;

/**
 * Web service reflective element types.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
public enum WsElementType implements Composite<WsElementType>
{
	// ========================= CONSTANTS =================================

	/**
	 * A web service class.
	 */
	CLASS
	{
		/**
		 * Return the set of permissible children types of this type.
		 * 
		 * @return the set of children types
		 * @see edu.utah.further.WsElementType.api.domain.uml.ElementType#getChildren()
		 */
		@Override
		public Set<WsElementType> getChildren()
		{
			return toSet(PARAMETER);
		}
	},

	/**
	 * A web method.
	 */
	METHOD
	{
		/**
		 * Return the set of permissible children types of this type.
		 * 
		 * @return the set of children types
		 * @see edu.utah.further.WsElementType.api.domain.uml.ElementType#getChildren()
		 */
		@Override
		public Set<WsElementType> getChildren()
		{
			return toSet(PARAMETER);
		}
	},

	/**
	 * A method parameter.
	 */
	PARAMETER
	{
		/**
		 * Return the set of permissible children types of this type.
		 * 
		 * @return the set of children types
		 * @see edu.utah.further.WsElementType.api.domain.uml.ElementType#getChildren()
		 */
		@Override
		public Set<WsElementType> getChildren()
		{
			return newSet();
		}
	};

	// ========================= FIELDS =======================================

	// ========================= CONSTRUCTORS =================================

	// ========================= METHODS ======================================

	/**
	 * Return the set of permissible children types of this type.
	 * 
	 * @return the set of children types
	 */
	@Override
	public abstract Set<WsElementType> getChildren();

	/**
	 * @return
	 * @see edu.utah.further.core.math.tree.TreeNode#getHasChildren()
	 */
	@Override
	public boolean getHasChildren()
	{
		return !getChildren().isEmpty();
	}

}
