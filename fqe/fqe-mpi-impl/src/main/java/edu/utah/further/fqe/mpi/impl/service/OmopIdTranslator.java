/*******************************************************************************
 * Source File: UpdbIdTranslator.java
 ******************************************************************************/
package edu.utah.further.fqe.mpi.impl.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.data.hibernate.adapter.CriteriaType;
import edu.utah.further.core.data.hibernate.adapter.GenericCriteria;
import edu.utah.further.core.data.hibernate.query.QueryBuilderHibernateImpl;
import edu.utah.further.core.query.domain.SearchCriteria;
import edu.utah.further.core.query.domain.SearchCriterion;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchType;
import edu.utah.further.fqe.mpi.api.IdTranslationProvider;
import edu.utah.further.fqe.mpi.impl.domain.LookupEntity;

/**
 * Translates OMOPv2 ids to a federated identifier.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2014 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Rich Hansen
 * @version Aug 26, 2014
 */
@Implementation
@Service("omopIdTranslator")
@Transactional(value = "lookupTransactionManager")
public final class OmopIdTranslator implements IdTranslationProvider
{
	/**
	 * {@link SessionFactory} for the lookup table
	 */
	@Autowired
	private SessionFactory lookupSessionFactory;

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mpi.api.IdTranslationProvider#translateToSourceId(java.lang.Long)
	 */
	@Override
	public List<Long> translateToSourceId(final Long federatedId)
	{
		final List<Long> id = CollectionUtil.newList();
		id.add(federatedId);
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mpi.api.IdTranslationProvider#translateToFederatedId(java.lang
	 * .Long)
	 */
	@Override
	public Long translateToFederatedId(final Long sourceId)
	{
		return sourceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mpi.api.IdTranslationProvider#translateToSourceIds(java.util.List)
	 */
	@Override
	public List<Long> translateToSourceIds(final List<Long> federatedIds)
	{
		final SearchCriterion rootAnd = SearchCriteria.junction(SearchType.CONJUNCTION);
		rootAnd.addCriterion(SearchCriteria.collection(SearchType.IN, "commonId",
				federatedIds.toArray()));

		final SearchQuery query = SearchCriteria.query(rootAnd, "LookupEntity");
		GenericCriteria crit = QueryBuilderHibernateImpl.convert(
				CriteriaType.CRITERIA, "edu.utah.further.fqe.mpi.impl.domain",
				lookupSessionFactory, query);

		final List<LookupEntity> lookups = crit.list();
		
		List<Long> sourceIds = new ArrayList<Long>();
		
		// Accumulate the source ids
		for (final LookupEntity lookup : lookups)
		{
			if(lookup.getSourceId() != null)
			{
				sourceIds.add(lookup.getSourceId());
			}
		}
		
		return sourceIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mpi.api.IdTranslationProvider#translateToFederatedIds(java.util
	 * .List)
	 */
	@Override
	public List<Long> translateToFederatedIds(final List<Long> sourceIds)
	{
		// OMOPv2 IDs current act as the federated ids
		return sourceIds;
	}

	/**
	 * Return the lookupSessionFactory property.
	 * 
	 * @return the lookupSessionFactory
	 */
	public SessionFactory getLookupSessionFactory()
	{
		return lookupSessionFactory;
	}

	/**
	 * Set a new value for the lookupSessionFactory property.
	 * 
	 * @param lookupSessionFactory
	 *            the lookupSessionFactory to set
	 */
	public void setLookupSessionFactory(final SessionFactory lookupSessionFactory)
	{
		this.lookupSessionFactory = lookupSessionFactory;
	}

}
