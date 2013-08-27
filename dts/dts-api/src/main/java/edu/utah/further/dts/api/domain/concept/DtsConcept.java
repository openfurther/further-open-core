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
package edu.utah.further.dts.api.domain.concept;

import java.util.List;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.dts.api.annotation.DtsEntity;
import edu.utah.further.dts.api.domain.association.DtsAssociation;
import edu.utah.further.dts.api.domain.association.DtsSynonym;
import edu.utah.further.dts.api.domain.namespace.DtsComposite;
import edu.utah.further.dts.api.domain.namespace.DtsPropertiedObject;
import edu.utah.further.dts.api.to.DtsConceptUniqueId;

/**
 * An interface extracted from Apelon DTS API's <code>OntylogConcept</code> and
 * <code>DTSConcept</code>. We do not distinguish between the two.
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
@DtsEntity
@Api
public interface DtsConcept extends DtsPropertiedObject, DtsComposite
{
	// ========================= METHODS FROM DTSConcept ===================

	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#addConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// boolean addConceptAssociation(ConceptAssociation arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#addInverseConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// boolean addInverseConceptAssociation(ConceptAssociation arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#addSynonym(com.apelon.dts.client.association.Synonym)
	// */
	// boolean addSynonym(Synonym arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#containsConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// boolean containsConceptAssociation(ConceptAssociation arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#containsInverseConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// boolean containsInverseConceptAssociation(ConceptAssociation arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#containsSynonym(com.apelon.dts.client.association.Synonym)
	// */
	// boolean containsSynonym(Synonym arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#deleteConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// boolean deleteConceptAssociation(ConceptAssociation arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#deleteInverseConceptAssociation(com.apelon.dts.client.association.ConceptAssociation)
	// */
	// boolean deleteInverseConceptAssociation(ConceptAssociation arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.DTSConcept#deleteSynonym(com.apelon.dts.client.association.Synonym)
	// */
	// boolean deleteSynonym(Synonym arg0);
	//

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getFetchedConceptAssociations()
	 */
	List<DtsAssociation> getFetchedConceptAssociations();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getFetchedInverseConceptAssociations()
	 */
	List<DtsAssociation> getFetchedInverseConceptAssociations();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getFetchedPreferredTerm()
	 */
	DtsSynonym getFetchedPreferredTerm();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getFetchedSynonyms()
	 */
	List<DtsSynonym> getFetchedSynonyms();

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.DTSConcept#getMatchItemType()
	// */
	// MatchItemType getMatchItemType();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getMatchString()
	 */
	String getMatchString();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getNumSpecifiedConceptAssociations()
	 */
	int getNumSpecifiedConceptAssociations();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getNumSpecifiedInverseConceptAssociations()
	 */
	int getNumSpecifiedInverseConceptAssociations();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.DTSConcept#getNumSpecifiedSynonyms()
	 */
	int getNumSpecifiedSynonyms();

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.DTSConcept#setNumSpecifiedConceptAssociations(int)
	 */
	void setNumSpecifiedConceptAssociations(int arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.DTSConcept#setNumSpecifiedInverseConceptAssociations(int)
	 */
	void setNumSpecifiedInverseConceptAssociations(int arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.DTSConcept#setNumSpecifiedSynonyms(int)
	 */
	void setNumSpecifiedSynonyms(int arg0);

	// ========================= METHODS FROM DTSOntylogConcept ============

	// /**
	// * A view method that returns the DTS object associated with this object, if it
	// * exists.
	// *
	// * @return Apelon DTS Concept view
	// */
	// OntylogConcept getAsOntylogyConcept();

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#addDefiningConcept(com.apelon.dts.client.concept.OntylogConcept)
	 */
	boolean addDefiningConcept(DtsConcept arg0);

	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#addDefiningRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// boolean addDefiningRole(DTSRole arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#addInverseRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// boolean addInverseRole(DTSRole arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#addRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// boolean addRole(DTSRole arg0);

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#containsDefiningConcept(com.apelon.dts.client.concept.OntylogConcept)
	 */
	boolean containsDefiningConcept(DtsConcept arg0);

	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#containsDefiningRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// boolean containsDefiningRole(DTSRole arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#containsInverseRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// boolean containsInverseRole(DTSRole arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#containsRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// boolean containsRole(DTSRole arg0);

	/**
	 * @param arg0
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#deleteDefiningConcept(com.apelon.dts.client.concept.OntylogConcept)
	 */
	boolean deleteDefiningConcept(DtsConcept arg0);

	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#deleteDefiningRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// boolean deleteDefiningRole(DTSRole arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#deleteInverseRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// boolean deleteInverseRole(DTSRole arg0);
	//
	// /**
	// * @param arg0
	// * @return
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#deleteRole(com.apelon.dts.client.attribute.DTSRole)
	// */
	// boolean deleteRole(DTSRole arg0);
	//
	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getDefiningRoleGroups()
	// */
	// List<RoleGroup> getDefiningRoleGroups();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedDefiningConcepts()
	 */
	List<DtsConcept> getFetchedDefiningConcepts();

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedDefiningRoles()
	// */
	// List<DtsRole> getFetchedDefiningRoles();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedHasSubs()
	 */
	boolean getFetchedHasSubs();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedHasSups()
	 */
	boolean getFetchedHasSups();

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedInverseRoles()
	// */
	// List<DtsRole> getFetchedInverseRoles();
	//
	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedKind()
	// */
	// Kind getFetchedKind();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedPrimitive()
	 */
	boolean getFetchedPrimitive();

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedRoles()
	// */
	// List<DtsRole> getFetchedRoles();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedSubconcepts()
	 */
	List<DtsConcept> getFetchedSubconcepts();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getFetchedSuperconcepts()
	 */
	List<DtsConcept> getFetchedSuperconcepts();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getNumSpecifiedInverseRoles()
	 */
	int getNumSpecifiedInverseRoles();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.OntylogConcept#getNumSpecifiedRoles()
	 */
	int getNumSpecifiedRoles();

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.OntylogConcept#getRoleGroups()
	// */
	// List<RoleGroup> getRoleGroups();

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedHasSubs(boolean)
	 */
	void setFetchedHasSubs(boolean arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedHasSups(boolean)
	 */
	void setFetchedHasSups(boolean arg0);

	// /**
	// * @param arg0
	// * @see
	// com.apelon.dts.client.concept.OntylogConcept#setFetchedKind(com.apelon.dts.client.attribute.Kind)
	// */
	// void setFetchedKind(Kind arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedPrimitive(boolean)
	 */
	void setFetchedPrimitive(boolean arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedSubconcepts(com.apelon.dts.client.concept.OntylogConcept[])
	 */
	void setFetchedSubconcepts(List<? extends DtsConcept> arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setFetchedSuperconcepts(com.apelon.dts.client.concept.OntylogConcept[])
	 */
	void setFetchedSuperconcepts(List<? extends DtsConcept> arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setNumSpecifiedInverseRoles(int)
	 */
	void setNumSpecifiedInverseRoles(int arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.OntylogConcept#setNumSpecifiedRoles(int)
	 */
	void setNumSpecifiedRoles(int arg0);

	// ========================= OTHER METHODS =============================

	/**
	 * Return a unique identifier view of the concept.
	 *
	 * @return a unique identifier object corresponding to this object
	 */
	DtsConceptUniqueId getAsUniqueId();
}