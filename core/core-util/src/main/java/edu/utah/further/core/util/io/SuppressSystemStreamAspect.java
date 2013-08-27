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
package edu.utah.further.core.util.io;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.io.LogLevel;
import edu.utah.further.core.api.io.SystemOutputStream;

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
public class SuppressSystemStreamAspect
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(SuppressSystemStreamAspect.class);

	/**
	 * Must be a constant expression to be part of an AspectJ annotation value.
	 */
	private static final String ANNOTATION_CLASS_NAME = "edu.utah.further.core.util.io.SuppressSystemStream";

	/**
	 * Keeps track of the instance creation.
	 */
	// private static final SingletonInstanceManager singletonInstanceManager = new
	// SingletonInstanceManager(
	// SuppressSystemStreamAspect.class);

	// ========================= DEPENDENCIES ==============================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create this aspect.
	 */
	public SuppressSystemStreamAspect()
	{
		// singletonInstanceManager.validateInstance();
		if (log.isDebugEnabled())
		{
			log.debug("Creating System stream suppression aspect");
		}
	}

	// ========================= POINTCUTS =================================

	// ========================= METHODS ===================================

	/**
	 * Open a session before a DTS method invocation and close after it returns. Includes
	 * exception throwing by the method and normal return conditions.
	 * <p>
	 * We need separate {@link #adviseClass(ProceedingJoinPoint, SuppressSystemStream)}
	 * and {@link #adviseMethod(ProceedingJoinPoint, SuppressSystemStream)} because if we
	 * try to bind to the class and method annotations in the same advice method we get an
	 * "::0 inconsistent binding" AspectJ error.
	 */
	@Around(value = "@within(classAnnotation) && !@annotation(" + ANNOTATION_CLASS_NAME
			+ ")")
	public Object adviseClass(final ProceedingJoinPoint jp,
			final SuppressSystemStreams classAnnotation) throws Throwable
	{
		return suppressStream(jp, classAnnotation);
	}

	/**
	 * Open a session before a DTS method invocation and close after it returns. Includes
	 * exception throwing by the method and normal return conditions.
	 */
	@Around(value = "@annotation(methodAnnotation)")
	public Object adviseMethod(final ProceedingJoinPoint jp,
			final SuppressSystemStreams methodAnnotation) throws Throwable
	{
		return suppressStream(jp, methodAnnotation);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Execute DTS session wrapping for an applicable join point.
	 * 
	 * @param jp
	 *            proceeding join point
	 * @param suppressStreamAnnotations
	 *            system stream suppression container annotation
	 * @return proceeding joint point's return value
	 * @throws Throwable
	 *             if wrapped method invocation throws an exception
	 */
	@SuppressWarnings("resource")
	private Object suppressStream(final ProceedingJoinPoint jp,
			final SuppressSystemStreams suppressStreamAnnotations) throws Throwable
	{
		// Read suppression annotations and create map of
		// system-stream-enum-to-original-streams
		final List<SystemOutputStream> redirectedStreams = CollectionUtil.newList();
		final Map<SystemOutputStream, PrintStream> originalStreams = CollectionUtil
				.newMap();
		for (final SuppressSystemStream streamAnnotation : suppressStreamAnnotations
				.value())
		{
			final SystemOutputStream stream = streamAnnotation.stream();
			final LogLevel level = streamAnnotation.level();

			// Save original stream
			redirectedStreams.add(stream);
			final PrintStream out = stream.getStream();
			originalStreams.put(stream, out);

			// Redirect output to suppressed LogStream
			stream.setStream(LogStream.newRedirectedStream(out, new LoggingOutputStream(
					log, level)));
		}

		// AOP: treat both happy and failed paths
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

		// Restore original streams
		for (final SystemOutputStream stream : redirectedStreams)
		{
			stream.setStream(originalStreams.get(stream));
		}

		if (throwable != null)
		{
			throw throwable;
		}
		return retval;
	}
}
