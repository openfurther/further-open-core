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
package edu.utah.further.i2b2.query.model;

/**
 * Represents a value constraint in an I2b2 Query
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Aug 20, 2009
 */
public interface I2b2QueryValueConstraint
{
	/**
	 * Undefined value when units of measure are undefined.
	 */
	public static final String UNDEFINED = "undefined";

	/**
	 * Return the valueType property.
	 * 
	 * @return the valueType
	 */
	I2b2ValueType getValueType();

	/**
	 * Return the valueUnitOfMeasure property.
	 * 
	 * @return the valueUnitOfMeasure
	 */
	String getValueUnitOfMeasure();

	/**
	 * Return the valueOperator property.
	 * 
	 * @return the valueOperator
	 */
	I2b2ValueOperator getValueOperator();

	/**
	 * Return the valueConstraint property.
	 * 
	 * @return the valueConstraint
	 */
	String getValueConstraint();
}
