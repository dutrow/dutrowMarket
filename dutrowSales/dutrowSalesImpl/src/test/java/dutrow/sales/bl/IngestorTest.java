/**
 * 
 */
package dutrow.sales.bl;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bl.impl.Ingestor;
import dutrow.sales.dao.JPATestBase;

/**
 * @author dutroda1
 * 
 */
public class IngestorTest extends JPATestBase {
	private static Log log = LogFactory.getLog(IngestorTest.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.JPATestBase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dutrow.sales.dao.JPATestBase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testIngest1() throws Exception {
		log.info("*** testIngest1 ***");

		String fileName = "xml/eSales-1.xml";
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(fileName);
		assertNotNull(fileName + " not found", is);

		Ingestor ingestor = new Ingestor(is, accountDao, auctionDao);
		ingestor.ingest();

		// verify we have the expected number of accounts
		assertEquals("unexpected number of accounts", 2, accountDao.getAccounts().size());
	}
	

	@Test
	public void testIngest10() throws Exception {
		log.info("*** testIngest10 ***");
		
		String fileName = "xml/eSales-10.xml";
		InputStream is = Thread.currentThread()
		                       .getContextClassLoader()
		                       .getResourceAsStream(fileName);
		assertNotNull(fileName + " not found", is);
		
		Ingestor ingestor = new Ingestor(is, accountDao, auctionDao);
		ingestor.ingest();
		
		//verify we have the expected number of accounts		
		assertEquals("unexpected number of accounts",19,
				accountManager.getAccounts(0, 1000).size());
	}

	@Test
	public void testIngest100() throws Exception {
		log.info("*** testIngest10 ***");
		
		String fileName = "xml/eSales-100.xml";
		InputStream is = Thread.currentThread()
		                       .getContextClassLoader()
		                       .getResourceAsStream(fileName);
		assertNotNull(fileName + " not found", is);
		
		Ingestor ingestor = new Ingestor(is, accountDao, auctionDao);
		ingestor.ingest();
		
		//verify we have the expected number of accounts		
		assertEquals("unexpected number of accounts",209,
				accountManager.getAccounts(0, 1000).size());
	}

	@Test
	public void testIngestAll() throws Exception {
		log.info("*** testIngestAll ***");
		
		String fileName = "xml/eSales-all.xml";
		InputStream is = Thread.currentThread()
		                       .getContextClassLoader()
		                       .getResourceAsStream(fileName);
		assertNotNull(fileName + " not found", is);
		
		Ingestor ingestor = new Ingestor(is, accountDao, auctionDao);
		ingestor.ingest();
		
		
		//verify we have the expected number of accounts		
		assertEquals("unexpected number of accounts",841,
				accountManager.getAccounts(0, 1000).size());
	}

}
