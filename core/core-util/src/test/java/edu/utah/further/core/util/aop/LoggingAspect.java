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
import org.springframework.stereotype.Component;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;

/**
 * An aspect that logs Hibernate-generated physical SQL queries and their parameters to
 * the virtual repository per FUR-1567.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 16, 2011
 */
@Aspect
@Component("loggingAspect")
// Non-final for proxy subclassing
public class LoggingAspect
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

	/**
	 *
	 */
	@Pointcut("execution(public * *(..))")
	private void publicMethod()
	{
	}

	@Pointcut("publicMethod() && this(logger+)")
	private void log(final Logger logger)
	{

	}

	@Pointcut("publicMethod() && within(org.slf4j..*)")
	private void withinLogPackage()
	{

	}

	// ========================= CONSTRUCTORS ============================

	/**
	 * Explicitly declared for debugging.
	 */
	public LoggingAspect()
	{
		super();
	}

	// /**
	// * Advise a method.
	// *
	// * @return advised processor's <code>process()</code> method return type
	// */
	// @Around("log(logger)")
	// public Object aroundAdvice(final ProceedingJoinPoint pjp, final Logger logger)
	// throws Throwable
	// {
	// return advise(pjp);
	// }

	/**
	 * Advise a method.
	 *
	 * @return advised processor's <code>process()</code> method return type
	 */
	@Around("withinLogPackage()")
	public Object aroundAdviceLog(final ProceedingJoinPoint pjp) throws Throwable
	{
		return advise(pjp);
	}

	/**
	 * Advise a method.
	 *
	 * @return advised processor's <code>process()</code> method return type
	 */
	@Around("annotatedClass()")
	public Object aroundAdvice(final ProceedingJoinPoint pjp) throws Throwable
	{
		return advise(pjp);
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

	/**
	 * Set a new value for the numInvocations property.
	 *
	 * @param numInvocations
	 *            the numInvocations to set
	 */
	public void setNumInvocations(final int numInvocations)
	{
		this.numInvocations = numInvocations;
	}

	// ========================= METHODS ===============================

	/**
	 * Increment the invocation counter.
	 */
	private void incrementNumInvocations()
	{
		numInvocations++;
	}

	/**
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	private Object advise(final ProceedingJoinPoint pjp) throws Throwable
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
}
