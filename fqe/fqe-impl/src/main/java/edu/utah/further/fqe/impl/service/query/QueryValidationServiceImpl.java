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
package edu.utah.further.fqe.impl.service.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryTo;
import edu.utah.further.fqe.api.service.query.QueryValidationService;
import edu.utah.further.fqe.api.validation.RuleExecutor;
import edu.utah.further.fqe.api.validation.RuleListUtil;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.impl.validation.domain.ValidationRule;

/**
 * Search query validation service Implementation using XML based rule definitions
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
 * @version Jan 30, 2012
 */

@Service("queryValidationService")
public class QueryValidationServiceImpl implements QueryValidationService<ValidationRule>
{

	@Autowired
	RuleListUtil<ValidationRule> ruleUtil;

	@Autowired
	RuleExecutor<ValidationRule> executor;

	/**
	 * XmlService
	 */
	@Autowired
	protected XmlService xmlService;

	@Override
	public boolean validateQuery(QueryContext queryContext)
	{
		// TODO Auto-generated method stub
		SearchQueryTo query = SearchQueryTo.newCopy(queryContext.getQuery());
		List<ValidationRule> rules = ruleUtil.getRules();

		return executeRules(query, rules);
	}

	/**
	 * @param query
	 * @param rules
	 * @return
	 */
	private boolean executeRules(SearchQuery query, List<ValidationRule> rules)
	{

		boolean valid = true;
		for (ValidationRule rule : rules)
		{

			valid = executor.executeRule(query, rule);
		}

		return valid;
	}

}
