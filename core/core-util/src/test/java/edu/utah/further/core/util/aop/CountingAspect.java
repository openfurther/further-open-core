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
package edu.utah.further.core.util.aop;

import static org.slf4j.LoggerFactory.getLogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;

/**
 * A test aspect that counts how many times it is invoked.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Jun 2, 2010
 */
@Aspect
// Non-final for proxy subclassing
public class CountingAspect
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(CountingAspect.class);

	// ========================= DEPENDENCIES ==============================

	// ========================= FIELDS ====================================

	private volatile int numInvocations = 0;

	// ========================= POINTCUTS =================================

	/**
	 * TODO: improve the AspectJ expressions to be something like (annotatedClass() ||
	 * annotatedMethod()) && processorMethod(). The last one captures (args(chainRequest)
	 * && this(requestProcessor+)) only. I couldn't get this to work with AspectJ syntax,
	 * so repeating this expression here and in
	 * {@link #annotatedMethod(ChainRequest, RequestProcessor)}.
	 */
	@Pointcut("@within(" + SomeAnnotation.CLASS_NAME + "))")
	private void annotatedClass()
	{

	}

	// ========================= ADVICES =================================

	/**
	 * Advise a method.
	 *
	 * @return advised processor's <code>process()</code> method return type
	 */
	@Around("annotatedClass()")
	public Object aroundAdvice(final ProceedingJoinPoint pjp) throws Throwable
	{
		if (log.isDebugEnabled())
		{
			log.debug("aroundAdvice(pjp " + pjp + ")");
		}
		incrementNumInvocations();
		Object retVal = null;
		try
		{
			// Execute the processor
			retVal = pjp.proceed();
			return retVal;
		}
		catch (final Throwable throwable)
		{
			throw throwable;
		}
	}

	// ========================= GET/SET ===============================

	/**
	 * Return the numInvocations property.
	 *
	 * @return the numInvocations
	 */
	public int getNumInvocations()
	{
		return numInvocations;
	}

	// ========================= METHODS ===============================

	/**
	 * Increment the invocation counter.
	 */
	private void incrementNumInvocations()
	{
		numInvocations++;
	}
}
