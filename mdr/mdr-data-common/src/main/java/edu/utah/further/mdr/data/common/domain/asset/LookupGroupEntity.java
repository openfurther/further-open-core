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
package edu.utah.further.mdr.data.common.domain.asset;

import static edu.utah.further.core.api.collections.CollectionUtil.newSortedSet;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.Final;
import edu.utah.further.mdr.api.domain.asset.LookupValue;

/**
 * A group homologous to an enumerated value. A group has a set of enumerated values. In
 * particular, the group's value set is ordered; see
 * {@link LookupValue#compareTo(LookupValue)}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @resource Mar 19, 2009
 */
@Entity
@Table(name = "LOOKUP_GROUP")
public class LookupGroupEntity implements PersistentEntity<Long>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable resource identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * The unique identifier of this entity.
	 */
	@Id
	@GeneratedValue
	@Column(name = "LOOKUP_GROUP_ID")
	@Final
	private Long id;

	/**
	 * Group label.
	 */
	@Column(name = "LOOKUP_GROUP_LABEL", length = 1000, nullable = true)
	private String label;

	/**
	 * Link to the group's set of values.
	 */
	@OneToMany
	@Sort(type = SortType.NATURAL)
	private SortedSet<LookupValueEntity> valueSet = newSortedSet();

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("id", id)
				.append("label", label);
		return builder.toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#copyFrom(edu.utah.further.mdr.api.domain.asset.Resource)
	 */
	public LookupGroupEntity copyFrom(final LookupGroupEntity other)
	{
		if (other == null)
		{
			return this;
		}

		// Identifier is not copied

		// Deep-copy fields
		this.label = other.getLabel();

		// Deep-copy collection references and deep-copy their elements in this case
		// because it's easy
		this.valueSet = newSortedSet();
		for (final LookupValue lookupValue : other.getValueSet())
		{
			addLookupValue(new LookupValueEntity().copyFrom(lookupValue));
		}

		return this;
	}

	// ========================= IMPLEMENTATION: PersistentEntity ==========

	/**
	 * @return
	 * @see edu.utah.further.mdr.api.domain.asset.Resource#getId()
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * Return the label property.
	 *
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * Set a new value for the label property.
	 *
	 * @param label
	 *            the label to set
	 */
	public void setLabel(final String label)
	{
		this.label = label;
	}

	// ========================= IMPLEMENTATION: LookupGroup ===============

	/**
	 * Return the valueSet property.
	 *
	 * @return the valueSet
	 */
	public SortedSet<LookupValue> getValueSet()
	{
		return CollectionUtil.<LookupValue> newSortedSet(valueSet);
	}

	/**
	 * Set a new value for the valueSet property.
	 *
	 * @param valueSet
	 *            the valueSet to set
	 */
	public void setValueSet(final SortedSet<LookupValueEntity> valueSet)
	{
		this.valueSet = valueSet;
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public void addLookupValue(final LookupValue e)
	{
		valueSet.add((LookupValueEntity) e);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	public void addLookupValues(final Set<? extends LookupValueEntity> c)
	{
		valueSet.addAll(c);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	public boolean removeLookupValue(final LookupValue o)
	{
		return valueSet.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#removeAll(java.util.Collection)
	 */
	public boolean removeLookupValues(final Collection<? extends LookupValue> c)
	{
		return valueSet.removeAll(c);
	}

	// ========================= PRIVATE METHODS ===========================
}
