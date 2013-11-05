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

import static edu.utah.further.core.api.time.TimeUtil.ONE_SECOND;
import static edu.utah.further.ds.api.util.AttributeName.META_DATA;
import static edu.utah.further.ds.api.util.AttributeName.QUERY_CONTEXT;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.basex.query.QueryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.chain.UtilityProcessor;
import edu.utah.further.ds.api.util.StatusType;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.domain.ResultContext;
import edu.utah.further.fqe.ds.api.domain.StatusMetaData;

/**
 * Query Processor Monitor Advice is like a watch guard: it reports status of each
 * {@link UtilityProcessor} as well as handles when a {@link UtilityProcessor} fails by
 * terminating the chain. This implementation does NOT wire status processors into the
 * request chain, but invokes them directly from this class.
 * <p>
 * Currently the FURTHeR project uses Spring-AOP for its AOP implementation which is a
 * purley java based AOP implementation using dynamic proxies. In order for the following
 * aspect to advise a {@link QueryProcessor}, the processor <i>must</i> be wired as a
 * Spring bean and be available in the {@link ApplicationContext}.
 * <p>
 * Also note that due to Spring's nature of implementation, one can not advise a self
 * invoked method, that is, a method which is called from another method in the same
 * class. Spring would instead create a proxy around the method which calls the method you
 * wanted advised and your method will not be advised.
 * <p>
 * Additionally, an advised processor cannot be final.
 * <p>
 * Monitor aspect does not apply to all life cycles and should only be used on life
 * cycle's which operates with a {@link QueryContext}.
 * <p>
 * This aspect relies on status reporter dependencies. Think of it as an advice template
 * whose status notification logic is executed by function pointers. These can be wired to
 * different implementations in main/test spring contexts, or even injected from another
 * OSGi module inside an ESB.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Feb 3, 2010
 */
@Component("qpMonitorAdvice")
public final class QpMonitorAdvice extends AbstractQpMonitorAdvice
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(QpMonitorAdvice.class);

	// ========================= FIELDS ====================================

	// ========================= ADVICES ===================================

	/*
	 * (non-Javadoc)
	 * 
	 * @seef
	 * edu.utah.further.ds.api.advice.RequestProcessorAroundAdvice#doAround(org.aspectj
	 * .lang. ProceedingJoinPoint, edu.utah.further.core.api.chain.ChainRequest,
	 * edu.utah.further.core.api.chain.RequestProcessor)
	 */
	@Override
	public Object doAround(final ProceedingJoinPoint pjp, final ChainRequest request,
			final RequestProcessor requestProcessor)
	{
		printPreProcessorReport(request, requestProcessor);
		Object retVal = null;
		final long startTime = System.nanoTime();
		try
		{
			// Execute and time the processor
			retVal = pjp.proceed();
			final long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()
					- startTime);

			// Inject the proper status processor based the POST-request-processor query
			// state
			final QueryContext postQueryContext = request.getAttribute(QUERY_CONTEXT);
			final StatusType statusType = StatusType.valueOf(postQueryContext.isFailed());
			notifyStatus(request, requestProcessor, statusType, durationMillis);
			if (statusType.isFailed())
			{
				// Change return value of the processor's method to terminate chain
				retVal = Boolean.TRUE;
			}
		}
		catch (final Throwable throwable)
		{
			final long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()
					- startTime);
			// Take control of the request and execute a terminating processor; pass
			// the exception on to the status reporter
			request.setException(throwable);
			final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);
			final ResultContext resultContext = queryContext.getResultContext();
			if (throwable.getClass().isAssignableFrom(AccessDeniedException.class))
			{
				resultContext.setNumRecords(ResultContext.ACCESS_DENIED);
			}
			else
			{
				// Override all errors to be -1 results so that partial results are not
				// displayed or zero is not displayed.
				resultContext.setNumRecords(-1);
			}

			final String exceptionString = getExceptionAsString(throwable);
			log.error("QpMonitorAdvice caught the following exception in processor "
					+ requestProcessor.getName() + ": ", exceptionString);

			notifyStatus(request, requestProcessor, StatusType.FAIL, durationMillis);
			// Change return value of the processor's method to terminate chain
			retVal = Boolean.TRUE;
		}
		return retVal;
	}

	// ========================= GETTERS & SETTERS =========================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param request
	 * @param requestProcessor
	 * @param statusType
	 * @param durationMillis
	 *            duration of the process described by this status, in milliseconds
	 */
	private final void notifyStatus(final ChainRequest request,
			final RequestProcessor requestProcessor, final StatusType statusType,
			final long durationMillis)
	{
		// Decide whether to fail query based on the current status
		final QueryContext queryContext = request.getAttribute(QUERY_CONTEXT);
		final DsMetaData dsMetaData = request.getAttribute(META_DATA);

		boolean queryFailed = false;

		try
		{
			if (log.isDebugEnabled())
			{
				log.debug(dsMetaData.getName() + ": notifying current status "
						+ queryContext + " " + requestProcessor.getName() + " "
						+ statusType);
			}
			queryFailed = queryContext.isFailed();
			if (statusType.isFailed() && !queryFailed)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Failing query");
				}
				queryContext.fail();
			}
			if (log.isInfoEnabled())
			{
				log.info(String.format("Time [%-30s] %.3g sec, %s", StringUtil
						.getNameNullSafe(requestProcessor), new Double(
						(1.0d * durationMillis) / ONE_SECOND), statusType));
			}
		}
		catch (final Exception e)
		{
			log.error("An exception occurred while updating status", e);
			queryContext.fail();
		}
		finally
		{
			// In the event that anything above fails, this ensures that
			// Update query context status
			logException(request);
			setCurrentStatus(request, requestProcessor, statusType, durationMillis);
			logQueryStatus(queryContext, dsMetaData, queryFailed);
			// Notify other components of the new status
			statusReporter.notify(queryContext);
		}

	}

	/**
	 * @param request
	 */
	private void logException(final ChainRequest request)
	{
		final Throwable exception = request.getException();
		if (exception != null)
		{
			log.error("An exception occurred during query processing", exception);
		}
	}

	/**
	 * @param queryContext
	 * @param queryFailed
	 */
	private void logQueryStatus(final QueryContext queryContext,
			final DsMetaData dsMetaData, final boolean queryFailed)
	{
		if (log.isDebugEnabled())
		{
			final String mark = queryFailed ? "%" : "*";
			log.debug(StringUtils.repeat(mark, 40));
			final StatusMetaData status = queryContext.getCurrentStatus();
			log.debug(status != null ? (dsMetaData.getName() + ": " + status.getStatus())
					: Strings.NULL_TO_STRING);
			log.debug(StringUtils.repeat(mark, 40));
		}
	}

	/**
	 * @param throwable
	 * @return
	 */
	private static String getExceptionAsString(final Throwable throwable)
	{
		String exceptionString = throwable.toString();
		if (ReflectionUtil.instanceOf(throwable, ApplicationException.class))
		{
			final ApplicationException error = (ApplicationException) throwable;
			if (error.getCode() != null)
			{
				if (error.getCode().isRecoverable())
				{
					exceptionString = throwable.getClass() + ": "
							+ throwable.getMessage();
				}
			}

		}
		return exceptionString;
	}
}
