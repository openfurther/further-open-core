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
package edu.utah.further.ds.api.ws;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.xml.MarshallerOptions;
import edu.utah.further.core.api.xml.XmlService;

/**
 * A factory for WsDsResponse package private implementations.
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
 * @version Sep 8, 2010
 */
public final class WsDsResponseFactory
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(WsDsResponseFactory.class);

	// ========================= IMPL: WsDsResponseFactory =======================
	/**
	 * Returns a streamed response.
	 * 
	 * @param inputStream
	 * @return
	 */
	public static final WsDsResponse streamedResponse(final InputStream responseStream)
	{
		return new WsDsStreamResponse(responseStream);
	}

	/**
	 * Return a marshalled response.
	 * 
	 * @param responseStream
	 * @param classes
	 * @return
	 */
	public static final WsDsResponse marshalledResponse(final XmlService xmlService,
			final MarshallerOptions options, final InputStream responseStream)
	{
		return new WsDsMarshalledResponse(xmlService, options, responseStream);
	}

	/**
	 * Returns a marshalled count response.
	 * 
	 * @param responseStream
	 * @param classes
	 * @return
	 */
	public static final <T> WsDsResponse marshalledCountResponse(
			final WsDsResponseCountGetter<T> countGetter, final XmlService xmlService,
			final MarshallerOptions options, final InputStream responseStream,
			final Class<?> errorClass)
	{
		return new WsDsMarshalledCountResponse<>(countGetter, xmlService, options,
				responseStream, errorClass);
	}
}
