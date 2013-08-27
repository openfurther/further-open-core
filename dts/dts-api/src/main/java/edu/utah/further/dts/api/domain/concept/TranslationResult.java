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
package edu.utah.further.dts.api.domain.concept;

import java.util.List;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.exception.ApplicationError;
import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A JavaBean representing a DTS concept translation result. Immutable.
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
 * @version Oct 19, 2009
 */
public final class TranslationResult
{
	// ========================= CONSTANTS =================================

	/**
	 * A result with <code>null</code> fields.
	 */
	public static final TranslationResult NULL_RESULT = new TranslationResult(null);

	// ========================= FIELDS ====================================

	/**
	 * Target concepts.
	 */
	private List<DtsConcept> concepts = CollectionUtil.newList();

	/**
	 * Is the source concept's namespace a standard FURTHeR namespace.
	 */
	private final Boolean standardSourceNamespace;

	/**
	 * A flag indicating whether the source concept was found. <code>null</code> indicates
	 * "not applicable".
	 */
	private final Boolean foundSourceConcept;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param concepts
	 * @param standardSourceNamespace
	 * @param foundSourceConcept
	 */
	public TranslationResult(final List<DtsConcept> concepts)
	{
		this(concepts, null, null);
	}

	/**
	 * @param concepts
	 * @param standardSourceNamespace
	 * @param foundSourceConcept
	 */
	public TranslationResult(final List<DtsConcept> concepts,
			final Boolean standardSourceNamespace, final Boolean foundSourceConcept)
	{
		super();
		this.concepts = concepts;
		this.standardSourceNamespace = standardSourceNamespace;
		this.foundSourceConcept = foundSourceConcept;
	}

	// ========================= METHODS ===================================

	/**
	 * Returns the unique concept or throws an exception if it is not unique
	 *
	 * @return
	 */
	public DtsConcept unique()
	{
		if (concepts.size() == 0)
		{
			throw new ApplicationException(new ApplicationError(
					ErrorCode.RESOURCE_NOT_FOUND, "No results returned"));
		}

		if (concepts.size() > 1)
		{
			throw new ApplicationException(new ApplicationError(
					ErrorCode.MULTIPLE_CONCEPTS_FOUND, "Nonunique result for concept"));
		}

		return concepts.get(0);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the concepts property.
	 *
	 * @return the concepts
	 */
	public List<DtsConcept> getConcepts()
	{
		return concepts;
	}

	/**
	 * Return the standardSourceNamespace property.
	 *
	 * @return the standardSourceNamespace
	 */
	public Boolean getStandardSourceNamespace()
	{
		return standardSourceNamespace;
	}

	/**
	 * Return the foundSourceConcept property.
	 *
	 * @return the foundSourceConcept
	 */
	public Boolean isFoundSourceConcept()
	{
		return foundSourceConcept;
	}
}