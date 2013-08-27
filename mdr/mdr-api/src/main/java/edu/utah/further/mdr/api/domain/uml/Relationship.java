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
package edu.utah.further.mdr.api.domain.uml;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.context.Api;

/**
 * A UML class relationship element.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Api
public class Relationship extends UmlElementImpl
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * Class type. Required.
	 */
	private final RelationshipType relationshipType;

	/**
	 * Source class XMI ID. Required.
	 */
	private final String sourceXmiId;

	/**
	 * Target class XMI ID. Required.
	 */
	private final String targetXmiId;

	/**
	 * Source class name. Required.
	 */
	private final String sourceName;

	/**
	 * Target class name. Required.
	 */
	private final String targetName;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a class relationship element.
	 *
	 * @param xmiId
	 * @param name
	 * @param relationshipType
	 * @param sourceXmiId
	 * @param targetXmiId
	 * @param sourceName
	 * @param targetName
	 */
	public Relationship(final String xmiId, final String name,
			final RelationshipType relationshipType, final String sourceXmiId,
			final String targetXmiId, final String sourceName, final String targetName)
	{
		super(xmiId, name, ElementType.RELATIONSHIP);
		this.relationshipType = relationshipType;
		this.sourceXmiId = sourceXmiId;
		this.targetXmiId = targetXmiId;
		this.sourceName = sourceName;
		this.targetName = targetName;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).appendSuper(
				super.toString()).append("relationshipType", relationshipType).append(
				"sourceXmiId", sourceXmiId).append("targetXmiId", targetXmiId).append(
				"sourceName", sourceName).append("targetName", targetName).toString();
	}

	// ========================= IMPLEMENTATION: Visitable<ItemVisitor> ===

	/**
	 * Let a visitor process this item. Part of the Visitor pattern. This calls back the
	 * visitor's <code>visit()</code> method with this item type.
	 *
	 * @param visitor
	 *            item data visitor whose <code>visit()</code> method is invoked
	 */
	@Override
	public void accept(final  UmlElementVisitor visitor)
	{
		visitor.visit(this);
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the relationshipType property.
	 *
	 * @return the relationshipType
	 */
	public RelationshipType getRelationshipType()
	{
		return relationshipType;
	}

	/**
	 * Return the sourceXmiId property.
	 *
	 * @return the sourceXmiId
	 */
	public String getSourceXmiId()
	{
		return sourceXmiId;
	}

	/**
	 * Return the targetXmiId property.
	 *
	 * @return the targetXmiId
	 */
	public String getTargetXmiId()
	{
		return targetXmiId;
	}

	/**
	 * Return the sourceName property.
	 *
	 * @return the sourceName
	 */
	public String getSourceName()
	{
		return sourceName;
	}

	/**
	 * Return the targetName property.
	 *
	 * @return the targetName
	 */
	public String getTargetName()
	{
		return targetName;
	}

	// ========================= PRIVATE METHODS ===========================
}
