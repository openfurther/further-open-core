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

import static edu.utah.further.core.api.collections.CollectionUtil.newSortedSet;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.SortedSet;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.math.tree.SimpleCompositeVisitor;
import edu.utah.further.mdr.api.domain.uml.CaDsrLocalValueDomain;
import edu.utah.further.mdr.api.domain.uml.UmlClass;
import edu.utah.further.mdr.api.domain.uml.UmlElement;
import edu.utah.further.mdr.api.domain.uml.UmlElementVisitor;
import edu.utah.further.mdr.api.domain.uml.UmlElementVisitorStub;

/**
 * Visits a flat-structure model (in which all classes are direct children of the model
 * element), and converts it into a hierarchical-package model.
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
 * @version Dec 16, 2008
 */
@Implementation
public class UmlModelTraverser extends SimpleCompositeVisitor<UmlElement>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UmlModelTraverser.class);

	// ========================= FIELDS ====================================

	/**
	 * UML model whose elements are visited by this class.
	 */
	private final UmlElement model;

	/**
	 * A list of error messages to append to.
	 */
	SortedSet<UmlElement> elements;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a visitor and visit the element.
	 *
	 * @param model
	 *            UML model whose elements are visited by this class
	 */
	public UmlModelTraverser(final UmlElement model)
	{
		super();
		this.model = model;
	}

	// ========================= METHODS ===================================

	/**
	 * A factory method that builds the hierarchical UML model element. Visits the
	 * original flat model and builds the hierarchical model from it.
	 *
	 * @return hierarchical UML model element
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	public SortedSet<UmlElement> getElements()
	{
		// Create a new model root element
		elements = newSortedSet();

		// Build package structure
		final PackageBuilder packageBuilder = new PackageBuilder();
		packageBuilder.visit(model);

		return elements;
	}

	// ========================= PRIVATE METHODS ===========================

	// ========================= PRIVATE TYPES =============================

	/**
	 * This class visits the flat model and builds a model with the right package
	 * structure and <i>no</i> classes.
	 */
	private class PackageBuilder extends SimpleCompositeVisitor<UmlElement>
	{
		/**
		 * Prevent synthetic accessor emulation.
		 */
		PackageBuilder()
		{
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
			final UmlElementVisitor elementVisitor = new PackageBuilderVisitor();
			element.accept(elementVisitor);
			return null;
		}
	}

	/**
	 * This class visits the flat model and builds a model with the right package
	 * structure and <i>no</i> classes.
	 */
	private class PackageBuilderVisitor extends UmlElementVisitorStub
	{
		/**
		 * Prevent synthetic accessor emulation.
		 */
		PackageBuilderVisitor()
		{
		}

		// ========================= IMPLEMENTATION: UmlElementVisitorStub =====

		/**
		 * Fetch DTS information for a local value domain.
		 *
		 * @param clazz
		 *            load value domain class
		 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitorStub#visit(edu.utah.further.mdr.api.domain.uml.CaDsrLocalValueDomain)
		 */
		@Override
		public void visit(final UmlClass clazz)
		{
			elements.add(clazz);
		}

		 /**
		 * Fetch DTS information for a local value domain.
		 *
		 * @param domain
		 *            load value domain class
		 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitorStub#visit(edu.utah.further.mdr.api.domain.uml.CaDsrLocalValueDomain)
		 */
		@Override
		public void visit(final CaDsrLocalValueDomain domain)
		{
			visit((UmlClass) domain);
		}

		// /**
		// * Add relationship under model root.
		// *
		// * @param relationship
		// * class relationship object
		// * @see
		// edu.utah.further.core.dts.domain.uml.UmlElementVisitor#visit(edu.utah.further.core.dts.domain.uml.Relationship)
		// */
		// @Override
		// public void visit(final Relationship relationship)
		// {
		// // Add relationships under the model root
		// modelRoot.addChild(relationship);
		// }
	}
}
