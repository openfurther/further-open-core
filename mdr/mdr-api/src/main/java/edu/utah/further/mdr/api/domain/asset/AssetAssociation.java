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
package edu.utah.further.mdr.api.domain.asset;

import java.util.Collection;
import java.util.Map;

import edu.utah.further.core.api.data.PersistentEntity;
import edu.utah.further.core.api.lang.CopyableFrom;

/**
 * An asset's associations
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Mar 27, 2012
 */
public interface AssetAssociation extends PersistentEntity<Long>,
		CopyableFrom<AssetAssociation, AssetAssociation>
{
	/**
	 * Return the leftType property.
	 * 
	 * @return the leftType
	 */
	String getLeftType();

	/**
	 * Set a new value for the leftType property.
	 * 
	 * @param leftType
	 *            the leftType to set
	 */
	void setLeftType(String leftType);

	/**
	 * Return the leftTypeId property.
	 * 
	 * @return the leftTypeId
	 */
	Long getLeftTypeId();

	/**
	 * Set a new value for the leftTypeId property.
	 * 
	 * @param leftTypeId
	 *            the leftTypeId to set
	 */
	void setLeftTypeId(Long leftTypeId);

	/**
	 * Return the leftNamespace property.
	 * 
	 * @return the leftNamespace
	 */
	String getLeftNamespace();

	/**
	 * Set a new value for the leftNamespace property.
	 * 
	 * @param leftNamespace
	 *            the leftNamespace to set
	 */
	void setLeftNamespace(String leftNamespace);

	/**
	 * Return the leftNamespaceId property.
	 * 
	 * @return the leftNamespaceId
	 */
	Long getLeftNamespaceId();

	/**
	 * Set a new value for the leftNamespaceId property.
	 * 
	 * @param leftNamespaceId
	 *            the leftNamespaceId to set
	 */
	void setLeftNamespaceId(Long leftNamespaceId);

	/**
	 * Return the leftAsset property.
	 * 
	 * @return the leftAsset
	 */
	String getLeftAsset();

	/**
	 * Set a new value for the leftAsset property.
	 * 
	 * @param leftAsset
	 *            the leftAsset to set
	 */
	void setLeftAsset(String leftAsset);

	/**
	 * Return the leftAssetId property.
	 * 
	 * @return the leftAssetId
	 */
	Long getLeftAssetId();

	/**
	 * Set a new value for the leftAssetId property.
	 * 
	 * @param leftAssetId
	 *            the leftAssetId to set
	 */
	void setLeftAssetId(Long leftAssetId);

	/**
	 * Return the associationLabel property.
	 * 
	 * @return the associationLabel
	 */
	String getAssociation();

	/**
	 * Set a new value for the associationLabel property.
	 * 
	 * @param associationLabel
	 *            the associationLabel to set
	 */
	void setAssociation(String association);

	/**
	 * Return the assocationId property.
	 * 
	 * @return the assocationId
	 */
	Long getAssociationId();

	/**
	 * Set a new value for the assocationId property.
	 * 
	 * @param assocationId
	 *            the assocationId to set
	 */
	void setAssociationId(Long associationId);

	/**
	 * Return the rightType property.
	 * 
	 * @return the rightType
	 */
	String getRightType();

	/**
	 * Set a new value for the rightType property.
	 * 
	 * @param rightType
	 *            the rightType to set
	 */
	void setRightType(String rightType);

	/**
	 * Return the rightTypeId property.
	 * 
	 * @return the rightTypeId
	 */
	Long getRightTypeId();

	/**
	 * Set a new value for the rightTypeId property.
	 * 
	 * @param rightTypeId
	 *            the rightTypeId to set
	 */
	void setRightTypeId(Long rightTypeId);

	/**
	 * Return the rightNamespace property.
	 * 
	 * @return the rightNamespace
	 */
	String getRightNamespace();

	/**
	 * Set a new value for the rightNamespace property.
	 * 
	 * @param rightNamespace
	 *            the rightNamespace to set
	 */
	void setRightNamespace(String rightNamespace);

	/**
	 * Return the rightNamespaceId property.
	 * 
	 * @return the rightNamespaceId
	 */
	Long getRightNamespaceId();

	/**
	 * Set a new value for the rightNamespaceId property.
	 * 
	 * @param rightNamespaceId
	 *            the rightNamespaceId to set
	 */
	void setRightNamespaceId(Long rightNamespaceId);

	/**
	 * Return the rightAsset property.
	 * 
	 * @return the rightAsset
	 */
	String getRightAsset();

	/**
	 * Set a new value for the rightAsset property.
	 * 
	 * @param rightAsset
	 *            the rightAsset to set
	 */
	void setRightAsset(String rightAsset);

	/**
	 * Return the rightAssetId property.
	 * 
	 * @return the rightAssetId
	 */
	Long getRightAssetId();

	/**
	 * Set a new value for the rightAssetId property.
	 * 
	 * @param rightAssetId
	 *            the rightAssetId to set
	 */
	void setRightAssetId(Long rightAssetId);

	/**
	 * Return the properties property.
	 * 
	 * @return the properties
	 */
	Collection<AssetAssociationProperty> getProperties();

	/**
	 * Set a new value for the properties property.
	 * 
	 * @param properties
	 *            the properties to set
	 */
	void setProperties(Collection<AssetAssociationProperty> properties);

	/**
	 * Returns the {@link Collection} of {@link AssetAssociationProperty}'s as a map of
	 * key/value's
	 * 
	 * @return
	 */
	Map<String, String> getPropertiesAsMap();
}
