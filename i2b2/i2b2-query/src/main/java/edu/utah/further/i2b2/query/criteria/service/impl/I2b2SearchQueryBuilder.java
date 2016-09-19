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
import static edu.utah.further.core.query.domain.SearchCriteria.collectionSubQuery;
import static edu.utah.further.core.query.domain.SearchCriteria.junction;
import static edu.utah.further.core.query.domain.SearchCriteria.queryBuilder;
import static edu.utah.further.i2b2.query.criteria.key.I2b2KeyType.fromPattern;
import static org.apache.commons.lang.Validate.notNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryAlias;
import edu.utah.further.core.query.domain.SearchType;
import edu.utah.further.i2b2.query.criteria.key.I2b2KeyType;
import edu.utah.further.i2b2.query.criteria.service.AliasGenerator;
import edu.utah.further.i2b2.query.criteria.service.I2b2QueryService;
import edu.utah.further.i2b2.query.model.I2b2Query;
import edu.utah.further.i2b2.query.model.I2b2QueryGroup;
import edu.utah.further.i2b2.query.model.I2b2QueryItem;
import edu.utah.further.i2b2.query.model.I2b2QueryValueConstraint;

/**
 * Constructs a {@link SearchQuery} from an {@link I2b2Query}. This class is NOT thread
 * safe and should be synchronized externally.
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
 * @version Aug 24, 2009
 */
final class I2b2SearchQueryBuilder implements Builder<SearchQuery>
{
	// ========================= FIELDS ===================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(I2b2SearchQueryBuilder.class);

	// ========================= FIELDS ===================================

	/**
	 * The I2b2Query which holds information needed to build the search criteria.
	 */
	private final I2b2Query i2b2Query;

	/**
	 * The AND of all of the panels
	 */
	private final SearchCriterion andCriterion = junction(SearchType.CONJUNCTION);

	/**
	 * A running list of aliases that need to be generated
	 */
	private final Set<SearchQueryAlias> aliases = newSet();

	/**
	 * A running list of {@link I2b2KeyType}'s seen
	 */
	private final List<I2b2KeyType> pastKeys = newList();

	/**
	 * Observation alias generator
	 */
	private final AliasGenerator obsAliasGenerator = new AliasGeneratorImpl("Observation");

	/**
	 * Location alias generator
	 */
	private final AliasGenerator locAliasGenerator = new AliasGeneratorImpl("Location");

	/**
	 * Order alias generator
	 */
	private final AliasGenerator orderAliasGenerator = new AliasGeneratorImpl("Order");

	/**
	 * Order alias generator
	 */
	private final AliasGenerator encounterAliasGenerator = new AliasGeneratorImpl(
			"Encounter");

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a search query builder.
	 * 
	 * 
	 * @param i2b2Query
	 */
	public I2b2SearchQueryBuilder(final I2b2Query i2b2Query)
	{
		super();

		// Validate dependencies
		notNull(i2b2Query);

		// Set fields
		this.i2b2Query = i2b2Query;
	}

	// ========================= IMPLEMENTATION: Builder<SearchQuery> ======

	/**
	 * Constructs a {@link SearchQuery} from an {@link I2b2Query}.
	 * 
	 * Each QueryGroup represents an AND gate; each QueryItem is an OR gate; if a
	 * QueryGroup is inverted, it is equivalent to NOT AND.
	 * 
	 * @return
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public SearchQuery build()
	{
		final List<I2b2QueryGroup> i2b2QueryGroups = i2b2Query.getQueryGroups();

		for (final I2b2QueryGroup i2b2QueryGroup : i2b2QueryGroups)
		{
			// TODO: This is a little messy, clean up when time permits
			SearchCriterion currGrpCrit = junction(SearchType.DISJUNCTION);
			final List<I2b2KeyType> currKeys = getI2b2QueryService().getNonDemKeysInGrp(
					i2b2QueryGroup);
			final Set<SearchQueryAlias> currAliases = CollectionUtil.newSet();
			final boolean subQryReq = isSubQueryRequired(currKeys);

			final String observationAlias = obsAliasGenerator.getAlias(subQryReq);
			/*
			 * Never generate new at this point for Orders or Locations. Change when
			 * subqueries for these types are required. They'll be required in later
			 * versions when we have different types of orders other than Medication and
			 * different types of locations other than CurrentLocation.
			 */
			final String orderAlias = orderAliasGenerator.getAlias(false);
			final String locationAlias = locAliasGenerator.getAlias(false);
			final String encounterAlias = encounterAliasGenerator.getAlias(false);

			final GroupedCriteria groupedCriteria = new GroupedCriteria();

			for (final I2b2QueryItem i2b2QueryItem : i2b2QueryGroup.getQueryItems())
			{

				// (FUR-1772)
				// Handle Grouped Criteria
				String hackPattern = "";
				if (i2b2QueryItem.getToolTip().equals("[MultumDrug]")) {
					hackPattern = "MultumDrug:";
				}
				if (fromPattern(hackPattern + i2b2QueryItem.getItemKey()).isGroupedCriteria())
				{
					groupedCriteria.addCriteria(i2b2QueryItem);
				}

				// Translates non grouped criteria
				else
				{
					final I2b2SearchCriterionBuilder criterionBuilder = toCriteria(
							i2b2QueryItem, observationAlias, orderAlias, locationAlias,
							encounterAlias);

					final SearchCriterion currentCriteria = criterionBuilder.build();
					currAliases.addAll(criterionBuilder.buildAliases());

					currGrpCrit = handleCurrCriteria(i2b2QueryGroup, i2b2QueryItem,
							currentCriteria, currGrpCrit);
				}
			}

			// Translate groups
			for (final Entry<I2b2KeyType, List<String>> entry : groupedCriteria
					.getGroupedCriteria()
					.entrySet())
			{
				if (!entry.getValue().isEmpty())
				{
					currGrpCrit = handleCriteriaGroup(i2b2QueryGroup, currGrpCrit,
							entry.getKey(), currAliases, observationAlias, orderAlias,
							locationAlias, encounterAlias, entry.getValue(), null);
				}

			}

			if (subQryReq)
			{
				currGrpCrit = handleSubQuery(currAliases, currGrpCrit);
				// Do not add current alias to outer query
				currAliases.clear();
			}

			currGrpCrit = handleInverted(i2b2QueryGroup, currGrpCrit);

			aliases.addAll(currAliases);

			andCriterion.addCriterion(currGrpCrit);
		}

		// The root object is hardcoded for now, eventually, when the interface permits,
		// have it send in what the root should be.
		return queryBuilder(andCriterion)
				.addAliases(CollectionUtil.newList(aliases))
				.setRootObject("Person")
				.build();
	}

	/**
	 * @param i2b2QueryGroup
	 * @param currGrpCrit
	 * @param currAliases
	 * @param observationAlias
	 * @param orderAlias
	 * @param locationAlias
	 * @param domain
	 * @param valueConstraint
	 * @return
	 */
	private SearchCriterion handleCriteriaGroup(final I2b2QueryGroup i2b2QueryGroup,
			final SearchCriterion currGrpCrit, final I2b2KeyType keyType,
			final Set<SearchQueryAlias> currAliases, final String observationAlias,
			final String orderAlias, final String locationAlias,
			final String encounterAlias, final List<String> domain,
			final I2b2QueryValueConstraint valueConstraint)
	{
		final I2b2SearchCriterionBuilder criterionBuilder = toCriteria(keyType,
				valueConstraint, domain, observationAlias, orderAlias, locationAlias,
				encounterAlias);

		final SearchCriterion currentCriteria = criterionBuilder.build();
		currAliases.addAll(criterionBuilder.buildAliases());

		return handleCurrCriteria(i2b2QueryGroup, null, currentCriteria, currGrpCrit);
	}

	// ========================= PRIVATE METHODS ==========================

	/**
	 * Handles when a Query Group is inverted.
	 * 
	 * @param i2b2QueryGroup
	 */
	private SearchCriterion handleInverted(final I2b2QueryGroup i2b2QueryGroup,
			final SearchCriterion currGrpCrit)
	{
		if (i2b2QueryGroup.isInverted())
		{
			return SearchCriteria.unary(SearchType.NOT, currGrpCrit);
		}

		return currGrpCrit;
	}

	/**
	 * @param currAliases
	 * @return
	 */
	private SearchCriterion handleSubQuery(final Set<SearchQueryAlias> currAliases,
			final SearchCriterion currGrpCrit)
	{
		return collectionSubQuery(SearchType.IN, "compositeId", SearchCriteria
				.queryBuilder(currGrpCrit)
				.addAliases(CollectionUtil.newList(currAliases))
				.setRootObject("Person")
				.build());
	}

	/**
	 * Handles the current criteria, warns if the criteria was not converted.
	 * 
	 * @param i2b2QueryGroup
	 * @param i2b2QueryItem
	 * @param currentCriteria
	 */
	private SearchCriterion handleCurrCriteria(final I2b2QueryGroup i2b2QueryGroup,
			final I2b2QueryItem i2b2QueryItem, final SearchCriterion currentCriteria,
			final SearchCriterion currGrpCrit)
	{
		if (currentCriteria != null)
		{
			if (i2b2QueryGroup.getQueryItems().size() > 1)
			{
				// Add to children
				currGrpCrit.addCriterion(currentCriteria);
				return currGrpCrit;
			}
			// Avoid having a DISJUNCTION with 1 child
			return currentCriteria;
		}
		handleUnknown(i2b2QueryItem);
		return currGrpCrit;
	}

	/**
	 * Determines based on past key types and current key types whether or not a subquery
	 * is required.
	 * 
	 * @param currKeys
	 * @return
	 */
	private boolean isSubQueryRequired(final List<I2b2KeyType> currKeys)
	{
		try
		{
			for (final I2b2KeyType keyType : currKeys)
			{
				for (final I2b2KeyType conflict : keyType.getConflicts())
				{
					if (pastKeys.contains(conflict))
					{
						return true;
					}
				}

			}
			return false;
		}
		finally
		{
			// Always add all the current keys
			pastKeys.addAll(currKeys);
		}

	}

	/**
	 * Converts a {@link I2b2QueryItem} to a {@link SearchCriterion}
	 * 
	 * @param aliases
	 * @param i2b2QueryItem
	 * @return
	 */
	private I2b2SearchCriterionBuilder toCriteria(final I2b2QueryItem i2b2QueryItem,
			final String obsAlias, final String orderAlias, final String locAlias,
			final String encounterAlias)
	{
		return new I2b2SearchCriterionBuilder(fromPattern(i2b2QueryItem.getItemKey()))
				.setDomain(getI2b2QueryService().findDomain(i2b2QueryItem))
				.setValueConstraint(i2b2QueryItem.getConstrainByValue())
				.setDateConstraint(i2b2QueryItem.getConstrainByDate())
				.setObservationAlias(obsAlias)
				.setOrderAlias(orderAlias)
				.setLocationAlias(locAlias)
				.setEncounterAlias(encounterAlias);
	}

	/**
	 * Converts a {@link I2b2QueryItem} to a {@link SearchCriterion}
	 * 
	 * @param aliases
	 * @param i2b2QueryItem
	 * @return
	 */
	private I2b2SearchCriterionBuilder toCriteria(final I2b2KeyType keyType,
			final I2b2QueryValueConstraint constraint, final List<String> domain,
			final String obsAlias, final String orderAlias, final String locAlias,
			final String encounterAlias)
	{
		return new I2b2SearchCriterionBuilder(keyType)
				.setDomain(domain)
				.setValueConstraint(constraint)
				.setObservationAlias(obsAlias)
				.setOrderAlias(orderAlias)
				.setLocationAlias(locAlias)
				.setEncounterAlias(encounterAlias);
	}

	/**
	 * Gets the current {@link I2b2QueryService}
	 * 
	 * @return
	 */
	private I2b2QueryService getI2b2QueryService()
	{
		return I2b2QueryResourceLocator.getInstance().getI2b2QueryService();
	}

	/**
	 * Logs an unknown query item
	 * 
	 * @param i2b2QueryItem
	 */
	private void handleUnknown(final I2b2QueryItem i2b2QueryItem)
	{
		throw new ApplicationException(
				"Criteria for I2B2 key "
						+ i2b2QueryItem.getItemKey()
						+ " could not be created, no mapping for this key type was found, cannot continue. "
						+ "Consider adding a new pattern to detect this key type.");
	}
}
