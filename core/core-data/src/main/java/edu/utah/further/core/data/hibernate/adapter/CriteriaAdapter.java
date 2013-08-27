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
package edu.utah.further.core.data.hibernate.adapter;

import static edu.utah.further.core.api.message.Messages.unsupportedMessage;

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.transform.ResultTransformer;

import edu.utah.further.core.api.exception.BusinessRuleException;

/**
 * Adapts a {@link Criteria} to the {@link GenericCriteria} common interface.
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
 * @version Jul 21, 2010
 */
final class CriteriaAdapter extends AbstractCriteriaAdapter
{
	// ========================= FIELDS ====================================

	/**
	 * The adapted object.
	 */
	private final Criteria delegate;

	// ========================= METHODS ===================================

	/**
	 * @param delegate
	 */
	CriteriaAdapter(final Criteria delegate)
	{
		super(CriteriaType.CRITERIA);
		this.delegate = delegate;
	}

	// ========================= IMPL: CriteriaBase ========================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.data.hibernate.adapter.CriteriaBase#getAlias()
	 */
	@Override
	public String getAlias()
	{
		return delegate.getAlias();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setProjection(org.hibernate
	 * .criterion.Projection)
	 */
	@Override
	public GenericCriteria setProjection(final Projection projection)
	{
		delegate.setProjection(projection);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#add(org.hibernate.criterion
	 * .Criterion)
	 */
	@Override
	public GenericCriteria add(final Criterion criterion)
	{
		delegate.add(criterion);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#addOrder(org.hibernate
	 * .criterion.Order)
	 */
	@Override
	public GenericCriteria addOrder(final Order order)
	{
		delegate.addOrder(order);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setFetchMode(java.lang
	 * .String, org.hibernate.FetchMode)
	 */
	@Override
	public GenericCriteria setFetchMode(final String associationPath, final FetchMode mode)
	{
		delegate.setFetchMode(associationPath, mode);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setLockMode(org.hibernate
	 * .LockMode)
	 */
	@Override
	public GenericCriteria setLockMode(final LockMode lockMode)
	{
		delegate.setLockMode(lockMode);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setLockMode(java.lang
	 * .String, org.hibernate.LockMode)
	 */
	@Override
	public GenericCriteria setLockMode(final String alias, final LockMode lockMode)
	{
		delegate.setLockMode(lockMode);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#createAlias(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public GenericCriteria addAlias(final String alias, final String associationPath)
	{
		delegate.createAlias(associationPath, alias);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#createAlias(java.lang
	 * .String, java.lang.String, int)
	 */
	@Override
	public GenericCriteria addAlias(final String alias, final String associationPath,
			final int joinType)
	{
		delegate.createAlias(associationPath, alias, joinType);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#createCriteria(java.lang
	 * .String)
	 */
	@Override
	public GenericCriteria createCriteria(final String associationPath)
	{
		delegate.createCriteria(associationPath);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#createCriteria(java.lang
	 * .String, int)
	 */
	@Override
	public GenericCriteria createCriteria(final String associationPath, final int joinType)
	{
		delegate.createCriteria(associationPath, joinType);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#createCriteria(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public GenericCriteria createCriteria(final String associationPath, final String alias)
	{
		delegate.createCriteria(associationPath, alias);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#createCriteria(java.lang
	 * .String, java.lang.String, int)
	 */
	@Override
	public GenericCriteria createCriteria(final String associationPath,
			final String alias, final int joinType)
	{
		delegate.createCriteria(associationPath, alias, joinType);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setResultTransformer(
	 * org.hibernate.transform.ResultTransformer)
	 */
	@Override
	public GenericCriteria setResultTransformer(final ResultTransformer resultTransformer)
	{
		delegate.setResultTransformer(resultTransformer);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setMaxResults(int)
	 */
	@Override
	public GenericCriteria setMaxResults(final int maxResults)
	{
		delegate.setMaxResults(maxResults);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setFirstResult(int)
	 */
	@Override
	public GenericCriteria setFirstResult(final int firstResult)
	{
		delegate.setFirstResult(firstResult);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setFetchSize(int)
	 */
	@Override
	public GenericCriteria setFetchSize(final int fetchSize)
	{
		delegate.setFetchSize(fetchSize);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setTimeout(int)
	 */
	@Override
	public GenericCriteria setTimeout(final int timeout)
	{
		delegate.setTimeout(timeout);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setCacheable(boolean)
	 */
	@Override
	public GenericCriteria setCacheable(final boolean cacheable)
	{
		delegate.setCacheable(cacheable);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setCacheRegion(java.lang
	 * .String)
	 */
	@Override
	public GenericCriteria setCacheRegion(final String cacheRegion)
	{
		delegate.setCacheRegion(cacheRegion);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setComment(java.lang.
	 * String)
	 */
	@Override
	public GenericCriteria setComment(final String comment)
	{
		delegate.setComment(comment);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setFlushMode(org.hibernate
	 * .FlushMode)
	 */
	@Override
	public GenericCriteria setFlushMode(final FlushMode flushMode)
	{
		delegate.setFlushMode(flushMode);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#setCacheMode(org.hibernate
	 * .CacheMode)
	 */
	@Override
	public GenericCriteria setCacheMode(final CacheMode cacheMode)
	{
		delegate.setCacheMode(cacheMode);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.data.hibernate.adapter.CriteriaBase#list()
	 */
	@Override
	public <T> List<T> list()
	{
		return delegate.list();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.data.hibernate.adapter.CriteriaBase#scroll()
	 */
	@Override
	public ScrollableResults scroll()
	{
		return delegate.scroll();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.data.hibernate.adapter.CriteriaBase#scroll(org.hibernate.
	 * ScrollMode)
	 */
	@Override
	public ScrollableResults scroll(final ScrollMode scrollMode)
	{
		return delegate.scroll(toHibernateScrollMode(scrollMode));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.data.hibernate.adapter.CriteriaBase#uniqueResult()
	 */
	@Override
	public <T> T uniqueResult()
	{
		return (T) delegate.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.data.hibernate.adapter.AbstractCriteriaAdapter#
	 * getHibernateCriteria()
	 */
	@Override
	public Criteria getHibernateCriteria()
	{
		return delegate;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Converts our enumerated scroll mode type to its Hibernate counterpart.
	 *
	 * @param scrollMode
	 *            our scroll mode
	 * @return Hibernate scroll mode
	 */
	protected static final org.hibernate.ScrollMode toHibernateScrollMode(
			final ScrollMode scrollMode)
	{
		switch (scrollMode)
		{
			case FORWARD_ONLY:
			{
				return org.hibernate.ScrollMode.FORWARD_ONLY;
			}

			case SCROLL_INSENSITIVE:
			{
				return org.hibernate.ScrollMode.SCROLL_INSENSITIVE;
			}

			case SCROLL_SENSITIVE:
			{
				return org.hibernate.ScrollMode.SCROLL_SENSITIVE;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(scrollMode));
			}
		}
	}
}
