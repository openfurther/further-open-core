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
package edu.utah.further.core.util.schema;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;

/**
 * A factory bean facade that exposes a setter of a {@link Component}'s children property.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Oct 20, 2009
 */
public class ComponentFactoryBean implements FactoryBean<Component>
{
	// ========================= FIELDS ====================================

	/**
	 * Top-level bean.
	 */
	private Component parent;

	/**
	 * Children property of {@link #parent}.
	 */
	private List<Component> children;

	// ========================= IMPLEMENTATION: FactoryBean ===============

	/**
	 * @return
	 * @throws Exception
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Component getObject() throws Exception
	{
		if (this.children != null && this.children.size() > 0)
		{
			for (final Component childComponent : children)
			{
				this.parent.addComponent(childComponent);
			}
		}
		return this.parent;
	}

	/**
	 * @return
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<Component> getObjectType()
	{
		return Component.class;
	}

	/**
	 * @return
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton()
	{
		return true;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @param parent
	 */
	public void setParent(final Component parent)
	{
		this.parent = parent;
	}

	/**
	 * @param children
	 */
	public void setChildren(final List<Component> children)
	{
		this.children = children;
	}
}