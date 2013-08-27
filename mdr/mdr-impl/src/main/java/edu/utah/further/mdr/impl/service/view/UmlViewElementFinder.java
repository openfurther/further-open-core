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
package edu.utah.further.mdr.impl.service.view;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.mdr.api.domain.uml.CaDsrLocalValueDomain;
import edu.utah.further.mdr.api.domain.uml.UmlElement;
import edu.utah.further.mdr.api.domain.uml.UmlElementVisitorStub;
import edu.utah.further.mdr.api.domain.view.ViewElement;
import edu.utah.further.mdr.api.domain.view.ViewElementFinder;
import edu.utah.further.mdr.api.domain.view.ViewModel;
import edu.utah.further.mdr.api.domain.view.ViewNode;

/**
 * Finds a ViewElement of a ViewModel who's keys are of type {@link UmlElement}. This
 * implementation is designed to handle disconnected graphs.
 *
 * Note: This class is not thread safe.
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
 * @version Aug 17, 2009
 */
public final class UmlViewElementFinder<T extends ViewNode<UmlElement, E, T>, E extends ViewElement>
		implements ViewElementFinder<UmlElement, T, E>
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * The concept we are searching for
	 */
	private DtsConceptId thatDtsConceptId;

	/**
	 * Flag to indicate we found the ViewElement which holds the concept
	 */
	private boolean found;

	// ========================= IMPLEMENTATION: ViewElementFinder =========

	/**
	 * Finds a {@link ViewElement} given a {@link ViewModel} and a {@link DtsConceptId}.
	 * Returns null if not found.
	 *
	 * @param viewModel
	 * @param conceptId
	 * @return view element
	 * @see edu.utah.further.mdr.api.domain.view.ViewElementFinder#findViewElementByDtsConceptId(edu.utah.further.mdr.api.domain.view.ViewModel,
	 *      edu.utah.further.dts.api.to.DtsConceptId)
	 */
	@Override
	public E findViewElementByDtsConceptId(final ViewModel<UmlElement, T, E> viewModel,
			final DtsConceptId conceptId)
	{
		this.thatDtsConceptId = conceptId;
		final Map<UmlElement, T> nodeMap = viewModel.getNodeMap();
		final Set<Entry<UmlElement, T>> nodeEntries = nodeMap.entrySet();
		for (final Entry<UmlElement, T> entry : nodeEntries)
		{
			if (!found)
			{
				final UmlElement umlElement = entry.getKey();
				final CaDsrLocalValueDomainVisitor visitor = new CaDsrLocalValueDomainVisitor();
				umlElement.accept(visitor);
			}
			else
			{
				return entry.getValue().getViewElement();
			}

		}
		// Not found
		return null;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Inner class for visiting only {@link CaDsrLocalValueDomain}'s to find the
	 * {@link ViewElement} which contains the given {@link DtsConceptId}.
	 */
	private class CaDsrLocalValueDomainVisitor extends UmlElementVisitorStub
	{
		/**
		 * @param visitable
		 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitorStub#visit(edu.utah.further.mdr.api.domain.uml.CaDsrLocalValueDomain)
		 */
		@Override
		public void visit(final CaDsrLocalValueDomain visitable)
		{
			final DtsConceptId thisDtsConceptId = visitable.getConceptId();
			if (thatDtsConceptId.equals(thisDtsConceptId))
			{
				found = true;
			}
		}
	}
}
