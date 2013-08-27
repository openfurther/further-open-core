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
package edu.utah.further.i2b2.query.tester;

import static edu.utah.further.core.util.io.LoggingUtil.debugPrintAndCenter;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.utah.further.i2b2.query.criteria.service.I2b2QueryService;
import edu.utah.further.i2b2.query.model.I2b2Query;

/**
 * A unit test class of the osgi configuration and services that runs upon bundle's
 * start-up in an OSGi container.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Shan He {@code <shan.he@utah.edu>}
 * @version May 05, 2010
 */

public class I2b2QueryConnectionTester
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(I2b2QueryConnectionTester.class);

	// ========================= FIELDS ====================================

	/**
	 * Encapsulate service type methods of an {@link I2b2Query}.
	 */
	@Autowired
	private I2b2QueryService i2b2QueryService;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A unit test of this service that runs when the bundle is started.
	 */
	@PostConstruct
	public void i2b2QueryConnectionTest()
	{
		//TODO: Better way for testing connection. If others remove \FURTHER\ hierarchy this will fail.
		/**
		 * A term for which we want to find the domain or children of
		 */
		final String ITEM_KEY = "\\FURTHER\\Demographics\\Gender\\";

		debugPrintAndCenter(log, "i2b2QueryConnectionTest() begin");
		final List<String> children = i2b2QueryService.findDomain(ITEM_KEY);

		Validate.notNull(children);
		Validate.isTrue(children.size() >= 2);
		
		debugPrintAndCenter(log, "i2b2QueryConnectionTest() end");
	}

}
