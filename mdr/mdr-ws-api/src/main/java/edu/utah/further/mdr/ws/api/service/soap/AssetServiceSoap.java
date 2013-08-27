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
package edu.utah.further.mdr.ws.api.service.soap;

import javax.jws.WebService;

import edu.utah.further.core.api.exception.WsException;
import edu.utah.further.core.api.ws.Documentation;
import edu.utah.further.mdr.api.domain.asset.AssetAssociation;
import edu.utah.further.mdr.ws.api.to.AssetAssociationToImpl;
import edu.utah.further.mdr.ws.api.to.AssetAssociationToList;
import edu.utah.further.mdr.ws.api.to.AssetToImpl;
import edu.utah.further.mdr.ws.api.to.ResourceToImpl;

/**
 * Asset search/query SOAP web services - central class. Each method is a different web
 * service.
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
 * @version Feb 18, 2009
 */
@WebService
@Documentation(name = "MDR asset CRUD services", description = "MDR asset CRUD services")
public interface AssetServiceSoap
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

	/**
	 * Find an asset by unique identifier.
	 * 
	 * @param id
	 *            asset unique identifier to look for
	 * @return persistent asset domain entity, if found. If not found, returns
	 *         <code>null</code>
	 */
	@Documentation(name = "Find asset by ID", description = "Return an asset by ID.")
	AssetToImpl findAssetById(Long id) throws WsException;

	/**
	 * Find a resource by unique identifier.
	 * 
	 * @param id
	 *            resource unique identifier to look for
	 * @return persistent asset domain entity, if found. If not found, returns
	 *         <code>null</code>
	 */
	@Documentation(name = "Find resource by ID", description = "Return a resource by ID.")
	ResourceToImpl findResourceById(Long id) throws WsException;
	
	/**
	 * Find an asset association by unique identifier.
	 * 
	 * @param id
	 *            asset association unique identifier to look for
	 * @return an {@link AssetAssociation} if one exists
	 */
	@Documentation(name = "Find asset assocation by ID", description = "Return an asset association by ID.")
	AssetAssociationToImpl findAssetAssocationById(Long id);

	/**
	 * Find an {@link AssetAssociation} by left/right label.
	 * 
	 * @param side
	 *            the side of the association (left/right)
	 * @param label
	 *            the label of the association
	 * @return an {@link AssetAssociation} if one exists
	 */
	@Documentation(name = "Find asset association by side and label", description = "Return an association by side and label")
	AssetAssociationToList findAssetAssociationBySideAndLabel(String side, String label)
			throws WsException;

	/**
	 * Find an {@link AssetAssociation} by left/right label and association.
	 * 
	 * @param side
	 *            the side of the association (left/right)
	 * @param label
	 *            the label of the association
	 * @param association
	 *            the assocation
	 * @return an {@link AssetAssociation} if one exists
	 */
	@Documentation(name = "Find asset association by side and label and association", description = "Return an association by side, label, and association")
	AssetAssociationToList findAssetAssociationBySideLabelAndAssociation(String side, String label,
			String association) throws WsException;
}
