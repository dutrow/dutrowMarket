/**
 * 
 */
package dutrow.bidbot.bo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * @author dutroda1
 * 
 */
public class BidAccountTest {
	private static Log log = LogFactory.getLog(BidAccountTest.class);

	public BidAccount createBidder() {
		BidAccount acct = new BidAccount();
		acct.setUserId("bidbotSellerAcct");
		acct.setSalesAccount("bidder");
		acct.setSalesPassword("bidderPwd");

		return acct;
	}

	/**
	 * Test method for {@link dutrow.bidbot.bo.BidAccount#getUserId()}.
	 */
	@Test
	public void testGetUserId() {
		BidAccount a = createBidder();
		log.info(a.getUserId());
	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.bo.BidAccount#setUserId(java.lang.String)}.
	 */
	@Test
	public void testSetUserId() {
		BidAccount a = createBidder();
		log.info(a);
		a.setUserId("update");
		log.info(a);
	}

	/**
	 * Test method for {@link dutrow.bidbot.bo.BidAccount#getSalesAccount()}.
	 */
	@Test
	public void testGetSalesAccount() {
		BidAccount a = createBidder();
		log.info(a.getSalesAccount());
	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.bo.BidAccount#setSalesAccount(java.lang.String)}.
	 */
	@Test
	public void testSetSalesAccount() {
		BidAccount a = createBidder();
		log.info(a);
		a.setSalesAccount("saUpdate");
		log.info(a);
	}

	/**
	 * Test method for {@link dutrow.bidbot.bo.BidAccount#getSalesPassword()}.
	 */
	@Test
	public void testGetSalesPassword() {
		BidAccount a = createBidder();
		log.info(a.getSalesPassword());
	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.bo.BidAccount#setSalesPassword(java.lang.String)}.
	 */
	@Test
	public void testSetSalesPassword() {
		BidAccount a = createBidder();
		log.info(a);
		a.setSalesAccount("saUpdate");
		log.info(a);
	}

	/**
	 * Test method for {@link dutrow.bidbot.bo.BidAccount#getOrders()}.
	 */
	@Test
	public void testGetOrders() {
		BidAccount a = createBidder();
		List<BidOrder> orders = a.getOrders();
		if (orders != null)
			for (BidOrder o : orders)
				log.info(o);

	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.bo.BidAccount#setOrders(java.util.List)}.
	 */
	@Test
	public void testSetOrders() {
		BidAccount a = createBidder();
		log.info(a);
		a.setSalesAccount("saUpdate");
		log.info(a);
	}

}
