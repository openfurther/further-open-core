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

import static edu.utah.further.core.api.constant.Strings.NEW_LINE_STRING;

import java.util.List;

import edu.utah.further.core.api.chain.RequestHandler;
import edu.utah.further.core.api.constant.Strings;
import edu.utah.further.core.api.lang.ReflectionUtil;
import edu.utah.further.core.api.text.StringUtil;

/**
 * A request handler recursive printer. Uses the visitor pattern.
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
 * @version May 27, 2010
 */
final class RequestHandlerPrinter implements RequestHandlerVisitor
{
	// ========================= FIELDS ====================================

	/**
	 * Textual representation buffer built during <code>visit()</code> calls.
	 */
	private final StringBuilder builder = StringUtil.newStringBuilder();

	/**
	 * Keeps track of current depth in the request handler tree.
	 */
	private int depth = 0;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Hide constructor - use the {@link #print(RequestHandler)} factory method instead.
	 * 
	 * @param depth
	 */
	private RequestHandlerPrinter(final int depth)
	{
		super();
		this.depth = depth;
	}

	/**
	 * @param handler
	 */
	public static String print(final AbstractRequestHandler handler, final int depth)
	{
		final RequestHandlerPrinter printer = new RequestHandlerPrinter(depth);
		handler.accept(printer);
		return printer.builder.toString();
	}

	/**
	 * @param handler
	 */
	public static String print(final AbstractRequestHandler handler)
	{
		return print(handler, 0);
	}

	// ========================= METHODS ===================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.chain.RequestHandlerVisitor#visit(edu.utah.further.core
	 * .api.chain.RequestHandler)
	 */
	@Override
	public void visit(final AbstractRequestHandler visitable)
	{
		builder/* .append(getIndentString(depth)) */.append(visitable.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.chain.RequestHandlerVisitor#visit(edu.utah.further.core
	 * .util.chain.RequestHandlerChainImpl)
	 */
	@Override
	public void visit(final RequestHandlerChainImpl visitable)
	{
		// Recursive call
		builder/* .append(getIndentString(depth)) */.append("Chain:").append(
				NEW_LINE_STRING);
		depth++;
		final List<RequestHandler> handlerList = visitable.handlerList;
		final int last = handlerList.size() - 1;
		for (int i = 0; i <= last; i++)
		{
			builder.append(getIndentString(depth)).append(
					String.format("#%2d ", new Integer(i)));
			final RequestHandler child = handlerList.get(i);
			if (ReflectionUtil.instanceOf(child, AbstractRequestHandler.class))
			{
				((AbstractRequestHandler) child).accept(this);
			}
			else
			{
				builder.append(child.toString());
			}
			if (i < last)
			{
				builder.append(NEW_LINE_STRING);
			}
		}
		depth--;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.utah.further.core.chain.RequestHandlerVisitor#visit(edu.utah.further.core
	 * .util.chain.RequestHandlerSimpleImpl)
	 */
	@Override
	public void visit(final RequestHandlerSimpleImpl visitable)
	{
		builder
		/* append(getIndentString(depth)). */.append(visitable.getProcessor().toString(
				depth));
	}

	// ========================= PRIVATE METHODS ===========================

	// /**
	// * Main visiting call at a certain request chain node. Adds common pre- and
	// * post-processing.
	// *
	// * @param visitable
	// * request handler to visit
	// */
	// private void visitMainCall(final RequestHandler visitable)
	// {
	// builder.append(getIndentString(depth));
	// visit(visitable);
	// }

	/**
	 * Return an indentation string for use by {@link #toString(int)}.
	 * 
	 * @param depth
	 *            current depth
	 * @return indentation string to be printed when this object's {@link #toString(int)}
	 *         is called with depth=<code>depth</code>
	 */
	private static String getIndentString(final int depth)
	{
		return (depth == 0) ? Strings.EMPTY_STRING : String.format("%-" + (4 * depth)
				+ "s", Strings.SPACE_STRING);
	}
}
