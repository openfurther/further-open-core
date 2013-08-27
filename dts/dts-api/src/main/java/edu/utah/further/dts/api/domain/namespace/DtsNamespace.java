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
package edu.utah.further.dts.api.domain.namespace;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.dts.api.annotation.DtsEntity;

/**
 * An interface extracted from Apelon DTS API's <code>Namespace</code>.
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
@DtsEntity
@Api
public interface DtsNamespace extends DtsComposite, Cloneable
{
	// ========================= METHODS ===================================

	/**
	 * Return the enumerated type-safe namespace type of this object.
	 *
	 * @return the enumerated type-safe namespace type
	 * @see com.apelon.dts.client.namespace.Namespace#getNamespaceType()
	 */
	DtsNamespaceType getNamespaceType();

	// /**
	// * A view method that returns the DTS object associated with this object, if it
	// * exists.
	// *
	// * @return Apelon DTS namespace view
	// */
	// Namespace getAsNamespace();

	// ========================= APELON METHODS ============================

	// /**
	// * @return
	// * @see com.apelon.dts.client.namespace.Namespace#getAuthority()
	// */
	// Authority getAuthority();
	//
	// /**
	// * @return
	// * @see com.apelon.dts.client.namespace.Namespace#getContentVersion()
	// */
	// ContentVersion getContentVersion();

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#getLinkedNamespaceId()
	 */
	int getLinkedNamespaceId();

	//
	// /**
	// * @return
	// * @see com.apelon.dts.client.namespace.Namespace#getNamespaceType()
	// */
	// NamespaceType getNamespaceType();

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#getRelativeId()
	 */
	int getRelativeId();

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#isLocal()
	 */
	boolean isLocal();

	/**
	 * @return
	 * @see com.apelon.dts.client.namespace.Namespace#isWritable()
	 */
	boolean isWritable();

	// /**
	// * @param arg0
	// * @see
	// com.apelon.dts.client.namespace.Namespace#setAuthority(com.apelon.dts.client.namespace.Authority)
	// */
	// void setAuthority(Authority arg0);
	//
	// /**
	// * @param arg0
	// * @see
	// com.apelon.dts.client.namespace.Namespace#setContentVersion(com.apelon.dts.client.namespace.ContentVersion)
	// */
	// void setContentVersion(ContentVersion arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.namespace.Namespace#setRelativeId(int)
	 */
	void setRelativeId(int arg0);

	/**
	 * @param arg0
	 * @see com.apelon.dts.client.namespace.Namespace#setWritable(boolean)
	 */
	void setWritable(boolean arg0);
}