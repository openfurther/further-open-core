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
package edu.utah.further.core.qunit.runner;

import static edu.utah.further.core.api.text.ToStringCustomStyles.SHORT_WITH_SPACES_STYLE;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.utah.further.core.api.constant.Strings;

/**
 * A bean that holds XML test suite global parameters.
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
 * @version May 13, 2010
 */
public final class QTestContext
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * ID of the target (destination) namespace of the translation.
	 */
	private Long targetNamespaceId;

	/**
	 * A service user id indicated on created queries. Specified in the data source
	 * context.
	 */
	private String serviceUserId;

	/**
	 * Optional class-path to prepend to all input file names.
	 */
	private String inputClassPath = Strings.EMPTY_STRING;

	/**
	 * Optional class-path to prepend to all expected file names.
	 */
	private String expectedClassPath = Strings.EMPTY_STRING;

	/**
	 * Currently running test number out of the entire number of tests in the suite.
	 */
	private int currentTestNumber = 0;

	/**
	 * Number of tests in the suite.
	 */
	private int numTests;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Return the hash code of this object. includes all fields.
	 *
	 * @return hash code of this object
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(targetNamespaceId)
				.append(serviceUserId)
				.append(inputClassPath)
				.append(expectedClassPath)
				.toHashCode();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;

		final QTestContext that = (QTestContext) obj;
		return new EqualsBuilder()
				.append(targetNamespaceId, that.targetNamespaceId)
				.append(serviceUserId, that.serviceUserId)
				.append(inputClassPath, that.inputClassPath)
				.append(expectedClassPath, that.expectedClassPath)
				.isEquals();
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_WITH_SPACES_STYLE)
				.append("targetNamespaceId", targetNamespaceId)
				.append("serviceUserId", serviceUserId)
				.append("inputClassPath", inputClassPath)
				.append("expectedClassPath", expectedClassPath)
				.toString();
	}

	// ========================= METHODS ===================================

	/**
	 * @return
	 */
	public String getTestInfoMessage()
	{
		return String.format("Test %2d/%2d", new Integer(getCurrentTestNumber()),
				new Integer(getNumTests()));
	}

	// ========================= GET & SET =================================

	/**
	 * Return the targetNamespaceId property.
	 *
	 * @return the targetNamespaceId
	 */
	public Long getTargetNamespaceId()
	{
		return targetNamespaceId;
	}

	/**
	 * Set a new value for the targetNamespaceId property.
	 *
	 * @param targetNamespaceId
	 *            the targetNamespaceId to set
	 */
	public void setTargetNamespaceId(final Long targetNamespaceId)
	{
		this.targetNamespaceId = targetNamespaceId;
	}

	/**
	 * Return the serviceUserId property.
	 *
	 * @return the serviceUserId
	 */
	public String getServiceUserId()
	{
		return serviceUserId;
	}

	/**
	 * Set a new value for the serviceUserId property.
	 *
	 * @param serviceUserId
	 *            the serviceUserId to set
	 */
	public void setServiceUserId(final String serviceUserId)
	{
		this.serviceUserId = serviceUserId;
	}

	/**
	 * Return the inputClassPath property.
	 *
	 * @return the inputClassPath
	 */
	public String getInputClassPath()
	{
		return inputClassPath;
	}

	/**
	 * Set a new value for the inputClassPath property.
	 *
	 * @param inputClassPath
	 *            the inputClassPath to set
	 */
	public void setInputClassPath(final String inputClassPath)
	{
		if (StringUtils.isBlank(inputClassPath))
		{
			throw new IllegalArgumentException(
					"Input class path must not be a blank string");
		}
		this.inputClassPath = inputClassPath;
	}

	/**
	 * Return the expectedClassPath property.
	 *
	 * @return the expectedClassPath
	 */
	public String getExpectedClassPath()
	{
		return expectedClassPath;
	}

	/**
	 * Set a new value for the expectedClassPath property.
	 *
	 * @param expectedClassPath
	 *            the expectedClassPath to set
	 */
	public void setExpectedClassPath(final String expectedClassPath)
	{
		if (StringUtils.isBlank(expectedClassPath))
		{
			throw new IllegalArgumentException(
					"Expected class path must not be a blank string");
		}
		this.expectedClassPath = expectedClassPath;
	}

	/**
	 * Return the currentTestNumber property.
	 *
	 * @return the currentTestNumber
	 */
	public int getCurrentTestNumber()
	{
		return currentTestNumber;
	}

	/**
	 * Set a new value for the currentTestNumber property.
	 *
	 * @param currentTestNumber
	 *            the currentTestNumber to set
	 */
	public void setCurrentTestNumber(final int currentTestNumber)
	{
		this.currentTestNumber = currentTestNumber;
	}

	/**
	 * Return the numTests property.
	 *
	 * @return the numTests
	 */
	public int getNumTests()
	{
		return numTests;
	}

	/**
	 * Set a new value for the numTests property.
	 *
	 * @param numTests
	 *            the numTests to set
	 */
	public void setNumTests(final int numTests)
	{
		this.numTests = numTests;
	}

}
