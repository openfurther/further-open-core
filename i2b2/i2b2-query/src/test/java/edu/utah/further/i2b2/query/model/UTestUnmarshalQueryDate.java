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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import edu.utah.further.i2b2.query.fixture.I2b2QueryFixture;

/**
 * Ensures that dates are properly unmarshalled and ignore timezones.
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
 * @version Nov 16, 2011
 */
public class UTestUnmarshalQueryDate extends I2b2QueryFixture
{
	private static final String QUERY_WITH_DATE = "i2b2-request-query-simple-with-date.xml";

	/**
	 * Ensures that a time zoned string of 1/1/2006 returns a year of 2006 and not
	 * 12/31/2005
	 * 
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	@SuppressWarnings("boxing")
	public void unmarshallDate() throws JAXBException, IOException
	{
		final I2b2Query i2b2Query = xmlService.unmarshalResource(REQ_DIR
				+ QUERY_WITH_DATE, I2b2Query.class);

		final int year = Integer.valueOf(new SimpleDateFormat("yyyy").format(i2b2Query
				.getQueryGroups()
				.get(0)
				.getQueryItems()
				.get(0)
				.getConstrainByDate()
				.getDateFrom()));

		assertThat(year, is(2006));
	}
}
