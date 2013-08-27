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
package edu.utah.further.ds.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.chain.UtilityProcessor;
import edu.utah.further.ds.api.advice.RequestProcessorAroundAdvice;

/**
 * An object decorator of a Query Processor around-advice.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Mar 11, 2010
 */
@Aspect
// Non-final for proxy subclassing
public class QpAroundAspect
{
	// ========================= FIELDS ==================================

	/**
	 * The around-advice to execute.
	 */
	private RequestProcessorAroundAdvice aroundAdvice;

	// ========================= POINTCUTS ===============================

	/**
	 * TODO: improve the AspectJ expressions to be something like (annotatedClass() ||
	 * annotatedMethod()) && processorMethod(). The last one captures (args(chainRequest)
	 * && this(requestProcessor+)) only. I couldn't get this to work with AspectJ syntax,
	 * so repeating this expression here and in
	 * {@link #annotatedMethod(ChainRequest, RequestProcessor)}.
	 */
	@Pointcut("@within(" + UtilityProcessor.CLASS_NAME + ")"
			+ " && args(chainRequest) && this(requestProcessor+)")
	private void annotatedClass(final ChainRequest chainRequest,
			final RequestProcessor requestProcessor)
	{

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
	@Around("annotatedClass(chainRequest, requestProcessor)")
	public Object aroundAdvice(final ProceedingJoinPoint pjp,
			final ChainRequest chainRequest, final RequestProcessor requestProcessor)
	{
		return aroundAdvice.doAround(pjp, chainRequest, requestProcessor);
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
