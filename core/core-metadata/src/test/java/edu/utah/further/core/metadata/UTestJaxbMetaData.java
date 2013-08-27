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
package edu.utah.further.core.metadata;

import static edu.utah.further.core.api.lang.CoreUtil.getResourceAsStream;
import static junit.framework.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.metadata.to.MetaData;
import edu.utah.further.core.test.annotation.UnitTest;

/**
 * Tests the person search service.
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
 * @version May 29, 2009
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
public final class UTestJaxbMetaData extends CoreMetadataFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestJaxbMetaData.class);

	// ========================= DEPENDENCIES ==============================

	// ========================= FIELDS ====================================

	// ========================= TESTING METHODS ===========================

	/**
	 * Marshal a MetaData set.
	 */
	@Test
	public void marshalMetaDataSet() throws Exception
	{
		final MetaData set = new MetaData();
		final MetaData metaData = new MetaData("name", "description");
		// metaData.setStatus(Status.ACTIVE);
		set.addChild(metaData);
		assertEquals(1, set.getNumChildren());

		final String s = xmlService.marshal(set);
		if (log.isDebugEnabled())
		{
			log.debug("Entity    = " + set);
			log.debug("Marshaled = " + s);
		}
	}

	/**
	 * Unmarshal a MetaData set.
	 */
	@Test
	public void unmarshalMetaDataSetFromAegisFormat() throws Exception
	{
		MetaData result;
		try (final InputStream is = getResourceAsStream("metadataset-aegis.xml")) {
			result = xmlService.unmarshal(is, MetaData.class);
		}
		assertEquals(0, result.getNumChildren());
	}

	/**
	 * Unmarshal a MetaData set.
	 */
	@Test
	public void unmarshalMetaDataSetFromJaxbFormat() throws Exception
	{
		MetaData result;
		try (final InputStream is = getResourceAsStream("metadataset-jaxb.xml")) {
			result = xmlService.unmarshal(is, MetaData.class);
		}
		assertEquals(1, result.getNumChildren());
	}

	// ========================= PRIVATE METHODS ===========================

}
