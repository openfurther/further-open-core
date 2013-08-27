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
package edu.utah.further.core.util.registry;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static junit.framework.Assert.assertEquals;
import static org.apache.commons.lang.StringUtils.replace;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.utah.further.core.api.exception.ApplicationException;

/**
 * A source and registry learning test.
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
 * @version Nov 6, 2008
 */
public class UTestRegistry
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestRegistry.class);

	private static enum NodeType
	{
		SOURCE, REGISTRY
	}

	// ========================= FIELDS ====================================

	/**
	 * Some sample sources to use.
	 */
	private List<Source> sources;

	private Registry registry;

	private RegistryClient client;

	// ========================= SETUP METHODS =============================

	@Before
	public void setUp()
	{
		sources = newList();
		for (int i = 1; i <= 3; i++)
		{
			sources.add((Source) newNode(NodeType.SOURCE, "Source " + i));
		}
		registry = (Registry) newNode(NodeType.REGISTRY, "Registry A");
		client = new RegistryClient(registry);
	}

	@After
	public void tearDown()
	{
		client = null;
		registry = null;
		sources = null;
	}

	// ========================= METHODS ===================================

	/**
	 * Register sources with a registry.
	 */
	@Test
	public void registerSources()
	{
		registerAllSources();
	}

	/**
	 * Register sources with a registry, then unregister them.
	 */
	@Test
	public void unregisterSources()
	{
		registerAllSources();

		final int numSources = sources.size();
		for (int i = 0; i < numSources; i++)
		{
			final Source source = sources.get(i);
			source.unregisterFrom(registry);
			assertEquals(numSources - i - 1, registry.size());
			log.debug("" + registry);
		}
	}

	/**
	 * Register sources with a registry. Send a data request from the registry to all
	 * sources.
	 */
	@Test
	public void sendDataRequest()
	{
		registerAllSources();
		client.sendDataRequest(0l);
		final List<String> results = client.getJoinedData().getData();
		log.debug("results = " + results);
		assertEquals(registry.size(), results.size());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * A factory template method of grid nodes.
	 *
	 * @param type
	 * @param name
	 * @return
	 */
	private static Node newNode(final NodeType type, final String name)
	{
		final String domain = replace(name.toLowerCase(), " ", "") + ".net";
		InternetAddress admin;
		try
		{
			admin = new InternetAddress("admin@" + domain);
		}
		catch (final AddressException e)
		{
			throw new ApplicationException("Bad admin address", e);
		}

		switch (type)
		{
			case SOURCE:
			{
				return new SourceImpl(name, name + " description", "http://" + domain,
						admin);
			}

			case REGISTRY:
			{
				return new RegistryImpl(name, name + " description", "http://"
						+ name.toLowerCase() + ".net", admin);
			}

			default:
			{
				return null;
			}
		}
	}

	/**
	 * Register all sources.
	 */
	private void registerAllSources()
	{
		for (int i = 0; i < sources.size(); i++)
		{
			final Source source = sources.get(i);
			source.registerWith(registry);
			assertEquals(i + 1, registry.size());
			log.debug("" + registry);
		}
	}
}
