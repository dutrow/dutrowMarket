/**
 * 
 */
package dutrow.bidbot.bl;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.dao.JPATestBase;

/**
 * @author dutroda1
 *
 */
public class EndToEndTest extends JPATestBase{
	private static Log log = LogFactory.getLog(EndToEndTest.class);
		
	@Test
	public void clear(){
		log.info("Simple way to clear the database.");
	}
	
	@Test
	public void endToEnd(){
		
		Assert.assertTrue("Reset unsuccessful", testSupport.reset());
		
		/*  Requirements:
		 * 	reset (using BidbotTestUtilImpl) -- reset the bidbot database to an initial starting state.
			place order (using OrderMgmtImpl) -- i.e., create a record within bidbot that indicates the sale and maximum bid. This may require some stubbing in project 1.
			place bid (using OrderMgmtImpl) -- i.e., place a bid that is higher than the current bid for an open auction but less than the order maximum. This will require some stubbing for project1.
			end order (using OrderMgmtImpl) -- i.e., complete order processing once auction has closed and note if won. This will require some stubbing in project1.
			get order status (using OrderMgmtImpl) -- i.e., did user win or not.
		 */
		/* Additional:
		 * createBidAccount
		 */
		
		log.info("testEndToEnd");
		
		Assert.assertTrue("Reset unsuccessful", testSupport.reset());
		
		BidAccount bidder = orderManager.createAccount("bb", "bidder", "pwdBidder");
		Assert.assertNotNull("Account Creation: " + bidder);
		
		//createOrder - create a record within bidbot that indicates the sale and maximum bid. 
		BidOrder bidOrder = testSupport.createOrder(1, bidder);
		orderManager.createOrder(bidOrder);
		//placeBid - place a bid that is higher than the current bid for an open auction but less than the order maximum. This will require some stubbing for project1.
		float newBid = 10f;
		orderManager.placeBid(bidOrder, newBid);
		//endOrder - complete order processing once auction has closed and note if won. This will require some stubbing in project1.
		boolean ended = orderManager.endOrder(bidOrder.getBidOrderId());
		//Assert.assertTrue("Order not ended", ended);
		//getOrderStatus - did user win or not.
		boolean orderStatus = orderManager.getOrderStatus(bidOrder.getBidOrderId());
		//Assert.assertTrue("Bidder should have won", orderStatus);
		
		
	}
	

}
