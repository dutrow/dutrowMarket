/**
 * 
 */
package dutrow.bidbot.bl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import dutrow.bidbot.bo.BidOrder;
import dutrow.bidbot.dao.JPATestBase;


/**
 * @author dutroda1
 *
 */
public class BusinessLogicTest extends JPATestBase {
	private static Log log = LogFactory.getLog(BusinessLogicTest.class);

	
	
	@Test
	public void testOrderManager(){
		log.info("testOrderManager");
		log.info("createOrder - create a record within bidbot that indicates the sale and maximum bid. This may require some stubbing in project 1.");
		BidOrder bidOrder = testSupport.createOrder();
		Assert.assertNotNull("Test Support created a null bidOrder", bidOrder);
		orderManager.createOrder(bidOrder);
		log.info("placeBid - place a bid that is higher than the current bid for an open auction but less than the order maximum. This will require some stubbing for project1.");
		float newBid = 10f;
		orderManager.placeBid(bidOrder, newBid);
		log.info("endOrder - complete order processing once auction has closed and note if won. This will require some stubbing in project1.");
		boolean ended = orderManager.endOrder();
		//Assert.assertTrue("Order not ended", ended);
		log.info("getOrderStatus - did user win or not.");
		boolean orderStatus = orderManager.getOrderStatus();
		//Assert.assertTrue("Bidder should have won", orderStatus);
	}
	

}
