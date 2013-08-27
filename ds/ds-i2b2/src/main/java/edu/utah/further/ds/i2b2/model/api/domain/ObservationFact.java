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
package edu.utah.further.ds.i2b2.model.api.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import edu.utah.further.core.api.lang.PubliclyCloneable;

/**
 * Observation Fact
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Rick L. Bradshaw <rick.bradshaw@utah.edu>}
 * @version April 15, 2010
 */
public interface ObservationFact extends PubliclyCloneable<ObservationFact>
{

	ObservationFactId getId();

	void setId(ObservationFactId observationFactPK);

	BigInteger getPatientNum();

	void setPatientNum(BigInteger patientNum);

	String getValtypeCd();

	void setValtypeCd(String valtypeCd);

	String getTvalChar();

	void setTvalChar(String tvalChar);

	BigDecimal getNvalNum();

	void setNvalNum(BigDecimal nvalNum);

	String getValueflagCd();

	void setValueflagCd(String valueflagCd);

	BigDecimal getQuantityNum();

	void setQuantityNum(BigDecimal quantityNum);

	String getUnitsCd();

	void setUnitsCd(String unitsCd);

	Date getEndDate();

	void setEndDate(Date endDate);

	String getLocationCd();

	void setLocationCd(String locationCd);

	BigDecimal getConfidenceNum();

	void setConfidenceNum(BigDecimal confidenceNum);

	String getObservationBlob();

	void setObservationBlob(String observationBlob);

	Date getUpdateDate();

	void setUpdateDate(Date updateDate);

	Date getDownloadDate();

	void setDownloadDate(Date downloadDate);

	Date getImportDate();

	void setImportDate(Date importDate);

	String getSourcesystemCd();

	void setSourcesystemCd(String sourcesystemCd);

	BigInteger getUploadId();

	void setUploadId(BigInteger uploadId);

}