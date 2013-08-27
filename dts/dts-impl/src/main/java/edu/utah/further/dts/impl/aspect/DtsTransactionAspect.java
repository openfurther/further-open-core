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

import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;
import static org.slf4j.LoggerFactory.getLogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.dts.api.annotation.DtsPropagation;
import edu.utah.further.dts.api.annotation.DtsTransactional;
import edu.utah.further.dts.api.service.ConnectionFactory;
import edu.utah.further.dts.api.service.DtsOptions;
import edu.utah.further.dts.impl.util.DtsUtil;

/**
 * An aspect that weaves DTS session opening and closing around DTS-transactional methods.
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
public class DtsTransactionAspect
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(DtsTransactionAspect.class);

	/**
	 * Keeps track of the instance creation. Now allowing multiple instances for test
	 * suites that contain multiple declarations in different spring context files.
	 */
	// private static final SingletonInstanceManager singletonInstanceManager = new
	// SingletonInstanceManager(
	// DtsTransactionAspect.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * produces DTS connections.
	 */
	@Autowired
	private ConnectionFactory connectionFactory;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create this aspect.
	 */
	public DtsTransactionAspect()
	{
		// singletonInstanceManager.validateInstance();
		if (log.isDebugEnabled())
		{
			log.debug("Creating DTS transaction aspect");
		}
	}

	// ========================= POINTCUTS =================================

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
	@Around(value = "@within(classAnnotation) && !@annotation("
			+ DtsTransactional.CLASS_NAME + ")")
	public Object adviseClass(final ProceedingJoinPoint jp,
			final DtsTransactional classAnnotation) throws Throwable
	{
		return wrapInDtsSession(jp, classAnnotation);
	}

	/**
	 * Open a session before a DTS method invocation and close after it returns. Includes
	 * exception throwing by the method and normal return conditions.
	 */
	@Around(value = "@annotation(methodAnnotation)")
	public Object adviseMethod(final ProceedingJoinPoint jp,
			final DtsTransactional methodAnnotation) throws Throwable
	{
		return wrapInDtsSession(jp, methodAnnotation);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Execute DTS session wrapping for an applicable join point.
	 * 
	 * @param jp
	 *            proceeding join point
	 * @param dtsTransactionalAnnotation
	 *            DTS-transactionality annotation
	 * @return proceeding joint point's return value
	 * @throws Throwable
	 *             if wrapped method invocation throws an exception
	 */
	private Object wrapInDtsSession(final ProceedingJoinPoint jp,
			final DtsTransactional dtsTransactionalAnnotation) throws Throwable
	{
		// Fetch join point parameters
		final String methodName = jp.getSignature().getDeclaringTypeName() + "."
				+ jp.getSignature().getName();
		final DtsPropagation propagation = dtsTransactionalAnnotation.propagation();

		// Validate session boundaries
		final boolean sessionOpen = connectionFactory.isSessionOpen();
		if ((propagation == DtsPropagation.NEVER) && sessionOpen)
		{
			throw new IllegalStateException("DTS session boundaries violation: method "
					+ methodName + " declares propagation " + propagation
					+ " but there is currently an open DTS session");
		}
		if ((propagation == DtsPropagation.MANDATORY) && !sessionOpen)
		{
			throw new IllegalStateException("DTS session boundaries violation: method "
					+ methodName + " declares propagation " + propagation
					+ " but there is currently no open DTS session");
		}

		// Wrap method invocation in a DTS session if needed
		final boolean needToCreateNewSession = !sessionOpen
				&& (propagation == DtsPropagation.REQUIRED);
		if (needToCreateNewSession)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Starting session for " + methodName + ", propagation "
						+ propagation);
			}
			connectionFactory.startSession();
		}

		// Treat both happy and failed paths. If an exception is thrown, save it
		// in throwable; then close the session; then process throwable
		Throwable throwable = null;
		Object retval = null;
		try
		{
			retval = jp.proceed();
		}
		catch (final Throwable t)
		{
			throwable = t;
		}

		if (needToCreateNewSession)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Closing session for " + methodName);
			}
			connectionFactory.closeSession();
		}

		// ========================
		// Exception handling
		// ========================
		if (throwable != null)
		{
			// Discover if there's an options argument to the method; otherwise
			// always throw an exception
			final Object[] args = jp.getArgs();
			DtsOptions options = discoverDtsOptionsMethodArgument(args);
			if (options == null)
			{
				options = newDefaultOptions();
			}

			// Decide if to throw a FURTHeR-wrapped exception
			DtsUtil.wrapAndThrowApelonException(options.isThrowExceptionOnFailure(),
					throwable);

			// If not, throw the raw exception
			throw throwable;
		}
		return retval;
	}

	/**
	 * @param args
	 * @return
	 */
	private DtsOptions discoverDtsOptionsMethodArgument(final Object[] args)
	{
		for (final Object arg : args)
		{
			if (instanceOf(arg, DtsOptions.class))
			{
				return (DtsOptions) arg;
			}

		}
		return null;
	}

	/**
	 * Default exception handling options.
	 * 
	 * @return
	 */
	private DtsOptions newDefaultOptions()
	{
		final DtsOptions options = new DtsOptions();
		options.setThrowExceptionOnFailure(true);
		return options;
	}
}
