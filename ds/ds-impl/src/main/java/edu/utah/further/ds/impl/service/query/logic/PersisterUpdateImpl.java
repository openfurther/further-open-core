package edu.utah.further.ds.impl.service.query.logic;

import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.collections.page.PagingLoopController;
import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.ds.api.service.query.logic.Persister;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
// TODO: import edu.utah.further.mpi.link.api.domain.Link;

/**
 * Persists new entities to a database using <code>dao.update()</code>. Also checks for
 * duplicates in the entity collections. If a previous collection element with the same ID
 * has already been persisted within the persister's transaction boundaries, the duplicate
 * is ignored, so the first record "wins" the duplicate resolution.
 * <p>
 * If an entity with the same ID has already been persisted in a previous transaction
 * (=result set page), this persister overrides it with its record because of the use of
 * <code>update()</code>.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 4, 2010
 */
@Service("persisterUpdate")
public class PersisterUpdateImpl implements Persister
{
	
	
	/**
	 * A generic data access object to persistent entities.
	 */
	@Autowired
	@Qualifier("inMemoryDao")
	private Dao dao;
	
	/**
	 * A generic data access object to persistent entities.
	 */
	@Autowired
	@Qualifier("linkDao")
	private Dao linkDao;

	/**
	 * Transaction template for more fine grained control over the transaction
	 */
	@Autowired
	@Qualifier("inMemoryTransactionTemplate")
	private TransactionTemplate transactionTemplate;

	// ========================= CONSTANTS =============================
	
	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(PersisterUpdateImpl.class);

	// ========================= IMPL: RequestProcessor ====================
	
	/**
	 * @param entities
	 * @param controller
	 * @return
	 * @see edu.utah.further.ds.api.service.query.logic.Persister#persist(edu.utah.further.core.api.data.Dao,
	 *      java.util.Collection,
	 *      edu.utah.further.core.api.collections.page.PagingLoopController)
	 */
	@Override
	public List<PersistentEntity<?>> persist(final Collection<? extends PersistentEntity<?>> entities,
			final PagingLoopController controller, final ChainRequest request)
	{
		
		final QueryContext childQc = request.getAttribute(QUERY_CONTEXT);
		final String queryId = childQc.getExecutionId();
		
		final int maxResults = controller.getMaxResults();
		final Set<PersistentEntity<?>> distinctEntities = CollectionUtil
				.<PersistentEntity<?>> newSet();
		transactionTemplate.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			public void doInTransactionWithoutResult(
					final TransactionStatus status)
			{
				int entityNum = 0;
				int counter = controller.getResultCount();
				for (final PersistentEntity<?> persistentEntity : entities)
				{
					if (distinctEntities.contains(persistentEntity))
					{
						// Already encountered this ID within the collection. Note: will not
						// detect duplication versus a previous page because dao.update() won't
						// complain and counter will be incremented even though it shouldn't. See
						// FUR-1308.
						if (log.isDebugEnabled())
						{
							log.debug("Found duplicate ID within this page. Ignoring entity #"
									+ entityNum + " ID " + persistentEntity.getId());
						}
					}
					else
					{
						// First-encountered entity with this ID within this page, persist and add
						// to distinctEntities unless it was already saved during a previous page.
						// In that case, ignore our copy.
						counter++;
						if (log.isDebugEnabled())
						{
							log.debug("Persisting entity #" + entityNum + ". Entity counter "
									+ counter);
						}
						//Skip entity if a duplicate is found in the LinkEntity table, add to distinctEntities list if not
						Map<String, Object> linkProperties = CollectionUtil.newMap();
						linkProperties.put("queryId", queryId);
						linkProperties.put("masterPersonId", persistentEntity.getId());
						// TODO:
						//if (linkDao.findByProperties(Link.class, linkProperties).isEmpty()) 
						//{
							dao.update(persistentEntity);
						//}
						//else 
						//{
						//	if (log.isDebugEnabled())
						//	{
						//		log.debug("Found duplicate ID across pages. Ignoring entity #"
						//				+ entityNum + " ID " + persistentEntity.getId() + "in count but updating.");
						//	}
						//	dao.update(persistentEntity);
						//	continue;
						//}
						entityNum++;
						distinctEntities.add(persistentEntity);
						if (counter == maxResults)
						{
							// Reached end of outer paging loop, truncate this page
							if (log.isInfoEnabled())
							{
								log.info("Reached maxResults " + maxResults
										+ " in result set, truncating page");
							}
							break;
						}
					}
				}
			}
		});
		
			// Flush the current page's batch of reads, inserts and updates
			// and release memory
			// Does not however work on the ESB: https://jira.chpc.utah.edu/browse/FUR-1318
			// dao.flush();

		return CollectionUtil.newList(distinctEntities);
		
	}
	
	// ========================= GET/SET =================================

	/**
	 * Set a new value for the dao property.
	 *
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(final Dao dao)
	{
		this.dao = dao;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.ds.api.service.query.logic.Persister#persist(java.util.Collection, edu.utah.further.core.api.collections.page.PagingLoopController)
	 */
	@Override
	public List<PersistentEntity<?>> persist(
			Collection<? extends PersistentEntity<?>> entities,
			PagingLoopController controller)
	{
		// TODO Auto-generated method stub
		return persist(entities, controller, null);
	}
}
