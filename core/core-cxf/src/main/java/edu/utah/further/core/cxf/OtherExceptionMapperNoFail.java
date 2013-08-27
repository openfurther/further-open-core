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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import edu.utah.further.core.api.constant.ErrorCode;
import edu.utah.further.core.api.exception.ApplicationError;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.text.StringUtil;

/**
 * A {@link WsException} CXF marshaller. Returns a HTTP response OK code suitable for
 * Rick's XQuery clients.
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
public class OtherExceptionMapperNoFail implements ExceptionMapper<Exception>
{
	// ========================= FIELDS ====================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(OtherExceptionMapperNoFail.class);

	// ========================= IMPLEMENTATION: ExceptionMapper ===========

	/**
	 * @param exception
	 * @return
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Object)
	 */
	@Override
	public Response toResponse(final Exception exception)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Mapping exception " + exception);
			log.debug("Exception stack trace: ");
			log.debug(StringUtil.getStackTraceAsString(exception));
		}
		final ResponseBuilder builder = Response.status(Status.OK);
		builder.type(MediaType.APPLICATION_XML);
		builder.entity(new ApplicationError(ErrorCode.INTERNAL_ERROR, exception
				.getMessage()));
		final Response response = builder.build();
		return response;
	}

}
