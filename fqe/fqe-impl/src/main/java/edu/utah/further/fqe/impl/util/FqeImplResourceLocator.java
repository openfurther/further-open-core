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
package edu.utah.further.fqe.impl.util;

import javax.annotation.PostConstruct;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.ResourceLocator;
import edu.utah.further.core.camel.EndpointConsumer;
import edu.utah.further.core.query.domain.SearchQuery;
import edu.utah.further.fqe.api.service.query.QueryContextService;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.util.FqeEndpointNames;
import edu.utah.further.fqe.impl.service.plan.DeEvaluator;

/**
 * A convenient resource-locating class to inject services into non-Spring-managed
 * classes. This implementation does not depend on LTW support/Configurable annotation.
 * Obviously, only domain classes created AFTER this service is initialized can be DI-ed.
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
@Service("fqeImplResourceLocator")
@ResourceLocator
public class FqeImplResourceLocator
{
	// ========================= CONSTANTS =================================

	/**
	 * The singleton instance of this class maintained by Spring.
	 */
	private static FqeImplResourceLocator instance;

	// ========================= DEPENDENCIES ==============================

	/**
	 * Final FQE results are received and cached by this service.
	 */
	@Autowired
	private EndpointConsumer endpointConsumer;

	/**
	 * The request endpoint with marshalling.
	 */
	@EndpointInject(uri = FqeEndpointNames.MARSHAL_REQUEST)
	private Endpoint marshalRequest;

	/**
	 * Handles {@link QueryContext} DAO operations and searches.
	 */
	@Autowired
	private QueryContextService queryContextService;

	/**
	 * Processes {@link SearchQuery}s within a phased FQC plan.
	 */
	@Autowired
	private DeEvaluator deEvaluator;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Set a static reference to the Spring-managed instance so that we can refer to it in
	 * domain classes without LTW/Configurable annotation.
	 * </p>
	 */
	@PostConstruct
	protected void initializeStaticAccess()
	{
		synchronized (FqeImplResourceLocator.class)
		{
			instance = this;
		}
	}

	/**
	 * Return the instance property.
	 * 
	 * @return the instance
	 */
	public static FqeImplResourceLocator getInstance()
	{
		return instance;
	}

	// ========================= METHODS ===================================

	/**
	 * Return the endpointConsumer property.
	 * 
	 * @return the endpointConsumer
	 */
	public EndpointConsumer getEndpointConsumer()
	{
		return endpointConsumer;
	}

	/**
	 * Return the marshalRequest property.
	 * 
	 * @return the marshalRequest
	 */
	public Endpoint getMarshalRequest()
	{
		return marshalRequest;
	}

	/**
	 * Return the queryContextService property.
	 * 
	 * @return the queryContextService
	 */
	public QueryContextService getQueryContextService()
	{
		return queryContextService;
	}

	/**
	 * Return the deEvaluator property.
	 *
	 * @return the deEvaluator
	 */
	public DeEvaluator getDeEvaluator()
	{
		return deEvaluator;
	}
}
