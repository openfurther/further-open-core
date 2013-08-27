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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.xml.MarshallerOptions;
import edu.utah.further.core.api.xml.XmlService;

/**
 * A web service data source marshalled response.
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
// Package private - see factory
class WsDsMarshalledResponse implements WsDsResponse
{

	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(WsDsMarshalledResponse.class);

	// ========================= FIELDS ====================================
	/**
	 * Xml marshalling service
	 */
	private final XmlService xmlService;

	/**
	 * The response stream
	 */
	private InputStream responseStream;

	/**
	 * The cached result
	 */
	private Object result;

	/**
	 * Marshaller options instantiated in the WS client
	 */
	private final MarshallerOptions options;

	// ========================= CONSTRUCTORS ====================================

	/**
	 * Constructor
	 * 
	 * @param responseStream
	 *            the ResponseStream
	 * @param classes
	 *            the class(es) that represent the marshalled result
	 */
	public WsDsMarshalledResponse(final XmlService xmlService,
			final MarshallerOptions options, final InputStream responseStream)
	{
		super();
		this.xmlService = xmlService;
		this.responseStream = responseStream;
		this.options = options;
	}

	// ========================= IMPL: WsDsResponse ===============================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.ds.api.ws.WsDsResponse#getResponse()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getResponse()
	{
		if (result == null)
		{
			try
			{
				final Object marshalledResult = xmlService.unmarshal(responseStream,
						options);
				result = marshalledResult;
			}
			catch (final JAXBException e)
			{
				throw new ApplicationException(
						"Unmarshalling exception occurred while processing result", e);
			}
			finally {
				try
				{
					responseStream.close();
				}
				catch (final IOException e)
				{
					// Ignore
				}
			}
		}

		return (T) result;
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

	// ========================= IMPL: GET/SET ===============================

	/**
	 * Set a new value for the responseStream property.
	 * 
	 * @param responseStream
	 *            the responseStream to set
	 */
	public void setResponseStream(final InputStream responseStream)
	{
		this.responseStream = responseStream;
	}

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
