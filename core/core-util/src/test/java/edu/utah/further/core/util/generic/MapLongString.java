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
package edu.utah.further.core.util.generic;

import java.util.Map;

import com.google.common.collect.ForwardingMap;

import edu.utah.further.core.api.context.Implementation;

/**
 * A wrapper of a generic map that lends itself to Spring auto-wiring, because the
 * internal map property uses a setter-DI that can convert correctly generic types.
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
@Implementation
final class MapLongString extends ForwardingMap<Long, String>
{
	// ========================= FIELDS ====================================

	private final Map<Long, String> map;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param map
	 */
	public MapLongString(final Map<Long, String> map)
	{
		this.map = map;
	}

	// ========================= IMPLEMENTATION: ForwardingMap =============

	/**
	 * @return
	 * @see com.google.common.collect.ForwardingMap#delegate()
	 */
	@Override
	protected Map<Long, String> delegate()
	{
		return map;
	}
}
