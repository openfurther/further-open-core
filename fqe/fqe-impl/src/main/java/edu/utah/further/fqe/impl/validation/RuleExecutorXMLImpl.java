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
package edu.utah.further.fqe.impl.validation;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.NamespaceContext;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.api.xml.XmlUtil;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.xml.xpath.XPathNamespaceContext;
import edu.utah.further.core.xml.xpath.XPathUtil;
import edu.utah.further.fqe.api.validation.RuleExecutor;
import edu.utah.further.fqe.impl.validation.domain.ActionType;
import edu.utah.further.fqe.impl.validation.domain.RelationshipType;
import edu.utah.further.fqe.impl.validation.domain.RuleCondition;
import edu.utah.further.fqe.impl.validation.domain.RuleType;
import edu.utah.further.fqe.impl.validation.domain.ValidationRule;

/**
 * Rule execution Implementation using XML based rule definitions
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version Feb 1, 2012
 */
@Component
public class RuleExecutorXMLImpl implements RuleExecutor<ValidationRule>
{

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(RuleListUtilXMLImpl.class);

	// ========================= Instance Variables ==============================

	/**
	 * The query criteria relation that the rule is checking for
	 */
	private RelationshipType relationship;

	/**
	 * A list of observation properties that rule is checking for
	 */
	private List<String> observations;

	/**
	 * The type of rule that is being run on the query
	 */
	private RuleType ruleType;

	/**
	 * A conditional for the rule parameter
	 */
	private RuleCondition ruleCondition;

	/**
	 * The action that is taken if the validation rule catches the case it's checking for
	 */
	private ActionType action;

	/**
	 * The parameter of the ruleCondition
	 */
	private int ruleParameter;

	/**
	 * The search query that is being validated
	 */
	private Document searchQuery;

	/**
	 * The {@link NamespaceContext} for which all xpaths in this class should execute
	 * under.
	 */
	private final XPathNamespaceContext namespaceContext;

	// ========================= Services ==============================

	/**
	 * XmlService
	 */
	@Autowired
	protected XmlService xmlService;

	/**
	 * 
	 */
	public RuleExecutorXMLImpl()
	{
		namespaceContext = new XPathNamespaceContext();
		namespaceContext.addPrefix("query", XmlNamespace.CORE_QUERY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.fqe.api.validation.RuleExecutor#executeRules(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public boolean executeRule(final SearchQuery query, final ValidationRule rule)
	{

		observations = rule.getCriteriaIdentifier().getObservation();
		relationship = rule.getCriteriaIdentifier().getRelationship();
		ruleType = rule.getRule().getRuleDefinition().getRuleType();
		ruleCondition = rule.getRule().getRuleDefinition().getRuleCondition();
		ruleParameter = rule.getRule().getParameter().intValue();
		action = rule.getAction();

		searchQuery = toDocument(query);

		switch (ruleType)
		{
			case DATA_TYPE_RESTRICTION:
			{
				return dataTypeRuleExecutor();
			}
			case OBSERVATION_RESTRICTION:
			{
				return observationRuleExecutor();
			}
			default:
			{
				return true;
			}
		}
	}

	/**
	 * Executes a data type condition rule
	 * 
	 * @return
	 */
	public boolean dataTypeRuleExecutor()
	{
		final List<Node> matchingSearches = getMatchingSearchTypes();

		if (!matchingSearches.isEmpty())
		{
			for (final Node search : matchingSearches)
			{
				int obsCount = 0;
				for (final String observation : observations)
				{

					if (!XPathUtil.executeXPathExpression(search,
							String.format(PATH_TO_PARAMETER_VAL, observation),
							namespaceContext).isEmpty())
					{
						obsCount++;
					}
				}
				if (determineResult(obsCount) && action.equals(ActionType.FAIL))
				{

					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 
	 * Executes an observation count rule
	 * 
	 * @return
	 */
	public boolean observationRuleExecutor()
	{

		final List<Node> matchingSearches = getMatchingSearchTypes();

		if (!matchingSearches.isEmpty())
		{
			for (final Node search : matchingSearches)
			{
				if (determineResult(XPathUtil.executeXPathExpression(search,
						PATH_TO_PARAMETERS, namespaceContext).size())
						&& action.equals(ActionType.FAIL))
				{
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 
	 * Finds all nodes in a search query that matches rule relationship type
	 * 
	 * @return nodes
	 */
	private List<Node> getMatchingSearchTypes()
	{

		String xpathExpression = null;

		switch (relationship)
		{
			case DISJUNCTION:
			{
				xpathExpression = PATH_TO_DISJUNCTION_SEARCHTYPE;
				break;
			}
			case CONJUNCTION:
			{
				xpathExpression = PATH_TO_CONJUNCTION_SEARCHTYPE;
				break;
			}
			case IN:
			{
				xpathExpression = PATH_TO_IN_SEARCHTYPE;
				break;
			}
			default:
			{
				return CollectionUtil.newList();
			}
		}

		return XPathUtil.executeXPathExpression(searchQuery, xpathExpression,
				namespaceContext);

	}

	/**
	 * Determines the result based on the rule condition and parameter
	 * 
	 * @param count
	 * @return
	 */
	private boolean determineResult(final int count)
	{

		switch (ruleCondition)
		{
			case GREATER:
			{
				return count > ruleParameter;
			}

			case LESSTHAN:
			{
				return count < ruleParameter;
			}

			case EQUALS:
			{
				return count == ruleParameter;
			}
			default:
			{
				return false;
			}
		}
	}

	/**
	 * Transforms the SearchQuery into a DOM Document so that we can traverse it
	 * 
	 * @param searchQuery
	 * @return
	 * @throws JAXBException
	 * @throws DocumentException
	 */
	private Document toDocument(final SearchQuery query)
	{
		try
		{
			final String searchQueryStr = xmlService.marshal(query);
			return XmlUtil.createDomDocument(searchQueryStr);
		}
		catch (final JAXBException e)
		{
			throw new ApplicationException(
					"Unable to marshal SearchQuery object for validation", e);
		}

	}
}
