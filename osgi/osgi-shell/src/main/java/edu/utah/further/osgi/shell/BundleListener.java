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
package edu.utah.further.osgi.shell;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextEvent;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextListener;

/**
 * An example of an OSGi bundle listener. Listens to all events.
 * <p>
 * May perhaps make the difference between our "fls"'s success in gathering all events
 * about bundles, versus the standard "osgi:list"'s shortcoming to do so.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Mar 7, 2010
 */
public final class BundleListener implements OsgiBundleApplicationContextListener
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(BundleListener.class);

	// ========================= CONSTRUCTORS =============================

	/**
	 * @throws Exception
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Initializing listener");
		}
	}

	// ========================= IMPL: OsgiBundleApplicationContextListen. =

	/**
	 * @param event
	 * @see org.springframework.osgi.context.event.OsgiBundleApplicationContextListener#onOsgiApplicationEvent(org.springframework.osgi.context.event.OsgiBundleApplicationContextEvent)
	 */
	@Override
	public void onOsgiApplicationEvent(final OsgiBundleApplicationContextEvent event)
	{
		if (log.isInfoEnabled())
		{
			log.info("Received event " + event.getBundle());
		}
	}

}
