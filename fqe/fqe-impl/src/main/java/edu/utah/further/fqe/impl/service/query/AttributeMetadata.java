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
package edu.utah.further.fqe.impl.service.query;

/**
 * A mock implementation that holds some hard-coded meta data on a demographics category
 * attribute name. Immutable.
 * <p>
 * TODO: replace this with logical model attributes read from the MDR (FUR-1554).
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Apr 12, 2011
 */
final class AttributeMetadata
{
	/**
	 * Standard attribute name.
	 */
	private final String name;

	/**
	 * Textual representation of the attribute.
	 */
	private final String displayName;

	/**
	 * Corresponding virtual repository column na,e.
	 */
	private final String columnName;

	/**
	 * @param name
	 * @param displayName
	 * @param columnName
	 */
	public AttributeMetadata(final String name, final String displayName,
			final String columnName)
	{
		super();
		this.name = name;
		this.displayName = displayName;
		this.columnName = columnName;
	}

	/**
	 * Return the name property.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Return the displayName property.
	 *
	 * @return the displayName
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * Return the columnName property.
	 *
	 * @return the columnName
	 */
	public String getColumnName()
	{
		return columnName;
	}

}
