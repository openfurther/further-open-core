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
package edu.utah.further.fqe.ds.model.further.export;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.fqe.ds.api.domain.AbstractQueryContext;
import edu.utah.further.fqe.ds.api.domain.ExportContext;
import edu.utah.further.fqe.ds.api.domain.Exporter;
import edu.utah.further.ds.further.model.impl.domain.Observation;
import edu.utah.further.ds.further.model.impl.domain.Person;

/**
 * A comma separated value implementation of an {@link Exporter}
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Rich Hansen {@code <rich.hansen@utah.edu>}
 * @version Jul 31, 2014
 */
@Service("csvExporter")
@Transactional
public final class CsvExporterImpl implements Exporter
{
	
	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AbstractQueryContext.class);

	/**
	 * String used to designate that a particular code was not found in DTS 
	 */
	private static final String NOT_FOUND = "NOT_FOUND";

	/**
	 * Strings used to find Demographic map entries 
	 */
	public static final String GENDER_PERSON_SOURCE_CD = "gender"; 
	public static final String BIRTH_DATE_PERSON_SOURCE_CD = "birthdate"; 
	public static final String BIRTH_YEAR_PERSON_SOURCE_CD = "birthyear"; 
	public static final String BIRTH_MONTH_PERSON_SOURCE_CD = "birthmonth"; 
	public static final String BIRTH_DAY_PERSON_SOURCE_CD = "birthday"; 
	public static final String EDUCATION_PERSON_SOURCE_CD = "education"; 
	public static final String MULTI_BIRTH_IND_PERSON_SOURCE_CD = "multibirthind"; 
	public static final String MULTI_BIRTH_NUM_PERSON_SOURCE_CD = "multibirthnum"; 
	public static final String DEATH_DATE_PERSON_SOURCE_CD = "deathdate"; 
	public static final String DEATH_YEAR_PERSON_SOURCE_CD = "deathyear"; 
	public static final String PEDIGREE_PERSON_SOURCE_CD = "pedigree"; 
	public static final String ETHNICITY_PERSON_SOURCE_CD = "ethnicity"; 
	public static final String RACE_PERSON_SOURCE_CD = "race"; 
	public static final String RELIGION_PERSON_SOURCE_CD = "religion"; 
	public static final String PRIMARYLANGUAGE_PERSON_SOURCE_CD = "primarylanguage"; 
	public static final String MARITAL_PERSON_SOURCE_CD = "marital"; 
	public static final String CAUSEOFDEATH_PERSON_SOURCE_CD = "causeofdeath"; 
	public static final String VITALSTATUS_PERSON_SOURCE_CD = "vitalstatus"; 
	
	// ========================= DEPENDENCIES ==============================

	/**
	 * Terminology services
	 */
	@Autowired
	private DtsOperationService dos;

	/**
	 * A prefix to namespace mapper. Prefixes that do not require a namespace -1.
	 */
	@Resource(name = "prefixMapper")
	private Map<String, Integer> prefixMapper;

	// ========================= IMPLEMENTATION: Exporter =======

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.ds.api.domain.Exporter#format(java.util.List,
	 * edu.utah.further.fqe.ds.api.domain.ExportContext)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <F> F format(final List<?> results, final ExportContext exportContext)
	{
		if (results == null || results.size() == 0)
		{
			throw new ApplicationException(
					"No results found. Your query may have returned zero results. "
							+ "If you think this is an error, ensure that you are "
							+ "running not running a count only query.");
		}
		final Class<?> resultClazz = results.get(0).getClass();

		// Handle Person results
		if (resultClazz.equals(Person.class))
		{
			// We've already checked the type
			final List<Person> persons = (List<Person>) results;
			final Map<String, String> nameMapper = getCodeToNameMap(persons);

			// Build the CSV header
			final StringBuilder sb = new StringBuilder();
			sb.append(Joiner.on(",").join(createPersonHeaderList())
					+ System.getProperty("line.separator"));

			// Build the CSV data
			for (final Person person : persons)
			{
				sb.append(new PersonStringAdapter(person, nameMapper)
						+ System.getProperty("line.separator"));
			}

			log.debug("Header is: " + sb.toString());

			return (F) sb.toString();
		}
		
		// handle other result types here

		// blow up otherwise
		throw new ApplicationException("Unsupported result type: "
				+ resultClazz.getCanonicalName());
	}

	// ========================= GET/SET METHODS ===========================

	/**
	 * Return the prefixMapper property.
	 * 
	 * @return the prefixMapper
	 */
	public Map<String, Integer> getPrefixMapper()
	{
		return prefixMapper;
	}

	/**
	 * Set a new value for the prefixMapper property.
	 * 
	 * @param prefixMapper
	 *            the prefixMapper to set
	 */
	public void setPrefixMapper(final Map<String, Integer> prefixMapper)
	{
		this.prefixMapper = prefixMapper;
	}

	/**
	 * Return the dos property.
	 * 
	 * @return the dos
	 */
	public DtsOperationService getDos()
	{
		return dos;
	}

	/**
	 * Set a new value for the dos property.
	 * 
	 * @param dos
	 *            the dos to set
	 */
	public void setDos(final DtsOperationService dos)
	{
		this.dos = dos;
	}

	// ========================= PRIVATE METHODS/CLASSES ===========================

	/**
	 * Returns a map of the concept_cd to it's named value.
	 * 
	 * E.g. SNOMED:248152002 -> Female
	 * 
	 * @param persons
	 * @return
	 */
	private Map<String, String> getCodeToNameMap(
			final List<Person> persons)
	{
		final Map<String, String> terminologyNameMap = CollectionUtil.newMap();
		Map<DtsNamespace, Set<String>> translationErrors = null;
		DtsNamespace dtsNamespace = null;
		String code = null;
		
		for (final Person person : persons)
		{
			log.debug("Processing person: " + person.getId());

			// Lookup the Gender name
			if(person.getAdministrativeGenderNamespaceId() != null) 
			{
				dtsNamespace = dos.findNamespaceById(person.getAdministrativeGenderNamespaceId().intValue());
				code = person.getAdministrativeGender();
	
				codeToNameLookup(terminologyNameMap, translationErrors, dtsNamespace, person.getAdministrativeGenderNamespaceId().toString(), code);
			}

			// Lookup the Ethnicity name
			if(person.getEthnicityNamespaceId() != null) 
			{
				dtsNamespace = dos.findNamespaceById(person.getEthnicityNamespaceId().intValue());
				code = person.getEthnicity();

				codeToNameLookup(terminologyNameMap, translationErrors, dtsNamespace, person.getEthnicityNamespaceId().toString(), code);
			}
			
			// Lookup the Race name
			if(person.getRaceNamespaceId() != null) 
			{
				dtsNamespace = dos.findNamespaceById(person.getRaceNamespaceId().intValue());
				code = person.getRace();

				codeToNameLookup(terminologyNameMap, translationErrors, dtsNamespace, person.getRaceNamespaceId().toString(), code);
			}

			// Lookup the Religion name
			if(person.getReligionNamespaceId() != null) 
			{
				dtsNamespace = dos.findNamespaceById(person.getReligionNamespaceId().intValue());
				code = person.getReligion();

				codeToNameLookup(terminologyNameMap, translationErrors, dtsNamespace, person.getReligionNamespaceId().toString(), code);
			}

			// Lookup the PrimaryLanguage name
			if(person.getPrimaryLanguageNamespaceId() != null) 
			{
				dtsNamespace = dos.findNamespaceById(person.getPrimaryLanguageNamespaceId().intValue());
				code = person.getPrimaryLanguage();

				codeToNameLookup(terminologyNameMap, translationErrors, dtsNamespace, person.getPrimaryLanguageNamespaceId().toString(), code);
			}

			// Lookup the MaritalStatus name
			if(person.getMaritalStatusNamespaceId() != null) 
			{
				dtsNamespace = dos.findNamespaceById(person.getMaritalStatusNamespaceId().intValue());
				code = person.getMaritalStatus();

				codeToNameLookup(terminologyNameMap, translationErrors, dtsNamespace, person.getMaritalStatusNamespaceId().toString(), code);
			}

			// Lookup the CauseOfDeath name
			if(person.getCauseOfDeathNamespaceId() != null) 
			{
				dtsNamespace = dos.findNamespaceById(person.getCauseOfDeathNamespaceId().intValue());
				code = person.getCauseOfDeath();

				codeToNameLookup(terminologyNameMap, translationErrors, dtsNamespace, person.getCauseOfDeathNamespaceId().toString(), code);
			}

			// Lookup the VitalStatus name
			if(person.getVitalStatusNamespaceId() != null) 
			{
				dtsNamespace = dos.findNamespaceById(person.getVitalStatusNamespaceId().intValue());
				code = person.getVitalStatus();

				codeToNameLookup(terminologyNameMap, translationErrors, dtsNamespace, person.getVitalStatusNamespaceId().toString(), code);
			}
		}

		return terminologyNameMap;
	}

	/**
	 * Lookup logic supporting creation of map of the concept_cd to it's named value.
	 * 
	 * 
	 * @param terminologyNameMap
	 * @param translationErrors
	 * @param dtsNamespace
	 * @param code
	 * @return
	 */
	private void codeToNameLookup(
			final Map<String, String> terminologyNameMap,
			Map<DtsNamespace, Set<String>> translationErrors,
			final DtsNamespace dtsNamespace, final String namespaceId, final String code) {
		final DtsConcept dtsConcept = dtsNamespace.isLocal() ? dos
				.findConceptByLocalCode(dtsNamespace, code) : dos
				.findConceptByCodeInSource(dtsNamespace, code);

		String name = (dtsConcept == null) ? "" : dtsConcept.getName();

		// Replace all commas in names.
		name = name.replace(",", ";");

		// Keep track of all untranslated codes
		if (dtsConcept == null)
		{
			if (translationErrors == null)
			{
				translationErrors = CollectionUtil.newMap();
			}
			Set<String> untranslatedCodes = translationErrors
					.get(dtsNamespace);

			if (untranslatedCodes == null)
			{
				untranslatedCodes = CollectionUtil.newSet();
			}

			untranslatedCodes.add(code);
			translationErrors.put(dtsNamespace, untranslatedCodes);
		}

		// Put the <namespace id + concept_cd,name> into the terminologyNameMap
		terminologyNameMap.put(namespaceId + ":" + code, name);
	}

	/**
	 * Creates the list of headers (attribute names) to put at the top of the CSV
	 * 
	 * @return
	 */
	private List<String> createPersonHeaderList()
	{
		// Create the header
		final List<String> headerValues = CollectionUtil.newList();
		headerValues.add("PERSON NUM");
		for (final DemographicExportAttribute attribute : DemographicExportAttribute
				.values())
		{
			if (attribute.isIgnored()) {
				continue;
			}
			
			headerValues.add(attribute.getDisplayName());
			if (attribute.isValueCoded())
			{
				headerValues.add(attribute.getDisplayName() + " CODE");
			}
		}
		return headerValues;
	}

	/**
	 * This class maps observations to person attributes and provides a toString in a
	 * comma separated value format. In the i2b2 model, we chose to store all demographic
	 * data as observations, therefore the observations need to be mapped back to person
	 * attributes.
	 * <p>
	 * -----------------------------------------------------------------------------------
	 * <br>
	 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
	 * Contact: {@code <further@utah.edu>}<br>
	 * Biomedical Informatics, 26 South 2000 East<br>
	 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
	 * Day Phone: 1-801-581-4080<br>
	 * -----------------------------------------------------------------------------------
	 * 
	 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
	 * @version Oct 8, 2012
	 */
	private static final class PersonStringAdapter
	{
		/**
		 * The person to adapt
		 */
		private final Person person;

		/**
		 * Holds the mapping between the code of the value and the attribute
		 */
		private final Map<DemographicExportAttribute, AttributeValue> attributeValueMapper = CollectionUtil
				.newMap();

		/**
		 * Constructor
		 * 
		 * @param person
		 */
		public PersonStringAdapter(final Person person,
				final Map<String, String> nameMapper)
		{
			this.person = person;

			log.debug("Adapting person: " + person.getId());

			// Adapt the Gender 
			String source = GENDER_PERSON_SOURCE_CD; 
			DemographicExportAttribute attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			String concept = 
					(person.getAdministrativeGenderNamespaceId() == null ? "" : person.getAdministrativeGenderNamespaceId())
					+ ":" 
					+ (person.getAdministrativeGender() == null ? "" : person.getAdministrativeGender());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the Ethnicity 
			 source = ETHNICITY_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					(person.getEthnicityNamespaceId() == null ? "" : person.getEthnicityNamespaceId())
					+ ":" 
					+ (person.getEthnicity() == null ? "" : person.getEthnicity());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the DateOfBirth 
			 source = BIRTH_DATE_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					source
					+ ":" 
					+ (person.getDateOfBirth() == null ? "" : person.getDateOfBirth());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the BirthYear 
			 source = BIRTH_YEAR_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					source
					+ ":" 
					+ (person.getBirthYear() == null ? "" : person.getBirthYear());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the BirthMonth 
			 source = BIRTH_MONTH_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					source
					+ ":" 
					+ (person.getBirthMonth() == null ? "" : person.getBirthMonth());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the BirthDay 
			 source = BIRTH_DAY_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					source
					+ ":" 
					+ (person.getBirthDay() == null ? "" : person.getBirthDay());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the EducationLevel 
			 source = EDUCATION_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					source
					+ ":" 
					+ (person.getEducationLevel() == null ? "" : person.getEducationLevel());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the MultipleBirthIndicator 
			 source = MULTI_BIRTH_IND_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					source
					+ ":" 
					+ (person.getMultipleBirthIndicator() == null ? "" : person.getMultipleBirthIndicator());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the MultipleBirthIndicatorOrderNumber 
			 source = MULTI_BIRTH_NUM_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					source
					+ ":" 
					+ (person.getMultipleBirthIndicatorOrderNumber() == null ? "" : person.getMultipleBirthIndicatorOrderNumber());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the DateOfDeath 
			 source = DEATH_DATE_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					source
					+ ":" 
					+ (person.getDateOfDeath() == null ? "" : person.getDateOfDeath());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the DeathYear 
			 source = DEATH_YEAR_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					source
					+ ":" 
					+ (person.getDeathYear() == null ? "" : person.getDeathYear());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the PedigreeQuality 
			 source = PEDIGREE_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					source
					+ ":" 
					+ (person.getPedigreeQuality() == null ? "" : person.getPedigreeQuality());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the Race 
			 source = RACE_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					(person.getRaceNamespaceId() == null ? "" : person.getRaceNamespaceId())
					+ ":" 
					+ (person.getRace() == null ? "" : person.getRace());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the Religion 
			 source = RELIGION_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					(person.getReligionNamespaceId() == null ? "" : person.getReligionNamespaceId())
					+ ":" 
					+ (person.getReligion() == null ? "" : person.getReligion());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the PrimaryLanguage 
			 source = PRIMARYLANGUAGE_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					(person.getPrimaryLanguageNamespaceId() == null ? "" : person.getPrimaryLanguageNamespaceId())
					+ ":" 
					+ (person.getPrimaryLanguage() == null ? "" : person.getPrimaryLanguage());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the MaritalStatus 
			 source = MARITAL_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					(person.getMaritalStatusNamespaceId() == null ? "" : person.getMaritalStatusNamespaceId())
					+ ":" 
					+ (person.getMaritalStatus() == null ? "" : person.getMaritalStatus());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the CauseOfDeath 
			 source = CAUSEOFDEATH_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					(person.getCauseOfDeathNamespaceId() == null ? "" : person.getCauseOfDeathNamespaceId())
					+ ":" 
					+ (person.getCauseOfDeath() == null ? "" : person.getCauseOfDeath());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

			// Adapt the VitalStatus 
			 source = VITALSTATUS_PERSON_SOURCE_CD; 
			 attribute = DemographicExportAttribute
					.getAttributeBySourceCode(source);

			 concept = 
					(person.getVitalStatusNamespaceId() == null ? "" : person.getVitalStatusNamespaceId())
					+ ":" 
					+ (person.getVitalStatus() == null ? "" : person.getVitalStatus());
			attributeValueMapper.put(attribute, new AttributeValue(concept,
					nameMapper.get(concept)));

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			final List<String> values = CollectionUtil.newList();
			// Always add the person id as the first value
			values.add(person.getId().getId().toString());
			for (final DemographicExportAttribute exportAttribute : DemographicExportAttribute
					.values())
			{
				if (exportAttribute.isIgnored()) {
					continue;
				}
				
				final AttributeValue value = attributeValueMapper.get(exportAttribute);
				if (value == null)
				{
					// they don't have this export attribute
					values.add("");
					if (exportAttribute.isValueCoded())
					{
						values.add("");
					}
				}
				else
				{
					if(value.getName() != null)
					{
						if (exportAttribute.isValueCoded())
						{
							values.add(value.getName());
						} else {
							// FUR-2481 - replace colons with underscore
							if(value.getCode() != null) 
							{
								String newValue = value.getCode().replace(":", "_");
								values.add(newValue);
							} else {
								values.add("");
							}
						}
					} else {
						values.add("");
					}

					if (exportAttribute.isValueCoded())
					{
						if ("".equals(value.getName())) {
							// FUR-2482 - replace codes that aren't in DTS with NOT_FOUND
							values.add(NOT_FOUND);
						} else {
							// FUR-2481 - replace colons with underscore
							if(value.getCode() != null) 
							{
								String newValue = value.getCode().replace(":", "_");
								values.add(newValue);
							} else {
								values.add("");
							}
						}
					}
				}
			}
			return Joiner.on(",").join(values);
		}

	}

	/**
	 * Holds a given codes attribute key, name, and code
	 * <p>
	 * -----------------------------------------------------------------------------------
	 * <br>
	 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
	 * Contact: {@code <further@utah.edu>}<br>
	 * Biomedical Informatics, 26 South 2000 East<br>
	 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
	 * Day Phone: 1-801-581-4080<br>
	 * -----------------------------------------------------------------------------------
	 * 
	 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
	 * @version Oct 9, 2012
	 */
	private static final class AttributeValue
	{
		/**
		 * The name of this attribute
		 */
		private final String name;

		/**
		 * The code of this attribute
		 */
		private final String code;

		/**
		 * @param key
		 * @param name
		 */
		public AttributeValue(final String code, final String name)
		{
			super();
			this.code = code;
			this.name = name;
		}

		/**
		 * Return the code property.
		 * 
		 * @return the code
		 */
		public String getCode()
		{
			return code;
		}

		/**
		 * Return the name property.
		 * 
		 * @return the name
		 */
		public String getName()
		{
			return name;
		}

	}
}
