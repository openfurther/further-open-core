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
package edu.utah.further.fqe.ds.api;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.utah.further.core.api.xml.IdentifiableMapAdapter;
import edu.utah.further.core.api.xml.XmlNamespace;
import edu.utah.further.fqe.ds.api.service.results.ResultType;
import edu.utah.further.fqe.ds.api.to.ResultContextMapEntryToImpl;
import edu.utah.further.fqe.ds.api.to.ResultContextToImpl;

/**
 * A JAXB adapter of the {@link DtsConceptToImpl#properties} map. Properties are sorted by
 * name in the XML view, even though the underlying data structure is a plain {@link Map}.
 * This adapter is responsible for sorting the map for the view.
 * <p>
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
public final class ResultContextMapAdapter
		extends
		IdentifiableMapAdapter<ResultType, ResultContextToImpl, ResultContextMapEntryToImpl, ResultContextMapAdapter.ValueType>
{
	@XmlRootElement(namespace = XmlNamespace.FQE, name = "resultViewValueType")
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "resultViewValueType")
	public static class ValueType
	{
		@XmlElement(name = "entry", required = false, namespace = XmlNamespace.FQE)
		public List<ResultContextMapEntryToImpl> list;
	}
}
