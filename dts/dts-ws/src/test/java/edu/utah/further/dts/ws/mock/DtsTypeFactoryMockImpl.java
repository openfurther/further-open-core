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
package edu.utah.further.dts.ws.mock;

import static edu.utah.further.core.api.message.Messages.unsupportedMessage;

import org.springframework.stereotype.Service;

import edu.utah.further.core.api.context.Mock;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.dts.api.domain.concept.DtsTypeFactory;
import edu.utah.further.dts.api.domain.namespace.DtsDataType;
import edu.utah.further.dts.api.to.DtsConceptToImpl;
import edu.utah.further.dts.api.to.DtsDataTo;
import edu.utah.further.dts.api.to.DtsNamespaceToImpl;

/**
 * DTS Data type object factory mock implementation.
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
@Service("dtsWsTypeFactoryMockImpl")
@Mock
public class DtsTypeFactoryMockImpl implements DtsTypeFactory
{
	// ========================= IMPLEMENTATION: DtsTypeFactory ============

	/**
	 * @param dtsDataType
	 * @param name
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsTypeFactory#newInstance(edu.utah.further.dts.api.domain.namespace.DtsDataType,
	 *      java.lang.String)
	 */
	@Override
	public DtsDataTo newInstance(final DtsDataType dtsDataType, final String name)
	{
		final DtsDataTo to = newInstance(dtsDataType);
		to.setName(name);
		return to;
	}

	// ========================= IMPLEMENTATION: PRIVATE METHODS ===========

	/**
	 * @param dtsDataType
	 * @param name
	 * @return
	 * @see edu.utah.further.dts.api.domain.concept.DtsTypeFactory#newInstance(edu.utah.further.dts.api.domain.namespace.DtsDataType,
	 *      java.lang.String)
	 */
	private DtsDataTo newInstance(final DtsDataType dtsDataType)
	{
		switch (dtsDataType)
		{
			case CONCEPT:
			{
				return new DtsConceptToImpl();
			}

			case NAMESPACE:
			{
				return new DtsNamespaceToImpl();
			}

				// case OBJECT:
				// {
				// return new DtsObjectMockImpl();
				// }
				//
				// case PROPERTIED_OBJECT:
				// {
				// return new DtsPropertiedObjectMockImpl();
				// }

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(dtsDataType));
			}
		}
	}
}
