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

import static org.junit.Assert.assertFalse;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.xml.bind.api.JAXBRIContext;

import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.core.query.domain.SearchQueryTo;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.xml.jaxb.JaxbConfigurationFactoryBean;
import edu.utah.further.fqe.api.validation.RuleExecutor;
import edu.utah.further.fqe.impl.fixture.FqeImplValidationRulesFixture;
import edu.utah.further.fqe.impl.validation.domain.ValidationRule;

/**
 * Unit test for testing production validation rules
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
public class UTestRuleExecutor extends FqeImplValidationRulesFixture
{

	@Autowired
	RuleExecutor<ValidationRule> ruleExecutor;

	@Autowired
	XmlService xmlService;

	private String MULTIPLE_DATATYPE_QUERY = SAMPLE_QUERY_PATH
			+ "multipleDataTypesQuery.xml";

	private String ICD_EXCESS_QUERY = SAMPLE_QUERY_PATH + "excessIcd9CodesQuery.xml";

	/**
	 * JAXB Configuration.
	 */
	private static final Map<String, Object> CORE_QUERY_JAXB_CONFIG = JaxbConfigurationFactoryBean.DEFAULT_JAXB_CONFIG;

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	// ========================= SETUP METHODS =============================

	@Before
	public void setup()
	{
		/* Override the base */
		CORE_QUERY_JAXB_CONFIG.put(JAXBRIContext.DEFAULT_NAMESPACE_REMAP,
				XmlNamespace.CORE_QUERY);
	}

	@Test
	public void testMultipleDatatypeRule() throws Exception
	{

		final SearchQuery query = xmlService.unmarshalResource(MULTIPLE_DATATYPE_QUERY,
				SearchQueryTo.class);
		boolean result = ruleExecutor.executeRule(query, dataTypeRule());
		assertFalse(result);
	}

	@Test
	public void testICDRule() throws Exception
	{

		final SearchQuery query = xmlService.unmarshalResource(ICD_EXCESS_QUERY,
				SearchQueryTo.class);
		boolean result = ruleExecutor.executeRule(query, icdCountRule());
		assertFalse(result);
	}

}
