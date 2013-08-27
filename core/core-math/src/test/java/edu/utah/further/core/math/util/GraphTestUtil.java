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
package edu.utah.further.core.math.util;

import java.util.Random;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.context.Utility;

/**
 * Generates some test graphs.
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
 * @version Dec 16, 2010
 */
@Utility
public final class GraphTestUtil
{
	// ========================= CONSTANTS ==================================

	/**
	 * Generates random data.
	 */
	private static final Random RANDOM_GENERATOR = new Random();

	// ========================= METHODS ====================================

	/**
	 * @return
	 */
	public static Graph<String, DefaultEdge> stringGraph()
	{
		final Graph<String, DefaultEdge> g = new ListenableDirectedGraph<>(
				DefaultEdge.class);
		g.addVertex("v1");
		g.addVertex("v2");
		g.addVertex("v3");
		g.addVertex("v4");

		g.addEdge("v1", "v2");
		g.addEdge("v2", "v3");
		g.addEdge("v3", "v1");
		g.addEdge("v4", "v3");

		return g;
	}

	/**
	 * Craete a toy graph based on String objects.
	 * 
	 * @return a graph based on String objects.
	 */
	public static UndirectedGraph<String, DefaultEdge> undirectedStringGraph()
	{
		final UndirectedGraph<String, DefaultEdge> g = new SimpleGraph<>(
				DefaultEdge.class);

		final String v1 = "v1";
		final String v2 = "v2";
		final String v3 = "v3";
		final String v4 = "v4";

		// add the vertices
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);

		// add edges to create a circuit
		g.addEdge(v1, v2);
		g.addEdge(v2, v3);
		g.addEdge(v3, v4);
		g.addEdge(v4, v1);

		return g;
	}

	/**
	 * Create a simple directed graph.
	 * 
	 * @see http://en.wikipedia.org/wiki/Topological_sorting
	 */
	@SuppressWarnings("boxing")
	public static DirectedGraph<Integer, DefaultEdge> directedGraph()
	{
		final DirectedGraph<Integer, DefaultEdge> g = new SimpleDirectedGraph<>(
				DefaultEdge.class);

		final Set<Integer> vertices = CollectionUtil.toSet(7, 5, 3, 11, 8, 2, 9, 10);
		for (final Integer v : vertices)
		{
			g.addVertex(v);
		}

		g.addEdge(7, 8);
		g.addEdge(7, 11);
		g.addEdge(5, 11);
		g.addEdge(3, 8);
		g.addEdge(3, 10);
		g.addEdge(11, 2);
		g.addEdge(11, 9);
		g.addEdge(11, 10);
		g.addEdge(8, 9);

		return g;
	}

	/**
	 * Create a simple directed graph.
	 */
	@SuppressWarnings("boxing")
	public static WeightedGraph<Integer, DefaultWeightedEdge> weightedDirectedGraph()
	{
		final WeightedGraph<Integer, DefaultWeightedEdge> g = new SimpleWeightedGraph<>(
				DefaultWeightedEdge.class);

		final Set<Integer> vertices = CollectionUtil.toSet(7, 5, 3, 11, 8, 2, 9, 10);
		for (final Integer v : vertices)
		{
			g.addVertex(v);
		}

		final DefaultWeightedEdge e = g.addEdge(7, 8);
		g.setEdgeWeight(e, 1.0d);

		return g;
	}

	/**
	 * @param runningJobs
	 * @param v
	 */
	public static <E> E randomElement(final Set<E> set)
	{
		final int size = set.size();
		final int index = RANDOM_GENERATOR.nextInt(size);
		int i = 0;
		for (final E element : set)
		{
			if (i == index)
			{
				return element;
			}
			i++;
		}
		throw new IllegalStateException("Random index outside set size range: index "
				+ index + " size " + size);
	}
}
