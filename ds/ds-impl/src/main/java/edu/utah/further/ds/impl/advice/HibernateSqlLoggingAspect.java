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
package edu.utah.further.ds.impl.advice;

import org.apache.log4j.Priority;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.ds.api.advice.RequestProcessorAroundAdvice;

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
@Component("hibernateSqlLoggingAspect")
// Non-final for proxy subclassing
public class HibernateSqlLoggingAspect
{
	// ========================= FIELDS ==================================

	/**
	 * The around-advice to execute.
	 */
	private RequestProcessorAroundAdvice aroundAdvice;

	// ========================= POINTCUTS =================================

	/**
	 *
	 */
	@Pointcut("execution(public * log(..))")
	private void logMethod()
	{
	}

	@Pointcut("within(org.apache.log4j..*)")
	private void inLoggerClass()
	{
	}

	@Pointcut("logMethod() && inLoggerClass()")
	private void logOperation()
	{
	}

	/**
	 * TODO: improve the AspectJ expressions to be something like (annotatedClass() ||
	 * annotatedMethod()) && processorMethod(). The last one captures (args(chainRequest)
	 * && this(requestProcessor+)) only. I couldn't get this to work with AspectJ syntax,
	 * so repeating this expression here and in
	 * {@link #annotatedMethod(ChainRequest, RequestProcessor)}.
	 */
	@Pointcut("logOperation() && args(priority,message,..)")
	private void logOperationWithArgs(final Priority priority, final Object message)
	{

	}

	// ========================= CONSTRUCTORS ============================

	/**
	 * Explicitly declared for debugging.
	 */
	public HibernateSqlLoggingAspect()
	{
		super();
	}

	// ========================= ADVICES =================================

	/**
	 * Advise a processor's <code>process()</code> method.
	 * 
	 * @param pjp
	 *            join point information (required for an around advice such as this one)
	 * @param chainRequest
	 *            chain request argument of the <code>process()</code> method
	 * @param requestProcessor
	 *            the advised request processor instance
	 * @return advised processor's <code>process()</code> method return type
	 */
	// @Around("logOperationWithArgs(priority,message)")
	// public Object aroundAdvice(final ProceedingJoinPoint pjp, final Priority priority,
	// final Object message) throws Throwable
	@Around("logOperation()")
	public Object aroundAdvice(final ProceedingJoinPoint pjp) throws Throwable
	{
		System.out.println("Advice invoked for " + pjp);
		Object retVal = null;
		try
		{
			// Execute and time the processor
			retVal = pjp.proceed();
			return retVal;
		}
		catch (final Throwable t)
		{
			throw t;
		}
	}

	// ========================= GET/SET ===============================

	/**
	 * @return the aroundAdvice
	 */
	public RequestProcessorAroundAdvice getAroundAdvice()
	{
		return aroundAdvice;
	}

	/**
	 * @param aroundAdvice
	 *            the aroundAdvice to set
	 */
	public void setAroundAdvice(final RequestProcessorAroundAdvice aroundAdvice)
	{
		this.aroundAdvice = aroundAdvice;
	}

}
