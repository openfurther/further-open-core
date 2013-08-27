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

import static edu.utah.further.core.api.message.Severity.ERROR;
import static edu.utah.further.mdr.impl.service.util.UmlUtil.updateAncestryMarks;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.message.SeverityMessageContainer;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.util.message.SeverityMessageContainerImpl;
import edu.utah.further.core.math.tree.SimpleCompositeVisitor;
import edu.utah.further.mdr.api.domain.uml.UmlElement;
import edu.utah.further.mdr.api.domain.uml.UmlElementVisitor;
import edu.utah.further.mdr.api.domain.uml.UmlModel;
import edu.utah.further.mdr.api.service.uml.XmiParserOptions;

/**
 * Visits a UML model element tree and builds its element associations, fetches value sets
 * from DTS, etc.
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
class UmlModelIntegrator extends SimpleCompositeVisitor<UmlElement>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UmlModelIntegrator.class);

	// ========================= FIELDS ====================================
	/**
	 * UML model whose elements are visited by this class.
	 */
	private final UmlModel model;

	/**
	 * A list of error messages generated during integration.
	 */
	private SeverityMessageContainer messages;

	/**
	 * XMI parser options to effect.
	 */
	private final XmiParserOptions options;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param model
	 *            UML model whose elements are visited by this class
	 * @param options
	 *            XMI parser options to effect
	 */
	public UmlModelIntegrator(final UmlModel model, final XmiParserOptions options)
	{
		super();
		this.model = model;
		this.options = options;
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
		// Visit an element with a visitor class
		final UmlElementVisitor elementVisitor = new UmlModelVisitor(model, messages,
				options);
		try
		{
			// Element-specific functions
			element.accept(elementVisitor);

			// Update element markers (applies to all elements)
			updateAncestryMarks(element);
		}
		catch (final BusinessRuleException e)
		{
			messages.addMessage(e.getSeverity(), e.getMessage());

			// Update element markers (applies to all elements) here too
			updateAncestryMarks(element);
		}
		catch (final Throwable e)
		{
			String message = e.getMessage();
			if (isBlank(message))
			{
				message = "Failed to load element " + element + ": "
						+ e.getClass().getSimpleName();
			}
			messages.addMessage(ERROR, message);
		}

		// No intermediate results to store
		return null;
	}

	// ========================= METHODS ===================================

	/**
	 * Visit all model elements and set their references as we go.
	 *
	 * @return integration message container
	 * @see edu.utah.further.core.math.tree.SimpleCompositeVisitor#visit(edu.utah.further.core.math.tree.Composite)
	 */
	public SeverityMessageContainer integrate()
	{
		messages = new SeverityMessageContainerImpl();
		if (model != null)
		{
			super.visit(model);
		}
		return messages;
	}

}
