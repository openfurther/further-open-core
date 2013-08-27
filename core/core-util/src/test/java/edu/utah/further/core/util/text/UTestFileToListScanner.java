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
package edu.utah.further.core.util.text;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.utah.further.core.util.fixture.CoreUtilFixture;

/**
 * Tests parsing text files into a string list.
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
@RunWith(SpringJUnit4ClassRunner.class)
public final class UTestFileToListScanner extends CoreUtilFixture
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(UTestFileToListScanner.class);

	/**
	 * File name to parse.
	 */
	private static final String FILE_NAME = "simple-file.txt";

	// ========================= FIELDS ====================================

	/**
	 * File parser.
	 */
	@Resource(name = "lineListFileScanner")
	private FileScanner<List<String>> fileScanner;

	// ========================= TESTING METHODS ===========================

	/**
	 * Test parsing a text file to a line list.
	 *
	 * @throws IOException
	 */
	@Test
	public void parseFileToList() throws IOException
	{
		assertThat(fileScanner.parseResource(FILE_NAME),
				is(Arrays.asList("abcde", "1234", "a b c")));
	}

	// ========================= PRIVATE METHODS ============================
}
