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
package edu.utah.further.core.data.hibernate.listeners;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.hibernate.EntityMode;
import org.hibernate.StatelessSession;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;

import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A Hibernate event listener that listens for updates and block updates from overwriting
 * existing data with NULL data. This is primarily used during result federation where a
 * patient from multiple sources is resolved to the same person but one source has less
 * information (a null value) than another source. This also works in reverse, when there
 * is less information stored and an update occurs that provides more information, the
 * null is replaced.
 * 
 * NOTE: This may cause deadlocks if the database does not support nested transactions as
 * well as read uncommited. Prior to this event occurring, a write lock is acquired and
 * thus some databases will require that write lock to be released before updates and
 * reads can occur.
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
 * @version Nov 29, 2011
 */
public class PreUpdatePreventNullOverwriteListener implements PreUpdateEventListener
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(PreUpdatePreventNullOverwriteListener.class);

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -8082513098920827661L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.event.PreUpdateEventListener#onPreUpdate(org.hibernate.event.
	 * PreUpdateEvent)
	 */
	@SuppressWarnings("resource")
	@Override
	public boolean onPreUpdate(final PreUpdateEvent event)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Received pre-update event");
		}
		final EntityPersister entityPersister = event.getPersister();
		final EntityMode entityMode = entityPersister.guessEntityMode(event.getEntity());
		final Connection connection = getConnection(entityPersister);
		final StatelessSession session = entityPersister
				.getFactory()
				.openStatelessSession(connection);
		session.beginTransaction();
		final Serializable entityId = entityPersister.getIdentifier(event.getEntity(),
				(SessionImplementor)session);
		final Object existingEntity = session.get(event.getEntity().getClass(), entityId);

		if (log.isDebugEnabled())
		{
			log.debug("Loaded existing entity " + existingEntity);
		}

		boolean updatedExistingEntity = false;
		for (int i = 0; i < entityPersister.getPropertyNames().length; i++)
		{
			final Object oldPropValue = entityPersister.getPropertyValue(existingEntity,
					i, entityMode);
			if (oldPropValue == null)
			{

				if (log.isDebugEnabled())
				{
					log.debug("Found a property in the existing "
							+ "entity that was null, checking new entity");
				}

				final Object newPropValue = entityPersister.getPropertyValue(
						event.getEntity(), i, entityMode);

				if (!(newPropValue instanceof Collection<?>) && newPropValue != null)
				{

					if (log.isDebugEnabled())
					{
						log.debug("The new entity contains a non-null "
								+ "value, updating existing entity");
					}

					entityPersister.setPropertyValue(existingEntity, i, newPropValue,
							entityMode);
					updatedExistingEntity = true;
				}
			}
		}

		if (updatedExistingEntity)
		{
			entityPersister.getFactory().getCurrentSession().cancelQuery();
			session.update(existingEntity);
		}

		session.getTransaction().commit();
		session.close();

		return updatedExistingEntity;
	}

	/**
	 * @param entityPersister
	 * @return
	 */
	private Connection getConnection(final EntityPersister entityPersister)
	{
		Connection connection;
		try
		{
			connection = entityPersister
					.getFactory()
					.getConnectionProvider()
					.getConnection();
		}
		catch (final SQLException e)
		{
			throw new ApplicationException(
					"Unable to retrieve the connection during PreUpdatePreventNullOverwriteListener");
		}
		return connection;
	}

}
