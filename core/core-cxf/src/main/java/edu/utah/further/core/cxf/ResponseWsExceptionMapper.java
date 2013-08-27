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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import org.slf4j.Logger;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.exception.ApplicationError;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.util.io.IoUtil;

/**
 * A {@link WsException} CXF unmarshaller.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Feb 25, 2009
 */
@Provider
public class ResponseWsExceptionMapper implements ResponseExceptionMapper<WsException>
{
	// ========================= FIELDS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ResponseWsExceptionMapper.class);

	// ========================= DEPENEDNCIES ==============================

	/**
	 * Executes JAXB utilities. Injected + lazily initialized via a service locator
	 * pattern and a double-locking singleton pattern.
	 */
	private volatile XmlService xmlService;

	// ========================= IMPLEMENTATION: ExceptionMapper ===========

	/**
	 * @param r
	 * @return
	 * @see org.apache.cxf.jaxrs.client.ResponseExceptionMapper#fromResponse(javax.ws.rs.core.Response)
	 */
	@SuppressWarnings("resource")
	// is is closed in finally block
	@Override
	public WsException fromResponse(final Response r)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Mapping r " + r);
		}

		InputStream is = null;
		try
		{
			is = getResponseInputStream(r);
			if (is.markSupported())
			{
				// FUR-1331: if we can create a string copy of the input stream, check if
				// it's empty and protected against unmarshalled exceptions
				final ByteArrayOutputStream copy = IoUtil.copyInputStream(is);
				final String errorXml = new String(copy.toByteArray());
				copy.close();
				if (log.isDebugEnabled())
				{
					log.debug("Error XML:" + errorXml);
				}
				if (StringUtils.isBlank(errorXml))
				{
					return new WsException(ErrorCode.INTERNAL_ERROR,
							"An error without an explicit message has occurred");
				}
			}
			final ApplicationError error = getXmlService().unmarshal(is,
					ApplicationError.class);
			return new WsException(error);
		}
		catch (final Throwable e)
		{
			return new WsException(ErrorCode.INTERNAL_ERROR,
					"Failed to unmarshal exception from XML: ", e);
		}
		finally
		{
			try
			{
				if (is != null)
				{
					is.close();
				}
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param r
	 * @return
	 */
	private InputStream getResponseInputStream(final Response r)
	{
		final Object entity = r.getEntity();
		return (ReflectionUtil.instanceOf(entity, String.class)) ? new ByteArrayInputStream(
				((String) entity).getBytes()) : (InputStream) entity;
	}

	/**
	 * Return the xmlService property.
	 *
	 * @return the xmlService
	 */
	public XmlService getXmlService()
	{
		// using the double-checked locking with volatile
		// @see http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
		if (xmlService == null)
		{
			synchronized (this)
			{
				if (xmlService == null)
				{
					xmlService = CoreCxfResourceLocator.getInstance().getXmlService();
				}
			}
		}
		return xmlService;
	}
}
