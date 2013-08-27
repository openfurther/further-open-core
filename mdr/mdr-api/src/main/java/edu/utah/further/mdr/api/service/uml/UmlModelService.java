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
package edu.utah.further.mdr.api.service.uml;

import java.util.List;

import edu.utah.further.core.api.context.Api;
import edu.utah.further.mdr.api.domain.uml.ModelInfo;
import edu.utah.further.mdr.api.domain.uml.ModelMetaData;
import edu.utah.further.mdr.api.domain.uml.UmlElement;

/**
 * Manages UML model loading.
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
 * @version Nov 6, 2008
 */
@Api
public interface UmlModelService
{
	// ========================= CONSTANTS =================================

	// ========================= METHODS ===================================

	/**
	 * Re-load models from files/resources. This method is synchronized.
	 */
	void loadAllModels();

	/**
	 * Return a saved UML model by name.
	 *
	 * @param modelName
	 *            model name
	 * @return UML model information bean. If not found, returns <code>null</code>
	 */
	ModelInfo getModel(String modelName);

	/**
	 * Return the list of saved model meta data objects.
	 *
	 * @return model meta data object list
	 */
	List<ModelMetaData> getModelMetaDataList();

	/**
	 * Find an element in all saved models by ID.
	 *
	 * @param elementXMIId
	 *            XMI element ID to look for
	 * @return corresponding element; if not found, returns <code>null</code>
	 */
	UmlElement findElementById(String elementXMIId);

	/**
	 * Load a model. Default XMI parsing options are used.
	 *
	 * @param modelMetaData
	 *            model meta data
	 * @return model loading information bean
	 */
	ModelInfo loadModel(ModelMetaData modelMetaData);

	/**
	 * Load a UML model.
	 *
	 * @param modelMetaData
	 *            model meta data
	 * @param options
	 *            options to pass to the XMI parser
	 * @return model loading information bean
	 */
	ModelInfo loadModel(ModelMetaData modelMetaData, XmiParserOptions options);

	/**
	 * Load a model and save it in the UML model service lookup map.
	 *
	 * @param modelMetaData
	 *            model meta data
	 * @return model loading information bean
	 */
	ModelInfo loadAndSaveModel(ModelMetaData modelMetaData);

	/**
	 * Load a model and save it in the UML model service lookup map. Default XMI parsing
	 * options are used.
	 *
	 * @param modelMetaData
	 *            model meta data options to pass to the XMI parser
	 * @return model loading information bean
	 * @return model loading information bean
	 */
	ModelInfo loadAndSaveModel(ModelMetaData modelMetaData, XmiParserOptions options);
}
