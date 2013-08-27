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
package edu.utah.further.ds.api.service.query.logic;

import java.util.List;

import edu.utah.further.core.api.xml.MutableTransferList;

/**
 * Deprecated.
 *
 * @see EntityTransferList
 *
 *      A factory of data source transfer object. An alternative to reflection that is
 *      type-safe, faster than reflection, and suitable for OSGi deployment, because
 *      sometimes it is called from a reusable <code>ds</code> bundle that does not see
 *      the transfer object classes of the <code>ds</code> client bundle.
 *      <p>
 *
 *
 *
 *      ----------------------------------------------------------------------------------
 *      - <br>
 *      (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 *      Contact: {@code <further@utah.edu>}<br>
 *      Biomedical Informatics, 26 South 2000 East<br>
 *      Room 5775 HSEB, Salt Lake City, UT 84112<br>
 *      Day Phone: 1-801-581-4080<br>
 *
 *
 *
 *      ----------------------------------------------------------------------------------
 *      -
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Jun 4, 2010
 */
// @Deprecated //Deprecating the impls instead to avoid some eclipse warnings
public interface TransferObjectFactory
{
	// ========================= METHODS ===================================

	/**
	 * Return an transfer object that wraps a persistent entity list.
	 *
	 * @param <T>
	 *            transfer object type
	 * @param fullyQualifiedClass
	 *            fully-qualified name of the transfer object class to be instantiated
	 * @return entity list wrapper instance of type <code><T></code>, or <code>null</code>
	 *         , if this type of transfer object is not supported by the client data
	 *         source
	 */
	<T> MutableTransferList<T> newInstance(String fullyQualifiedClass,
			List<? extends T> entities);
}
