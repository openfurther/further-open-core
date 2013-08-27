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
package edu.utah.further.dts.api.util;

/**
 * 
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2011 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-213-3288<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Phillip Warner {@code <phillip.warner@utah.edu>}
 * @version Nov 28, 2011
 *
 * @tags
 */
public enum SearchBy
{
	STARTS_WITH {
		@Override
		public String pad(final String searchTerm)
		{
			return searchTerm + WILDCARD;
		}
	},
	CONTAINS {
		@Override
		public String pad(final String searchTerm)
		{
			return WILDCARD + searchTerm + WILDCARD;
		}
	},
	ENDS_WITH {
		@Override
		public String pad(final String searchTerm)
		{
			return WILDCARD + searchTerm;
		}
	};
	private static final String WILDCARD = "*";
	
	public abstract String pad(final String searchTerm);
}
