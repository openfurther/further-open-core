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
package edu.utah.further.party3.apelon.internal;

/**
 * An enum containing all the namespaces of interest to DTS service testing. Note that
 * these are soft references that depend on specific data assumed to exist in the physical
 * DTS.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 1, 2009
 */
public enum ApelonHardcodedNamespaceForTests
{
	// ========================= ENUMERATED CONSTANTS ======================

	CERNER("Cerner"),

	ICD9("ICD-9-CM"),

	SNOMED("SNOMED CT"),

	SNOMED_EXTENSION("FurtherOntylog"),

	HL7("HL7"),

	UUEDW("UUEDW"),

	NCI("NCI Thesaurus [National Cancer Institute Thesaurus]"),

	LOINC("LOINC [Logical Observation Identifiers Names and Codes]"),

	CPT("CPT");

	// ========================= FIELDS =======================================

	/**
	 * Namespace's name.
	 */
	private final String name;

	// ========================= CONSTRUCTORS =================================

	/**
	 * @param name
	 */
	private ApelonHardcodedNamespaceForTests(final String name)
	{
		this.name = name;
	}

	// ========================= METHODS ======================================

	/**
	 * Return this DTS namespace's name.
	 * 
	 * @return header name string
	 * @see edu.utah.further.core.api.annotation.HttpHeader#getName()
	 */
	public final String getName()
	{
		return name;
	}
}
