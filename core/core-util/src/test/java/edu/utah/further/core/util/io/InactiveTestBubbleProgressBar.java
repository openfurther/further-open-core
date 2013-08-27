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
package edu.utah.further.core.util.io;

import org.junit.Test;

/**
 * Methods to display on the console the fact your app is still alive and busy working
 * away. These are classics from the.
 * <p/>
 * days of the Apple][ and DOS. These won't work on eclipse which displays \b as a box
 * instead of backspace.
 * <p>
 * Licence: This software may be copied and used freely for any purpose but military.
 * http://mindprod.com/contact/nonmil.html
 * <p>
 * TODO: convert this to a JUnit test suite sometime.
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author Roedy Green, Canadian Mind Products
 * @version 1.0 2009-01-01 - initial version
 * @since 2009-01-01
 * @version Nov 28, 2009
 */
public final class InactiveTestBubbleProgressBar
{
	// ------------------------------ CONSTANTS ------------------------------

	/**
	 * chars to cycle through in the bubble display
	 */
	private static final char bubbles[] =
	{ '.', 'o', 'O', 'o' };

	/**
	 * chars to cycle through in the spinner display
	 */
	private static final char spins[] =
	{ '-', '\\', '|', '/' };

	/**
	 * where we are in the bubble cycle 0..4
	 */
	private static int bubbleCycle = 0;

	/**
	 * where we are in the spinner cycle 0..4
	 */
	private static int spinCycle = 0;

	// -------------------------- STATIC METHODS --------------------------

	/**
	 * display an animated bubble on the console that kicks over each elapsedTime spinner
	 * is called.
	 */
	private static void bubbler()
	{
		// display a varying char over top of the previous one.
		// If \b does not work, try \r

		System.out.print(bubbles[bubbleCycle] + "\b");
		// use "\r" instead of '\r' to force + to mean concatenation
		bubbleCycle = (bubbleCycle + 1) & 3;
		// cycles 0, 1, 2, 3, 0, 1...
	}

	/**
	 * display an animated spinner on the console that kicks over each elapsedTime spinner
	 * is called.
	 */
	private static void spinner()
	{
		// display a varying char over top of the previous one.
		System.out.print(spins[spinCycle] + "\b");

		spinCycle = (spinCycle + 1) & 3;
		// cycles 0, 1, 2, 3, 0, 1 ...
	}

	// --------------------------- main() method ---------------------------

	/**
	 * Test harness for bubbler and spinner
	 * 
	 * @param args
	 *            not used
	 */
	@Test
	public void bubblerRun()
	// public static void main(final String[] args)
	{
		final int millis = 1;
		for (int i = 0; i < 20; i++)
		{
			try
			{
				// simulate the program doing useful work.
				Thread.sleep(millis);
			}
			catch (final InterruptedException e)
			{
			}
			bubbler();
		}// end for

		for (int i = 0; i < 20; i++)
		{
			try
			{
				Thread.sleep(millis);
			}
			catch (final InterruptedException e)
			{
			}
			spinner();
		}// end for
	}
}
