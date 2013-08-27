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
package edu.utah.further.core.api.constant;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;
import edu.utah.further.core.api.io.SystemProperty;
import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * This interface centralizes string and character constants, labels and names used
 * throughout the application. All strings referring to attributes should of course be
 * different from each other.
 * <p>
 * ---------------------------------------------------------------------------- -------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * ---------------------------------------------------------------------------- -------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 13, 2008
 * @see {@link XmlNamespace}
 */
@Api
@Constants
public abstract class Strings
{
	// ========================= CONSTANTS =================================

	// -----------------------------------------------------------------------
	// Miscellaneous strings
	// -----------------------------------------------------------------------

	/**
	 * White space character.
	 */
	public static final char SPACE_CHAR = ' ';

	/**
	 * Tab character.
	 */
	public static final char TAB_CHAR = '\t';

	/**
	 * A separator between an entity and its property name, e.g. the "." in the HQL
	 * expression "item.data".
	 */
	public static final char PROPERTY_SCOPE_CHAR = '.';

	/**
	 * A separator between an entity and its property name, e.g. the "." in the HQL
	 * expression "item.data".
	 */
	public static final String PROPERTY_SCOPE_STRING = ".";

	/**
	 * Non-null empty string.
	 */
	public static final String EMPTY_STRING = "";

	/**
	 * White space string.
	 */
	public static final String SPACE_STRING = " ";

	/**
	 * New line string. Platform-dependent.
	 */
	public static final String NEW_LINE_STRING = SystemProperty.LINE_SEPARATOR.getValue();


	/**
	 * A UNIX-style newline sequence.
	 */
	public static final String UNIX_NEW_LINE_STRING = "\r\n";

	/**
	 * Tab character as string.
	 */
	public static final String TAB_STRING = "\t";

	/**
	 * Printed in case of a null string.
	 */
	public static final String NULL_TO_STRING = "-"; // "<null>";

	/**
	 * Printed if property is not applicable.
	 */
	public static final String NOT_APPLICABLE = "N/A";

	/**
	 * Printed if property is empty / not applicable.
	 */
	public static final String NONE = "-";

	/**
	 * Printed if an object is yet to be added.
	 */
	public static final String TO_BE_ADDED = "TBA";

	/**
	 * What to display when a password string is to be masked.
	 */
	public static final String MASKED_PASSWORD = "*****";

	/**
	 * A prefix of all message bundle keys that refer to enumerated types.
	 */
	public static final String ENUM_SCOPE = "enum.";

	/**
	 * Default opening bracket (scope) in tree printouts.
	 */
	public static final String SCOPE_OPEN = "{";

	/**
	 * Default closing bracket (scope) in tree printouts.
	 */
	public static final String SCOPE_CLOSE = "}";

	/**
	 * Enumerated-type constant name standard separator. For instance, a name might be
	 * <code>BANK_OF_AMERICA</code>.
	 */
	public static final char ENUM_NAME_SEPARATOR_CHAR = '_';

	/**
	 * Enumerated-type constant name standard separator. For instance, a name might be
	 * <code>BANK_OF_AMERICA</code>.
	 */
	public static final String ENUM_NAME_SEPARATOR = "_";

	/**
	 * XML tag name standard separator. For instance, a name might be
	 * <code>bank-of-america</code>.
	 */
	public static final String XML_NAME_SEPARATOR = "-";

	/**
	 * A text quotation symbol.
	 */
	public static final String SINGLE_QUOTE = "'";

	/**
	 * A text quotation symbol.
	 */
	public static final String DOUBLE_QUOTE = "\"";

	/**
	 * Symbol to show at the end of truncated strings.
	 */
	public static final String ELIPSIS = "...";
	/**
	 * Separates parts of a JSF component identifier. For instance, a component's ID may
	 * be "id:key:value".
	 */
	public static final String JSF_SEPARATOR = ":";

	/**
	 * Separates parts of a JAXB context path (package list).
	 */
	public static final String JAXB_PACKAGE_SEPARATOR = ":";

	/**
	 * Default encoding scheme of URL parameters and XML resources.
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	// -----------------------------------------------------------------------
	// Application resource keys & messages used in managers and exception
	// -----------------------------------------------------------------------

	/**
	 * A common prefix for exception message keys. Convention: global message uses the key
	 * "error.ExceptionClassSimpleName" the resource bundle.
	 */
	public static final String EXCEPTION_KEY_PREFIX = "error.";

	/**
	 * ANSI SQL Wildcard character
	 */
	public static final String SQL_WILDCARD = "%";

	// -----------------------------------------------------------------------
	// Arrays and lists
	// -----------------------------------------------------------------------

	// -----------------------------------------------------------------------
	// Files and directories
	// -----------------------------------------------------------------------

	/**
	 * A symbol of a virtual directory.
	 */
	public static final String VIRTUAL_DIRECTORY = "/";
}
