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
package edu.utah.further.core.camel;

import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.api.xml.XmlNamespace.XML_SCHEMA_INSTANCE;
import static edu.utah.further.core.api.xml.XmlNamespace.XSI_TYPE;
import static edu.utah.further.core.api.xml.XmlUtil.closeTag;
import static edu.utah.further.core.api.xml.XmlUtil.getQualifiedElement;
import static edu.utah.further.core.api.xml.XmlUtil.openTag;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.regex.RegExUtil;

/**
 * Strip an XML message body marshalled by JAXB from its root entity tag namespace
 * declarations. The default namespace of JAXB does not handle all cases for us, so we use
 * this post-processor when we need to.
 * <p>
 * TODO 1: refactor to a chain of post-processors, each one doing one thing (e.g. removing
 * doc header, removing root tag namespace, adding default namespace).
 * <p>
 * TODO 2: what if this is a large XML document? or streamed? we probably want only to
 * read the beginning and end of the document to optimize performance.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 17, 2009
 */
public final class JaxbNamespaceStripper
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(JaxbNamespaceStripper.class);

	// ========================= FIELDS ====================================

	/**
	 * A flag for keeping/removing XML document header.
	 */
	private boolean removeDocumentHeader = true;

	/**
	 * XML namespace to qualify new root element with.
	 */
	private final String newNamespace;

	/**
	 * Root entity name of message XML bodies.
	 */
	private final String superEntity;

	/**
	 * Is XML entity polymorphic (using xsi:type) or not.
	 */
	private final boolean polymorphic;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a root-entity-namespace-stripping translator without entity polymorphism.
	 *
	 * @param newNamespace
	 *            XML namespace to qualify new root element with
	 */
	public static JaxbNamespaceStripper newInstance(final String newNamespace)
	{
		return new JaxbNamespaceStripper(newNamespace, null, false);
	}

	/**
	 * Construct a root-entity-namespace-stripping translator.
	 *
	 * @param newNamespace
	 *            XML namespace to qualify new root element with
	 * @param superEntity
	 *            root entity name of message XML bodies to set in the transformed body.
	 *            Usually the same class or a super-class of the original root XML entity
	 * @param polymorphic
	 *            is XML entity polymorphic (using xsi:type) or not.
	 */
	public static JaxbNamespaceStripper newPolymorphicInstance(final String newNamespace,
			final String superEntity, final boolean polymorphic)
	{
		return new JaxbNamespaceStripper(newNamespace, superEntity, polymorphic);
	}

	/**
	 * Construct a root-entity-namespace-stripping translator.
	 *
	 * @param newNamespace
	 *            XML namespace to qualify new root element with
	 * @param superEntity
	 *            root entity name of message XML bodies to set in the transformed body.
	 *            Usually the same class or a super-class of the original root XML entity
	 * @param polymorphic
	 *            is XML entity polymorphic (using xsi:type) or not.
	 */
	private JaxbNamespaceStripper(final String newNamespace, final String superEntity,
			final boolean polymorphic)
	{
		super();
		this.newNamespace = newNamespace;
		this.superEntity = superEntity;
		this.polymorphic = polymorphic;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the removeDocumentHeader property.
	 *
	 * @return the removeDocumentHeader
	 */
	public boolean isRemoveDocumentHeader()
	{
		return removeDocumentHeader;
	}

	/**
	 * Set a new value for the removeDocumentHeader property.
	 *
	 * @param removeDocumentHeader
	 *            the removeDocumentHeader to set
	 */
	public void setRemoveDocumentHeader(final boolean removeDocumentHeader)
	{
		this.removeDocumentHeader = removeDocumentHeader;
	}

	/**
	 * Return the newNamespace property.
	 *
	 * @return the newNamespace
	 */
	public String getNewNamespace()
	{
		return newNamespace;
	}

	/**
	 * Return the superEntity property.
	 *
	 * @return the superEntity
	 */
	public String getSuperEntity()
	{
		return superEntity;
	}

	/**
	 * Return the polymorphic property.
	 *
	 * @return the polymorphic
	 */
	public boolean isPolymorphic()
	{
		return polymorphic;
	}

	// ========================= METHODS ===================================

	/**
	 * Add namespace and XMLNS declaration and a root tag of the body.
	 * <p>
	 * TODO: optimize this method if it proves slow for long bodies.
	 *
	 * @param rawBody
	 *            message XML body before transformation
	 * @return transformed body
	 */
	public String stripNamespace(final String rawBody)
	{
		String body = rawBody;

		// Remove XML document header
		if (removeDocumentHeader)
		{
			body = body.replaceFirst(
					"<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>",
					EMPTY_STRING);
		}

		// Find the current root element name
		final String regex = "<.\\w*:(\\w*) xmlns:.*=\"http://.*\">";
		final List<String> groups = RegExUtil.capture(body, regex);
		if (groups.size() < 1)
		{
			// No root tag found, do nothing
			return body;
		}
		final String oldEntity = groups.get(0);

		// Replace root element opening tag
		final Map<String, String> attributes = CollectionUtil.newMap();

		// An xsi:type is required only if this oldEntity is a sub-class of newEntity
		// and if oldEntity is polymorphic.
		final String qualifiedOldEntity = getQualifiedElement(newNamespace, oldEntity);
		final String newEntity = polymorphic ? superEntity : qualifiedOldEntity;
		if (polymorphic && !oldEntity.equals(newEntity))
		{
			attributes.put(getQualifiedElement(XML_SCHEMA_INSTANCE, XSI_TYPE),
					qualifiedOldEntity);
		}
		final String newOpenTag = openTag(newEntity, attributes).toString();
		body = body.replaceFirst(regex, newOpenTag);

		body = replaceRootClosingTag(body, oldEntity, newEntity);

		if (log.isDebugEnabled())
		{
			log.debug("Replaced " + oldEntity + " with " + newEntity);
			log.debug("Raw body = " + rawBody);
			log.debug("Stripped body = " + body);
		}
		return body;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param rawBody
	 * @param oldEntity
	 * @param newEntity
	 * @return
	 */
	private String replaceRootClosingTag(final String rawBody, final String oldEntity,
			final String newEntity)
	{
		// Replace root element closing tag
		final String newCloseTag = closeTag(newEntity).toString();
		final String body = rawBody
				.replaceFirst("</\\w*:" + oldEntity + ">", newCloseTag);
		return body;
	}
}