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
/*******************************************************************************
 * Source File: RandomWordGenerationServiceImpl.java
 ******************************************************************************/
package edu.utah.further.core.util.math.random;

import java.util.Random;

import org.springframework.stereotype.Service;

/**
 * Taken from GPW - Generate pronounceable passwords This program uses statistics on the
 * frequency of three-letter sequences in English to generate passwords. The statistics
 * are generated from your dictionary by the program loadtris.
 * 
 * See www.multicians.org/thvv/gpw.html for history and info. Tom Van Vleck
 * 
 * THVV 06/01/94 Coded THVV 04/14/96 converted to Java THVV 07/30/97 fixed for Netscape
 * 4.0.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @see http://www.multicians.org/thvv/gpw.html
 * @version Mar 23, 2010
 */
@Service("randomWordGenerationService")
public final class RandomWordGenerationServiceImpl implements RandomWordGenerationService
{
	// ========================= CONSTANTS =================================

	/**
	 * Alphabet to construct words from.
	 */
	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

	// ========================= IMPL: RandomWordGenerationService =========

	/**
	 * @param wordLength
	 * @return
	 * @see edu.utah.further.core.util.math.random.RandomWordGenerationService#generate(int)
	 */
	@Override
	public String generate(final int wordLength)
	{
		final GpwData data = new GpwData();
		int c1, c2, c3;
		long sum = 0;
		int nchar;
		long ranno;
		double pik;
		StringBuilder password;
		final Random ran = new Random(); // new random source seeded by clock

		// Pick a random starting point.
		password = new StringBuilder(wordLength);
		pik = ran.nextDouble(); // random number [0,1]
		ranno = (long) (pik * data.getSigma()); // weight by sum of frequencies
		sum = 0;
		for (c1 = 0; c1 < 26; c1++)
		{
			for (c2 = 0; c2 < 26; c2++)
			{
				for (c3 = 0; c3 < 26; c3++)
				{
					sum += data.get(c1, c2, c3);
					if (sum > ranno)
					{
						password.append(alphabet.charAt(c1));
						password.append(alphabet.charAt(c2));
						password.append(alphabet.charAt(c3));
						c1 = 26; // Found start. Break all 3 loops.
						c2 = 26;
						c3 = 26;
					} // if sum
				} // for c3
			} // for c2
		} // for c1

		// Now do a random walk.
		nchar = 3;
		while (nchar < wordLength)
		{
			c1 = alphabet.indexOf(password.charAt(nchar - 2));
			c2 = alphabet.indexOf(password.charAt(nchar - 1));
			sum = 0;
			for (c3 = 0; c3 < 26; c3++)
				sum += data.get(c1, c2, c3);
			if (sum == 0)
			{
				break; // exit while loop
			}
			pik = ran.nextDouble();
			ranno = (long) (pik * sum);
			sum = 0;
			for (c3 = 0; c3 < 26; c3++)
			{
				sum += data.get(c1, c2, c3);
				if (sum > ranno)
				{
					password.append(alphabet.charAt(c3));
					c3 = 26; // break for loop
				} // if sum
			} // for c3
			nchar++;
		} // while nchar
		return password.toString();
	} // generate()

}
