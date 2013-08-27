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
package edu.utah.further.core.util.collections.page;

import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static edu.utah.further.core.api.lang.ReflectionUtil.isGeneralizationOf;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.page.IterableType;
import edu.utah.further.core.api.collections.page.Pager;
import edu.utah.further.core.api.collections.page.PagerFactory;
import edu.utah.further.core.api.collections.page.PagingProvider;
import edu.utah.further.core.api.collections.page.PagingStrategy;
import edu.utah.further.core.api.context.Labeled;
import edu.utah.further.core.api.message.ValidationUtil;

/**
 * A default implementation of a pager factory. Can be wired with a collection of provider
 * classes. Each such class contains pager factory methods, which must implement
 * {@link PagingProvider}.
 * <p>
 * Like Camel, we might expand this implementation in the future to include other method
 * signatures (e.g. without a {@link PagingStrategy} parameter) using reflection.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Sep 9, 2010
 * @see https://jira.chpc.utah.edu/browse/FUR-1278
 */
public class DefaultPagerFactory implements PagerFactory
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(DefaultPagerFactory.class);

	// ========================= FIELDS ====================================

	/**
	 * A lookup table that matches an pager type and a particular iterable object type to
	 * a factory method that creates appropriate iterators.
	 */
	private final Map<PagingProviderKey, PagingProvider<?>> factoryMethods = CollectionUtil
			.newMap();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Register the default paging providers here.
	 */
	public DefaultPagerFactory()
	{
		super();
		addProvider(new PagingProviderList(IterableType.LIST));
		addProvider(new PagingProviderSortedSet(IterableType.SORTED_SET));
	}

	/**
	 * Register the default paging providers here.
	 */
	@PostConstruct
	private void afterPropertiesSet()
	{
		if (log.isDebugEnabled())
		{
			log.debug("factoryMethods " + factoryMethods);
		}
	}

	// ========================= METHODS ===================================

	/**
	 * @param iterable
	 * @param iterableType
	 * @return
	 * @see edu.utah.further.core.api.collections.page.PagerFactory#pager(java.lang.Object,
	 *      edu.utah.further.core.api.context.Labeled)
	 */
	@Override
	public Pager<?> pager(final Object iterable, final Labeled iterableType)
	{
		return pager(iterable, iterableType, PagingStrategy.DEFAULT_PAGE_SIZE);
	}

	/**
	 * @param iterable
	 * @param iterableType
	 * @param pageSize
	 * @return
	 * @see edu.utah.further.core.api.collections.page.PagerFactory#pager(java.lang.Object,
	 *      edu.utah.further.core.api.context.Labeled, int)
	 */
	@Override
	public Pager<?> pager(final Object iterable, final Labeled iterableType,
			final int pageSize)
	{
		ValidationUtil.validateIsTrue(pageSize > 0, "Page size must be positive!");
		return pager(iterable, new DefaultPagingStrategy(iterableType, pageSize));
	}

	/**
	 * Return a paging iterator of some type of an iterable object. Only selected object
	 * types (suitable for result set paging in the FQE/DS flow) are supported.
	 *
	 * @param iterable
	 *            an iterable object (e.g. List, StAX stream)
	 * @param pagingStrategy
	 * @return pager instance
	 * @see edu.utah.further.core.api.collections.page.PagerFactory#pager(java.lang.Object,
	 *      edu.utah.further.core.api.collections.page.PagingStrategy)
	 */
	@Override
	public Pager<?> pager(final Object iterable, final PagingStrategy pagingStrategy)
	{
		// Validate input arguments
		final Labeled iterableType = pagingStrategy.getIterableType();
		ValidationUtil.validateNotNull("iterable type", iterableType);
		ValidationUtil.validateNotNull("iterable", iterable);

		// Look for a provider method
		final PagingProviderKey key = new PagingProviderKey(iterableType,
				iterable.getClass());
		final PagingProviderKey providerKey = getProviderKey(key);
		if (providerKey == null)
		{
			throw new UnsupportedOperationException(
					"Did not find a paging provider (factory method) for key " + key);
		}
		return invokeProvider(providerKey, iterable, pagingStrategy);
	}

	// ========================= GET & SET =================================

	/**
	 * Set a new value for the providers property.
	 *
	 * @param providers
	 *            the providers to set
	 */
	public void setProviders(final List<PagingProvider<?>> providers)
	{
		for (final PagingProvider<?> provider : providers)
		{
			addProvider(provider);
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Add a paging iterator provider class. Adds all methods annotated with
	 * {@link PagingProvider} to {@link #factoryMethods}.
	 *
	 * @param providerClass
	 */
	private void addProvider(final PagingProvider<?> provider)
	{
		// Register all provider's methods annotated with FACTORY_METHOD_ANNOTATION
		if (log.isInfoEnabled())
		{
			log.info("Adding provider " + provider + " for iterable type label "
					+ provider.getIterableType());
		}

		// Attempt to insert into the factory method map; can only safely insert
		// once per key; if not, die
		final PagingProviderKey key = new PagingProviderKey(provider.getIterableType(),
				provider.getObjectType());
		final PagingProviderKey existingKey = getProviderKey(key);
		if (existingKey != null)
		{
			throw new IllegalStateException("Provider method conflict for key " + key
					+ " with existing key " + existingKey);
		}

		factoryMethods.put(key, provider);
	}

	/**
	 * Check if there is a key with a super-class/interface of <code>key.objectType</code>
	 * . If so, return it, otherwise return <code>null</code>.
	 *
	 * @param key
	 *            key to match in {@link #factoryMethods}
	 * @return a key with a super-class/interface of <code>key.objectType</code> exists),
	 *         or <code>null</code>, if it doesn't exist.
	 */
	private PagingProviderKey getProviderKey(final PagingProviderKey key)
	{
		final Class<?> objectType = key.objectType;
		for (final PagingProviderKey existingKey : factoryMethods.keySet())
		{
			if (log.isDebugEnabled())
			{
				log.debug("Is " + objectType + " a generalization of "
						+ existingKey.objectType + "? "
						+ instanceOf(objectType, existingKey.objectType));
			}
			if ((existingKey.iterableTypeLabel.equals(key.iterableTypeLabel))
					&& isGeneralizationOf(objectType, existingKey.objectType))
			{
				return existingKey;
			}
		}
		return null;
	}

	/**
	 * A provider method map key.
	 */
	private static class PagingProviderKey
	{
		// ========================= FIELDS ====================================

		/**
		 * Iterable type. May include several object types.
		 */
		private final String iterableTypeLabel;

		/**
		 * Specific iterable object type.
		 */
		private final Class<?> objectType;

		// ========================= CONSTRUCTORs ==============================

		/**
		 * @param iterableType
		 * @param objectType
		 */
		public PagingProviderKey(final Labeled iterableType, final Class<?> objectType)
		{
			super();
			this.iterableTypeLabel = iterableType.getLabel();
			this.objectType = objectType;
		}

		// ========================= IMPLEMENTATION: Object ====================

		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
					.append("iterableTypeLabel", iterableTypeLabel)
					.append("objectType", objectType)
					.toString();
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public final int hashCode()
		{
			return new HashCodeBuilder()
					.append(iterableTypeLabel)
					.append(objectType)
					.toHashCode();
		}

		/**
		 * Compare two Pairs based on data.
		 *
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public final boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			final PagingProviderKey that = (PagingProviderKey) obj;

			return new EqualsBuilder()
					.append(this.iterableTypeLabel, that.iterableTypeLabel)
					.append(this.objectType, that.objectType)
					.isEquals();
		}
	}

	/**
	 * @param <T>
	 * @param providerKey
	 * @param type
	 * @param iterable
	 * @param pagingStrategy
	 * @return
	 */
	private <T> Pager<?> invokeProvider(final PagingProviderKey providerKey,
			final T iterable, final PagingStrategy pagingStrategy)
	{
		// Invoke provider method. OK to cast to PagingProvider<T> because we already
		// verified via provider.objectType when inserting provider into the map
		final PagingProvider<T> provider = (PagingProvider<T>) factoryMethods
				.get(providerKey);
		if (log.isInfoEnabled())
		{
			log.info("Found provider " + provider + " for " + providerKey);
		}
		return provider.newPager(iterable, pagingStrategy);
	}
}
