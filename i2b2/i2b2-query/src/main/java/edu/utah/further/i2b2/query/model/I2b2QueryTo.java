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
package edu.utah.further.i2b2.query.model;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.xml.jaxb.adapter.SingleSpaceNormalizedStringAdapter;
import edu.utah.further.core.xml.jaxb.adapter.TrimmedNormalizedStringAdapter;

/**
 * I2B2 Query Model Transfer Object used for binding XML and transferring it to the FQE.
 * 
 * <strong>This implementation is not thread safe.</strong>
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
 * @version Aug 19, 2009
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = I2b2QueryModelConstants.QUERY_DEFINITION, namespace = I2b2QueryModelConstants.NAMESPACE)
public class I2b2QueryTo implements I2b2Query
{
	// ========================= FIELDS ====================================

	/**
	 * Identifier of the user that created this query.
	 */
	@XmlElement(name = I2b2QueryModelConstants.USER)
	@XmlJavaTypeAdapter(TrimmedNormalizedStringAdapter.class)
	private String userId;

	/**
	 * List of panels to AND into the i2b2 query search criteria.
	 */
	@XmlElement(name = I2b2QueryModelConstants.PANEL)
	private List<I2b2QueryModelAnd> andList;

	// ========================= IMPL: Object ==============================

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("queryGroups",
				andList).toString();
	}

	// ========================= IMPL: I2b2Query ===========================

	/**
	 * Return the userId property.
	 * <p>
	 * TODO: replace by delegation to a user object per FUR-865.
	 * 
	 * @return the userId
	 */
	@Override
	public String getUserId()
	{
		return userId;
	}

	/**
	 * Set a new value for the userId property.
	 * <p>
	 * TODO: replace by delegation to a user object per FUR-865.
	 * 
	 * @param userId
	 *            the userId to set
	 */
	@Override
	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	/**
	 * @return
	 * @see edu.utah.further.i2b2.query.model.I2b2Query#getQueryGroups()
	 */
	@Override
	public List<I2b2QueryGroup> getQueryGroups()
	{
		return CollectionUtil.<I2b2QueryGroup> newList(this.getAndList());
	}

	/**
	 * Return the andList property.
	 * 
	 * @return the andList
	 */
	private List<I2b2QueryModelAnd> getAndList()
	{
		return andList;
	}

	/**
	 * Set a new value for the andList property.
	 * 
	 * @param andList
	 *            the andList to set
	 */
	@SuppressWarnings("unused")
	private void setAndList(final List<I2b2QueryModelAnd> andList)
	{
		this.andList = andList;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * A group of items for a query.
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	private static final class I2b2QueryModelAnd implements I2b2QueryGroup
	{
		@XmlElement(name = I2b2QueryModelConstants.TOTAL_ITEM_OCCURRENCES)
		private int occurrences;

		@XmlElement(name = I2b2QueryModelConstants.INVERT)
		private boolean inverted;

		@XmlElement(name = I2b2QueryModelConstants.ITEM)
		private List<I2b2QueryModelOr> orList;

		/**
		 * @return
		 * @see edu.utah.further.i2b2.query.model.I2b2QueryGroup#getQueryItems()
		 */
		@Override
		public List<I2b2QueryItem> getQueryItems()
		{
			return CollectionUtil.<I2b2QueryItem> newList(this.getOrList());
		}

		/**
		 * Return the occurrences property.
		 * 
		 * @return the occurrences
		 */
		@Override
		public int getOccurrences()
		{
			return occurrences;
		}

		/**
		 * Set a new value for the occurrences property.
		 * 
		 * @param occurrences
		 *            the occurrences to set
		 */
		@SuppressWarnings("unused")
		private void setOccurrences(final int occurrences)
		{
			this.occurrences = occurrences;
		}

		/**
		 * Return the inverted property.
		 * 
		 * @return the inverted
		 */
		@Override
		public boolean isInverted()
		{
			return this.inverted;
		}

		/**
		 * Set a new value for the inverted property.
		 * 
		 * @param inverted
		 *            the inverted to set
		 */
		@SuppressWarnings("unused")
		private void setInverted(final int inverted)
		{
			switch (inverted)
			{
				case 0:
				{
					this.inverted = false;
					break;
				}
				case 1:
				{
					this.inverted = true;
					break;
				}
				default:
				{
					this.inverted = false;
					break;
				}
			}
		}

		/**
		 * Return the orList property.
		 * 
		 * @return the orList
		 */
		private List<I2b2QueryModelOr> getOrList()
		{
			return orList;
		}

		/**
		 * Set a new value for the orList property.
		 * 
		 * @param orList
		 *            the orList to set
		 */
		@SuppressWarnings("unused")
		private void setOrList(final List<I2b2QueryModelOr> orList)
		{
			this.orList = orList;
		}

		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
					.append("occurrences", getOccurrences())
					.append("invert", isInverted())
					.append("items", getOrList())
					.toString();
		}

	}

	/**
	 * The item of a query
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	private static final class I2b2QueryModelOr implements I2b2QueryItem
	{
		@XmlElement(name = I2b2QueryModelConstants.HLEVEL)
		private int hlevel;

		@XmlElement(name = I2b2QueryModelConstants.ITEM_NAME)
		private String itemName;

		/**
		 * The item key in the i2b2 database.
		 * <p>
		 * Note: the applied JAXB adapter reduces consecutive space sequences to a single
		 * space, which assumes that all keys in the i2b2 database do not have consecutive
		 * space sequences.
		 */
		@XmlElement(name = I2b2QueryModelConstants.ITEM_KEY)
		@XmlJavaTypeAdapter(SingleSpaceNormalizedStringAdapter.class)
		private String itemKey;

		@XmlElement(name = I2b2QueryModelConstants.TOOLTIP)
		private String toolTip;

		@XmlElement(name = I2b2QueryModelConstants.CLASS)
		private String clazz;

		@XmlElement(name = I2b2QueryModelConstants.CONSTRAIN_BY_DATE)
		private I2b2QueryModelDateConstraint constrainByDate;

		@XmlElement(name = I2b2QueryModelConstants.ITEM_ICON)
		private String itemIcon;

		@XmlElement(name = I2b2QueryModelConstants.ITEM_IS_SYNONYM)
		private String itemIsSynonym;

		@XmlElement(name = I2b2QueryModelConstants.CONSTRAIN_BY_VALUE, nillable = true)
		private I2b2QueryModelValueConstraint constrainByValue;

		/**
		 * Return the hlevel property.
		 * 
		 * @return the hlevel
		 */
		@Override
		public int getHlevel()
		{
			return hlevel;
		}

		/**
		 * Set a new value for the hlevel property.
		 * 
		 * @param hlevel
		 *            the hlevel to set
		 */
		@SuppressWarnings("unused")
		private void setHlevel(final int hlevel)
		{
			this.hlevel = hlevel;
		}

		/**
		 * Return the itemName property.
		 * 
		 * @return the itemName
		 */
		@Override
		public String getItemName()
		{
			return itemName;
		}

		/**
		 * Set a new value for the itemName property.
		 * 
		 * @param itemName
		 *            the itemName to set
		 */
		@SuppressWarnings("unused")
		private void setItemName(final String itemName)
		{
			this.itemName = itemName;
		}

		/**
		 * Return the itemKey property.
		 * 
		 * @return the itemKey
		 */
		@Override
		public String getItemKey()
		{
			return itemKey;
		}

		/**
		 * Set a new value for the itemKey property.
		 * 
		 * @param itemKey
		 *            the itemKey to set
		 */
		@SuppressWarnings("unused")
		private void setItemKey(final String itemKey)
		{
			this.itemKey = itemKey;
		}

		/**
		 * Return the toolTip property.
		 * 
		 * @return the toolTip
		 */
		@Override
		public String getToolTip()
		{
			return toolTip;
		}

		/**
		 * Set a new value for the toolTip property.
		 * 
		 * @param toolTip
		 *            the toolTip to set
		 */
		@SuppressWarnings("unused")
		private void setToolTip(final String toolTip)
		{
			this.toolTip = toolTip;
		}

		/**
		 * Return the clazz property.
		 * 
		 * @return the clazz
		 */
		@Override
		public String getClazz()
		{
			return clazz;
		}

		/**
		 * Set a new value for the clazz property.
		 * 
		 * @param clazz
		 *            the clazz to set
		 */
		@SuppressWarnings("unused")
		private void setClazz(final String clazz)
		{
			this.clazz = clazz;
		}

		/**
		 * Return the constrainByDate property.
		 * 
		 * @return the constrainByDate
		 */
		@Override
		public I2b2QueryModelDateConstraint getConstrainByDate()
		{
			return constrainByDate;
		}

		/**
		 * Set a new value for the constrainByDate property.
		 * 
		 * @param constrainByDate
		 *            the constrainByDate to set
		 */
		@SuppressWarnings("unused")
		private void setConstrainByDate(final I2b2QueryModelDateConstraint constrainByDate)
		{
			this.constrainByDate = constrainByDate;
		}

		/**
		 * Return the itemIcon property.
		 * 
		 * @return the itemIcon
		 */
		@Override
		public String getItemIcon()
		{
			return itemIcon;
		}

		/**
		 * Set a new value for the itemIcon property.
		 * 
		 * @param itemIcon
		 *            the itemIcon to set
		 */
		@SuppressWarnings("unused")
		private void setItemIcon(final String itemIcon)
		{
			this.itemIcon = itemIcon;
		}

		/**
		 * Return the itemIsSynonym property.
		 * 
		 * @return the itemIsSynonym
		 */
		@Override
		public String getItemIsSynonym()
		{
			return itemIsSynonym;
		}

		/**
		 * Set a new value for the itemIsSynonym property.
		 * 
		 * @param itemIsSynonym
		 *            the itemIsSynonym to set
		 */
		@SuppressWarnings("unused")
		private void setItemIsSynonym(final String itemIsSynonym)
		{
			this.itemIsSynonym = itemIsSynonym;
		}

		/**
		 * Return the constrainByValue property.
		 * 
		 * @return the constrainByValue
		 */
		@Override
		public I2b2QueryModelValueConstraint getConstrainByValue()
		{
			return constrainByValue;
		}

		/**
		 * Set a new value for the constrainByValue property.
		 * 
		 * @param constrainByValue
		 *            the constrainByValue to set
		 */
		@SuppressWarnings("unused")
		private void setConstrainByValue(
				final I2b2QueryModelValueConstraint constrainByValue)
		{
			this.constrainByValue = constrainByValue;
		}

		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
					.append("hlevel", getHlevel())
					.append("item_name", getItemName())
					.append("item_key", getItemKey())
					.append("tooltip", getToolTip())
					.append("class", getClazz())
					.append("constrain_by_date", getConstrainByDate())
					.append("item_icon", getItemIcon())
					.append("item_is_synonym", getItemIsSynonym())
					.append("constrain_by_value", constrainByValue)
					.toString();
		}

	}

	/**
	 * Date constraint range of the request
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	private static final class I2b2QueryModelDateConstraint implements
			I2b2QueryDateConstraint
	{
		@XmlElement(name = I2b2QueryModelConstants.DATE_FROM)
		@XmlJavaTypeAdapter(DateNoTimeZoneAdapter.class)
		private Date dateFrom;

		@XmlElement(name = I2b2QueryModelConstants.DATE_TO)
		@XmlJavaTypeAdapter(DateNoTimeZoneAdapter.class)
		private Date dateTo;

		/**
		 * Return the dateFrom property.
		 * 
		 * @return the dateFrom
		 */
		@Override
		public Date getDateFrom()
		{
			return dateFrom;
		}

		/**
		 * Set a new value for the dateFrom property.
		 * 
		 * @param dateFrom
		 *            the dateFrom to set
		 */
		@SuppressWarnings("unused")
		private void setDateFrom(final Date dateFrom)
		{
			this.dateFrom = dateFrom;
		}

		/**
		 * Return the dateTo property.
		 * 
		 * @return the dateTo
		 */
		@Override
		public Date getDateTo()
		{
			return dateTo;
		}

		/**
		 * Set a new value for the dateTo property.
		 * 
		 * @param dateTo
		 *            the dateTo to set
		 */
		@SuppressWarnings("unused")
		private void setDateTo(final Date dateTo)
		{
			this.dateTo = dateTo;
		}

		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
					.append("date_from", getDateFrom())
					.append("date_to", getDateTo())
					.toString();
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static final class I2b2QueryModelValueConstraint implements
			I2b2QueryValueConstraint
	{
		@XmlElement(name = I2b2QueryModelConstants.VALUE_TYPE)
		private I2b2ValueType valueType;

		@XmlElement(name = I2b2QueryModelConstants.VALUE_UNIT_OF_MEASURE, nillable = true)
		private String valueUnitOfMeasure;

		@XmlElement(name = I2b2QueryModelConstants.VALUE_OPERATOR)
		private I2b2ValueOperator valueOperator;

		@XmlElement(name = I2b2QueryModelConstants.VALUE_CONSTRAINT)
		private String valueConstraint;

		/**
		 * Return the valueType property.
		 * 
		 * @return the valueType
		 */
		@Override
		public I2b2ValueType getValueType()
		{
			return valueType;
		}

		/**
		 * Set a new value for the valueType property.
		 * 
		 * @param valueType
		 *            the valueType to set
		 */
		@SuppressWarnings("unused")
		private void setValueType(final I2b2ValueType valueType)
		{
			this.valueType = valueType;
		}

		/**
		 * Return the valueOperator property.
		 * 
		 * @return the valueOperator
		 */
		@Override
		public I2b2ValueOperator getValueOperator()
		{
			return valueOperator;
		}

		/**
		 * Set a new value for the valueOperator property.
		 * 
		 * @param valueOperator
		 *            the valueOperator to set
		 */
		@SuppressWarnings("unused")
		private void setValueOperator(final I2b2ValueOperator valueOperator)
		{
			this.valueOperator = valueOperator;
		}

		/**
		 * Return the valueUnitOfMeasure property.
		 * 
		 * @return the valueUnitOfMeasure
		 */
		@Override
		public String getValueUnitOfMeasure()
		{
			return (valueUnitOfMeasure != null) ? StringUtil.stripNewLinesAndTabs(
					valueUnitOfMeasure).trim() : null;
		}

		/**
		 * Set a new value for the valueUnitOfMeasure property.
		 * 
		 * @param valueUnitOfMeasure
		 *            the valueUnitOfMeasure to set
		 */
		@SuppressWarnings("unused")
		private void setValueUnitOfMeasure(final String valueUnitOfMeasure)
		{
			this.valueUnitOfMeasure = valueUnitOfMeasure;
		}

		/**
		 * Return the valueConstraint property.
		 * 
		 * @return the valueConstraint
		 */
		@Override
		public String getValueConstraint()
		{
			return (valueConstraint != null) ? StringUtil.stripNewLinesAndTabs(
					valueConstraint).trim() : null;
		}

		/**
		 * Set a new value for the valueConstraint property.
		 * 
		 * @param valueConstraint
		 *            the valueConstraint to set
		 */
		@SuppressWarnings("unused")
		private void setValueConstraint(final String valueConstraint)
		{
			this.valueConstraint = valueConstraint;
		}

		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
					.append("value_type", getValueType())
					.append("value_unit_of_measure", getValueUnitOfMeasure())
					.append("value_operator", getValueOperator())
					.append("value_constraint", getValueConstraint())
					.toString();
		}

	}

}
