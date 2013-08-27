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
package edu.utah.further.mdr.api.service.uml;

import java.io.InputStream;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.message.SeverityMessageContainer;
import edu.utah.further.mdr.api.domain.uml.UmlModel;

/**
 * An abstraction of XMI model file parsers.
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
@Api
public interface XmiParser
{
	// ========================= METHODS ===================================

	/**
	 * Parse UML model from XMI file.
	 *
	 * @param queryResourceName
	 *            XQuery file resource name
	 * @param xmiResourceName
	 *            XMI file resource name
	 * @return the output UML model
	 */
	UmlModel parse(String queryResourceName, String xmiResourceName);

	/**
	 * Parse UML model from an XMI input stream.
	 *
	 * @param queryResourceName
	 *            XQuery file resource name
	 * @param xmiInputStream
	 *            XMI file resource input stream
	 * @return the output UML model
	 */
	UmlModel parse(String queryResourceName, InputStream xmiInputStream);

	/**
	 * Return a container of messages (usually errors) generated during the last
	 * {@link #parse()} call.
	 *
	 * @return list of parser messages for the last loaded UML file
	 */
	SeverityMessageContainer getMessages();

	/**
	 * Return the options property.
	 *
	 * @return the options
	 */
	XmiParserOptions getOptions();

	/**
	 * Set a new value for the options property.
	 *
	 * @param options
	 *            the options to set
	 */
	void setOptions(XmiParserOptions options);
}