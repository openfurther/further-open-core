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
package edu.utah.further.core.util.scope;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.scope.NamespaceService;
import edu.utah.further.core.api.scope.Namespaces;

/**
 * Unit test for namespace service
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Jul 23, 2013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
{ "/core-util-test-context.xml" })
public class UTestNamespaceService
{
	/**
	 * The namespace service used to lookup namespace identifiers and names.
	 */
	@Autowired
	private NamespaceService namespaceService;

	private static final String CUSTOM_NAMESPACE = "my_custom_namespace";

	/**
	 * Retrieve a name for a standard namespace
	 */
	@Test
	public void getStandardNamespaceName()
	{
		assertThat(namespaceService.getNamespaceName(Namespaces.ICD_9_CM), is("ICD-9-CM"));
	}

	/**
	 * Retrieve a namespace identifier for a standard known namespace
	 */
	@Test
	public void getStandardNamespaceId()
	{
		assertThat(new Integer(namespaceService.getNamespaceId(Namespaces.SNOMED_CT)),
				is(new Integer(30)));
	}

	/**
	 * Retrieve a name for a custom/local namespace
	 */
	@Test
	public void getCustomNamespaceName()
	{
		assertThat(namespaceService.getNamespaceName(Namespaces
				.namespaceFor(CUSTOM_NAMESPACE)), is("My Custom Namespace"));
	}

	/**
	 * Retrieve a namespace identifier for a custom/local namespace
	 */
	@Test
	public void getCustomNamespaceId()
	{
		assertThat(
				new Integer(namespaceService.getNamespaceId(Namespaces
						.namespaceFor(CUSTOM_NAMESPACE))), is(new Integer(1337)));
	}

	/**
	 * Negative testing to ensure that a proper exception is thrown when requesting an id
	 * for an unknown namespace.
	 */
	@Test(expected = ApplicationException.class)
	public void getUnknownNamespaceId()
	{
		namespaceService.getNamespaceId(Namespaces.namespaceFor("blah"));
	}

	/**
	 * Negative testing to ensure that a proper exception is thrown when requesting a name
	 * for an unknown namespace.
	 */
	@Test(expected = ApplicationException.class)
	public void getUnknownNamespaceName()
	{
		namespaceService.getNamespaceName(Namespaces.namespaceFor("blah"));
	}
}
