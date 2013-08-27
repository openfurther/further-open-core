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

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.context.Constants;

/**
 * Centralizes XMI tag names read from the XMI file and used to create class instances of
 * this package.
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
 * @version Nov 11, 2008
 */
@Api
@Constants
public abstract class TagNames
{
	// ========================= CONSTANTS: PACKAGES =======================

	/**
	 * Main class package name.
	 */
	public static final String PACKAGE_MAIN_CLASS = "utah";

	/**
	 * Name of package in which Value Domain classes reside.
	 */
	public static final String PACKAGE_VALUE_DOMAINS = "ValueDomains";

	// ========================= CONSTANTS: ATTRIBUTES =====================

	public static final String XMI_ID = "xmi-id";
	public static final String PARENT_XMI_ID = "parent-xmi-id";
	public static final String NAME = "name";
	public static final String TYPE = "element-type";

	// ========================= CONSTANTS: ELEMENTS =======================

	// UML Element and model properties
	public static final String MODEL = "model";
	public static final String ELEMENT = "element";
	public static final String CONNECTOR = "connector";
	public static final String DOCUMENTATION = "dmt";

	// UmlClass properties
	public static final String CLASS_TYPE = "class-type";
	public static final String SUPER_CLASS_NAME = "super-class-name";
	public static final String PACKAGES = "packages";
	public static final String PACKAGE = "package";

	// CaDsrLocalValueDomain properties
	public static final String NAMESPACE = "namespace";
	public static final String PROPERTY_NAME = "property-name";
	public static final String PROPERTY_VALUE = "property-value";
	public static final String SUB_PROPERTY_NAME = "sub-property-name";
	public static final String SUB_PROPERTY_VALUE = "sub-property-value";

	// UmlMember properties
	public static final String MEMBER_CLASS_NAME = "member-class-name";

	// Connector properties
	public static final String RELATIONSHIP_TYPE = "relationship-type";
	public static final String SOURCE = "source";
	public static final String TARGET = "target";

	// ========================= CONSTANTS: ELEMENT VALUES =================

	/**
	 * Indicates that a CA DSR LVD is required by caDSR to be annotated, but does not yet
	 * have a corresponding concept in DTS. Can be used by other elements to indicate no
	 * value.
	 */
	public static final String NONE = "None";
}
