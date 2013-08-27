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
package edu.utah.further.core.api.xml.transform;


/**
 * An abstraction of XML DOM serializers.
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
 * @version Aug 14, 2009
 */
abstract class AbstractDocumentSerializer implements DocumentSerializer
{
	// ========================= CONSTANTS =================================

	/**
	 * Default XML namespace.
	 */
	protected static final String XMLNS_XML = "xmlns:xml";

	// ========================= CONSTRUCTORS ==============================

	// ========================= FIELDS ====================================

	/**
	 * Indent lines or not.
	 */
	private boolean indent = false;

	/**
	 * If indentation is turned on, specifies the indentation amount.
	 */
	private int indentAmount = 0;

	/**
	 * If true, printed elements will not contain the default XML namespace attribute
	 * "xlmns:xml".
	 */
	private boolean printElementXmlXlmns = true;

	// ========================= IMPLEMENTATION: DocumentSerializer ========

	/**
	 * Return the indent property.
	 *
	 * @return the indent
	 */
	@Override
	public boolean isIndent()
	{
		return indent;
	}

	/**
	 * Set a new value for the indent property.
	 *
	 * @param indent
	 *            the indent to set
	 */
	@Override
	public void setIndent(final boolean indent)
	{
		this.indent = indent;
	}

	/**
	 * Return the indentAmount property.
	 *
	 * @return the indentAmount
	 */
	@Override
	public int getIndentAmount()
	{
		return indentAmount;
	}

	/**
	 * Set a new value for the indentAmount property.
	 *
	 * @param indentAmount
	 *            the indentAmount to set
	 */
	@Override
	public void setIndentAmount(final int indentAmount)
	{
		this.indentAmount = indentAmount;
	}

	/**
	 * Return the printElementXmlXlmns property.
	 *
	 * @return the printElementXmlXlmns
	 */
	@Override
	public boolean isPrintElementXmlXlmns()
	{
		return printElementXmlXlmns;
	}

	/**
	 * Set a new value for the printElementXmlXlmns property.
	 *
	 * @param printElementXmlXlmns
	 *            the printElementXmlXlmns to set
	 */
	@Override
	public void setPrintElementXmlXlmns(final boolean printElementXmlXlmns)
	{
		this.printElementXmlXlmns = printElementXmlXlmns;
	}

	// ========================= PRIVATE METHODS ===========================
}
