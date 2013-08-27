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
package edu.utah.further.dts.impl.service;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.constant.ErrorCode.BAD_CONCEPT_TYPE;
import static edu.utah.further.core.api.constant.ErrorCode.MULTIPLE_CONCEPTS_FOUND;
import static edu.utah.further.core.api.constant.ErrorCode.RESOURCE_NOT_FOUND;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;

import com.apelon.dts.client.association.ConceptAssociation;
import com.apelon.dts.client.concept.DTSConcept;
import com.apelon.dts.client.concept.OntylogConcept;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.service.DtsOptions;
import edu.utah.further.dts.impl.domain.DtsConceptImpl;

/**
 * Common utilities that were re-factored from {@link DtsOperationServiceImpl} to make it
 * simpler.
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
 * @version Nov 6, 2008
 */
@Utility
class DtsOperationUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(DtsOperationUtil.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * @param concept
	 */
	public static void validateBusinessEntity(final DtsConcept concept)
	{
		final Class<DtsConceptImpl> superClazz = DtsConceptImpl.class;
		if (!ReflectionUtil.instanceOf(concept, superClazz))
		{
			throw new ApplicationException(BAD_CONCEPT_TYPE, "Concept type "
					+ concept.getClass() + " is not a sub-class of " + superClazz);
		}
	}

	/**
	 * @param line
	 * @return
	 */
	public static List<String> parseTqlResultLine(final String line)
	{
		// Assuming lines are in the format "item1 |item2 | item3 | ..."
		try (final Scanner lineScanner = new Scanner(line))
		{
			lineScanner.useDelimiter("\\s*\\|\\s*");
			final List<String> items = newList();
			while (lineScanner.hasNext())
			{
				items.add(lineScanner.next());
			}
			return items;
		}
	}

	/**
	 * Validate that a match list contains a unique match.
	 *
	 * @param matches
	 *            list of matches
	 * @throws ApplicationException
	 *             if matches list is not of size 1.
	 */
	public static List<DtsConcept> validateAndReturnUniqueMatch(
			final List<DtsConcept> matches, final DtsOptions options)
	{
		final int numMatches = matches.size();
		if (options.isThrowExceptionOnFailure())
		{
			if (numMatches == 0)
			{
				throw new ApplicationException(RESOURCE_NOT_FOUND,
						"Expected a unique concept but did not find any concepts");
			}
			if (numMatches > 1)
			{
				throw new ApplicationException(MULTIPLE_CONCEPTS_FOUND,
						"Expected a unique concept but found " + numMatches + " concepts");
			}
		}
		return (numMatches != 1) ? null : matches;
	}

	/**
	 * Validate that a match list contains a unique match.
	 *
	 * @param matches
	 *            list of matches
	 * @throws ApplicationException
	 *             if matches list is not of size 1.
	 */
	public static List<DtsConcept> validateAndReturnMatches(
			final List<DtsConcept> matches, final DtsOptions options)
	{
		final int numMatches = matches.size();
		if (options.isThrowExceptionOnFailure())
		{
			if (numMatches == 0)
			{
				throw new ApplicationException(RESOURCE_NOT_FOUND,
						"Expected at least one concept but did not find any concepts");
			}
		}
		return (numMatches == 0) ? null : matches;
	}

	/**
	 * Is a concept [inversely] associated to another concept.
	 *
	 * @param apelonConcept
	 *            concept to examine
	 * @param apelonOtherConcept
	 *            [inversely] associated concept
	 * @param inverse
	 *            if <code>true</code>, searches for an inversely associated concept,
	 *            otherwise for a forward-associated concept
	 * @return <code>true</code> if and only if the [inverse] association exists in
	 *         <code>concept</code>
	 */
	public static boolean isConceptAssociatedTo(final OntylogConcept apelonConcept,
			final OntylogConcept apelonOtherConcept, final boolean inverse)
	{
		final ConceptAssociation[] associationArray = inverse ? apelonConcept
				.getFetchedInverseConceptAssociations() : apelonConcept
				.getFetchedConceptAssociations();
		for (final ConceptAssociation association : associationArray)
		{
			final DTSConcept associatedConcept = inverse ? association.getFromConcept()
					: association.getToConcept();
			if (apelonOtherConcept.equals(associatedConcept))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Takes an array of Apelon concepts and converts them to a List of {@link DtsConcept}
	 * s.
	 *
	 * @param ontylogConcepts
	 *            array of {@link OntylogConcept}s
	 * @return
	 */
	public static <T extends DTSConcept> List<DtsConcept> toDtsConceptList(
			final T[] ontylogConcepts)
	{
		final List<DtsConcept> concepts = CollectionUtil.newList();

		for (int i = 0; i < ontylogConcepts.length; i++)
		{
			concepts.add(new DtsConceptImpl((OntylogConcept) ontylogConcepts[i]));
		}

		return concepts;
	}

	/**
	 * Takes a list of Apelon concepts and converts them to a List of {@link DtsConcept}s.
	 *
	 * @param ontylogConcepts
	 *            array of {@link OntylogConcept}s
	 * @return
	 */
	public static <T extends DTSConcept> List<DtsConcept> toDtsConceptList(
			final List<? extends T> ontylogConcepts)
	{
		final List<DtsConcept> concepts = CollectionUtil.newList();
		for (final T ontylogConcept : ontylogConcepts)
		{
			concepts.add(new DtsConceptImpl((OntylogConcept) ontylogConcept));
		}
		return concepts;
	}

}
