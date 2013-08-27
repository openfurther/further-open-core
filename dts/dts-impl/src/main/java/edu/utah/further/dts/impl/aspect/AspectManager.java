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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;

/**
 * Holds a list of DTS AOP aspects. Illustrates how to nest custom tags within a custom
 * tag of a spring schema.
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
 * @version Oct 18, 2009
 */
public class AspectManager
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(DtsTransactionAspect.class);

	// ========================= FIELDS ====================================

	/**
	 * List of active aspects. Any object can be an aspect, so there's no wildcard bound.
	 */
	private final List<Object> aspects = newList();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create this aspect.
	 */
	public AspectManager()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Creating aspect manager");
		}
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return
	 */
	public List<Object> getAspects()
	{
		return newList(aspects);
	}

	/**
	 * mmm, there is no setter method for the 'aspects' property.
	 * 
	 * @param aspect
	 *            a DTS aspect to add to the manager's active list. Must have a
	 *            no-argument constructor and annotated with {@link Aspect}
	 */
	public void addAspect(final Object aspect)
	{
		aspects.add(aspect);
	}
}