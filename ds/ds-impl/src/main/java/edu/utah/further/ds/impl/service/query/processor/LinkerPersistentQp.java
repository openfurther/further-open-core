package edu.utah.further.ds.impl.service.query.processor;

import static edu.utah.further.ds.api.util.AttributeName.FUTURES_LINK;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_RESULT;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.discrete.HasIdentifier;
import edu.utah.further.core.chain.AbstractDelegatingUtilityProcessor;
import edu.utah.further.core.util.concurrent.NamedThreadFactory;
import edu.utah.further.ds.api.service.query.logic.PersistentLinker;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * The linker query processor responsible is for invoking the linkage process required to
 * create the linkage structure needed for performing union and intersections of data.
 * <p>
 * This class spawns a new thread so that a new transaction can be opened without
 * affecting outer transactions and worrying about nested transaction support.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Oct 14, 2010
 */
public final class LinkerPersistentQp extends
		AbstractDelegatingUtilityProcessor<PersistentLinker<String, Long>>
{
	// ========================= CONSTANTS ===================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(LinkerPersistentQp.class);

	// ========================= FIELDS =================================

	/**
	 * A generic data access object to persistent entities.
	 */
	private Dao dao;

	/**
	 * Transaction template for more fine grained control over the transaction
	 */
	private TransactionTemplate transactionTemplate;

	/**
	 * The master namespace identifier
	 */
	private Long masterNamespaceId;

	/**
	 * The thread pool size.
	 */
	private int poolSize = 10;

	/**
	 * The thread pool.
	 */
	private ExecutorService pool = Executors.newFixedThreadPool(poolSize,
			new NamedThreadFactory("Linker-"));

	// ========================= Impl: RequestHandler ======================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.
	 * api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);

		List<? extends HasIdentifier<?>> identifiers = null;
		try
		{
			identifiers = request.getAttribute(QUERY_RESULT);
		}
		catch (final ClassCastException e)
		{
			log.error("A class cast exception occurred while trying to cast "
					+ request.getAttribute(QUERY_RESULT).toString() + " to a List");
			throw e;
		}

		final String queryId = queryContext.getExecutionId();
		final Long datasourceId = queryContext.getTargetNamespaceId();

		getDelegate().setLinkDao(dao);

		if (log.isDebugEnabled())
		{
			log.debug("Submitting new LinkHandler");
		}

		// This is asynchronous and returns immediately
		final Future<?> future = pool.submit(new LinkHandler(identifiers, queryId,
				datasourceId, request.getAttributes()));

		// Keep track of our futures to join later
		NamedThreadFactory.addFuture(future, request, FUTURES_LINK.getLabel());

		return false;
	}

	// ========================= GET/SET METHODS ==============================

	/**
	 * Return the linkDao property.
	 * 
	 * @return the linkDao
	 */
	public Dao getDao()
	{
		return dao;
	}

	/**
	 * Set a new value for the linkDao property.
	 * 
	 * @param linkDao
	 *            the linkDao to set
	 */
	public void setDao(final Dao linkDao)
	{
		this.dao = linkDao;
	}

	/**
	 * Return the transactionTemplate property.
	 * 
	 * @return the transactionTemplate
	 */
	public TransactionTemplate getTransactionTemplate()
	{
		return transactionTemplate;
	}

	/**
	 * Set a new value for the transactionTemplate property.
	 * 
	 * @param transactionTemplate
	 *            the transactionTemplate to set
	 */
	public void setTransactionTemplate(final TransactionTemplate transactionTemplate)
	{
		this.transactionTemplate = transactionTemplate;
	}

	/**
	 * Return the masterNamespaceId property.
	 * 
	 * @return the masterNamespaceId
	 */
	public Long getMasterNamespaceId()
	{
		return masterNamespaceId;
	}

	/**
	 * Set a new value for the masterNamespaceId property.
	 * 
	 * @param masterNamespaceId
	 *            the masterNamespaceId to set
	 */
	public void setMasterNamespaceId(final Long masterNamespaceId)
	{
		this.masterNamespaceId = masterNamespaceId;
	}

	/**
	 * Return the poolSize property.
	 * 
	 * @return the poolSize
	 */
	public int getPoolSize()
	{
		return poolSize;
	}

	/**
	 * Set a new value for the poolSize property.
	 * 
	 * @param poolSize
	 *            the poolSize to set
	 */
	public void setPoolSize(final int poolSize)
	{
		this.poolSize = poolSize;
		this.pool = Executors.newFixedThreadPool(poolSize);
	}

	// ========================= PKG PRIVATE ==============================

	/**
	 * Inner class for handling the link. In a separate thread so it doesn't affect outer
	 * transactions (e.g. closing an outer transaction by accident).
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
	 * @version Oct 14, 2010
	 */
	private class LinkHandler implements Runnable
	{
		final List<? extends HasIdentifier<?>> identifiers;
		final String queryId;
		final Long datasourceId;
		final Map<String, Object> attributes;

		/**
		 * @param identifiers
		 * @param queryId
		 * @param datasourceId
		 * @param attributes
		 */
		private LinkHandler(final List<? extends HasIdentifier<?>> identifiers,
				final String queryId, final Long datasourceId,
				final Map<String, Object> attributes)
		{
			super();
			this.identifiers = identifiers;
			this.queryId = queryId;
			this.datasourceId = datasourceId;
			this.attributes = attributes;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			try
			{
				transactionTemplate.execute(new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(
							final TransactionStatus status)
					{
						log.debug("Linking " + identifiers.size()
								+ " identifiers for queryId " + queryId);
						getDelegate().link(identifiers, queryId, datasourceId,
								masterNamespaceId, attributes);
					}
				});
			}
			catch (final Exception e)
			{
				log
						.error("An exception occurred in the link persisting thread that could not be rethrown",
								e);
			}

		}
	}

	// ========================= PRIVATE ==============================
}
