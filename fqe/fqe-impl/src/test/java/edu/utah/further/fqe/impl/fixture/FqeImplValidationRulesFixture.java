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
package edu.utah.further.fqe.impl.fixture;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;

import java.math.BigInteger;
import java.util.List;

import edu.utah.further.fqe.impl.validation.domain.ActionType;
import edu.utah.further.fqe.impl.validation.domain.CriteriaIdentifier;
import edu.utah.further.fqe.impl.validation.domain.RelationshipType;
import edu.utah.further.fqe.impl.validation.domain.Rule;
import edu.utah.further.fqe.impl.validation.domain.RuleCondition;
import edu.utah.further.fqe.impl.validation.domain.RuleDefinition;
import edu.utah.further.fqe.impl.validation.domain.RuleType;
import edu.utah.further.fqe.impl.validation.domain.ValidationRule;

/**
 * ...
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
 * @version Feb 15, 2012
 */
public abstract class FqeImplValidationRulesFixture extends FqeImplUtestFixture {
	
	public final String SAMPLE_QUERY_PATH = "validation/queries/";
	
	public List<ValidationRule> testRules() {
		
		final List<ValidationRule> rules = newList();
		rules.add(dataTypeRule());
		rules.add(icdCountRule());
		return rules;
	}
	
	public ValidationRule dataTypeRule() {
	
		final ValidationRule validationRule = new ValidationRule();
		final CriteriaIdentifier criteriaIdentifer = new CriteriaIdentifier();
		final List<String> observations = newList();
		observations.add("439401001");
		observations.add("364712009");
		observations.add("416342005");
		criteriaIdentifer.getObservation().addAll(observations);
		criteriaIdentifer.setRelationship(RelationshipType.DISJUNCTION);
		validationRule.setCriteriaIdentifier(criteriaIdentifer);
		final Rule rule = new Rule();
		final RuleDefinition ruleDef = new RuleDefinition();
		ruleDef.setRuleCondition(RuleCondition.GREATER);
		ruleDef.setRuleType(RuleType.DATA_TYPE_RESTRICTION);
		rule.setRuleDefinition(ruleDef);
		rule.setParameter(new BigInteger("1"));
		validationRule.setRule(rule);
		validationRule.setAction(ActionType.FAIL);
		return validationRule;
	}
	
	public ValidationRule icdCountRule() {
		
		final ValidationRule validationRule = new ValidationRule();
		final CriteriaIdentifier criteriaIdentifer = new CriteriaIdentifier();
		final List<String> observations = newList();
		observations.add("439401001");
		criteriaIdentifer.getObservation().addAll(observations);
		criteriaIdentifer.setRelationship(RelationshipType.IN);
		validationRule.setCriteriaIdentifier(criteriaIdentifer);
		final Rule rule = new Rule();
		final RuleDefinition ruleDef = new RuleDefinition();
		ruleDef.setRuleCondition(RuleCondition.GREATER);
		ruleDef.setRuleType(RuleType.OBSERVATION_RESTRICTION);
		rule.setRuleDefinition(ruleDef);
		rule.setParameter(new BigInteger("50"));
		validationRule.setRule(rule);
		validationRule.setAction(ActionType.FAIL);
		return validationRule;
	}

}
