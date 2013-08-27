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
package edu.utah.further.ds.impl.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.chain.ChainRequestImpl;
import edu.utah.further.ds.api.service.metadata.MetaDataService;
import edu.utah.further.ds.api.util.AttributeName;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;

/**
 * A life cycle responsible for retrieving information about a data source (up, down,
 * etc). Typically only wired with {@link MetaDataService}, but it is possible to add more
 * processes if necessary.
 *
 * Implementations have access to the {@link #commandType}-name request attribute, and
 * should set an instance of this object as a result of any query.
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
 * @version Feb 10, 2010
 */
public class MetaDataLifeCycle extends AbstractLifeCycle<DsMetaData, DsMetaData>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = LoggerFactory.getLogger(MetaDataLifeCycle.class);

	// ========================= FIELDS ====================================

	/**
	 * Determines the request attribute in which chain output is stored.
	 */
	private final AttributeName attributeName;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor. Uses the default attribute name value of
	 * {@link AttributeName#META_DATA}.
	 *
	 * @param commandType
	 */
	public MetaDataLifeCycle()
	{
		this(AttributeName.META_DATA);
	}

	/**
	 * @param commandType
	 */
	public MetaDataLifeCycle(final AttributeName attributeName)
	{
		super(DsMetaData.class);
		this.attributeName = attributeName;
	}

	// ========================= IMPL: CommandTrigger ======================

	/**
	 * Ignoring the input argument for the time being. In the future, it may contain a
	 * time interval to retrieve historical meta data, for example. The input data type
	 * might change to some other convenient marshallable TO.
	 *
	 *
	 * @param input
	 * @return
	 * @see edu.utah.further.fqe.ds.api.service.CommandTrigger#triggerCommand(java.lang.Object)
	 */
	@Override
	public DsMetaData triggerCommand(final DsMetaData input)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Starting life cycle, input " + input);
		}
		final RequestHandler requestHandlerChain = buildChain();
		final ChainRequest chainRequest = new ChainRequestImpl();

		// Inject the input parameter into the request. Will be overridden with the
		// command's output by the processing chain.
		chainRequest.setAttribute(attributeName, input);

		requestHandlerChain.handle(chainRequest);

		return chainRequest.getAttribute(attributeName);
	}
}
