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
package edu.utah.further.core.api.scope;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Standard namespaces with the ability to support custom namespaces
 * 
 * <p>
 * ---------------------------------------------------------------------------------- -<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * ---------------------------------------------------------------------------------- -
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 22, 2013
 * @see https://blogs.oracle.com/darcy/entry/enums_and_mixins
 */
public enum Namespaces implements Namespace
{
	/**
	 * International Statistical Classification of Diseases and Related Health Problems 9
	 * namespace
	 */
	ICD_9_CM,

	/**
	 * International Statistical Classification of Diseases and Related Health Problems 10
	 * namespace
	 */
	ICD_10,
	
	/**
	 * International Classification of Diseases for Oncology namespace
	 */
	ICD_O,

	/**
	 * Coding for Interventions 4 namespace
	 */
	CPT_4,

	/**
	 * SNOMED Clinical Terms namespace
	 */
	SNOMED_CT,

	/**
	 * Health Level 7 namespace
	 */
	HL7,

	/**
	 * National Cancer Institute Thesaurus namespace
	 */
	NCI,

	/**
	 * Logical Observation Identifiers Names and Codes namespace
	 */
	LOINC,

	/**
	 * FURTHeR namespace
	 */
	FURTHER,

	/**
	 * Multum Drug namespace
	 */
	MULTUMDRUG,

	/**
	 * Medical Subject Headings namespace
	 */
	MESH,

	/**
	 * RxNorm namespace
	 */
	RXNORM,

	/**
	 * RxTerms namespace
	 */
	RXTERMS,

	/**
	 * Unified Code for Units of Measure namespace
	 */
	UCUM;

	/**
	 * Namespace properties follow the pattern namespace.[namespace name].name or
	 * namespace.[namespace name].id
	 */
	private static final String NAMESPACE = "namespace.";

	/**
	 * 
	 */
	private static final ConcurrentHashMap<String, Namespace> standardPlusCustomNamespaces = new ConcurrentHashMap<>();

	/**
	 * Factory method for namespaces that returns an existing namespace or creates a new
	 * one if it does not exist. For example: Namespace.namespaceFor('SNOMED_CT') would
	 * return the standard SNOMED_CT namespace but
	 * Namespace.namespaceFor('CUSTOM_NAMESPACE') would return a new Namespace
	 * representing the CUSTOM_NAMSPACE.
	 * 
	 * @param name
	 * @return
	 */
	public static Namespace namespaceFor(final String name)
	{
		// Add all of the standard namespaces so this can be used (although discouraged
		// since compile time ENUM constants exist) on standard namespaces.
		if (standardPlusCustomNamespaces.isEmpty())
		{
			for (final Namespaces namespace : values())
			{
				standardPlusCustomNamespaces.putIfAbsent(namespace.name().toLowerCase(),
						namespace);
			}
		}

		standardPlusCustomNamespaces.putIfAbsent(name.toLowerCase(), new Namespace()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see edu.utah.further.core.api.scope.Namespace#getKeyBase()
			 */
			@Override
			public String getLookupKey()
			{
				return NAMESPACE + name.toLowerCase();
			}

			/*
			 * (non-Javadoc)
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString()
			{
				return name.toLowerCase();
			}
			
			
		});

		return standardPlusCustomNamespaces.get(name.toLowerCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.scope.Namespace#getKeyBase()
	 */
	@Override
	public String getLookupKey()
	{
		return NAMESPACE + name().toLowerCase();
	}

}
