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

import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;

import java.io.InputStream;

import edu.utah.further.core.api.xml.MarshallerOptions;
import edu.utah.further.core.api.xml.XmlService;

/**
 * Returns a count response from a marshalled web service response.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Andrew Iskander {@code <andrew.iskander@utah.edu>}
 * @version Apr 9, 2012
 */
class WsDsMarshalledCountResponse<S> extends WsDsMarshalledResponse
{

	// ========================= FIELDS ====================================
	/**
	 * Count getter utility
	 */
	private final WsDsResponseCountGetter<S> countGetter;

	/**
	 * The error class
	 */
	private final Class<?> errorClass;

	/**
	 * @param countGetter
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.ds.api.ws.WsDsMarshalledResponse#getResponse()
	 */
	public WsDsMarshalledCountResponse(final WsDsResponseCountGetter<S> countGetter,
			final XmlService xmlService, final MarshallerOptions options,
			final InputStream responseStream, final Class<?> errorClass)
	{
		super(xmlService, options, responseStream);
		this.countGetter = countGetter;
		this.errorClass = errorClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.ds.api.ws.WsDsResponse#getResponse()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getResponse()
	{
		final Object response = super.getResponse();
		if (instanceOf(response, errorClass))
		{
			return (T) response;
		}
		return (T) countGetter.getCount((S) super.getResponse());
	}

}
