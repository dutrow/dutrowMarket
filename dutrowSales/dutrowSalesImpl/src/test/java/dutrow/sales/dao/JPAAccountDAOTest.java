/**
 * 
 */
package dutrow.sales.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bo.Account;

/**
 * @author dutroda1
 * 
 */
public class JPAAccountDAOTest extends JPATestBase{
	private static Log log = LogFactory.getLog(JPAAccountDAOTest.class);
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		log.debug("JPAAccountDAOTest::tearDown");
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.dao.jpa.JPAAccountDAO#createAccount(dutrow.sales.bo.Account)}
	 * .
	 */
	@Test
	public void testCreateAccount() {
		log.info("JPAAccountDAOTest::testCreateAccount");
		testSupport.createAndPersistAccountExamples();
	}


	/**
	 * Test method for
	 * {@link dutrow.sales.dao.jpa.JPAAccountDAO#getAccountByUser(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAccountByUser() {
		testSupport.createAndPersistAccountExamples();
		Account dan = accountDao.getAccountByUser("dutrow");
		log.info("Got Account: " + dan); 
		Account jim = accountDao.getAccountByUser("jcs");
		log.info("Got Account: " + jim);
		Account seller = accountDao.getAccountByUser("seller");
		log.info("Got Account: " + seller);
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.dao.jpa.JPAAccountDAO#getAccountByEmail(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAccountByEmail() {
		testSupport.createAndPersistAccountExamples();
		Account dan = accountDao.getAccountByEmail("dan.dutrow@gmail.com");
		log.info("FOUND: " + dan.toString());
		Assert.assertTrue("Mismatched User", dan.getUserId() == "dutrow");
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.dao.jpa.JPAAccountDAO#updateAccount(dutrow.sales.bo.Account)}
	 * .
	 */
	@Test
	public void testUpdateAccount() {
		log.info("JPAAccountDAOTest::testUpdateAccount");
		testSupport.createAndPersistAccountExamples();
		Account dan = accountDao.getAccountByUser("dutrow");
		log.info("Got Account: " + dan);
		dan.setMiddleName("The Man");
		assertTrue(accountDao.updateAccount(dan));
		dan = accountDao.getAccountByUser("dutrow");
		log.info("Updated Account: " + dan);
	}

	/**
	 * Test method for {@link dutrow.sales.dao.jpa.JPAAccountDAO#getAccounts()}.
	 */
	@Test
	public void testGetAccounts() {
		log.info("JPAAccountDAOTest::testGetAccounts");
		testSupport.createAndPersistAccountExamples();
		Collection<Account> accounts = accountDao.getAccounts();
		for(Account a : accounts){
			log.info("Got Account: " + a);
		}
	}
	
	/**
	 * Test method for
	 * {@link dutrow.sales.dao.jpa.JPAAccountDAO#removeAccount(java.lang.String)}.
	 */
	@Test
	public void testRemoveAccount() {
		log.info("JPAAccountDAOTest::testRemoveAccount");
		testSupport.createAndPersistAccountExamples();
		accountDao.removeAccount("dutrow");
		
		assertNull(accountDao.getAccountByUser("dutrow"));
		
		log.info("Remaining Accounts (should not include dutrow):");
		Collection<Account> accounts = accountDao.getAccounts();
		for(Account a : accounts){
			log.info("Got Account: " + a);
		}
	}

	
}
