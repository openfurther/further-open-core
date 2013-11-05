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

import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.fromPattern;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import edu.utah.further.i2b2.query.fixture.I2b2QueryFixture;

/**
 * Unit test for {@link I2b2KeyType} methods
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
 * @version Aug 6, 2010
 */
public final class UTestI2b2KeyType extends I2b2QueryFixture
{

	private static final String ICD9 = "\\\\FURTHER\\FURTHER\\ICD-9\\"
			+ "Diseases and injuries (001-999.99)\\Complications of pregnancy, childbirth, and the puerperium (630-679.99)\\"
			+ "Complications mainly related to pregnancy (640-649.99)\\"
			+ "Hypertension complicating pregnancy, childbirth, and the puerperium (642)\\"
			+ "Eclampsia complicating pregnancy, childbirth or the puerperium (642.6)\\";

	private static final String ICD10 = "\\\\FURTHER\\FURTHER\\ICD-10\\Neoplasms [C00-D48.9]\\"
			+ "Benign neoplasms [D10-D36.9]\\Benign neoplasm of ovary [D27]\\";

	private static final String ICD9_IN_STR = "Something with ICD-9 in the middle of the string";

	private static final String CPT = "\\\\FURTHER\\FURTHER\\CPT\\Anesthesia Procedures (Level 1: 00100-01999) \\"
			+ "Anesthesia for Burn Excisions or Debridement (Level 2: 01951-01953)\\"
			+ "Anesthesia for second- and third-degree burn excision or debridement with or without skin grafting, "
			+ "any site, for total body surface area (TBSA) treated during anesthesia and surgery; between 4% and 9% "
			+ "of total body surface area (01952)\\";

	private static final String SNOMED_PROC = "\\\\FURTHER\\FURTHER\\SNOMED_Procedure\\Procedure (procedure)\\"
			+ "Procedure by site (procedure)\\Procedure on body part (procedure)\\Procedure on trunk (procedure)\\"
			+ "Procedure on abdomen (procedure)\\Abdomen excision (procedure)\\Prostatectomy (procedure)\\";

	private static final String RXNORM = "\\\\FURTHER\\FURTHER\\Medication Order\\"
			+ "alternative medicines  [drug category]\\herbal products  [drug category]\\";

	private static final String LOINC = "\\\\FURTHER\\FURTHER\\"
			+ "Labtests\\Chemistry\\Anemia Related Studies\\LOINC:20567-4\\";

	private static final String AGE = "\\\\FURTHER\\FURTHER\\Demographics\\Age\\0-9 years old\\";

	private static final String BIRTH_YEAR = "\\\\FURTHER\\FURTHER\\Demographics\\Birth Year\\1980-1989\\";

	private static final String GENDER = "\\\\FURTHER\\FURTHER\\Demographics\\Gender\\Female\\";

	private static final String LANGUAGE = "\\\\FURTHER\\FURTHER\\Demographics\\Language\\Arabic\\";

	private static final String MARITAL_STATUS = "\\\\FURTHER\\FURTHER\\Demographics\\Marital Status\\Divorced\\";

	private static final String RACE = "\\\\FURTHER\\FURTHER\\Demographics\\Race\\Hispanic\\";

	private static final String RELIGION = "\\\\FURTHER\\FURTHER\\Demographics\\Religion\\Atheist\\";

	private static final String VITAL_STATUS = "\\\\FURTHER\\FURTHER\\Demographics\\Vital Status\\Deceased\\";

	private static final String PEDIGREE_QUALITY = "\\\\FURTHER\\FURTHER\\Demographics\\Pedigree Quality\\Pedigree Quality 1\\";

	private static final String STATE = "\\\\FURTHER_DEV\\FURTHER-DEV\\Demographics\\"
			+ "Geographic Location\\Current Location\\United States\\Alabama\\";

	private static final String HDG = "\\\\FURTHER_DEV\\FURTHER-DEV\\Demographics\\"
			+ "Geographic Location\\Current Location\\United States\\Utah\\Health District Group\\"
			+ "Bear River Health District Group\\";

	private static final String COUNTY = "\\\\FURTHER_DEV\\FURTHER-DEV\\Demographics\\"
			+ "Geographic Location\\Current Location\\United States\\Utah\\Health District Group\\"
			+ "Central Utah Health District Group\\Juab County\\";

	@Test
	public void keyTypeFromPattern()
	{
		// Positive assertions
		assertThat(fromPattern(ICD9), is(I2b2KeyType.ICD9));
		assertThat(fromPattern(ICD10), is(I2b2KeyType.ICD10));
		assertThat(fromPattern(CPT), is(I2b2KeyType.CPT));
		assertThat(fromPattern(SNOMED_PROC), is(I2b2KeyType.SNOMED_PROC));
		assertThat(fromPattern(LOINC), is(I2b2KeyType.LOINC));
		assertThat(fromPattern(RXNORM), is(I2b2KeyType.RXNORM));
		assertThat(fromPattern(AGE), is(I2b2KeyType.DEM_AGE));
		assertThat(fromPattern(BIRTH_YEAR), is(I2b2KeyType.DEM_BIRTH_YEAR));
		assertThat(fromPattern(GENDER), is(I2b2KeyType.DEM_GENDER));
		assertThat(fromPattern(LANGUAGE), is(I2b2KeyType.DEM_LANG));
		assertThat(fromPattern(MARITAL_STATUS), is(I2b2KeyType.DEM_MARITAL_STATUS));
		assertThat(fromPattern(RACE), is(I2b2KeyType.DEM_RACE));
		assertThat(fromPattern(RELIGION), is(I2b2KeyType.DEM_RELIGION));
		assertThat(fromPattern(VITAL_STATUS), is(I2b2KeyType.DEM_VITAL_STATUS));
		assertThat(fromPattern(PEDIGREE_QUALITY), is(I2b2KeyType.DEM_PEDIGREE_QUALITY));
		assertThat(fromPattern(STATE), is(I2b2KeyType.DEM_LOCATION_STATE));
		assertThat(fromPattern(HDG), is(I2b2KeyType.DEM_LOCATION_HDG));
		assertThat(fromPattern(COUNTY), is(I2b2KeyType.DEM_LOCATION_COUNTY));

		// Negative assertions
		assertThat(fromPattern(ICD9_IN_STR), is(not(I2b2KeyType.ICD9)));
		assertThat(fromPattern(HDG), is(not(I2b2KeyType.DEM_LOCATION_STATE)));
		assertThat(fromPattern(COUNTY), is(not(I2b2KeyType.DEM_LOCATION_HDG)));
	}
}
