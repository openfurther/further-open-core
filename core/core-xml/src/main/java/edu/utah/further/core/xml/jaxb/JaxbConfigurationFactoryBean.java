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
package edu.utah.further.core.xml.jaxb;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Service;

/**
 * Creates a JAXB context configuration.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 13, 2008
 */
// Temporary, for Spring 2.5.6.X backward-compatibility
@Service("jaxbConfigurationFactoryBean")
public class JaxbConfigurationFactoryBean implements FactoryBean<Map<String, Object>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(JaxbConfigurationFactoryBean.class);

	/**
	 * A custom JAXB context used by default if a configuration not specified in the
	 * marshalling and unmarshalling methods of this service.
	 */
	public static final Map<String, Object> DEFAULT_JAXB_CONFIG = JaxbConfig.FURTHER
			.getJaxbConfig();

	// ========================= IMPLEMENTATION: FactoryBean ===============

	/**
	 * @return
	 * @throws Exception
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Map<String, Object> getObject() throws Exception
	{
		return DEFAULT_JAXB_CONFIG;
	}

	/**
	 * @return
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<Map<String, Object>> getObjectType()
	{
		return (Class<Map<String, Object>>) DEFAULT_JAXB_CONFIG.getClass();
	}

	/**
	 * @return
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton()
	{
		return true;
	}

}
