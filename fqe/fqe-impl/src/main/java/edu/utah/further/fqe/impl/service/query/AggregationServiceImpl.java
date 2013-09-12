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
package edu.utah.further.fqe.impl.service.query;

import static edu.utah.further.core.api.constant.Constants.INVALID_VALUE_INTEGER;
import static edu.utah.further.fqe.ds.api.service.results.ResultType.INTERSECTION;
import static edu.utah.further.fqe.ds.api.service.results.ResultType.SUM;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.CollectionUtil.MapType;
import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.fqe.api.service.query.AggregationService;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.api.util.FqeQueryContextUtil;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResult;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResultTo;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResults;
import edu.utah.further.fqe.api.ws.to.aggregate.AggregatedResultsTo;
import edu.utah.further.fqe.api.ws.to.aggregate.Category;
import edu.utah.further.fqe.api.ws.to.aggregate.CategoryTo;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.domain.ResultContextKey;
import edu.utah.further.fqe.ds.api.service.results.ResultDataService;
import edu.utah.further.fqe.ds.api.service.results.ResultSummaryService;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.ResultContextKeyToImpl;
import edu.utah.further.fqe.ds.api.util.FqeDsQueryContextUtil;

/**
 * A data source result set aggregation service implementation. Relies on a Hibernate
 * persistent layer of {@link QueryContext}s.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jun 1, 2010
 */
@Service("aggregationService")
@Transactional
public class AggregationServiceImpl implements AggregationService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(AggregationServiceImpl.class);

	/**
	 * Demographic category metadata. TODO: replace this with logical model attributes
	 * read from the MDR (FUR-1554). Note: in principle, this module should be unaware of
	 * VR column names, which is the responsibility of the data layer. Since this is a
	 * mock implementation, however, we store it here for easier access by all involved
	 * classes. This map acts like the logical model and VR data model MDR XMIs.
	 */
	private static final Map<String, AttributeMetadata> ATTRIBUTE_METADATA = CollectionUtil
			.newMap(MapType.CONCURRENT_HASH_MAP);

	static
	{
		addAttributeMetadata("age", "Age†", "AGE_IN_YEARS_NUM");
		addAttributeMetadata("religion", "Religion", "RELIGION_CD");
		addAttributeMetadata("race", "Race", "RACE_CD");
		addAttributeMetadata("sex", "Sex", "SEX_CD");
		addAttributeMetadata("language", "Language", "LANGUAGE_CD");
		addAttributeMetadata("maritalstatus", "Marital Status", "MARITAL_STATUS_CD");
		addAttributeMetadata("vitalStatus", "Vital Status/Deceased", "VITAL_STATUS_CD");
		addAttributeMetadata("pedigreeQuality", "Pedigree Quality", "PEDIGREE_QUALITY");
	}

	/**
	 * @param name
	 * @param displayName
	 * @param columnName
	 */
	private static void addAttributeMetadata(final String name, final String displayName,
			final String columnName)
	{
		ATTRIBUTE_METADATA
				.put(name, new AttributeMetadata(name, displayName, columnName));
	}

	// ========================= DEPENDENCIES ==============================

	/**
	 * {@link QueryContext} CRUD service.
	 */
	@Autowired
	private QueryContextService qcService;

	/**
	 * Service for retrieving count results
	 */
	@Autowired
	private ResultSummaryService resultSummaryService;

	/**
	 * Service for retrieving data results
	 */
	@Autowired
	private ResultDataService resultDataService;

	/**
	 * Handles generic DAO operations and searches.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;

	/**
	 * Count results smaller than this value are scrubbed.
	 */
	private int resultMaskBoundary = 5;

	/**
	 * Name of histogram category that lumps all small values.
	 */
	private String resultMaskOther = "Other";

	/**
	 * Name of histogram category for missing data.
	 */
	private final String missingData = "Missing Data";

	// ========================= IMPLEMENTATION: DataService ===============

	/**
	 * Synchronize the parent FQC state of with a DQC's state (because we are not
	 * cascading updates in the QC entity, this needs to be done "manually").
	 * <p>
	 * TODO: replace by the observer pattern? (parent observes its children)
	 * 
	 * @param child
	 *            DS query context. Assumed to have a persistent parent
	 * @see edu.utah.further.fqe.api.service.query.AggregationService#updateParentState(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public synchronized void updateParentState(final QueryContext child)
	{
		// Need to reload parent entity because of the argument outlined above for the
		// entity: the might already be associated with the persistent session
		final QueryContext parent = qcService.findById(child.getParent().getId());
		if (log.isDebugEnabled())
		{
			log.debug("updateParentState()");
			log.debug("Child  " + child);
			log.debug("Parent " + parent);
		}
		if (parent.isInFinalState())
		{
			// Parent already completed, don't update its state
			return;
		}

		// Synchronize parent state with updated child state
		updateStateUponChildStart(parent, child);
		if (parent.isStarted())
		{
			updateExecutingStateForceful(parent);
		}

		// Save parent changes to database
		if (log.isDebugEnabled())
		{
			log.debug("Saving synchronized parent " + parent);
		}
		dao.update(parent);
	}

	/**
	 * Update the parent result set count to be the sum of its children counts.
	 * 
	 * @param parent
	 *            federated query context. Assumed to be persistent
	 * @see edu.utah.further.fqe.api.service.query.AggregationService#updateCounts(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public synchronized void updateCounts(final QueryContext parent)
	{
		// Need to reload parent entity because of the argument outlined above for the
		// entity: the might already be associated with the persistent session
		final QueryContext reloadedParent = qcService.findById(parent.getId());

		// A simple update of the parent raw result set size for now
		sumUpCounts(reloadedParent);

		// Save parent changes to database
		dao.update(reloadedParent);
	}

	/**
	 * Generate post-query result views (union, intersection, etc.)
	 * 
	 * @param parent
	 *            federated query context
	 * @see edu.utah.further.fqe.api.service.query.AggregationService#generateResultViews(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public synchronized void generateResultViews(final QueryContext federatedQueryContext)
	{
		final QueryContext parent = qcService.findById(federatedQueryContext.getId());
		if (parent.getResultViews() != null && parent.getResultViews().size() > 0)
		{
			log.debug("Resultviews have already been generated, "
					+ "query finished early or was sealed by QuerySealer");
			return;
		}
		if (log.isDebugEnabled())
		{
			log.debug("generateResultViews() " + parent);
		}
		final List<String> queryIds = qcService.findChildrenQueryIdsByParent(parent);
		switch (parent.getQueryType())
		{
			case DATA_QUERY:
			{
				addResultViewTo(parent, queryIds, SUM, null);
				addResultViewTo(parent, queryIds, INTERSECTION, new Integer(1));
				// Could also be called with last arg = null but that might mess up view
				// TO order
				// in result TO
				addResultViewTo(parent, queryIds, INTERSECTION,
						new Integer(queryIds.size()));
				break;
			}
			case COUNT_QUERY:
			{
				break;
			}
		}

		// Save parent changes to database
		dao.update(parent);
	}

	/**
	 * @param federatedQueryContext
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.AggregationService#generatedAggregatedResults(edu.utah.further.fqe.ds.api.domain.QueryContext)
	 */
	@Override
	public synchronized AggregatedResults generatedAggregatedResults(
			final QueryContext federatedQueryContext)
	{
		final QueryContext parent = qcService.findById(federatedQueryContext.getId());
		if (log.isDebugEnabled())
		{
			log.debug("generateResultViews() " + parent);
		}
		final List<String> queryIds = qcService.findChildrenQueryIdsByParent(parent);
		final AggregatedResults results = new AggregatedResultsTo();
		final int numDataSources = queryIds.size();
		results.setNumDataSources(numDataSources);
		results.addResult(mockAggregatedResult(SUM, INVALID_VALUE_INTEGER, queryIds));
		if (numDataSources > 1)
		{
			// Add intersections only if there are at least two data sources
			for (int i = 1; i <= numDataSources; i++)
			{
				results.addResult(mockAggregatedResult(INTERSECTION, i, queryIds));
			}
		}
		return results;
	}

	/**
	 * @param federatedQueryContext
	 * @param resultType
	 * @param intersectionIndex
	 * @return
	 * @see edu.utah.further.fqe.api.service.query.AggregationService#generatedAggregatedResult(QueryContext,
	 *      ResultType, int)
	 */
	@Override
	public synchronized AggregatedResult generatedAggregatedResult(
			final QueryContext federatedQueryContext, final ResultType resultType,
			final int intersectionIndex)
	{
		final QueryContext parent = qcService.findById(federatedQueryContext.getId());
		if (log.isDebugEnabled())
		{
			log.debug("generateResultViews() " + parent);
		}
		final List<String> queryIds = qcService.findChildrenQueryIdsByParent(parent);
		final AggregatedResult result = mockAggregatedResult(resultType,
				intersectionIndex, queryIds);
		return result;
	}

	/**
	 * Add missing data Category to aggregated demographic results
	 * 
	 * @param results
	 * 
	 * @return results with missing data
	 * 
	 */

	@Override
	@SuppressWarnings("boxing")
	public AggregatedResults addMissingDataEntries(final AggregatedResults results,
			final Map<ResultContextKey, ResultContext> resultViews)
	{

		for (final AggregatedResult result : results.getResults())
		{
			// Get total records
			long numResults = 0;
			if (result.getKey().getType().equals(ResultType.SUM))
			{
				final ResultContext resultView = resultViews
						.get(new ResultContextKeyToImpl(ResultType.SUM, null));
				numResults = resultView.getNumRecords();
			}
			else
			{
				final ResultContext resultView = resultViews.get(result.getKey());
				numResults = resultView.getNumRecords();
			}
			for (final Category category : result.getCategories())
			{
				// check if total records is greater than number of demographic category
				// records
				long numRecords = 0;
				for (final Map.Entry<String, Long> entry : category
						.getEntries()
						.entrySet())
				{
					numRecords += entry.getValue();
				}
				if (numResults > numRecords)
				{
					category.addEntry(missingData, numResults - numRecords);
				}
			}
		}
		return results;
	}

	/**
	 * Scrub positive counts that are smaller than the mask boundary value. By convention,
	 * all scrubbed entries are set to {@link Constants#INVALID_VALUE_BOXED_LONG}.
	 * 
	 * @param results
	 *            raw counts
	 * @return scrubbed results
	 */
	@Override
	public AggregatedResults scrubResults(final AggregatedResults results)
	{
		for (final AggregatedResult result : results.getResults())
		{
			for (final Category category : result.getCategories())
			{
				// FUR-1745: (a) find all categories with small entries
				final Set<String> smallEntryKeys = CollectionUtil.newSet();
				for (final Map.Entry<String, Long> entry : category
						.getEntries()
						.entrySet())
				{
					final Long value = entry.getValue();
					if (FqeQueryContextUtil.shouldBeMasked(value.longValue(),
							resultMaskBoundary))
					{
						smallEntryKeys.add(entry.getKey());
						// category.addEntry(entry.getKey(),
						// Constants.INVALID_VALUE_BOXED_LONG);
					}
				}

				// FUR-1745: (b) lump all small entries into one "Other" category
				if (!smallEntryKeys.isEmpty())
				{
					for (final String key : smallEntryKeys)
					{
						category.removeEntry(key);
					}
					category
							.addEntry(resultMaskOther, Constants.INVALID_VALUE_BOXED_LONG);
				}
				if (category.getName().equals("Age†"))
				{
					if (category.removeEntry(Strings.NULL_TO_STRING) != null)
					{
						category.addEntry(resultMaskOther,
								Constants.INVALID_VALUE_BOXED_LONG);
					}
				}
			}
		}
		return results;
	}

	/**
	 * Dependency-inject a count scrub threshold.
	 * 
	 * @param resultMaskBoundary
	 *            new count scrub threshold
	 * @see edu.utah.further.fqe.api.service.query.AggregationService#setResultMaskBoundary(int)
	 */
	@Override
	public void setResultMaskBoundary(final int resultMaskBoundary)
	{
		this.resultMaskBoundary = resultMaskBoundary;
	}

	/**
	 * Set a new value for the resultMaskOther property.
	 * 
	 * @param resultMaskOther
	 *            the resultMaskOther to set
	 * @see edu.utah.further.fqe.api.service.query.AggregationService#setResultMaskOther(java.lang.String)
	 */
	@Override
	public void setResultMaskOther(final String resultMaskOther)
	{
		this.resultMaskOther = resultMaskOther;
	}
	
	// ========================= GET/SET METHODS ===========================

	/**
	 * Return the qcService property.
	 *
	 * @return the qcService
	 */
	public QueryContextService getQcService()
	{
		return qcService;
	}

	/**
	 * Set a new value for the qcService property.
	 *
	 * @param qcService the qcService to set
	 */
	public void setQcService(final QueryContextService qcService)
	{
		this.qcService = qcService;
	}

	/**
	 * Return the resultSummaryService property.
	 *
	 * @return the resultSummaryService
	 */
	public ResultSummaryService getResultSummaryService()
	{
		return resultSummaryService;
	}

	/**
	 * Set a new value for the resultSummaryService property.
	 *
	 * @param resultSummaryService the resultSummaryService to set
	 */
	public void setResultSummaryService(final ResultSummaryService resultSummaryService)
	{
		this.resultSummaryService = resultSummaryService;
	}

	/**
	 * Return the resultDataService property.
	 *
	 * @return the resultDataService
	 */
	public ResultDataService getResultDataService()
	{
		return resultDataService;
	}

	/**
	 * Set a new value for the resultDataService property.
	 *
	 * @param resultDataService the resultDataService to set
	 */
	public void setResultDataService(final ResultDataService resultDataService)
	{
		this.resultDataService = resultDataService;
	}

	/**
	 * Return the dao property.
	 *
	 * @return the dao
	 */
	public Dao getDao()
	{
		return dao;
	}

	/**
	 * Set a new value for the dao property.
	 *
	 * @param dao the dao to set
	 */
	public void setDao(final Dao dao)
	{
		this.dao = dao;
	}
	
	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param resultType
	 * @param intersectionIndex
	 * @param queryIds
	 * @return
	 */
	private AggregatedResult mockAggregatedResult(final ResultType resultType,
			final int intersectionIndex, final List<String> queryIds)
	{
		final AggregatedResult result = new AggregatedResultTo(resultType, new Integer(
				intersectionIndex));
		for (final Map.Entry<String, AttributeMetadata> entry : ATTRIBUTE_METADATA
				.entrySet())
		{
			result.addCategory(generateCategory(queryIds, entry.getValue(), resultType,
					intersectionIndex));
		}
		return result;
	}

	/**
	 * Compute a single demographic category histogram.
	 * 
	 * @param result
	 * @param queryIds
	 * @param attributeName
	 * @param resultType
	 * @param intersectionIndex
	 * @return
	 */
	private Category generateCategory(final List<String> queryIds,
			final AttributeMetadata attributeMetadata, final ResultType resultType,
			final int intersectionIndex)
	{
		final Map<String, Long> join = resultDataService.join(queryIds,
				attributeMetadata.getColumnName(), resultType, intersectionIndex);
		return new CategoryTo(attributeMetadata.getDisplayName(), join);
	}

	/**
	 * @param parent
	 * @param queryIds
	 * @param resultType
	 * @param intersectionIndex
	 */
	private void addResultViewTo(final QueryContext parent, final List<String> queryIds,
			final ResultType resultType, final Integer intersectionIndex)
	{
		FqeDsQueryContextUtil.addResultViewTo(parent, resultType, intersectionIndex,
				resultSummaryService.join(queryIds, resultType).longValue());
	}

	/**
	 * @param parent
	 * @param child
	 */
	private void updateStateUponChildStart(final QueryContext parent,
			final QueryContext child)
	{
		if ((child.isStarted() || child.isInFinalState()) && !parent.isStarted()
				&& !parent.isFailed())
		{
			if (log.isDebugEnabled())
			{
				log.debug("Starting parent " + parent
						+ " because there's a running child: " + child);
			}
			parent.start();
		}
	}

	/**
	 * Update an executing parent state according to children completion states (FUR-575):
	 * <ul>
	 * <li>Transition query to COMPLETED state regardless of staleness if At least
	 * maxRespondingDataSources DS's have responded.
	 * </ul>
	 * 
	 * @param parent
	 *            federated query contexts to update
	 */
	private synchronized void updateExecutingStateForceful(final QueryContext parent)
	{
		final int numRespondingDs = qcService.findCompletedChildren(parent).size();
		final int maxRespondingDs = parent.getMaxRespondingDataSources();
		if (numRespondingDs >= maxRespondingDs)
		{
			if (log.isDebugEnabled())
			{
				log.debug(numRespondingDs + " DS's responded >= maximum required ("
						+ maxRespondingDs + "). Finishing query early.");
			}
			parent.finish();
			generateResultViews(parent);
		}
	}

	/**
	 * Set the parent's result set count to the sum of all result set counts of all its
	 * children QC's.
	 * 
	 * @param parent
	 *            federated QC
	 */
	private synchronized void sumUpCounts(final QueryContext parent)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Aggregating counts of query ID " + parent.getId());
		}
		int sum = 0;
		for (final QueryContext child : qcService.findCompletedChildren(parent))
		{
			final long dataSourceCount = child.getResultContext().getNumRecords();
			if (StringUtil.isValidLong(dataSourceCount))
			{
				sum += dataSourceCount;
			}
			if (log.isDebugEnabled())
			{
				log.debug("Child DS-ID " + child.getDataSourceId() + " count "
						+ dataSourceCount);
			}
		}
		parent.getResultContext().setNumRecords(sum);
		if (log.isDebugEnabled())
		{
			log.debug("Total count of query ID " + parent.getId() + ": " + sum);
		}
	}

}
