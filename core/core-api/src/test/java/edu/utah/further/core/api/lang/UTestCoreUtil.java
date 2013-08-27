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
package edu.utah.further.core.api.lang;

import static edu.utah.further.core.api.lang.CoreUtil.oneFalse;
import static edu.utah.further.core.api.lang.CoreUtil.oneTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.annotation.ExpectedException;

import edu.utah.further.core.api.exception.BusinessRuleException;

/**
 * Test core utilities in {@link CoreUtil}.
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
 * @version Nov 6, 2008
 */
public final class UTestCoreUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestCoreUtil.class);

	// ========================= METHODS ===================================

	/**
	 * Test an expected exception. Honored by any normal JUnit 4 runner, not just by the
	 * Spring JUnit runner (unlike {@link ExpectedException}).
	 */
	@Test(expected = BusinessRuleException.class)
	public void expectedException()
	{
		throw new BusinessRuleException("Expected error");
	}

	/**
	 * Test the generalized exclusive OR.
	 */
	@Test
	public void oneTrueTwoArguments()
	{
		assertFalse(oneTrue(false, false));
		assertTrue(oneTrue(false, true));
		assertTrue(oneTrue(true, false));
		assertFalse(oneTrue(true, true));
	}

	/**
	 * Test the generalized exclusive OR.
	 */
	@Test
	public void oneTrueThreeArguments()
	{
		assertFalse(oneTrue(false, false, false));
		assertTrue(oneTrue(false, false, true));
		assertTrue(oneTrue(false, true, false));
		assertFalse(oneTrue(false, true, true));
		assertTrue(oneTrue(true, false, false));
		assertFalse(oneTrue(true, false, true));
		assertFalse(oneTrue(true, true, false));
		assertFalse(oneTrue(true, true, true));
	}

	/**
	 * Test the adjoint generalized exclusive OR.
	 */
	@Test
	public void oneFalseTwoArguments()
	{
		assertFalse(oneFalse(false, false));
		assertTrue(oneFalse(false, true));
		assertTrue(oneFalse(true, false));
		assertFalse(oneFalse(true, true));
	}

	/**
	 * Test the adjoint generalized exclusive OR.
	 */
	@Test
	public void oneFalseThreeArguments()
	{
		assertFalse(oneFalse(false, false, false));
		assertFalse(oneFalse(false, false, true));
		assertFalse(oneFalse(false, true, false));
		assertTrue(oneFalse(false, true, true));
		assertFalse(oneFalse(true, false, false));
		assertTrue(oneFalse(true, false, true));
		assertTrue(oneFalse(true, true, false));
		assertFalse(oneFalse(true, true, true));
	}

	/**
	 * Test invoking the protected {@link Object#clone()} via reflection.
	 * <p>
	 * On the surface, this seems to not be possible to invoke {@link Object#clone()}
	 * outside the scope of the class being cloned, because {@link Class#getMethods()}
	 * returns all <i>public</i> methods of the class and its super-classes and
	 * interfaces, while {@link Class#getDeclaredMethods()} returns all methods, but only
	 * of the class, not its super-classes and interfaces. But we can call
	 * <code>Object.class.getDeclaredMethods()</code>, get a handle to
	 * <code>clone()</code>, and invoke it on the actual class. It can be <i>invoked</i>
	 * on <i>any</i> class (but only reflectively <code>obtained</code> from
	 * <code>Object.class</code>).
	 */
	@Test
	public void cloneByReflection()
	{
		final SimpleCloneable original = new SimpleCloneable();
		original.setValue(123);

		final SimpleCloneable copy = original.copy();
		assertTrue(copy != original);
		assertEquals(original, copy);
	}

	// ========================= PRIVATE METHODS ===========================

}
