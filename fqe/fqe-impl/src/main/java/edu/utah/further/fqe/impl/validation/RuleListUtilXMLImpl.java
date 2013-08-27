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

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.fqe.api.validation.RuleListUtil;
import edu.utah.further.fqe.impl.validation.domain.ValidationRule;

/**
 * Utility implementation for returning a list of rules using XML based rule definitions
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
 * @param <O>
 */
@Component
public class RuleListUtilXMLImpl implements RuleListUtil<ValidationRule>
{

	/**
	 * Default rule directory
	 */
	protected static final String DEFAULT_RULE_DIR = "file:validation/rules/*.xml";

	/**
	 * Rule Directory instance variable
	 */
	private String ruleDir = new String();

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(RuleListUtilXMLImpl.class);

	/**
	 * XmlService
	 */
	@Autowired
	protected XmlService xmlService;

	/**
	 * A Spring {@link ResourceLoader} for loading the rules
	 */
	@Autowired
	private ResourceLoader resourceLoader;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.validation.RuleListUtil#getRules()
	 */
	@Override
	public List<ValidationRule> getRules()
	{

		final List<ValidationRule> rules = newList();
		final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(
				resourceLoader);
		Resource[] ruleNames = new Resource[0];
		try
		{
			ruleNames = resolver.getResources((ruleDir.isEmpty()) ? DEFAULT_RULE_DIR
					: ruleDir);
		}
		catch (final IOException e)
		{
			log.error("An IOException occurred", e);
		}
		for (final Resource name : ruleNames)
		{
			rules.add(loadRuleFromClassPath(name));
		}

		return rules;
	}

	/**
	 * 
	 * Marshals an XML rule instance into a java object
	 * 
	 * @param fileName
	 * @return rule
	 */
	private ValidationRule loadRuleFromClassPath(final Resource fileName)
	{

		ValidationRule rule = new ValidationRule();
		try
		{
			rule = xmlService.unmarshal(fileName.getInputStream(), ValidationRule.class);
		}
		catch (final JAXBException e)
		{
			log.error("A JAXBException occurred", e);
		}
		catch (final IOException e)
		{
			log.error("An IOException occurred", e);
		}

		return rule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.fqe.api.validation.RuleListUtil#setRuleDir(java.lang.String)
	 */
	@Override
	public void setRuleDir(final String ruleDir)
	{

		this.ruleDir = ruleDir;
	}
}
