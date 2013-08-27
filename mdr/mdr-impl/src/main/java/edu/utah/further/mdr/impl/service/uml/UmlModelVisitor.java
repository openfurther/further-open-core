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

import static edu.utah.further.core.api.message.Messages.unsupportedMessage;
import static edu.utah.further.core.api.message.Severity.ERROR;
import static edu.utah.further.core.api.message.Severity.INFO;
import static edu.utah.further.core.api.message.Severity.WARNING;
import static edu.utah.further.core.api.message.ValidationUtil.validateIsFalse;
import static edu.utah.further.core.api.message.ValidationUtil.validateIsTrue;
import static edu.utah.further.core.api.text.StringUtil.isCamelCaseString;
import static edu.utah.further.core.api.text.StringUtil.isClassNameString;
import static edu.utah.further.core.api.text.StringUtil.isPackageNameString;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.dts.api.service.DtsOptions.DEFAULT_ALL_ATTRIBUTES;
import static edu.utah.further.mdr.api.domain.uml.ClassType.PRIMITIVE;
import static edu.utah.further.mdr.api.domain.uml.ElementStatus.ACTIVE;
import static edu.utah.further.mdr.api.domain.uml.ElementStatus.ACTIVE_WITH_INFO;
import static edu.utah.further.mdr.api.domain.uml.ElementStatus.IN_PROGRESS;
import static edu.utah.further.mdr.api.domain.uml.TagNames.NONE;
import static edu.utah.further.mdr.api.domain.uml.UmlElementBuilder.isActiveNamespace;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.message.SeverityMessageContainer;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.api.to.DtsConceptId;
import edu.utah.further.mdr.api.domain.uml.CaDsrLocalValueDomain;
import edu.utah.further.mdr.api.domain.uml.Relationship;
import edu.utah.further.mdr.api.domain.uml.RelationshipType;
import edu.utah.further.mdr.api.domain.uml.UmlClass;
import edu.utah.further.mdr.api.domain.uml.UmlElement;
import edu.utah.further.mdr.api.domain.uml.UmlElementVisitorStub;
import edu.utah.further.mdr.api.domain.uml.UmlMember;
import edu.utah.further.mdr.api.domain.uml.UmlModel;
import edu.utah.further.mdr.api.domain.uml.UmlPackage;
import edu.utah.further.mdr.api.service.uml.XmiParserOptions;
import edu.utah.further.mdr.impl.service.util.DtsResourceLocator;

/**
 * Visits model {@link UmlElement}s and sets references to other elements to build the UML
 * model integrity.
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
public class UmlModelVisitor extends UmlElementVisitorStub
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UmlModelVisitor.class);

	// ========================= FIELDS ====================================

	/**
	 * UML model whose elements are visited by this class.
	 */
	private final UmlModel model;

	/**
	 * A list of error messages to append to.
	 */
	private final SeverityMessageContainer messages;

	/**
	 * XMI parser options to effect.
	 */
	private final XmiParserOptions options;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a visitor and visit the element.
	 * 
	 * @param model
	 *            UML model whose elements are visited by this class
	 * @param messages
	 *            A list of error messages to append to.
	 * @param options
	 *            XMI parser options to effect
	 */
	public UmlModelVisitor(final UmlModel model, final SeverityMessageContainer messages,
			final XmiParserOptions options)
	{
		super();
		this.model = model;
		this.messages = messages;
		this.options = options;
	}

	// ========================= IMPLEMENTATION: UmlElementVisitorStub =====

	/**
	 * Determine and set a model's status.
	 * 
	 * @param aModel
	 *            a model element
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitorStub#visit(edu.utah.further.mdr.api.domain.uml.UmlModel)
	 */
	@Override
	public void visit(final UmlModel aModel)
	{
		visit((UmlElement) aModel);
		aModel.setStatus(ACTIVE);
	}

	/**
	 * Determine and set a package's status.
	 * 
	 * @param pkg
	 *            a class
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitorStub#visit(edu.utah.further.mdr.api.domain.uml.CaDsrLocalValueDomain)
	 */
	@Override
	public void visit(final UmlPackage pkg)
	{
		visit((UmlElement) pkg);
		pkg.setStatus(ACTIVE);

		if (options.isCheckNamingConventions()
				&& !isPackageNameString(pkg.getSimpleName()))
		{
			messages.addMessage(INFO, "Package " + quote(pkg.getName())
					+ " name does not adhere to naming conventions");
			pkg.setStatus(ACTIVE_WITH_INFO);
		}
	}

	/**
	 * Set a member's class type reference.
	 * 
	 * @param member
	 *            class member
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitorStub#visit(edu.utah.further.mdr.api.domain.uml.UmlMember)
	 */
	@Override
	public void visit(final UmlMember member)
	{
		visit((UmlElement) member);

		final String memberClassName = member.getMemberClassName();
		final String errorMessagePrefix = "Member " + quote(member.getQualifiedName())
				+ ": type";
		if (NONE.equals(memberClassName))
		{
			messages.addMessage(WARNING, errorMessagePrefix + " class "
					+ quote(memberClassName) + " ignored");
			member.setStatus(IN_PROGRESS);
		}
		else
		{
			final UmlClass memberClass = findUmlClassByName(memberClassName,
					errorMessagePrefix);
			member.setMemberClass(memberClass);
			if (log.isDebugEnabled())
			{
				log.debug("Set member " + quote(member.getQualifiedName()) + " type to "
						+ member.getMemberClass());
			}
			member.setStatus(ACTIVE);

			if (options.isCheckNamingConventions()
					&& !isCamelCaseString(member.getName()))
			{
				messages.addMessage(INFO, "Member " + quote(member.getQualifiedName())
						+ " name does not adhere to naming conventions");
				member.setStatus(ACTIVE_WITH_INFO);
			}
		}
	}

	/**
	 * Determine and set a class' status.
	 * 
	 * @param clazz
	 *            a class
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitorStub#visit(edu.utah.further.mdr.api.domain.uml.CaDsrLocalValueDomain)
	 */
	@Override
	public void visit(final UmlClass clazz)
	{
		visit((UmlElement) clazz);

		clazz.getAllMembers(); // Must not throw an exception

		// CADSR-LVD requires extra validation that might worsen the status, but so far
		// class is OK
		clazz.setStatus(ACTIVE);

		// Check naming conventions only for non-primitive classes
		if (options.isCheckNamingConventions() && (clazz.getClassType() != PRIMITIVE)
				&& !isClassNameString(clazz.getName()))
		{
			messages.addMessage(INFO, "Class " + quote(clazz.getQualifiedName())
					+ " name does not adhere to naming conventions");
			clazz.setStatus(ACTIVE_WITH_INFO);
		}

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
		// Must call the super-class visit method for the integrator to work!
		visit((UmlClass) domain);

		// Parse only active namespaces = mapped domains
		final DtsConceptId conceptId = domain.getConceptId();
		if (isActiveNamespace(conceptId.getNamespace()))
		{
			// Set the concept
			DtsConcept concept = null;
			final String errorMessage = "Could not find DTS concept for domain class "
					+ quote(domain.getName()) + " in namespace "
					+ quote(conceptId.getNamespace()) + ", property name "
					+ quote(conceptId.getPropertyName()) + " property value "
					+ quote(conceptId.getPropertyValue());
			try
			{
				concept = getDtsOperationService().findConceptById(conceptId,
						DEFAULT_ALL_ATTRIBUTES);
			}
			catch (final BusinessRuleException e)
			{
				throw new BusinessRuleException(errorMessage + ": " + e.getMessage());
			}
			validateIsTrue(ERROR, concept != null, errorMessage);
			domain.setConcept(concept);

			// Set the value set
			final List<DtsConcept> children = getDtsOperationService().getChildren(
					conceptId);
			// TODO: add message severity level to validation
			validateIsTrue(WARNING, !children.isEmpty(),
					"Value domain set is empty for domain class "
							+ quote(domain.getName()) + " in namespace "
							+ quote(conceptId.getNamespace()) + ", property name "
							+ quote(conceptId.getPropertyName()) + " property value "
							+ quote(conceptId.getPropertyValue()));
			domain.setValueSet(children);

			if (log.isDebugEnabled())
			{
				log.debug("Set value set of " + quote(domain.getName()) + " to "
						+ children);
			}
		}
	}

	/**
	 * Set relationship references in the source and target classes.
	 * 
	 * @param relationship
	 *            class relationship object
	 * @see edu.utah.further.mdr.api.domain.uml.UmlElementVisitor#visit(edu.utah.further.mdr.api.domain.uml.Relationship)
	 */
	@Override
	public void visit(final Relationship relationship)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Effecting relationship: " + relationship);
		}
		final RelationshipType relationshipType = relationship.getRelationshipType();
		switch (relationshipType)
		{
			case GENERALIZATION:
			{
				visitGeneralizationRelationship(relationship);
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(relationshipType));
			}
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Set a generalization relationship (class SOURCE extends class TARGET) references in
	 * the source class.
	 * 
	 * @param relationship
	 */
	private void visitGeneralizationRelationship(final Relationship relationship)
	{
		// Example of a generalization connector element:
		// <connector type="GENERALIZATION">
		// <source>PharmaceuticalOrder</source>
		// <target>Order</target>
		// </connector>

		// Some argument validation checks
		final String source = relationship.getSourceXmiId();
		final String target = relationship.getTargetXmiId();
		validateIsFalse(source.equals(target), "A class cannot extend itself");

		final UmlClass sourceClass = findClassByXmiId(relationship, relationship
				.getSourceXmiId());
		final UmlClass targetClass = findClassByXmiId(relationship, relationship
				.getTargetXmiId());

		if ((sourceClass == null) || (targetClass == null))
		{
			final String errorMessagePrefix = "In " + relationship + ", ";
			if (sourceClass == null)
			{
				messages.addMessage(ERROR, errorMessagePrefix + " sub-class "
						+ quote(relationship.getSourceName()) + " XMI ID "
						+ relationship.getSourceXmiId() + " was not found");
			}
			if (targetClass == null)
			{
				messages.addMessage(ERROR, errorMessagePrefix + " super-class "
						+ quote(relationship.getTargetName()) + " XMI ID "
						+ relationship.getTargetXmiId() + " was not found");
			}
		}
		else
		{
			// Arguments OK, set target as the superclass of source
			sourceClass.setSuperClass(targetClass);
			relationship.setStatus(ACTIVE);
		}
	}

	/**
	 * @param xmiId
	 * @return
	 */
	private UmlClass findClassByXmiId(final Relationship relationship, final String xmiId)
	{
		try
		{
			return (UmlClass) UmlElementFinder.findElementById(model, xmiId);
		}
		catch (final Throwable e)
		{
			messages.addMessage(WARNING, "Tried to effect "
					+ relationship.getRelationshipType()
					+ " relationship, but class with XMI ID " + quote(xmiId)
					+ " was not found");
			return null;
		}
	}

	/**
	 * @param className
	 * @param errorMessagePrefix
	 * @return
	 */
	private UmlClass findUmlClassByName(final String className,
			final String errorMessagePrefix)
	{
		// Searching for classes (almost) for each element visited potentially
		// has quadratic complexity. TODO: make sure it scales well with UML model size.
		final List<UmlClass> classList = model.getClassByName(className);
		validateIsTrue(classList.size() > 0, errorMessagePrefix + " class "
				+ quote(className) + " was not found");
		validateIsTrue(classList.size() < 2, errorMessagePrefix
				+ " name clash: multiple classes with the same name " + quote(className)
				+ " were found in were found in different packages");
		final UmlClass clazz = classList.get(0);
		validateIsTrue(clazz != null, errorMessagePrefix + " class " + quote(className)
				+ " not found");
		return clazz;
	}

	/**
	 * @return
	 */
	private DtsOperationService getDtsOperationService()
	{
		return DtsResourceLocator.getInstance().getDtsOperationService();
	}
}
