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

/**
 * A convenient struct that holds the results of an XML test.
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
 * @version Sep 2, 2010
 */
public final class QTestResult
{
	// ========================= FIELDS ====================================

	/**
	 * Result of comparison of actual and expected test outputs.
	 */
	private final boolean comarisonResult;

	/**
	 * Actual test output, serialized to a string.
	 */
	private final String actualResourceString;

	/**
	 * A convenient place-holder for the formatted expected test output, normally in XML.
	 */
	private String formattedExpectedOutput;

	/**
	 * A convenient place-holder for the formatted expected test output, normally in XML.
	 */
	private String formattedActualOutput;

	/**
	 * Test final status.
	 */
	private QTestStatus status = QTestStatus.FAIL;

	/**
	 * Exception thrown during the test (if none thrown, set to <code>null</code>).
	 */
	private Throwable throwable;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Sets default field values.
	 */
	public QTestResult()
	{
		this(false, null);
	}

	/**
	 * @param comarisonResult
	 * @param actualResourceString
	 */
	public QTestResult(final boolean comarisonResult, final String actualResourceString)
	{
		super();
		this.comarisonResult = comarisonResult;
		this.actualResourceString = actualResourceString;
	}

	// ========================= GET & SET =================================

	/**
	 * Return the comarisonResult property.
	 *
	 * @return the comarisonResult
	 */
	public boolean isComarisonResult()
	{
		return comarisonResult;
	}

	/**
	 * Return the actualResourceString property.
	 *
	 * @return the actualResourceString
	 */
	public String getActualResourceString()
	{
		return actualResourceString;
	}

	/**
	 * Return the status property.
	 *
	 * @return the status
	 */
	public QTestStatus getStatus()
	{
		return status;
	}

	/**
	 * Set a new value for the status property.
	 *
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final QTestStatus status)
	{
		this.status = status;
	}

	/**
	 * Return the throwable property.
	 *
	 * @return the throwable
	 */
	public Throwable getThrowable()
	{
		return throwable;
	}

	/**
	 * Set a new value for the throwable property.
	 *
	 * @param throwable
	 *            the throwable to set
	 */
	public void setThrowable(final Throwable throwable)
	{
		this.throwable = throwable;
	}

	/**
	 * Return the formattedExpectedOutput property.
	 *
	 * @return the formattedExpectedOutput
	 */
	public String getFormattedExpectedOutput()
	{
		return formattedExpectedOutput;
	}

	/**
	 * Set a new value for the formattedExpectedOutput property.
	 *
	 * @param formattedExpectedOutput
	 *            the formattedExpectedOutput to set
	 */
	public void setFormattedExpectedOutput(final String formattedExpectedOutput)
	{
		this.formattedExpectedOutput = formattedExpectedOutput;
	}

	/**
	 * Return the formattedActualOutput property.
	 *
	 * @return the formattedActualOutput
	 */
	public String getFormattedActualOutput()
	{
		return formattedActualOutput;
	}

	/**
	 * Set a new value for the formattedActualOutput property.
	 *
	 * @param formattedActualOutput
	 *            the formattedActualOutput to set
	 */
	public void setFormattedActualOutput(final String formattedActualOutput)
	{
		this.formattedActualOutput = formattedActualOutput;
	}
}
