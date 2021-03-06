/**
 * 
 */
package dutrow.bidbot.dao;

import java.util.Collection;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import dutrow.bidbot.bo.BidAccount;
import dutrow.bidbot.bo.BidOrder;

/**
 * @author dutroda1
 * 
 */
public class JPABidAccountDAOTest extends JPATestBase {
	private static Log log = LogFactory.getLog(JPABidAccountDAOTest.class);

	/**
	 * Test method for
	 * {@link dutrow.bidbot.jpa.JPABidAccountDAO#createAccount(dutrow.bidbot.bo.BidAccount)}
	 * .
	 */
	@Test
	public void testCreateAccount() {
		log.info("testCreateAccount");
		BidAccount ba = testSupport.createBidder();
		accountDao.createAccount(ba);
	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.jpa.JPABidAccountDAO#getAccountById(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAccountById() {
		log.info("testGetAccountById");
		BidAccount ba = testSupport.createBidder();
		accountDao.createAccount(ba);
		accountDao.getAccountById(ba.getUserId());
	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.jpa.JPABidAccountDAO#updateAccount(dutrow.bidbot.bo.BidAccount)}
	 * .
	 */
	@Test
	public void testUpdateAccount() {
		log.info("testUpdateAccount");
		BidAccount ba = testSupport.createBidder();
		accountDao.createAccount(ba);
		ba.setSalesPassword("P@$$WR&");
		accountDao.updateAccount(ba);
	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.jpa.JPABidAccountDAO#removeAccount(java.lang.String)}
	 * .
	 */
	@Test
	public void testRemoveAccount() {
		log.info("testRemoveAccount");
		BidAccount ba = testSupport.createBidder();
		accountDao.createAccount(ba);
		accountDao.removeAccount(ba.getUserId());
	}

	/**
	 * Test method for {@link dutrow.bidbot.jpa.JPABidAccountDAO#getAccounts()}.
	 */
	@Test
	public void testGetAccounts() {
		log.info("testGetAccounts");
		BidAccount ba = testSupport.createBidder();
		accountDao.createAccount(ba);
		Collection<BidAccount> accounts = accountDao.getAccounts();

		log.info("Get Accounts: ");
		for (BidAccount acct : accounts) {
			log.info(acct);
		}
	}

	/**
	 * Test method for
	 * {@link dutrow.bidbot.jpa.JPAOrderDAO#createOrder(dutrow.bidbot.bo.BidOrder)}
	 * .
	 */
	@Test
	public void testCreateOrder() {
		log.info("testCreateOrder");
		BidAccount bidder = testSupport.createBidder();
		accountDao.createAccount(bidder);
		BidOrder bo = testSupport.createOrder(1, bidder);
		Assert.assertTrue("Order creation", accountDao.createOrder(bo));
		log.info("Order created: " + bo);
	}

	/**
	 * Test method for {@link dutrow.bidbot.jpa.JPAOrderDAO#getBidOrders()}.
	 */
	@Test
	public void testGetBidOrders() {
		log.info("testGetBidOrders");
		BidAccount bidder = testSupport.createBidder();
		accountDao.createAccount(bidder);
		BidOrder bo = testSupport.createOrder(1, bidder);
		accountDao.createOrder(bo);
		Collection<BidOrder> orders = accountDao.getBidOrders();

		log.info("Get Orders: ");
		for (BidOrder o : orders) {
			log.info(o);
		}

	}

	/**
	 * Test method for {@link dutrow.bidbot.jpa.JPAOrderDAO#getOrderById(long)}.
	 */
	@Test
	public void testGetOrderById() {
		log.info("testGetOrderById");
		BidAccount bidder = testSupport.createBidder();
		accountDao.createAccount(bidder);
		BidOrder bo = testSupport.createOrder(1, bidder);
		Assert.assertTrue("Order creation", accountDao.createOrder(bo));
		BidOrder bid = accountDao.getOrderById(bo.getBidOrderId());
		Assert.assertNotNull("Order retrieval", bid);
		log.info("Order retrieved: " + bid);

	}

}
