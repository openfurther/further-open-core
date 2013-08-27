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
package edu.utah.further.ds.api.service.query.logic;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;

/**
 * A Schematron compiler service for compiling a Schematron validation file using the
 * multi-step process.
 * 
 * The short of it:
 * 
 * Step 1: Expand Inclusions <br/>
 * Step 2: Expand Abstract Patterns<br/>
 * Step 3: Compile to XSLT<br/>
 * 
 * From the documentation:
 * 
 * 1) First, preprocess your Schematron schema with iso_dsdl_include.xsl. This is a macro
 * processor to assemble the schema from various parts. If your schema is not in separate
 * parts, you can skip this stage.
 * 
 * 2) Second, preprocess the output from stage 1 with iso_abstract_expand.xsl. This is a
 * macro processor to convert abstract patterns to real patterns. If your schema does not
 * use abstract patterns, you can skip this stage.
 * 
 * 3) Third, compile the Schematron schema into an XSLT script. This will typically use
 * iso_svrl_for_xslt1.xsl or iso_svrl_for_xslt2.xsl (which in turn invoke
 * iso_schematron_skeleton_for_xslt1.xsl or iso_schematron_skeleton_for_saxon.xsl)
 * However, other "meta-styleseets" are also in common use; the principle of operation is
 * the same. If your schema uses Schematron phases, supply these as command
 * line/invocation parameters to this process.
 * 
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Dec 10, 2009
 */
public interface SchematronCompilerService
{
	// ========================= METHODS ===================================

	/**
	 * Compiles a Schematron file.
	 * 
	 * @param filename
	 *            the filename and path of the Schematron file
	 * @return the compiled Schematron file or null if an error occurred
	 */
	byte[] compile(String filename);

	/**
	 * Compiles a Schematron file.
	 * 
	 * @param file
	 *            the Schematron file
	 * @return the compiled Schematron file or null if an error occurred
	 */
	byte[] compile(File file);

	/**
	 * Compiles a Schematron file
	 * 
	 * @param inputStream
	 *            an inputstream representing the Schematron file
	 * @return the compiled Schematron file or null if an error occurred
	 */
	byte[] compile(InputStream inputStream);

	/**
	 * Compiles a Schematron file
	 * 
	 * @param reader
	 *            a reader representing the Schematron file
	 * @return the compiled Schematron file or null if an error occurred
	 */
	byte[] compile(Reader reader);

	/**
	 * Compiles a Schematron file
	 * 
	 * @param source
	 *            a source representing the Schematron file
	 * @return the compiled Schematron file or null if an error occurred
	 */
	byte[] compile(Source source);

	/**
	 * @param templatesInclude
	 *            the templatesInclude to set
	 */
	void setTemplatesInclude(Templates templatesInclude);

	/**
	 * @param templatesExpand
	 *            the templatesExpand to set
	 */
	void setTemplatesExpand(Templates templatesExpand);

	/**
	 * @param templatesCompile
	 *            the templatesCompile to set
	 */
	void setTemplatesCompile(Templates templatesCompile);
}