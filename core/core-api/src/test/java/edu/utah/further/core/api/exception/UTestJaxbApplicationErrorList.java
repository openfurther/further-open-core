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
package edu.utah.further.core.api.exception;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;

import edu.utah.further.core.api.CoreApiFixture;
import edu.utah.further.core.api.constant.ErrorCode;

/**
 * Tests marshalling {@link ApplicationErrorList}
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 19, 2013
 */
public class UTestJaxbApplicationErrorList extends CoreApiFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestJaxbWsException.class);

	/**
	 * JAXB-annotated class package.
	 */
	private static final String PACKAGE = ApplicationErrorList.class
			.getPackage()
			.getName();

	// ========================= TESTING METHODS ===========================

	/**
	 * Marshal a ApplcationError
	 */
	@Test
	public void marshalApplicationError() throws Exception
	{
		final ApplicationErrorList entity = newApplicationErrorList();
		final String s = testMarshalling(entity, PACKAGE);
		assertThat(s, containsString(":errors"));
		assertThat(s, containsString(":errors>"));
		assertThat(s, containsString("<error>"));
		assertThat(s, containsString("</error>"));
		assertThat(s, containsString("myValue"));
		assertThat(s, containsString("yourValue"));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private static ApplicationErrorList newApplicationErrorList()
	{
		final ApplicationError errorOne = new ApplicationError(ErrorCode.INTERNAL_ERROR,
				"myValue", "a", "b", "c");
		final ApplicationError errorTwo = new ApplicationError(ErrorCode.INTERNAL_ERROR,
				"yourValue", "a", "b", "c");
		final List<ApplicationError> errors = new ArrayList<>();
		errors.add(errorOne);
		errors.add(errorTwo);
		return new ApplicationErrorList(errors);
	}
}
