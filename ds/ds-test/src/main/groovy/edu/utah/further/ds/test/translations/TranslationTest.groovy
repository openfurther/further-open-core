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
package edu.utah.further.ds.test.translations

import org.apache.cxf.BusFactory
import org.custommonkey.xmlunit.XMLUnit
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

import spock.lang.Specification

/**
 * Query or result translation tests extend this class for common setup, cleanupSpec and helper methods.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Oct 8, 2013
 */
class TranslationTest extends Specification {
	
	def setup() {
		XMLUnit.setIgnoreWhitespace(true)
		XMLUnit.setIgnoreComments(true)
	}

	/**
	 * Returns an ORDERED list of inputstreams representing files at location.
	 *
	 * @return
	 */
	protected def queryFiles(location) {
		new PathMatchingResourcePatternResolver().getResources(location)
				.collectEntries(new TreeMap()){[it.filename, it.inputStream]}
	}

	def cleanupSpec() {
		BusFactory.getDefaultBus().shutdown(true);
	}
}
