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
package edu.utah.further.core.xml.xpath;

import static edu.utah.further.core.api.lang.CoreUtil.preventUtilityConstruction;
import static javax.xml.xpath.XPathConstants.NODESET;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.InputStream;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathConstants;

import org.slf4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Utility;
import edu.utah.further.core.api.xml.transform.DocumentSerializer;
import edu.utah.further.core.api.xml.transform.SimpleDocumentSerializer;
import edu.utah.further.core.api.xml.transform.XmlTransformUtil;

/**
 * Utility methods for common XQuery operations. All methods wrap standard XQuery
 * exceptions with unchecked exceptions.
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
 * @version Sep 10, 2009
 */
@Utility
public final class XPathUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(XPathUtil.class);

	/**
	 * For XML node printouts.
	 */
	private static final DocumentSerializer XML_SERIALIZER = new SimpleDocumentSerializer();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Hide constructor in utility class.
	 */
	private XPathUtil()
	{
		preventUtilityConstruction();
	}

	// ========================= METHODS ===================================

	/**
	 * @param inputStream
	 * @param xpathExpression
	 * @param nsContext
	 * @return
	 */
	public static String getNodeSubTreeAsString(final InputStream inputStream,
			final String xpathExpression, final NamespaceContext nsContext)
	{
		final NodeList nodes = executeXPathExpression(inputStream, xpathExpression,
				nsContext);

		// Marshal the node tree to an XML string, if the node tree is found. If not,
		// return null.
		if (nodes.getLength() == 0)
		{
			return null;
		}
		final Node node = nodes.item(0);
		return (node == null) ? null : XmlTransformUtil.printToString(node,
				XML_SERIALIZER);
	}

	/**
	 * @param inputStream
	 * @param xpathExpression
	 * @return
	 */
	public static NodeList executeXPathExpression(final InputStream inputStream,
			final String xpathExpression, final NamespaceContext nsContext)
	{
		return (NodeList) new XPathParser(xpathExpression, nsContext).evaluateXPath(
				inputStream, NODESET);
	}

	/**
	 * Executes the namespace aware xpath expression on the given node. Similar to
	 * {@link #executeXPathExpression(InputStream, String, NamespaceContext)} but uses
	 * Node as input and returns an actual collection of {@link Node}s
	 * 
	 * @param xpathExpression
	 * @param namespaceContext
	 * @param document
	 * @return
	 */
	public static final List<Node> executeXPathExpression(final Node node,
			final String xpathExpression, final NamespaceContext namespaceContext)
	{
		final NodeList nodeList = (NodeList) new XPathParser(xpathExpression,
				namespaceContext).evaluateXPath(node, XPathConstants.NODESET);

		return toListNode(nodeList);
	}

	/**
	 * Converts a {@link NodeList} to a {@link List} of nodes.
	 * 
	 * @param nodeList
	 * @return
	 */
	public static final List<Node> toListNode(final NodeList nodeList)
	{
		final List<Node> actualNodeList = CollectionUtil.newList();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			actualNodeList.add(nodeList.item(i));
		}

		return actualNodeList;
	}

}
