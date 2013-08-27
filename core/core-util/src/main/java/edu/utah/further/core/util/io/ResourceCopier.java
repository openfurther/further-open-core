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
package edu.utah.further.core.util.io;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

/**
 * A utility class that copies one classpath resource (file) into another file under the
 * same directory.
 * <p>
 * In particular, this is useful for copying a custom Spring Hibernate data source
 * configuration name and copyting the corresponding import file to
 * <code>import.sql</code>. This is important because Hibernate 3.4 does not yet allow
 * customizing <code> SchemaExport</code> import file name; when we migrate to Hibernate
 * 3.6+, this class will become obsolete.
 * <p>
 * For instance, if the <code>spring.properties</code> file contains the line
 *
 * <pre>
 * <code>
 * datasource.unit.config = utest-hsqldb
 * </code>
 * </pre>
 *
 * and this bean is a dependency (depends-on bean tag in the XML config) of the
 * sessionFactory bean, then it is expected that the file <code>import-hsqldb.sql</code>
 * is found in the spring resource loading context, and it is copied to
 * <code>import.sql</code> under the same directory.
 *
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Apr 18, 2011
 */
public class ResourceCopier
{
	// ========================= DEPENDENCIES ==============================

	/**
	 * Source resource path to copy from.
	 */
	private Resource sourceResource;

	/**
	 * Source file name to copy to.
	 */
	private String targetFileName;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Copy to <code>import.sql</code>.
	 */
	@PostConstruct
	protected void afterPropertiesSet()
	{
		Validate.notNull(sourceResource, "A source resource must be specified");
		Validate.notNull(targetFileName, "A target resource must be specified");

		try
		{
			final File sourceFile = sourceResource.getFile();
			final File newImportFile = new File(sourceFile.getParent(), "import.sql");
			FileCopyUtils.copy(sourceFile, newImportFile);
		}
		catch (final IOException e)
		{
			throw new BeanInitializationException("Could not copy resource "
					+ sourceResource, e);
		}
	}

	// ========================= DEPENDENCY INJECTION ======================

	/**
	 * Set a new value for the sourceResource property.
	 *
	 * @param sourceResource
	 *            the sourceResource to set
	 */
	public void setSourceResource(final Resource sourceResource)
	{
		this.sourceResource = sourceResource;
	}

	/**
	 * Set a new value for the targetFileName property.
	 *
	 * @param targetFileName
	 *            the targetFileName to set
	 */
	public void setTargetFileName(final String targetFileName)
	{
		this.targetFileName = targetFileName;
	}

}
