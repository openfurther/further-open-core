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

import static edu.utah.further.core.api.lang.ReflectionUtil.instanceOf;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.annotation.PostConstruct;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A commons HttpClient template for easy reusable usage of HttpClient
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Sep 7, 2010
 */
// @Service("httpClientTemplate")
// No annotation scanning to due to core-ws-jdk15
public class HttpClientTemplate
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(HttpClientTemplate.class);

	// ========================= FIELDS ====================================

	/**
	 * Default timeout of 1 hour.
	 */
	private int connectionTimeout = 1000 * 60 * 60;

	/**
	 * Default read timeout of 1 hour.
	 */
	private int readTimeout = 1000 * 60 * 60;

	/**
	 * Default retry of 5 times
	 */
	private int retryCount = 5;

	/**
	 * Default of 1000 so as not to bottleneck
	 */
	private int maxConnectionsPerHost = 1000;

	// ========================= DEPS ====================================

	/**
	 * Multi-threaded connection manager for exclusive access
	 */
	private final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

	/**
	 * Define the HttpClient here and pass it so we only define ONE instance as
	 * recommended in the documentation.
	 */
	private final HttpClient httpClient = new HttpClient(connectionManager);

	// ========================= GET/SET ====================================

	/**
	 * Return the connectionTimeout property.
	 *
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout()
	{
		return connectionTimeout;
	}

	/**
	 * Set a new value for the connectionTimeout property.
	 *
	 * @param connectionTimeout
	 *            the connectionTimeout to set
	 */
	public void setConnectionTimeout(final int connectionTimeout)
	{
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * Return the readTimeout property.
	 *
	 * @return the readTimeout
	 */
	public int getReadTimeout()
	{
		return readTimeout;
	}

	/**
	 * Set a new value for the readTimeout property.
	 *
	 * @param readTimeout
	 *            the readTimeout to set
	 */
	public void setReadTimeout(final int readTimeout)
	{
		this.readTimeout = readTimeout;
	}

	/**
	 * Return the retryCount property.
	 *
	 * @return the retryCount
	 */
	public int getRetryCount()
	{
		return retryCount;
	}

	/**
	 * Set a new value for the retryCount property.
	 *
	 * @param retryCount
	 *            the retryCount to set
	 */
	public void setRetryCount(final int retryCount)
	{
		this.retryCount = retryCount;
	}

	/**
	 * Return the maxConnectionsPerHost property.
	 *
	 * @return the maxConnectionsPerHost
	 */
	public int getMaxConnectionsPerHost()
	{
		return maxConnectionsPerHost;
	}

	/**
	 * Set a new value for the maxConnectionsPerHost property.
	 *
	 * @param maxConnectionsPerHost
	 *            the maxConnectionsPerHost to set
	 */
	public void setMaxConnectionsPerHost(final int maxConnectionsPerHost)
	{
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	// ========================= IMPL: HttpClientTemplate =======================

	/**
	 * Executes a HttpClient HttpMethod with the given settings of this class. This method
	 * is not responsible for releasing the connection unless there is an exception.
	 * Releasing of the connection is left to the caller since releasing the connection at
	 * this point would be pointless; the caller would not be able to read from the
	 * stream.
	 *
	 * @param method
	 *            the method to execute
	 * @return an open inputstream for reading.
	 */
	public int execute(final HttpMethod method)
	{
		Exception exception = null;
		try
		{
			return httpClient.executeMethod(method);
		}
		catch (final Exception e)
		{
			exception = e;
			throw new ApplicationException(
					"An exception occured while invoking the Http Method", e);
		}
		finally
		{
			if (exception != null)
			{
				method.releaseConnection();
			}

		}
	}

	// ========================= PRIVATE METHODS ====================================

	/**
	 * Private {@link HttpClient} initialization.
	 */
	@PostConstruct
	private final void afterPropertiesSet()
	{
		// Client is higher in the hierarchy than manager so set the parameters here
		final HttpClientParams clientParams = new HttpClientParams();
		clientParams.setConnectionManagerClass(connectionManager.getClass());
		clientParams.setConnectionManagerTimeout(connectionTimeout);
		clientParams.setSoTimeout(readTimeout);
		clientParams.setParameter("http.connection.timeout", new Integer(
				connectionTimeout));
		// A retry handler for when a connection fails
		clientParams.setParameter(HttpMethodParams.RETRY_HANDLER,
				new HttpMethodRetryHandler()
				{
					@Override
					public boolean retryMethod(final HttpMethod method,
							final IOException exception, final int executionCount)
					{
						if (executionCount >= retryCount)
						{
							// Do not retry if over max retry count
							return false;
						}
						if (instanceOf(exception, NoHttpResponseException.class))
						{
							// Retry if the server dropped connection on us
							return true;
						}
						if (instanceOf(exception, SocketException.class))
						{
							// Retry if the server reset connection on us
							return true;
						}
						if (instanceOf(exception, SocketTimeoutException.class))
						{
							// Retry if the read timed out
							return true;
						}
						if (!method.isRequestSent())
						{
							// Retry if the request has not been sent fully or
							// if it's OK to retry methods that have been sent
							return true;
						}
						// otherwise do not retry
						return false;
					}
				});
		httpClient.setParams(clientParams);

		final HttpConnectionManagerParams connectionManagerParams = connectionManager
				.getParams();
		connectionManagerParams.setDefaultMaxConnectionsPerHost(maxConnectionsPerHost);
		connectionManager.setParams(connectionManagerParams);
	}
}
