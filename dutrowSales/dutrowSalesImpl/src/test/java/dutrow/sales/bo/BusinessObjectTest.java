/**
 * 
 */
package dutrow.sales.bo;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bl.TestSupport;
import dutrow.sales.bl.impl.TestSupportImpl;

/**
 * @author dutroda1
 *
 */
public class BusinessObjectTest {
	private static Log log = LogFactory.getLog(BusinessObjectTest.class);
	
	TestSupport testSupport;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.trace("Set up for BusinessObjectTest");
		testSupport = new TestSupportImpl(null, null, null);// shouldn't need DAOs to work
	}
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		log.trace("Tear down for BusinessLogicTest");
	}
	
	@Test
	public void testSellers(){
		log.info("Test Sellers");
		Account a = testSupport.createSellerExample();
		assertNotNull("Seller is null", a);
	}

	@Test
	public void testAccounts(){
		log.info("Test Accounts");
		Account dan = testSupport.createDan();
		assertNotNull("Dan is nulll", dan);
		Account jim = testSupport.createJim();
		assertNotNull("Jim is null", jim);
	}
	
	@Test
	public void createAuctionItems(){
		log.info("Test Auction Items");
		AuctionItem ai = testSupport.createAuctionItem("bronze medal", testSupport.createSellerExample());
		assertNotNull("Auction Item Null", ai);
	}
	
}
