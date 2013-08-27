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
package edu.utah.further.dts.impl.aspect;

import static org.slf4j.LoggerFactory.getLogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;

import edu.utah.further.dts.api.annotation.DtsPropagation;
import edu.utah.further.dts.api.annotation.DtsTransactional;

/**
 * A test aspect that weaves DTS session opening and closing around DTS-transactional test
 * methods and changes their return type so that we can test this aspect.
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
 * @version May 29, 2009
 */
@Aspect
public class ReturnTypeChangingDtsTransactionAspect
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ReturnTypeChangingDtsTransactionAspect.class);

	/**
	 * Keeps track of the instance creation. Now allowing multiple instances for test
	 * suites that contain multiple declarations in different spring context files.
	 */
	// private static final SingletonInstanceManager singletonInstanceManager = new
	// SingletonInstanceManager(
	// ReturnTypeChangingDtsTransactionAspect.class);

	// ========================= DEPENDENCIES ==============================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create this aspect.
	 */
	public ReturnTypeChangingDtsTransactionAspect()
	{
		// singletonInstanceManager.validateInstance();
		if (log.isDebugEnabled())
		{
			log.debug("Creating a return-type changer for testing purposes");
		}
	}

	// ========================= POINTCUTS =================================

	@Pointcut("execution(edu.utah.further.dts.api.annotation.DtsPropagation edu.utah.further.dts.impl.aspect.SomeDtsService*.*(..))")
	private void testMethod()
	{

	}

	// ========================= METHODS ===================================

	/**
	 * Open a session before a DTS method invocation and close after it returns. Includes
	 * exception throwing by the method and normal return conditions.
	 * <p>
	 * We need separate {@link #adviseClass(ProceedingJoinPoint, DtsTransactional)} and
	 * {@link #adviseMethod(ProceedingJoinPoint, DtsTransactional)} because if we try to
	 * bind to the class and method annotations in the same advice method we get an
	 * "::0 inconsistent binding" AspectJ error.
	 */
	@Around(value = "testMethod() && @within(classAnnotation) && !@annotation(edu.utah.further.dts.api.annotation.DtsTransactional)")
	public DtsPropagation adviseClass(final ProceedingJoinPoint jp,
			final DtsTransactional classAnnotation) throws Throwable
	{
		return wrapInDtsSession(jp, classAnnotation);
	}

	/**
	 * Open a session before a DTS method invocation and close after it returns. Includes
	 * exception throwing by the method and normal return conditions.
	 */
	@Around(value = "testMethod() && @annotation(methodAnnotation)")
	public DtsPropagation adviseMethod(final ProceedingJoinPoint jp,
			final DtsTransactional methodAnnotation) throws Throwable
	{
		return wrapInDtsSession(jp, methodAnnotation);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param jp
	 * @param dtsTransactionalAnnotation
	 * @return
	 * @throws Throwable
	 */
	private DtsPropagation wrapInDtsSession(final ProceedingJoinPoint jp,
			final DtsTransactional dtsTransactionalAnnotation) throws Throwable
	{
		// We know what the return type is because of pointcut restrictions
		final DtsPropagation propagation = dtsTransactionalAnnotation.propagation();
		final DtsPropagation proceed = (DtsPropagation) jp.proceed();

		if (proceed != propagation)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Changing return type from " + proceed + " to " + propagation);
			}
		}
		return propagation;
	}
}
