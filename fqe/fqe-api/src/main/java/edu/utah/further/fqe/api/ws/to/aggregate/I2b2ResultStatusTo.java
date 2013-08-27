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
package edu.utah.further.fqe.api.ws.to.aggregate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import edu.utah.further.core.api.xml.XmlNamespace;

/**
 * A JAXB acknowledgment object that must be present in every fake i2b2 response, because
 * its XML is expected by the i2b2 web client:
 * <p>
 *
 * <pre>
 * 	<result_status>
 * 		<status type="DONE">DONE</status>
 * 		<polling_url interval_ms="100" />
 * 	</result_status>
 * </pre>
 *
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Mar 30, 2011
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = XmlNamespace.FQE, name = I2b2ResultStatusTo.ENTITY_NAME)
@XmlType(name = "", propOrder =
{ "status", "pollingUrl" })
public final class I2b2ResultStatusTo
{
	// ========================= FIELDS ====================================

	static final String ENTITY_NAME = "result_status";

	@XmlElementRef(namespace = XmlNamespace.FQE)
	private Status status = new Status();

	@XmlElementRef(namespace = XmlNamespace.FQE)
	private PollingUrl pollingUrl = new PollingUrl();

	// ========================= NESTED TYPES ==============================

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(namespace = XmlNamespace.FQE, name = Status.STATUS_ENTITY_NAME)
	@XmlType(name = "", propOrder =
	{ "type", "text" })
	public static class Status
	{
		static final String STATUS_ENTITY_NAME = "status";

		@XmlAttribute(name = "type")
		private String type = "DONE";

		@XmlValue
		private String text = "DONE";
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(namespace = XmlNamespace.FQE, name = PollingUrl.POLLING_URL_ENTITY_NAME)
	@XmlType(name = "", propOrder =
	{ "intervalMs" })
	public static class PollingUrl
	{
		static final String POLLING_URL_ENTITY_NAME = "polling_url";

		@XmlAttribute(name = "interval_ms")
		private long intervalMs = 100l;
	}
}