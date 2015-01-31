package edu.utah.further.ds.impl.service.query.logic;

import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.api.discrete.HasIdentifier;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.ds.api.service.query.logic.PersistentLinker;
//import edu.utah.further.mpi.link.impl.domain.LinkEntity;

/**
 * A linker implementation which persists the linking structures to a database table.
 *
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
 * @version Oct 15, 2010
 */
@Service("persistentLinker")
public final class LinkerPersistentImpl implements PersistentLinker<String, Long>
{
	// ========================= CONSTANTS ===================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(LinkerPersistentImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * Link Data Access Object for persisting links
	 */
	private Dao linkDao;

	// ========================= IMPL: Linker ==============================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.ds.api.service.query.logic.Linker#link(java.util.List,
	 * java.lang.Comparable, java.lang.Comparable, java.lang.Long, java.util.Map)
	 */
	@Override
	public void link(final List<? extends HasIdentifier<?>> identifiers,
			final String queryId, final Long datasourceId, final Long masterNamespaceId,
			final Map<String, Object> attributes)
	{
		Validate.notNull(identifiers);

		if (log.isDebugEnabled())
		{
			log.debug("Received " + identifiers.size() + " identifiers to link");
		}

		//for (final HasIdentifier<?> identifier : identifiers)
		//{
			//final LinkEntity linkEntity = createLink(queryId, datasourceId,
					//masterNamespaceId, identifier);
//
			//linkDao.save(linkEntity);
//
			//if (log.isDebugEnabled())
			//{
				//log.debug("Created link " + linkEntity);
			//}
//
		//}

		if (log.isDebugEnabled())
		{
			log.debug("Finished creating links for " + identifiers.size() + " identifiers");
		}
	}

	// ========================= GET/SET METHODS =======================

	/**
	 * Return the linkDao property.
	 *
	 * @return the linkDao
	 */
	@Override
	public Dao getLinkDao()
	{
		return linkDao;
	}

	/**
	 * Set a new value for the linkDao property.
	 *
	 * @param linkDao
	 *            the linkDao to set
	 */
	@Override
	public void setLinkDao(final Dao linkDao)
	{
		this.linkDao = linkDao;
	}

	// ========================= PRIVATE METHODS =======================

	/**
	 * @param queryId
	 * @param datasourceId
	 * @param masterNamespaceId
	 * @param identifier
	 * @return
	 */
	//private LinkEntity createLink(final String queryId, final Long datasourceId,
			//final Long masterNamespaceId, final HasIdentifier<?> identifier)
	//{
		//HasIdentifier<?> newIdentifier = identifier;
		//if (instanceOf(identifier.getId(), HasIdentifier.class))
		//{
			//// Handle Composite Key Entities
			//newIdentifier = (HasIdentifier<?>) identifier.getId();
		//}
//
		//// We now have a single identifier
		//final Long personId = getLongValue(newIdentifier.getId());
//
		//final LinkEntity linkEntity = new LinkEntity();
		//linkEntity.setQueryId(queryId);
		//linkEntity.setMasterPersonId(personId);
		//linkEntity.setDatasourceNamespaceId(datasourceId);
		//linkEntity.setMasterNamespaceId(masterNamespaceId);
		//return linkEntity;
	//}

	/**
	 * Because identifier types can vary across entities we need to determine the type and
	 * get the long value for the link entity.
	 *
	 * @param identifier
	 * @return
	 */
	private final Long getLongValue(final Object identifier)
	{
		if (instanceOf(identifier, Long.class))
		{
			return (Long) identifier;
		}
		else if (instanceOf(identifier, BigDecimal.class))
		{
			// Some entities use BigDecimal for their person identifier
			return new Long(((BigDecimal) identifier).longValue());
		}
		else if (instanceOf(identifier, BigInteger.class))
		{
			// Some entities use BigInteger for their person identifier
			return new Long(((BigInteger) identifier).longValue());
		}
		else
		{
			log.error("Unable to get long value for identifier");
			throw new ApplicationException(
					"Unable to get long value from identifier class "
							+ identifier.getClass()
							+ ". If this identifier is a composite identifier, ensure that it implements HasIdentifier. If it's not a composite identifier, consider adding support for converting this type to a Long.");
		}
	}
}
