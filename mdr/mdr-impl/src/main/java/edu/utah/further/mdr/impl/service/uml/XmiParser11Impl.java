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
package edu.utah.further.mdr.impl.service.uml;

import static org.slf4j.LoggerFactory.getLogger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xquery.XQException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.message.Severity;
import edu.utah.further.mdr.api.domain.uml.ElementType;
import edu.utah.further.mdr.api.domain.uml.UmlElement;
import edu.utah.further.mdr.api.domain.uml.UmlElementBuilder;
import edu.utah.further.mdr.api.domain.uml.UmlModel;

/**
 * XMI 1.1 parser implementation.
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
 * @version Dec 5, 2008
 * @see http://www.ibm.com/developerworks/library/x-javaxpathapi.html#changed
 */
@Implementation
@Service("xmiParser11")
@Scope(Constants.Scope.PROTOTYPE)
public class XmiParser11Impl extends AbstractXmiParser
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(XmiParser11Impl.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an XMI 1.1 parser.
	 */
	public XmiParser11Impl()
	{
		super();
	}

	// ========================= IMPLEMENTATION: AbstractXmiParserS ========

	/**
	 * @param results
	 * @return
	 * @throws XMLStreamException 
	 * @throws XQException
	 */
	@Override
	protected UmlModel parseResults(final XMLStreamReader results) throws XMLStreamException
	{
		final UmlModel model = (UmlModel) (new UmlElementBuilder("MODEL_ROOT",
				"UML Model", ElementType.MODEL).build());

		if (results.hasNext())
		{
			final Element dts = getRootElement(results);
			final NodeList elements = dts.getChildNodes();
			for (int i = 0; i < elements.getLength(); i++)
			{
				try
				{
					final UmlElement vd = new UmlElementBuilder(
							(Element) elements.item(i)).build();
					model.addChild(vd);
				}
				catch (final Throwable e)
				{
					final String message = e.getMessage();
					addMessage(Severity.ERROR,
							StringUtils.isBlank(message) ? "Failed to build UmlElement: "
									+ e.toString() : message);
				}
			}
		}
		else
		{
			addMessage(Severity.ERROR, "xquery did not return a result set");
		}
		return model;
	}
}