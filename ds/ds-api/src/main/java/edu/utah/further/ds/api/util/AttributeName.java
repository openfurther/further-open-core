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
package edu.utah.further.ds.api.util;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Labeled;

/**
 * Chain request attribute name conventions that request processors agree upon during a
 * data source life cycle.
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
 * @version Mar 16, 2010
 */
public enum AttributeName implements Labeled
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Metadata Service.
	 */
	META_DATA,

	/**
	 * <code>QueryContext</code>.
	 */
	QUERY_CONTEXT,
	
	/**
	 * A result context
	 */
	RESULT_CONTEXT,

	/**
	 * Execution ID.
	 */
	EXECUTION_ID,

	/**
	 * <code>SearchQuery</code>.
	 */
	SEARCH_QUERY,
	
	/**
	 * <code>SearchQuery</code> root entity object.
	 */
	SEARCH_QUERY_ROOT,

	/**
	 * The package where the rootObject of the SearchQuery exists.
	 */
	SEARCH_QUERY_PKG,

	/**
	 * Hibernate Session Factory.
	 */
	SESSION_FACTORY,

	/**
	 * Query result.
	 */
	QUERY_RESULT,

	/**
	 * Result page iterator.
	 */
	RESULT_PAGER,

	/**
	 * Query translation artifact.
	 */
	QUERY_TRANSLATION,

	/**
	 * Result set translation artifact.
	 */
	RESULT_TRANSLATION,
	
	/**
	 * The packages required to marshal results.
	 */
	RESULT_MARSHAL_PKGS,
	
	/**
	 * The packages required to unmarshal results after result translation.
	 */
	RESULT_UNMARSHAL_PKGS,

	// /**
	// * Result set translation - local DTS namespace ID. -- done via
	// QueryContext.getTargetNamespaceId()
	// */
	// RESULT_TRANSLATION_LOCAL_NAMESPACE_ID,

	/**
	 * Marshalled object.
	 */
	MARSHAL_OBJ,
	
	/**
	 * Marshalled pkgs.
	 */
	MARSHAL_PKGS,

	/**
	 * Marshalled result.
	 */
	MARSHAL_RSLT,

	/**
	 * Marshalled schema.
	 */
	MARSHAL_SCHEMA,

	/**
	 * Result translation schema.
	 */
	RESULT_SCHEMA,

	/**
	 * Query schema.
	 */
	QUERY_SCHEMA,

	/**
	 * Unmarshalled src.
	 */
	UNMARSHAL_SRC,

	/**
	 * Unmarshalled pkgs.
	 */
	UNMARSHAL_PKGS,

	/**
	 * Unmarshalled result.
	 */
	UNMARSHAL_RSLT,

	/**
	 * Persistent entities.
	 */
	PERSIST_ENTITIES,

	/**
	 * Attribute for the data source type (JDBC/web service/...).
	 */
	DS_TYPE,

	/**
	 * Attribute for the value of the pager object controlling an on-going paging loop.
	 */
	PAGING_LOOP_CONTROLLER,

	/**
	 * Attribute for the value of the number of header rows to ignore at the beginning of
	 * the paging loop.
	 */
	PAGING_NUM_HEADER_ROWS,

	/**
	 * Overrides the default paging request processor's page size for this particular
	 * request if set.
	 */
	PAGING_PAGE_SIZE,

	/**
	 * Datasource webservice client.
	 */
	WS_DS_CLIENT;

	// ========================= CONSTANTS =================================

	/**
	 * Cached part of {@link #getLabel()}'s computation.
	 */
	private static final String LABEL_PREFIX = AttributeName.class.getCanonicalName()
			+ Strings.PROPERTY_SCOPE_CHAR;

	// ========================= IMPL: Labeled ==============================

	/**
	 * @return
	 * @see edu.utah.further.core.api.context.Labeled#getLabel()
	 */
	@Override
	public String getLabel()
	{
		return LABEL_PREFIX + name();
	}

	// ========================= IMPL: Object ==============================

	/**
	 * @return
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return getLabel();
	}

}
