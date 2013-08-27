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
package edu.utah.further.ds.impl.service.query.mock;

import java.util.List;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.xml.MutableTransferList;

/**
 * A facade that adapts a plain entity {@link List} to an {@link EntityTransferList}.
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
 * @version Jan 27, 2011
 */
public final class MutableTransferListFacadeImpl<T> implements MutableTransferList<T>
{
	// ========================= FIELDS ====================================

	/**
	 * Internal list of entities.
	 */
	private final List<T> entityList = CollectionUtil.newList();

	// ========================= IMPL: EntityTransferList ==================
	/**
	 * @return
	 * @see edu.utah.further.core.api.xml.TransferList#getList()
	 */
	@Override
	public List<T> getList()
	{
		return entityList;
	}

	/**
	 * @see edu.utah.further.core.api.xml.EntityTransferList#setEntities(java.util.List)
	 * @param entities
	 */
	@Override
	public void setList(final List<? extends T> entities)
	{
		CollectionUtil.setListElements(entityList, entities);
	}
}
