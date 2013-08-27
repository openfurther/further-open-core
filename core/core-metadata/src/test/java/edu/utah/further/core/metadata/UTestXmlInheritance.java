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

import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.test.annotation.UnitTest;
import edu.utah.further.core.metadata.to.MetaData;
import edu.utah.further.core.metadata.to.WsElementMd;

/**
 * A unit test of inheritance and polymorphism treatment in JAXB marshalling.
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
 * @version Oct 17, 2008
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
public final class UTestXmlInheritance extends CoreMetadataFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestXmlInheritance.class);

	// ========================= FIELDS ====================================

	// ========================= DEPENDENCIES ==============================

	// ========================= SETUP METHODS =============================

	// ========================= TESTING METHODS ===========================

	/**
	 * Marshal a base-class entity.
	 */
	@Test
	public void marshalBaseClass() throws Exception
	{
		final MetaData entity = new MetaData("a", "b");
		xmlService.marshalAndPrint(entity);
	}

	/**
	 * Marshal a second sub-class entity.
	 */
	@Test
	public void marshalSubClass() throws Exception
	{
		final MetaData entity = new WsElementMd("a", "b", "c");
		xmlService.marshalAndPrint(entity);
	}

	/**
	 * Marshal a heterogeneous list of entities that have a common super-class.
	 */
	@Test
	public void marshalPolymorphicList() throws Exception
	{
		final MetaData entity = new MetaData("a", "b");
		final MetaData entity1 = new MetaData("a0", "b0");
		entity.addChild(entity1);
		final MetaData entity2 = new WsElementMd("a2", "b2", "c2");
		entity.addChild(entity2);
		xmlService.marshalAndPrint(entity);
	}

	/**
	 * Marshal a heterogeneous list of entities that have a common super-class.
	 */
	@Test
	public void marshalPolymorphicListOfASubClass() throws Exception
	{
		final MetaData entity = new WsElementMd("a", "b", "c");
		final MetaData entity1 = new MetaData("a0", "b0");
		entity.addChild(entity1);
		final MetaData entity2 = new WsElementMd("a2", "b2", "c2");
		entity.addChild(entity2);
		xmlService.marshalAndPrint(entity);
	}
}
