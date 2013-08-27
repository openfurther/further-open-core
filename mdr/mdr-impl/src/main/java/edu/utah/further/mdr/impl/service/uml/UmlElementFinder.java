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

import static edu.utah.further.core.api.message.ValidationUtil.validateNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.math.tree.SimpleCompositeVisitor;
import edu.utah.further.mdr.api.domain.uml.UmlElement;

/**
 * Visits a UML model element tree and matches an element by ID.
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
 * @version Mar 23, 2009
 */
@Implementation
class UmlElementFinder extends SimpleCompositeVisitor<UmlElement>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UmlElementFinder.class);

	// ========================= FIELDS ====================================

	/**
	 * Element unique identifier to match.
	 */
	private final String elementXmiId;

	/**
	 *The element with the specified ID found in the model.
	 */
	private UmlElement elementFound = null;

	private boolean found = false;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param elementXmiId
	 *            Element XMI ID to match
	 */
	private UmlElementFinder(final String elementXmiId)
	{
		super();
		validateNotNull("Cannot search for a null UML element UUID", elementXmiId);
		this.elementXmiId = elementXmiId;
	}

	/**
	 * A factory method.
	 *
	 * @param model
	 * @param elementXmiId
	 * @return element found
	 */
	public static UmlElement findElementById(final UmlElement model,
			final String elementXmiId)
	{
		// if (log.isDebugEnabled())
		// {
		// log.debug("Finding element by XMI ID " + elementXmiId);
		// }
		final UmlElementFinder finder = new UmlElementFinder(elementXmiId);
		finder.visit(model);
		return finder.elementFound;
	}

	// ========================= IMPLEMENTATION: SimpleTreeVisitor =========

	/**
	 * Do this operation at a node after operating on its children. This is a hook.
	 *
	 * @param element
	 *            currently visited element in the UML model tree
	 * @return an optional object containing intermediate processing results
	 * @see edu.utah.further.core.math.tree.SimpleCompositeVisitor#executePost(edu.utah.further.core.math.tree.Composite)
	 */
	@Override
	protected Object executePost(final UmlElement element)
	{
		// if (log.isDebugEnabled())
		// {
		// log.debug("XMI ID = " + element.getXmiId() + " element " + element);
		// }
		if (!found && elementXmiId.equals(element.getXmiId()))
		{
			elementFound = element;
			found = true;
		}
		return null;
	}

	/**
	 * @param thisNode
	 * @return
	 * @see edu.utah.further.core.math.tree.SimpleCompositeVisitor#isProcessChildren(edu.utah.further.core.math.tree.Composite)
	 */
	@Override
	protected boolean isProcessChildren(final UmlElement thisNode)
	{
		return !found;
	}

	// ========================= METHODS ===================================
}
