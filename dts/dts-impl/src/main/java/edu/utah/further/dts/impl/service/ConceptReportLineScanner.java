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
package edu.utah.further.dts.impl.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Scanner;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.utah.further.core.util.text.AbstractLineScanner;
import edu.utah.further.dts.api.domain.concept.ConceptReport;

/**
 * Parses a file lines into a list of tokens separated by a separator into
 * {@link ConceptReport} objects. Blank lines are ignored.
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
 * @version Sep 2, 2010
 */
@Service("conceptReportLineScanner")
public final class ConceptReportLineScanner extends AbstractLineScanner<ConceptReport>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(ConceptReportLineScanner.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * By default, uses a comma delimiter.
	 */
	public ConceptReportLineScanner()
	{
		setDelimiter("\\s*,\\s*");
	}

	// ========================= FIELDS ====================================

	// ========================= IMPL: LineScanner =========================

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.utah.further.core.util.text.LineScanner#parse(java.lang.String)
	 */
	@Override
	public ConceptReport parse(final String line)
	{
		try (final Scanner scanner = new Scanner(line)) 
		{
			scanner.useDelimiter(getDelimiter());

			final ConceptReport report = new ConceptReport(scanner.next());
			if (log.isDebugEnabled())
			{
				log.debug("Parsed report " + report);
			}

			return report;
		}
		
	}

}
