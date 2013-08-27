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

import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;
import static edu.utah.further.core.api.constant.Strings.SCOPE_CLOSE;
import static edu.utah.further.core.api.constant.Strings.SCOPE_OPEN;
import static edu.utah.further.core.api.text.StringUtil.newStringBuilder;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;

import org.slf4j.Logger;

import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.context.Implementation;
import edu.utah.further.core.api.tree.PrintableComposite;
import edu.utah.further.core.api.tree.Printer;

/**
 * Print a tree in pre-traversal ordering. Not coupled to the persistent layer.
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
public class TraversalPrinter<T extends PrintableComposite<T>> implements Printer<T>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(TraversalPrinter.class);

	/**
	 * Amount of space to add upon each depth increment.
	 */
	private static final int INDENT_PER_DEPTH = 3;

	// ========================= FIELDS ====================================

	/**
	 * The tree to be printed.
	 */
	private final T tree;

	// The following fields are used to construct a formatted tree string.
	// Using StringBuilders instead of String for faster concatenation.
	// When we are done, we use the output.toString() method to output
	// the tree as a String.
	private String output = Strings.EMPTY_STRING;

	private StringBuilder openingBracket = new StringBuilder(SCOPE_OPEN);

	private StringBuilder closingBracket = new StringBuilder(SCOPE_CLOSE);

	private StringBuilder space = new StringBuilder(Strings.SPACE_STRING);

	private StringBuilder lineFeed = new StringBuilder(NEW_LINE_STRING);

	private boolean prePrint = true;

	private boolean postPrint = false;

	private boolean printBrackets = false;

	// Convenient local variables
	private int depth;

	private int indent;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a tree printer with default formatting values.
	 *
	 * @param tree
	 *            the tree to be printed
	 */
	public TraversalPrinter(final T tree)
	{
		this.tree = tree;
	}

	/**
	 * Construct a tree printer.
	 *
	 * @param tree
	 *            the tree to be printed
	 * @param openingBracket
	 *            print this bracket before entering a child node depth in the tree
	 * @param closingBracket
	 *            print this bracket before going back to a parent node depth in the tree
	 * @param space
	 *            characters to fill indented space before each node
	 * @param lineFeed
	 *            line feed characters to insert after each node
	 * @param prePrint
	 *            if true, prints nodes in pre-traversal order
	 * @param postPrint
	 *            if true, prints nodes in post-traversal order
	 * @param printBrackets
	 *            if true, prints opening- and closing- node indentation bracket symbols
	 */
	public TraversalPrinter(final T tree, final String openingBracket,
			final String closingBracket, final String space, final String lineFeed,
			final boolean prePrint, final boolean postPrint, final boolean printBrackets)
	{
		this.tree = tree;
		this.openingBracket = new StringBuilder(openingBracket);
		this.closingBracket = new StringBuilder(closingBracket);
		this.space = new StringBuilder(space);
		this.lineFeed = new StringBuilder(lineFeed);
		this.prePrint = prePrint;
		this.postPrint = postPrint;
		this.printBrackets = printBrackets;
	}

	/**
	 * Copy constructor. Copies all printer fields except the tree reference.
	 *
	 * @param newTree
	 *            tree reference of the constructed object
	 * @param other
	 *            copy all other printer fields from this object
	 */
	public TraversalPrinter(final T newTree, final TraversalPrinter<T> other)
	{
		this.tree = newTree;

		this.openingBracket = other.openingBracket;
		this.closingBracket = other.closingBracket;
		this.space = other.space;
		this.lineFeed = other.lineFeed;
		this.prePrint = other.prePrint;
		this.postPrint = other.postPrint;
		this.printBrackets = other.printBrackets;
	}

	// ========================= IMPLEMENTATION: TreeVisitor ===============

	/**
	 * Print a tree in pre-traversal order.
	 *
	 * @param thisNode
	 *            the root node of the tree to be printed.
	 * @return string representation of the tree under the specified root node
	 */
	@Override
	public String visit(final T thisNode)
	{
		final StringBuilder s = newStringBuilder();
		final StringBuilder indentStr = newStringBuilder();
		for (int i = 0; i < indent; i++)
		{
			indentStr.append(space);
		}

		// -----------------------------------
		// Pre-traversal node printout
		// -----------------------------------
		if (prePrint)
		{
			s.append(indentStr);
			// Call back to node data printout function
			s.append(thisNode.printData());
			s.append(lineFeed);
		}

		// -----------------------------------
		// Indent
		// -----------------------------------
		if (printBrackets)
		{
			s.append(indentStr);
			s.append(openingBracket);
			s.append(lineFeed);
		}
		depth++;
		indent += INDENT_PER_DEPTH;

		// -----------------------------------
		// Process child nodes
		// -----------------------------------
		final Collection<T> children = thisNode.getChildren();
		if (children != null)
		{
			for (final T child : children)
			{
				s.append(visit(child));
			}
		}
		else
		{
			// Children have not yet been loaded, don't process them
			s.append(indentStr);
			s.append(" [children not initialized]\n");
		}

		// -----------------------------------
		// Unindent
		// -----------------------------------
		depth--;
		indent -= INDENT_PER_DEPTH;
		if (printBrackets)
		{
			s.append(indentStr);
			s.append(closingBracket);
			s.append(lineFeed);
		}

		// -----------------------------------
		// Post-traversal node printout
		// -----------------------------------
		if (postPrint)
		{
			s.append(indentStr);
			// Call back to node data printout function
			s.append(thisNode.printData());
			s.append(lineFeed);
		}

		return s.toString();
	}

	// ========================= METHODS ===================================

	/**
	 * @return Output the tree in pre-traversal order as a string.
	 */
	@Override
	public String toString()
	{
		depth = 0;
		indent = 0;
		// log.debug("Printing tree " + "@" +
		// Integer.toHexString(tree.hashCode()));
		// log.debug("root node children list " + tree.getChildren());
		output = visit(tree);
		// log.debug("output " + output);
		return output.toString();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the openingBracket property.
	 *
	 * @return the openingBracket
	 */
	public StringBuilder getOpeningBracket()
	{
		return openingBracket;
	}

	/**
	 * Set a new value for the openingBracket property.
	 *
	 * @param openingBracket
	 *            the openingBracket to set
	 */
	public void setOpeningBracket(final StringBuilder openingBracket)
	{
		this.openingBracket = openingBracket;
	}

	/**
	 * Return the closingBracket property.
	 *
	 * @return the closingBracket
	 */
	public StringBuilder getClosingBracket()
	{
		return closingBracket;
	}

	/**
	 * Set a new value for the closingBracket property.
	 *
	 * @param closingBracket
	 *            the closingBracket to set
	 */
	public void setClosingBracket(final StringBuilder closingBracket)
	{
		this.closingBracket = closingBracket;
	}

	/**
	 * Return the space property.
	 *
	 * @return the space
	 */
	public StringBuilder getSpace()
	{
		return space;
	}

	/**
	 * Set a new value for the space property.
	 *
	 * @param space
	 *            the space to set
	 */
	public void setSpace(final StringBuilder space)
	{
		this.space = space;
	}

	/**
	 * Return the lineFeed property.
	 *
	 * @return the lineFeed
	 */
	public StringBuilder getLineFeed()
	{
		return lineFeed;
	}

	/**
	 * Set a new value for the lineFeed property.
	 *
	 * @param lineFeed
	 *            the lineFeed to set
	 */
	public void setLineFeed(final String lineFeed)
	{
		this.lineFeed = new StringBuilder(lineFeed);
	}

	/**
	 * Return the prePrint property.
	 *
	 * @return the prePrint
	 */
	public boolean isPrePrint()
	{
		return prePrint;
	}

	/**
	 * Set a new value for the prePrint property.
	 *
	 * @param prePrint
	 *            the prePrint to set
	 */
	public void setPrePrint(final boolean prePrint)
	{
		this.prePrint = prePrint;
	}

	/**
	 * Return the postPrint property.
	 *
	 * @return the postPrint
	 */
	public boolean isPostPrint()
	{
		return postPrint;
	}

	/**
	 * Set a new value for the postPrint property.
	 *
	 * @param postPrint
	 *            the postPrint to set
	 */
	public void setPostPrint(final boolean postPrint)
	{
		this.postPrint = postPrint;
	}

	/**
	 * Return the printBrackets property.
	 *
	 * @return the printBrackets
	 */
	public boolean isPrintBrackets()
	{
		return printBrackets;
	}

	/**
	 * Set a new value for the printBrackets property.
	 *
	 * @param printBrackets
	 *            the printBrackets to set
	 */
	public void setPrintBrackets(final boolean printBrackets)
	{
		this.printBrackets = printBrackets;
	}

	/**
	 * Return the indent property.
	 *
	 * @return the indent
	 */
	public int getIndent()
	{
		return indent;
	}

	/**
	 * Set a new value for the indent property.
	 *
	 * @param indent
	 *            the indent to set
	 */
	public void setIndent(final int indent)
	{
		this.indent = indent;
	}

}
