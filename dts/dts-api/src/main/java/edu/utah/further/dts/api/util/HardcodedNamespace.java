/*******************************************************************************
 * Source File: HardcodedNamespace.java
 ******************************************************************************/
package edu.utah.further.dts.api.util;

import edu.utah.further.core.api.context.Named;

/**
 * (Carried over from Further code base.)
 * 
 * An enum containing DTS namespace information for DTS service testing. Note that these
 * are soft references that depend on specific data assumed to exist in the DTS database.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 1, 2009
 */
public enum HardcodedNamespace implements Named
{
	// ========================= ENUMERATED CONSTANTS ======================

	ICD9("ICD-9-CM", 10),

	ICD10("ICD-10 R", 1518),
	
	ICDO("ICD-O", 65043),

	CPT("CPT", 20),

	SNOMED("SNOMED CT", 30),

	HL7("HL7", 1017),

	NCI("NCI Thesaurus [National Cancer Institute Thesaurus]", 1039),

	LOINC("LOINC", 5102),

	CERNER("Cerner", 32770),

	SNOMED_EXTENSION("FurtherOntylog", 32771),

	FURTHER("Further", 32769),

	UUEDW("UUEDW", 32776),

	UPDBL("UPDBL", 32774),

	OMOP_V2("OMOP-v2", 32868),

	OPENMRS_V1_9("OpenMRS-V1_9", 32812),

	UUEDW_APO("UUEDW APO", 64901),

	IH_APO("Intermountain APO", 64902),

	MULTUMDRUG("MultumDrug", 60),

	MESH("MeSH", 1033),

	RXNORM_R("RxNorm R", 1552),

	RXTERMS("RxTerms", 32804);

	// ========================= FIELDS =======================================

	/**
	 * Namespace's name.
	 */
	private final String name;

	/**
	 * Namespace's name.
	 */
	private final int id;

	// ========================= CONSTRUCTORS =================================

	/**
	 * @param name
	 * @param id
	 */
	private HardcodedNamespace(final String name, final int id)
	{
		this.name = name;
		this.id = id;
	}

	// ========================= IMPL: Named ==================================

	/**
	 * Return this DTS namespace's name.
	 * 
	 * @return header name string
	 * @see edu.utah.further.core.api.context.Named#getName()
	 */
	@Override
	public final String getName()
	{
		return name;
	}

	// ========================= GET / SET ====================================

	/**
	 * Return this DTS namespace's ID. (non-Javadoc)
	 */
	public int getId()
	{
		return id;
	}
}
