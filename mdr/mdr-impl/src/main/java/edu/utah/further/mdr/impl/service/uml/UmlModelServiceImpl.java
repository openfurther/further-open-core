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
package edu.utah.further.mdr.impl.service.uml;

import static edu.utah.further.core.api.collections.CollectionUtil.newList;
import static edu.utah.further.core.api.text.StringUtil.pluralForm;
import static edu.utah.further.core.api.text.StringUtil.quote;
import static edu.utah.further.mdr.api.service.uml.XmiVersion.XMI_2_1;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import edu.utah.further.core.api.collections.CollectionUtil;
import edu.utah.further.core.api.constant.Constants;
import edu.utah.further.mdr.api.domain.uml.ModelInfo;
import edu.utah.further.mdr.api.domain.uml.ModelMetaData;
import edu.utah.further.mdr.api.domain.uml.UmlElement;
import edu.utah.further.mdr.api.domain.uml.UmlModel;
import edu.utah.further.mdr.api.service.uml.UmlModelService;
import edu.utah.further.mdr.api.service.uml.XmiParser;
import edu.utah.further.mdr.api.service.uml.XmiParserOptions;
import edu.utah.further.mdr.api.util.XmiParserFactory;

/**
 * Manages UML model loading. A simple implementation that uses a map to cache models.
 * Models are currently loaded upon application start-up from a list of hard-coded file
 * names on the classpath.
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
@Service("umlModelService")
@Scope(Constants.Scope.SINGLETON)
public class UmlModelServiceImpl implements UmlModelService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	private static final Logger log = getLogger(UmlModelServiceImpl.class);

	// ========================= FIELDS ====================================

	/**
	 * Holds the loaded UML models. Keys are model meta data's model names.
	 */
	private Map<String, ModelInfo> modelMap;

	// ========================= DEPENDENCIES ==============================

	/**
	 * List of saved models' meta data objects.
	 */
	@Resource(name = "modelMetaDataList")
	private List<ModelMetaData> modelMetaDataList;

	/**
	 * Instantiates XMI parser prototype beans.
	 */
	@Autowired
	private XmiParserFactory xmiParserFactory;

	// ========================= IMPLEMENTATION: UmlModelService ==========

	/**
	 * Re-load models from files/resources. This method is synchronized.
	 *
	 * @see edu.utah.further.dts.api.service.uml.UmlModelService#loadAllModels()
	 */
	@Override
	synchronized public void loadAllModels()
	{
		modelMap = CollectionUtil.newMap();
		// List of saved model resources. Each resource is a a pair (queryFile,xmiFile).
		for (final ModelMetaData modelMetaData : modelMetaDataList)
		{
			loadAndSaveModel(modelMetaData);
		}
		if (log.isInfoEnabled())
		{
			final int numModels = modelMap.size();
			if (log.isInfoEnabled())
			{
				log.info("Loaded " + numModels + " " + pluralForm("model", numModels)
						+ ".");
			}
		}
	}

	/**
	 * Return a saved UML model by name.
	 *
	 * @param modelName
	 *            model name
	 * @return UML model information bean. If not found, returns <code>null</code>
	 * @see edu.utah.further.dts.api.service.uml.UmlModelService#getModel(java.lang.String)
	 */
	@Override
	public ModelInfo getModel(final String modelName)
	{
		loadIfNotLoadedYet();
		return modelMap.get(modelName);
	}

	/**
	 * Return the list of saved model meta data objects.
	 *
	 * @return model meta data object list
	 * @see edu.utah.further.dts.api.service.uml.UmlModelService#getModelNames(java.lang.String)
	 */
	@Override
	public List<ModelMetaData> getModelMetaDataList()
	{
		loadIfNotLoadedYet();
		final List<ModelMetaData> mdList = newList();
		for (final ModelInfo modelInfo : modelMap.values())
		{
			mdList.add(modelInfo.getModelMetaData());
		}
		return mdList;
	}

	/**
	 * Find an element in all saved models by ID.
	 *
	 * @param elementXmiId
	 *            XMI ID of element
	 * @return element; if not found, returns <code>null</code>
	 * @see edu.utah.further.dts.api.service.uml.UmlModelService#findElementById(java.lang.String)
	 */
	@Override
	public UmlElement findElementById(final String elementXmiId)
	{
		loadIfNotLoadedYet();
		for (final Map.Entry<String, ModelInfo> entry : modelMap.entrySet())
		{
			final UmlElement model = entry.getValue().getModel();
			final UmlElement element = UmlElementFinder.findElementById(model,
					elementXmiId);
			if (element != null)
			{
				return element;
			}
		}
		return null;
	}

	/**
	 * Load a model.
	 *
	 * @param modelMetaData
	 *            model meta data
	 * @return model loading information bean
	 * @see edu.utah.further.dts.api.service.uml.UmlModelService#loadModel(edu.utah.further.core.dts.domain.uml.ModelMetaData)
	 */
	@Override
	public ModelInfo loadModel(final ModelMetaData modelMetaData)
	{
		return loadModel(modelMetaData, xmiParserFactory.newXmiParserOptions());
	}

	/**
	 * Load a UML model.
	 *
	 * @param modelMetaData
	 *            model meta data
	 * @param options
	 *            options to pass to the XMI parser
	 * @return model loading information bean
	 * @see edu.utah.further.dts.api.service.uml.UmlModelService#loadModel(edu.utah.further.core.dts.domain.uml.ModelMetaData,
	 *      edu.utah.further.dts.api.service.uml.XmiParserOptions)
	 */
	@Override
	public ModelInfo loadModel(final ModelMetaData modelMetaData,
			final XmiParserOptions options)
	{
		if (log.isInfoEnabled())
		{
			log.info((options.isDebug() ? "Printing" : "Loading") + " model "
					+ quote(modelMetaData.getName()) + "...");
		}
		final XmiParser parser = xmiParserFactory.newXmiParser(XMI_2_1);
		parser.setOptions(options);
		UmlModel model = null;
		if (modelMetaData.isXmiDirectAccess())
		{
			model = parser.parse(modelMetaData.getQueryResource(), modelMetaData
					.getXmiInputStream());
		}
		else
		{
			model = parser.parse(modelMetaData.getQueryResource(), modelMetaData
					.getXmiResource());
		}
		return new ModelInfo(model, parser.getMessages(), modelMetaData);
	}

	/**
	 * Load a model and save it in the UML model service lookup map.
	 *
	 * @param modelMetaData
	 *            model meta data
	 * @return model loading information bean
	 * @see edu.utah.further.dts.api.service.uml.UmlModelService#loadAndSaveModel(edu.utah.further.core.dts.domain.uml.ModelMetaData)
	 */
	@Override
	public ModelInfo loadAndSaveModel(final ModelMetaData modelMetaData)
	{
		return loadAndSaveModel(modelMetaData, xmiParserFactory.newXmiParserOptions());
	}

	/**
	 * Load a model and save it in the UML model service lookup map. Default XMI parsing
	 * options are used.
	 *
	 * @param modelMetaData
	 *            model meta data options to pass to the XMI parser
	 * @return model loading information bean
	 * @return model loading information bean
	 *@see edu.utah.further.dts.api.service.uml.UmlModelService#loadAndSaveModel(edu.utah.further.core.dts.domain.uml.ModelMetaData,
	 *      edu.utah.further.dts.api.service.uml.XmiParserOptions)
	 */
	@Override
	public ModelInfo loadAndSaveModel(final ModelMetaData modelMetaData,
			final XmiParserOptions options)
	{
		final ModelInfo modelInfo = loadModel(modelMetaData, options);
		final String modelName = modelMetaData.getName();
		if (modelInfo.getModel() != null)
		{
			modelMap.put(modelName, modelInfo);
		}
		if (log.isInfoEnabled())
		{
			log.info("Saved model " + quote(modelMetaData.getName()) + ".");
		}
		return modelInfo;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Load all models if they are not yet cached by {@link UmlModelServiceImpl#modelMap}.
	 */
	private void loadIfNotLoadedYet()
	{
		// TODO: add a cron job to load periodically in addition to this
		// lazy-loading, which is aimed at avoiding Spring loading this bean before
		// AspectJ is initialized for proper DTS session demarcation.
		if (modelMap == null)
		{
			loadAllModels();
		}
	}
}
