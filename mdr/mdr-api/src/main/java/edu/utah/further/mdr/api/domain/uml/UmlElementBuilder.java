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

import static edu.utah.further.core.api.constant.Strings.EMPTY_STRING;
import static edu.utah.further.core.api.discrete.EnumUtil.valueOfNullSafe;
import static edu.utah.further.core.api.message.Messages.unsupportedMessage;
import static edu.utah.further.core.api.message.ValidationUtil.validateIsTrue;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.mdr.api.domain.uml.ClassType.PRIMITIVE;
import static edu.utah.further.mdr.api.domain.uml.ElementStatus.ACTIVE;
import static edu.utah.further.mdr.api.domain.uml.ElementStatus.INVALID;
import static edu.utah.further.mdr.api.domain.uml.ElementStatus.IN_PROGRESS;
import static edu.utah.further.mdr.api.domain.uml.TagNames.CLASS_TYPE;
import static edu.utah.further.mdr.api.domain.uml.TagNames.DOCUMENTATION;
import static edu.utah.further.mdr.api.domain.uml.TagNames.MEMBER_CLASS_NAME;
import static edu.utah.further.mdr.api.domain.uml.TagNames.NAME;
import static edu.utah.further.mdr.api.domain.uml.TagNames.NAMESPACE;
import static edu.utah.further.mdr.api.domain.uml.TagNames.NONE;
import static edu.utah.further.mdr.api.domain.uml.TagNames.PROPERTY_NAME;
import static edu.utah.further.mdr.api.domain.uml.TagNames.PROPERTY_VALUE;
import static edu.utah.further.mdr.api.domain.uml.TagNames.RELATIONSHIP_TYPE;
import static edu.utah.further.mdr.api.domain.uml.TagNames.SOURCE;
import static edu.utah.further.mdr.api.domain.uml.TagNames.SUB_PROPERTY_NAME;
import static edu.utah.further.mdr.api.domain.uml.TagNames.SUB_PROPERTY_VALUE;
import static edu.utah.further.mdr.api.domain.uml.TagNames.SUPER_CLASS_NAME;
import static edu.utah.further.mdr.api.domain.uml.TagNames.TARGET;
import static edu.utah.further.mdr.api.domain.uml.TagNames.TYPE;
import static edu.utah.further.mdr.api.domain.uml.TagNames.XMI_ID;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.dts.api.to.DtsConceptId;

/**
 * A builder of a {@link UmlElement}. May return a sub-class of {@link UmlElement}.
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
 * @version Dec 12, 2008
 */
@Api
public class UmlElementBuilder implements Builder<UmlElement>
{
	// ========================= CONSTANTS =================================

	/**
	 * A function pointer for DOM element processing.
	 */
	private static interface ElementProcessor
	{
		void process(Node node, String nodeName, String nodeValue);
	}

	// ========================= FIELDS: REQUIRED ==========================

	/**
	 * XMI element identifier.
	 */
	private final String xmiId;

	/**
	 * Domain class name.
	 */
	private final String name;

	/**
	 * Element type.
	 */
	private final ElementType elementType;

	// ========================= FIELDS: OPTIONAL ==========================

	/**
	 * Parent element element.
	 */
	private UmlElement parent;

	/**
	 * A description of the element.
	 */
	private String documentation;

	/**
	 * Class type. Required.
	 */
	private ClassType classType;

	/**
	 * Class package, e.g.
	 */
	private final UmlPackage pkg = new UmlPackage("PKG", EMPTY_STRING);

	/**
	 * Super-class name. Required.
	 */
	private String superClassName;

	/**
	 * DTS namespace.
	 */
	private String namespace;

	/**
	 * DTS concept property name.
	 */
	private String propertyName;

	/**
	 * DTS concept property value.
	 */
	private String propertyValue;

	/**
	 * DTS concept property name for further search within the
	 * namespace-propertyName-propertyValue-concept-rooted DTS sub-tree
	 */
	@SuppressWarnings("unused")
	private String subPropertyName;

	/**
	 * DTS concept property value.
	 */
	@SuppressWarnings("unused")
	private String subPropertyValue;

	/**
	 * Name of this member's class (type).
	 */
	private String memberClassName;

	/**
	 * A member's owning class.
	 */
	private UmlClass parentClass;

	/**
	 * Class type. Required.
	 */
	private RelationshipType relationshipType;

	/**
	 * Source class XMI ID. Required for a relationship.
	 */
	private String sourceXmiId;

	/**
	 * Target class XMI ID. Required for a relationship.
	 */
	private String targetXmiId;

	/**
	 * Source class name. Required for a relationship.
	 */
	private String sourceName;

	/**
	 * Target class name. Required for a relationship.
	 */
	private String targetName;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param xmiId
	 * @param name
	 * @param elementType
	 */
	public UmlElementBuilder(final String xmiId, final String name,
			final ElementType elementType)
	{
		super();
		this.xmiId = setXmiId(xmiId);
		this.name = setName(name);
		this.elementType = setElementType(elementType);
	}

	/**
	 * @param element
	 */
	public UmlElementBuilder(final Element element)
	{
		super();
		this.xmiId = setXmiId(element.getAttribute(XMI_ID));
		this.name = setName(element.getAttribute(NAME));
		this.elementType = setElementType(valueOfNullSafe(ElementType.class, element
				.getAttribute(TYPE)));
		setElement(element);
	}

	// ========================= IMPLEMENTATION: Builder ===================

	/**
	 * A factory method that builds a UML element.
	 * 
	 * @return UML element instance
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public UmlElement build()
	{
		UmlElement element = null;
		switch (elementType)
		{
			case MODEL:
			case PACKAGE:
			{
				element = buildPackageElement();
				break;
			}

			case CLASS:
			{
				element = buildClassElement();
				break;
			}

			case MEMBER:
			{
				element = buildUmlMemberElement();
				break;
			}

			case RELATIONSHIP:
			{
				element = buildRelationship();
				break;
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(elementType));
			}
		}

		// Properties common to all elements
		element.setDocumentation(documentation);

		if (parent != null)
		{
			addUnderParent(element);
		}

		element.setStatus(INVALID);
		return element;
	}

	// ========================= METHODS ===================================

	/**
	 * Check whether a namespace is an active namespace or not. Compares its name with
	 * {@link TagNames#NONE}.
	 * 
	 * @param namespace
	 *            namespace's name. Must be a non-blank string
	 * @return <code>true</code> if and only if <code>namespace</code> is an active
	 *         namespace (does not equal {@link TagNames#NONE})
	 */
	public static boolean isActiveNamespace(final String namespace)
	{
		return !(NONE.equals(namespace));
	}

	/**
	 * Set a new value for the parentClass property.
	 * 
	 * @param parentClass
	 *            the parentClass to set
	 * @return this object, for chaining calls
	 */
	public UmlElementBuilder setParentClass(final UmlClass parentClass)
	{
		this.parentClass = parentClass;
		return this;
	}

	/**
	 * Set a new value for the documentation property.
	 * 
	 * @param documentation
	 *            the documentation to set
	 */
	public void setDocumentation(final String documentation)
	{
		this.documentation = documentation;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the elementType property.
	 * 
	 * @return the elementType
	 */
	public ElementType getElementType()
	{
		return elementType;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param element
	 */
	private void addUnderParent(final UmlElement element)
	{
		switch (elementType)
		{
			case PACKAGE:
			case CLASS:
			case RELATIONSHIP:
			{
				parent.addChild(element);
				break;
			}

			default:
			{
				// Do nothing
			}
		}
	}

	/**
	 * @return
	 */
	private UmlPackage buildPackageElement()
	{
		switch (elementType)
		{
			case MODEL:
			{
				return new UmlModel(xmiId, name);
			}

			case PACKAGE:
			{
				return new UmlPackage(xmiId, name);
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(elementType));
			}
		}
	}

	/**
	 * @return
	 */
	private UmlClass buildClassElement()
	{
		validateClassParameters();
		switch (classType)
		{
			case DEFAULT:
			case PRIMITIVE:
			{
				return buildUmlDefaultClass();
			}

			case CADSR_LOCAL_VALUE_DOMAIN:
			{
				return buildCaDsrLocalValueDomain();
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(classType));
			}
		}
	}

	/**
	 * @return
	 */
	private UmlClass buildUmlDefaultClass()
	{
		return new UmlClass(xmiId, name, classType, superClassName);
	}

	/**
	 * @return
	 */
	private CaDsrLocalValueDomain buildCaDsrLocalValueDomain()
	{
		final ElementStatus status = validateCaDsrLocalValueDomainParameters();
		final DtsConceptId id = new DtsConceptId(namespace, propertyName, propertyValue);
		final CaDsrLocalValueDomain lvd = new CaDsrLocalValueDomain(xmiId, name,
				superClassName, id);
		lvd.setStatus(status);
		return lvd;
	}

	/**
	 * @return
	 */
	private UmlMember buildUmlMemberElement()
	{
		validateMemberParameters();
		return new UmlMember(xmiId, name, memberClassName, parentClass);
	}

	/**
	 * @return
	 */
	private Relationship buildRelationship()
	{
		validateRelationshipParameters();
		switch (relationshipType)
		{
			case GENERALIZATION:
			{
				return buildUmlRelationship();
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(relationshipType));
			}
		}
	}

	/**
	 * @return
	 */
	private Relationship buildUmlRelationship()
	{
		return new Relationship(xmiId, name, relationshipType, sourceXmiId, targetXmiId,
				sourceName, targetName);
	}

	/**
	 * Sets some {@link UmlElement} properties found in a DOM element.
	 * 
	 * @param element
	 *            DOM element to read properties from
	 * @param processor
	 *            DOM element processor containing business logic of setting the
	 *            properties
	 */
	private void setElement(final Node element, final ElementProcessor processor)
	{
		final NodeList dataList = element.getChildNodes();
		for (int j = 0; j < dataList.getLength(); j++)
		{
			// Read children tags and set properties as they are found
			final Node childNode = dataList.item(j);
			final String nodeName = childNode.getNodeName();
			final String nodeValue = childNode.getTextContent();
			processor.process(childNode, nodeName, nodeValue);
		}
	}

	/**
	 * Sets all {@link UmlElement} properties found in a DOM element.
	 * 
	 * @param element
	 *            DOM element to read properties from
	 */
	private void setElement(final Element element)
	{
		setElement(element, new ElementProcessor()
		{
			@Override
			public void process(final Node node, final String nodeName,
					final String nodeValue)
			{
				// UmlElement properties
				if (DOCUMENTATION.equals(nodeName))
				{
					documentation = nodeValue;
				}

				// UmlClass properties
				if (CLASS_TYPE.equals(nodeName))
				{
					classType = valueOfNullSafe(ClassType.class, nodeValue);
				}
				else if (SUPER_CLASS_NAME.equals(nodeName))
				{
					superClassName = nodeValue;
				}
				// else if (PACKAGES.equals(nodeName))
				// {
				// setPackage(dataNode);
				// }

				// CaDsrLocalValueDomain properties
				else if (NAMESPACE.equals(nodeName))
				{
					namespace = nodeValue;
				}
				else if (PROPERTY_NAME.equals(nodeName))
				{
					propertyName = nodeValue;
				}
				else if (PROPERTY_VALUE.equals(nodeName))
				{
					propertyValue = nodeValue;
				}
				else if (SUB_PROPERTY_NAME.equals(nodeName))
				{
					subPropertyName = nodeValue;
				}
				else if (SUB_PROPERTY_VALUE.equals(nodeName))
				{
					subPropertyValue = nodeValue;
				}

				// UmlMember properties
				else if (MEMBER_CLASS_NAME.equals(nodeName))
				{
					memberClassName = nodeValue;
				}

				// Relationship properties
				else if (RELATIONSHIP_TYPE.equals(nodeName))
				{
					relationshipType = valueOfNullSafe(RelationshipType.class, nodeValue);
				}
				else if (SOURCE.equals(nodeName) || TARGET.equals(nodeName))
				{
					setRelationshipElement(node, nodeName);
				}
			}
		});
	}

	/**
	 * Set a new value for the parent property.
	 * 
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(final UmlElement parent)
	{
		this.parent = parent;
	}

	/**
	 * Set a new value for the xmiId property.
	 * 
	 * @param xmiId
	 *            the xmiId to set
	 */
	private String setXmiId(final String xmiId)
	{
		validateIsTrue(isNotBlank(xmiId), getValidationMessagePrefix() + "an XMI ID");
		return xmiId;
	}

	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	private String setName(final String name)
	{
		validateIsTrue(isNotBlank(name), getValidationMessagePrefix() + "a name");
		return name;
	}

	/**
	 * Set a new value for the elementType property.
	 * 
	 * @param elementType
	 *            the elementType to set
	 */
	private ElementType setElementType(final ElementType elementType)
	{
		validateIsTrue(elementType != null, getValidationMessagePrefix()
				+ "a valid element type");
		return elementType;
	}

	/**
	 * Validate class parameters.
	 */
	private void validateClassParameters()
	{
		validateIsTrue(classType != null, getValidationMessagePrefix()
				+ "a valid class type");
		if (classType != PRIMITIVE)
		{
			validateIsTrue((pkg != null) && !pkg.isEmpty(), getElementName()
					+ "a non-empty package");
		}
	}

	/**
	 * Validate value domain tag values. If the DTS namespace is {@link TagNames#NONE},
	 * the other tags are ignored.
	 * 
	 * @return element status
	 */
	private ElementStatus validateCaDsrLocalValueDomainParameters()
	{
		validateIsTrue(isNotBlank(namespace), getValidationMessagePrefix()
				+ "a DTS namespace");
		if (isActiveNamespace(namespace))
		{
			validateIsTrue(isNotBlank(propertyName), getValidationMessagePrefix()
					+ "a DTS property name");
			validateIsTrue(isNotBlank(propertyValue), getValidationMessagePrefix()
					+ "a DTS property value");
			return ACTIVE;
		}
		return IN_PROGRESS;
	}

	/**
	 *
	 */
	private void validateRelationshipParameters()
	{
		validateIsTrue(relationshipType != null, getValidationMessagePrefix()
				+ "a valid relationship type");
		validateIsTrue(isNotBlank(sourceXmiId), getValidationMessagePrefix()
				+ "source class XMI ID");
		validateIsTrue(isNotBlank(targetXmiId), getValidationMessagePrefix()
				+ "target class XMI ID");
		validateIsTrue(isNotBlank(sourceName), getValidationMessagePrefix()
				+ "source class name");
		validateIsTrue(isNotBlank(targetName), getValidationMessagePrefix()
				+ "target class name");
	}

	/**
	 *
	 */
	private void validateMemberParameters()
	{
		validateIsTrue(isNotBlank(memberClassName), getValidationMessagePrefix()
				+ "a member class name");
		validateIsTrue(parentClass != null, getValidationMessagePrefix()
				+ "a parent (owning) class");
	}

	/**
	 * @return
	 */
	private String getValidationMessagePrefix()
	{
		return getElementName() + " must have ";
	}

	/**
	 * @return
	 */
	private String getElementName()
	{
		return (name == null) ? UmlElement.class.getSimpleName() : quote(name);
	}

	/**
	 * Sets part of a {@link Relationship} {@link UmlElement} properties found in a DOM
	 * element.
	 * 
	 * @param element
	 *            DOM element to read properties from
	 * @param context
	 *            context within the {@link Relationship} element; must be "source" or
	 *            "target"
	 */
	private void setRelationshipElement(final Node element, final String context)
	{
		final ElementProcessor sourceProcessor = new ElementProcessor()
		{
			@Override
			public void process(final Node node, final String nodeName,
					final String nodeValue)
			{
				if (XMI_ID.equals(nodeName))
				{
					sourceXmiId = nodeValue;
				}
				else if (NAME.equals(nodeName))
				{
					sourceName = nodeValue;
				}
			}
		};

		final ElementProcessor targetProcessor = new ElementProcessor()
		{
			@Override
			public void process(final Node node, final String nodeName,
					final String nodeValue)
			{
				if (XMI_ID.equals(nodeName))
				{
					targetXmiId = nodeValue;
				}
				else if (NAME.equals(nodeName))
				{
					targetName = nodeValue;
				}
			}
		};

		setElement(element, SOURCE.equals(context) ? sourceProcessor : targetProcessor);
	}
}
