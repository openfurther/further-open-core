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
package edu.utah.further.dts.api.domain.association;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.dts.api.annotation.DtsEntity;
import edu.utah.further.dts.api.domain.concept.DtsConcept;

/**
 * An interface extracted from Apelon DTS API's <code>Synonym</code>.
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
 * @version Jul 1, 2009
 */
@DtsEntity
@Api
public interface DtsSynonym extends DtsAssociation
{
	// ========================= READ METHODS ==============================

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.Synonym#getConcept()
	 */
	DtsConcept getConcept();

	// /**
	// * @return
	// * @see com.apelon.dts.client.concept.Synonym#getTerm()
	// */
	// Term getTerm();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.Synonym#isLocalAddition()
	 */
	boolean isLocalAddition();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.Synonym#isPreferred()
	 */
	boolean isPreferred();

	// ========================= WRITE METHODS =============================

	// /**
	// * @param arg0
	// * @see com.apelon.dts.client.concept.Synonym#setConcept()
	// */
	// void setConcept(DtsConcept arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.Synonym#setLocalAddition()
	 */
	void setLocalAddition(boolean arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.concept.Synonym#setPreferred()
	 */
	void setPreferred(boolean arg0);
}