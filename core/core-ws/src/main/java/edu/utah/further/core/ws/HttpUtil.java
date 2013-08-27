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
package edu.utah.further.core.ws;

import static edu.utah.further.core.api.constant.ErrorCode.PROTOCOL_VIOLATION;
import static edu.utah.further.core.api.constant.ErrorCode.SERVER_ERROR;
import static edu.utah.further.core.api.constant.ErrorCode.TRANSPORT_ERROR;
import static org.apache.commons.lang.Validate.isTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.utah.further.core.api.exception.ApplicationError;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.ws.HttpHeader;
import edu.utah.further.core.api.xml.XmlService;

/**
 * Centralizes generic HTTP utilities.
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
 * @version Oct 13, 2008
 */
public class HttpUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * Number of trials that an HTTP client tries before failing an HTTP method.
	 */
	public static final int NUMBER_HTTP_REQUEST_TRIALS = 3;

	/**
	 * MIME type strings regular expression.
	 */
	public static final String MIME_TYPE_REGEX = "\\w*/\\w*";

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Log log = LogFactory.getLog(HttpUtil.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * JAXB utilities.
	 */
	private XmlService xmlService;

	// ========================= STATIC METHODS ============================

	/**
	 * Convert a MIME type string in the format "type/subType" to a CXF media type.
	 * 
	 * @param mimeTypeString
	 *            a MIME type string in the format "type/subType", e.g. "application/xml".
	 *            <i>Must match this MIME type string regular expression performed
	 *            here</i>
	 * @return corresponding CXF {@link MediaType} object, or <code>null</code> if
	 *         <code>mimeTypeString</code> is not in the correct format
	 */
	public static MediaType newMediaType(final String mimeTypeString)
	{
		if ((mimeTypeString != null) && mimeTypeString.matches(MIME_TYPE_REGEX))
		{
			final String[] parts = mimeTypeString.split("/");
			return new MediaType(parts[0], parts[1]);
		}
		return null;
	}

	/**
	 * Static method for retrieving the response from an already executed method. This
	 * method does not responsible for closing the inputstream unless an exception occurs
	 * during retrieval of the response.
	 * 
	 * @param method
	 * @return
	 */
	public static InputStream getHttpResponseStream(final HttpMethod method)
	{
		isTrue(method.hasBeenUsed());

		Exception exception = null;
		try
		{
			return method.getResponseBodyAsStream();
		}
		catch (final IOException e)
		{
			exception = e;
			throw new ApplicationException(
					"An exception occured while retrieving the response", e);
		}
		finally
		{
			if (exception != null)
			{
				method.releaseConnection();
			}
		}
	}

	/**
	 * Open an HTTP connection to a URL and read the returned response into an object.
	 * 
	 * @param url
	 *            remote URL (usually a web service's URL)
	 * @param httpMethod
	 *            HTTP method
	 * @return <code>HttpClient</code> {@link HttpMethod} transfer object, containing the
	 *         response headers and body
	 */
	public static HttpResponseTo getHttpResponseToDefaultMethod(final String url,
			final edu.utah.further.core.api.ws.HttpMethod httpMethod)
	{
		return getHttpResponse(url, newMethod(url, httpMethod));
	}

	/**
	 * Open an HTTP GET method connection to a URL and read the returned response into an
	 * object.
	 * 
	 * @param url
	 *            remote URL (usually a web service's URL)
	 * @param httpMethod
	 *            HTTP method
	 * @return <code>HttpClient</code> {@link HttpMethod} transfer object, containing the
	 *         response headers and body
	 */
	public static HttpResponseTo getHttpResponse(final String url, final HttpMethod method)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Sending HTTP " + method + " request to " + url);
		}

		// Create an instance of HttpClient.
		final HttpClient client = new HttpClient();

		// Create a method instance.
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(NUMBER_HTTP_REQUEST_TRIALS, false));
		try
		{
			// Execute the method
			final int statusCode = client.executeMethod(method);
			if (statusCode >= 400)
			{
				log.error("Method failed: " + method.getStatusLine());
			}
			return new HttpResponseTo(method);
		}
		catch (final Throwable e)
		{
			if (log.isInfoEnabled())
			{
				log.info("Failed to receive HTTP request from " + url + ": "
						+ e.getMessage());
			}
		}
		return null;
	}

	/**
	 * @param url
	 * @param data
	 * @return
	 */
	public static PostMethod createPostMethod(final String url, final NameValuePair[] data)
	{
		final PostMethod method = new PostMethod(url);
		//method.addRequestHeader(HttpHeader.CONTENT_TYPE.getName(), "text/plain");
		method.setRequestBody(data);
		// Provide custom retry handler is necessary
		return method;
	}

	/**
	 * Create an HTTP POST method with a string body. This calls the deprecated
	 * {@link PostMethod#setRequestBody(String)}, but since we are not planning on using
	 * newer version of the HttpClient library, and this was verified to work with version
	 * 3.0.1 that matches i2b2's axis2 web application version, we add suppress the
	 * deprecation warning here. If there's ever a problem, use
	 * {@link #createPostMethod(String, NameValuePair[])} instead, which should require
	 * only minor changes to the FURTHeR i2b2 hook and web service interface.
	 * 
	 * @param url
	 *            target HTTP URL
	 * @param body
	 *            POST body
	 * @return HTTP POST method with the URL and body
	 */
	@SuppressWarnings("deprecation")
	public static PostMethod createPostMethod(final String url, final String body)
	{
		final PostMethod method = new PostMethod(url);
		// Set both upper and lower case headers, just in case
		method.addRequestHeader(HttpHeader.CONTENT_TYPE.getName(), "application/xml");
		method.addRequestHeader(HttpHeader.CONTENT_TYPE.getName().toLowerCase(),
				"application/xml");
		method.setRequestBody(body);
		// Provide custom retry handler is necessary
		return method;
	}

	// /**
	// * Get an input stream's content as a string.
	// *
	// * @param in
	// * input string
	// * @return input stream's content as a string
	// * @throws IOException
	// */
	// public static String getStringFromInputStream(final InputStream in)
	// throws IOException
	// {
	// final CachedOutputStream bos = new CachedOutputStream();
	// IOUtils.copy(in, bos);
	// in.close();
	// bos.close();
	// final String xmlString = bos.getOut().toString();
	// if (log.isDebugEnabled())
	// {
	// log.debug(xmlString);
	// }
	// return xmlString;
	// }

	/**
	 * Read an entity from an XML string.
	 * 
	 * @param <T>
	 * @param implementationType
	 * @param xmlString
	 * @param additionalRootClasses
	 *            root classes to add to the Aegis context in addition to
	 *            <code>entity.getClass()</code>
	 * @param addImplementationTypeAsRootClass
	 *            iff <code>true</code>, implementation type is added to the list of Aegis
	 *            root classes
	 * @return
	 * @throws Exception
	 */
	public <T> T readEntityFromString(final Class<T> implementationType,
			final String xmlString, final Set<Class<?>> additionalRootClasses,
			final boolean addImplementationTypeAsRootClass) throws Exception
	{
		// TODO: improve this code by passing a set of root classes to reader.unmarshal()
		// that includes both implementationType and ApplicationError class. In this way,
		// we unmarshal only once instead of twice.
		try
		{
			final InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
			final T result = (T) xmlService.unmarshal(inputStream, implementationType);
			return result;
		}
		catch (final Exception e)
		{
			final InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
			final ApplicationError error = xmlService.unmarshal(inputStream,
					ApplicationError.class);
			throw new WsException(error);
		}
	}

	/**
	 * Open an HTTP GET method connection to a URL and read the returned response into an
	 * object.
	 * 
	 * @param <T>
	 *            return type
	 * @param url
	 *            remote URL (usually a web service's URL)
	 * @param timeoutSecs
	 *            HTTP socket connection timeout [seconds]
	 * @return <code>HttpClient</code> {@link HttpMethod} transfer object, containing the
	 *         response headers and body
	 * @throws WsException
	 *             if an error as occurred either in transport or protocol
	 */
	public static HttpResponseTo getHttpGetResponseBody(final String url,
			final int timeoutSecs) throws WsException
	{
		// Create an instance of HttpClient with appropriate parameters
		final SimpleHttpConnectionManager cm = new SimpleHttpConnectionManager();
		final HttpConnectionManagerParams params = cm.getParams();
		params.setConnectionTimeout(timeoutSecs * 1000);
		params.setSoTimeout(timeoutSecs * 1000);
		cm.setParams(params);
		final HttpClient client = new HttpClient(cm);

		// Create a method instance
		final GetMethod method = new GetMethod(url);

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(NUMBER_HTTP_REQUEST_TRIALS, false));
		try
		{
			// Execute the method
			/* final int statusCode = */client.executeMethod(method);
			// if (statusCode != HttpStatus.SC_OK)
			// {
			// log.error("Method failed: " + method.getStatusLine());
			// }
			return new HttpResponseTo(method);
		}
		catch (final HttpException e)
		{
			// HTTP protocol violation (e.g. bad method?!)
			throw new WsException(PROTOCOL_VIOLATION, e.getMessage());
		}
		catch (final IOException e)
		{
			// Transport error. Could be an invalid URL
			throw new WsException(TRANSPORT_ERROR, e.getMessage());
		}
		catch (final Throwable e)
		{
			// Exceptions
			throw new WsException(SERVER_ERROR, e.getMessage());
		}
		finally
		{
			// Important: release the connection
			method.releaseConnection();
		}
	}

	/**
	 * Open an HTTP GET method connection to a URL, read the returned XML response, and
	 * unmarshal the returned result into a Java object. Assuming a text/xml response
	 * type.
	 * 
	 * @param <T>
	 *            return type
	 * @param implementationType
	 *            return type
	 * @param url
	 *            remote URL (usually a web service's URL)
	 * @param provider
	 *            entity XML marshalling provider
	 * @param additionalRootClasses
	 *            root classes to add to the Aegis context in addition to
	 *            <code>entity.getClass()</code>
	 * @param addImplementationTypeAsRootClass
	 *            iff <code>true</code>, implementation type is added to the list of Aegis
	 *            root classes
	 * @param timeoutSecs
	 *            HTTP socket connection timeout [seconds]
	 * @return unmarshalled result
	 * @throws WsException
	 *             if an error entity was returned from the URL
	 */
	public <T> T getHttpGetResult(final Class<T> implementationType, final String url,
			final Set<Class<?>> additionalRootClasses,
			final boolean addImplementationTypeAsRootClass, final int timeoutSecs)
			throws WsException
	{
		try
		{
			// Send request and get response body as input stream bytes (non-chucked
			// retrieval)
			final HttpResponseTo response = getHttpGetResponseBody(url, timeoutSecs);
			final byte[] responseBody = response.getResponseBody();
			final String xmlString = new String(responseBody);
			final InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
			return (T) xmlService.unmarshal(inputStream, implementationType);
		}
		catch (final Exception e)
		{
			// Exceptions
			throw new WsException(SERVER_ERROR, e.getMessage());
		}
	}

	// ========================= DEPENDENCY INJECTION ======================

	/**
	 * Set a new value for the xmlService property.
	 * 
	 * @param xmlService
	 *            the xmlService to set
	 */
	public void setXmlService(final XmlService xmlService)
	{
		this.xmlService = xmlService;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param url
	 * @param httpMethod
	 * @return
	 */
	private static HttpMethod newMethod(final String url,
			final edu.utah.further.core.api.ws.HttpMethod httpMethod)
	{
		switch (httpMethod)
		{
			case GET:
			{
				return newGetMethod(url);
			}

			default:
			{
				throw new UnsupportedOperationException("Unrecognized http method "
						+ httpMethod);
			}
		}
	}

	/**
	 * @param url
	 * @return
	 */
	private static HttpMethod newGetMethod(final String url)
	{
		final HttpMethod method = new GetMethod(url);

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(NUMBER_HTTP_REQUEST_TRIALS, false));
		return method;
	}
}
