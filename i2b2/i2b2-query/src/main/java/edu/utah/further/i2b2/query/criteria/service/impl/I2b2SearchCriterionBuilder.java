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
package edu.utah.further.i2b2.query.criteria.service.impl;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.collections.CollectionUtil.newSet;
import static edu.utah.further.core.query.domain.Relation.EQ;
import static edu.utah.further.core.query.domain.Relation.GE;
import static edu.utah.further.core.query.domain.Relation.GT;
import static edu.utah.further.core.query.domain.SearchCriteria.addSimpleExpression;
import static edu.utah.further.core.query.domain.SearchCriteria.junction;
import static edu.utah.further.core.query.domain.SearchCriteria.simpleExpression;
import static edu.utah.further.i2b2.query.criteria.key.PersonLocationType.fromCode;
import static org.apache.commons.lang.Validate.notNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import com.google.common.collect.Lists;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.api.scope.Namespace;
import edu.utah.further.core.api.scope.NamespaceService;
import edu.utah.further.core.api.scope.Namespaces;
import edu.utah.further.core.query.domain.Relation;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQueryAlias;
import edu.utah.further.core.query.domain.SearchQueryAliasTo;
import edu.utah.further.core.query.domain.SearchType;
import edu.utah.further.i2b2.query.criteria.key.I2b2KeyType;
import edu.utah.further.i2b2.query.model.I2b2QueryDateConstraint;
import edu.utah.further.i2b2.query.model.I2b2QueryValueConstraint;
import edu.utah.further.i2b2.query.model.I2b2ValueOperator;

/**
 * Constructs a {@link SearchCriterion} based the {@link I2b2KeyType}. Each
 * {@link I2b2KeyType} has a different strategy for how it handles the domain.
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
final class I2b2SearchCriterionBuilder implements Builder<SearchCriterion>
{
	// ========================= CONSTANTS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(I2b2SearchCriterionBuilder.class);

	/**
	 * Constant namespace identifier for Utah Population Database.
	 */
	private static final String UPDBL = "UPDBL";

	/**
	 * The path to the collection of ObservationEntity's in PersonEntity in fqe-model
	 */
	private static final String OBSERVATION_PATH = "observations";

	/**
	 * The association path object type
	 */
	private static final String OBSERVATION_ASSOC_OBJ = "Observation";

	/**
	 * The path to the collection of LocationEntity's in PersonEntity in fqe-model
	 */
	private static final String LOCATIONS_PATH = "locations";

	/**
	 * The association path object type
	 */
	private static final String LOCATIONS_ASSOC_OBJ = "Location";

	/**
	 * The path to the collection of OrderEntity's in PersonEntity in fqe-model
	 */
	private static final String ORDERS_PATH = "orders";

	/**
	 * The association path object type
	 */
	private static final String ORDERS_ASSOC_OBJ = "Order";

	/**
	 * The path to the collection of EncounterEntity's in PersonEntity in fqe-model
	 */
	private static final String ENCOUNTERS_PATH = "encounters";

	/**
	 * The association path object type
	 */
	private static final String ENCOUNTERS_ASSOC_OBJ = "Encounter";

	/**
	 * Dot Char
	 */
	private static final String DOT = ".";

	// ========================= FIELDS ====================================

	/**
	 * The key type for which to build the search criterion
	 */
	private final I2b2KeyType keyType;

	/**
	 * The domain for which to construct this query, this is basically all the concept's
	 * children
	 */
	private List<String> domain;

	/**
	 * The value constraints of this {@link SearchCriterion}
	 */
	private I2b2QueryValueConstraint valueConstraint;

	/**
	 * The date constraints of this {@link SearchCriterion}
	 */
	private I2b2QueryDateConstraint dateConstraint;

	/**
	 * A map of alias definitions.
	 */
	private final Set<SearchQueryAlias> aliases = newSet();

	/**
	 * The alias to use for Observations
	 */
	private String observationAlias;

	/**
	 * The alias to use for Locations
	 */
	private String locationAlias;

	/**
	 * The alias to use for Orders
	 */
	private String orderAlias;

	/**
	 * The alias to user for Encounters
	 */
	private String encounterAlias;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param keyType
	 */
	public I2b2SearchCriterionBuilder(final I2b2KeyType keyType)
	{
		super();
		this.keyType = keyType;
	}

	// ========================= IMPLEMENTATION: Builder<SearchCriterion> ==

	/**
	 * Constructs an I2b2 equivalent {@link SearchCriterion} based on the
	 * {@link I2b2KeyType}
	 * 
	 * @return
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public SearchCriterion build()
	{
		// Could change this to a visitor but switch will do for now. Consider changing it
		// to visitor if KeyTypes become very large.

		if (keyType != I2b2KeyType.UNKNOWN && domain.size() == 0)
		{
			throw new ApplicationException("The domain of codes for key " + keyType
					+ " was empty. Unable to create conversion for this query. "
					+ "Check that there are no extra spaces in the c_fullname attribute "
					+ "and ensure that the c_basecode is non-null.");
		}

		switch (keyType)
		{
			case ICD9:
			{
				Validate.notNull(observationAlias,
						"Observation Alias Required to build ICD9");
				return createPhrase(Namespaces.ICD_9_CM, observationAlias,
						"observationNamespaceId", "observationType", "439401001",
						"observation");
			}
			case ICD10:
			{
				Validate.notNull(observationAlias,
						"Observation Alias Required to build ICD10");
				return createPhrase(Namespaces.ICD_10, observationAlias,
						"observationNamespaceId", "observationType", "439401001",
						"observation");
			}
			case ICDO:
			{
				Validate.notNull(observationAlias,
						"Observation Alias Required to build ICDO");
				return createPhrase(Namespaces.ICD_O, observationAlias,
						"observationNamespaceId", "observationType", "439401001",
						"observation");
			}
			case CPT:
			{
				Validate.notNull(observationAlias,
						"Observation Alias Required to build CPT");
				return createPhrase(Namespaces.CPT_4, observationAlias,
						"observationNamespaceId", "observationType", "71388002",
						"observation");
			}
			case SNOMED_PROC:
			{
				Validate.notNull(observationAlias,
						"Observation Alias Required to build CPT");
				return createPhrase(Namespaces.SNOMED_CT, observationAlias,
						"observationNamespaceId", "observationType", "71388002",
						"observation");
			}
			case LOINC:
			{
				Validate.notNull(observationAlias,
						"Observation Alias Required to build LOINC");
				return createPhrase(Namespaces.LOINC, observationAlias,
						"observationNamespaceId", "observationType", "364712009",
						"observation");
			}
			case RXNORM:
			{
				Validate.notNull(orderAlias, "Order Alias Required to build RXNORM");
				return createPhrase(Namespaces.RXNORM, orderAlias,
						"orderItemNamespaceId", "type", "Medication", "orderItem");
			}
			case ONCOLOGY_INSITU:
			{
				Validate.notNull(observationAlias,
						"Observation Alias Required to build Oncology In-Situ");
				return createPhrase(Namespaces.namespaceFor(UPDBL), observationAlias,
						"observationNamespaceId", "observationType", "439401001",
						"observation", "observationMod", "2",
						"observationModNamespaceId", Namespaces.namespaceFor(UPDBL));
			}
			case ONCOLOGY_MALIG:
			{
				Validate.notNull(observationAlias,
						"Observation Alias Required to build Oncology In-Situ");
				return createPhrase(Namespaces.namespaceFor(UPDBL), observationAlias,
						"observationNamespaceId", "observationType", "439401001",
						"observation", "observationMod", "3",
						"observationModNamespaceId", Namespaces.namespaceFor(UPDBL));
			}
			case DEM_GENDER:
			case DEM_LANG:
			case DEM_MARITAL_STATUS:
			case DEM_RACE:
			case DEM_RELIGION:
			case DEM_VITAL_STATUS:
			case DEM_PEDIGREE_QUALITY:
			case DEM_AGE:
			case DEM_BIRTH_YEAR:
			case DEM_BIRTHDATE:
			case DEM_LOCATION_STATE:
			case DEM_LOCATION_HDG:
			case DEM_LOCATION_COUNTY:
			case DEM_ETHNICITY:
			{
				return buildDemographics();
			}
			case ENC_ADMIT_SRC:
			case ENC_ADMIT_TYPE:
			case ENC_LNGTH_STAY:
			case ENC_PAT_TYPE:
			case ENC_PAT_CLASS:
			{
				return buildEncounters();
			}
			case BIO_SPECIMEN:
			{
				Validate.notNull(observationAlias,
						"Observation Alias Required to build BIOSPECIMEN");
				return createPhrase(Namespaces.SNOMED_CT, observationAlias,
						"observationNamespaceId", "observationType", "Specimen",
						"observation");
			}
			case UNKNOWN:
			{
				log.warn("I2b2KeyType UNKNOWN, this search parameter will be ignored");
				return null;
			}
			default:
			{
				log.warn("I2b2KeyType UNKNOWN, this search parameter will be ignored");
				return null;
			}
		}
	}

	public Set<SearchQueryAlias> buildAliases()
	{
		switch (keyType)
		{
			case ICD10:
			case ICD9:
			case ICDO:
			case CPT:
			case SNOMED_PROC:
			case LOINC:
			case BIO_SPECIMEN:
			case ONCOLOGY_INSITU:
			case ONCOLOGY_MALIG:
			{
				addAlias(OBSERVATION_ASSOC_OBJ, observationAlias, OBSERVATION_PATH);
				break;
			}
			case RXNORM:
			{
				addAlias(ORDERS_ASSOC_OBJ, orderAlias, ORDERS_PATH);
				break;
			}
			case ENC_PAT_CLASS:
			case ENC_ADMIT_SRC:
			case ENC_ADMIT_TYPE:
			case ENC_LNGTH_STAY:
			case ENC_PAT_TYPE:
			{
				addAlias(ENCOUNTERS_ASSOC_OBJ, encounterAlias, ENCOUNTERS_PATH);
				break;
			}
			case DEM_GENDER:
				break;
			case DEM_LANG:
				break;
			case DEM_MARITAL_STATUS:
				break;
			case DEM_RACE:
				break;
			case DEM_RELIGION:
				break;
			case DEM_VITAL_STATUS:
				break;
			case DEM_PEDIGREE_QUALITY:
				break;
			case DEM_AGE:
				break;
			case DEM_BIRTHDATE:
				break;
			case DEM_BIRTH_YEAR:
				break;
			case DEM_ETHNICITY:
				break;
			case DEM_LOCATION_STATE:
			case DEM_LOCATION_HDG:
			case DEM_LOCATION_COUNTY:
			{
				addAlias(LOCATIONS_ASSOC_OBJ, locationAlias, LOCATIONS_PATH);
				break;
			}
			case UNKNOWN:
				break;
			default:
				break;
		}

		return aliases;
	}

	// ========================= METHODS ===================================

	/**
	 * Sets the domain of this {@link SearchCriterion}
	 * 
	 * @param domain
	 * @return this for chaining
	 */
	public I2b2SearchCriterionBuilder setDomain(final List<String> domain)
	{
		this.domain = domain;
		return this;
	}

	/**
	 * Set a new value for the valueConstraint property.
	 * 
	 * @param valueConstraint
	 *            the valueConstraint to set
	 * @return this for chaining
	 */
	public I2b2SearchCriterionBuilder setValueConstraint(
			final I2b2QueryValueConstraint valueConstraint)
	{
		this.valueConstraint = valueConstraint;
		return this;
	}

	/**
	 * Set a new value for the dateConstraint property.
	 * 
	 * @param dateConstraint
	 *            the dateConstraint to set
	 * @return this for chaining
	 */
	public I2b2SearchCriterionBuilder setDateConstraint(
			final I2b2QueryDateConstraint dateConstraint)
	{
		this.dateConstraint = dateConstraint;
		return this;
	}

	/**
	 * Set a new value for the valueConstraint property.
	 * 
	 * @param obsAliasGenerator
	 * @return this for chaining
	 */
	public I2b2SearchCriterionBuilder setObservationAlias(final String observationAlias)
	{
		this.observationAlias = observationAlias;
		return this;
	}

	/**
	 * Set a new value for the locationAlias property.
	 * 
	 * @param locationAlias
	 *            the locationAlias to set
	 */
	public I2b2SearchCriterionBuilder setLocationAlias(final String locationAlias)
	{
		this.locationAlias = locationAlias;
		return this;
	}

	/**
	 * Set a new value for the orderAlias property.
	 * 
	 * @param orderAlias
	 *            the orderAlias to set
	 */
	public I2b2SearchCriterionBuilder setOrderAlias(final String orderAlias)
	{
		this.orderAlias = orderAlias;
		return this;
	}

	/**
	 * Set a new value for the encounterAlias property.
	 * 
	 * @param encounterAlias
	 *            the encounterAlias to set
	 */
	public I2b2SearchCriterionBuilder setEncounterAlias(final String encounterAlias)
	{
		this.encounterAlias = encounterAlias;
		return this;
	}

	/**
	 * Set a new value for the aliases property.
	 * 
	 * @param aliases
	 *            the aliases to set
	 * @return this for chaining
	 */
	public I2b2SearchCriterionBuilder setAliases(final List<SearchQueryAlias> aliases)
	{
		this.aliases.addAll(aliases);
		return this;
	}

	// ========================= PRIVATE METHODS ==============================

	/**
	 * Creates a CONJUNCTION phrase of the namespaceId, type, and code
	 * 
	 * @param namespace
	 * @param alias
	 * @param namespaceProperty
	 * @param typeProperty
	 * @param type
	 * @param codeProperty
	 * @return
	 */
	private SearchCriterion createPhrase(final Namespace namespace, final String alias,
			final String namespaceProperty, final String typeProperty, final String type,
			final String codeProperty)
	{
		final List<List<String>> lists = Lists.partition(domain, Constants.MAX_IN);
		SearchCriterion subCriterion = null;
		// If the lists size is > 1 then we have to do an OR of a bunch of INs
		final SearchCriterion parent = lists.size() > 1 ? junction(SearchType.DISJUNCTION)
				: null;
		for (final List<String> list : lists)
		{
			subCriterion = getPhraseCriterion(namespace);
			subCriterion.addCriterion(simpleExpression(Relation.EQ, alias + DOT
					+ namespaceProperty, getNamespaceId(namespace)));
			subCriterion.addCriterion(simpleExpression(Relation.EQ, alias + DOT
					+ typeProperty, type));
			subCriterion.addCriterion(constructInQuery(list, alias + DOT + codeProperty));
			if (dateConstraint != null
					&& (dateConstraint.getDateFrom() != null || dateConstraint
							.getDateTo() != null))
			{
				subCriterion.addCriterion(getDateConstraint());
			}

			if (parent != null)
			{
				parent.addCriterion(subCriterion);
			}
		}

		// We don't have a parent so just return us.
		if (parent == null)
		{
			return subCriterion;
		}

		// We have a parent of all subcriterion.
		return parent;
	}

	private SearchCriterion createPhrase(final Namespace namespace, final String alias,
			final String namespaceProperty, final String typeProperty, final String type,
			final String codeProperty, final String modifierProperty,
			final String modifier, final String modifierNamespaceProperty,
			final Namespace modifierNamespace)
	{
		final SearchCriterion criterion = createPhrase(namespace, alias,
				namespaceProperty, typeProperty, type, codeProperty);
		criterion.addCriterion(simpleExpression(Relation.EQ, alias + DOT
				+ modifierProperty, modifier));
		criterion.addCriterion(simpleExpression(Relation.EQ, alias + DOT
				+ modifierNamespaceProperty, getNamespaceId(modifierNamespace)));
		return criterion;
	}

	/**
	 * Creates a CONJUNCTION phrase with a namespace, namespaceId property, and property
	 * 
	 * @return
	 */
	private SearchCriterion createPhrase(final Namespace namespace,
			final String namespaceIdProperty, final String property)
	{
		final SearchCriterion subCriterion = junction(SearchType.CONJUNCTION);
		subCriterion.addCriterion(simpleExpression(Relation.EQ, namespaceIdProperty,
				getNamespaceId(namespace)));
		subCriterion.addCriterion(constructInQuery(domain, property));
		return subCriterion;
	}

	/**
	 * Returns the phrase criterion needed to build a specific phrase.
	 * 
	 * @return
	 */
	private SearchCriterion getPhraseCriterion(final Namespace namespace)
	{
		final NamespaceService namespaceService = I2b2QueryResourceLocator
				.getInstance()
				.getNamespaceService();
		final String LOINC = namespaceService.getNamespaceName(Namespaces.LOINC);
		if (LOINC.equals(namespaceService.getNamespaceName(namespace)))
		{
			return buildLoincSubCriterion();
		}

		return junction(SearchType.CONJUNCTION);
	}

	/**
	 * Builds LOINC sub criteria such as value constraints like high low or logical
	 * operations on values
	 * 
	 * @return the constructed {@link SearchCriterion}
	 */
	private SearchCriterion buildLoincSubCriterion()
	{
		final SearchCriterion subCriterion = junction(SearchType.CONJUNCTION);
		if (valueConstraint != null)
		{
			final I2b2ValueOperator valueOperator = valueConstraint.getValueOperator();

			switch (valueConstraint.getValueType())
			{
				case FLAG:
				{
					String value = null;
					if (valueConstraint.getValueConstraint() == null)
					{
						throw new ApplicationException(
								"Expected value constraint for type FLAG but was null");
					}

					final String constraint = valueConstraint
							.getValueConstraint()
							.toLowerCase();

					// Observation flags are hard coded to SNOMED qualifiers
					if ("h".equals(constraint))
					{
						value = "75540009";
					}
					else if ("l".equals(constraint))
					{
						value = "62482003";
					}
					else if ("n".equals(constraint))
					{
						value = "17621005";
					}
					else if ("a".equals(constraint))
					{
						value = "263654008";
					}
					else
					{
						throw new ApplicationException(
								"Unexpected value constraint. Only H, L, N, A are supported");
					}

					subCriterion.addCriterion(valueOperator.createExpression(
							observationAlias + DOT + "observationFlag", value));
					subCriterion.addCriterion(simpleExpression(Relation.EQ,
							"observationFlagNamespaceId",
							getNamespaceId(Namespaces.SNOMED_CT)));
					break;
				}
				case NUMBER:
				{
					subCriterion.addCriterion(valueOperator.createExpression(
							observationAlias + DOT + "valueNumber",
							valueConstraint.getValueConstraint()));
					subCriterion.addCriterion(simpleExpression(Relation.EQ,
							observationAlias + DOT + "valueType", "Number"));

					// Some lab values don't have units of measure
					if (!valueConstraint.getValueUnitOfMeasure().equals(
							I2b2QueryValueConstraint.UNDEFINED))
					{
						subCriterion.addCriterion(simpleExpression(Relation.EQ,
								observationAlias + DOT + "unitOfMeasureNamespaceId",
								getNamespaceId(Namespaces.UCUM)));
						subCriterion.addCriterion(createSimpleEqCriteria(observationAlias
								+ DOT + "unitOfMeasure",
								valueConstraint.getValueUnitOfMeasure()));
					}

					break;
				}
			}
		}
		return subCriterion;
	}

	/**
	 * Builds a demographics criterion based on the key type
	 * 
	 * @return
	 */
	private SearchCriterion buildDemographics()
	{
		switch (keyType)
		{
			case DEM_GENDER:
			{
				return createPhrase(Namespaces.SNOMED_CT,
						"administrativeGenderNamespaceId", "administrativeGender");
			}
			case DEM_LANG:
			{
				return createPhrase(Namespaces.SNOMED_CT, "primaryLanguageNamespaceId",
						"primaryLanguage");
			}
			case DEM_MARITAL_STATUS:
			{
				return createPhrase(Namespaces.SNOMED_CT, "maritalStatusNamespaceId",
						"maritalStatus");
			}
			case DEM_RACE:
			{
				return createPhrase(Namespaces.SNOMED_CT, "raceNamespaceId", "race");
			}
			case DEM_RELIGION:
			{
				return createPhrase(Namespaces.SNOMED_CT, "religionNamespaceId",
						"religion");
			}
			case DEM_VITAL_STATUS:
			{
				return createPhrase(Namespaces.SNOMED_CT, "vitalStatusNamespaceId",
						"vitalStatus");
			}
			case DEM_PEDIGREE_QUALITY:
			{
				return buildDemPedigreeQuality();
			}
			case DEM_AGE:
			{
				return buildCriteriaWithGreaterThan("age");
			}
			case DEM_BIRTH_YEAR:
			{
				return buildDemBirthYear();
			}
			case DEM_BIRTHDATE:
			{
				return buildDemBirthDate();
			}
			case DEM_ETHNICITY:
			{
				return buildDemEthnicity();
			}
			case DEM_LOCATION_STATE:
			{
				return createLocation("State");
			}
			case DEM_LOCATION_HDG:
			{
				return createLocation("Health District Group");
			}
			case DEM_LOCATION_COUNTY:
			{
				return createLocation("County");
			}
			default:
			{
				log.warn("I2b2KeyType UNKNOWN, this search parameter will be ignored");
				return null;
			}
		}
	}

	/**
	 * Builds an Encounter criterion based on the key type
	 * 
	 * @return
	 */
	private SearchCriterion buildEncounters()
	{
		SearchCriterion criterion = null;
		switch (keyType)
		{
			case ENC_ADMIT_SRC:
			{
				criterion = buildEncounterCriterion("admitSourceNamespaceId",
						"admitSource");
				break;
			}
			case ENC_ADMIT_TYPE:
			{
				criterion = buildEncounterCriterion("admitTypeNamespaceId", "admitType");
				break;
			}
			case ENC_LNGTH_STAY:
			{
				criterion = buildEncLengthOfStay();
				break;
			}
			case ENC_PAT_CLASS:
			{
				criterion = buildEncounterCriterion("patientClassNamespaceId",
						"patientClass");
				break;
			}
			case ENC_PAT_TYPE:
			{
				criterion = buildEncounterCriterion("patientTypeNamespaceId",
						"patientType");
				break;
			}
			default:
			{
				log.warn("I2b2KeyType UNKNOWN, this search parameter will be ignored");
				return null;
			}
		}

		if (dateConstraint != null
				&& (dateConstraint.getDateFrom() != null || dateConstraint.getDateTo() != null))
		{
			criterion.addCriterion(getDateConstraint());
		}

		return criterion;
	}

	/**
	 * Demographic ethnicity criterions are constructed by OR'ing all children of a given
	 * key and the key itself, typically there are no children and its just the key
	 * itself.
	 * 
	 * @return the constructed {@link SearchCriterion}
	 */
	private SearchCriterion buildDemEthnicity()
	{
		return createPhrase(Namespaces.SNOMED_CT, "ethnicityNamespaceId", "ethnicity");
	}

	/**
	 * Demographic pedigree criterions are constructed by OR'ing all children of a given
	 * key and the key itself, typically there are no children and its just the key
	 * itself.
	 * 
	 * @return the constructed {@link SearchCriterion}
	 */
	private SearchCriterion buildDemPedigreeQuality()
	{
		final SearchCriterion subCriterion = junction(SearchType.CONJUNCTION);
		subCriterion.addCriterion(constructInQuery(domain, "pedigreeQuality"));
		return subCriterion;
	}

	/**
	 * Demographic birth date criterion
	 * 
	 * @return the constructed {@link SearchCriterion}
	 */
	private SearchCriterion buildDemBirthDate()
	{
		final List<Date> dates = toDateList(domain);
		return SearchCriteria.range(SearchType.BETWEEN, "dateOfBirth",
				Collections.min(dates), Collections.max(dates));
	}

	/**
	 * Demographic birth year criterions for i2b2 are constructed using a BETWEEN
	 * 
	 * @return the constructed {@link SearchCriterion}
	 */
	@SuppressWarnings("boxing")
	private SearchCriterion buildDemBirthYear()
	{
		final List<Integer> years = toIntegerList(domain);
		return SearchCriteria.range(SearchType.BETWEEN, "birthYear", new Integer(
				Collections.min(years)), new Integer(Collections.max(years)));
	}

	/**
	 * Encounter Length of Stay for i2b2
	 * 
	 * @return the constructed {@link SearchCriterion}
	 */
	private SearchCriterion buildEncLengthOfStay()
	{
		final SearchCriterion and = SearchCriteria.junction(SearchType.CONJUNCTION);
		addSimpleExpression(and, EQ, encounterAlias + DOT
				+ "lengthOfStayUnitsNamespaceId", getNamespaceId(Namespaces.SNOMED_CT));
		// Hard coded to Days right now (Snomed 258703001 = days)
		addSimpleExpression(and, EQ, encounterAlias + DOT + "lengthOfStayUnits",
				258703001);
		and.addCriterion(buildCriteriaWithGreaterThan(encounterAlias + DOT
				+ "lengthOfStay"));
		return and;
	}

	@SuppressWarnings("boxing")
	private SearchCriterion buildCriteriaWithGreaterThan(final String parameterName)
	{

		String greaterThanEntry = new String();
		for (final String entry : domain)
		{
			if (entry.contains(">"))
			{
				greaterThanEntry = entry;
				domain.remove(entry);
				break;
			}
		}
		if (!greaterThanEntry.isEmpty())
		{
			if (domain.isEmpty())
			{
				return simpleExpression(greaterThanEntry.contains("=") ? GE : GT,
						parameterName, Integer.valueOf(greaterThanEntry
								.substring(greaterThanEntry.length() - 2)));
			}
			final List<Integer> years = toIntegerList(domain);
			return simpleExpression(GE, parameterName, Collections.min(years));
		}
		final List<Integer> years = toIntegerList(domain);
		return SearchCriteria.range(SearchType.BETWEEN, parameterName, new Integer(
				Collections.min(years)), new Integer(Collections.max(years)));
	}

	/**
	 * Builds a general encounter criterion given the namespaceId field and the actual
	 * criteria field.
	 * 
	 * @param namespaceIdField
	 * @param criterionField
	 * @return
	 */
	private SearchCriterion buildEncounterCriterion(final String namespaceIdField,
			final String criterionField)
	{
		final SearchCriterion and = SearchCriteria.junction(SearchType.CONJUNCTION);
		addSimpleExpression(and, EQ, encounterAlias + DOT + namespaceIdField,
				getNamespaceId(Namespaces.namespaceFor("@DSCUSTOM-26@")));
		and.addCriterion(constructInQuery(domain, encounterAlias + DOT + criterionField));
		return and;
	}

	/**
	 * Converts a {@link List} of {@link String}'s to a {@link List} of {@link Integer}'s
	 * 
	 * @param strings
	 * @return
	 */
	private List<Integer> toIntegerList(final List<String> strings)
	{
		final List<Integer> integer = newList();
		for (final String num : domain)
		{
			integer.add(Integer.valueOf(num));
		}
		return integer;
	}

	/**
	 * Converts a {@link List} of {@link String}'s to a {@link List} of {@link Integer}'s
	 * 
	 * @param strings
	 * @return
	 */
	private List<Date> toDateList(final List<String> strings)
	{
		final List<Date> dates = newList();
		for (final String date : domain)
		{
			try
			{
				dates.add(DateFormat.getInstance().parse(date));
			}
			catch (final ParseException e)
			{
				log.error("Unsupported date format");
			}
		}
		return dates;
	}

	/**
	 * FUR-1824
	 * 
	 * Returns a date constraint criterion based on the {@link I2b2KeyType}
	 * 
	 * @return a {@link SearchCriterion} to be added to an existing CONJUNCTION or to a
	 *         new CONJUNCTION
	 */
	private SearchCriterion getDateConstraint()
	{
		SearchType searchType;
		Relation relation = null;
		Object value = null;
		if (dateConstraint.getDateFrom() == null)
		{
			searchType = SearchType.SIMPLE;
			relation = Relation.LE;
			value = dateConstraint.getDateTo();
		}
		else if (dateConstraint.getDateTo() == null)
		{
			searchType = SearchType.SIMPLE;
			relation = Relation.GE;
			value = dateConstraint.getDateFrom();
		}
		else
		{
			searchType = SearchType.BETWEEN;
		}

		final String propertyName;

		switch (this.keyType)
		{
			case ICD9:
			case ICD10:
			case CPT:
			case LOINC:
			case BIO_SPECIMEN:
				propertyName = observationAlias + DOT + "startDateTime";
				break;
			case DEM_LOCATION_COUNTY:
			case DEM_LOCATION_HDG:
			case DEM_LOCATION_STATE:
				propertyName = locationAlias + DOT + "startDateTime";
				break;
			case RXNORM:
				propertyName = orderAlias + DOT + "dateTime";
				break;
			case ENC_ADMIT_SRC:
			case ENC_ADMIT_TYPE:
			case ENC_LNGTH_STAY:
			case ENC_PAT_TYPE:
				propertyName = encounterAlias + DOT + "admissionDate";
				break;
			default:
				throw new ApplicationException(
						"Reached unexpected I2b2KeyType in getDateConstraint() call");
		}

		switch (searchType)
		{
			case SIMPLE:
				notNull(relation);
				notNull(value);
				return SearchCriteria.simpleExpression(relation, propertyName, value);
			case BETWEEN:
				return SearchCriteria.range(searchType, propertyName,
						dateConstraint.getDateFrom(), dateConstraint.getDateTo());
			default:
				throw new ApplicationException(
						"Reached unexpected searchType in getDateConstraint() "
								+ "call. Only SIMPLE and BETWEEN are valid but recieved "
								+ searchType);
		}
	}

	/**
	 * Helper method to create locations criterion. Uses a single code from the domain to
	 * extract the prefix and determine the person location type.
	 * 
	 * @return
	 */
	private SearchCriterion createLocation(final String locationType)
	{
		// Get only the first code
		final String code = domain.get(0);

		final SearchCriterion and = SearchCriteria.junction(SearchType.CONJUNCTION);
		addSimpleExpression(and, EQ, locationAlias + DOT + "locationNamespaceId",
				getNamespaceId(Namespaces.SNOMED_CT));
		addSimpleExpression(and, EQ, locationAlias + DOT + "locationType", locationType);
		addSimpleExpression(and, EQ, locationAlias + DOT + "personLocationType",
				fromCode(code).getFieldName());

		/*
		 * FUR-1149 - For 0.5 we've decided only to select the top level item from i2b2
		 * when constructing the logical query. We do not add any of the children.
		 */
		final List<String> noChildren = newList();
		// Get rid of the prefix
		noChildren.add(code.substring(code.indexOf('|') + 1));

		and.addCriterion(constructInQuery(noChildren, locationAlias + DOT + "location"));
		if (dateConstraint != null && dateConstraint.getDateFrom() != null)
		{
			and.addCriterion(getDateConstraint());
		}
		return and;
	}

	/**
	 * Helper method to construct a IN query. If the list of items is greater than 1, an
	 * IN expression is created (a IN (foo, bar)). If the list of items is equal to one, a
	 * simple expression is return.
	 * 
	 * @param items
	 *            a list of basecodes to construct a {@link SearchCriterion} upon
	 * @param propertyName
	 *            the logical model property name, typically class.field
	 * @return
	 */
	private SearchCriterion constructInQuery(final List<String> items,
			final String propertyName)
	{
		// Ensure we have items
		if (items.size() <= 0)
		{
			return null;
		}

		// If its just a single item, its a simple equals criterion
		if (items.size() == 1)
		{
			return createSimpleEqCriteria(propertyName, items.get(0));
		}

		// If its 2, do an IN of each criterion
		final SearchCriterion inCriterion = SearchCriteria.collection(SearchType.IN,
				propertyName, (Object[]) items.toArray(new String[items.size()]));
		return inCriterion;
	}

	/**
	 * Helper method to create an equals criteria for a property and value.
	 * 
	 * @param propertyName
	 *            the logical model property name, typically class.field
	 * @param value
	 *            the value of the equals criteria; the right side of the statement (a =
	 *            b)
	 * @return
	 */
	private SearchCriterion createSimpleEqCriteria(final String propertyName,
			final String value)
	{
		return SearchCriteria.simpleExpression(Relation.EQ, propertyName, value);
	}

	/**
	 * @param namespace
	 * @return
	 */
	private Long getNamespaceId(final Namespace namespace)
	{
		return new Long(I2b2QueryResourceLocator
				.getInstance()
				.getNamespaceService()
				.getNamespaceId(namespace));
	}

	/**
	 * Adds an alias to the alias map
	 * 
	 * @param alias
	 * @param associationPath
	 */
	private final void addAlias(final String assosciationObject, final String alias,
			final String associationPath)
	{
		aliases.add(new SearchQueryAliasTo(assosciationObject, alias, associationPath));
	}

}
