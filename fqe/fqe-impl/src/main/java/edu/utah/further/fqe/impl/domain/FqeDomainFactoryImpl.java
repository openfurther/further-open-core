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
package edu.utah.further.fqe.impl.domain;

import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static edu.utah.further.core.query.domain.SearchQueryTo.newCopy;
import static org.slf4j.LoggerFactory.getLogger;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * The default factory of persistent implementations of the FQE domain entities.
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
 * @version Mar 25, 2010
 */
@Service("fqeDomainFactory")
public class FqeDomainFactoryImpl implements FqeDomainFactory
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(FqeDomainFactoryImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * Marshalling/Unmarshalling service.
	 */
	@Autowired
	private XmlService xmlService;

	// ========================= IMPL: FqeDomainFactoryImpl ================

	// /**
	// * @return
	// * @see edu.utah.further.fqe.impl.domain.FqeDomainFactory#newQueryContext()
	// */
	// public QueryContext newQueryContext()
	// {
	// return new QueryContextEntity();
	// }

	/**
	 * Marshal and set the query xml
	 *
	 * @param other
	 * @return
	 * @see edu.utah.further.fqe.impl.domain.FqeDomainFactory#newQueryContextEntity(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public QueryContextEntity newQueryContextEntity(final QueryContext other)
	{
		// Deep-copy QC; does not include query XML fields
		final QueryContextEntity entity = new QueryContextEntity().copyFrom(other);

		// Set query XML fields
		for (final SearchQuery query : entity.getQueries())
		{
			if (instanceOf(query, SearchQueryEntity.class))
			{
				final SearchQueryEntity queryEntity = (SearchQueryEntity) query;
				queryEntity.setQueryXml(marshalSearchQuery(query));
				queryEntity.setQueryContext(entity);
				queryEntity.setQid(query.getId());
			}
			else
			{
				log
						.error("Skipped copying query XML because search query is not of SearchQueryEntity type. We shouldn't be here because it must be of this type if it this is a child of a QueryContextEntity!");
			}
		}
		return entity;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param otherQuery
	 * @return
	 */
	private String marshalSearchQuery(final SearchQuery otherQuery)
	{
		String searchQueryXml = null;
		if (otherQuery != null)
		{
			try
			{
				// Have to copy to SearchQueryTo for marshalling
				searchQueryXml = xmlService.marshal(newCopy(otherQuery),
						xmlService.options());
			}
			catch (final JAXBException e)
			{
				log.error(
						"Failed to marshal SearchQuery for copying QueryContext entity",
						e);
			}
		}
		else
		{
			log.warn("QueryContext query was null when creating a entity to persist");
		}
		return searchQueryXml;
	}
}
