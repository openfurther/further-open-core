/*******************************************************************************
 * Source File: TransferList.java
 ******************************************************************************/
package edu.utah.further.core.api.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents a transfer object which is a list of other transfer objects and provides a
 * common interface for retrieving that list of transfer objects.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version May 26, 2010
 */
public interface TransferList<T>
{
	// ========================= METHODS ===================================

	/**
	 * Gets the list of transfer objects from this transfer object
	 * 
	 * @return a list of transfer objects
	 */
	@XmlTransient
	List<T> getList();
}
