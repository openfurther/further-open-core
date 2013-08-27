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
import edu.utah.further.core.api.context.Named;
import edu.utah.further.core.api.context.Valued;
import edu.utah.further.dts.api.annotation.DtsEntity;
import edu.utah.further.dts.api.domain.attribute.DtsQualifier;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.namespace.DtsObject;

/**
 * An interface extracted from Apelon DTS API's <code>Association</code>.
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
 * @version Jun 30, 2009
 */
@DtsEntity
@Api
public interface DtsAssociation extends Named, Valued<String>
{
	// ========================= READ METHODS ==============================

	/**
	 * Return the association type of this object.
	 *
	 * @return the association type of this object
	 */
	EnumAssociationType getEnumAssociationType();

	/**
	 * @return
	 * @see com.apelon.dts.client.concept.Association#getAssosciationType()
	 */
	DtsAssociationType getAssociationType();

	/**
	 * Return the item the association is drawn from. Apelon returns a {@link DtsObject}
	 * here, but we don't need to be that generic. We always work with concepts.
	 *
	 * @return the item the association is drawn from
	 * @see com.apelon.dts.client.concept.Association#getFromItem()
	 */
	DtsConcept getFromItem();

	/**
	 * Return the item the association is drawn to. Apelon returns a {@link DtsObject}
	 * here, but we don't need to be that generic. We always work with concepts.
	 *
	 * @return the item the association is drawn to
	 * @see com.apelon.dts.client.concept.Association#getFromItem()
	 */
	DtsConcept getToItem();
	
	/**
	 * Tests whether the given {@link DtsQualifier} is an attribute of this association.
	 * @param qualifier
	 * @return
	 * @see com.apelon.dts.client.association.QualifiedAssociation#containsQualifier(DtsQualifier)
	 */
	boolean containsQualifier(DtsQualifier qualifier);

	// ========================= WRITE METHODS =============================

	// /**
	// * @param associationType
	// * @see com.apelon.dts.client.concept.Association#setAssosciationType()
	// */
	// void setAssociationType(DtsAssociationType associationType);
}