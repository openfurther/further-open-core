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
package edu.utah.further.mdr.impl.service.test;

import static edu.utah.further.core.util.io.LoggingUtil.debugPrintAndCenter;
import static org.slf4j.LoggerFactory.getLogger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.service.asset.AssetService;

/**
 * Hibernate implementation of generic and global functions related to the data layer.
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
 * @version Jan 28, 2009
 */
public class MdrServiceTester
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(MdrServiceTester.class);

	// ========================= FIELDS ====================================

	/**
	 * Handles generic DAO operations and searches.
	 */
	@Autowired
	@Qualifier("assetService")
	private AssetService assetService;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A unit test of a Hibernate-transacted-operation.
	 */
	@SuppressWarnings("boxing")
	@PostConstruct
	protected void findAssetById()
	{
		debugPrintAndCenter(log, "findAssetById() begin");
		final Asset asset = assetService.findAssetById(2l);
		if (log.isDebugEnabled())
		{
			log.debug("asset : " + asset);
		}
		// assertEquals(1, list.size());
		debugPrintAndCenter(log, "findAssetById() end");
	}

	/**
	 * A unit test of a Hibernate-transacted-operation.
	 */
	@SuppressWarnings("boxing")
	@PostConstruct
	protected void findResourceById()
	{
		debugPrintAndCenter(log, "findResourceById() begin");
		final Resource resource = assetService.findResourceById(20l);
		if (log.isDebugEnabled())
		{
			log.debug("resource : " + resource);
			log.debug("resource XML: " + ((resource == null) ? "-" : resource.getXml()));
		}
		// assertEquals(1, list.size());

		debugPrintAndCenter(log, "findResourceById() end");
	}
}
