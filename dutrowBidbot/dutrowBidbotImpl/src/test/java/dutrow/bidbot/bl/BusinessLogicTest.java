/**
 * 
 */
package dutrow.bidbot.bl;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dutrow.bidbot.blimpl.OrderMgmtImpl;
import dutrow.bidbot.bo.BidOrder;


/**
 * @author dutroda1
 *
 */
public class BusinessLogicTest {
	private static Log log = LogFactory.getLog(BusinessLogicTest.class);
	
	OrderMgmt orderManager;
	BidbotTestUtil testSupport;
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.trace("Set up for BusinessLogicTest");
		orderManager = new OrderMgmtImpl();
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
	public void testOrderManager(){
		log.info("testOrderManager");
		//createOrder - create a record within bidbot that indicates the sale and maximum bid. This may require some stubbing in project 1.\
		BidOrder bidOrder = testSupport.createOrder();
		orderManager.createOrder(bidOrder);
		//placeBid - place a bid that is higher than the current bid for an open auction but less than the order maximum. This will require some stubbing for project1.
		float newBid = 10f;
		orderManager.placeBid(bidOrder, newBid);
		//endOrder - complete order processing once auction has closed and note if won. This will require some stubbing in project1.
		boolean ended = orderManager.endOrder();
		//Assert.assertTrue("Order not ended", ended);
		//getOrderStatus - did user win or not.
		boolean orderStatus = orderManager.getOrderStatus();
		//Assert.assertTrue("Bidder should have won", orderStatus);
	}
	

}
