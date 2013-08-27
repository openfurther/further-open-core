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
package edu.utah.further.mdr.api.domain.uml;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.InputStream;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.core.metadata.to.MetaData;

/**
 * A JavaBean that holds Meta data of a UML model.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 */
@Api
public class ModelMetaData extends MetaData
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(ModelMetaData.class);

	// ========================= FIELDS ====================================

	/**
	 * XQuery file resource URI name to query the model's XMI file for information
	 * required to build the model.
	 */
	private String queryResource;

	/**
	 * Model's XMI file resource URI (indirect access).
	 */
	private String xmiResource;

	/**
	 * Model's XMI file input stream (direct access).
	 */
	private InputStream xmiInputStream;

	/**
	 * Fully qualified mame of top package of model classes.
	 */
	private String modelPackageName;

	/**
	 * Use input stream direct access for XMI resource or not. Default: <code>false</code>
	 */
	private boolean xmiDirectAccess = false;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a model meta data.
	 *
	 * @param name
	 *            name unique identifier of this meta data object
	 * @param description
	 *            optional description
	 */
	public ModelMetaData(final String name, final String description)
	{
		super(name, description);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE).appendSuper(
				super.toString()).append("queryResource", queryResource).append(
				"xmiResource", xmiResource).append("modelPackageName", modelPackageName)
				.toString();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the queryResource property.
	 *
	 * @return the queryResource
	 */
	public String getQueryResource()
	{
		return queryResource;
	}

	/**
	 * Set a new value for the queryResource property.
	 *
	 * @param queryResource
	 *            the queryResource to set
	 */
	public void setQueryResource(String queryResource)
	{
		this.queryResource = queryResource;
	}

	/**
	 * Return the xmiResource property.
	 *
	 * @return the xmiResource
	 */
	public String getXmiResource()
	{
		return xmiResource;
	}

	/**
	 * Set a new value for the xmiResource property.
	 *
	 * @param xmiResource
	 *            the xmiResource to set
	 */
	public void setXmiResource(String xmiResource)
	{
		this.xmiResource = xmiResource;
	}

	/**
	 * Return the modelPackageName property.
	 *
	 * @return the modelPackageName
	 */
	public String getModelPackageName()
	{
		return modelPackageName;
	}

	/**
	 * Set a new value for the modelPackage property.
	 *
	 * @param modelPackage
	 *            the name of the model package to set
	 */
	public void setModelPackageName(String modelPackageName)
	{
		this.modelPackageName = modelPackageName;
	}

	/**
	 * Return the xmiInputStream property.
	 *
	 * @return the xmiInputStream
	 */
	public InputStream getXmiInputStream()
	{
		return xmiInputStream;
	}

	/**
	 * Set a new value for the xmiInputStream property.
	 *
	 * @param xmiInputStream
	 *            the xmiInputStream to set
	 */
	public void setXmiInputStream(InputStream xmiInputStream)
	{
		this.xmiInputStream = xmiInputStream;
	}

	/**
	 * Return the xmiDirectAccess property.
	 *
	 * @return the xmiDirectAccess flag (is input stream direct access for XMI resource
	 *         activated). Default: <code>false</code>
	 */
	public boolean isXmiDirectAccess()
	{
		return xmiDirectAccess;
	}

	/**
	 * Set a new value the input stream direct access for XMI resource. Default:
	 * <code>false</code>.
	 *
	 * @param xmiDirectAccess
	 *            the xmiDirectAccess to set
	 */
	public void setXmiDirectAccess(boolean xmiDirectAccess)
	{
		this.xmiDirectAccess = xmiDirectAccess;
	}

}
