/**
 * 
 */
package dutrow.sales.ejbclient;

import static org.junit.Assert.assertNotNull;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import dutrow.sales.bl.AccountMgmtException;
import dutrow.sales.dto.AccountDTO;
import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.TestSupportRemote;

/**
 * @author dutroda1
 * 
 */
public class AccountMgmtIT extends Support {
	private static final Log log = LogFactory.getLog(AccountMgmtIT.class);

	private static final String registrarJNDI = System
			.getProperty(
					"jndi.name.registrar",
					"dutrowSalesEAR/dutrowSalesEJB/AccountMgmtEJB!dutrow.sales.ejb.AccountMgmtRemote");
	private AccountMgmtRemote accountManager;
	

	AccountDTO dan;

	
	public void configureJndi(){
		assertNotNull("jndi.name.registrar not supplied", registrarJNDI);

		log.debug("jndi name:" + registrarJNDI);
		try {
			accountManager = (AccountMgmtRemote) jndi.lookup(registrarJNDI);
		} catch (NamingException ne) {
			log.warn(ne.getMessage());
			log.warn(ne.getExplanation());
		}
		log.debug("buyerManager=" + accountManager);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		log.debug("Set up for AccountMgmtIT");

		configureJndi();

		dan = new AccountDTO("dutrow", "Dan", "Allan", "Dutrow",
				"dan.dutrow@gmail.com");
		accountManager.createAccountDTO(dan);

	}

	@Test
	public void testCreateAccount() {
		log.info("testCreateAccount");
		AccountDTO jim = new AccountDTO("jcs", "Jim", "C", "Stafford",
				"jcstaff@gmail.com");
		try {
			accountManager.createAccountDTO(jim);
		} catch (AccountMgmtException e) {
			log.warn(e.getMessage());
		}
	}

	@Test
	public void testGetAccount() {
		log.info("testGetAccount");
		AccountDTO twin = null;
		try {
			twin = accountManager.getAccountDTO(dan.userId);
		} catch (AccountMgmtException e) {
			log.warn(e.getMessage());
		}
		Assert.assertNotNull("Account not found", twin);
		log.info("dan: " + dan.toString());
		log.info("twin:" + twin.toString());
		Assert.assertEquals("Accounts not equal", dan.toString(),
				twin.toString());
	}

	@Test
	public void testUpdateAccount() {
		log.info("testUpdateAccount");
		AccountDTO retrieved = null;
		try {
			retrieved = accountManager.getAccountDTO("dutrow");
			log.info("retrieved: " + retrieved.toString());
		} catch (AccountMgmtException e) {
			log.warn(e.getMessage());
		}
		Assert.assertNotNull("Could not retrieve account", retrieved);
		if (retrieved != null)
			retrieved.middleName = "Allan Lundahl";
		log.info("testAccountManager::updateAccount");
		try {
			Assert.assertTrue("Could not update account",
					accountManager.updateAccountDTO(retrieved));
		} catch (AccountMgmtException e) {
			log.warn(e.getMessage());
		}

	}

	@Test
	public void testCloseAccount() {
		log.info("testCloseAccount");
		AccountDTO jim = new AccountDTO("jcs", "Jim", "C", "Stafford",
				"jcstaff@gmail.com");
		try {
			accountManager.createAccountDTO(jim);
		} catch (AccountMgmtException e) {
			log.warn(e.getMessage());
		}
		log.info("testAccountManager::closeAccount");
		try {
			Assert.assertTrue("Could not close account",
					accountManager.closeAccountDTO(jim.userId));
		} catch (AccountMgmtException e) {
			log.warn(e.getMessage());
		}
	}

}
