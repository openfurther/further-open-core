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
package edu.utah.further.osgi.shell;

import static edu.utah.further.osgi.shell.ShellUtil.println;

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

/**
 * Demonstrates a simple extension of the Apache Karaf OSGi shell.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2010 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @see http://felix.apache.org/site/61-extending-the-console.html
 * @see https://wiki.chpc.utah.edu/display/further/Custom+Fuse+4.2+Shell+Commands
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}</code>
 * @version Mar 8, 2010
 */
@Command(scope = "f", name = "hello", description = "Says hello")
public final class HelloShellCommand extends OsgiCommandSupport
{
	// ========================= IMPL: OsgiCommandSupport ==================

	/**
	 * Execute the command. Prints a welcome message.
	 * 
	 * @return
	 * @throws Exception
	 * @see org.apache.felix.karaf.shell.console.OsgiCommandSupport#doExecute()
	 */
	@Override
	protected Object doExecute() throws Exception
	{
		println("Executing Hello command");
		return null;
	}
}
