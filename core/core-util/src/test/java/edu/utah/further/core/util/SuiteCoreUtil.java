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
package edu.utah.further.core.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import edu.utah.further.core.util.aop.SuiteAop;
import edu.utah.further.core.util.collections.SuiteCollections;
import edu.utah.further.core.util.composite.SuiteComposite;
import edu.utah.further.core.util.converter.SuiteConverter;
import edu.utah.further.core.util.encrypt.SuiteEncrypt;
import edu.utah.further.core.util.generic.SuiteGeneric;
import edu.utah.further.core.util.io.SuiteIo;
import edu.utah.further.core.util.regex.SuiteRegex;
import edu.utah.further.core.util.registry.SuiteRegistry;
import edu.utah.further.core.util.schema.SuiteSchema;
import edu.utah.further.core.util.text.SuiteText;

/**
 * A test suite that includes all tests in the core utility module.
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
 * @version May 29, 2009
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{ SuiteAop.class, SuiteCollections.class, SuiteConverter.class, SuiteComposite.class,
		SuiteGeneric.class, SuiteIo.class, SuiteRegex.class, SuiteRegistry.class,
		SuiteSchema.class, SuiteText.class, SuiteEncrypt.class })
public final class SuiteCoreUtil
{
}