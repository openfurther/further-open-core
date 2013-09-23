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
package edu.utah.further.core.test.xml;

import static org.slf4j.LoggerFactory.getLogger;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.slf4j.Logger;
import org.w3c.dom.Node;

/**
 * {@link DifferenceListener} implementation that ignores named elements.
 * 
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
 * @version Sep 23, 2013
 * @see http 
 *      ://stackoverflow.com/questions/1241593/java-how-do-i-ignore-certain-elements-when-
 *      comparing-xml
 */
public class IgnoreNamedElementsDifferenceListener implements DifferenceListener
{

	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(MethodHandles.lookup().lookupClass());

	// ========================= FIELDS =====================================

	/**
	 * The names of elements to ignore.
	 */
	private final List<String> ignoredElements;

	// ========================= CONSTRUCTORS ===============================

	/**
	 * Default constructor with a list of ignored elements.
	 * 
	 * @param ignoredElements
	 */
	public IgnoreNamedElementsDifferenceListener(final List<String> ignoredElements)
	{
		super();
		this.ignoredElements = ignoredElements;
	}

	// ========================= IMPL:DifferenceListener ====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.custommonkey.xmlunit.DifferenceListener#differenceFound(org.custommonkey.xmlunit
	 * .Difference)
	 */
	@Override
	public int differenceFound(final Difference difference)
	{
		if (difference.getId() == DifferenceConstants.TEXT_VALUE_ID
				&& ignoredElements.contains(difference
						.getControlNodeDetail()
						.getNode()
						.getParentNode()
						.getNodeName()))
		{
			return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
		}

		return RETURN_ACCEPT_DIFFERENCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.custommonkey.xmlunit.DifferenceListener#skippedComparison(org.w3c.dom.Node,
	 * org.w3c.dom.Node)
	 */
	@Override
	public void skippedComparison(final Node control, final Node test)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Skipped comparison. Control: " + control + "Test: " + test);
		}
	}

}
