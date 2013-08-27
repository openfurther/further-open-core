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
package edu.utah.further.core.util.context;

import static edu.utah.further.core.api.constant.MavenPhase.TEST;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.MavenPhase;
import edu.utah.further.core.api.lang.CoreUtil;

/**
 * A portable test fixture helper class that can be reused by multiple modules (notably
 * web service modules).
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
 * @version Oct 9, 2009
 */
public final class PFixtureManager
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(PFixtureManager.class);

	// ========================= FIELDS ====================================

	/**
	 * Maven phase during which this test is running.
	 */
	private MavenPhase mavenPhase = TEST;

	/**
	 * Web service server application context.
	 */
	private volatile ApplicationContext serverContext;

	// ========================= DEPENDENCIES ==============================

	/**
	 * List of integration test context file locations on the classpath.
	 */
	private final List<String> contextLocations;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an integration test fixture manager.
	 *
	 * @param contextLocations
	 *            List of integration test context file locations on the classpath.
	 */
	public PFixtureManager(final List<String> contextLocations)
	{
		super();
		setUpFromSystemProperties();
		this.contextLocations = contextLocations;
	}

	// ========================= METHODS ===================================

	/**
	 * Set up fixture artifacts.
	 */
	public void initialize()
	{
		initWebServiceServerApplicationContext();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the contextLocations property.
	 *
	 * @return the contextLocations
	 */
	public List<String> getContextLocations()
	{
		return CollectionUtil.newList(contextLocations);
	}

	/**
	 * Return the mavenPhase property.
	 *
	 * @return the mavenPhase
	 */
	public MavenPhase getMavenPhase()
	{
		return mavenPhase;
	}

	/**
	 * Set a new value for the mavenPhase property. Overrides the default maven phase read
	 * from system properties during {@link PFixtureManager} bean construction.
	 *
	 * @param mavenPhase
	 *            the mavenPhase to set
	 */
	public void setMavenPhase(final MavenPhase mavenPhase)
	{
		this.mavenPhase = mavenPhase;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Set up fixture using system properties.
	 */
	private void setUpFromSystemProperties()
	{
		final MavenPhase systemPropertyPhase = CoreUtil.getMavenPhaseSystemProperty();
		if (systemPropertyPhase != null)
		{
			mavenPhase = systemPropertyPhase;
		}
	}

	/**
	 * Create a spring application context that will start a jetty server that runs our
	 * web services.
	 */
	synchronized private void initWebServiceServerApplicationContext()
	{
		// Initialize context only once for all tests
		if ((mavenPhase == MavenPhase.INTEGRATION_TEST) && (serverContext == null))
		{
			if (log.isDebugEnabled())
			{
				log.debug("Initializing web server");
			}
			serverContext = new ClassPathXmlApplicationContext(contextLocations
					.toArray(CollectionUtil.EMPTY_STRING_ARRAY));
		}
	}

}
