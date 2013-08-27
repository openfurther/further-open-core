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
import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.mdr.api.domain.uml.ElementType.CLASS;
import static edu.utah.further.mdr.api.domain.uml.TagNames.ELEMENT;
import static edu.utah.further.mdr.api.domain.uml.TagNames.PARENT_XMI_ID;
import static edu.utah.further.mdr.api.domain.uml.UmlPackage.DEFAULT_PACKAGE;
import static org.slf4j.LoggerFactory.getLogger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.message.SeverityMessageContainer;
import edu.utah.further.mdr.api.domain.uml.ElementType;
import edu.utah.further.mdr.api.domain.uml.TagNames;
import edu.utah.further.mdr.api.domain.uml.UmlClass;
import edu.utah.further.mdr.api.domain.uml.UmlElement;
import edu.utah.further.mdr.api.domain.uml.UmlElementBuilder;
import edu.utah.further.mdr.api.domain.uml.UmlMember;
import edu.utah.further.mdr.api.domain.uml.UmlModel;
import edu.utah.further.mdr.api.domain.uml.UmlPackage;

/**
 * XMI 2.1 parser implementation. Right now this is not a singleton service and requires
 * an instance per model loading. In the future, we might make it a singleton class.
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
 * @see http://www.ibm.com/developerworks/library/x-javaxpathapi.html#changed
 */
@Implementation
@Service("xmiParser21")
@Scope(Constants.Scope.PROTOTYPE)
public class XmiParser21Impl extends AbstractXmiParser
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(XmiParser21Impl.class);

	/**
	 * XMI file UML namespace schema URL.
	 */
	private static final String UML_NAMESPACE_URL = "http://schema.omg.org/spec/UML/2.1";

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an XMI 1.1 parser.
	 */
	public XmiParser21Impl()
	{
		super();
		addLineTransformer(UML_NAMESPACE_LINE_TRANSFORMER);
	}

	// ========================= IMPLEMENTATION: AbstractXmiParser =========

	/**
	 * @param results
	 * @return
	 */
	@Override
	protected UmlModel parseResults(final XMLStreamReader results) throws XMLStreamException
	{
		final Element modelElement = getRootElement(results);
		final UmlModel model = loadModel(modelElement);
		if (model == null)
		{
			return null;
		}

		// // Restructure model as a package tree
		// final UmlModelPackageBuilder packageBuilder = new
		// UmlModelPackageBuilder(model);
		// model = packageBuilder.build();
		// addMessages(packageBuilder.getMessages());

		// Build model relationships to ensure its integrity
		final UmlModelIntegrator integrator = new UmlModelIntegrator(model, options);
		final SeverityMessageContainer integrationMessages = integrator.integrate();
		addMessages(integrationMessages);

		return model;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Fix a bug in EA XMI export by removing standardizing the <code>uml</code> namespace
	 * schema URL to the same string everywhere. Also, re-encode an input stream in UTF-8
	 * encoding.
	 * <p>
	 * Specifically, we replace all strings like:<br>
	 * xmlns:uml="http://schema.omg.org/spec/UML/2.1/"<br>
	 * xmlns:uml="http://schema.omg.org/spec/UML/2.1/uml.xml"<br>
	 * xmlns:uml="http://schema.omg.org/spec/UML/2.1/ANYTHING ELSE"<br>
	 * by:<br>
	 * xmlns:uml="http://schema.omg.org/spec/UML/2.1"<br>
	 * </p>
	 */
	private static final LineTransformer UML_NAMESPACE_LINE_TRANSFORMER = new LineTransformer()
	{
		@Override
		public String transform(final String line)
		{
			final String oldRegexp = "xmlns:uml=\\\"" + UML_NAMESPACE_URL + "/.*\\\"";
			final String standardString = "xmlns:uml=\\\"" + UML_NAMESPACE_URL + "\\\"";
			return line.replaceAll(oldRegexp, standardString);
		}
	};

	/**
	 * Add all packages and add them under the model {@link UmlElement}.
	 * 
	 * @param modelElement
	 *            model's DOM element
	 * @param model
	 *            UML model to update
	 */
	private void loadPackages(final Element modelElement, final UmlModel model)
	{
		final NodeList childrenElements = modelElement.getChildNodes();
		for (int i = 0; i < childrenElements.getLength(); i++)
		{
			// Skip everything that's not an element
			if (childrenElements.item(i).getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}

			final Element packageElement = (Element) childrenElements.item(i);
			if (log.isDebugEnabled())
			{
				log.debug(" packageElement = " + packageElement + " name "
						+ packageElement.getNodeName());
			}
			if (TagNames.PACKAGE.equals(packageElement.getNodeName()))
			{
				try
				{
					final UmlElementBuilder builder = new UmlElementBuilder(
							packageElement);

					// Find the pacakge's parent element; relying on the package ordering
					// in the XMI file to ensure post-traversal package element order.
					// If parent not found, add the package directly under the model root.
					// TODO: improve this O(n^2) operation by a better tree building
					// algorithm.
					final UmlElement parent = findAndAddParentToBuilder(model, model,
							packageElement, builder);

					// Construct a new package element
					final UmlElement pkg = builder.build();

					// TODO: improve this O(n^2) operation by a better tree building
					parent.addChild(pkg, false);

					if (log.isDebugEnabled())
					{
						log.debug("Loaded package " + quote(pkg.toString())
								+ " under package " + quote(parent.getName()));
					}
				}
				catch (final Throwable e)
				{
					addMessage(ERROR, e.getMessage());
				}
			}
		}
	}

	/**
	 * Add all classes and add them directly under the model {@link UmlElement}.
	 * 
	 * @param modelElement
	 *            model's DOM element
	 * @param model
	 *            UML model to update
	 */
	private void loadClasses(final Element modelElement, final UmlModel model)
	{
		final NodeList childrenElements = modelElement.getChildNodes();
		for (int i = 0; i < childrenElements.getLength(); i++)
		{
			// Skip everything that's not an element
			if (childrenElements.item(i).getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}

			final Element classElement = (Element) childrenElements.item(i);
			if (ELEMENT.equals(classElement.getNodeName()))
			{
				try
				{
					// If this is a class element, add class and its members to the model
					// (we ignore the model's original package structure). Otherwise,
					// just add the element as a child of the model element
					final UmlElementBuilder builder = new UmlElementBuilder(classElement);
					final UmlPackage defaultPackage = (builder.getElementType() == CLASS) ? model
							.getDefaultPackage() : model;
					findAndAddParentToBuilder(model, defaultPackage, classElement,
							builder);
					final UmlElement child = builder.build();

					if (log.isDebugEnabled())
					{
						if (child.getElementType() == CLASS)
						{
							log.debug("Loaded class "
									+ quote(((UmlClass) child).getQualifiedName()));
						}
						else
						{
							log.debug("Loaded " + child);
						}
					}

					processUmlClassElement(model, classElement, child);
				}
				catch (final Throwable e)
				{
					addMessage(ERROR, e.getMessage());
				}
			}
		}
	}

	/**
	 * @param model
	 * @param classElement
	 * @param child
	 */
	private void processUmlClassElement(final UmlModel model, final Element classElement,
			final UmlElement child)
	{
		switch (child.getElementType())
		{
			case CLASS:
			{
				addClass(model, classElement, (UmlClass) child);
				break;
			}

			default:
			{
				// Do nothing
				break;
			}
		}
	}

	/**
	 * @param model
	 * @param classElement
	 * @param clazz
	 */
	private void addClass(final UmlModel model, final Element classElement,
			final UmlClass clazz)
	{
		model.addClass(clazz);

		// Add members to class
		loadMembers(classElement, clazz);
	}

	/**
	 * Add attributes to class.
	 * 
	 * @param classElement
	 * @param clazz
	 */
	private void loadMembers(final Node classElement, final UmlClass clazz)
	{
		final NodeList attributeElements = classElement.getChildNodes();
		for (int j = 0; j < attributeElements.getLength(); j++)
		{
			// Skip everything that's not an element
			if (attributeElements.item(j).getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}

			final Element attributeElement = (Element) attributeElements.item(j);
			if (ELEMENT.equals(attributeElement.getNodeName()))
			{
				final UmlMember attribute = (UmlMember) new UmlElementBuilder(
						attributeElement).setParentClass(clazz).build();
				clazz.addChild(attribute);
				if (log.isDebugEnabled())
				{
					log.debug("Loaded attribute " + quote(attribute.getQualifiedName()));
				}
			}
		}
	}

	/**
	 * Read super-sub-class relationships and set them in all model classes.
	 * 
	 * @param modelElement
	 *            model's DOM element
	 * @param model
	 *            UML model to update
	 */
	private void addGeneralizationRelationships(final Element modelElement,
			final UmlElement model)
	{
		final NodeList classElements = modelElement.getChildNodes();
		for (int i = 0; i < classElements.getLength(); i++)
		{
			// Skip everything that's not an element
			if (classElements.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			final Element connectorElement = (Element) classElements.item(i);
			if (ELEMENT.equals(connectorElement.getNodeName()))
			{
				// Unoptimized code: rebuilding all children of model again, even if they
				// are classes and are already rebuild.
			}
		}
	}

	/**
	 * @param modelElement
	 *            model's DOM element
	 * @return fully populated UML model object
	 */
	private UmlModel loadModel(final Element modelElement)
	{
		// Load root model element. If failure, do not proceed
		final UmlModel model = loadModelElement(modelElement);
		if (model == null)
		{
			return null;
		}

		// Load package structure
		loadPackages(modelElement, model);

		// Load classes and attributes
		loadClasses(modelElement, model);

		// Add class relationships
		addGeneralizationRelationships(modelElement, model);

		return model;
	}

	/**
	 * @param modelElement
	 * @return
	 */
	private UmlModel loadModelElement(final Element modelElement)
	{
		UmlModel model = null;
		try
		{
			model = (UmlModel) new UmlElementBuilder(modelElement).build();
			if (log.isDebugEnabled())
			{
				log.debug("Loaded UML model element " + quote(model.toString()));
			}

			// Construct a default package under the model. Classes that are
			// not designated to packages will be added under this default package.
			final UmlElementBuilder builder = new UmlElementBuilder(DEFAULT_PACKAGE,
					DEFAULT_PACKAGE, ElementType.PACKAGE);
			builder.setParent(model);
			final UmlPackage defaultPackage = (UmlPackage) builder.build();
			model.setDefaultPackage(defaultPackage);
		}
		catch (final Throwable e)
		{
			addMessage(
					ERROR,
					"Could not find a UML model in the file. This may be due to an EA bug that is not yet covered in our code: the UML schema namespace URL is exported with different values for different elements in the file. Right now, the model root UML element reports the error: "
							+ e.getMessage());
		}

		return model;
	}

	/**
	 * Find a parent package specified by a DOM element attribute in the model. If not
	 * found, returns a default package.
	 * 
	 * @param model
	 * @param defaultPackage
	 *            default package to return if parent not found found
	 * @param packageElement
	 * @param builder
	 * @return parent element of the package in the model
	 */
	private UmlElement findAndAddParentToBuilder(final UmlModel model,
			final UmlPackage defaultPackage, final Element packageElement,
			final UmlElementBuilder builder)
	{
		final String parentXmiId = packageElement.getAttribute(PARENT_XMI_ID);
		UmlElement parent = UmlElementFinder.findElementById(model, parentXmiId);
		if (parent == null)
		{
			parent = defaultPackage;
		}
		// log.debug("parent XMI ID = " + parentXmiId + " parent " + parent);
		builder.setParent(parent);
		return parent;
	}
}