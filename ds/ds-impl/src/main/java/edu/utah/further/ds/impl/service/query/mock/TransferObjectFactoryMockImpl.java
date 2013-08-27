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

import edu.utah.further.core.api.xml.MutableTransferList;
import edu.utah.further.ds.api.service.query.logic.ResultTranslator;
import edu.utah.further.ds.api.service.query.logic.TransferObjectFactory;

/**
 * @deprecated
 * @see EntityTransferList
 *
 *      Mock implementation of a transfer object factory for {@link ResultTranslator}.
 *      <p>
 *
 *
 *      -----------------------------------------------------------------------------------
 *      <br>
 *      (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 *      Contact: {@code <further@utah.edu>}<br>
 *      Biomedical Informatics, 26 South 2000 East<br>
 *      Room 5775 HSEB, Salt Lake City, UT 84112<br>
 *      Day Phone: 1-801-581-4080<br>
 *
 *
 *      -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Jun 4, 2010
 */
/*
 * @Mock
 *
 * @Service("transferObjectFactoryMock")
 */
@Deprecated
public class TransferObjectFactoryMockImpl implements TransferObjectFactory
{
	// ========================= CONSTANTS =================================

	// ========================= Impl: TransferObjectFactory ===============

	/**
	 * Note: {@link ClassCastException}s will be thrown if <code>T</code> does not match
	 * <code>fullyQualifiedClass</code>.
	 */
	@Override
	public <T> MutableTransferList<T> newInstance(
			final String fullyQualifiedClass, final List<? extends T> entities)
	{
		return null;
	}

}
