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

import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.core.api.xml.XmlNamespace.XML_SCHEMA_INSTANCE;
import static edu.utah.further.core.api.xml.XmlNamespace.XML_SCHEMA_INSTANCE_NAMESPACE;
import static edu.utah.further.core.api.xml.XmlUtil.fullTag;
import static edu.utah.further.core.api.xml.XmlUtil.getQualifiedElement;
import static edu.utah.further.core.api.xml.XmlUtil.getQualifiedNamespace;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Wrap a data source response concatenation with a root entity tag.
 * <p>
 * TODO: replace by automatically reading the namespace and the root tag name by
 * reflection of JAXB annotations of the entity. Pass the entity class instead to this
 * object.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Jul 3, 2009
 */
public final class JaxbRootTagTranslator implements Processor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(JaxbRootTagTranslator.class);

	// ========================= FIELDS ====================================

	/**
	 * Root tag to add at the beginning and end of the message body.
	 */
	private final String rootTag;

	/**
	 * Root entity namespace to add to root tag.
	 */
	private final String namespace;

	/**
	 * Alias to assign to the root entity namespace throughout the XML document.
	 */
	private final String namespaceAlias;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct an aggregated message translator.
	 *
	 * @param rootTag
	 *            root tag to add at the beginning and end of the message body
	 * @param namespace
	 *            root entity namespace to add to root tag
	 * @param namespaceAlias
	 *            alias to assign to the root entity namespace throughout the XML
	 *            document.
	 */
	public JaxbRootTagTranslator(final String rootTag, final String namespace,
			final String namespaceAlias)
	{
		super();
		this.rootTag = rootTag;
		this.namespace = namespace;
		this.namespaceAlias = namespaceAlias;
	}

	// ========================= IMPLEMENTATION: Processor =================

	/**
	 * @param exchange
	 * @throws Exception
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(final Exchange exchange) throws Exception
	{
		if (log.isDebugEnabled())
		{
			log.debug("Adding root tag " + quote(rootTag));
		}
		final Map<String, String> attributes = CollectionUtil.newMap();
		attributes.put(getQualifiedNamespace(namespaceAlias), namespace);
		attributes.put(getQualifiedNamespace(XML_SCHEMA_INSTANCE), XML_SCHEMA_INSTANCE_NAMESPACE);
		final String qualifiedRootTag = getQualifiedElement(namespaceAlias, rootTag);
		final String body = (String) exchange.getIn().getBody();
		final String newDocument = fullTag(qualifiedRootTag, attributes, body).toString();

		if (log.isDebugEnabled())
		{
			log.debug("Outgoing body: " + newDocument);
		}
		CamelUtil.copyHeaders(exchange, exchange);
		exchange.getOut().setBody(newDocument);
	}
}