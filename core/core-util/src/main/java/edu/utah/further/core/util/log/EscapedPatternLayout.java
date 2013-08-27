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
package edu.utah.further.core.util.log;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * A {@link PatternLayout} decorator that escapes quotes and other special characters that
 * cannot appear in a SQL INSERT VALUES parameter value. Registers our
 * {@link EscapedPatternParser}.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version May 10, 2011
 */
public final class EscapedPatternLayout extends PatternLayout
{
	// ========================= IMPL: PatternLayout =======================

	/**
	 * @param pattern
	 * @return
	 * @see org.apache.log4j.PatternLayout#createPatternParser(java.lang.String)
	 */
	@Override
	protected PatternParser createPatternParser(final String pattern)
	{
		return new EscapedPatternParser(pattern);
	}

	/**
	 * A Log4J {@link PatternParser} decorator that registers our
	 * {@link EscapedMessagePatternConverter} within the {@link PatternParser} converter
	 * chain.
	 */
	private static class EscapedPatternParser extends PatternParser
	{
		/**
		 * @param pattern
		 */
		private EscapedPatternParser(final String pattern)
		{
			super(pattern);
		}

		/**
		 * @param c
		 * @see org.apache.log4j.helpers.PatternParser#finalizeConverter(char)
		 */
		@Override
		protected void finalizeConverter(final char c)
		{
			switch (c)
			{
				case 'm':
				{
					final PatternConverter pc = new EscapedMessagePatternConverter(
							formattingInfo);
					// LogLog.debug("MESSAGE converter.");
					// formattingInfo.dump();
					currentLiteral.setLength(0);
					addConverter(pc);
					break;
				}

				default:
				{
					super.finalizeConverter(c);
					break;
				}
			}
		}

	}

	/**
	 * A Log4J {@link PatternConverter} decorator that escapes special characters in a
	 * logging event message.
	 */
	private static class EscapedMessagePatternConverter extends PatternConverter
	{
		EscapedMessagePatternConverter(final FormattingInfo formattingInfo)
		{
			super(formattingInfo);
		}

		@Override
		public String convert(final LoggingEvent event)
		{
			// Raw message
			String message = event.getRenderedMessage();
			// Escape quotes
			message = message.replaceAll("'", "''");
			return message;
		}
	}
}
