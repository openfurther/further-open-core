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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.fqe.api.validation.RuleListUtil;
import edu.utah.further.fqe.impl.fixture.FqeImplValidationRulesFixture;
import edu.utah.further.fqe.impl.validation.domain.ValidationRule;

/**
 * Unit test for testing proper loading of validation rules from XML
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
@UnitTest
public class UTestRuleListUtil extends FqeImplValidationRulesFixture
{

	@Autowired
	RuleListUtil<ValidationRule> ruleListUtil;

	private final String VALIDATION_RULES_PATH = "validation/rules/*.xml";

	@Before
	public void setup()
	{

		ruleListUtil.setRuleDir(VALIDATION_RULES_PATH);
	}

	@Test
	public void testRuleListUtil()
	{

		List<ValidationRule> rules = ruleListUtil.getRules();
		assertEquals(rules.size(), testRules().size());
	}

}
