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
package edu.utah.further.i2b2.query.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import edu.utah.further.core.api.collections.CollectionUtil;

/**
 * Transfer object of the further configuration in an i2b2 request.
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
 * @version Dec 16, 2010
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request", namespace = "http://www.i2b2.org/xsd/hive/msg/1.1/")
public class I2b2FurtherConfigTo
{
	@XmlElement(name = "message_body")
	private MessageBody messageBody;

	/**
	 * Return the messageBody property.
	 * 
	 * @return the messageBody
	 */
	public MessageBody getMessageBody()
	{
		return messageBody;
	}

	/**
	 * Set a new value for the messageBody property.
	 * 
	 * @param messageBody
	 *            the messageBody to set
	 */
	public void setMessageBody(final MessageBody messageBody)
	{
		this.messageBody = messageBody;
	}

	/**
	 * Return the data sources property.
	 * 
	 * @return the data sources
	 */
	public List<String> getDatasources()
	{
		final InnerRequest innerRequest = messageBody.getInnerRequest();

		if (innerRequest == null)
		{
			return CollectionUtil.newList();
		}

		final ResultOutputList resultOutputList = innerRequest.getResultOutputList();

		if (resultOutputList == null)
		{
			return CollectionUtil.newList();
		}

		final FurtherConfig furtherConfig = resultOutputList.getFurtherConfig();

		if (furtherConfig == null)
		{
			return CollectionUtil.newList();
		}

		return furtherConfig.getDatasources();
	}

	public Long getAssociatedResult()
	{
		final InnerRequest innerRequest = messageBody.getInnerRequest();

		Long associatedResult = null;
		if (innerRequest != null && innerRequest.getResultOutputList() != null
				&& innerRequest.getResultOutputList().getFurtherConfig() != null)
		{
			associatedResult = innerRequest
					.getResultOutputList()
					.getFurtherConfig()
					.getAssociatedResult();
		}
		return associatedResult;
	}

	public String getQueryType()
	{
		final InnerRequest innerRequest = messageBody.getInnerRequest();

		String queryType = null;
		if (innerRequest != null && innerRequest.getResultOutputList() != null
				&& innerRequest.getResultOutputList().getFurtherConfig() != null)
		{
			queryType = innerRequest
					.getResultOutputList()
					.getFurtherConfig()
					.getQueryType();
		}
		return queryType;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static final class MessageBody
	{
		@XmlElement(name = "request", namespace = "http://www.i2b2.org/xsd/cell/crc/psm/1.1/")
		private InnerRequest innerRequest;

		/**
		 * Return the innerRequest property.
		 * 
		 * @return the innerRequest
		 */
		public InnerRequest getInnerRequest()
		{
			return innerRequest;
		}

		/**
		 * Set a new value for the innerRequest property.
		 * 
		 * @param innerRequest
		 *            the innerRequest to set
		 */
		@SuppressWarnings("unused")
		public void setInnerRequest(final InnerRequest innerRequest)
		{
			this.innerRequest = innerRequest;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static final class InnerRequest
	{
		@XmlElement(name = "result_output_list")
		private ResultOutputList resultOutputList;

		/**
		 * Return the resultOutputList property.
		 * 
		 * @return the resultOutputList
		 */
		public ResultOutputList getResultOutputList()
		{
			return resultOutputList;
		}

		/**
		 * Set a new value for the resultOutputList property.
		 * 
		 * @param resultOutputList
		 *            the resultOutputList to set
		 */
		@SuppressWarnings("unused")
		public void setResultOutputList(final ResultOutputList resultOutputList)
		{
			this.resultOutputList = resultOutputList;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static final class ResultOutputList
	{
		@XmlElement(name = "further-config")
		private FurtherConfig furtherConfig;

		/**
		 * Return the furtherConfig property.
		 * 
		 * @return the furtherConfig
		 */
		public FurtherConfig getFurtherConfig()
		{
			return furtherConfig;
		}

		/**
		 * Set a new value for the furtherConfig property.
		 * 
		 * @param furtherConfig
		 *            the furtherConfig to set
		 */
		@SuppressWarnings("unused")
		public void setFurtherConfig(final FurtherConfig furtherConfig)
		{
			this.furtherConfig = furtherConfig;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static final class FurtherConfig
	{
		@XmlElement(name = "fqe-process")
		private boolean fqeProcess;

		@XmlElementWrapper(name = "datasources")
		@XmlElement(name = "datasource")
		private List<String> datasources = CollectionUtil.newList();

		@XmlElement(name = "queryType")
		private String queryType;

		@XmlElement(name = "associatedResults", required = false)
		private Long associatedResult;

		/**
		 * Return the fqeProcess property.
		 * 
		 * @return the fqeProcess
		 */
		@SuppressWarnings("unused")
		public boolean isFqeProcess()
		{
			return fqeProcess;
		}

		/**
		 * Set a new value for the fqeProcess property.
		 * 
		 * @param fqeProcess
		 *            the fqeProcess to set
		 */
		@SuppressWarnings("unused")
		public void setFqeProcess(final boolean fqeProcess)
		{
			this.fqeProcess = fqeProcess;
		}

		/**
		 * Return the datasources property.
		 * 
		 * @return the datasources
		 */
		public List<String> getDatasources()
		{
			return datasources;
		}

		/**
		 * Set a new value for the datasources property.
		 * 
		 * @param datasources
		 *            the datasources to set
		 */
		@SuppressWarnings("unused")
		public void setDatasources(final List<String> datasources)
		{
			this.datasources = datasources;
		}

		/**
		 * Return the associatedResult property.
		 * 
		 * @return the associatedResult
		 */
		public Long getAssociatedResult()
		{
			return associatedResult;
		}

		/**
		 * Set a new value for the associatedResult property.
		 * 
		 * @param associatedResult
		 *            the associatedResult to set
		 */
		@SuppressWarnings("unused")
		public void setAssociatedResult(final Long associatedResult)
		{
			this.associatedResult = associatedResult;
		}

		/**
		 * Return the queryType property.
		 * 
		 * @return the queryType
		 */
		public String getQueryType()
		{
			return queryType;
		}

		/**
		 * Set a new value for the queryType property.
		 * 
		 * @param queryType
		 *            the queryType to set
		 */
		@SuppressWarnings("unused")
		public void setQueryType(final String queryType)
		{
			this.queryType = queryType;
		}

	}
}
