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
package edu.utah.further.mdr.api.domain.asset;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.CopyableFrom;

/**
 * An asset association property
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2011 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-213-3288<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Apr 11, 2012
 */
public interface AssetAssociationProperty extends PersistentEntity<Long>,
		CopyableFrom<AssetAssociationProperty, AssetAssociationProperty>
{
	public static final String JAVA_DATA_TYPE = "ATTR_VALUE_TRANS_TO_DATA_TYPE";

	/**
	 * Return the name property.
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Set a new value for the name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	void setName(String name);

	/**
	 * Return the value property.
	 * 
	 * @return the value
	 */
	String getValue();

	/**
	 * Set a new value for the value property.
	 * 
	 * @param value
	 *            the value to set
	 */
	void setValue(String value);
}
