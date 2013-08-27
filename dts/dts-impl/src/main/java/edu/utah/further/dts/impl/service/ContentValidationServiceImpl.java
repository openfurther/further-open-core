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

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.message.Severity;
import edu.utah.further.dts.api.domain.concept.ConceptReport;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.concept.TranslationResult;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.ContentValidationService;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.api.service.DtsOptions;
import edu.utah.further.dts.api.to.DtsConceptId;

/**
 * Centralizes utilities related to validating content entered by FURTHeR content
 * engineers into DTS (tests concepts and their inter-namespace associations).
 * <p>
 * This implementation delegates to the active {@link DtsOperationService} implementation
 * in the current context.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @see https://jira.chpc.utah.edu/browse/FUR-1249
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Sep 2, 2010
 */
@Service("contentValidationService")
public class ContentValidationServiceImpl implements ContentValidationService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ContentValidationServiceImpl.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes DTS services.
	 */
	@Autowired
	private DtsOperationService dos;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: ContentValidationService ==

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.dts.api.service.ContentValidationService#findInexistentCodes(edu
	 * .utah.further.dts.api.domain.namespace.DtsNamespace, java.lang.String,
	 * java.util.Collection)
	 */
	@Override
	public List<ConceptReport> findInexistentCodes(final DtsNamespace sourceNamespace,
			final String propertyName, final Collection<String> inputValues)
	{
		final List<ConceptReport> outputReports = CollectionUtil.newList();
		final String sourceNamespaceName = sourceNamespace.getName();
		for (final String code : inputValues)
		{
			final DtsConcept concept = dos.findConceptById(new DtsConceptId(
					sourceNamespaceName, propertyName, code),
					DtsOptions.DEFAULT_NO_ATTRIBUTES);
			final boolean foundConcept = (concept != null);

			// Flag concepts that don't exist
			final ConceptReport outputReport = new ConceptReport(code);
			outputReport.setSeverity(foundConcept ? Severity.INFO : Severity.ERROR);
			outputReports.add(outputReport);
		}
		return outputReports;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.dts.api.service.ContentValidationService#findMissingCodes(edu.
	 * utah.further.dts.api.domain.namespace.DtsNamespace, java.lang.String,
	 * java.util.Collection, edu.utah.further.dts.api.domain.namespace.DtsNamespace)
	 */
	@Override
	public List<ConceptReport> findCodesWithoutTranslation(
			final DtsNamespace sourceNamespace, final String propertyName,
			final Collection<String> inputValues, final DtsNamespace targetNamespace)
	{
		final List<ConceptReport> outputReports = CollectionUtil.newList();
		final String sourceNamespaceName = sourceNamespace.getName();
		final String targetNamespaceName = targetNamespace.getName();
		for (final String code : inputValues)
		{
			final TranslationResult translationResult = dos.translateConcept(
					new DtsConceptId(sourceNamespaceName, propertyName, code),
					new DtsOptions(targetNamespaceName, null)
							.setThrowExceptionOnFailure(false));
			final boolean foundTranslation = !translationResult.getConcepts().isEmpty();

			// Flag concepts that have 0 associations. If they have one or more
			// associations, they are OK
			final ConceptReport outputReport = new ConceptReport(code);
			outputReport.setSeverity(foundTranslation ? Severity.INFO : Severity.ERROR);
			outputReports.add(outputReport);
		}
		return outputReports;
	}

}
