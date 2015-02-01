/*******************************************************************************
 * Source File: DataSourceQueryTranslatorFixture.java
 ******************************************************************************/
package edu.utah.further.ds.test.util;

import java.util.List;

import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import edu.utah.further.core.api.data.Dao;
import edu.utah.further.core.qunit.runner.QTestContext;
import edu.utah.further.core.qunit.runner.QTestFixture;
import edu.utah.further.core.qunit.runner.QTestRunner;
import edu.utah.further.fqe.impl.domain.QueryContextEntity;

/**
 * A common fixture of query translation tests. Runs an XML test suite against the Oracle
 * stored procedure.
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
@ContextConfiguration(locations =
{ "/META-INF/ds/test/ds-test-mdr-ws-client-context.xml",
		"/META-INF/ds/impl/test/ds-impl-test-context.xml",
		"/META-INF/ds/impl/test/ds-impl-test-query-translator-datasource.xml",
		"/META-INF/ds/impl/test/ds-impl-test-query-translator-context.xml",
		"/META-INF/ds/impl/test/ds-impl-test-services-context.xml"})
public abstract class DataSourceQueryTranslatorFixture extends QTestFixture
{
	// ========================= CONSTANTS ==============================

	/**
	 * Database column name of user ID of created queries.
	 */
	private static final String SERVICE_USER_ID_COLUMN = "userId";

	// ========================= DEPENDENCIES ===========================

	/**
	 * Test running service.
	 */
	@Autowired
	@Qualifier("qTestRunnerQueryTranslator")
	private QTestRunner qTestRunner;

	/**
	 * Dao for database clean up.
	 */
	@Autowired
	@Qualifier("dao")
	private Dao dao;

	/**
	 * For controlling transactions.
	 */
	@Autowired
	@Qualifier("translatorTransactionTemplate")
	private TransactionTemplate transactionTemplate;

	/**
	 * JDBC Template for sql querying
	 */
	@Autowired
	@Qualifier("virtualRepoJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	// ========================= IMPL: QTestRunner =========================

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.utah.further.ds.test.util.XmlTestFixture#getXmlTestRunner()
	 */
	@Override
	public QTestRunner getQTestRunner()
	{
		return qTestRunner;
	}

	// ========================= SETUP METHODS ==========================

	/**
	 * Delete anything we used to dirty the database.
	 */
	@After
	public void cleanUp()
	{
		final QTestContext context = getQTestSuite().getContext();

		/*
		 * Since this test class uses a transaction template for fine grained control, we
		 * can't have one long transaction for this class using the
		 * 
		 * @Transactional annotation. The transaction will already be closed when it
		 * reaches the @After method, thus we need to execute the cleanup using a
		 * transaction template as well.
		 */
		transactionTemplate.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(
					final TransactionStatus transactionStatus)
			{
				// Retrieve all logical test queries created by the service user ID
				final List<QueryContextEntity> queryEntities = dao.findByProperty(
						QueryContextEntity.class, SERVICE_USER_ID_COLUMN,
						context.getServiceUserId());

				// Delete these queries and their physical query counterparts
				for (final QueryContextEntity qce : queryEntities)
				{
					dao.delete(qce);
				}

				jdbcTemplate.update("DELETE FROM query_def where created_by_user_id = ?",
						context.getServiceUserId());
			}
		});

	}

	// ========================= METHODS ================================

	// ========================= PRIVATE METHODS ========================
}
