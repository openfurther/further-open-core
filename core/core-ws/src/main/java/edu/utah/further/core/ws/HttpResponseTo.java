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

import static edu.utah.further.core.ws.HttpUtil.newMediaType;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HeaderGroup;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.ws.HttpHeader;

/**
 * An HTTP response transfer object. Saves the response body and headers.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Apr 6, 2009
 */
public final class HttpResponseTo
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(HttpResponseTo.class);

	// ========================= FIELDS ====================================

	/**
	 * An type-safe name of the HTTP method, e.g. "GET" or "POST".
	 */
	private edu.utah.further.core.api.ws.HttpMethod httpMethod;

	/**
	 * The Status-Line from the response.
	 */
	private StatusLine statusLine = null;

	/**
	 * Response headers, if any..
	 */
	private final HeaderGroup responseHeaders = new HeaderGroup();

	/**
	 * Path of the HTTP method.
	 */
	private String path = null;

	/**
	 * Query string of the HTTP method, if any.
	 */
	private String queryString = null;

	/**
	 * Buffer for the response.
	 */
	private byte[] responseBody = null;

	/**
	 * HTTP protocol parameters.
	 */
	private HttpMethodParams params = new HttpMethodParams();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * No-arg constructor.
	 */
	public HttpResponseTo()
	{
		super();
	}

	/**
	 * Copy-constructor from an {@link HttpMethod}.
	 * 
	 * @param method
	 *            method to copy fields from
	 */
	public HttpResponseTo(final HttpMethod method) throws IOException
	{
		this.httpMethod = edu.utah.further.core.api.ws.HttpMethod.valueOf(method
				.getName());
		this.statusLine = method.getStatusLine();

		for (final Header header : method.getResponseHeaders())
		{
			this.responseHeaders
					.addHeader(new Header(header.getName(), header.getValue()));
		}

		this.path = method.getPath();
		this.queryString = method.getQueryString();
		this.responseBody = method.getResponseBody();
		setParams(method.getParams());
	}

	// ========================= GETTERS & SETTERS =========================

	// ------------------------------------------- Property Setters and Getters

	/**
	 * Obtains the type-safe name of the HTTP method as used in the HTTP request line, for
	 * example <tt>"GET"</tt> or <tt>"POST"</tt>.
	 * 
	 * @return the name of this method
	 */
	public edu.utah.further.core.api.ws.HttpMethod getHttpMethod()
	{
		return httpMethod;
	}

	/**
	 * Gets the path of this HTTP method. Calling this method <em>after</em> the request
	 * has been executed will return the <em>actual</em> path, following any redirects
	 * automatically handled by this HTTP method.
	 * 
	 * @return the path to request or "/" if the path is blank.
	 */
	public String getPath()
	{
		return (path == null || path.equals("")) ? "/" : path;
	}

	/**
	 * Sets the path of the HTTP method. It is responsibility of the caller to ensure that
	 * the path is properly encoded (URL safe).
	 * 
	 * @param path
	 *            the path of the HTTP method. The path is expected to be URL-encoded
	 */
	public void setPath(final String path)
	{
		this.path = path;
	}

	/**
	 * Gets the query string of this HTTP method.
	 * 
	 * @return The query string
	 */
	public String getQueryString()
	{
		return queryString;
	}

	/**
	 * Sets the query string of this HTTP method. The caller must ensure that the string
	 * is properly URL encoded. The query string should not start with the question mark
	 * character.
	 * 
	 * @param queryString
	 *            the query string
	 * 
	 * @see EncodingUtil#formUrlEncode(NameValuePair[], String)
	 */
	public void setQueryString(final String queryString)
	{
		this.queryString = queryString;
	}

	/**
	 * Gets the {@link HeaderGroup header group} storing the response headers.
	 * 
	 * @return a HeaderGroup
	 * 
	 * @since 2.0beta1
	 */
	protected HeaderGroup getResponseHeaderGroup()
	{
		return responseHeaders;
	}

	/**
	 * @see org.apache.commons.httpclient.HttpMethod#getResponseHeaders(java.lang.String)
	 * 
	 * @since 3.0
	 */
	public Header[] getResponseHeaders(final String headerName)
	{
		return getResponseHeaderGroup().getHeaders(headerName);
	}

	/**
	 * Returns an array of the response headers that the HTTP method currently has in the
	 * order in which they were read.
	 * 
	 * @return an array of response headers.
	 */
	public Header[] getResponseHeaders()
	{
		return getResponseHeaderGroup().getAllHeaders();
	}

	/**
	 * Gets the response header associated with the given name. Header name matching is
	 * case insensitive. <tt>null</tt> will be returned if either <i>headerName</i> is
	 * <tt>null</tt> or there is no matching header for <i>headerName</i>.
	 * 
	 * @param headerName
	 *            the header name to match
	 * 
	 * @return the matching header
	 */
	public Header getResponseHeader(final String headerName)
	{
		if (headerName == null)
		{
			return null;
		}
		return getResponseHeaderGroup().getCondensedHeader(headerName);
	}

	/**
	 * Returns the response status code.
	 * 
	 * @return the status code associated with the latest response.
	 */
	public int getStatusCode()
	{
		return statusLine.getStatusCode();
	}

	/**
	 * Provides access to the response status line.
	 * 
	 * @return the status line object from the latest response.
	 * @since 2.0
	 */
	public StatusLine getStatusLine()
	{
		return statusLine;
	}

	/**
	 * Checks if response data is available.
	 * 
	 * @return <tt>true</tt> if response data is available, <tt>false</tt> otherwise.
	 */
	private boolean isResponseAvailable()
	{
		return (responseBody != null);
	}

	/**
	 * Return the length (in bytes) of the response body, as specified in a
	 * <tt>Content-Length</tt> header.
	 * 
	 * <p>
	 * Return <tt>-1</tt> when the content-length is unknown.
	 * </p>
	 * 
	 * @return content length, if <tt>Content-Length</tt> header is available. <tt>0</tt>
	 *         indicates that the request has no body. If <tt>Content-Length</tt> header
	 *         is not present, the method returns <tt>-1</tt>.
	 */
	public long getResponseContentLength()
	{
		final Header[] headers = getResponseHeaderGroup().getHeaders(
				HttpHeader.CONTENT_LENGTH.getName());
		if (headers.length == 0)
		{
			return -1;
		}
		if (headers.length > 1)
		{
			throw new ApplicationException("Multiple content-length headers detected");
		}
		final Header header = headers[0];
		try
		{
			return Long.parseLong(header.getValue());
		}
		catch (final NumberFormatException e)
		{
			throw new ApplicationException("Invalid content-length value", e);
		}
	}

	/**
	 * Returns the response body of the HTTP method, if any, as an array of bytes. If
	 * response body is not available or cannot be read, returns <tt>null</tt>.
	 * 
	 * @return The response body.
	 */
	public byte[] getResponseBody()
	{
		return this.responseBody;
	}

	/**
	 * Returns the response body of the HTTP method, if any, as a {@link String}. If
	 * response body is not available or cannot be read, returns <tt>null</tt> The string
	 * conversion on the data is done using the character encoding specified in
	 * <tt>Content-Type</tt> header. Buffers the response and this method can be called
	 * several times yielding the same result each time.
	 * 
	 * Note: This will cause the entire response body to be buffered in memory. A
	 * malicious server may easily exhaust all the VM memory. It is strongly recommended,
	 * to use getResponseAsStream if the content length of the response is unknown or
	 * resonably large.
	 * 
	 * @return The response body or <code>null</code>.
	 * 
	 * @throws IOException
	 *             If an I/O (transport) problem occurs while obtaining the response body.
	 */
	public String getResponseBodyAsString() throws IOException
	{
		final byte[] rawdata = isResponseAvailable() ? getResponseBody() : null;
		return (rawdata != null) ? EncodingUtil.getString(rawdata, getResponseCharSet())
				: null;
	}

	/**
	 * Returns the status text (or "reason phrase") associated with the latest response.
	 * 
	 * @return The status text.
	 */
	public String getStatusText()
	{
		return statusLine.getReasonPhrase();
	}

	/**
	 * Returns {@link HttpMethodParams HTTP protocol parameters} associated with this
	 * method.
	 * 
	 * @return HTTP parameters.
	 * 
	 * @since 3.0
	 */
	public HttpMethodParams getParams()
	{
		return this.params;
	}

	/**
	 * Assigns {@link HttpMethodParams HTTP protocol parameters} for this method.
	 * 
	 * @since 3.0
	 * 
	 * @see HttpMethodParams
	 */
	public void setParams(final HttpMethodParams params)
	{
		if (params == null)
		{
			throw new IllegalArgumentException("Parameters may not be null");
		}
		this.params = params;
	}

	/**
	 * Per RFC 2616 section 4.3, some response can never contain a message body.
	 * 
	 * @param status
	 *            - the HTTP status code
	 * 
	 * @return <tt>true</tt> if the message may contain a body, <tt>false</tt> if it can
	 *         not contain a message body
	 */
	public boolean isCanResponseHaveBody()
	{
		final int status = getStatusCode();
		return (!((status >= 100 && status <= 199) || (status == 204) || (status == 304)));
	}

	/**
	 * Returns the character encoding of the response from the <tt>Content-Type</tt>
	 * header.
	 * 
	 * @return String The character set.
	 */
	public String getResponseCharSet()
	{
		return getContentCharSet(getResponseHeader(HttpHeader.CONTENT_TYPE.getName()));
	}

	/**
	 * Returns the MIME type of the response from the <tt>Content-Type</tt> header.
	 * 
	 * @return String the response MIME type
	 */
	public MediaType getMediaType()
	{
		final Header contentType = getResponseHeader(HttpHeader.CONTENT_TYPE.getName());
		return (contentType == null) ? null : newMediaType(contentType.getValue());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Generates HTTP request line according to the specified attributes.
	 * 
	 * @param connection
	 *            the {@link HttpConnection connection} used to execute this HTTP method
	 * @param name
	 *            the method name generate a request for
	 * @param requestPath
	 *            the path string for the request
	 * @param query
	 *            the query string for the request
	 * @param version
	 *            the protocol version to use (e.g. HTTP/1.0)
	 * 
	 * @return HTTP request line
	 */
	protected static String generateRequestLine(final HttpConnection connection,
			final String name, final String requestPath, final String query,
			final String version)
	{
		final StringBuffer buf = new StringBuffer();
		// Append method name
		buf.append(name).append(Strings.SPACE_STRING);
		// Absolute or relative URL?
		if (!connection.isTransparent())
		{
			final Protocol protocol = connection.getProtocol();
			buf.append(protocol.getScheme().toLowerCase());
			buf.append("://");
			buf.append(connection.getHost());
			if ((connection.getPort() != -1)
					&& (connection.getPort() != protocol.getDefaultPort()))
			{
				buf.append(":");
				buf.append(connection.getPort());
			}
		}
		// Append path, if any
		if (requestPath == null)
		{
			buf.append("/");
		}
		else
		{
			if (!connection.isTransparent() && !requestPath.startsWith("/"))
			{
				buf.append("/");
			}
			buf.append(requestPath);
		}
		// Append query, if any
		if (query != null)
		{
			if (query.indexOf("?") != 0)
			{
				buf.append("?");
			}
			buf.append(query);
		}
		// Append protocol
		buf.append(Strings.SPACE_STRING)
				.append(version)
				.append(Strings.UNIX_NEW_LINE_STRING);
		return buf.toString();
	}

	/**
	 * Returns the character set from the <tt>Content-Type</tt> header.
	 * 
	 * @param contentheader
	 *            The content header.
	 * @return String The character set.
	 */
	protected String getContentCharSet(final Header contentheader)
	{
		String charset = null;
		if (contentheader != null)
		{
			final HeaderElement values[] = contentheader.getElements();
			// I expect only one header element to be there
			// No more. no less
			if (values.length == 1)
			{
				final NameValuePair param = values[0].getParameterByName("charset");
				if (param != null)
				{
					// If I get anything "funny"
					// UnsupportedEncondingException will result
					charset = param.getValue();
				}
			}
		}
		if (charset == null)
		{
			charset = getParams().getContentCharset();
		}
		return charset;
	}

}
