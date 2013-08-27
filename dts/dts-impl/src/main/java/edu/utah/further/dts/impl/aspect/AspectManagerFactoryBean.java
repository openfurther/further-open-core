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
package edu.utah.further.dts.impl.aspect;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;

/**
 * A spring factory bean of an {@link AspectManager}. Exposes an aspect setter that the
 * manager does not.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @see http://static.springsource.org/spring/docs/2.5.6/reference/extensible-xml.html
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Oct 18, 2009
 */
public class AspectManagerFactoryBean implements FactoryBean<AspectManager>
{
	// ========================= FIELDS ====================================

	/**
	 * A composite object described by a DTS custom schema tag.
	 */
	private AspectManager parent;

	/**
	 * A list of active DTS AOP aspects. Each described by a DTS custom schema tag nested
	 * within the parent manager's tag.
	 */
	private List<Object> children;

	// ========================= IMPLEMENTATION: FactoryBean ==============

	/**
	 * @return
	 * @throws Exception
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public AspectManager getObject() throws Exception
	{
		if (children != null)
		{
			for (final Object childComponent : children)
			{
				parent.addAspect(childComponent);
			}
		}
		return parent;
	}

	/**
	 * @return
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<AspectManager> getObjectType()
	{
		return AspectManager.class;
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
	public void setParent(final AspectManager parent)
	{
		this.parent = parent;
	}

	/**
	 * @param children
	 */
	public void setChildren(final List<Object> children)
	{
		this.children = children;
	}
}