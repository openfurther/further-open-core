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
package edu.utah.further.core.util.math.random;

final class GpwData
{
	static short tris[][][] = null;
	static long sigma[] = null; // 125729

	GpwData()
	{
		int c1, c2, c3;
		tris = new short[26][26][26];
		sigma = new long[1];
		GpwDataInit1.fill(this); // Break into two classes for NS 4.0
		GpwDataInit2.fill(this); // .. its Java 1.1 barfs on methods > 65K
		for (c1 = 0; c1 < 26; c1++)
		{
			for (c2 = 0; c2 < 26; c2++)
			{
				for (c3 = 0; c3 < 26; c3++)
				{
					sigma[0] += tris[c1][c2][c3];
				} // for c3
			} // for c2
		} // for c1
	} // constructor

	void set(final int x1, final int x2, final int x3, final short v)
	{
		tris[x1][x2][x3] = v;
	} // set()

	long get(final int x1, final int x2, final int x3)
	{
		return tris[x1][x2][x3];
	} // get()

	long getSigma()
	{
		return sigma[0];
	} // get()

} // GpwData
