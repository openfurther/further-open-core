package edu.utah.further.fqe.ds.model.further.export;

/*******************************************************************************
 * Source File: DemographicExportAttribute.java
 ******************************************************************************/

import edu.utah.further.core.api.exception.ApplicationException;

/**
 * Explicit attributes that are exported for demographics since all demographics are
 * mapped to ObservationFacts.
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
 * @version Oct 18, 2012
 */
public enum DemographicExportAttribute
{
	// ========================= ENUMERATED CONSTANTS ======================

	AGE("AGE", false, true, "age"),

	BIRTH_YEAR("BIRTH YEAR", false, "birthyear"),

	RELIGION("RELIGION", "religion"),

	RACE("RACE", "race", "ethnicity"),

	SEX("SEX", "sex", "gender"),

	LANG("LANGUAGE", "language", "primarylanguage"),

	MARITAL("MARITAL STATUS", "marital"),

	CAUSEOFDEATH("CAUSE OF DEATH", "causeofdeath"),

	VITALSTATUS("VITAL STATUS", "vitalstatus"),

	STATE("STATE", "state"),

	COUNTY("COUNTY", "county");

	// ========================= FIELDS ======================

	/**
	 * The display name when exported, used when generating the header
	 */
	private final String displayName;

	/**
	 * Source codes that represent this {@link DemographicsExportAttribute}
	 */
	private String[] sourceCodes = new String[0];

	/**
	 * Whether or not the attribute has a coded value
	 */
	private boolean valueCoded = true;

	/**
	 * Whether or not to ignore this attribute
	 */
	private boolean ignored = false;

	// ========================= CONSTRUCTORS ===================================

	/**
	 * Default constructor
	 * 
	 * @param displayName
	 *            the display name when exported
	 */
	private DemographicExportAttribute(final String displayName)
	{
		this.displayName = displayName;
	}

	/**
	 * Constructor
	 * 
	 * @param displayName
	 *            the display name when exported
	 * @param valueCoded
	 *            whether or not the value should have a coded value in addition to its
	 *            descriptive value.
	 */
	private DemographicExportAttribute(final String displayName, final boolean valueCoded)
	{
		this.displayName = displayName;
		this.valueCoded = valueCoded;
	}

	/**
	 * Constructor
	 * 
	 * @param displayName
	 *            the display name when exported
	 * @param sourceCodes
	 *            source "codes" that will match to this
	 *            {@link DemographicsExportAttribute}
	 */
	private DemographicExportAttribute(final String displayName,
			final String... sourceCodes)
	{
		this.displayName = displayName;
		this.sourceCodes = sourceCodes;
	}

	/**
	 * Constructor
	 * 
	 * @param displayName
	 *            the display name when exported
	 * @param valueCoded
	 *            whether or not the value should have a coded value in addition to its
	 *            descriptive value.
	 * @param sourceCodes
	 *            source "codes" that will match to this
	 *            {@link DemographicsExportAttribute}
	 */
	private DemographicExportAttribute(final String displayName,
			final boolean valueCoded, final String... sourceCodes)
	{
		this.displayName = displayName;
		this.valueCoded = valueCoded;
		this.sourceCodes = sourceCodes;
	}

	/**
	 * Constructor
	 * 
	 * @param displayName
	 *            the display name when exported
	 * @param valueCoded
	 *            whether or not the value should have a coded value in addition to its
	 *            descriptive value.
	 * @param sourceCodes
	 *            source "codes" that will match to this
	 *            {@link DemographicsExportAttribute}
	 */
	private DemographicExportAttribute(final String displayName,
			final boolean valueCoded, final boolean ignored, final String... sourceCodes)
	{
		this.displayName = displayName;
		this.valueCoded = valueCoded;
		this.ignored = ignored;
		this.sourceCodes = sourceCodes;
	}

	// ========================= METHODS ===================================

	/**
	 * Returns the {@link DemographicsExportAttribute} based on the source code
	 * 
	 * @param code
	 * @return
	 */
	public static DemographicExportAttribute getAttributeBySourceCode(final String code)
	{
		if (code == null || code.length() == 0)
		{
			throw new ApplicationException(
					"Can't determine demographic attribute from null or empty source code");
		}

		final String codeLower = code.toLowerCase();

		for (final DemographicExportAttribute attribute : values())
		{
			for (final String sourceCode : attribute.sourceCodes)
			{
				// include the dot so that E.g. 'age' in Language is not detected wrongly.
				if (codeLower.contains("." + sourceCode))
				{
					return attribute;
				}
			}
		}

		throw new ApplicationException(
				"Unable to determine Demographic attribute for source code " + code);

	}

	// ========================= GET/SET METHODS ===========================

	/**
	 * Return the displayName property.
	 * 
	 * @return the displayName
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * Return the valueCoded property.
	 * 
	 * @return the valueCoded
	 */
	public boolean isValueCoded()
	{
		return valueCoded;
	}

	/**
	 * Return the ignored property.
	 * 
	 * @return the ignored
	 */
	public boolean isIgnored()
	{
		return ignored;
	}

}
