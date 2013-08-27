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
package edu.utah.further.ds.api.advice;

import org.aspectj.lang.ProceedingJoinPoint;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.chain.UtilityProcessor;

/**
 * Interface for around advice for any time of {@link RequestProcessor} (
 * {@link QueryExecutor}, {@link UtilityProcessor}, etc.).
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
 * @version Mar 11, 2010
 */
public interface RequestProcessorAroundAdvice
{
	// ========================= METHODS =================================

	/**
	 * Around advice of a chain request processor processing method.
	 *
	 * @param pjp
	 *            AOP joint point
	 * @param chainRequest
	 *            request being handled
	 * @param requestProcessor
	 *            request processor
	 * @return advised request processor's return value
	 */
	Object doAround(ProceedingJoinPoint pjp, ChainRequest chainRequest,
			RequestProcessor requestProcessor);
}
