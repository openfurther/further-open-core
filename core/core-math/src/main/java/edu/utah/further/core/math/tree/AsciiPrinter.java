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
package edu.utah.further.core.math.tree;

import static edu.utah.further.core.api.constant.Strings.SCOPE_CLOSE;
import static edu.utah.further.core.api.constant.Strings.SCOPE_OPEN;
import static edu.utah.further.core.api.text.StringUtil.newStringBuilder;

import java.io.Serializable;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Implementation;

/**
 * Print a tree of nodes in a general ASCII format. Each item's tag name is the item type.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, AVP Health Sciences IT Office, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Dec 11, 2008
 */
@Implementation
public class AsciiPrinter<D extends Serializable, T extends AbstractListTree<D, T>>
		extends SimpleCompositeVisitor<T>
{
	// ========================= CONSTANTS =================================

	private final StringBuilder openingBracket = new StringBuilder(SCOPE_OPEN);

	private final StringBuilder closingBracket = new StringBuilder(SCOPE_CLOSE);

	private final StringBuilder space = new StringBuilder(Strings.SPACE_STRING);

	// ========================= FIELDS =====================================

	/**
	 * A function pointer for printing item data.
	 */
	public interface NodeDataPrinter<T>
	{
		String print(T data);
	}

	/**
	 * Prints the tree node data.
	 */
	private final NodeDataPrinter<D> nodeDataPrinter;

	/**
	 * Convenient class-local variable: the output ASCII string.
	 */
	private StringBuilder ASCIIString;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a tree ASCII printer.
	 *
	 * @param rootNode
	 *            root node of tree to be printed
	 * @param nodeDataPrinter
	 *            Specified how a tree node data is printed
	 */
	public AsciiPrinter(final NodeDataPrinter<D> nodeDataPrinter)
	{
		super();
		this.nodeDataPrinter = nodeDataPrinter;
	}

	// ========================= METHODS ===================================

	/**
	 * Generate an ASCII string of the tree
	 *
	 * @param thisNode
	 *            tree root node
	 * @return tree ASCII representation
	 */
	public String print(final T thisNode)
	{
		ASCIIString = newStringBuilder();
		executeOnTree(thisNode);
		return ASCIIString.toString();
	}

	// ========================= IMPLEMENTATION: LimitedDepthTreeVisitor ===

	/**
	 * @see edu.utah.further.core.tree.SimpleTreeVisitor#executePre(edu.utah.further.core.api.tree.AbstractListTree)
	 */
	@Override
	protected Object executePre(final T thisNode)
	{
		// Append node data using call-back from the node data printer
		if (nodeDataPrinter != null)
		{
			ASCIIString.append(nodeDataPrinter.print(thisNode.getData()));
		}

		// Opening bracket before node's children are processed
		ASCIIString.append(space);
		ASCIIString.append(openingBracket);
		ASCIIString.append(space);
		return null;
	}

	/**
	 * @param thisNode
	 * @return
	 * @see edu.utah.further.core.tree.SimpleTreeVisitor#executePost(edu.utah.further.core.tree.MutableTree)
	 */
	@Override
	protected Object executePost(final T thisNode)
	{
		// Opening bracket at the end of node children processing
		ASCIIString.append(closingBracket);
		ASCIIString.append(space);
		return null;
	}

	// ========================= GETTERS & SETTERS =========================

}
