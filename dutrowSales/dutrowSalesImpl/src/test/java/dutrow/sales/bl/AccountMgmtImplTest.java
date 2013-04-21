/**
 * 
 */
package dutrow.sales.bl;

import java.util.Collection;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bo.Account;
import dutrow.sales.dao.JPATestBase;

/**
 * @author dutroda1
 * 
 */
public class AccountMgmtImplTest extends JPATestBase {
	private static Log log = LogFactory.getLog(AccountMgmtImplTest.class);

	private Account dan;
	
	
	@Before
	public void setUp() throws Exception {
		log.trace("Set up for AccountMgmtImplTest");
		super.setUp();
		dan = testSupport.createDan();
		accountManager.createAccount(dan);
	}
	
	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.AccountMgmtImpl#createAccount(dutrow.sales.bo.Account)}
	 * .
	 */
	@Test
	public void testCreateAccount() {
		log.info("testCreateAccount");
		Assert.assertTrue("Entity does not contain new account", em.contains(dan));
		
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.AccountMgmtImpl#getAccount(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAccount() {
		log.info("testGetAccount");
		Account twin = accountManager.getAccount(dan.getUserId());
		Assert.assertNotNull("Account not found", twin);
		Assert.assertEquals("Accounts not equal", dan.toString(), twin.toString());
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.AccountMgmtImpl#updateAccount(dutrow.sales.bo.Account)}
	 * .
	 */
	@Test
	public void testUpdateAccount() {
		log.info("testUpdateAccount");
		Account retrieved = accountManager.getAccount("dutrow");
		Assert.assertNotNull("Could not retrieve account", retrieved);
		String middleName = retrieved.getMiddleName();
		
		if (retrieved != null)
			retrieved.setMiddleName("Allan Lundahl");
		log.info("testAccountManager::updateAccount");
		Assert.assertTrue("Could not update account", accountManager.updateAccount(retrieved));
		
		Account updated = accountManager.getAccount("dutrow");
		Assert.assertNotSame("Update unsuccessful", middleName, updated.getMiddleName());
		
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.AccountMgmtImpl#closeAccount(java.lang.String)}
	 * .
	 */
	@Test
	public void testCloseAccount() {
		log.info("testCloseAccount");
		Account jim;
		jim = testSupport.createJim();
		accountDao.createAccount(jim);
		log.info("testAccountManager::closeAccount");
		Assert.assertTrue("Could not close account", accountManager.closeAccount(jim.getUserId()));
	}

	/**
	 * Test method for
	 * {@link dutrow.sales.bl.impl.AccountMgmtImpl#getAccounts(int, int)}.
	 */
	@Test
	public void testGetAccounts() {
		log.info("testGetAccounts");
		Collection<Account> accounts = testSupport.getAccounts();
		Assert.assertNotNull("Get Accounts Returned null", accounts);
		Assert.assertNotSame("Expected not to have zero accounts", 0, accounts.size());
		for (Account a : accounts){
			log.info("Found account: " + a.toString() );
		}
	}

}
