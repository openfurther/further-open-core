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
package edu.utah.further.core.api.ws;

import static edu.utah.further.core.api.constant.Strings.DEFAULT_ENCODING;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.lang.Builder;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.text.StringUtil;

/**
 * Builds a URL from URI parameters. Useful in REST client implementation.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Feb 27, 2009
 */
public final class UrlBuilder implements Builder<URL>
{
	// ========================= FIELDS ====================================

	/**
	 * Internal string builder.
	 */
	private final StringBuilder sb;

	/**
	 * Query parameters. Only scalar parameters supported for now.
	 */
	private final Map<String, Object> params = CollectionUtil.newMap();

	/**
	 * Encode query parameters in the URL or not.
	 */
	private boolean encode = true;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param url
	 */
	public UrlBuilder(final String url)
	{
		super();
		sb = new StringBuilder(url);
	}

	// ========================= IMPLEMENTATION: Builder<URL> ==============

	/**
	 * Build a {@link URL} object. {@link #buildAsString()} must return a valid URL if is
	 * method is called.
	 *
	 * @return
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	@Override
	public URL build()
	{
		try
		{
			final URL url = new URL(buildAsString());
			return url;
		}
		catch (final MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Return the constructed URL as a string. May be an invalid URL. The result of this
	 * method is passed into {@link #build()} for URL building.
	 *
	 * @return URL as string
	 * @see edu.utah.further.core.api.lang.Builder#build()
	 */
	public String buildAsString()
	{
		return appendParams(sb.toString());
	}

	// ========================= METHODS ===================================

	/**
	 *
	 */
	public UrlBuilder appendSlash()
	{
		append("/");
		return this;
	}

	/**
	 * @param literal
	 */
	public UrlBuilder append(final String literal)
	{
		if (literal != null)
		{
			sb.append(literal);
		}
		return this;
	}

	/**
	 * @param toEncode
	 */
	public UrlBuilder appendEncoded(final String toEncode)
	{
		try
		{
			if (toEncode != null)
			{
				// java.net.URLEncoder encodes space (' ') as a plus sign ('+'),
				// instead of %20 thus it will not be decoded properly by tomcat when the
				// request is parsed. Therefore replace all '+' by '%20'.
				// If there would have been any plus signs in the original string, they
				// would have been encoded by URLEncoder.encode()
				// (Note: the following replace call only works with JDK 1.5)
				final String encodedString = URLEncoder
						.encode(toEncode, DEFAULT_ENCODING)
						.replace("+", "%20");
				sb.append(encodedString);
			}
		}
		catch (final UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * Add a query parameter.
	 *
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 */
	public UrlBuilder queryParam(final String name, final String value)
	{
		params.put(name, value);
		return this;
	}

	/**
	 * Add a query parameter with an array value.
	 *
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter array value
	 */
	public UrlBuilder queryParam(final String name, final String[] value)
	{
		params.put(name, value);
		return this;
	}

	/**
	 * Set a new value for the encode property.
	 *
	 * @param encode
	 *            the encode to set
	 */
	public UrlBuilder setEncode(final boolean encode)
	{
		this.encode = encode;
		return this;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Append parameters to base URI.
	 *
	 * @param uri
	 *            An address that is base for adding parameters
	 * @param params
	 *            A map of parameters
	 * @return resulting URI
	 */
	private String appendParams(final String uri)
	{
		if (params.isEmpty())
		{
			return uri;
		}
		final String delim = (uri.indexOf('?') == -1) ? "?" : "&";
		return uri + delim + createQueryStringFromMap("&").toString();
	}

	/**
	 * Builds a query string from a given map of parameters.
	 *
	 * @param param
	 *            A map of parameters
	 * @param ampersand
	 *            String to use for ampersands (e.g. "&" or "&amp;" )
	 * @param encode
	 *            Whether or not to encode non-ASCII characters
	 * @return query string (with no leading "?")
	 */
	private StringBuilder createQueryStringFromMap(final String ampersand)
	{
		final StringBuilder result = new StringBuilder("");
		for (final Map.Entry<String, ?> entry : params.entrySet())
		{
			final Object o = entry.getValue();

			if (o == null)
			{
				appendQueryParameter(entry.getKey(), "", result, ampersand, encode);
			}
			else if (ReflectionUtil.instanceOf(o, String.class))
			{
				appendQueryParameter(entry.getKey(), o, result, ampersand, encode);
			}
			else if (ReflectionUtil.instanceOf(o, String[].class))
			{
				final String[] values = (String[]) o;

				for (int i = 0; i < values.length; i++)
				{
					appendQueryParameter(entry.getKey(), values[i], result, ampersand,
							encode);
				}
			}
			else
			{
				appendQueryParameter(entry.getKey(), o, result, ampersand, encode);
			}
		}

		return result;
	}

	/**
	 * Appends new key and value pair to query string.
	 *
	 * @param key0
	 *            parameter name
	 * @param value0
	 *            value of parameter
	 * @param queryString
	 *            existing query string
	 * @param ampersand
	 *            string to use for ampersand (e.g. "&" or "&amp;")
	 * @param encode
	 *            whether to encode value
	 * @return query string (with no leading "?")
	 */
	private static StringBuilder appendQueryParameter(final Object key0,
			final Object value0, final StringBuilder queryString, final String ampersand,
			final boolean encode)
	{
		if (queryString.length() > 0)
		{
			queryString.append(ampersand);
		}

		try
		{
			String key = StringUtil.getNullSafeToString(key0);
			String value = StringUtil.getNullSafeToString(value0);
			if (encode)
			{
				key = URLEncoder.encode(key0.toString(), DEFAULT_ENCODING);
				value = URLEncoder.encode(value0.toString(), DEFAULT_ENCODING);
			}
			queryString.append(key);
			queryString.append("=");
			queryString.append(value);
		}
		catch (final UnsupportedEncodingException e)
		{
			// do nothing
		}
		return queryString;
	}
}
