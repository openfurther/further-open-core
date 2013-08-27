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
package edu.utah.further.ds.impl.service.query.processor;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.chain.AbstractNonDelegatingUtilityProcessor;
import edu.utah.further.ds.api.util.AttributeName;

/**
 * A processor which ensures that the result stream is properly closed if it is still
 * open. If the stream result is read to completion, the stream is typically automatically
 * close but in the case of MAX_RESULTS applied to the result stream, the stream is left
 * open and this processor will ensure that it is closed.
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
 * @version Oct 14, 2010
 */
public final class StreamFinalizerQp extends AbstractNonDelegatingUtilityProcessor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(StreamFinalizerQp.class);

	// ========================= Impl: RequestHandler ======================

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.utah.further.core.api.chain.RequestProcessor#process(edu.utah.further.core.
	 * api.chain.ChainRequest)
	 */
	@Override
	public boolean process(final ChainRequest request)
	{
		// Read input parameters from the request
		final Object input = request.getAttribute(AttributeName.QUERY_RESULT);

		// Clean up
		request.setAttribute(AttributeName.QUERY_RESULT, null);

		// Ensure we have an InputStream
		if (!ReflectionUtil.instanceOf(input, InputStream.class))
		{
			// Log the error and return unhandled
			log
					.error("Unable to finalize stream, input is not an instance of InputStream but instead is "
							+ input.getClass());
			return false;
		}

		try (InputStream inputStream = (InputStream) input) {
			//do nothing - we just want to close it!
		}
		catch (final IOException e)
		{
			log.error("Unable to close Inputstream", e);
		}

		return false;
	}
}
