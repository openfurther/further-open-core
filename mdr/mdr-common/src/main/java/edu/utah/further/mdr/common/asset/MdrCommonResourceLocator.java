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
package edu.utah.further.mdr.common.asset;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.core.api.context.ResourceLocator;
import edu.utah.further.mdr.api.service.asset.AssetService;

/**
 * Helps inject {@link AssetService} into itself to get around non-AOPed self-invocation
 * problems.
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
 * @version Mar 17, 2011
 */
@Component(MdrCommonResourceLocator.BEAN_NAME)
@Scope(Constants.Scope.SINGLETON)
@ResourceLocator
public class MdrCommonResourceLocator
{
	// ========================= CONSTANTS =================================

	/**
	 * Spring bean name.
	 */
	public static final String BEAN_NAME = "mdrResourceLocator";

	/**
	 * The singleton instance of this class maintained by Spring.
	 */
	private static MdrCommonResourceLocator instance;

	// ========================= DEPENDENCIES ==============================

	/**
	 * MDR asset/resource business service.
	 */
	@Autowired
	private AssetService assetService;

	/**
	 * Spring application context.
	 */
	@Autowired
	private ApplicationContext applicationContext;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize the static instance of this class that is visible to classes in this
	 * package.
	 */
	@PostConstruct
	protected void initializeInstance()
	{
		MdrCommonResourceLocator.instance = (MdrCommonResourceLocator) applicationContext
				.getBean(MdrCommonResourceLocator.BEAN_NAME);
	}

	// ========================= METHODS ===================================

	/**
	 * Return the instance property.
	 *
	 * @return the instance
	 */
	public static MdrCommonResourceLocator getInstance()
	{
		return instance;
	}

	/**
	 * Return the assetService property.
	 *
	 * @return the assetService
	 */
	public AssetService getAssetService()
	{
		return assetService;
	}
}
