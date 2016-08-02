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
package edu.utah.further.dts.api.domain.namespace;

import java.util.List;
import java.util.Map;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.dts.api.annotation.DtsEntity;
import edu.utah.further.dts.api.domain.concept.DtsProperty;

/**
 * An interface extracted from Apelon DTS API's <code>PropertiedObject</code>.
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
 * @version Dec 17, 2008
 */
@DtsEntity
@Api
public interface DtsPropertiedObject extends DtsObject
{
	// ========================= METHODS COUPLED TO APELON API =============

	// /**
	// * A view method that returns the DTS object associated with this object, if it
	// * exists.
	// *
	// * @return Apelon DTS propertied object view
	// */
	// PropertiedObject getAsDTSPropertiedObject();

	/**
	 * Gets the properties which have been retrieved for this item. If a property is
	 * qualified, it will be returned with any qualifiers that are attached.
	 *
	 * @return a map of fetched properties in our API
	 * @see com.apelon.dts.client.attribute.PropertiedObject#getFetchedProperties()
	 */
	Map<String, List<DtsProperty>> getProperties();

	/**
	 * Return a property by name.
	 *
	 * @param propertyName
	 *            property name
	 * @return corresponding property of this object; if not found, returns
	 *         <code>null</code>
	 * @see java.util.Map#get(java.lang.Object)
	 */
	List<DtsProperty> getProperty(String propertyName);

	/**
	 * Return a property value by name.
	 *
	 * @param propertyName
	 *            property name
	 * @return corresponding property value; if the property not found, returns
	 *         <code>null</code>
	 */
	List<String> getPropertyValue(String propertyName);

	// ========================= METHODS ===================================

	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.attribute.PropertiedObject#addProperty(com.apelon.dts.client.attribute.DTSProperty)
	// */
	// boolean addProperty(DTSProperty arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.attribute.PropertiedObject#containsProperty(com.apelon.dts.client.attribute.DTSProperty)
	// */
	// boolean containsProperty(DTSProperty arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.attribute.PropertiedObject#deleteProperty(com.apelon.dts.client.attribute.DTSProperty)
	// */
	// boolean deleteProperty(DTSProperty arg0);

	/**
	 * @return
	 * @see com.apelon.dts.client.attribute.PropertiedObject#getNumSpecifiedProperties()
	 */
	int getNumSpecifiedProperties();

	// /**
	// * @param arg0
	// * @see
	// com.apelon.dts.client.attribute.PropertiedObject#setProperties(com.apelon.dts.client.attribute.DTSProperty[])
	// */
	// void setProperties(List<? extends DTSProperty> arg0);
}