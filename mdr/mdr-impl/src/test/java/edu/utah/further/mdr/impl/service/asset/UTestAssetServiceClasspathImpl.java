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
package edu.utah.further.mdr.impl.service.asset;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.text.PlaceHolderResolver;
import edu.utah.further.core.api.text.StringUtil;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.service.asset.AssetService;
import edu.utah.further.mdr.api.service.asset.MdrNames;

/**
 * Tests the MDR asset service classpath implementation.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}, N. Dustin Schultz
 *         {@code <dustin.schultz@utah.edu>}
 * 
 * @version Mar 23, 2009
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/mdr-impl-test-context.xml" })
public final class UTestAssetServiceClasspathImpl
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestAssetServiceClasspathImpl.class);

	// ========================= DEPENDENCIES ==============================

	/**
	 * The MDR asset service tested here.
	 */
	@Autowired
	private AssetService assetServiceClasspath;

	/**
	 * Resolves placeholders with values from properties files - used for asserting test
	 * values.
	 */
	@Autowired
	private PlaceHolderResolver resolver;

	// ========================= FIELDS ====================================

	// ========================= TESTING METHODS: ASSETS ===================

	// most of not all of the other methods are not implemented yet so test just the ones
	// that are

	/**
	 * Get a class path resource by path and filter place holders within storage. The
	 * beauty is that the input path to
	 * {@link AssetService#getActiveResourceByPath(String)} is the same as for production
	 * MDR calls.
	 */
	@Test
	public void filterResourcePlaceHolders()
	{
		final String path = "resource.txt";
		final Resource resource = assetServiceClasspath.getActiveResourceByPath(path);
		assertNotNull("Resource not found under path " + StringUtil.quote(path), resource);
		final String resourceContents = resource.getText();
		assertNotNull(resourceContents);
		assertThat(resourceContents,
				containsString(resolver
						.resolvePlaceholders(MdrNames.PLACE_HOLDER_DTS_WS_SERVER_URL)));
	}

	// ========================= PRIVATE METHODS ===========================

}
