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
package edu.utah.further.dts.api.to;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.collections.CollectionUtil.newMap;
import static edu.utah.further.core.api.collections.CollectionUtil.newSet;
import static edu.utah.further.core.api.message.Messages.unsupportedMessage;
import static edu.utah.further.core.api.message.Messages.unsupportedOperationMessage;
import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static edu.utah.further.dts.api.domain.association.EnumAssociationType.ASSOCIATION;
import static edu.utah.further.dts.api.domain.association.EnumAssociationType.SYNONYM;
import static edu.utah.further.dts.api.domain.namespace.DtsDataType.CONCEPT;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.domain.association.DtsSynonym;
import edu.utah.further.dts.api.domain.association.EnumAssociationType;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.concept.DtsProperty;
import edu.utah.further.dts.api.domain.concept.DtsPropertyImpl;
import edu.utah.further.dts.api.domain.namespace.DtsData;

/**
 * A DTS concept implementation and a transfer object.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------<br>
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 8, 2008
 */
@Implementation
// ============================
// JAXB annotations
// ============================
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XmlNamespace.DTS, name = DtsConceptToImpl.ENTITY_NAME, propOrder =
{ "hasChildren", "namespaceId", "matchString", "numSpecifiedConceptAssociations",
		"numSpecifiedInverseConceptAssociations", "numSpecifiedProperties",
		"numSpecifiedSynonyms", "fetchedHasSubs", "fetchedHasSups", "fetchedPrimitive",
		"numSpecifiedRoles", "numSpecifiedInverseRoles", "properties",
		"fetchedConceptAssociations", "fetchedInverseConceptAssociations",
		"fetchedSynonyms" })
@XmlRootElement(namespace = XmlNamespace.DTS, name = DtsConceptToImpl.ENTITY_NAME)
public class DtsConceptToImpl extends AbstractDtsDataTo implements DtsConceptTo
{
	// ========================= CONSTANTS =================================

	/**
	 * JAXB name of this entity.
	 */
	static final String ENTITY_NAME = "conceptTo";

	// ========================= FIELDS ====================================

	/**
	 * Data fields: ListComposite<DtsConcept>.
	 */

	/**
	 * Does this concept have children.
	 */
	@XmlElement(name = "hasChildren", nillable = false)
	private boolean hasChildren;

	/**
	 * Data fields: Concept.
	 */

	@XmlElement(name = "namespaceId", required = false)
	private int namespaceId;

	@XmlElement(name = "matchString", required = false)
	private String matchString;

	@XmlElement(name = "numSpecifiedConceptAssociations", required = false)
	private int numSpecifiedConceptAssociations;

	@XmlElement(name = "numSpecifiedInverseConceptAssociations", required = false)
	private int numSpecifiedInverseConceptAssociations;

	@XmlElement(name = "numSpecifiedProperties", required = false)
	private int numSpecifiedProperties;

	@XmlElement(name = "numSpecifiedSynonyms", required = false)
	private int numSpecifiedSynonyms;

	/**
	 * Data fields: Ontylog Concept.
	 */

	@XmlElement(name = "fetchedHasSubs", required = false)
	private boolean fetchedHasSubs;

	@XmlElement(name = "fetchedHasSups", required = false)
	private boolean fetchedHasSups;

	@XmlElement(name = "fetchedPrimitive", required = false)
	private boolean fetchedPrimitive;

	@XmlElement(name = "numSpecifiedRoles", required = false)
	private int numSpecifiedRoles;

	@XmlElement(name = "numSpecifiedInverseRoles", required = false)
	private int numSpecifiedInverseRoles;

	/**
	 * Associations to other entities.
	 */

	@XmlRootElement(name = "properties")
	private static class PropertyMap implements Named
	{
		@XmlTransient
		private String name;

		private Set<String> propertyNames = newSet();

		@XmlElement(name = "property")
		private List<DtsPropertyImpl> properties = newList();

		@Override
		public String getName()
		{
			return name;
		}

		public List<DtsPropertyImpl> getProperties()
		{
			return newList(properties);
		}

		public void clear()
		{
			propertyNames.clear();
			properties.clear();
		}

		public void addAll(List<DtsProperty> list)
		{
			for (DtsProperty property : list)
			{
				propertyNames.add(property.getName());
				this.properties.add((DtsPropertyImpl) property);
			}
		}

		public List<DtsPropertyImpl> get(String propertyName)
		{
			List<DtsPropertyImpl> list = newList();
			if (properties.contains(new DtsPropertyImpl(propertyName, null)))
			{
				for (DtsPropertyImpl property : properties)
				{
					list.add(property);
				}
			}
			return list;
		}

	}

	/**
	 * Concept's property map. Keyed and sorted by property name. Copied from
	 * {@link #concept} and cached.
	 */
	@XmlElement(name = "properties")
	private final PropertyMap properties = new PropertyMap();

	/**
	 * Concept's associations to other concepts.
	 */
	@XmlElementWrapper(name = "associations")
	@XmlElement(name = "association", required = false)
	private final List<DtsAssociationToImpl> fetchedConceptAssociations = newList();

	/**
	 * Concept's inverse associations to other concepts.
	 */
	@XmlElementWrapper(name = "inverseAssociations")
	@XmlElement(name = "association", required = false)
	private final List<DtsAssociationToImpl> fetchedInverseConceptAssociations = newList();

	/**
	 * Concept's synonym list.
	 */
	@XmlElementWrapper(name = "synonyms")
	@XmlElement(name = "synonym", required = false)
	private final List<DtsSynonymToImpl> fetchedSynonyms = newList();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Required for JAXB.
	 */
	public DtsConceptToImpl()
	{
		super(CONCEPT);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @param o
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object o)
	{
		// return namespace.equals(arg0);
		if (o == null)
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}

		// Works only because this method is final!!
		// if (getClass() != o.getClass())
		if (!ReflectionUtil.instanceOf(o, DtsConceptToImpl.class))
		{
			return false;
		}

		final DtsConceptToImpl that = (DtsConceptToImpl) o;
		return new EqualsBuilder()
				.append(getNamespaceId(), that.getNamespaceId())
				.append(getName(), that.getName())
				.isEquals();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder()
				.append(getNamespaceId())
				.append(getName())
				.toHashCode();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).append("namespaceId",
				getNamespaceId()).append("name", getName()).toString();
	}

	// ========================= IMPLEMENTATION: CopyableFrom ==============

	/**
	 * @param other
	 * @return
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public DtsConceptToImpl copyFrom(final DtsData other)
	{
		if (other == null)
		{
			return this;
		}
		super.copyFrom(other);

		if (ReflectionUtil.instanceOf(other, DtsConcept.class))
		{
			final DtsConcept that = (DtsConcept) other;

			this.hasChildren = that.getHasChildren();
			this.matchString = that.getMatchString();
			this.namespaceId = that.getNamespaceId();
			this.numSpecifiedConceptAssociations = that
					.getNumSpecifiedConceptAssociations();
			this.numSpecifiedInverseConceptAssociations = that
					.getNumSpecifiedInverseConceptAssociations();
			this.numSpecifiedProperties = that.getNumSpecifiedProperties();
			this.numSpecifiedSynonyms = that.getNumSpecifiedSynonyms();

			this.fetchedHasSubs = that.getFetchedHasSubs();
			this.fetchedHasSups = that.getFetchedHasSups();
			this.fetchedPrimitive = that.getFetchedPrimitive();
			this.numSpecifiedRoles = that.getNumSpecifiedRoles();
			this.numSpecifiedInverseRoles = that.getNumSpecifiedInverseRoles();

			// Deep-copy associations fields
			setProperties(that.getProperties());
			setFetchedConceptAssociations(that.getFetchedConceptAssociations());
			setFetchedInverseConceptAssociations(that
					.getFetchedInverseConceptAssociations());
			setFetchedSynonyms(that.getFetchedSynonyms());
		}
		return this;
	}

	// ========================= IMPLEMENTATION: DtsCompositeTo ============

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getChildren()
	 */
	@Override
	public List<DtsConcept> getChildren()
	{
		throw new ApplicationException(unsupportedOperationMessage("getChildren()"));
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsData#getHasChildren()
	 */
	@Override
	public boolean getHasChildren()
	{
		return hasChildren;
	}

	/**
	 * @param hasChildren
	 * @see edu.utah.further.dts.api.to.DtsDataTo#setHasChildren(boolean)
	 */
	@Override
	public void setHasChildren(final boolean hasChildren)
	{
		this.hasChildren = hasChildren;
	}

	// ========================= IMPLEMENTATION: DtsConcept ================

	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#addConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// public boolean addConceptAssociation(final ConceptAssociation arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#addInverseConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// public boolean addInverseConceptAssociation(final ConceptAssociation arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.attribute.PropertiedObject#addProperty(com.apelon.dts.client.attribute.DTSProperty)
	// */
	// public boolean addProperty(final DTSProperty arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#addSynonym(com.apelon.dts.client.association.Synonym)
	// */
	// public boolean addSynonym(final Synonym arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#containsConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// public boolean containsConceptAssociation(final ConceptAssociation arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#containsInverseConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// public boolean containsInverseConceptAssociation(final ConceptAssociation arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.attribute.PropertiedObject#containsProperty(com.apelon.dts.client.attribute.DTSProperty)
	// */
	// public boolean containsProperty(final DTSProperty arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#containsSynonym(com.apelon.dts.client.association.Synonym)
	// */
	// public boolean containsSynonym(final Synonym arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#deleteConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// public boolean deleteConceptAssociation(final ConceptAssociation arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#deleteInverseConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// public boolean deleteInverseConceptAssociation(final ConceptAssociation arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.attribute.PropertiedObject#deleteProperty(com.apelon.dts.client.attribute.DTSProperty)
	// */
	// public boolean deleteProperty(final DTSProperty arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#deleteSynonym(com.apelon.dts.client.association.Synonym)
	// */
	// public boolean deleteSynonym(final Synonym arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getFetchedConceptAssociations()
	 */
	@Override
	public List<DtsAssociation> getFetchedConceptAssociations()
	{
		return CollectionUtil.<DtsAssociation> newList(fetchedConceptAssociations);
	}

	/**
	 * Set a new value for the fetchedConceptAssociations property.
	 * 
	 * @param fetchedConceptAssociations
	 *            the fetchedConceptAssociations to set
	 */
	@Override
	public void setFetchedConceptAssociations(
			final List<DtsAssociation> fetchedConceptAssociations)
	{
		setAssociations(ASSOCIATION, this.fetchedConceptAssociations,
				fetchedConceptAssociations, false);
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getFetchedInverseConceptAssociations()
	 */
	@Override
	public List<DtsAssociation> getFetchedInverseConceptAssociations()
	{
		return CollectionUtil.<DtsAssociation> newList(fetchedInverseConceptAssociations);
	}

	/**
	 * Set a new value for the fetchedInverseConceptAssociations property.
	 * 
	 * @param fetchedInverseConceptAssociations
	 *            the fetchedInverseConceptAssociations to set
	 */
	@Override
	public void setFetchedInverseConceptAssociations(
			final List<DtsAssociation> fetchedInverseConceptAssociations)
	{
		setAssociations(ASSOCIATION, this.fetchedInverseConceptAssociations,
				fetchedInverseConceptAssociations, true);
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedSynonyms()
	 */
	@Override
	public List<DtsSynonym> getFetchedSynonyms()
	{
		return CollectionUtil.<DtsSynonym> newList(fetchedSynonyms);
	}

	/**
	 * Set a new value for the fetchedSynonyms property.
	 * 
	 * @param fetchedSynonyms
	 *            the fetchedSynonyms to set
	 */
	@Override
	public void setFetchedSynonyms(final List<DtsSynonym> fetchedSynonyms)
	{
		setAssociations(SYNONYM, this.fetchedSynonyms, fetchedSynonyms, true);
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getFetchedPreferredTerm()
	 */
	@Override
	public DtsSynonym getFetchedPreferredTerm()
	{
		throw new ApplicationException(
				unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	}

	//
	// /**
	// * @return
	// * @see com.apelon.dts.client.attribute.PropertiedObject#getFetchedProperties()
	// */
	// public List<DtsProperty> getFetchedProperties()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.DTSConcept#getMatchItemType()
	// */
	// public MatchItemType getMatchItemType()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getMatchString()
	 */
	@Override
	public String getMatchString()
	{
		return matchString;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#getNamespaceId()
	 */
	@Override
	public int getNamespaceId()
	{
		return namespaceId;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getNumSpecifiedConceptAssociations()
	 */
	@Override
	public int getNumSpecifiedConceptAssociations()
	{
		return numSpecifiedConceptAssociations;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getNumSpecifiedInverseConceptAssociations()
	 */
	@Override
	public int getNumSpecifiedInverseConceptAssociations()
	{
		return numSpecifiedInverseConceptAssociations;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.attribute.PropertiedObject#getNumSpecifiedProperties()
	 */
	@Override
	public int getNumSpecifiedProperties()
	{
		return numSpecifiedProperties;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getNumSpecifiedSynonyms()
	 */
	@Override
	public int getNumSpecifiedSynonyms()
	{
		return numSpecifiedSynonyms;
	}

	/**
	 * @return
	 * @see edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject#getProperties()
	 */
	@Override
	public Map<String, List<DtsProperty>> getProperties()
	{
		Map<String, List<DtsProperty>> propertyMap = newMap();
		for (DtsProperty property : properties.getProperties())
		{
			if (!propertyMap.containsKey(property.getName()))
			{
				propertyMap.put(property.getName(),
						CollectionUtil.<DtsProperty> newList());
			}
			propertyMap.get(property.getName()).add(property);
		}
		return CollectionUtil.<String, List<DtsProperty>> newMap(propertyMap);
	}

	/**
	 * Set a new value for the properties property.
	 * 
	 * @param properties
	 *            the properties to set
	 * @param properties
	 * @see edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject#setProperties(java.util.Map)
	 */
	public void setProperties(final Map<String, List<DtsProperty>> properties)
	{
		this.properties.clear();
		for (final Entry<String, List<DtsProperty>> entry : properties.entrySet())
		{
			this.properties.addAll(entry.getValue());
		}
	}

	/**
	 * Return a property by name.
	 * 
	 * @param propertyName
	 *            property name
	 * @return corresponding property of this object; if not found, returns
	 *         <code>null</code>
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public List<DtsProperty> getProperty(final String propertyName)
	{
		List<DtsProperty> dtsProperties = newList();
		for (DtsPropertyImpl dpi : properties.get(propertyName))
		{
			dtsProperties.add(dpi);
		}
		return dtsProperties;
	}

	/**
	 * Return a property value by name.
	 * 
	 * @param propertyName
	 *            property name
	 * @return corresponding property value; if the property not found, returns
	 *         <code>null</code>
	 * @see edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject#getPropertyValue(java.lang.String)
	 */
	@Override
	public List<String> getPropertyValue(final String propertyName)
	{
		List<String> propertyValues = CollectionUtil.newList();
		if (propertyName != null)
		{
			List<DtsPropertyImpl> property = properties.get(propertyName);
			for (DtsProperty prop : property)
			{
				if (prop.getName().equals(propertyName))
				{
					propertyValues.add(prop.getValue());
				}
			}
		}
		return propertyValues;
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.common.DTSObject#setNamespaceId(int)
	 */
	@Override
	public void setNamespaceId(final int namespaceId)
	{
		this.namespaceId = namespaceId;
	}

	/**
	 * @param numSpecifiedConceptAssociations
	 * @see com.apelon.dts.client.concept.DTSConcept#setNumSpecifiedConceptAssociations(int)
	 */
	@Override
	public void setNumSpecifiedConceptAssociations(
			final int numSpecifiedConceptAssociations)
	{
		this.numSpecifiedConceptAssociations = numSpecifiedConceptAssociations;
	}

	/**
	 * @param numSpecifiedInverseConceptAssociations
	 * @see com.apelon.dts.client.concept.DTSConcept#setNumSpecifiedInverseConceptAssociations(int)
	 */
	@Override
	public void setNumSpecifiedInverseConceptAssociations(
			final int numSpecifiedInverseConceptAssociations)
	{
		this.numSpecifiedInverseConceptAssociations = numSpecifiedInverseConceptAssociations;
	}

	/**
	 * @param numSpecifiedSynonyms
	 * @see com.apelon.dts.client.concept.DTSConcept#setNumSpecifiedSynonyms(int)
	 */
	@Override
	public void setNumSpecifiedSynonyms(final int numSpecifiedSynonyms)
	{
		this.numSpecifiedSynonyms = numSpecifiedSynonyms;
	}

	// public void setProperties(final List<DtsProperty> arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @return
	// * @see
	// edu.utah.further.dts.api.domain.concept.DtsPropertiedObject#getAsDTSPropertiedObject()
	// */
	// public PropertiedObject getAsDTSPropertiedObject()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @return
	// * @see edu.utah.further.dts.api.domain.concept.DtsObject#getAsDTSObject()
	// */
	// public DTSObject getAsDTSObject()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * Set a new value for the matchString property.
	 * 
	 * @param matchString
	 *            the matchString to set
	 */
	@Override
	public void setMatchString(final String matchString)
	{
		this.matchString = matchString;
	}

	/**
	 * Set a new value for the numSpecifiedProperties property.
	 * 
	 * @param numSpecifiedProperties
	 *            the numSpecifiedProperties to set
	 */
	@Override
	public void setNumSpecifiedProperties(final int numSpecifiedProperties)
	{
		this.numSpecifiedProperties = numSpecifiedProperties;
	}

	// ========================= IMPLEMENTATION: DtsConceptTo ==============

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#addDefiningConcept(com.apelon.dts.client.concept.OntylogConcept)
	 */
	@Override
	public boolean addDefiningConcept(final DtsConcept arg0)
	{
		throw new ApplicationException(
				unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	}

	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#addDefiningRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// public boolean addDefiningRole(final DTSRole arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#addInverseRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// public boolean addInverseRole(final DTSRole arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#addRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// public boolean addRole(final DTSRole arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#containsDefiningConcept(com.apelon.dts.client.concept.OntylogConcept)
	 */
	@Override
	public boolean containsDefiningConcept(final DtsConcept arg0)
	{
		throw new ApplicationException(
				unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	}

	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#containsDefiningRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// public boolean containsDefiningRole(final DTSRole arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#containsInverseRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// public boolean containsInverseRole(final DTSRole arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#containsRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// public boolean containsRole(final DTSRole arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#deleteDefiningConcept(com.apelon.dts.client.concept.OntylogConcept)
	 */
	@Override
	public boolean deleteDefiningConcept(final DtsConcept arg0)
	{
		throw new ApplicationException(
				unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	}

	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#deleteDefiningRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// public boolean deleteDefiningRole(final DTSRole arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#deleteInverseRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// public boolean deleteInverseRole(final DTSRole arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#deleteRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// public boolean deleteRole(final DTSRole arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getDefiningRoleGroups()
	// */
	// public List<RoleGroup> getDefiningRoleGroups()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedDefiningConcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedDefiningConcepts()
	{
		throw new ApplicationException(
				unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	}

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedDefiningRoles()
	// */
	// public List<DtsRole> getFetchedDefiningRoles()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedHasSubs()
	 */
	@Override
	public boolean getFetchedHasSubs()
	{
		return fetchedHasSubs;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedHasSups()
	 */
	@Override
	public boolean getFetchedHasSups()
	{
		return fetchedHasSups;
	}

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedInverseRoles()
	// */
	// public List<DtsRole> getFetchedInverseRoles()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }
	//
	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedKind()
	// */
	// public Kind getFetchedKind()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedPrimitive()
	 */
	@Override
	public boolean getFetchedPrimitive()
	{
		return fetchedPrimitive;
	}

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedRoles()
	// */
	// public List<DtsRole> getFetchedRoles()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedSubconcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedSubconcepts()
	{
		throw new ApplicationException(
				unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedSuperconcepts()
	 */
	@Override
	public List<DtsConcept> getFetchedSuperconcepts()
	{
		throw new ApplicationException(
				unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getNumSpecifiedInverseRoles()
	 */
	@Override
	public int getNumSpecifiedInverseRoles()
	{
		return numSpecifiedInverseRoles;
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getNumSpecifiedRoles()
	 */
	@Override
	public int getNumSpecifiedRoles()
	{
		return numSpecifiedRoles;
	}

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getRoleGroups()
	// */
	// public List<RoleGroup> getRoleGroups()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * @param fetchedHasSubs
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedHasSubs(boolean)
	 */
	@Override
	public void setFetchedHasSubs(final boolean fetchedHasSubs)
	{
		this.fetchedHasSubs = fetchedHasSubs;
	}

	/**
	 * @param fetchedHasSups
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedHasSups(boolean)
	 */
	@Override
	public void setFetchedHasSups(final boolean fetchedHasSups)
	{
		this.fetchedHasSups = fetchedHasSups;
	}

	// /**
	// * @param arg0
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#setFetchedKind(com.apelon.dts.client.attribute.Kind)
	// */
	// public void setFetchedKind(final Kind arg0)
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * @param fetchedPrimitive
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedPrimitive(boolean)
	 */
	@Override
	public void setFetchedPrimitive(final boolean fetchedPrimitive)
	{
		this.fetchedPrimitive = fetchedPrimitive;
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedSubconcepts(java.util.List)
	 */
	@Override
	public void setFetchedSubconcepts(final List<? extends DtsConcept> arg0)
	{
		throw new ApplicationException(
				unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	}

	/**
	 * @param arg0
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#setFetchedSuperconcepts(java.util.List)
	 */
	@Override
	public void setFetchedSuperconcepts(final List<? extends DtsConcept> arg0)
	{
		throw new ApplicationException(
				unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	}

	/**
	 * @param numSpecifiedInverseRoles
	 * @see com.apelon.dts.client.concept.OntylogConcept#setNumSpecifiedInverseRoles(int)
	 */
	@Override
	public void setNumSpecifiedInverseRoles(final int numSpecifiedInverseRoles)
	{
		this.numSpecifiedInverseRoles = numSpecifiedInverseRoles;
	}

	/**
	 * @param numSpecifiedRoles
	 * @see com.apelon.dts.client.concept.OntylogConcept#setNumSpecifiedRoles(int)
	 */
	@Override
	public void setNumSpecifiedRoles(final int numSpecifiedRoles)
	{
		this.numSpecifiedRoles = numSpecifiedRoles;
	}

	// /**
	// * @return
	// * @see
	// edu.utah.further.dts.api.domain.concept.DtsConcept#getAsOntylogyConcept()
	// */
	// public OntylogConcept getAsOntylogyConcept()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	// /**
	// * @return
	// * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getAsDTSConcept()
	// */
	// public DTSConcept getAsDTSConcept()
	// {
	// throw new ApplicationException(
	// unsupportedOperationMessage(UNSUPPORTED_OPERATION_MESSAGE));
	// }

	/**
	 * Return a unique identifier view of the concept.
	 * 
	 * @return a unique identifier object corresponding to this object
	 * @see edu.utah.further.dts.api.domain.concept.DtsConcept#getAsUniqueId()
	 */
	@Override
	public DtsConceptUniqueId getAsUniqueId()
	{
		return new DtsConceptUniqueId(getNamespaceId(), getId());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * A factory method of association TOs compatible with this concept TO. A hook for
	 * sub-classes.
	 * 
	 * @param type
	 *            association type
	 * @param fromItemAssociation
	 *            If <code>true</code>, this is an association for which we need to output
	 *            only the "fromItem" link. Otherwise, we output only the "toItem" link.
	 *            Applicable only to associations, not to synonyms. In synonyms, this
	 *            argument is ignored and the value <code>true</code> is always used.
	 * @return a new {@link DtsAssociationToImpl} instance
	 */
	protected DtsAssociationToImpl newDtsAssociationTo(final EnumAssociationType type,
			final boolean fromItemAssociation)
	{
		switch (type)
		{
			case ASSOCIATION:
			{
				return new DtsAssociationToImpl(fromItemAssociation);
			}

			case SYNONYM:
			{
				return new DtsSynonymToImpl();
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(type));
			}
		}
	}

	/**
	 * Set a list of associations in this concept. Note: unchecked warnings are turned off
	 * in this method. Make sure to run it for T,type values such that the T=return type
	 * of <code>newDtsAssociationTo(type, .)</code>.
	 * 
	 * @param <T>
	 *            element type of original list field
	 * @param <S>
	 *            element type of list to be set
	 * @param type
	 * @param list
	 *            concept list to be updated to <code>newList</code>
	 * @param newList
	 *            new value for <code>list</code>
	 * @param fromItemAssociation
	 *            If <code>true</code>, this is a list of association for which we need to
	 *            output only the "fromItem" link. Otherwise, we output only the "toItem"
	 *            link
	 */
	private <T extends DtsAssociationToImpl, S extends DtsAssociation> void setAssociations(
			final EnumAssociationType type, final List<T> list,
			final List<? extends S> newList, final boolean fromItemAssociation)
	{
		list.clear();
		for (final DtsAssociation element : newList)
		{
			list
					.add((T) newDtsAssociationTo(type, fromItemAssociation).copyFrom(
							element));
		}
	}
}
