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
package edu.utah.further.fqe.ds.api.factory;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;

import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.to.QueryContextTo;

/**
 * Resets the stale date and time of {@link QueryContext} to <code>null</code>. This
 * passes control over query timeout to a different component, because <code>null</code>
 * stale date fields do not override non-<code>null</code> stale date fields set by other
 * components on the appropriate {@link QueryContextTo}s.
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
 * @version Mar 25, 2010
 */
// @Mock
public final class ResettingStaleDateTimeFactory implements StaleDateTimeFactory
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ResettingStaleDateTimeFactory.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize the sealer.
	 */
	@PostConstruct
	public void afterPropertiesSet()
	{
		if (log.isInfoEnabled())
		{
			log.info("Initializing");
		}
	}

	// =============== IMPL: StaleDateTimeFactory ===========================

	/**
	 * Get the current stale date
	 * 
	 * @return the date & time at which a query will become stale from this point onwards.
	 */
	@Override
	public Date getStaleDateTime()
	{
		return null;
	}

	// ========================= GET/SET ==============================
}
