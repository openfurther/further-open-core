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
package edu.utah.further.mdr.ws.impl.domain;

import static edu.utah.further.core.api.text.StringUtil.stripNewLinesAndTabs;
import static edu.utah.further.core.qunit.runner.XmlAssertion.xmlAssertion;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.qunit.runner.XmlAssertion;
import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.mdr.api.domain.asset.Asset;
import edu.utah.further.mdr.api.domain.asset.Resource;
import edu.utah.further.mdr.api.to.asset.AssetTo;
import edu.utah.further.mdr.ws.api.to.AssetToImpl;
import edu.utah.further.mdr.ws.api.to.ResourceToImpl;
import edu.utah.further.mdr.ws.impl.fixture.MdrWsFixture;

/**
 * A unit test of reading and writing XML files into and from JavaBean entities in the MDR
 * WS module using JAXB.
 * <p>
 * ---------------------------------------------------------------------------- -------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * ---------------------------------------------------------------------------- -------
 * 
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 17, 2008
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
public final class UTestMarshalEntities extends MdrWsFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestMarshalEntities.class);

	/**
	 * Resource name with sample asset XML.
	 */
	private static final String ASSET_XML = "asset.xml";

	private static final Long id = new Long(234);
	private static final String desc = "UUEDW Person XMI";
	private static final String url = "http://localhost:7201/mdr/rest/asset/resource/14/storage";

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	// ========================= SETUP METHODS =============================

	// ========================= TESTING METHODS ===========================

	/**
	 * Marshal an MDR asset (not from an input file).
	 */
	@Test
	public void marshalAssetSimple() throws Exception
	{
		final String label = "FURTHeR";
		final String description = "This is the FURTHeR Namespace.  Its metadata are contained in its resources.";
		final AssetTo assetTo = new AssetToImpl();
		((AssetToImpl) assetTo).setId(id);
		assetTo.setLabel(label);
		assetTo.setDescription(description);
		final String marshalled = xmlService.marshal(assetTo);
		assertThat(marshalled, containsString(id.toString()));
		assertThat(marshalled, containsString(label));
		assertThat(marshalled, containsString(description));
	}

	/**
	 * Unmarshal an MDR asset.
	 */
	@Test
	public void unmarshalAsset() throws Exception
	{
		final AssetTo result = xmlService.unmarshalResource(ASSET_XML, AssetToImpl.class);
		assertEquals(3l, result.getId().longValue());
		assertEquals(2l, result.getTypeId().longValue());
		assertEquals(3l, result.getNamespaceId().longValue());
	}

	/**
	 * Marshal an {@link Asset} using JAXB.
	 */
	@Test
	public void unmarshalMarshalAsset() throws Exception
	{
		final AssetTo entity = xmlService.unmarshalResource(ASSET_XML, AssetToImpl.class);
		final String marshalled = stripNewLinesAndTabs(xmlService.marshal(entity));

		// Different machine clock time zones may cause different values for
		// the activation date field ==> ignore it
		xmlAssertion(XmlAssertion.Type.STREAM_MATCH)
				.actualResourceString(marshalled)
				.expectedResourceName(ASSET_XML)
				.ignoredElement("activationDate")
				.doAssert();
	}

	/**
	 * Marshal an MDR resource.
	 */
	@Test
	public void marshalResourceSmall() throws Exception
	{
		final Resource resource = createStubbedResource();
		final String marshalled = xmlService.marshal(resource);

		assertThat(marshalled, containsString(id.toString()));
		assertThat(marshalled, containsString(desc));
		assertThat(marshalled, containsString(url));
	}

	/**
	 * Create a stubbed out resource
	 * 
	 * @return
	 */
	private Resource createStubbedResource()
	{
		final ResourceToImpl resource = new ResourceToImpl();
		resource.setId(id);
		resource.setDescription(desc);
		resource.setUrl(url);
		return resource;
	}

}
