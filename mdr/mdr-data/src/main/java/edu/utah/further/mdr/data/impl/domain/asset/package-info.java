/*******************************************************************************
 * Source File: package-info.java
 * <p>
 * Declares Hibernate annotations for all model classes in this package.
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Oct 10, 2008
 ******************************************************************************/
@TypeDefs(
{ @TypeDef(name = "xml-type", typeClass = OracleXmlType.class),
                @TypeDef(name = "xml-type-as-string", typeClass = OracleXmlTypeAsString.class),
                @TypeDef(name = "xml-type-length", typeClass = OracleXmlTypeLength.class) })
package edu.utah.further.ds.data.domain;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import edu.utah.further.core.data.oracle.type.OracleXmlType;
import edu.utah.further.core.data.oracle.type.OracleXmlTypeAsString;
import edu.utah.further.core.data.oracle.type.OracleXmlTypeLength;
