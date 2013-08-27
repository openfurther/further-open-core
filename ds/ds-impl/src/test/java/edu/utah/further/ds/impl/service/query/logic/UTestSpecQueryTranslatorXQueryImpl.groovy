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
package edu.utah.further.ds.impl.service.query.logic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import spock.lang.Specification
import edu.utah.further.core.api.chain.AttributeContainerImpl
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.xml.XmlService
import edu.utah.further.core.chain.ChainRequestImpl
import edu.utah.further.core.query.domain.SearchQueryTo
import edu.utah.further.core.xml.xquery.XQueryService
import edu.utah.further.ds.api.util.AttributeName
import edu.utah.further.fqe.ds.api.domain.DsMetaData
import edu.utah.further.fqe.ds.api.domain.DsState
import edu.utah.further.fqe.ds.api.to.QueryContextTo;
import edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest
import groovy.util.logging.Slf4j

/**
 * ...
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
 * @version Aug 2, 2013
 */
@ContextConfiguration(locations = [
	"/META-INF/core/xquery/core-xquery-datasource.xml",
	"/ds-impl-test-annotation-context.xml"
])
@Slf4j
class UTestSpecQueryTranslatorXQueryImpl extends Specification {

	@Autowired
	XQueryService xQueryService

	@Autowired
	XmlService xmlService

	def queryTranslator = new QueryTranslatorXQueryImpl()
	def request;
	def queryContext;

	def setup() {
		queryTranslator.xmlService = xmlService
		queryTranslator.xqueryService = xQueryService;

		request = new ChainRequestImpl(new AttributeContainerImpl(new HashMap<>()))
		request.attributes = [(AttributeName.QUERY_TRANSLATION.label):"query/fqtCall.xq",
			(AttributeName.META_DATA.label):new DsMetaData("test", "test ds", new Long(311), DsState.ACTIVE)]
		
		def query = new SearchQueryTo();
		query.id = 10
		query.rootObject = "Person"
		queryContext = Mock(QueryContextTo) {
			1 * getQuery() >> query
		}
	}

	def "Execute Query Translation using the XQuery Request Processor"() {

		when:
		queryTranslator.assetServiceRest = Mock(AssetServiceRest) {
			1 * getActiveResourceContentByPath(_ as String) >> """
				declare variable \$tgNmspcId as xs:string external;
				declare variable \$docName as document-node() external;
				\$docName"""
		}

		and:
		def searchQuery = queryTranslator.translate(queryContext, request.attributes)

		then:
		searchQuery.id == 10
		searchQuery.rootObject == "Person"
	}

	def "Execute malformed XQuery"() {
		when:
		queryTranslator.assetServiceRest = Mock(AssetServiceRest) {
			1 * getActiveResourceContentByPath(_ as String) >> 'blah'
		}

		and:
		def searchQuery = queryTranslator.translate(queryContext, request.attributes)

		then:
		thrown(ApplicationException)
	}
}
