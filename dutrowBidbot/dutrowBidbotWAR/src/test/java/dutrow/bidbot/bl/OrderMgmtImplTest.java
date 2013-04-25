/**
 * 
 */
package dutrow.bidbot.bl;

import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.dao.JPATestBase;

/**
 * @author dutroda1
 * 
 */
public class OrderMgmtImplTest extends JPATestBase {
	private static Log log = LogFactory.getLog(OrderMgmtImplTest.class);

	/**
	 * Test method for
	 * {@link dutrow.bidbot.blimpl.OrderMgmtImpl#createAccount(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateAccount() {
		BidAccount ba1 = orderManager
				.createAccount("dan", "ddutrow", "passwdd");
		BidAccount ba2 = orderManager.getAccount("dan");
		Assert.assertNotNull("Created account null", ba1);
		Assert.assertNotNull("Retrieved account null", ba2);
		Assert.assertEquals(ba1.getSalesAccount(), ba2.getSalesAccount());
		Assert.assertEquals(ba1.getSalesPassword(), ba2.getSalesPassword());
	}

	@Test
	public void testOrderManager() {
		log.info("testOrderManager");
		BidAccount ba = testSupport.createBidder();
		orderManager.createAccount(ba);
		log.info("createOrder - create a record within bidbot that indicates the sale and maximum bid. This may require some stubbing in project 1.");
		BidOrder bidOrder = testSupport.createOrder(ba);
		Assert.assertNotNull("Test Support created a null bidOrder", bidOrder);
		orderManager.createAccount(bidOrder.getBidder());
		orderManager.createOrder(bidOrder);
		log.info("placeBid - place a bid that is higher than the current bid for an open auction but less than the order maximum. This will require some stubbing for project1.");
		float newBid = 10f;
		orderManager.placeBid(bidOrder, newBid);
		log.info("endOrder - complete order processing once auction has closed and note if won. This will require some stubbing in project1.");
		boolean ended = orderManager.endOrder(bidOrder.getBidOrderId());
		// Assert.assertTrue("Order not ended", ended);
		log.info("getOrderStatus - did user win or not.");
		boolean orderStatus = orderManager.getOrderStatus(bidOrder
				.getBidOrderId());
		// Assert.assertTrue("Bidder should have won", orderStatus);
	}
	
	/**
	 * Test method for
	 * {@link dutrow.bidbot.blimpl.OrderMgmtImpl#createOrder(dutrow.bidbot.bo.BidOrder)}
	 * .
	 */
	@Test
	public void testCreateOrder() {
		BidAccount ba = testSupport.createBidder();
		orderManager.createAccount(ba);
		BidOrder bo1 = new BidOrder(3, 4.5f, 7.5f, ba);
		Assert.assertTrue("Create Order", orderManager.createOrder(bo1));
		BidOrder bo2 = orderManager.getOrder(bo1.getBidOrderId());
		Assert.assertNotNull("returned BidOrder is null", bo2);
		Assert.assertEquals("Auction Id Test", bo1.getAuctionId(),
				bo2.getAuctionId());
		Assert.assertEquals("Bidder Sales Account Test", bo1.getBidder()
				.getSalesAccount(), bo2.getBidder().getSalesAccount());
		Assert.assertEquals("Start Bid Test", 0,
				Double.compare(bo1.getStartBid(), bo2.getStartBid()));
		Assert.assertEquals("Result Test", bo1.getResult(), bo2.getResult());
		
	}

	/**
	 * Test method for {@link dutrow.bidbot.blimpl.OrderMgmtImpl#endOrder()}.
	 */
	@Test
	public void testEndOrder() {
		BidAccount ba = testSupport.createBidder();
		orderManager.createAccount(ba);
		BidOrder bo = new BidOrder(3, 4.5f, 7.5f, ba);
		Assert.assertTrue("Create Order", orderManager.createOrder(bo));

		boolean endorder = orderManager.endOrder(bo.getBidOrderId());
		Assert.assertTrue("Order not properly ended.", endorder);
	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.blimpl.OrderMgmtImpl#getOrderStatus()}.
	 */
	@Test
	public void testGetOrderStatus() {
		BidAccount ba = testSupport.createBidder();
		orderManager.createAccount(ba);
		BidOrder bo = testSupport.createOrder(ba);
		orderManager.createAccount(bo.getBidder());
		orderManager.createOrder(bo);
		boolean orderStatus = orderManager.getOrderStatus(bo.getBidOrderId());
		Assert.assertFalse(orderStatus);
	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.blimpl.OrderMgmtImpl#placeBid(dutrow.bidbot.bo.BidOrder, float)}
	 * .
	 */
	@Test
	public void testPlaceBid() {
		BidAccount ba = testSupport.createBidder();
		orderManager.createAccount(ba);
		BidOrder bo = testSupport.createOrder(ba);
		orderManager.createAccount(bo.getBidder());
		orderManager.createOrder(bo);
		Assert.assertTrue(orderManager.placeBid(bo, 5f));
		Assert.assertFalse(orderManager.placeBid(bo, 200f));
	}

}
