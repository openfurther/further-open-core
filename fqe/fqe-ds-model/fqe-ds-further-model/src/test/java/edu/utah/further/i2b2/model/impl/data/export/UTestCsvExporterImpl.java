/*******************************************************************************
 * Source File: UTestCsvExporterImpl.java
 ******************************************************************************/
package edu.utah.further.i2b2.model.impl.data.export;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import edu.utah.further.core.data.util.SqlUtil;
import edu.utah.further.dts.api.domain.concept.DtsConcept;
import edu.utah.further.dts.api.domain.namespace.DtsNamespace;
import edu.utah.further.dts.api.service.DtsOperationService;
import edu.utah.further.dts.api.to.DtsConceptToImpl;
import edu.utah.further.dts.api.to.DtsNamespaceToImpl;
import edu.utah.further.fqe.ds.api.domain.ExportContext;
import edu.utah.further.fqe.ds.api.domain.Exporter;
import edu.utah.further.fqe.ds.api.domain.QueryContext;
import edu.utah.further.fqe.ds.api.service.results.ResultDataService;
import edu.utah.further.fqe.ds.api.to.QueryContextToImpl;
import edu.utah.further.fqe.ds.model.further.export.DemographicExportAttribute;
import edu.utah.further.fqe.mpi.impl.domain.IdentifierEntity;
import edu.utah.further.ds.i2b2.model.api.to.PatientDimensionTo;
import edu.utah.further.ds.i2b2.model.api.to.PatientDimensionsTo;
import edu.utah.further.ds.i2b2.model.impl.domain.PatientDimensionEntity;
import edu.utah.further.i2b2.model.impl.fixture.I2b2ModelImplFixture;
import edu.utah.further.ds.i2b2.model.impl.to.PatientDimensionsToImpl;

/**
 * Tests exporting data in a comma separated value format.
 * <p>
 * -----------------------------------------------------------------------------------<br>
 * (c) 2008-2012 FURTHeR Project, Health Sciences IT, University of Utah<br>
 * Contact: {@code <further@utah.edu>}<br>
 * Biomedical Informatics, 26 South 2000 East<br>
 * Room 5775 HSEB, Salt Lake City, UT 84112<br>
 * Day Phone: 1-801-581-4080<br>
 * -----------------------------------------------------------------------------------
 * 
 * @author N. Dustin Schultz {@code <dustin.schultz@utah.edu>}
 * @version Oct 8, 2012
 */
@TransactionConfiguration(transactionManager="transactionManagerMain", defaultRollback=true)
@Transactional
public class UTestCsvExporterImpl extends I2b2ModelImplFixture
{
	/**
	 * The exporter that exports the data to a CSV
	 */
	@Autowired
	private Exporter csvExporter;

	/**
	 * ResultService for getting the results to export
	 */
	@Autowired
	private ResultDataService resultDataService;

	@Autowired
	private DtsOperationService dos;

	/**
	 * Test query 1
	 */
	private QueryContext queryContextOne;

	/**
	 * Setup method for populating patient data to export
	 * 
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Before
	public void setup() throws JAXBException, IOException
	{
		final PatientDimensionsTo patientDimensionsTo = xmlService.unmarshalResource(
				"patient-dimensions-export.xml", PatientDimensionsToImpl.class);
		
		for (final PatientDimensionTo patientDimensionTo : patientDimensionsTo.getPatientDimension())
		{
			// Insert patient; if encountered multiple times, override (although this
			// shouldn't happen in this test)
			final PatientDimensionEntity patient = (PatientDimensionEntity) patientDimensionTo
					.copy();
			dao.update(patient);

			// Insert patient link entry
//			final LinkEntity link = new LinkEntity();
//			link.setQueryId(patientDimensionTo.getPatientDimensionPK().getQueryId());
//			link.setMasterPersonId(patient.getId());
//			dao.save(link);
			// from setup() in UTestIdentityResolutionLookupTable:
			final QueryContext parent = QueryContextToImpl.newInstance(1);

			final QueryContext queryContext1 = QueryContextToImpl
					.newInstanceWithExecutionId();
			queryContext1.setTargetNamespaceId(new Long(1));
			queryContext1.setParent(parent);

			this.queryContextOne = queryContext1;
			
			final IdentifierEntity idOne = new IdentifierEntity();
			idOne.setSourceId(patientDimensionTo.getPatientDimensionPK().getQueryId());
			idOne.setQueryId(queryContext1.getExecutionId());
			idOne.setVirtualId(new Long(patient.getId()));

			dao.save(idOne);
		}

		expect(
				dos.findConceptByCodeInSource(EasyMock.<DtsNamespace> anyObject(),
						EasyMock.<String> anyObject())).andStubAnswer(
				new IAnswer<DtsConcept>()
				{
					/*
					 * (non-Javadoc)
					 * 
					 * @see org.easymock.IAnswer#answer()
					 */
					@Override
					public DtsConcept answer() throws Throwable
					{
						DtsConcept concept = new DtsConceptToImpl();
						final String code = (String) EasyMock.getCurrentArguments()[1];
						if ("248152002".equals(code))
						{
							concept.setName("Female");
						}
						else if ("248153007".equals(code))
						{
							concept.setName("Male");
						}
						else if ("33553000".equals(code))
						{
							concept.setName("Widowed");
						}
						else
						{
							concept = null;
						}

						return concept;
					}
				});

		expect(dos.findNamespaceById(anyInt())).andStubAnswer(new IAnswer<DtsNamespace>()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.easymock.IAnswer#answer()
			 */
			@Override
			public DtsNamespace answer() throws Throwable
			{
				final DtsNamespace dtsNamespace = new DtsNamespaceToImpl();
				dtsNamespace.setId(1);
				dtsNamespace.setName("Awesome");
				return dtsNamespace;
			}

		});
		replay(dos);
	}

	/**
	 * Delete all patients from the database.
	 */
	@After
	public void tearDown()
	{
		dao.deleteAll(PatientDimensionEntity.class);
		dao.deleteAll(IdentifierEntity.class);
	}

	/**
	 * Test exporting the test data to a CSV.
	 */
	@Test
	@SuppressWarnings("boxing")
	public void exportToCsv()
	{
		final List<PatientDimensionEntity> results = resultDataService
				.getQueryResults("from " + PatientDimensionEntity.class.getName(), new ArrayList<Object>());

		final String data = csvExporter.format(results,
				EasyMock.createMock(ExportContext.class));
		
		for (final DemographicExportAttribute attribute : DemographicExportAttribute
				.values())
		{
			if (attribute.isIgnored())
			{
				assertThat(data, not(containsString("," + attribute.getDisplayName() + ",")));
			}
			else
			{
				assertThat(data, containsString("," + attribute.getDisplayName() + ","));
				if (attribute.isValueCoded())
				{
					assertThat(data, containsString("," + attribute.getDisplayName() + " CODE"));
				}
			}

		}

		// Split up everything by string
		final Multiset<String> strings = HashMultiset.create(Arrays.asList(data
				.split(",")));

		// If everything was comma-separated correctly then we expect these values
		assertThat(strings.count("Female"), is(3));
		assertThat(strings.count("Male"), is(2));
		assertThat(strings.count("Widowed"), is(5));
		assertThat(strings.count("1933"), is(1));

		// Check that values actually make it to the export data
		assertThat(strings.count("SNOMED_33553000"), is(5));
		assertThat(strings.count("SNOMED_248152002"), is(3));

		// Check that codes that aren't found in DTS are displayed as NOT_FOUND
		assertThat(strings.count("NOT_FOUND"), is(4));
	}
}
