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
package edu.utah.further.core.data.hibernate.interceptors;

import static org.slf4j.LoggerFactory.getLogger;

import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;

/**
 * Adds an SQL comment hint before the select statement.
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
 * @version May 17, 2012
 */
public class SqlCommentHintInterceptor extends EmptyInterceptor
{

	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(SqlCommentHintInterceptor.class);

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 8074127126135151517L;

	// ========================= FIELDS ====================================

	/**
	 * The hint to apply without the comment markers
	 */
	private String hint;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.EmptyInterceptor#onPrepareStatement(java.lang.String)
	 */
	@Override
	public String onPrepareStatement(final String sql)
	{
		final StringBuffer sb = new StringBuffer(sql);
		final String select = "select";
		final int offset = sb.indexOf(select) + select.length() + 1;
		if (hint == null) {
			log.warn("Hint was empty. No hint will be added to this SQL statement");
		}
		sb.insert(offset, "/*+ " + hint + " */ ");
		final String sqlWithHint = sb.toString();
		
		if (log.isDebugEnabled()) {
			log.debug("Added hint " + hint + " to sql string: " + sqlWithHint);
		}
			
		return sqlWithHint;
	}

	// ========================= GET/SET ===================================

	/**
	 * Return the hint property.
	 * 
	 * @return the hint
	 */
	public String getHint()
	{
		return hint;
	}

	/**
	 * Set a new value for the hint property.
	 * 
	 * @param hint
	 *            the hint to set
	 */
	public void setHint(final String hint)
	{
		this.hint = hint;
	}

}
