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
package edu.utah.further.core.chain;

import static edu.utah.further.core.chain.ChainNames.APPROVED;
import static edu.utah.further.core.chain.RequestHandlerBuilder.chain;
import static edu.utah.further.core.chain.RequestHandlerBuilder.chainBuilder;
import static org.apache.commons.logging.LogFactory.getLog;
import static org.junit.Assert.assertEquals;

import org.apache.commons.logging.Log;
import org.junit.Test;

import edu.utah.further.core.api.chain.ChainRequest;
import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.chain.RequestProcessor;
import edu.utah.further.core.chain.RequestHandlerBuilder;

/**
 * An example of how the chain of responsibility pattern works.
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
 * @version Sep 28, 2009
 */
public final class UTestChain
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Log log = getLog(UTestChain.class);

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= TESTING METHODS ===========================

	/**
	 * Test the chain pattern through the purchasing order example.
	 */
	@Test
	public void purchaseChain()
	{
		final RequestProcessor manager = new Manager("Manager", 1000);
		final RequestProcessor director = new Manager("Director", 2000);
		final RequestProcessor vp = new Manager("Vice President", 4000);
		final RequestProcessor president = new Manager("President", 8000);

		// PurchasePower noApproval = new ApprovalDenied();
		// RequestHandler[] handlers = { manager, director, vp, president,
		// noApproval };
		final RequestHandler chain = chain(manager, director, vp, president);

		final double[] amounts =
		{ 500, 1000, 2000, 4000, 7999, 8000 };
		final String[] expectedApprover =
		{ "Manager", "Director", "Vice President", "President", "President", null };

		// Loop over increasingly larger amounts and see that the approval
		// from the chain meets our expectation in all cases
		for (int i = 0; i < amounts.length; i++)
		{
			double amount = amounts[i];
			final ChainRequest request = new PurchaseRequest(amount);
			final boolean handled = chain.handle(request);
			final String approver = (String) request.getAttribute(APPROVED);
			if (log.isInfoEnabled())
			{
				log.info("amount " + amount + " approver " + approver
						+ " expectedApproved " + expectedApprover[i] + " handled "
						+ handled);
			}
			assertEquals(expectedApprover[i], approver);
			amount *= 2;
		}
	}

	/**
	 * Test printing a chain as a tree.
	 */
	@Test
	public void printChain()
	{
		// Sub-sub-chain
		final RequestProcessor manager3 = new Manager("Manager 2", 1200);
		final RequestProcessor director3 = new Manager("Director 2", 2200);
		final RequestHandler chain3 = chain(manager3, director3);

		// Sub-chain
		final RequestProcessor manager2 = new Manager("Manager 2", 1500);
		final RequestProcessor director2 = new Manager("Director 2", 2500);
		final RequestHandler chain2 = chainBuilder()
				.addProcessor(manager2)
				.addProcessor(director2)
				.addHandler(chain3)
				.build();

		// Main chain
		final RequestProcessor manager = new Manager("Manager", 1000);
		final RequestProcessor director = new Manager("Director", 2000);
		final RequestProcessor vp = new Manager("Vice President", 4000);
		final RequestProcessor president = new Manager("President", 8000);

		final RequestHandler chain = chainBuilder()
				.addProcessor(manager)
				.addProcessor(director)
				.addHandler(chain2)
				.addProcessor(vp)
				.addProcessor(president)
				.build();

		// Print the main chain
		if (log.isInfoEnabled())
		{
			log.info(RequestHandlerBuilder.simple(manager).toString());
			log.info(chain.toString());
		}
	}
}
