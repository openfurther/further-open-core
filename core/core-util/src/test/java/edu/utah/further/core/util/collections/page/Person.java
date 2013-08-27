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
package edu.utah.further.core.util.collections.page;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A simple JavaBean for CSV parsing tests.
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
 * @version Jan 27, 2011
 */
final class Person
{
	// ========================= FIELDS ====================================

	/** Some fields **/
	private String firstName;
	private String lastName;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize no fields.
	 */
	public Person()
	{
		super();
	}

	/**
	 * Initialize all fields.
	 *
	 * @param firstName
	 * @param lastName
	 */
	public Person(final String firstName, final String lastName)
	{
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Person that = (Person) obj;
		return new EqualsBuilder()
				.append(this.firstName, that.firstName)
				.append(this.lastName, that.lastName)
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(firstName).append(lastName).toHashCode();
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("firstName", firstName)
				.append("lastName", lastName)
				.toString();
	}

	// ========================= GET & SET =================================

	/**
	 * Return the firstName property.
	 *
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * Set a new value for the firstName property.
	 *
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * Return the lastName property.
	 *
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * Set a new value for the lastName property.
	 *
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}
}
