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
package edu.utah.further.dts.impl.service

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.association.AssociationQuery;
import com.apelon.dts.client.association.AssociationType
import com.apelon.dts.client.concept.ConceptAttributeSetDescriptor;
import com.apelon.dts.client.concept.ConceptParent
import com.apelon.dts.client.concept.DTSConcept;
import com.apelon.dts.client.concept.DTSSearchOptions;
import com.apelon.dts.client.concept.NavParentContext;
import com.apelon.dts.client.concept.NavQuery
import com.apelon.dts.client.concept.OntylogConcept;
import com.apelon.dts.client.concept.SearchQuery;

import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.util.cache.CachingService;
import edu.utah.further.dts.api.domain.concept.DtsConcept
import edu.utah.further.dts.api.util.SearchBy;
import edu.utah.further.dts.impl.domain.DtsConceptImpl

import spock.lang.Specification;

@ContextConfiguration(locations = [
	"/dts-impl-test-context.xml"
])
class UTestDtsOperationServiceImpl extends Specification {
	private static final String TEST_CONCEPT = "test";
	private static final String TEST_CONCEPT_CONTAINS = "*test*";
	private static final String TEST_CONCEPT_STARTS_WITH = "test*";

	@Autowired
	private DtsOperationServiceImpl dos;

	// mocks
	private DtsObjectFactory dof;
	private ConceptFinderService cfs;
	private CachingService cachingService;

	private SearchQuery searchQuery;

	// fake data
	private OntylogConcept[] ocs;

	def setup()
	{
		dof = Mock()
		cfs = Mock()
		cachingService = Mock()
		dos.dtsObjectFactory = dof
		dos.cfs = cfs
		dos.cacheService = cachingService

		searchQuery = Mock(SearchQuery)
	}

	def "call to findConceptsByName with no result"()
	{
		given:"input parameters and return values of edge methods"
		ocs = new OntylogConcept[0];
		def namespaceId = 1
		def conceptName = TEST_CONCEPT
		def searchBy = SearchBy.CONTAINS
		def synonyms = false

		and:"some expctations"
		1 * dof.createSearchQuery() >> searchQuery
		1 * searchQuery.findConceptsWithNameMatching(
				TEST_CONCEPT_CONTAINS,
				{it instanceof DTSSearchOptions},
				{it instanceof Boolean}) >> ocs
		0 * _._

		when:
		final List<DtsConcept> concepts = dos.findConceptsByName(
				namespaceId,
				conceptName,
				searchBy,
				synonyms)

		then:
		concepts.size() == 0
	}

	def "call to findConceptsByName with one result"()
	{
		given:"input parameters and return values of edge methods"
		ocs = new OntylogConcept[1];
		ocs[0] = new OntylogConcept("s1", 1, "s2", 2);
		def namespaceId = 1
		def conceptName = TEST_CONCEPT
		def searchBy = SearchBy.CONTAINS
		def synonyms = false

		and:"some expectations"
		dof.createSearchQuery() >> searchQuery
		1 * searchQuery.findConceptsWithNameMatching(
				TEST_CONCEPT_CONTAINS,
				{it instanceof DTSSearchOptions},
				{it instanceof Boolean}) >> ocs
		0 * _._

		when:
		final List<DtsConcept> concepts = dos.findConceptsByName(
				namespaceId,
				conceptName,
				searchBy,
				synonyms)

		then:
		concepts.size() == 1
	}

	def "call to findConceptsByName with null SearchBy defaults to STARTS_WITH, returns one result"()
	{
		given:"input parameters and return values of edge methods"
		ocs = new OntylogConcept[1];
		ocs[0] = new OntylogConcept("s1", 1, "s2", 2);
		def namespaceId = 1
		def conceptName = TEST_CONCEPT
		def searchBy = null
		def synonyms = false

		and:"some expectations"
		dof.createSearchQuery() >> searchQuery
		1 * searchQuery.findConceptsWithNameMatching(
				TEST_CONCEPT_STARTS_WITH,
				{it instanceof DTSSearchOptions},
				{it instanceof Boolean}) >> ocs
		0 * _._

		when:
		final List<DtsConcept> concepts = dos.findConceptsByName(
				namespaceId,
				conceptName,
				searchBy,
				synonyms)

		then:
		concepts.size() == 1
	}

	def "call to findConceptsByName where findCocneptsWithNameMatching throws a DTSException, expeect ApplicationException"()
	{
		given:"input parameters and return values of edge methods"
		ocs = new OntylogConcept[1];
		ocs[0] = new OntylogConcept("s1", 1, "s2", 2);
		def namespaceId = 1
		def conceptName = TEST_CONCEPT
		def searchBy = SearchBy.CONTAINS
		def synonyms = false

		and:"some expectations"
		dof.createSearchQuery() >> searchQuery
		1 * searchQuery.findConceptsWithNameMatching(
				TEST_CONCEPT_CONTAINS,
				{it instanceof DTSSearchOptions},
				{it instanceof Boolean}) >> {throw new DTSException("") }
		0 * _._

		when:
		final List<DtsConcept> concepts = dos.findConceptsByName(
				namespaceId,
				conceptName,
				searchBy,
				synonyms)

		then:
		thrown(ApplicationException)
	}

	def "call to hasSuperConcept with a single, terminal traversal returns true"()
	{
		given:"some mocks we'll use"
		NavQuery navQuery = Mock()
		DtsConceptImpl dtsConcept = Mock()
		DTSConcept apelonConcept = Mock()
		AssociationQuery assocQuery = Mock()
		def namespaceId = 1
		AssociationType assocType = Mock()
		NavParentContext navParentContext = Mock()
		ConceptParent conceptParent = Mock()
		def apelonConceptName = "apelonCN"

		and:"parameters"
		def superConceptName = "SUPER"
		def associationTypeName = "assocType"

		and:"expectations"
		1 * dof.createNavQuery() >> navQuery
		1 * dtsConcept.getAsDTSConcept() >> apelonConcept
		1 * dof.createAssociationQuery() >> assocQuery
		1 * dtsConcept.getNamespaceId() >> namespaceId
		1 * assocQuery.findAssociationTypeByName(associationTypeName, namespaceId) >> assocType
		1 * navQuery.getNavParentContext(apelonConcept, ConceptAttributeSetDescriptor.ALL_ATTRIBUTES, assocType) >> navParentContext
		1 * navParentContext.getParents() >> [conceptParent]
		1 * conceptParent.getName() >> apelonConceptName // not yet super, let's traverse once
		1 * navParentContext.getParents() >> [conceptParent]
		1 * navQuery.getNavParentContext(conceptParent, ConceptAttributeSetDescriptor.ALL_ATTRIBUTES, assocType) >> navParentContext
		1 * navParentContext.getParents() >> [conceptParent]
		1 * conceptParent.getName() >> superConceptName
		0 * _._

		when:
		boolean hasSuper = dos.hasSuperConcept(dtsConcept, superConceptName, associationTypeName)

		then:
		hasSuper == true
	}

	def "call to hasSuperConcept with no traversal returns false (no parent)"() {
		given:"some mocks we'll use"
		NavQuery navQuery = Mock()
		DtsConceptImpl dtsConcept = Mock()
		DTSConcept apelonConcept = Mock()
		AssociationQuery assocQuery = Mock()
		def namespaceId = 1
		AssociationType assocType = Mock()
		NavParentContext navParentContext = Mock()
		ConceptParent conceptParent = Mock()
		def apelonConceptName = "apelonCN"

		and:"parameters"
		def superConceptName = "SUPER"
		def associationTypeName = "assocType"

		and:"expectations"
		1 * dof.createNavQuery() >> navQuery
		1 * dtsConcept.getAsDTSConcept() >> apelonConcept
		1 * dof.createAssociationQuery() >> assocQuery
		1 * dtsConcept.getNamespaceId() >> namespaceId
		1 * assocQuery.findAssociationTypeByName(associationTypeName, namespaceId) >> assocType
		1 * navQuery.getNavParentContext(apelonConcept, ConceptAttributeSetDescriptor.ALL_ATTRIBUTES, assocType) >> navParentContext
		2 * navParentContext.getParents() >> []
		0 * _._

		when:
		boolean hasSuper = dos.hasSuperConcept(dtsConcept, superConceptName, associationTypeName)

		then:
		hasSuper == false
	}

	def "call to hasSuperConcept with a single, multiple traversal, hits traversal limit, returns false"()
	{
		given:"some mocks we'll use"
		NavQuery navQuery = Mock()
		DtsConceptImpl dtsConcept = Mock()
		DTSConcept apelonConcept = Mock()
		AssociationQuery assocQuery = Mock()
		def namespaceId = 1
		AssociationType assocType = Mock()
		NavParentContext navParentContext = Mock()
		ConceptParent conceptParent = Mock()
		def apelonConceptName = "apelonCN"

		and:"parameters"
		def superConceptName = "SUPER"
		def associationTypeName = "assocType"

		and:"expectations"
		1 * dof.createNavQuery() >> navQuery
		1 * dtsConcept.getAsDTSConcept() >> apelonConcept
		1 * dof.createAssociationQuery() >> assocQuery
		1 * dtsConcept.getNamespaceId() >> namespaceId
		1 * assocQuery.findAssociationTypeByName(associationTypeName, namespaceId) >> assocType
		1 * navQuery.getNavParentContext(apelonConcept, ConceptAttributeSetDescriptor.ALL_ATTRIBUTES, assocType) >> navParentContext
		29 * navParentContext.getParents() >> [conceptParent]
		15 * conceptParent.getName() >> apelonConceptName // not yet super, let's traverse once
		15 * navQuery.getNavParentContext(conceptParent, ConceptAttributeSetDescriptor.ALL_ATTRIBUTES, assocType) >> navParentContext
		//		30 * navParentContext.getParents() >> [conceptParent]
		//		16 * conceptParent.getName() >> apelonConceptName
		0 * _._

		when:
		boolean hasSuper = dos.hasSuperConcept(dtsConcept, superConceptName, associationTypeName)

		then:
		hasSuper == false
	}

}
