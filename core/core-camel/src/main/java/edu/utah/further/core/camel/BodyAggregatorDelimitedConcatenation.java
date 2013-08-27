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
package edu.utah.further.core.camel;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.text.StringUtil;

/**
 * A Camel processor that aggregates a list of strings into a delimited concatenated
 * string.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jul 3, 2009
 */
public final class BodyAggregatorDelimitedConcatenation implements BodyAggregator
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(BodyAggregatorDelimitedConcatenation.class);

	// ========================= FIELDS ====================================

	/**
	 * Delimiter of message bodies.
	 */
	private final String delimiter;

	/**
	 * A flag for keeping/removing XML document header.
	 */
	private boolean removeDocumentHeader = true;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a delimiter-based aggregation strategy.
	 * 
	 * @param delimiter
	 *            delimiter of message bodies
	 */
	public BodyAggregatorDelimitedConcatenation(final String delimiter)
	{
		super();
		this.delimiter = delimiter;
	}

	// ========================= IMPL: BodyAggregator ======================

	/**
	 * @param bodies
	 * @return
	 * @see edu.utah.further.core.camel.BodyAggregator#aggregate(java.util.List)
	 */
	@Override
	public final String aggregate(final List<String> bodies)
	{
		final List<String> transformedBodies = CollectionUtil.newList();
		for (final String body : bodies)
		{
			final String preProcessedBody = preProcess(body);
			transformedBodies.add(preProcessedBody);
			if (log.isDebugEnabled())
			{
				log.debug("Pre-processing " + body + " into " + preProcessedBody);
			}
		}
		final String chained = StringUtil.chain(transformedBodies, delimiter);
		if (log.isDebugEnabled())
		{
			log.debug("Chaining with delimiter " + delimiter + " to " + chained);
		}
		return chained;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the removeDocumentHeader property.
	 * 
	 * @return the removeDocumentHeader
	 */
	public boolean isRemoveDocumentHeader()
	{
		return removeDocumentHeader;
	}

	/**
	 * Set a new value for the removeDocumentHeader property.
	 * 
	 * @param removeDocumentHeader
	 *            the removeDocumentHeader to set
	 */
	public void setRemoveDocumentHeader(final boolean removeDocumentHeader)
	{
		this.removeDocumentHeader = removeDocumentHeader;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Pre-process a body before it is concatenated to all other bodies in the list passed
	 * into {@link #aggregate(List)}.
	 * 
	 * @param body
	 *            an individual message body (before aggregation)
	 * @return the pre-processed body
	 */
	protected final String preProcess(final String body)
	{
		return removeDocumentHeader ? body.replaceFirst(
				"<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>",
				Strings.EMPTY_STRING) : body;
	}
}
