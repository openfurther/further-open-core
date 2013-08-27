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
package edu.utah.further.dts.api.service;

import java.util.Collection;
import java.util.List;

import edu.utah.further.core.api.message.Severity;
import edu.utah.further.dts.api.domain.concept.ConceptReport;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;

/**
 * Centralizes utilities related to validating content entered by FURTHeR content
 * engineers into DTS (tests concepts and their inter-namespace associations).
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
 * @version Sep 2, 2010
 */
public interface ContentValidationService
{
	// ========================= METHODS ===================================

	/**
	 * Find the subset of codes in a source namespace that do not exist in a namespace.
	 *
	 * @param sourceNamespace
	 *            source namespace to look under for concepts
	 * @param propertyName
	 *            property name to search by
	 * @param inputValues
	 *            list of input {@link ConceptReport} objects. Each object only contains
	 *            the code = property value to search for
	 * @return a list of reports corresponding to each of the codes. Reports' severity
	 *         fields of missing concepts are set to {@link Severity#ERROR}, otherwise
	 *         {@value Severity#INFO}. Report references are different than those in the
	 *         <code>inputValues</code> collection
	 */
	List<ConceptReport> findInexistentCodes(DtsNamespace sourceNamespace,
			String propertyName, Collection<String> inputValues);

	/**
	 * Find the subset of codes in a source namespace that do not have an association to a
	 * target namespace. These are typically codes from a standard namespace that were not
	 * mapped by the content engineer to a local namespace.
	 *
	 * @param sourceNamespace
	 *            source namespace to look under for concepts
	 * @param propertyName
	 *            property name to search by
	 * @param inputValues
	 *            list of input {@link ConceptReport} objects. Each object only contains
	 *            the code to search for
	 * @param targetNamespace
	 *            target namespace of the association
	 * @return a list of reports corresponding to each of the codes. Reports' severity
	 *         fields of missing concepts are set to {@link Severity#ERROR}, otherwise
	 *         {@value Severity#INFO}. Report references are different than those in the
	 *         <code>inputValues</code> collection
	 */
	List<ConceptReport> findCodesWithoutTranslation(DtsNamespace sourceNamespace,
			String propertyName, Collection<String> inputValues,
			DtsNamespace targetNamespace);
}