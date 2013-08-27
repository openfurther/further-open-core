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
package edu.utah.further.core.util.scope;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.scope.Namespace;
import edu.utah.further.core.api.scope.NamespaceService;

/**
 * A {@link NamespaceService} implementation back by properties. All properties files in
 * /META-INF/namespaces/ which start with 'namespaces-' and end with .properties will be
 * considered. Properties defined later in the classpath will override properties defined
 * earlier in the classpath.
 * 
 * The format for properties files is as follows: 
 * namespace.[name of namespace].name=The name of your namespace 
 * namespace.[name of namespace].id=An unique integer representing the namespace
 * 
 * For example: 
 *
 * namespace.my_local_ds.name=My Local Data Source
 * namespace.my_local_ds.id=10
 * 
 * You'd then be able to call {@code Namespaces.namespaceFor("my_local_ds")}
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 22, 2013
 */
@Service
public class NamespaceServiceImpl implements NamespaceService
{
	/**
	 * Backed by properties
	 */
	private Properties properties;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.scope.NamespaceService#getNamespaceName(edu.utah.further
	 * .core.api.scope.Namespace)
	 */
	@Override
	public String getNamespaceName(final Namespace namespace)
	{
		checkAndLoadDefaultClasspathProperties();
		final String name = properties.getProperty(namespace.getLookupKey() + ".name");

		if (name == null)
		{
			throw new ApplicationException("Unable to find name for namespace: "
					+ namespace);
		}

		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.api.scope.NamespaceService#getNamespaceId(edu.utah.further
	 * .core.api.scope.Namespace)
	 * 
	 * @throws java.lang.NumberFormatException if identifier is not an integer.
	 */
	@Override
	public int getNamespaceId(final Namespace namespace)
	{
		checkAndLoadDefaultClasspathProperties();
		final String fullLookUpKey = namespace.getLookupKey() + ".id";
		final String propertyValue = properties.getProperty(fullLookUpKey);

		if (propertyValue == null)
		{
			throw new ApplicationException("Unable to find id for namespace " + namespace);
		}

		return Integer.valueOf(propertyValue).intValue();
	}

	/**
	 * Set a new value for the properties property.
	 * 
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final Properties properties)
	{
		this.properties = properties;
	}

	private void checkAndLoadDefaultClasspathProperties()
	{
		if (properties != null)
		{
			return;
		}

		final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try
		{
			final Resource[] propertyFiles = resolver
					.getResources("classpath*:/META-INF/namespaces/namespaces-*.properties");

			properties = new Properties();

			for (final Resource propertyFile : propertyFiles)
			{
				final Properties currentProperties = new Properties();
				currentProperties.load(propertyFile.getInputStream());

				for (final Entry<Object, Object> property : currentProperties.entrySet())
				{
					properties.put(property.getKey(), property.getValue());
				}
			}

		}
		catch (final IOException e)
		{
			throw new ApplicationException(
					"Unable to read properties files for NamespaceService", e);
		}

		if (properties.size() == 0)
		{
			throw new ApplicationException(
					"No namespace properties could be loaded for the NamespaceService");
		}
	}
}
