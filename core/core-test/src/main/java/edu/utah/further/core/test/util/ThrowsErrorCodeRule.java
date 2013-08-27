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
package edu.utah.further.core.test.util;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.HasErrorCode;
import edu.utah.further.core.test.annotation.ThrowsErrorCode;

/**
 * Verifies that a JUnit test method throws an {@link ApplicationException} with a certain
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Aug 10, 2010
 */
public final class ThrowsErrorCodeRule implements TestRule
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.junit.rules.TestRule#apply(org.junit.runners.model.Statement,
	 * org.junit.runner.Description)
	 */
	@Override
	public Statement apply(final Statement statement, final Description description)
	{
		return new Statement()
		{

			@Override
			public void evaluate() throws Throwable
			{
				final ThrowsErrorCode throwsErrorCode = description
						.getAnnotation(ThrowsErrorCode.class);
				if (throwsErrorCode == null)
				{
					statement.evaluate();
				}
				else
				{
					try
					{
						statement.evaluate();
					}
					catch (final Throwable e)
					{
						assertThat(e, instanceOf(HasErrorCode.class));
						final HasErrorCode ex = (HasErrorCode) e;
						assertEquals(throwsErrorCode.value(), ex.getCode());
						return;
					}
					throw new AssertionError("Expected test to throw "
							+ HasErrorCode.class);
				}
			}
		};
	}
	// ========================= IMPL: MethodRule ==========================

}
