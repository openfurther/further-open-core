/*******************************************************************************
 * Source File: RunnerXQuery.java
 ******************************************************************************/
package edu.utah.further.ds.test.util;

import static edu.utah.further.core.api.lang.CoreUtil.getResourceAsStreamValidate;
import static edu.utah.further.core.qunit.runner.XmlAssertion.xmlAssertion;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.stream.XMLStreamReader;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.utah.further.core.qunit.runner.AbstractQTestRunner;
import edu.utah.further.core.qunit.runner.OutputFormatterXmlImpl;
import edu.utah.further.core.qunit.runner.QTestContext;
import edu.utah.further.core.qunit.runner.QTestData;
import edu.utah.further.core.qunit.runner.QTestResult;
import edu.utah.further.core.qunit.runner.XmlAssertion;
import edu.utah.further.core.util.io.IoUtil;
import edu.utah.further.core.xml.xquery.XQueryService;
import edu.utah.further.mdr.ws.api.service.rest.AssetServiceRest;

/**
 * A service that runs individual XQuery test cases.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 *
 * @author Oren E. Livne {@code <oren.livne@utah.edu>}
 * @version Aug 27, 2010
 */
@Service("qTestRunnerXQuery")
public final class RunnerXQuery extends AbstractQTestRunner
{
	// ========================= CONSTANTS =================================

	// ========================= DEPENDENCIES ==============================

	/**
	 * Executes XQuery programs.
	 */
	@Autowired
	private XQueryService xqueryService;

	/**
	 * MDR web service client.
	 */
	@Resource(name="mdrAssetServiceRestClient")
	private AssetServiceRest assetServiceRest;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initializes all internal fields for XML test processing.
	 */
	public RunnerXQuery()
	{
		super(new OutputFormatterXmlImpl());
	}

	// ========================= IMPL: AbstractXmlTestRunner ================

	/**
	 * An XQuery program test template (exact match). Both the XQuery output and the
	 * expected output resource are converted to StAX streams and converted. (non-Javadoc)
	 *
	 * @see edu.utah.further.core.qunit.runner.AbstractQTestRunner#runInternal(edu.utah.further.core.qunit.runner.QTestData,
	 *      edu.utah.further.core.qunit.runner.QTestContext)
	 */
	@Override
	protected QTestResult runInternal(final QTestData testData,
			final QTestContext context) throws Throwable
	{
		final InputStream xqueryInputStream = new ByteArrayInputStream(assetServiceRest
				.getActiveResourceContentByPath(testData.getTransformer())
				.getBytes());

		// Make a copy of the XQuery program stream
		final byte[] xqueryCopy = IoUtil.copyInputStream(xqueryInputStream).toByteArray();

		// Run XQuery program
		final String inputResourceName = context.getInputClassPath()
				+ testData.getInput();
		final XMLStreamReader actualResult = xqueryService.executeIntoStream(
				xqueryInputStream, getResourceAsStreamValidate(inputResourceName), testData.getParameters());

		final String expectedResourceName = context.getExpectedClassPath()
				+ testData.getExpected();

		// Validate XQuery output. Do not die on the assertion yet to debug-print below
		final boolean result = xmlAssertion(XmlAssertion.Type.STREAM_MATCH)
				.actualResourceXmlStreamReader(actualResult)
				.expectedResourceName(expectedResourceName)
				.ignoredElements(testData.getIgnoredElements())
				.die(false)
				.doAssert();

		final String actualResourceString = xqueryService.executeIntoString(
				new ByteArrayInputStream(xqueryCopy),
				getResourceAsStreamValidate(inputResourceName), testData.getParameters());

		return new QTestResult(result, actualResourceString);
	}

	// ========================= PRIVATE METHODS ===================================
}
