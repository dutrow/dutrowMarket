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
	public void a_testCreateAccount() {
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
	public void b_testGetAccountById() {
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
	public void c_testUpdateAccount() {
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
	public void d_testRemoveAccount() {
		log.info("testRemoveAccount");
		BidAccount ba = testSupport.createBidder();
		accountDao.createAccount(ba);
		accountDao.removeAccount(ba.getUserId());
	}

	/**
	 * Test method for {@link dutrow.bidbot.jpa.JPABidAccountDAO#getAccounts()}.
	 */
	@Test
	public void e_testGetAccounts() {
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
	public void f_testCreateOrder() {
		log.info("testCreateOrder");
		BidOrder bo = testSupport.createOrder();
		Assert.assertTrue("Order creation", accountDao.createOrder(bo));
		log.info("Order created: " + bo);
	}

	/**
	 * Test method for {@link dutrow.bidbot.jpa.JPAOrderDAO#getBidOrders()}.
	 */
	@Test
	public void g_testGetBidOrders() {
		log.info("testGetBidOrders");
		BidOrder bo = testSupport.createOrder();
		accountDao.createAccount(bo.getBidder()); // persist through the account
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
	public void h_testGetOrderById() {
		log.info("testGetOrderById");
		BidOrder bo = testSupport.createOrder();
		Assert.assertTrue("Order creation", accountDao.createOrder(bo));
		BidOrder bid = accountDao.getOrderById(bo.getBidOrderId());
		Assert.assertNotNull("Order retrieval", bid);
		log.info("Order retrieved: " + bid);

	}

}
