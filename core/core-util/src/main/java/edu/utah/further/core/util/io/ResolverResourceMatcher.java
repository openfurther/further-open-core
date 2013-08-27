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
package edu.utah.further.core.util.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import edu.utah.further.core.api.context.Api;

/**
 * A filter of resources that resolves a resource path into a {@link Resource} class.
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
 * @version Oct 13, 2008
 */
@Api
public class ResolverResourceMatcher implements ResourceMatcher<Resource>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Resource resolver. DI-ed.
	 */
	@Autowired
	private ResourcePatternResolver resolver;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Return the textual representation of this matcher.
	 *
	 * @return the textual representation of this matcher object
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return "Resource resolver " + resolver;
	}

	// ========================= IMPLEMENTATION: ResourceMatcher ===========

	/**
	 * @param path
	 * @return
	 * @see edu.utah.further.core.util.io.ClasspathUtil.ResourceMatcher#match(java.lang.String)
	 */
	@Override
	public Resource match(final String path)
	{
		return resolver.getResource(path);
	}

	// ========================= PRIVATE METHODS ===========================

}
