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
package edu.utah.further.mdr.api.util;

import static edu.utah.further.core.api.message.Messages.unsupportedMessage;
import static edu.utah.further.core.api.message.Messages.unsupportedOperationMessage;
import edu.utah.further.core.api.exception.ApplicationException;
import edu.utah.further.core.api.exception.BusinessRuleException;
import edu.utah.further.mdr.api.service.uml.XmiParser;
import edu.utah.further.mdr.api.service.uml.XmiVersion;

/**
 * A convenient factory of singleton beans. Configured by Spring method injection.
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
public abstract class AbstractXmiParserFactory implements XmiParserFactory
{
	// ========================= IMPL: XmiParserFactory ====================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.mdr.api.util.XmiParserFactory#newXmiParser(edu.utah.further.mdr
	 * .api.service.uml.XmiVersion)
	 */
	@Override
	public final XmiParser newXmiParser(final XmiVersion version)
	{
		switch (version)
		{
			case XMI_1_1:
			{
				return newXmiParser11();
			}

			case XMI_2_1:
			{
				return newXmiParser21();
			}

			default:
			{
				throw new BusinessRuleException(unsupportedMessage(version));
			}
		}
	}

	// ========================= PRIVATE METHODS ===========================

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.mdr.api.util.PrivateInterface#newXmiParser11()
	 */
	public XmiParser newXmiParser11()
	{
		throw new ApplicationException(newMissingMethodInjectionMessage("newXmiParser11"));
	}

	/*
	 * (non-Javadoc)
	 * @see edu.utah.further.mdr.api.util.PrivateInterface#newXmiParser21()
	 */
	public XmiParser newXmiParser21()
	{
		throw new ApplicationException(newMissingMethodInjectionMessage("newXmiParser21"));
	}

	/**
	 * @param methodName
	 * @return
	 */
	private String newMissingMethodInjectionMessage(final String methodName)
	{
		return unsupportedOperationMessage(methodName
				+ "(): missing spring method injection");
	}
}
