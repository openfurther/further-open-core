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

/**
 * A default empty implementation of a UML element visitor. All <code>visit()</code>
 * methods do nothing. Includes all UML element types from all sub-components. This is
 * basically a <i>stub</i>.
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
 * @version Dec 11, 2008
 */
public class UmlElementVisitorStub implements UmlElementVisitor
{
	// ========================= FIELDS ====================================

	// ========================= IMPLEMENTATION: UmlElementVisitor =========

	/**
	 * @param visitable
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitor#visit(edu.utah.further.mdr.api.domain.uml.UmlClass)
	 */
	@Override
	public void visit(final UmlClass visitable)
	{
		// Method stub
	}

	/**
	 * @param visitable
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitor#visit(edu.utah.further.mdr.api.domain.uml.UmlModel)
	 */
	@Override
	public void visit(final UmlModel visitable)
	{
		// Method stub
	}

	/**
	 * @param visitable
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitor#visit(edu.utah.further.mdr.api.domain.uml.UmlPackage)
	 */
	@Override
	public void visit(final UmlPackage visitable)
	{
		// Method stub
	}

	/**
	 * @param visitable
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitor#visit(edu.utah.further.mdr.api.domain.uml.CaDsrLocalValueDomain)
	 */
	@Override
	public void visit(final CaDsrLocalValueDomain visitable)
	{
		// Method stub
	}

	/**
	 * @param visitable
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitor#visit(edu.utah.further.mdr.api.domain.uml.UmlMember)
	 */
	@Override
	public void visit(final UmlMember visitable)
	{
		// Method stub
	}

	/**
	 * @param visitable
	 * @see edu.utah.further.core.api.visitor.Visitor#visit(edu.utah.further.core.api.visitor.Visitable)
	 */
	@Override
	public void visit(final UmlElement visitable)
	{
		// Method stub
	}

	/**
	 * @param visitable
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitor#visit(edu.utah.further.mdr.api.domain.uml.Relationship)
	 */
	@Override
	public void visit(final Relationship visitable)
	{
		// Method stub
	}

}
