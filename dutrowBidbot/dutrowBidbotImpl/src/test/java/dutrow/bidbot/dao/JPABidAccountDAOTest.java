/**
 * 
 */
package dutrow.bidbot.dao;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import dutrow.bidbot.bo.BidAccount;

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

}
