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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.mdr.api.service.uml.XmiParserOptions;

/**
 * XMI parser options bean implementation.
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
 */
@Component("xmiParserOptions")
@Scope(Constants.Scope.PROTOTYPE)
public class XmiParserOptionsImpl implements XmiParserOptions
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(XmiParserOptionsImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * If <code>true</code>, information messages are added for UML elements that do not
	 * adhere to naming conventions. By default, this flag is <code>true</code>.
	 */
	private boolean checkNamingConventions = true;

	/**
	 * If <code>true</code>, results are printed to the screen, otherwise the model is
	 * constructed.
	 */
	private boolean debug = false;

	// ========================= DEPENDENCIES ==============================

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	// ========================= IMPLEMENTATION: XmiParserOptions ==========

	/**
	 * Return the debug property.
	 *
	 * @return the debug
	 */
	@Override
	public boolean isDebug()
	{
		return debug;
	}

	/**
	 * Return the checkNamingConventions property.
	 *
	 * @return the checkNamingConventions
	 */
	@Override
	public boolean isCheckNamingConventions()
	{
		return checkNamingConventions;
	}

	/**
	 * Set a new value for the checkNamingConventions property.
	 *
	 * @param checkNamingConventions
	 *            the checkNamingConventions to set
	 */
	@Override
	public void setCheckNamingConventions(final boolean checkNamingConventions)
	{
		this.checkNamingConventions = checkNamingConventions;
	}

	/**
	 * Set a new value for the debug property.
	 *
	 * @param debug
	 *            the debug to set
	 */
	@Override
	public void setDebug(final boolean debug)
	{
		this.debug = debug;
	}
}