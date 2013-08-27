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
package edu.utah.further.dts.impl.domain.association;

import static edu.utah.further.dts.api.domain.association.EnumAssociationType.SYNONYM;

import com.apelon.dts.client.association.AssociationType;
import com.apelon.dts.client.association.Synonym;
import com.apelon.dts.client.term.Term;

import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.dts.api.domain.association.DtsSynonym;
import edu.utah.further.dts.api.domain.concept.DtsConcept;

/**
 * A DTS concept synonym implementation. Wraps an Apelon DTS synonym object.
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
 * @version Dec 8, 2008
 */
@Implementation
public class DtsSynonymImpl extends DtsAssociationImpl implements DtsSynonym
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * The Apelon DTS synonym to be wrapped.
	 */
	private final Synonym synonym;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * A default c-tor. Required by JAXB.
	 */
	public DtsSynonymImpl()
	{
		this(null);
	}

	/**
	 * Wrap an Apelon synonym with our API.
	 *
	 * @param synonym
	 *            Apelon synonym
	 */
	public DtsSynonymImpl(final Synonym synonym)
	{
		super(SYNONYM, synonym);
		this.synonym = synonym;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @return
	 * @see com.apelon.dts.client.common.DTSObject#toString()
	 */
	@Override
	public String toString()
	{
		return (synonym == null) ? null : synonym.toString();
	}

	// ========================= IMPLEMENTATION: DtsSynonym ================

	/**
	 * @return
	 * @see com.apelon.dts.client.association.Synonym#getConcept()
	 */
	@Override
	public DtsConcept getConcept()
	{
		return getFromItem();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.association.Synonym#getTerm()
	 */
	public Term getTerm()
	{
		return synonym.getTerm();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.association.Synonym#isLocalAddition()
	 */
	@Override
	public boolean isLocalAddition()
	{
		return synonym.isLocalAddition();
	}

	/**
	 * @return
	 * @see com.apelon.dts.client.association.Synonym#isPreferred()
	 */
	@Override
	public boolean isPreferred()
	{
		return synonym.isPreferred();
	}

	// /**
	// * @param arg0
	// * @see
	// com.apelon.dts.client.association.Synonym#setConcept(com.apelon.dts.client.concept.DTSConcept)
	// */
	// public void setConcept(DTSConcept arg0)
	// {
	// synonym.setConcept(arg0);
	// }

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.association.Synonym#setLocalAddition(boolean)
	 */
	@Override
	public void setLocalAddition(boolean arg0)
	{
		synonym.setLocalAddition(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.association.Synonym#setPreferred(boolean)
	 */
	@Override
	public void setPreferred(boolean arg0)
	{
		synonym.setPreferred(arg0);
	}

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.association.Association#setAssociationType(com.apelon.dts.client.association.AssociationType)
	 */
	public void setAssociationType(AssociationType arg0)
	{
		synonym.setAssociationType(arg0);
	}

	// ========================= PRIVATE METHODS ===========================

}
