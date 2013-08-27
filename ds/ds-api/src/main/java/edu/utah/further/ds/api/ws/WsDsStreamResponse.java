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

import static org.apache.commons.lang.Validate.notNull;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A web service data source response implementation that returns a stream.
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
 * @version Jul 1, 2010
 */
// Package private - see factory
class WsDsStreamResponse implements WsDsResponse
{

	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(WsDsStreamResponse.class);

	// ========================= FIELDS =================================
	/**
	 * The web service response
	 */
	private final InputStream responseStream;

	// ========================= CONSTRUCTORS ============================

	/**
	 * @param records
	 * @param response
	 */
	public WsDsStreamResponse(final InputStream responseStream)
	{
		this.responseStream = responseStream;
	}

	// ========================= IMPL: WsDsResponse =======================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.ds.api.ws.WsDsResponse#getResponse()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getResponse()
	{
		notNull(responseStream);
		return (T) responseStream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.ds.api.ws.WsDsResponse#close()
	 */
	@Override
	public void close()
	{
		if (responseStream != null)
		{
			try
			{
				responseStream.close();
			}
			catch (IOException e)
			{
				// ignore
			}
		}

	}

	// ========================= GET/SET ============================

	/**
	 * Return the responseStream property.
	 * 
	 * @return the responseStream
	 */
	public InputStream getResponseStream()
	{
		return responseStream;
	}

}
