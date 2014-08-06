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

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.fqe.ds.api.domain.AbstractQueryContext;
import edu.utah.further.fqe.ds.api.domain.ExportContext;
import edu.utah.further.fqe.ds.api.domain.Exporter;
import edu.utah.further.ds.i2b2.model.api.domain.ObservationFact;
import edu.utah.further.ds.i2b2.model.impl.domain.PatientDimensionEntity;

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

		// Handle PatientDimensionEntity results
		if (resultClazz.equals(PatientDimensionEntity.class))
		{
			// We've already checked the type
			final List<PatientDimensionEntity> patients = (List<PatientDimensionEntity>) results;
			final Map<String, String> nameMapper = getCodeToNameMap(patients);

			// Build the CSV header
			final StringBuilder sb = new StringBuilder();
			sb.append(Joiner.on(",").join(createPersonHeaderList())
					+ System.getProperty("line.separator"));

			// Build the CSV data
			for (final PatientDimensionEntity patient : patients)
			{
				sb.append(new PersonStringAdapter(patient, nameMapper)
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
	 * @param patients
	 * @return
	 */
	private Map<String, String> getCodeToNameMap(
			final List<PatientDimensionEntity> patients)
	{
		final Map<String, String> terminologyNameMap = CollectionUtil.newMap();
		Map<DtsNamespace, Set<String>> translationErrors = null;

		for (final PatientDimensionEntity patient : patients)
		{
			log.debug("Processing patient: " + patient.getId());

			for (final ObservationFact observation : patient.getObservations())
			{
				log.debug("Processing observation: " + observation.getId());

				// Concepts come in the form of namespace:code
				final String concept = observation.getId().getConceptCd();
				final String[] conceptCode = concept.split(":");
				// This is the namespace
				final String prefix = conceptCode[0];
				String code = conceptCode[1];

				// Handle codes like CR|XC368
				if (code.indexOf('|') != -1)
				{
					code = code.substring(code.indexOf('|') + 1);
				}

				// Find the namespace to use based on the prefix
				final Integer namespace = prefixMapper.get(prefix);
				
				log.debug("namespace is: " + namespace);

				if (namespace.intValue() == -1)
				{
					// The name is the code in these cases. e.g. LENGTHOFSTAY:1 or
					// DEM|AGE:2
					terminologyNameMap.put(concept, code);
				}
				else
				{
					// Lookup the name based on the namespace and code
					final DtsNamespace dtsNamespace = dos.findNamespaceById(namespace
							.intValue());

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

					// Put the <concept_cd,name> into the terminologyNameMap
					terminologyNameMap.put(concept, name);
				}
			}
		}

		return terminologyNameMap;
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
		 * The patient to adapt
		 */
		private final PatientDimensionEntity patient;

		/**
		 * Holds the mapping between the code of the value and the attribute
		 */
		private final Map<DemographicExportAttribute, AttributeValue> attributeValueMapper = CollectionUtil
				.newMap();

		/**
		 * Constructor
		 * 
		 * @param patient
		 */
		public PersonStringAdapter(final PatientDimensionEntity patient,
				final Map<String, String> nameMapper)
		{
			this.patient = patient;

			for (final ObservationFact observation : patient.getObservations())
			{
				final String source = observation.getSourcesystemCd();
				final DemographicExportAttribute attribute = DemographicExportAttribute
						.getAttributeBySourceCode(source);

				final String concept = observation.getId().getConceptCd();
				attributeValueMapper.put(attribute, new AttributeValue(concept,
						nameMapper.get(concept)));
			}
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
			// Always add the patient id as the first value
			values.add(patient.getId().toString());
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
					values.add(value.getName());

					if (exportAttribute.isValueCoded())
					{
						if ("".equals(value.getName())) {
							// FUR-2482 - replace codes that aren't in DTS with NOT_FOUND
							values.add(NOT_FOUND);
						} else {
							// FUR-2481 - replace colons with underscore
							values.add(value.getCode().replace(":", "_"));
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
