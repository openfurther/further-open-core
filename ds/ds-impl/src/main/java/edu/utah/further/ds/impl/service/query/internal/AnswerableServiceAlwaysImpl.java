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
package edu.utah.further.ds.impl.service.query.internal;

import org.springframework.stereotype.Service;

import edu.utah.further.ds.api.service.query.AnswerableService;
import edu.utah.further.fqe.ds.api.domain.DsMetaData;
import edu.utah.further.fqe.ds.api.domain.QueryContext;

/**
 * A dummy implementation that always returns true, regardless of the conditions.
 * 
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
 * @version Aug 6, 2013
 */
@Service("answerableServiceAlways")
public class AnswerableServiceAlwaysImpl implements AnswerableService
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.ds.api.service.query.AnswerableService#canAnswer(edu.utah.further
	 * .fqe.ds.api.domain.QueryContext, edu.utah.further.fqe.ds.api.domain.DsMetaData)
	 */
	@Override
	public boolean canAnswer(final QueryContext queryContext, final DsMetaData dsMetaData)
	{
		// always return true - the query is always answerable
		return true;
	}

}
