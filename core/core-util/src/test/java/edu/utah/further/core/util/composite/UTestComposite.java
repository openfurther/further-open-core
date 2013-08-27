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
package edu.utah.further.core.util.composite;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static org.apache.commons.lang.StringUtils.replace;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

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
public class UTestComposite
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestComposite.class);

	private static enum SourceType
	{
		DATA, COMPOSITE
	}

	// ========================= FIELDS ====================================

	/**
	 * Some sample sources to use.
	 */
	private List<Source> sources;

	// ========================= SETUP METHODS =============================

	/**
	 *
	 */
	@Before
	public void setUp()
	{
		sources = newList();
		for (int i = 0; i < 10; i++)
		{
			sources.add(newSource(SourceType.DATA, "Source " + i));
		}
	}

	/**
	 *
	 */
	@After
	public void tearDown()
	{
		sources = null;
	}

	// ========================= METHODS ===================================

	/**
	 * Send a query to a composite source.
	 */
	@Test
	public void executeFederatedQuery()
	{
		final CompositeSource compositeA = (CompositeSource) newSource(
				SourceType.COMPOSITE, "Composite Source A");
		compositeA.addAll(sources.subList(0, 4));

		final CompositeSource compositeB = (CompositeSource) newSource(
				SourceType.COMPOSITE, "Composite Source B");
		compositeB.addAll(sources.subList(4, 8));

		final CompositeSource compositeC = (CompositeSource) newSource(
				SourceType.COMPOSITE, "Composite Source C");
		compositeC.addAll(sources.subList(8, 10));
		compositeC.add(compositeA);
		compositeC.add(compositeB);

		final Result federatedResult = compositeC.executeQuery();
		log.debug("compositeC.executeQuery = " + federatedResult);
		Assert.assertEquals(10, federatedResult.getData().size());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * A factory template method of grid nodes.
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	private static Source newSource(final SourceType type, final String name)
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
			case DATA:
			{
				return new DataSource(name, name + " description", "http://" + domain,
						admin);
			}

			case COMPOSITE:
			{
				return new CompositeSource(name, name + " description", "http://"
						+ name.toLowerCase() + ".net", admin);
			}

			default:
			{
				return null;
			}
		}
	}
}
