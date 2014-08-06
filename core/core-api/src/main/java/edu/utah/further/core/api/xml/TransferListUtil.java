/*****************************************************************************************
 * Source File: TransferListUtil.java
 ****************************************************************************************/
package edu.utah.further.core.api.xml;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static edu.utah.further.core.api.text.StringUtil.getClassAsStringNullSafe;

import java.util.List;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.PubliclyCloneable;

/**
 * Utilities involving {@link TransferList}s and copying data from TO lists to persistent
 * entity lists.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Apr 11, 2011
 */
@Utility
public final class TransferListUtil
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private TransferListUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * Retrieve a list of entities from a chain request transfer object during query
	 * processing. This method supports handling Lists of entities as well as a single
	 * entity in the request.
	 * 
	 * @param object
	 *            the Object to persist from the request
	 * @return a list of {@link PersistentEntity}'s to persist
	 */
	public static final List<PersistentEntity<?>> getEntities(final Object object)
	{
		// Return typed collection in order to use Dao
		final List<PersistentEntity<?>> persistentEntities = CollectionUtil.newList();

		if (instanceOf(object, List.class))
		{
			final List<?> objects = List.class.cast(object);
			for (final Object listObj : objects)
			{
				persistentEntities.add(getAsEntity(listObj));
			}
		}
		else if (instanceOf(object, TransferList.class))
		{
			final TransferList<?> transferList = TransferList.class.cast(object);
			return getEntities(transferList.getList());
		}
		else if (instanceOf(object, PersistentEntity.class)
				|| instanceOf(object, PubliclyCloneable.class))
		{
			persistentEntities.add(getAsEntity(object));
		}
		else
		{
			throw new ApplicationException(
					"Unable to persist object: type "
							+ getClassAsStringNullSafe(object)
							+ " is not List<PersistentEntity<?>>, "
							+ "List<PubliclyCloneable<?>>, PubliclyCloneable<?>, PersistentEntity<?> or TransferList<? extends PubliclyCloneable>. Please read the JavaDocs of this class");
		}

		return persistentEntities;
	}

	/**
	 * Private helper method in order to take care of type conversions to
	 * PersistentEntity.
	 * 
	 * @param obj
	 * @return
	 */
	public static final PersistentEntity<?> getAsEntity(final Object obj)
	{
		if (!instanceOf(obj, PersistentEntity.class))
		{
			if (!instanceOf(obj, PubliclyCloneable.class))
			{
				throw new ApplicationException(
						"Unable to persist object: type "
								+ getClassAsStringNullSafe(obj)
								+ " does not implement PersistentEntity or PubliclyCloneable which returns a type of PersistentEntity");
			}
			final Object copy = PubliclyCloneable.class.cast(obj).copy();
			// Need to check if the copy is a PersistentEntity
			if (!instanceOf(copy, PersistentEntity.class))
			{
				throw new ApplicationException(
						"Unable to persist object:  type "
								+ getClassAsStringNullSafe(obj)
								+ " implements PubliclyCloneable but does not return a type of PersistentEntity");
			}
			return PersistentEntity.class.cast(copy);
		}
		return PersistentEntity.class.cast(obj);
	}

	// ========================= PRIVATE METHODS ===========================
}
