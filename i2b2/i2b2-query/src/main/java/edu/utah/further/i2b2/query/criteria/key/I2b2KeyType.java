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
package edu.utah.further.i2b2.query.criteria.key;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.KeyTypePattern.ALPHA_SPACE;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.KeyTypePattern.ANY;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.KeyTypePattern.BIOSPEC;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.KeyTypePattern.DEMOGRAPHICS;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.KeyTypePattern.DEMOGRAPHICS_HDG;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.KeyTypePattern.DEMOGRAPHICS_STATE;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.KeyTypePattern.DEMOGRAPHICS_ZIPCD;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.KeyTypePattern.ENCOUNTER;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.KeyTypePattern.SEPARATOR;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.KeyTypePattern.UCR;
import static org.apache.commons.lang.Validate.notNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.i2b2.query.model.I2b2Query;

/**
 * Represents an I2b2 key type for a given i2b2 query item key.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Aug 26, 2009
 */
public enum I2b2KeyType
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Represents a query who's key contains ICD9.
	 */
	// This has Diseases, Procedures, and Supplementary to avoid capturing ICD-O codes
	// which are in the same diagnosis tree unfortunately. It makes sense for the
	// researcher but not the code.
	ICD9("ICD-9", ANY + SEPARATOR + "ICD-9" + SEPARATOR
			+ "(Diseases|Procedures|Supplementary)" + ANY, false, "CPT", "LOINC",
			"ICD-10", "ICD-9", "NEONATAL", "APO ICD9", "APO ICD10", "OBS VAL",
			"NEO OBS VAL", "COMPLICATING", "BIOSPEC", "ICD-O", "SNOMED_PROC"),

	/**
	 * Represents a query who's key contains ICD10.
	 */
	ICD10("ICD-10", ANY + SEPARATOR + "ICD-10" + SEPARATOR + ANY, false, "CPT", "LOINC",
			"ICD-9", "ICD-10", "NEONATAL", "APO ICD9", "APO ICD10", "OBS VAL",
			"NEO OBS VAL", "COMPLICATING", "BIOSPEC", "ICD-O", "SNOMED_PROC"),

	/**
	 * Represents a query who's key contains ICD10.
	 */
	ICDO("ICD-O", ANY + SEPARATOR + "Oncology \\[ICD-O\\]" + SEPARATOR + ANY, false,
			"CPT", "LOINC", "ICD-9", "ICD-10", "NEONATAL", "APO ICD9", "APO ICD10",
			"OBS VAL", "NEO OBS VAL", "COMPLICATING", "BIOSPEC", "ICD-O", "SNOMED_PROC"),

	/**
	 * Represents a query who's key contains CPT
	 */
	CPT("CPT", ANY + SEPARATOR + "CPT" + SEPARATOR + ANY, false, "ICD-9", "ICD-10",
			"LOINC", "CPT", "NEONATAL", "APO ICD9", "APO ICD10", "OBS VAL",
			"NEO OBS VAL", "COMPLICATING", "BIOSPEC", "ICD-O", "SNOMED_PROC"),

	/**
	 * Represents a query who's key contains SNOMED procedures
	 */
	SNOMED_PROC("SNOMED_PROC", ANY + SEPARATOR + "SNOMED_Procedure" + SEPARATOR + ANY,
			false, "ICD-9", "ICD-10", "LOINC", "CPT", "NEONATAL", "APO ICD9",
			"APO ICD10", "OBS VAL", "NEO OBS VAL", "COMPLICATING", "BIOSPEC", "ICD-O",
			"SNOMED_PROC"),

	/**
	 * Represents a query who's key contains LOINC
	 */
	LOINC("LOINC", ANY + SEPARATOR + "Labtests" + SEPARATOR + ANY, false, "ICD-9",
			"ICD-10", "CPT", "LOINC", "NEONATAL", "APO ICD9", "APO ICD10", "OBS VAL",
			"NEO OBS VAL", "COMPLICATING", "BIOSPEC", "ICD-O", "SNOMED_PROC"),

	MULTUMDRUG("MULTUM", "MultumDrug:" + ANY + SEPARATOR + "Medication Order" + SEPARATOR + ANY, false),
	
	/**
	 * Represents a query who's key contains Medication Order
	 */
	RXNORM("RXNORM", ANY + SEPARATOR + "Medication Order" + SEPARATOR + ANY, false),
	
	
	/**
	 * Utah cancer registry In-Situ codes
	 */
	ONCOLOGY_INSITU("ONCOLOGY_INSITU", ANY + UCR + "In-situ" + ANY, false),

	/**
	 * Utah cancer registry Malignant codes
	 */
	ONCOLOGY_MALIG("ONCOLOGY_MALIG", ANY + UCR + "Malignant" + ANY, false),

	/**
	 * Represents a query who's key contains demographics and gender
	 */
	DEM_GENDER("GENDER", ANY + DEMOGRAPHICS + "Gender" + SEPARATOR + ANY, true),

	/**
	 * Represents a query who's key contains demographics and language
	 */
	DEM_LANG("LANGUAGE", ANY + DEMOGRAPHICS + "Language" + SEPARATOR + ANY, true),

	/**
	 * Represents a query who's key contains demographics and race
	 */
	DEM_RACE("RACE", ANY + DEMOGRAPHICS + "Race" + SEPARATOR + ANY, true),

	/**
	 * Represents a query who's key contains demographics and marital status
	 */
	DEM_MARITAL_STATUS("MARITAL STATUS", ANY + DEMOGRAPHICS + "Marital Status"
			+ SEPARATOR + ANY, true),

	/**
	 * Represents a query who's key contains demographics and religion
	 */
	DEM_RELIGION("RELIGION", ANY + DEMOGRAPHICS + "Religion" + SEPARATOR + ANY, true),

	/**
	 * Represents a query who's key contains demographics and age
	 */
	DEM_AGE("AGE", ANY + DEMOGRAPHICS + "Age" + SEPARATOR + ANY, true, true),

	/**
	 * Represents a query who's key contains demographics and birth year
	 */
	DEM_BIRTH_YEAR("BIRTH_YEAR", ANY + DEMOGRAPHICS + "Birth Year" + SEPARATOR + ANY,
			true, true),

	/**
	 * Represents a query who's key contains demographics and vital status
	 */
	DEM_VITAL_STATUS("VITAL_STATUS", ANY + DEMOGRAPHICS + "Vital Status" + SEPARATOR
			+ ANY, true),

	/**
	 * Represents a query who's key contains demographics and pedigree quality
	 */
	DEM_PEDIGREE_QUALITY("PEDIGREE QUALITY", ANY + DEMOGRAPHICS + "Pedigree Quality"
			+ SEPARATOR + ANY, true),

	/**
	 * Represents a query who's key contains Birthdate
	 */
	DEM_BIRTHDATE("Birthdate", ANY + DEMOGRAPHICS + "Birthdate" + SEPARATOR + ANY, true),

	/**
	 * Represents a query who's key contains Ethnicity
	 */
	DEM_ETHNICITY("ETHNICITY", ANY + DEMOGRAPHICS + "Ethnicity" + SEPARATOR + ANY, true),

	/**
	 * Represents a query who's key contains demographics and state location
	 */
	DEM_LOCATION_STATE("STATE", DEMOGRAPHICS_STATE, true),

	/**
	 * Represents a query who's key contains demographics and health district group
	 * location
	 */
	DEM_LOCATION_HDG("HEALTH DISTRICT GROUP", DEMOGRAPHICS_HDG, true),

	/**
	 * Represents a query who's key contains demographics and health district group
	 * location
	 */
	DEM_LOCATION_ZIPCD("ZIP CODE", DEMOGRAPHICS_ZIPCD, true),
	
	/**
	 * Represents a query who's key contains demographics and county location
	 */
	DEM_LOCATION_COUNTY("COUNTY", DEMOGRAPHICS_HDG + ALPHA_SPACE + SEPARATOR, true),

	/**
	 * Represents a query who's key contains encounter and admit source
	 */
	ENC_ADMIT_SRC("ADMIT SOURCE", ANY + ENCOUNTER + "Admit Source" + SEPARATOR + ANY,
			false),

	/**
	 * Represents a query who's key contains encounter and length of stay
	 */
	ENC_LNGTH_STAY("LENGTH OF STAY",
			ANY + ENCOUNTER + "Length Of Stay" + SEPARATOR + ANY, false, true),

	/**
	 * Represents a query who's key contains encounter and patient class
	 */
	ENC_PAT_CLASS("PATIENT CLASS", ANY + ENCOUNTER + "Patient Class" + SEPARATOR + ANY,
			false),

	/**
	 * Represents a query who's key contains encounter and patient type
	 */
	ENC_PAT_TYPE("PATIENT TYPE", ANY + ENCOUNTER + "Patient Type" + SEPARATOR + ANY,
			false),

	/**
	 * Represents a query who's key contains encounter and admit type
	 */
	ENC_ADMIT_TYPE("ADMIT TYPE", ANY + ENCOUNTER + "Admit Type" + SEPARATOR + ANY, false),

	/**
	 * Represents a query who's key contains biospecimen and sample
	 */
	BIO_SPECIMEN("BIOSPEC", ANY + BIOSPEC + ANY, false, "ICD-9", "ICD-10", "CPT",
			"LOINC", "NEONATAL", "APO ICD9", "APO ICD10", "OBS VAL", "NEO OBS VAL",
			"COMPLICATING", "BIOSPEC", "ICD-O", "SNOMED_PROC"),

	/**
	 * Default value when a key type cannot be determined.
	 */
	UNKNOWN(Strings.EMPTY_STRING, "", false);

	// ========================= FIELDS ======================

	/**
	 * The constant which represents the key
	 */
	private final String keyConstant;

	/**
	 * The constant which represents the key
	 */
	private final String pattern;

	/**
	 * Whether or not this key is a demographic key as these are handled differently
	 */
	private final boolean isDemographic;

	/**
	 * Whether or not this key is to be treated as a grouped criteria
	 */
	private final boolean isGroupedCriteria;

	/**
	 * An array of string const keys which conflict with this key. A conflict occurs in
	 * the FURTHeR model due to the way Observations are stored. A single column is used
	 * for all labs, procedures, and diagnoses codes and they are differentiated by
	 * another 'type' column. Since a type cannot be two different values at once, we
	 * declare other types as conflicts.
	 */
	private final String[] conflicts;

	/**
	 * A list of keys which conflict with this key
	 */
	private List<I2b2KeyType> conflictsList;

	// ========================= CONSTRUCTORS ===================================

	/**
	 * Non grouped criteria constructor
	 * 
	 * @param keyConstant
	 * @param isDemographic
	 */
	private I2b2KeyType(final String keyConstant, final String pattern,
			final boolean isDemographic, final String... conflicts)
	{
		this.keyConstant = keyConstant;
		this.pattern = pattern;
		this.isDemographic = isDemographic;
		this.isGroupedCriteria = false;
		this.conflicts = conflicts;
	}

	/**
	 * Grouped criteria constructor
	 * 
	 * @param keyConstant
	 * @param isDemographic
	 */
	private I2b2KeyType(final String keyConstant, final String pattern,
			final boolean isDemographic, final boolean isGroupedCriteria,
			final String... conflicts)
	{
		this.keyConstant = keyConstant;
		this.pattern = pattern;
		this.isDemographic = isDemographic;
		this.isGroupedCriteria = isGroupedCriteria;
		this.conflicts = conflicts;
	}

	// ========================= METHODS ===================================

	/**
	 * @return the keyConstant
	 */
	public String getKeyConstant()
	{
		return keyConstant;
	}

	/**
	 * @return the isDemographic
	 */
	public boolean isDemographic()
	{
		return isDemographic;
	}

	/**
	 * Return the isGroupedCriteria property.
	 * 
	 * @return the isGroupedCriteria
	 */
	public boolean isGroupedCriteria()
	{
		return isGroupedCriteria;
	}

	/**
	 * Return the pattern property.
	 * 
	 * @return the pattern
	 */
	public String getPattern()
	{
		return pattern;
	}

	/**
	 * Return the conflicts property.
	 * 
	 * @return the conflicts
	 */
	public List<I2b2KeyType> getConflicts()
	{
		if (conflictsList == null)
		{
			conflictsList = newList();

			for (final String conflict : conflicts)
			{
				conflictsList.add(fromKey(conflict));
			}
		}

		return conflictsList;
	}

	/**
	 * Return the given {@link I2b2KeyType} for a given item key.
	 * 
	 * @param keyString
	 *            the key string (c_fullname) of an {@link I2b2Query}
	 * @return the key type represented by the item key
	 */
	public static final I2b2KeyType fromPattern(final String keyString)
	{
		notNull(keyString);
		for (final I2b2KeyType i2b2KeyType : I2b2KeyType.values())
		{
			final Pattern pattern = Pattern.compile(i2b2KeyType.getPattern());
			final Matcher matcher = pattern.matcher(keyString);
			if (matcher.matches())
			{
				return i2b2KeyType;
			}
		}

		return UNKNOWN;
	}

	/**
	 * Return the given {@link I2b2KeyType} for a given key constant.
	 * 
	 * @param keyConstant
	 *            the key constant as defined by {@link #keyConstant}
	 * @return the key type represented by the item key
	 */
	public static final I2b2KeyType fromKey(final String keyConstant)
	{
		notNull(keyConstant);
		final String keyConstUpper = keyConstant.toUpperCase();
		for (final I2b2KeyType i2b2KeyType : I2b2KeyType.values())
		{
			if (keyConstUpper.contains(i2b2KeyType.getKeyConstant()))
			{
				return i2b2KeyType;
			}
		}

		return UNKNOWN;
	}

	/**
	 * An inner class for constant key type patterns. Public instead of private for use in
	 * static imports.
	 * <p>
	 * -----------------------------------------------------------------------------------
	 * <br>
	 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
	 * Contact: {@code <further@utah.edu>}<br>
	 * Biomedical Informatics, 26 South 2000 East<br>
	 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
	 * Day Phone: 1-801-581-4080<br>
	 * -----------------------------------------------------------------------------------
	 * 
	 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
	 * @version Aug 6, 2010
	 */
	public static final class KeyTypePattern
	{
		/**
		 * Matches anything, at least 1
		 */
		public static final String ANY = "(.)+";

		/**
		 * Alphabetical characters, all cases, at least 1
		 */
		public static final String ALPHA_SPACE = "([A-Za-z\\s])+";
		
		/**
		 * Numeric characters
		 */
		public static final String NUMERIC_SPACE = "([0-9]{5})";

		/**
		 * A quoted file separator pattern
		 */
		public static final String SEPARATOR = "\\\\";

		/**
		 * General demographics pattern (/Demographics/)
		 */
		public static final String DEMOGRAPHICS = SEPARATOR + "Demographics" + SEPARATOR;

		/**
		 * Encounters pattern (/Encounter/)
		 */
		public static final String ENCOUNTER = SEPARATOR + "Encounter" + SEPARATOR;

		/**
		 * APO pattern (/Adverse Pregnancy Outcome/)
		 */
		public static final String APO = SEPARATOR + "Adverse Pregnancy Outcomes"
				+ SEPARATOR;

		/**
		 * UCR pattern
		 */
		public static final String UCR = SEPARATOR + "Utah Cancer Registry" + SEPARATOR;

		/**
		 * APO pattern (/Adverse Pregnancy Outcome/Observation/)
		 */
		public static final String APO_OBSERVATION = APO + "Observation" + SEPARATOR;

		/**
		 * Biological Specimen pattern (/Biological Specimen/)
		 */
		public static final String BIOSPEC = APO + "Biological Specimen" + SEPARATOR;

		/**
		 * General demographics location pattern (/Demographics/Geographic Location/[type
		 * of location]/United States/
		 */
		private static final String DEMOGRAPHICS_LOC = ANY + DEMOGRAPHICS
				+ "Geographic Location" + SEPARATOR + ANY + SEPARATOR + "United States"
				+ SEPARATOR;

		/**
		 * State Pattern
		 */
		public static final String DEMOGRAPHICS_STATE = DEMOGRAPHICS_LOC + ALPHA_SPACE
				+ SEPARATOR;

		/**
		 * Health District Group Pattern
		 */
		public static final String DEMOGRAPHICS_HDG = DEMOGRAPHICS_STATE
				+ "Health District Group" + SEPARATOR + ALPHA_SPACE + SEPARATOR;

		/**
		 * Health District Group Pattern
		 */
		public static final String DEMOGRAPHICS_ZIPCD = DEMOGRAPHICS_STATE
				+ "Zip Code" + SEPARATOR + NUMERIC_SPACE + SEPARATOR;
		
	}
}
