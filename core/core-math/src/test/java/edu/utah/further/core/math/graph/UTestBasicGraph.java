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
import static edu.utah.further.core.math.util.GraphTestUtil.stringGraph;
import static edu.utah.further.core.math.util.GraphTestUtil.undirectedStringGraph;
import static edu.utah.further.core.math.util.GraphTestUtil.weightedDirectedGraph;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * Basic graph manipulation using the JUNG2 library.
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
public final class UTestBasicGraph
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UTestBasicGraph.class);

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= TESTING METHODS ===========================

	/**
	 * Create a simple directed graph.
	 */
	@Test
	@SuppressWarnings("boxing")
	public void createSimpleDirectedGraph()
	{
		final DirectedGraph<Integer, Double> g = new DefaultDirectedGraph<>(
				Double.class);
		g.addVertex(1);
		g.addVertex(2);
		g.addEdge(1, 2, 1.0d);
		assertEquals(2, g.vertexSet().size());
		assertEquals(1, g.edgeSet().size());
		if (log.isDebugEnabled())
		{
			log.debug("Graph " + g);
		}
	}

	/**
	 * Create an unweighted directed graph.
	 */
	@Test
	public void createDirectedGraph()
	{
		final DirectedGraph<Integer, DefaultEdge> g = directedGraph();
		if (log.isDebugEnabled())
		{
			log.debug("Graph " + g);
		}
		assertEquals(8, g.vertexSet().size());
		assertEquals(9, g.edgeSet().size());
	}

	/**
	 * Create a weighted directed graph.
	 */
	@Test
	public void createWeightedDirectedGraph()
	{
		final WeightedGraph<Integer, DefaultWeightedEdge> g = weightedDirectedGraph();
		if (log.isDebugEnabled())
		{
			log.debug("Graph " + g);
		}
		assertEquals(8, g.vertexSet().size());
		assertEquals(1, g.edgeSet().size());
	}

	/**
	 * Create a test directed graph.
	 */
	@Test
	public void createSimpleGraph()
	{
		final Graph<String, DefaultEdge> g = stringGraph();
		if (log.isDebugEnabled())
		{
			log.debug("Graph " + g);
		}
		assertEquals(4, g.vertexSet().size());
		assertEquals(4, g.edgeSet().size());
	}

	/**
	 * Create a test directed graph.
	 */
	@Test
	public void createUndirectedSimpleGraph()
	{
		final UndirectedGraph<String, DefaultEdge> g = undirectedStringGraph();
		if (log.isDebugEnabled())
		{
			log.debug("Graph " + g);
		}
		assertEquals(4, g.vertexSet().size());
		assertEquals(4, g.edgeSet().size());
	}

	// ========================= PRIVATE METHODS ============================
}
