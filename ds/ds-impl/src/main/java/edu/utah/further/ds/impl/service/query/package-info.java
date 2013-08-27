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
 * Source File: package-info.java
 * <p>
 * Contains interfaces and default implementations of data query flow processors.
 * <p>
 * Query processors are under the <code>processor</code> package and named
 * <code>OperatorQp</code> for some operation (<code>Qp</code> stands for
 * <b>Q</b>uery <b>P</b>rocessor).
 * <p> 
 * Auxiliary hierarchies of components to which processors delegate local business logic
 * are named <code>Operator*</code> and lie in the <code>logic</code> package.
 * For example, <code>InitializerQp</code> delegates
 * to the <code>Initializer</code> hierarchy.
 * <p>
 * The <code>mock</code> directory contains mock implementations of both processors
 * and their components, using the naming conventions <code>OperatorQpMock</code> and
 * <code>OperatorMock</code>, respectively.
 * -------------------------------------------------------------------------<br>
 * (c) 2008-2013 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Apr 21, 2010
 ******************************************************************************/
package edu.utah.further.ds.impl.service.query;

