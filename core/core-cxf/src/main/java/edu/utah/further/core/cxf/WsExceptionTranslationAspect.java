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
package edu.utah.further.core.cxf;

import static org.slf4j.LoggerFactory.getLogger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.lang.ReflectionUtil;

/**
 * An aspect that translates {@link ApplicationException} to {@link WsException}s in web
 * service methods. Inspired by Spring's hibernate exception translation support. weaves
 * DTS session opening and closing around DTS-transactional methods.
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
 * @version Oct 23, 2009
 */
@Aspect
public class WsExceptionTranslationAspect
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(WsExceptionTranslationAspect.class);

	/**
	 * Must be a constant expression to be part of an AspectJ annotation value.
	 */
	private static final String WEB_SERVICE_SOAP_ANNOTATION_CLASS = "javax.jws.WebService";
	private static final String WEB_SERVICE_REST_ANNOTATION_CLASS = "javax.ws.rs.Path";

	// private static final String WEB_SERVICE_REST_ANNOTATION_METHOD =
	// WEB_SERVICE_REST_ANNOTATION_CLASS;

	// ========================= DEPENDENCIES ==============================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create this aspect.
	 */
	public WsExceptionTranslationAspect()
	{
		// singletonInstanceManager.validateInstance();
		if (log.isDebugEnabled())
		{
			log.debug("Creating web service exception translation aspect");
		}
	}

	// ========================= POINTCUTS =================================
	@Pointcut("execution(public * *(..))")
	private void publicMethod()
	{

	}

	// ========================= METHODS ===================================

	/**
	 * Translate exceptions in all public SOAP web service class methods.
	 *
	 * @param jp
	 * @param ex
	 * @throws Throwable
	 */
	@AfterThrowing(pointcut = "@within(" + WEB_SERVICE_SOAP_ANNOTATION_CLASS
			+ ") && publicMethod()", throwing = "ex")
	public void adviseSoapClass(final JoinPoint jp, final Exception ex) throws Throwable
	{
		throwWsException(jp, ex);
	}

	/**
	 * Translate exceptions in all public REST web service class methods.
	 *
	 * @param jp
	 * @param ex
	 * @throws Throwable
	 */
	@AfterThrowing(pointcut = "@within(" + WEB_SERVICE_REST_ANNOTATION_CLASS
			+ ") && publicMethod()", throwing = "ex")
	public void adviseRestClass(final JoinPoint jp, final Exception ex) throws Throwable
	{
		throwWsException(jp, ex);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Translate exceptions for an applicable join point.
	 *
	 * @param jp
	 * @param ex
	 * @throws WsException
	 */
	private void throwWsException(final JoinPoint jp, final Exception ex)
			throws WsException
	{
		if (log.isDebugEnabled())
		{
			log.debug("Translating exceptions for " + jp);
		}
		if (ReflectionUtil.instanceOf(ex, WsException.class))
		{
			throw (WsException) ex;
		}
		else if (ReflectionUtil.instanceOf(ex, ApplicationException.class))
		{
			throw new WsException((ApplicationException) ex);
		}
		else
		{
			throw new WsException(ErrorCode.INTERNAL_ERROR, ex.getMessage());
		}
	}
}
