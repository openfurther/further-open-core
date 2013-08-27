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
package edu.utah.further.dts.api.to;

import java.util.List;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.domain.association.DtsSynonym;
import edu.utah.further.dts.api.domain.concept.DtsConcept;

/**
 * A DTS <code>OntylogConcept</code> transfer object.
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
 * @version Dec 17, 2008
 */
@Api
public interface DtsConceptTo extends DtsConcept, DtsCompositeTo
{
	// ========================= METHODS FROM DTSConcept ===================

	/**
	 * Set a new value for the matchString property.
	 *
	 * @param matchString
	 *            the matchString to set
	 */
	void setMatchString(String matchString);

	/**
	 * Set a new value for the numSpecifiedConceptAssociations property.
	 *
	 * @param numSpecifiedConceptAssociations
	 *            the numSpecifiedConceptAssociations to set
	 */
	@Override
	void setNumSpecifiedConceptAssociations(int numSpecifiedConceptAssociations);

	/**
	 * Set a new value for the numSpecifiedInverseConceptAssociations property.
	 *
	 * @param numSpecifiedInverseConceptAssociations
	 *            the numSpecifiedInverseConceptAssociations to set
	 */
	@Override
	void setNumSpecifiedInverseConceptAssociations(
			int numSpecifiedInverseConceptAssociations);

	/**
	 * Set a new value for the numSpecifiedProperties property.
	 *
	 * @param numSpecifiedProperties
	 *            the numSpecifiedProperties to set
	 */
	void setNumSpecifiedProperties(int numSpecifiedProperties);

	/**
	 * Set a new value for the numSpecifiedSynonyms property.
	 *
	 * @param numSpecifiedSynonyms
	 *            the numSpecifiedSynonyms to set
	 */
	@Override
	void setNumSpecifiedSynonyms(int numSpecifiedSynonyms);

	/**
	 * Set a new value for the fetchedConceptAssociations property.
	 *
	 * @param fetchedConceptAssociations
	 *            the fetchedConceptAssociations to set
	 */
	void setFetchedConceptAssociations(List<DtsAssociation> fetchedConceptAssociations);

	/**
	 * Set a new value for the fetchedInverseConceptAssociations property.
	 *
	 * @param fetchedInverseConceptAssociations
	 *            the fetchedInverseConceptAssociations to set
	 */
	void setFetchedInverseConceptAssociations(
			List<DtsAssociation> fetchedInverseConceptAssociations);

	/**
	 * Set a new value for the fetchedSynonyms property.
	 *
	 * @param fetchedSynonyms
	 *            the fetchedSynonyms to set
	 */
	void setFetchedSynonyms(List<DtsSynonym> fetchedSynonyms);

	// ========================= METHODS FROM DTSOntylogyConcept ===========

	/**
	 * Set a new value for the numSpecifiedRoles property.
	 *
	 * @param numSpecifiedRoles
	 *            the numSpecifiedRoles to set
	 */
	@Override
	void setNumSpecifiedRoles(int numSpecifiedRoles);

	/**
	 * Set a new value for the numSpecifiedInverseRoles property.
	 *
	 * @param numSpecifiedInverseRoles
	 *            the numSpecifiedInverseRoles to set
	 */
	@Override
	void setNumSpecifiedInverseRoles(int numSpecifiedInverseRoles);
}