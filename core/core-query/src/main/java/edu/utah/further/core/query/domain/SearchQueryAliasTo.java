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
package edu.utah.further.core.query.domain;

import static org.slf4j.LoggerFactory.getLogger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;

import edu.utah.further.core.api.lang.CopyableFrom;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A {@link SearchQuery} alias transfer object implementation
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
 * @version May 23, 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "key", "value" })
@XmlRootElement(namespace = XmlNamespace.CORE_QUERY, name = SearchQueryAliasTo.ENTITY_NAME)

public class SearchQueryAliasTo implements SearchQueryAlias,
		CopyableFrom<SearchQueryAlias, SearchQueryAliasTo>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(SortCriterionTo.class);

	/**
	 * XML name of this entity.
	 */
	static final String ENTITY_NAME = "alias";
	
	// ========================= FIELDS ====================================

	/**
	 * Alias object type
	 */
	@XmlAttribute
	private String associationObject;

	/**
	 * Alias key
	 */
	@XmlElement
	private String key;

	/**
	 * Alias value
	 */
	@XmlElement
	private String value;

	// ========================= CONSTRUCTORS ====================================

	/**
	 * Default constructor
	 */
	public SearchQueryAliasTo()
	{
	}

	/**
	 * Constructor with all elements
	 * 
	 * @param object
	 * @param key
	 * @param value
	 */
	public SearchQueryAliasTo(final String object, final String key, final String value)
	{
		super();
		this.associationObject = object;
		this.key = key;
		this.value = value;
	}

	/**
	 * A copy-constructor.
	 * 
	 * @param other
	 *            other object to deep-copy fields from
	 */
	public static SearchQueryAliasTo newCopy(final SearchQueryAlias other)
	{
		return new SearchQueryAliasTo().copyFrom(other);
	}

	// ========================= IMPL: CopyableFrom ====================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.api.lang.CopyableFrom#copyFrom(java.lang.Object)
	 */
	@Override
	public SearchQueryAliasTo copyFrom(final SearchQueryAlias other)
	{
		this.associationObject = other.getAssociationObject();
		this.key = other.getKey();
		this.value = other.getValue();

		return this;
	}

	// ========================= GET/SET ====================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.query.domain.SearchQueryAlias#getObject()
	 */
	@Override
	public String getAssociationObject()
	{
		return associationObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.query.domain.SearchQueryAlias#setObject(java.lang.String)
	 */
	@Override
	public void setAssociationObject(final String object)
	{
		this.associationObject = object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.query.domain.SearchQueryAlias#getKey()
	 */
	@Override
	public String getKey()
	{
		return key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.query.domain.SearchQueryAlias#setKey(java.lang.String)
	 */
	@Override
	public void setKey(final String key)
	{
		this.key = key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.query.domain.SearchQueryAlias#getValue()
	 */
	@Override
	public String getValue()
	{
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.core.query.domain.SearchQueryAlias#setValue(java.lang.String)
	 */
	@Override
	public void setValue(final String value)
	{
		this.value = value;
	}

}
