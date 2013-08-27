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

import static edu.utah.further.osgi.shell.ShellUtil.accessToSystemBundleIsAllowed;
import static edu.utah.further.osgi.shell.ShellUtil.isASystemBundle;
import static edu.utah.further.osgi.shell.ShellUtil.println;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Option;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.Bundle;

abstract class BundleCommand extends OsgiCommandSupport
{
	// ========================= COMMAND OPTIONS ===========================

	@Argument(index = 0, name = "id", description = "The bundle ID", required = true, multiValued = false)
	long id;

	@Option(name = "--force", aliases = {}, description = "Forces the command to execute", required = false, multiValued = false)
	boolean force;

	// ========================= PRIVATE METHODS ===========================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 * @throws Exception
	 * @see org.apache.felix.karaf.shell.console.OsgiCommandSupport#doExecute()
	 */
	protected final void doExecute(final BundleExecutor executor) throws Exception
	{
		final Bundle bundle = getBundleContext().getBundle(id);
		if (bundle == null)
		{
			println("Bundle " + id + " not found");
		}
		else if (force || !isASystemBundle(getBundleContext(), bundle)
				|| accessToSystemBundleIsAllowed(bundle.getBundleId(), session))
		{
			executor.doExecute(bundle);
		}
	}
}
