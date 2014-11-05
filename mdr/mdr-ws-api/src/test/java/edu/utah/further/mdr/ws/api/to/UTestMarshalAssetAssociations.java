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
package edu.utah.further.mdr.ws.api.to;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import edu.utah.further.core.api.xml.XmlService;
import edu.utah.further.core.xml.jaxb.XmlServiceImpl;
import edu.utah.further.mdr.api.domain.asset.AssetAssociation;
import edu.utah.further.mdr.api.domain.asset.AssetAssociationProperty;
import edu.utah.further.mdr.api.to.asset.AssetAssociationTo;

/**
 * Marshalling tests for {@link AssetAssociationToImpl}, {@link AssetAssociationToList},
 * and {@link AssetAssociationPropertyToImpl}
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
 * @version May 13, 2013
 */
public class UTestMarshalAssetAssociations
{
	/**
	 * Test marshalling an {@link AssetAssociationToImpl} and an
	 * {@link AssetAssociationPropertyToImpl}
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void marshalAssetAssocationTo() throws JAXBException
	{
		final AssetAssociationTo assetAssociation = new AssetAssociationToImpl();
		assetAssociation.setAssociation("hasAttribute");
		assetAssociation.setAssociationId(new Long(7));
		assetAssociation.setLeftAsset("Person");
		assetAssociation.setLeftAssetId(new Long(1));
		assetAssociation.setLeftNamespace("@DSCUSTOM-26@");
		assetAssociation.setLeftNamespaceId(new Long(2));
		assetAssociation.setLeftType("Phyiscal Class");
		assetAssociation.setLeftTypeId(new Long(3));
		assetAssociation.setRightAsset("genderDwid");
		assetAssociation.setRightAssetId(new Long(4));
		assetAssociation.setRightNamespace("@DSCUSTOM-26@");
		assetAssociation.setRightNamespaceId(new Long(5));
		assetAssociation.setRightType("Class Attribute");
		assetAssociation.setRightTypeId(new Long(6));

		final Collection<AssetAssociationProperty> properties = new ArrayList<>();
		final AssetAssociationProperty property = new AssetAssociationPropertyToImpl();
		property.setName("JAVA_DATA_TYPE");
		property.setValue("java.lang.Long");
		properties.add(property);

		assetAssociation.setProperties(properties);

		final XmlService xmlService = new XmlServiceImpl();
		final String result = xmlService.marshal(assetAssociation);

		assertThat(result, containsString("assetAssociation>"));
		assertThat(result, containsString("<leftType>"));
		assertThat(result, containsString("<rightType>"));
		assertThat(result, containsString("<properties>"));
		assertThat(result, containsString("<key "));
		assertThat(result, containsString("<value "));
	}

	/**
	 * Test marshalling an {@link AssetAssociationToImpl},
	 * {@link AssetAssociationPropertyToImpl} and an {@link AssetAssociationToList}
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void marshalAssetAssociationToList() throws JAXBException
	{
		final AssetAssociationTo assetAssociationOne = new AssetAssociationToImpl();
		assetAssociationOne.setAssociation("hasAttribute");
		assetAssociationOne.setAssociationId(new Long(7));
		assetAssociationOne.setLeftAsset("Person");
		assetAssociationOne.setLeftAssetId(new Long(1));
		assetAssociationOne.setLeftNamespace("@DSCUSTOM-26@");
		assetAssociationOne.setLeftNamespaceId(new Long(2));
		assetAssociationOne.setLeftType("Phyiscal Class");
		assetAssociationOne.setLeftTypeId(new Long(3));
		assetAssociationOne.setRightAsset("genderDwid");
		assetAssociationOne.setRightAssetId(new Long(4));
		assetAssociationOne.setRightNamespace("@DSCUSTOM-26@");
		assetAssociationOne.setRightNamespaceId(new Long(5));
		assetAssociationOne.setRightType("Class Attribute");
		assetAssociationOne.setRightTypeId(new Long(6));

		final Collection<AssetAssociationProperty> propertiesOne = new ArrayList<>();
		final AssetAssociationProperty property = new AssetAssociationPropertyToImpl();
		property.setName("JAVA_DATA_TYPE");
		property.setValue("java.lang.Long");
		propertiesOne.add(property);

		assetAssociationOne.setProperties(propertiesOne);

		final AssetAssociationTo assetAssociationTwo = new AssetAssociationToImpl();
		assetAssociationTwo.setAssociation("hasAttribute");
		assetAssociationTwo.setAssociationId(new Long(7));
		assetAssociationTwo.setLeftAsset("Person");
		assetAssociationTwo.setLeftAssetId(new Long(1));
		assetAssociationTwo.setLeftNamespace("@DSCUSTOM-26@");
		assetAssociationTwo.setLeftNamespaceId(new Long(2));
		assetAssociationTwo.setLeftType("Phyiscal Class");
		assetAssociationTwo.setLeftTypeId(new Long(3));
		assetAssociationTwo.setRightAsset("raceDwid");
		assetAssociationTwo.setRightAssetId(new Long(4));
		assetAssociationTwo.setRightNamespace("@DSCUSTOM-26@");
		assetAssociationTwo.setRightNamespaceId(new Long(5));
		assetAssociationTwo.setRightType("Class Attribute");
		assetAssociationTwo.setRightTypeId(new Long(6));

		final Collection<AssetAssociationProperty> propertiesTwo = new ArrayList<>();
		propertiesTwo.add(property);

		assetAssociationTwo.setProperties(propertiesTwo);

		final List<AssetAssociation> associations = new ArrayList<>();
		associations.add(assetAssociationOne);
		associations.add(assetAssociationTwo);

		final AssetAssociationToList associationToList = new AssetAssociationToList(
				associations);

		final XmlService xmlService = new XmlServiceImpl();
		final String result = xmlService.marshal(associationToList);

		assertThat(result, containsString("assetAssociationList>"));
		assertThat(result, containsString("<assetAssociation>"));
		assertThat(result, containsString("<leftType>"));
		assertThat(result, containsString("<rightType>"));
		assertThat(result, containsString("<properties>"));
		assertThat(result, containsString("<key "));
		assertThat(result, containsString("<value "));
	}

}
