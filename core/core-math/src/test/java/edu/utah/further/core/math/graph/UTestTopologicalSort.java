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
package edu.utah.further.core.math.graph;

import static edu.utah.further.core.math.util.GraphTestUtil.directedGraph;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Iterator;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * Test topologically-sorting graph nodes.
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
 * @version Dec 15, 2010
 */
public final class UTestTopologicalSort
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestTopologicalSort.class);

	// ========================= TESTING METHODS ===========================

	/**
	 * Sort a simple directed graph.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void topologicalSort()
	{
		// Generate a small test graph
		final DirectedGraph<Integer, DefaultEdge> g = directedGraph();
		if (log.isDebugEnabled())
		{
			log.debug("Graph " + g);
		}

		// Topologically sort the graph

		// this is a valid order; it is not unique but it seems that JGraphT generated the
		// smallest-numbered available vertex first, so we can tell in advance what the
		// expected values are.
		final Integer[] expectedOrder =
		{ 3, 5, 7, 8, 11, 2, 9, 10 };
		final Iterator<Integer> iter = new TopologicalOrderIterator<>(
				g);
		int i = 0;
		while (iter.hasNext())
		{
			final Integer next = iter.next();
			if (log.isDebugEnabled())
			{
				log.debug(i + ": " + next);
			}
			assertEquals(expectedOrder[i], next);
			++i;
		}
	}

	/**
	 * Test retrieving edge sets of a vertex.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void edgeSets()
	{
		// Generate a small test graph
		final DirectedGraph<Integer, DefaultEdge> g = directedGraph();
		if (log.isDebugEnabled())
		{
			log.debug("Graph " + g);
		}

		final int vertex = 8;

		final Set<DefaultEdge> outgoingEdges = g.outgoingEdgesOf(vertex);
		final DefaultEdge e = outgoingEdges.iterator().next();
		assertEquals(new Integer(vertex), g.getEdgeSource(e));

		if (log.isDebugEnabled())
		{
			log.debug("All      edges of vertex " + vertex + ": " + g.edgesOf(vertex));
			log.debug("Outgoing edges of vertex " + vertex + ": "
					+ g.outgoingEdgesOf(vertex));
			log.debug("Incoming edges of vertex " + vertex + ": "
					+ g.incomingEdgesOf(vertex));
		}
	}

	// ========================= PRIVATE METHODS ============================
}
