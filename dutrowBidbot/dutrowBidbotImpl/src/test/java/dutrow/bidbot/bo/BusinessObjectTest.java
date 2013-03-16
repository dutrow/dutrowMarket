/**
 * 
 */
package dutrow.bidbot.bo;

import static org.junit.Assert.assertNotNull;
import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dutrow.bidbot.bl.BidbotTestUtil;
import dutrow.bidbot.bl.BidbotTestUtilImpl;

/**
 * @author dutroda1
 * 
 */
public class BusinessObjectTest {
	private static Log log = LogFactory.getLog(BusinessObjectTest.class);

	BidbotTestUtil testSupport;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.trace("Set up for BusinessObjectTest");
		testSupport = new BidbotTestUtilImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		log.trace("Tear down for BusinessLogicTest");
		Assert.assertTrue("Reset unsuccessful", testSupport.reset());
	}

	@Test
	public void testSellers() {
		log.info("Test Sellers");
		Account a = testSupport.createSeller();
		assertNotNull("Seller is null", a);
	}

	@Test
	public void testAccounts() {
		log.info("Test Accounts");
		Account dan = testSupport.createDan();
		assertNotNull("Dan is nulll", dan);
		Account jim = testSupport.createJim();
		assertNotNull("Jim is null", jim);
	}

	@Test
	public void createAuctionItems() {
		log.info("Test Auction Items");
		AuctionItem ai = testSupport.createAuctionItem("bronze medal",
				testSupport.createSeller());
		assertNotNull("Auction Item Null", ai);
	}

	@Test
	public void createBidAccounts() {
		BidAccount ba = testSupport.createBidAccount();
		Assert.assertNotNull("Could not create bid account", ba);
	}

	@Test
	public void createOrders() {
		Order order = testSupport.createOrder();
		log.info("Create Order: " + order.toString());
	}

}
